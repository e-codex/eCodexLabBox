package eu.ecodex.labbox.gw.setup;

import com.beust.jcommander.JCommander;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class PModeUpload {

    private static final Logger LOGGER = LogManager.getLogger(PModeUpload.class);

    byte[] pModeXml;

    String gatewayUrl;

    String username = "admin";

    String password = "123456";

    int maxRetries = 1;

    int retryWait = 1000;

    public byte[] getpModeXml() {
        return pModeXml;
    }

    public PModeUpload setpModeXml(byte[] pModeXml) {
        this.pModeXml = pModeXml;
        return this;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public PModeUpload setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public PModeUpload setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public PModeUpload setPassword(String password) {
        this.password = password;
        return this;
    }


    public int getMaxRetries() {
        return maxRetries;
    }

    public PModeUpload setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    public int getRetryWait() {
        return retryWait;
    }

    public PModeUpload setRetryWait(int retryWait) {
        this.retryWait = retryWait;
        return this;
    }

    public void uploadPModes() {
        String url = this.gatewayUrl;
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        try {

            BasicCookieStore cookieStore = new BasicCookieStore();

            RequestAddCookies addCookies = new RequestAddCookies();

            HttpClientContext context = HttpClientContext.create();
            CloseableHttpClient client = HttpClientBuilder.create()
                    .setDefaultCookieStore(cookieStore)
                    .addInterceptorLast(addCookies)
                    .setRetryHandler(new HttpRequestRetryHandler() {
                        @Override
                        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                            try {
                                Thread.sleep(retryWait);
                            } catch (InterruptedException ex) {

                            }
                            return executionCount < maxRetries;
                        }
                    })
                    .build();

//            context.setCookieStore(cookieStore);


//            JSONObject jsonObject = new JSONObject();
//            jsonObject.append("username", username);
//            jsonObject.append("password", password);

            StringEntity authEntity = new StringEntity(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", this.username, this.password));
//            LOGGER.info("authEntity {}", jsonObject.toString());


            String authUrl = url + "/rest/security/authentication";
            LOGGER.debug("post for auth on url: " + authUrl);
            HttpPost postAuth = new HttpPost(authUrl);
            postAuth.addHeader("Content-Type", "application/json");

            postAuth.setEntity(authEntity);

            HttpResponse authResponse = client.execute(postAuth);


            LOGGER.info("Statuscode from login was {}", authResponse.getStatusLine().getStatusCode());
            if (authResponse.getStatusLine().getStatusCode() == 200) {
                //SUPER
            } else {
                LOGGER.error("Statuscode is not 200\n {}", authResponse.getStatusLine());

                throw new RuntimeException("error on login");
            }
            LOGGER.trace("Cookies are: {}", cookieStore.getCookies());

            ByteArrayBody byteArrayBody = new ByteArrayBody(this.pModeXml, ContentType.APPLICATION_XML, "pmode.xml");
           
            MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create()
//                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
//                    .addPart("description", new StringBody("pmode.xml", ContentType.TEXT_XML))
                    .addTextBody("file", new String(this.pModeXml, "UTF-8"), ContentType.getByMimeType(MimeTypeUtils.APPLICATION_XML_VALUE));
//                    .addBinaryBody("file", this.pModeXml, ContentType.getByMimeType(MimeTypeUtils.APPLICATION_XML_VALUE), "pmode.xml");
            
//                    .addPart("file", byteArrayBody);


            multipartBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);


            String value = cookieStore.getCookies().stream()
                    .filter((Cookie cookie) -> {
                        return "XSRF-TOKEN".equals(cookie.getName());
                    })
                    .map(Cookie::getValue)
                    .findFirst().get();


            HttpEntity httpUploadEntity = multipartBuilder.build();

            HttpUriRequest uploadPost = RequestBuilder
                    .post(url + "/rest/pmode")
                    .addHeader("X-XSRF-TOKEN", value)
                    .addParameter("description", "Upload from pmodeuploader")
                    .addHeader(httpUploadEntity.getContentType())
                    .setEntity(httpUploadEntity)
                    .build();

            LOGGER.info("execute post {} with headers {}", uploadPost, uploadPost.getAllHeaders());

            HttpResponse uploadResponse = client.execute(uploadPost); //, context);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            uploadResponse.getEntity().writeTo(byteArrayOutputStream);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Statuscode from upload was {} with message {}, response body was {}",
                        uploadResponse.getStatusLine().getStatusCode(),
                        uploadResponse.getStatusLine(),
                        byteArrayOutputStream.toString("UTF-8"));
            } else {
                LOGGER.info("P-Modes uploaded with status code {}", uploadResponse.getStatusLine().getStatusCode());
            }

        } catch (IOException ioe) {
            LOGGER.error("P-Mode Upload failed - IOException occured", ioe);
        }
    }



    public static void main(String... progArgs) {
        Arguments arguments = new Arguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(progArgs);

        byte[] file = loadAsByteArray(arguments.getpModeFile());


        PModeUpload upload = new PModeUpload();
        upload.setPassword(arguments.getPassword())
                .setGatewayUrl(arguments.getUrl())
                .setUsername(arguments.getUsername())
                .setRetryWait(arguments.getWaitBetweenRetries())
                .setMaxRetries(arguments.getMaxRetries())
                .setpModeXml(file);

        upload.uploadPModes();

    }

    private static byte[] loadAsByteArray(String getpModeFile) {
        Path pModeFile = Paths.get(getpModeFile);

        try (FileInputStream is = new FileInputStream(pModeFile.toFile())) {
            return StreamUtils.copyToByteArray(is);
        } catch (FileNotFoundException fnfe) {
            throw new IllegalArgumentException(String.format("P-Mode File with path [%s] does not exist!", pModeFile), fnfe);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Error loading file from path [%s]!", pModeFile), e);
        }

    }


}

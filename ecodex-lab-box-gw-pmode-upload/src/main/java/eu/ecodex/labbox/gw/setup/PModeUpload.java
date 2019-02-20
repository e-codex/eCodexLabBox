package eu.ecodex.labbox.gw.setup;

import static org.toilelibre.libe.curl.Curl.curl;
import com.beust.jcommander.JCommander;


public class PModeUpload {

    public static void main(String... progArgs) {
        Arguments arguments = new Arguments();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(progArgs);




        curl("curl " + arguments.getUrl() + " /rest/security/authentication " +
                "-i " +
                "-H \"Content-Type: application/json\" " +
                "-X POST -d '{\"username\":" + arguments.getUsername() + "," + arguments.getPassword() + " :\"123456\"}' " +
                "-c cookie.txt");

//
//: ${2?"Usage: $0 ENDPOINT PMODE_FILE_PATH"}
//
//        ENDPOINT=$1
//        PMODE_FILE_PATH=$2
//
//        echo $ENDPOINT
//        echo $PMODE_FILE_PATH
//
//        curl http://$ENDPOINT/domibus/rest/security/authentication \
//        -i \
//        -H "Content-Type: application/json" \
//        -X POST -d '{"username":"admin","password":"123456"}' \
//        -c cookie.txt
//
//
//                JSESSIONID=`grep JSESSIONID cookie.txt |  cut -d$'\t' -f 7`
//        XSRFTOKEN=`grep XSRF-TOKEN cookie.txt |  cut -d$'\t' -f 7`
//
//        echo; echo
//        echo JSESSIONID=${JSESSIONID}
//        echo XSRFTOKEN=${XSRFTOKEN}
//        echo
//        echo  "X-XSRF-TOKEN: ${XSRFTOKEN}"
//
//        echo ; echo "UPLOADING PMODE ===================================" ; echo
//
//        curl http://$ENDPOINT/domibus/rest/pmode \
//        -b cookie.txt \
//        -v \
//        -H "X-XSRF-TOKEN: ${XSRFTOKEN}" \
//        -F  file=@$PMODE_FILE_PATH




    }


}

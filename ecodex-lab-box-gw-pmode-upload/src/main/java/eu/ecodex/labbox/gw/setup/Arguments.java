package eu.ecodex.labbox.gw.setup;

import com.beust.jcommander.Parameter;

public class Arguments {

    @Parameter(names =  {"-u", "-user"}, description = "Domibus username")
    private String username = "admin";

    @Parameter(names = {"-p", "-password"}, description = "Domibus password")
    private String password = "123456";

    @Parameter(names = {"-pmode"}, required = true, description = "p-mode file")
    private String pModeFile;

    @Parameter(names = {"-url"}, required = true, description = "url to domibus example: [http://localhost:8080/domibus]")
    private String url;

    @Parameter(names = {"-r", "-retry"}, required = false, description = "how often should the upload be retried in case of connection errors")
    private int maxRetries = 1;

    @Parameter(names = {"-ms", "-retrywait"}, required = false, description = "how long should be wait in milliseconds until a connection can be established?")
    private int waitBetweenRetries = 1000;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getpModeFile() {
        return pModeFile;
    }

    public void setpModeFile(String pModeFile) {
        this.pModeFile = pModeFile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getWaitBetweenRetries() {
        return waitBetweenRetries;
    }

    public void setWaitBetweenRetries(int waitBetweenRetries) {
        this.waitBetweenRetries = waitBetweenRetries;
    }
}

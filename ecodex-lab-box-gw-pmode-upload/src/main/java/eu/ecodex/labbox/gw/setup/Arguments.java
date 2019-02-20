package eu.ecodex.labbox.gw.setup;

import com.beust.jcommander.Parameter;

public class Arguments {

    @Parameter(names =  {"-u", "-user"}, description = "Domibus username")
    private String username = "admin";

    @Parameter(names = {"-p", "-password"}, description = "Domibus password")
    private String password = "123456";

    @Parameter(names = {"-p", "-pmode"}, required = true, description = "p-mode file")
    private String pModeFile;

    @Parameter(names = {"-u", "-url"}, required = true, description = "url to domibus example: [http://localhost:8080/domibus]")
    private String url;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}

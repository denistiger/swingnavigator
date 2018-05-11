package folder_management;

public class PasswordManager {
    private static String DEFAULT_LOGIN = "anonymous";
    private static String DEFAULT_PASSWORD = "";
    private String login = DEFAULT_LOGIN;
    private String password = DEFAULT_PASSWORD;
    private boolean isDefaultCredentials = true;

    public boolean isDefaultCredentials() {
        return isDefaultCredentials;
    }

    public void setCredentials(String login, String password) {
        isDefaultCredentials = false;
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void reset() {
        login = DEFAULT_LOGIN;
        password = DEFAULT_PASSWORD;
        isDefaultCredentials = true;
    }

}

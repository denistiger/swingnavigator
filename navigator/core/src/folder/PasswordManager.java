package folder;

import java.util.LinkedList;
import java.util.List;

public class PasswordManager {
    private static String DEFAULT_LOGIN = "anonymous";
    private static String DEFAULT_PASSWORD = "";
    private String login = DEFAULT_LOGIN;
    private String password = DEFAULT_PASSWORD;
    private boolean isDefaultCredentials = true;
    private List<ICredentialsListener> credentialsListeners;

    public interface ICredentialsListener {
        void setCredentials(String login, String password);
    }

    public PasswordManager() {
        credentialsListeners = new LinkedList<>();
    }

    public boolean isDefaultCredentials() {
        return isDefaultCredentials;
//        if (login.compareTo(DEFAULT_LOGIN) == 0 && password.compareTo(DEFAULT_PASSWORD) == 0) {
//            return true;
//        }
//        return false;
    }

    public void addListener(ICredentialsListener credentialsListener) {
        credentialsListeners.add(credentialsListener);
    }

    public void setCredentials(String login, String password) {
        isDefaultCredentials = false;
        this.login = login;
        this.password = password;
        for (ICredentialsListener credentialsListener : credentialsListeners) {
            credentialsListener.setCredentials(login, password);
        }
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

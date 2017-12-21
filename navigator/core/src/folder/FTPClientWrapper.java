package folder;

import org.apache.commons.net.ftp.*;

import java.io.IOException;
import java.io.InputStream;

public class FTPClientWrapper {
    private FTPClient ftp = null;
    private String login = "anonymous";
    private String pass = "";
    private String ftpPath;
    private int ftpPort = 2121;

    public FTPClientWrapper(String ftpPath) {
        this.ftpPath = ftpPath;
        init(this.ftpPath);
    }

    public FTPFile[] listFiles(String onFtpPath) {
        try {
            return ftp.listFiles(onFtpPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream retrieveFileStream(String onFtpPath) throws IOException {
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        return ftp.retrieveFileStream(onFtpPath);
    }

    public int getReplyCode() {
        return ftp.getReplyCode();
    }

    private void init(String ftpPath) {
        ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
//        config.setXXX(YYY); // change required options
        // for example config.setServerTimeZoneId("Pacific/Pitcairn")
        ftp.configure(config);
        ftp.setDefaultPort(ftpPort);
        connect();
    }

    public boolean connect() {
        int reply;
        try {
            ftp.connect(ftpPath);
            ftp.login(login, pass);

            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                disconnect();
                System.err.println("FTP server refused connection.");
            }
        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void disconnect() {
        if (ftp == null) {
            return;
        }
        try {
            ftp.logout();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch(IOException ioe) {
                }
            }
        }
    }


    public void setCredentials(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

    private boolean login() {
        try {
            return ftp.login(login, pass);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticated() {
        try {
            ftp.listFiles("");
            int code = ftp.getReplyCode();
            if (FTPReply.isNegativePermanent(code) || FTPReply.isPositiveIntermediate(code)){
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}

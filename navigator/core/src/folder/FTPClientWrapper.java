package folder;

import org.apache.commons.net.ftp.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

public class FTPClientWrapper {

    public enum FTPStatus {
        SUCCESS,
        ERROR,
        WRONG_CREDENTIALS
    }

    private static Map<Integer, FTPStatus> ftpCodeToStatus;

    static {
        ftpCodeToStatus = new TreeMap<>();
        ftpCodeToStatus.put(530, FTPStatus.WRONG_CREDENTIALS);
    }

    private FTPClient ftp = null;
    private String login = "anonymous";
    private String pass = "";
    private String ftpPath;
    private int ftpPort = 2121;

    public FTPClientWrapper(String ftpPath) {
        this.ftpPath = ftpPath;
        init();
    }

    public FTPClientWrapper(String ftpPath, int ftpPort) {
        this.ftpPath = ftpPath;
        this.ftpPort = ftpPort;
        init();
    }

    public FTPFile[] listFiles(String onFtpPath) {
        try {
            connect();
            if (!ftp.changeWorkingDirectory(onFtpPath)) {
                return null;
            }
            return ftp.listFiles(onFtpPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream retrieveFileStream(String onFtpPath) throws IOException {
        connect();
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        return ftp.retrieveFileStream(onFtpPath);
    }

    public int getReplyCode() {
        return ftp.getReplyCode();
    }

    private void init() {
        ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);
        ftp.setDefaultPort(ftpPort);
    }

    public FTPStatus connect() {
        int reply;
        disconnect();
        try {
            ftp.connect(ftpPath);
            ftp.login(login, pass);

            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                disconnect();
                System.err.println("FTP server refused connection.");
                if (ftpCodeToStatus.containsKey(reply)) {
                    return ftpCodeToStatus.get(reply);
                }
                return FTPStatus.ERROR;
            }
        }
        catch(IOException e){
            e.printStackTrace();
            return FTPStatus.ERROR;
        }
        return FTPStatus.SUCCESS;
    }

    public void disconnect() {
        if (ftp == null) {
            return;
        }
        try {
            ftp.logout();
        } catch(IOException e) {
//            e.printStackTrace();
        } finally {
            if(ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch(IOException ioe) {
                }
            }
        }
    }

    public boolean levelUp() {
        try {
            return ftp.changeToParentDirectory();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void setCredentials(String login, String pass) {
        this.login = login;
        this.pass = pass;
    }

//    private boolean login() {
//        try {
//            return ftp.login(login, pass);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

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

    public String getAbsolutePath() {
        String addr = ftp.getRemoteAddress().getCanonicalHostName();
        System.out.print(addr);
        return addr;
    }

}

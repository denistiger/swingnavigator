package folder.ftp_folder;

import folder.PasswordManager;
import org.apache.commons.net.ftp.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

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
    private String ftpPath;
    private PasswordManager passwordManager;
    private static final int DEFAULT_FTP_PORT = 21;
    private int ftpPort = DEFAULT_FTP_PORT;
    private Semaphore semaphore = new Semaphore(1);

    public FTPClientWrapper(String ftpPath) {
        this.ftpPath = ftpPath;
        init();
    }

    public FTPClientWrapper(String ftpPath, int ftpPort) {
        this.ftpPath = ftpPath;
        this.ftpPort = ftpPort;
        init();
    }

    public void setPasswordManager(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }

    public FTPFile[] listFiles(String onFtpPath) {
        try {
            semaphore.acquire();
            connect();
            if (!ftp.changeWorkingDirectory(onFtpPath)) {
                return null;
            }
            return ftp.listFiles(onFtpPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
        return null;
    }

    public InputStream retrieveFileStream(String onFtpPath) throws IOException {
        try {
            semaphore.acquire();
            connect();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            return ftp.retrieveFileStream(onFtpPath);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }
        return null;
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
            ftp.login(passwordManager.getLogin(), passwordManager.getPassword());

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
            e.printStackTrace();
        } finally {
            if(ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
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

    public String getWorkingDirectory() {
        try {
            return ftp.printWorkingDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFTPPath() {
        String addr = "ftp://";
        if (!passwordManager.isDefaultCredentials()) {
            addr += passwordManager.getLogin();
            if (!passwordManager.getPassword().isEmpty()) {
                addr += ":" + passwordManager.getPassword();
            }
            addr += "@";
        }
        addr += ftpPath;
        if (ftpPort != DEFAULT_FTP_PORT) {
            addr += ":" + Integer.toString(ftpPort);
        }
        return addr;
    }

}

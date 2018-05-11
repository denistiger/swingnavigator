package folder.ftp_folder;

import folder_management.PasswordManager;
import org.apache.commons.net.ftp.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Semaphore;

public class FTPClientWrapper {

    public enum FTPStatus {
        SUCCESS,
        ERROR,
        WRONG_CREDENTIALS
    }

    private static final Map<Integer, FTPStatus> ftpCodeToStatus;

    static {
        ftpCodeToStatus = new TreeMap<>();
        ftpCodeToStatus.put(530, FTPStatus.WRONG_CREDENTIALS);
    }

    private Map<Long, FTPClient> ftpClients = new HashMap<>();
    private final String ftpPath;
    private PasswordManager passwordManager;
    private static final int DEFAULT_FTP_PORT = 21;
    private final int ftpPort;
    private Semaphore semaphore = new Semaphore(1);

    FTPClientWrapper(String ftpPath) {
        this.ftpPath = ftpPath;
        ftpPort = DEFAULT_FTP_PORT;
    }

    FTPClientWrapper(String ftpPath, int ftpPort) {
        this.ftpPath = ftpPath;
        this.ftpPort = ftpPort;
    }

    void setPasswordManager(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }

    private FTPClient getFTPClient() {
        try {
            semaphore.acquire();
            long threadId = Thread.currentThread().getId();
            if (!ftpClients.containsKey(threadId)) {
                ftpClients.put(threadId, createFTPClient());
            }
            return ftpClients.get(threadId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }
        return null;
    }

    FTPFile[] listFiles(String onFtpPath) {
        try {
            FTPClient ftp = getFTPClient();
            connect(ftp);
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
        FTPClient ftp = getFTPClient();
        connect(ftp);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        return ftp.retrieveFileStream(onFtpPath);
    }

    int getReplyCode() {
        return getFTPClient().getReplyCode();
    }

    private FTPClient createFTPClient() {
        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);
        ftp.setDefaultPort(ftpPort);
        return ftp;
    }

    FTPStatus connect() {
        return connect(getFTPClient());
    }

    private FTPStatus connect(FTPClient ftp) {
        int reply;
        disconnect(ftp);
        try {
            ftp.connect(ftpPath);
            ftp.login(passwordManager.getLogin(), passwordManager.getPassword());

            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                disconnect(ftp);
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
    void disconnect() {
        disconnect(getFTPClient());
    }

    private void disconnect(FTPClient ftp) {
        if (ftp == null) {
            return;
        }
        try {
            ftp.logout();
        } catch(IOException ignored) {
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

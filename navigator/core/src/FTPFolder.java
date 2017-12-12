import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FTPFolder implements IFolder {

    FTPClient ftp = null;
    String localFTPPath;
    FolderTypes type;
    String name;

    public FTPFolder(String ftpPath) {
        localFTPPath = "";
        type = FolderTypes.FOLDER;
        init(ftpPath);
    }

    public FTPFolder(FTPClient client, String prefix, FolderTypes type, String name) {
        ftp = client;
        localFTPPath = prefix;
        this.type = type;
        this.name = name;
    }

    private void init(String ftpPath) {
        ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
//        config.setXXX(YYY); // change required options
        // for example config.setServerTimeZoneId("Pacific/Pitcairn")
        ftp.configure(config);
        ftp.setDefaultPort(2121);
        try {
            int reply;
            ftp.connect(ftpPath);
            ftp.login("anonymous","");
//            System.out.println("Connected to " + ftpPath + ".");
//            System.out.print(ftp.getReplyString());

            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
            // TODO check if password needed.
//            ftp.login(username, password);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void tearDown() {
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

    @Override
    public List<IFolder> getItems() {
        try {
            List<IFolder> items = new ArrayList<>();
            FTPFile[] files = ftp.listFiles(localFTPPath);
            for (FTPFile file : files) {
                items.add(new FTPFolder(ftp, localFTPPath.isEmpty() ? file.getName() : localFTPPath + "/" + file.getName(),
                        file.isDirectory() ? FolderTypes.FOLDER : FolderTypes.FILE, file.getName()));
            }
            return  items;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FolderTypes getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }
}

package folder.factory;

import folder.IFolder;
import folder_management.PasswordManager;
import folder.ftp_folder.FTPFolder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UniversalFolderFactory implements IFolderFactory {

    @Override
    public IFolder createIFolder(Map<String, Object> params) {
        if (params.containsKey(FILE)) {
            return new FolderFactory().createIFolder(params);
        }
        if (params.containsKey(FILEPATH)) {
            String filePath = (String) params.get(FILEPATH);
            try {
                URL url = new URL(filePath);
                if (url.getProtocol().compareTo("ftp") == 0) {
                    int port = url.getPort() == -1 ? 21 : url.getPort();
                    FTPFolder folder = new FTPFolder(url.getHost(), port, url.getPath());
                    PasswordManager passwordManager = (PasswordManager) params.get(PASSWORDMANAGER);
                    folder.setPasswordManager(passwordManager);
                    String userInfo = url.getUserInfo();
                    if (userInfo != null && passwordManager.isDefaultCredentials()) {
                        String[] userInfoSplit = userInfo.split(":", 2);
                        if (userInfoSplit.length > 0) {
                            passwordManager.setCredentials(userInfoSplit[0], userInfoSplit.length > 1 ? userInfoSplit[1] : "");
                        }
                    }
                    return folder;
                }
            } catch (MalformedURLException er) {

            }
            catch (Exception er){
                er.printStackTrace();
            }
            File file = new File(filePath);
            Map<String, Object> fileParams = new HashMap<>();
            fileParams.put(FILE, file);
            return new FolderFactory().createIFolder(fileParams);
        }
        return null;
    }
}

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.util.List;

public class DebugTests implements Runnable {

    private String path;

    public DebugTests(String[] args) {
        if (args.length > 0) {
            path = args[0];
        }
        else {
            path = null;
        }
    }

    public void drawFolderTree(IFolder iFolder, int level) {
        for (int i = 0; i < level - 1; ++i) {
            System.out.print("| ");
        }
        if (level > 0) {
            System.out.print("|-");
        }
        System.out.println(iFolder.getName() + " " + iFolder.getType());
        List<IFolder> list = iFolder.getItems();
        if (list == null) {
            return;
        }
        for (IFolder folder : list) {
            drawFolderTree(folder, level + 1);
        }
    }

    public String drawFolderTreeTest(IFolder iFolder) {
        String res = "(" + iFolder.getName() + " : " + iFolder.getType() + ")";
        List<IFolder> list = iFolder.getItems();
        if (list == null || list.size() == 0) {
            return res;
        }
        String inner = "{";
        for (IFolder folder : list) {
            inner += " " + drawFolderTreeTest(folder);
        }
        inner += " }";
        return "[ " + res + " has " + inner + " ]";
    }


    public void run() {
        try {
            if (path == null) {
                System.out.println("No path!");
                return;
            }
//            File file = new File(path);
//            IFolder iFolder = FolderFactory.createIFolder(file);
//            List<IFolder> items = iFolder.getItems();
            IFolder iFolder = new FTPFolder("127.0.0.1");
            drawFolderTree(iFolder, 0);
            //            String res = drawFolderTreeTest(iFolder);
//            System.out.println(res);
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    public static void main(String[] args){
        (new Thread(new DebugTests(args))).start();
    }

}

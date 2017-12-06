import com.sun.deploy.util.StringUtils;

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
        for (int i = 0; i < level; ++i) {
            System.out.print('-');
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

    public void run() {
        try {
            if (path == null) {
                System.out.println("No path!");
                return;
            }
            File file = new File(path);
            IFolder iFolder = FolderFactory.createIFolder(file);
            drawFolderTree(iFolder, 0);

        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    public static void main(String[] args){
        (new Thread(new DebugTests(args))).start();
    }
}

package testing;

import folder.factory.FolderFactory;
import folder.IFolder;
import folder.factory.IFolderFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PrintFilePreview implements Runnable {

    @Override
    public void run() {
        File file = new File("testData");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(IFolderFactory.FILE, file);
        IFolder iFolder = new FolderFactory().createIFolder(params);

        TestUtils.printIFolderPreview(iFolder, "Output/");
    }

    public static void main(String[] args) {
        (new Thread(new PrintFilePreview())).start();
    }
}

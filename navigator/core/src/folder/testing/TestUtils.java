package folder.testing;

import folder.FileTypeGetter;
import folder.IFolder;
import folder.file_preview.FilePreviewGenerator;
import thirdparty.IOUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class TestUtils {

    private static BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }

    public static void savePreview(IFolder file, String path) throws Exception {
        FilePreviewGenerator previewGenerator = new FilePreviewGenerator();
        if (path.compareTo("../../Output/__testData__folder__folder_in.zip__top2__top2.zip__2.jpg") == 0){
            InputStream inputStream = file.getInputStream();
            byte[] data = IOUtils.readFully(inputStream, -1, true);
            FileOutputStream fileOutput = new FileOutputStream(new File("../../image.jpg"));
            fileOutput.write(data);
            fileOutput.close();
        }
        ImageIcon imageIcon = previewGenerator.getFilePreviewSmall(file);
        File outputfile = new File(path + ".png");
        try {
            Image image = imageIcon.getImage();
            BufferedImage bufImage = toBufferedImage(image);
            ImageIO.write(bufImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String drawFolderTreeTest(IFolder iFolder) {
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

    public static String linuxFormat(IFolder folder, String prefix, boolean iterateInZip) {
        String res = "." + prefix + ":\n";
        if (folder == null) {
            return res + "null\n";
        }
        List<IFolder> items = folder.getItems();
        if (items == null) {
            return res;
        }
        items.sort(new Comparator<IFolder>(){
            @Override
            public int compare(IFolder iFolder, IFolder t1) {
                return iFolder.getName().compareTo(t1.getName());
            }
        });
        boolean first = true;
        for (IFolder item : items) {
            if (first) {
                res += item.getName();
                first = false;
            }
            else {
                res += "  " + item.getName();
            }
        }
        res += "\n";
        if (items.size() > 0) {
            res += "\n";
        }
        for (IFolder item : items) {
            if (iterateInZip ? FileTypeGetter.isFolderType(item.getType()) : item.getType() == IFolder.FolderTypes.FOLDER) {
                res += linuxFormat(item, prefix + "/" + item.getName(), iterateInZip);
            }
        }
        return res;
    }

    public static void printIFolderPreview(IFolder iFolder, String name_prefix) {
        if (iFolder == null) {
            return;
        }
        try {
            savePreview(iFolder, name_prefix + "__" + iFolder.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<IFolder> folders = iFolder.getItems();
        if (folders == null) {
            return;
        }
        for (IFolder folder : folders) {
            printIFolderPreview(folder, name_prefix + "__" + iFolder.getName());
        }
    }


    public static IFolder getByName(IFolder folder, String name) {
        List<IFolder> inner = folder.getItems();
        if (inner == null || inner.size() == 0) {
            return null;
        }
        for (IFolder fold : inner) {
            if (fold.getName().compareTo(name)==0) {
                return fold;
            }
        }
        return null;
    }

    public static String getFromScanner(Scanner in_data) {
        String origin = "";
        while (in_data.hasNext()) {
            String tmp = in_data.nextLine();
            origin += tmp + "\n";
        }
        origin += "\n";
        return origin;
    }

    public static String getFromStream(InputStream stream) {
        Scanner in = new Scanner(stream);
        return getFromScanner(in);
    }

    public static String getTestFile(String path){
        File base = new File(path);
        try {
            return getFromScanner(new Scanner(base));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkFileHasNoChildren(IFolder folder) {
        if (!FileTypeGetter.isFolderType(folder.getType())) {
            if (folder.getItems() != null) {
                return false;
            }
            return true;
        }
        for (IFolder item : folder.getItems()) {
            if (!checkFileHasNoChildren(item)) {
                return false;
            }
        }
        return true;
    }
}

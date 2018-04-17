package ui;

import folder.IFolder;
import folder.file_preview.IFilePreviewListener;

import javax.swing.*;

public class FolderButton extends FolderButtonSkeleton implements IFilePreviewListener {

    private static int MAX_LINE_LENGTH = 12;
    private static int MAX_LINES = 3;

    public static String toMultilineHTML(String filename) {
        String res = "<html><center>";
        int lineCount = 0;
        while (filename.length() > MAX_LINE_LENGTH && lineCount < MAX_LINES - 1) {
            res += filename.substring(0, MAX_LINE_LENGTH) + "<br>";
            filename = filename.substring(MAX_LINE_LENGTH);
            lineCount++;
        }
        if (filename.length() <= MAX_LINE_LENGTH) {
            res += filename;
        }
        else {
            // "..." for two symbols width. So we allow 13 symbols in the last line.
            res += filename.substring(0, MAX_LINE_LENGTH - 2) + "...";
        }
        res += "</center></html>";
        return res;
    }

    public FolderButton(IFolder folder, ImageIcon icon) {
        setText(FolderButton.toMultilineHTML(folder.getName()));
        setIcon(icon);
        setButtonAlignment();
        setFolder(folder);
    }

    @Override
    public void setPreviewIcon(ImageIcon icon) {
        setIcon(icon);
    }
}

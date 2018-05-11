package ui.folder_button;

import folder.IFolder;
import file_preview.IFilePreviewListener;

import javax.swing.*;

public class FolderButton extends FolderButtonSkeleton implements IFilePreviewListener {

    private static final int MAX_LINE_LENGTH = 12;
    private static final int MAX_LINES = 3;

    public static String toMultilineHTML(String filename) {
        int lineCount = 0;
        StringBuilder stringBuilder = new StringBuilder("<html><center>");
        while (filename.length() > MAX_LINE_LENGTH && lineCount < MAX_LINES - 1) {
            stringBuilder.append(filename.substring(0, MAX_LINE_LENGTH));
            stringBuilder.append("<br>");
            filename = filename.substring(MAX_LINE_LENGTH);
            lineCount++;
        }
        if (filename.length() <= MAX_LINE_LENGTH) {
            stringBuilder.append(filename);
        }
        else {
            // "..." for two symbols width. So we allow 13 symbols in the last line.
            stringBuilder.append(filename.substring(0, MAX_LINE_LENGTH - 2));
            stringBuilder.append("...");
        }
        stringBuilder.append("</center></html>");
        return stringBuilder.toString();
    }

    FolderButton(IFolder folder, ImageIcon icon) {
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

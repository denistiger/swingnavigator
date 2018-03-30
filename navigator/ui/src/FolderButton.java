import folder.IFolder;

import javax.swing.*;

public class FolderButton extends JLabel {
    private IFolder folder;

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
        super(FolderButton.toMultilineHTML(folder.getName()), icon, JLabel.CENTER);
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setVerticalAlignment(JLabel.TOP);
        this.folder = folder;
        setMinimumSize(getPreferredSize());
        setMaximumSize(getPreferredSize());
    }

    public IFolder getFolder() {
        return folder;
    }
}

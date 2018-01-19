import folder.IFolder;

import javax.swing.*;

public class FolderButton extends JLabel {
    private IFolder folder;

    public FolderButton(IFolder folder, ImageIcon icon) {
        super(folder.getName(), icon, JLabel.CENTER);
        setVerticalAlignment(JLabel.BOTTOM);
        this.folder = folder;
    }

    public IFolder getFolder() {
        return folder;
    }
}

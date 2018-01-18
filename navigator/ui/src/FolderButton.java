import folder.IFolder;

import javax.swing.*;

public class FolderButton extends JButton {
    private IFolder folder;

    public FolderButton(IFolder folder) {
        this.folder = folder;
    }

    public IFolder getFolder() {
        return folder;
    }
}

package ui.folder_button;

import folder.IFolder;
import ui.IOpenFolderListener;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class FolderButtonSkeleton extends JLabel {
    private IFolder folder;

    public FolderButtonSkeleton() {
        folder = null;
    }

    public void setButtonAlignment() {
        setHorizontalAlignment(JLabel.CENTER);
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setVerticalAlignment(JLabel.TOP);
        setMinimumSize(getPreferredSize());
        setMaximumSize(getPreferredSize());
    }

    protected void setFolder(IFolder folder) {
        this.folder = folder;
    }

    public IFolder getFolder() {
        return folder;
    }

    public void addOpenFolderListener(IOpenFolderListener iOpenFolderListener) {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (getFolder() != null) {
                        iOpenFolderListener.openFolder(getFolder());
                    }
                    else {
                        iOpenFolderListener.levelUp();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }
}

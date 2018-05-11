package ui.folder_button;

import folder.IFolder;
import ui.IOpenFolderListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class FolderButtonSkeleton extends JLabel {
    private IFolder folder;
    private static Font fontBase = new Font("Arial", Font.PLAIN, 12);
    private static Font fontSelected = new Font("Arial", Font.BOLD, 12);

    FolderButtonSkeleton() {
        folder = null;
    }

    void setButtonAlignment() {
        setHorizontalAlignment(JLabel.CENTER);
        setHorizontalTextPosition(JLabel.CENTER);
        setVerticalTextPosition(JLabel.BOTTOM);
        setVerticalAlignment(JLabel.TOP);
        setMinimumSize(getPreferredSize());
        setMaximumSize(getPreferredSize());

        setSelected(false);
    }

    public void setSelected(boolean selected) {
        if (selected) {
            setOpaque(true);
            setBackground(new Color(142, 180, 212));
            setForeground(Color.WHITE);
            setFont(fontSelected);
        }
        else {
            setForeground(Color.BLACK);
            setFont(fontBase);
            setOpaque(false);
        }
    }

    protected void setFolder(IFolder folder) {
        this.folder = folder;
    }

    public IFolder getFolder() {
        return folder;
    }

    public void notifyIOpenFolderListener(IOpenFolderListener iOpenFolderListener) {
        if (getFolder() != null) {
            iOpenFolderListener.openFolder(getFolder());
        }
        else {
            iOpenFolderListener.levelUp();
        }
    }

    void addOpenFolderListener(IOpenFolderListener iOpenFolderListener) {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    notifyIOpenFolderListener(iOpenFolderListener);
                }
                if (e.getClickCount() == 1) {
                    iOpenFolderListener.selectFolder(FolderButtonSkeleton.this);
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

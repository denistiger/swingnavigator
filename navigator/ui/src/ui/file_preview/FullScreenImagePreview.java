package ui.file_preview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FullScreenImagePreview extends JFrame{

    private ImagePreviewPanel previewPanel;

    public FullScreenImagePreview(ImagePreviewPanel imagePreviewPanel, IFullScreenListener fullScreenListener) {
        setSize(getToolkit().getScreenSize());
        setUndecorated(true);
        setTitle("Full Screen Image Preview");
        setVisible(false);
        this.previewPanel = imagePreviewPanel;
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((int)e.getKeyChar() == 8 /*Backspace*/ ||
                        (int)e.getKeyChar() == 27 /*Esc*/) {
                    fullScreenListener.exitFullScreen();
                }
            }

        });
    }

    public void showPreview(boolean showPreview) {
        setVisible(showPreview);
        repaint();
    }

}

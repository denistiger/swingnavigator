package ui.file_preview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FullScreenImagePreview extends JFrame{

    private ImagePreviewPanel previewPanel;
    private Color initialColor;

    public FullScreenImagePreview(ImagePreviewPanel imagePreviewPanel, IFullScreenListener fullScreenListener) {
        setSize(getToolkit().getScreenSize());
        setUndecorated(true);
        setTitle("Full Screen Image Preview");
        setVisible(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        previewPanel = imagePreviewPanel;
        initialColor = previewPanel.getBackground();
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    fullScreenListener.exitFullScreen();
                }
            }

        });
    }


    public void showPreview(boolean showPreview) {
        if (showPreview) {
            previewPanel.setBackground(Color.BLACK);
        }
        else {
            previewPanel.setBackground(initialColor);
        }
        setVisible(showPreview);
        repaint();
    }

}

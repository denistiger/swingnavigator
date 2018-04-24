package ui.file_preview;

import folder.IFolder;
import folder.FolderIterator;
import folder.file_preview.FilePreviewGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.im.spi.InputMethod;
import java.util.HashMap;
import java.util.Map;

public class GenericPreviewPanel extends FilePreviewPanel {

    private static FilePreviewPanel unknownFilePreview = new UnknownFilePreviewPanel();
    private static Map<IFolder.FolderTypes, FilePreviewPanel> previewPanelMap;
    static {
        previewPanelMap = new HashMap<>();
        previewPanelMap.put(IFolder.FolderTypes.IMAGE, new ImagePreviewPanel());
        previewPanelMap.put(IFolder.FolderTypes.TEXT_FILE, new TextFilePreviewPanel());
        previewPanelMap.put(IFolder.FolderTypes.OTHER_FILE, unknownFilePreview);
        previewPanelMap.put(IFolder.FolderTypes.FOLDER, unknownFilePreview);
        previewPanelMap.put(IFolder.FolderTypes.ZIP, unknownFilePreview);
        previewPanelMap.put(IFolder.FolderTypes.UNKNOWN, unknownFilePreview);
    }

    private FolderIterator folderIterator;
    private FilePreviewPanel currentPreview = null;

    private JButton prevButton, nextButton;
    private JLabel prevLabel, curLabel, nextLabel;

    public GenericPreviewPanel(FolderIterator folderIterator) {
        this.folderIterator = folderIterator;

        nextButton = new JButton("Next >");
        prevButton = new JButton("< Prev");

        nextButton.setEnabled(false);
        prevButton.setEnabled(false);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                folderIterator.next();
            }
        });

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                folderIterator.prev();
            }
        });

        prevLabel = new JLabel();
        curLabel = new JLabel();
        nextLabel = new JLabel();

        Font font = new Font("Arial", Font.BOLD, 16);
        prevLabel.setFont(font);
        curLabel.setFont(font);
        nextLabel.setFont(font);

        prevLabel.setHorizontalTextPosition(JLabel.LEFT);
        curLabel.setHorizontalTextPosition(JLabel.CENTER);
        nextLabel.setHorizontalTextPosition(JLabel.RIGHT);

        prevLabel.setVerticalTextPosition(JLabel.CENTER);
        curLabel.setVerticalTextPosition(JLabel.CENTER);
        nextLabel.setVerticalTextPosition(JLabel.CENTER);

        prevLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        curLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nextLabel.setHorizontalAlignment(SwingConstants.LEFT);

        prevLabel.setVerticalAlignment(SwingConstants.CENTER);
        curLabel.setVerticalAlignment(SwingConstants.CENTER);
        nextLabel.setVerticalAlignment(SwingConstants.CENTER);


        JPanel prevNextPanel = new JPanel();
        GridLayout prevNextPanelGrid = new GridLayout(1, 2);
        prevNextPanel.setLayout(prevNextPanelGrid);
        prevButton.setHorizontalAlignment(SwingConstants.CENTER);
        nextButton.setHorizontalAlignment(SwingConstants.CENTER);

        prevNextPanel.add(prevButton);
        prevNextPanel.add(nextButton);

        JPanel topMiddlePanel = new JPanel();
        GridLayout topMiddlePanelLayout = new GridLayout(2, 1);
        topMiddlePanel.setLayout(topMiddlePanelLayout);
        topMiddlePanel.add(curLabel);
//        topMiddlePanel.add(Box.createVerticalStrut(8));
        topMiddlePanel.add(prevNextPanel);

        JPanel topPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 3);
        gridLayout.setHgap(15);
        topPanel.setLayout(gridLayout);

        topPanel.add(prevLabel);
        topPanel.add(topMiddlePanel);
        topPanel.add(nextLabel);

        JPanel topPanelWithSpace = new JPanel();
        BoxLayout boxLayout = new BoxLayout(topPanelWithSpace, BoxLayout.Y_AXIS);
        topPanelWithSpace.setLayout(boxLayout);
        topPanelWithSpace.add(Box.createVerticalStrut(10));
        topPanelWithSpace.add(topPanel);
        topPanelWithSpace.add(Box.createVerticalStrut(10));

        BorderLayout borderLayout = new BorderLayout();

        setLayout(borderLayout);
        add(topPanelWithSpace, BorderLayout.PAGE_START);


        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {

                    private long when = 0;
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_RIGHT && (e.getWhen() - when) > 150) {
                            when = e.getWhen();
                            folderIterator.next();
                        }
                        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_LEFT && (e.getWhen() - when) > 150) {
                            when = e.getWhen();
                            folderIterator.prev();
                        }
                        return false;
                    }
                });

    }

    public void updatePreviewFile() {
        IFolder file = folderIterator.getIFolder();
        if (file != null) {
            try {
                setPreviewFile(file);
            } catch (PreviewException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTopPanel() {
        curLabel.setText(folderIterator.getIFolder().getName());

        FilePreviewGenerator previewGenerator = new FilePreviewGenerator();

        if (folderIterator.hasNext()) {
            nextLabel.setText(folderIterator.getNext().getName());
            nextLabel.setIcon(previewGenerator.getFilePreviewSmall(folderIterator.getNext()));
            nextButton.setEnabled(true);
        }
        else {
            nextLabel.setText("End of folder");
            nextLabel.setIcon(previewGenerator.getFilePreviewSmall(null));
            nextButton.setEnabled(false);
        }


        if (folderIterator.hasPrev()) {
            prevLabel.setText(folderIterator.getPrev().getName());
            prevLabel.setIcon(previewGenerator.getFilePreviewSmall(folderIterator.getPrev()));
            prevButton.setEnabled(true);
        }
        else {
            prevLabel.setText("Start of folder");
            prevLabel.setIcon(previewGenerator.getFilePreviewSmall(null));
            prevButton.setEnabled(false);
        }
    }

    @Override
    public void setPreviewFile(IFolder previewFile) throws PreviewException {
        if (currentPreview != null) {
            remove(currentPreview);
        }
        currentPreview = previewPanelMap.get(previewFile.getType());
        if (currentPreview == null) {
            currentPreview = unknownFilePreview;
        }
        updateTopPanel();
        currentPreview.setPreviewFile(previewFile);
        add(currentPreview, BorderLayout.CENTER);
        repaint();
    }
}

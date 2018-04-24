package ui.file_preview;

import folder.IFolder;
import folder.FolderIterator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

        JPanel topPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        topPanel.setLayout(flowLayout);

        topPanel.add(prevLabel);
        topPanel.add(prevButton);
        topPanel.add(curLabel);
        topPanel.add(nextButton);
        topPanel.add(nextLabel);

        BorderLayout borderLayout = new BorderLayout();

        setLayout(borderLayout);
        add(topPanel, BorderLayout.PAGE_START);
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
        if (folderIterator.hasNext()) {
            nextLabel.setText(folderIterator.getNext().getName());
            nextButton.setEnabled(true);
        }
        else {
            nextLabel.setText("End of folder");
            nextButton.setEnabled(false);
        }

        if (folderIterator.hasPrev()) {
            prevLabel.setText(folderIterator.getPrev().getName());
            prevButton.setEnabled(true);
        }
        else {
            prevLabel.setText("Start of folder");
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

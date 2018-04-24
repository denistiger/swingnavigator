package ui.file_preview;

import folder.IFolder;
import thirdparty.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class TextFilePreviewPanel extends FilePreviewPanel {

    private JTextArea textArea;

    public TextFilePreviewPanel() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void setPreviewFile(IFolder previewFile) {
        InputStream inputStream = previewFile.getInputStream();
        try {
            // TODO Return notification on large file. Read only first 10 000 000 bytes.
            byte[] data = IOUtils.readFully(inputStream, 1000000, false);
            textArea.setText(new String(data));
            textArea.setCaretPosition(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

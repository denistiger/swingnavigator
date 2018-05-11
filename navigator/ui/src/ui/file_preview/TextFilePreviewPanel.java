package ui.file_preview;

import folder.IFolder;
import thirdparty.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class TextFilePreviewPanel extends FilePreviewPanel {

    private JTextArea textArea;
    private static final int MAX_DISPLAYED_SYMBOLS = 1000000;

    TextFilePreviewPanel() {
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
            byte[] data = IOUtils.readFully(inputStream, MAX_DISPLAYED_SYMBOLS, false);
            inputStream.close();
            textArea.setText(new String(data));
            textArea.setCaretPosition(0);
            if (data.length == MAX_DISPLAYED_SYMBOLS) {
                textArea.append("\n\n Only first " + MAX_DISPLAYED_SYMBOLS + " were loaded for optimization purpose.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

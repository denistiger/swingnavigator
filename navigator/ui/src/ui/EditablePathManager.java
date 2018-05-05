package ui;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class EditablePathManager {

    private JPanel mainPanel;
    private JLabel pathLabel;
    private JTextField pathTextField;

    EditablePathManager() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        Font font = new Font("ARIAL", Font.BOLD, 20);

        pathLabel = new JLabel("Address");
        pathLabel.setFont(font);
        pathLabel.setMaximumSize(new Dimension(150, 50));

        pathTextField = new JTextField();
        pathTextField.setFont(font);
        pathTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        mainPanel.add(pathLabel);
        mainPanel.add(pathTextField);

        disableKeyEventsCapture();

    }

    private void disableKeyEventsCapture() {
        Action doNothing = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                //do nothing
            }
        };
        pathTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK), "doNothing");
        pathTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK), "doNothing");
        pathTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "doNothing");
        pathTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "doNothing");
        pathTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "doNothing");
        pathTextField.getActionMap().put("doNothing", doNothing);
    }

    public void addActionListener(ActionListener actionListener) {
        pathTextField.addActionListener(actionListener);
    }

    public void addDocumentListener(DocumentListener documentListener) {
        pathTextField.getDocument().addDocumentListener(documentListener);
    }

    public void setPath(String path) {
        pathTextField.setText(path);
    }

    public String getPath() {
        return pathTextField.getText();
    }

    public JPanel getPathPanel() {
        return mainPanel;
    }
}

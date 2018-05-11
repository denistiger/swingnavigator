package ui;

import folder_management.PasswordManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class PasswordDialog {

    private PasswordManager passwordManager;
    private JTextField loginText;
    private JPasswordField passwordText;
    private JDialog dialog;
    private boolean credentialsAreSet = false;

    PasswordDialog(JFrame parent, PasswordManager passwordManager) {
        dialog = new JDialog(parent, "Credentials are needed");
        dialog.setModal(true);
        this.passwordManager = passwordManager;

        Font font = new Font("Arial", Font.BOLD, 16);

        JPanel panel = new JPanel();
        BoxLayout vLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(vLayout);

        JPanel inputPanel = new JPanel();
        BoxLayout hLayout = new BoxLayout(inputPanel, BoxLayout.X_AXIS);
        inputPanel.setLayout(hLayout);

        JPanel labelPanel = new JPanel();
        BoxLayout vLabelLayout = new BoxLayout(labelPanel, BoxLayout.Y_AXIS);
        labelPanel.setLayout(vLabelLayout);
        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setFont(font);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(font);
        labelPanel.add(loginLabel);
        labelPanel.add(Box.createVerticalStrut(10));
        labelPanel.add(passwordLabel);

        JPanel editPanel = new JPanel();
        BoxLayout vEditLayout = new BoxLayout(editPanel, BoxLayout.Y_AXIS);
        editPanel.setLayout(vEditLayout);
        loginText = new JTextField();
        loginText.setText(passwordManager.getLogin());
        loginText.setFont(font);
        passwordText = new JPasswordField(14);
        passwordText.setText(passwordManager.getPassword());
        passwordText.setFont(font);
        passwordText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setCredentials();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        editPanel.add(loginText);
        editPanel.add(Box.createVerticalStrut(10));
        editPanel.add(passwordText);

        inputPanel.add(Box.createHorizontalStrut(8));
        inputPanel.add(labelPanel);
        inputPanel.add(Box.createHorizontalStrut(8));
        inputPanel.add(editPanel);
        inputPanel.add(Box.createHorizontalStrut(8));

        JPanel buttonsPanel = new JPanel();
        BoxLayout hButtonsLayout = new BoxLayout(buttonsPanel, BoxLayout.X_AXIS);
        buttonsPanel.setLayout(hButtonsLayout);

        JButton okButton = new JButton("Login");
        okButton.setFont(font);
        okButton.addActionListener(e -> {
            setCredentials();
            closeDialog();
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(font);
        cancelButton.addActionListener(e -> closeDialog());

        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        panel.add(Box.createVerticalStrut(15));
        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(15));
        panel.add(buttonsPanel);
        panel.add(Box.createVerticalStrut(10));

//        BorderLayout borderLayout = new BorderLayout();
//        setLayout(borderLayout);

        dialog.add(panel);

        dialog.pack();

        Rectangle frameBounds = parent.getBounds();
        Rectangle ownBounds = dialog.getBounds();

        Rectangle newBounds = new Rectangle((int)(ownBounds.getX() - ownBounds.getCenterX() + frameBounds.getCenterX()),
                (int)(ownBounds.getY() - ownBounds.getCenterY() + frameBounds.getCenterY()),
                (int)ownBounds.getWidth(), (int)ownBounds.getHeight());

        dialog.setBounds(newBounds);
        dialog.setResizable(false);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    boolean credentialsAreSet() {
        return credentialsAreSet;
    }

    private void closeDialog() {
        dialog.setVisible(false);
        dialog.dispose();
    }

    private void setCredentials() {
        passwordManager.setCredentials(loginText.getText(), String.valueOf(passwordText.getPassword()));
        credentialsAreSet = true;
    }
}

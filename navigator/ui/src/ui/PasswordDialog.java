package ui;

import folder.PasswordManager;

import javax.swing.*;

public class PasswordDialog extends JDialog {

    PasswordManager passwordManager;

    public PasswordDialog(JFrame parent, PasswordManager passwordManager) {
        super(parent, "Credentials are needed");
        setModal(true);
        this.passwordManager = passwordManager;
    }
}

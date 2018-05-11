package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

class EditablePathManager {

    public interface IPathEditListener {
        void newPathEntered();
        void pathAppended();
        void setEditableMode(boolean editableMode);
    }

    private static final int PANEL_HEIGHT = 50;

    private JPanel mainPanel, buttonsPanel;
    private JTextField pathTextField;
    private boolean editingMode = false;

    private String currentPath;

    private JButton editAddressButton, goButton, cancelButton;

    private List<KeyStroke> disabledKeyStrokes;

    private List<IPathEditListener> pathEditListeners;

    EditablePathManager() {
        pathEditListeners = new LinkedList<>();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        Font font = new Font("ARIAL", Font.BOLD, 20);

        JLabel pathLabel = new JLabel("Address");
        pathLabel.setFont(font);
        pathLabel.setMaximumSize(new Dimension(150, PANEL_HEIGHT));

        editAddressButton = new JButton("Edit Address");
        editAddressButton.setFont(font);

        editAddressButton.addActionListener(e -> allowAddressEditing(true));

        goButton = new JButton("Go Address:");
        goButton.setFont(font);

        goButton.addActionListener(e -> openPath());

        cancelButton = new JButton("Reset");
        cancelButton.setFont(font);

        cancelButton.addActionListener(e -> reset());

        pathTextField = new JTextField();
        pathTextField.setFont(font);
        pathTextField.setMaximumSize(new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT));

        initDisabledKeyStrokesList();

        allowAddressEditing(false);

        mainPanel.add(buttonsPanel);
        mainPanel.add(pathTextField);

        DocumentListener documentListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                notifyOnPathAppending();
            }

            public void removeUpdate(DocumentEvent e) {
                notifyOnPathAppending();
            }

            public void insertUpdate(DocumentEvent e) {
                notifyOnPathAppending();
            }
        };
        pathTextField.getDocument().addDocumentListener(documentListener);

        pathTextField.addActionListener(e -> {
            if (editingMode) {
                openPath();
            }
        });

        pathTextField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                undoDeprecatedAction();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                undoDeprecatedAction();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                undoDeprecatedAction();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                undoDeprecatedAction();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                undoDeprecatedAction();
            }
        });

        editAddressButton.setFocusable(false);
        goButton.setFocusable(false);
        cancelButton.setFocusable(false);
    }

    private void undoDeprecatedAction() {
        if (!editingMode) {
            pathTextField.setCaretPosition(pathTextField.getText().length());
        }
    }

    private void notifyOnPathAppending() {
        if (!editingMode) {
            for (IPathEditListener pathEditListener : pathEditListeners) {
                pathEditListener.pathAppended();
            }
        }
    }

    private void openPath() {
        for (IPathEditListener pathEditListener : pathEditListeners) {
            pathEditListener.newPathEntered();
        }
        allowAddressEditing(false);
    }

    private void reset() {
        pathTextField.setText(currentPath);
        allowAddressEditing(false);
    }

    private void setUpWidgets() {
        buttonsPanel.removeAll();
        buttonsPanel.add(Box.createHorizontalStrut(10));
        if (editingMode) {
            buttonsPanel.add(cancelButton);
            buttonsPanel.add(Box.createHorizontalStrut(15));
            buttonsPanel.add(goButton);
        }
        else {
            buttonsPanel.add(editAddressButton);
        }
        buttonsPanel.add(Box.createHorizontalStrut(10));
        buttonsPanel.setMinimumSize(new Dimension(250, PANEL_HEIGHT));
        buttonsPanel.setMaximumSize(new Dimension(250, PANEL_HEIGHT));

        mainPanel.revalidate();
    }

    void addListener(IPathEditListener pathEditListener) {
        pathEditListeners.add(pathEditListener);
    }

    private void allowAddressEditing(boolean allowEditing) {
        editingMode = allowEditing;
        if (editingMode) {
            currentPath = getPath();
            enableKeyEventCapture();
        }
        else {
            disableKeyEventsCapture();
            undoDeprecatedAction();
        }
        setUpWidgets();
        for (IPathEditListener pathEditListener : pathEditListeners) {
            pathEditListener.setEditableMode(editingMode);
        }
        pathTextField.requestFocus();
    }

    private void initDisabledKeyStrokesList() {
        disabledKeyStrokes = new LinkedList<>();
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.SHIFT_DOWN_MASK));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.SHIFT_DOWN_MASK));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,
                InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,
                InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.SHIFT_DOWN_MASK));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_END, InputEvent.SHIFT_DOWN_MASK));
        disabledKeyStrokes.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));

        Action doNothing = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                //do nothing
            }
        };
        pathTextField.getActionMap().put("doNothing", doNothing);
    }

    private void disableKeyEventsCapture() {
        for (KeyStroke keyStroke : disabledKeyStrokes) {
            pathTextField.getInputMap().put(keyStroke, "doNothing");
        }
    }

    private void enableKeyEventCapture() {
        for (KeyStroke keyStroke : disabledKeyStrokes) {
            pathTextField.getInputMap().remove(keyStroke);
        }
    }

    void setPath(String path) {
        pathTextField.setText(path);
    }

    String getPath() {
        return pathTextField.getText();
    }

    JPanel getPathPanel() {
        return mainPanel;
    }
}

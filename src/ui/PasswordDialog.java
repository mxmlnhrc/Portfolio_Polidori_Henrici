package ui;

import model.Projekt;
import ui.policy.DeletionPolicy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Dialogfenster für Passwortabfrage vor dem Löschen eines Projekts.
 */
public class PasswordDialog extends JDialog {
    private boolean deletionAllowed = false;
    private final DeletionPolicy<Projekt> policy;
    private final Projekt projekt;
    private final JPasswordField passwordField = new JPasswordField(20);

    /**
     * Konstruktor.
     * @param parent übergeordnetes Frame
     * @param policy Lösch-Policy zur Passwortprüfung
     * @param projekt das zu löschende Projekt
     */
    public PasswordDialog(Frame parent, DeletionPolicy<Projekt> policy, Projekt projekt) {
        super(parent, "Projekt löschen", true);
        this.policy = policy;
        this.projekt = projekt;
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Passwort:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        getContentPane().add(panel, BorderLayout.CENTER);

        okButton.addActionListener((ActionEvent e) -> onOk());
        cancelButton.addActionListener((ActionEvent e) -> onCancel());
    }

    private void onOk() {
        String credential = new String(passwordField.getPassword());
        if (policy.canDelete(projekt, credential)) {
            deletionAllowed = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Falsches Passwort!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    /**
     * Gibt zurück, ob die Löschung erlaubt wurde.
     * @return true, wenn Passwort korrekt und OK gedrückt
     */
    public boolean isDeletionAllowed() {
        return deletionAllowed;
    }
}

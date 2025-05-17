package ui;

import model.Projekt;
import javax.swing.*;
import java.awt.*;

public class EditDialog extends JDialog {
    private boolean confirmed = false;

    // Projektfelder
    private final JTextField titelField = new JTextField(20);
    private final JTextField noteField = new JTextField(5);
    private final JTextField datumField = new JTextField(10);

    // Studentenfelder für das Hinzufügen
    private final JTextField studentNameField = new JTextField(12);
    private final JTextField studentBirthField = new JTextField(10); // yyyy-MM-dd
    private final JTextField studentMatField = new JTextField(8);

    private final Projekt projekt;
    // Anzeige der bereits vorhandenen Studenten
    private final DefaultListModel<String> studentListModel = new DefaultListModel<>();
    private final JList<String> studentListView = new JList<>(studentListModel);

    public EditDialog(Frame parent, Projekt projekt) {
        super(parent, "Projekt bearbeiten", true);
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

        // Felder vorbefüllen
        titelField.setText(projekt.getTitel() != null ? projekt.getTitel() : "");
        noteField.setText(String.valueOf(projekt.getNote()));
        datumField.setText(projekt.getAbgabeDatum() != null ? projekt.getAbgabeDatum() : "");

        // GUI aufbauen
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Titel:"), gbc);
        gbc.gridx = 1; panel.add(titelField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Note (0-100):"), gbc);
        gbc.gridx = 1; panel.add(noteField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Abgabedatum (yyyyMMdd):"), gbc);
        gbc.gridx = 1; panel.add(datumField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        getContentPane().add(panel, BorderLayout.CENTER);

        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> onCancel());
    }

    private void onOk() {
        String titel = titelField.getText().trim();
        String noteText = noteField.getText().trim();
        String datumText = datumField.getText().trim();
        double note;
        try {
            projekt.setTitel(titel);
            note = Double.parseDouble(noteText);
            projekt.setNote(note);
            projekt.setAbgabeDatum(datumText);
            confirmed = true;
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe: " + ex.getMessage());
        }
    }

    private void onCancel() {
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

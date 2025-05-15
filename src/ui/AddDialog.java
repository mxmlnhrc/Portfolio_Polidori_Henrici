package ui;

import model.Projekt;
import model.Student;
import datastructure.EigeneListe;
import exception.EmptyNameException;
import exception.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Dialog zum Hinzufügen eines neuen Projekts.
 */
public class AddDialog extends JDialog {
    private boolean confirmed = false;

    private final JTextField titelField = new JTextField(20);
    private final JTextField noteField = new JTextField(5);
    private final JTextField datumField = new JTextField(10);

    /**
     * Konstruktor.
     * @param parent übergeordneter Frame
     * @param projectList Liste, der das neue Projekt hinzugefügt wird
     */
    public AddDialog(Frame parent, EigeneListe<Projekt> projectList) {
        super(parent, "Projekt hinzufügen", true);
        initComponents(projectList);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents(EigeneListe<Projekt> projectList) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        // Titel
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Titel:"), gbc);
        gbc.gridx = 1;
        panel.add(titelField, gbc);

        // Note
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Note (0-100):"), gbc);
        gbc.gridx = 1;
        panel.add(noteField, gbc);

        // Abgabedatum
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Abgabedatum (yyyyMMdd):"), gbc);
        gbc.gridx = 1;
        panel.add(datumField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        getContentPane().add(panel, BorderLayout.CENTER);

        // Action listeners
        okButton.addActionListener((ActionEvent e) -> onOk(projectList));
        cancelButton.addActionListener((ActionEvent e) -> onCancel());
    }

    private void onOk(EigeneListe<Projekt> projectList) {
        String titel = titelField.getText().trim();
        String noteText = noteField.getText().trim();
        String datumText = datumField.getText().trim();
        double note;
        try {
            if (titel.isEmpty()) {
                throw new EmptyNameException();
            }
            try {
                note = Double.parseDouble(noteText);
            } catch (NumberFormatException nfe) {
                throw new ValidationException("Note muss eine Zahl sein: " + noteText);
            }
            // Projekt-Konstruktor validiert Note und Datum
            Projekt projekt = new Projekt(titel, note, datumText);
            projectList.add(projekt);
            confirmed = true;
            dispose();
        } catch (ValidationException ex) {
            // EmptyNameException ist eine Unterklasse von ValidationException
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Eingabefehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }

    /**
     * @return true, wenn OK gedrückt und Eingaben erfolgreich validiert
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}

package ui;

import datastructure.EigeneListe;
import exception.DuplicatedMatrikelnummerException;
import exception.DuplicatedNameException;
import model.Projekt;
import model.Student;
import util.DateValidator;
import util.ProjektFilterUtil;
import util.Validator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EditDialog extends JDialog {
    private boolean confirmed = false;

    // Validator Datum
    private final Validator<String> dateValidator = new DateValidator();

    // Projektfelder
    private final JTextField titelField = new JTextField(20);
    private final JTextField noteField = new JTextField(5);
    private final JTextField datumField = new JTextField(10);

    // Studentenfelder für das Hinzufügen
    private final JTextField studentNameField = new JTextField(12);
    private final JTextField studentBirthField = new JTextField(10); // yyyyMMdd
    private final JTextField studentMatField = new JTextField(8);

    private final Projekt projekt;

    // Anzeige der bereits vorhandenen Studenten
    private final DefaultListModel<String> studentListModel = new DefaultListModel<>();
    private final JList<String> studentListView = new JList<>(studentListModel);

    // Bearbeiten-Feld und Button
    private final JTextField editStudentNameField = new JTextField(12);
    JButton editStudentBtn = new JButton("Student-Name ändern");

    private final EigeneListe<Projekt> projectList;

    /**
     * Konstruktor.
     * @param parent übergeordneter Frame
     * @param projekt Projekt, das bearbeitet werden soll
     * @param projectList Liste, der das neue Projekt hinzugefügt wird | Überprüft ob name einmalig ist.
     */
    public EditDialog(Frame parent, Projekt projekt, EigeneListe<Projekt> projectList) {
        super(parent, "Projekt bearbeiten", true);
        this.projekt = projekt;
        this.projectList = projectList;
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

        // --- Studentenbereich für Hinzufügen ---
        // Felder mit leeren Werten initialisieren
        studentNameField.setText("");
        studentBirthField.setText("");
        studentMatField.setText("");

        // Anzeigen aller bereits zugeordneten Studenten
        updateStudentListModel();

        // Studentenfelder
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1; panel.add(studentNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Geburtsdatum (yyyyMMdd):"), gbc);
        gbc.gridx = 1; panel.add(studentBirthField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Matrikelnummer:"), gbc);
        gbc.gridx = 1; panel.add(studentMatField, gbc);

        JButton addStudentBtn = new JButton("Student hinzufügen");
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(addStudentBtn, gbc);

        // Studentenliste anzeigen
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        JScrollPane studentScroll = new JScrollPane(studentListView);
        studentScroll.setPreferredSize(new Dimension(220, 80)); // z.B. 220px Breite, 80px Höhe
        panel.add(studentScroll, gbc);

        // Listener für den Button
        addStudentBtn.addActionListener(e -> onAddStudent());

        //Studenten entfernen
        JButton removeStudentBtn = new JButton("Student entfernen");
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        panel.add(removeStudentBtn, gbc);

        removeStudentBtn.addActionListener(e -> onRemoveStudent());

        // Bearbeiten eines Studenten
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 1;
        panel.add(new JLabel("Bearbeiteter Name:"), gbc);
        gbc.gridx = 1;
        panel.add(editStudentNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 2;
        panel.add(editStudentBtn, gbc);

        editStudentBtn.addActionListener(e -> onEditStudentName());

        // GUI aufbauen
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Titel:"), gbc);
        gbc.gridx = 1; panel.add(titelField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Note (0-100):"), gbc);
        gbc.gridx = 1; panel.add(noteField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Abgabedatum (yyyyMMdd):"), gbc);
        gbc.gridx = 1; panel.add(datumField, gbc);

        // Separator zwischen Projekt- und Studentenfeldern
        gbc.gridx = 0;
        gbc.gridy = 3; // oder die nächste freie Zeile nach dem letzten Projektfeld!
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);
        gbc.fill = GridBagConstraints.NONE; // Zurücksetzen!

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 12; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        getContentPane().add(panel, BorderLayout.CENTER);

        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> onCancel());
    }

    /**
     * Ändert den Namen des ausgewählten Studenten.
     * Name-Feld muss nicht leer sein.
     */
    private void onEditStudentName() {
        int selectedIndex = studentListView.getSelectedIndex();
        String newName = editStudentNameField.getText().trim();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Kein Student ausgewählt!");
            return;
        }
        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Der neue Name darf nicht leer sein!");
            return;
        }
        // Matrikelnummer extrahieren wie zuvor:
        String selectedValue = studentListModel.getElementAt(selectedIndex);
        String mat = selectedValue.replaceAll(".*\\(([^)]+)\\)$", "$1");

        Student toEdit = null;
        for (Student s : projekt.getTeilnehmer()) {
            if (s.getMatrikelnummer().equals(mat)) {
                toEdit = s;
                break;
            }
        }
        if (toEdit != null) {
            try {
                toEdit.setName(newName);
                updateStudentListModel();
                editStudentNameField.setText("");
            } catch (exception.EmptyNameException ex) {
                JOptionPane.showMessageDialog(this, "Name darf nicht leer sein: " + ex.getMessage());
            }
            updateStudentListModel();
            editStudentNameField.setText("");
        }
    }


    /**
     * Entfernt den ausgewählten Studenten aus dem Projekt.
     */
    private void onRemoveStudent() {
        int selectedIndex = studentListView.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, "Kein Student ausgewählt!");
            return;
        }
        String selectedValue = studentListModel.getElementAt(selectedIndex);

        // Extrahiere Matrikelnummer aus Anzeige: "Name (Matrikelnummer)"
        String mat = selectedValue.replaceAll(".*\\(([^)]+)\\)$", "$1");

        Student toRemove = null;
        for (Student s : projekt.getTeilnehmer()) {
            if (s.getMatrikelnummer().equals(mat)) {
                toRemove = s;
                break;
            }
        }
        if (toRemove != null) {
            projekt.removeStudent(toRemove);
            updateStudentListModel();
        }
    }

    /**
     * Fügt einen neuen Studenten zum Projekt hinzu, falls gültig.
     * Wird durch Klick auf den Button ausgelöst.
     */
    private void onAddStudent() {
        String name = studentNameField.getText().trim();
        String birth = studentBirthField.getText().trim();
        String matrikel = studentMatField.getText().trim();
        try {
            // Geburtsdatum validieren
            dateValidator.validate(birth);

            if (name.isEmpty() || birth.isEmpty() || matrikel.isEmpty()) {
                throw new IllegalArgumentException("Alle Felder müssen ausgefüllt sein!");
            }

            // Matrikelnummer-Eindeutigkeit prüfen (binär durchsuchen, da Tree nach Matrikelnummer sortiert)
            for (Student s : ProjektFilterUtil.getAllStudents(projectList)) {
                if (s.getMatrikelnummer().equals(matrikel)) {
                    throw new DuplicatedMatrikelnummerException(
                            "Die Matrikelnummer \"" + matrikel + "\" ist bereits vergeben!");
                }
            }

            Student s = new Student(name, birth, matrikel);

            projekt.addStudent(s);
            updateStudentListModel();

            // Eingabefelder leeren
            studentNameField.setText("");
            studentBirthField.setText("");
            studentMatField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe: " + ex.getMessage());
        }
    }

    /**
     * Aktualisiert die JList, sodass alle aktuellen Studenten angezeigt werden.
     */
    private void updateStudentListModel() {
        studentListModel.clear();
        for (Student s : projekt.getTeilnehmer()) {
            studentListModel.addElement(s.getName() + " (" + s.getMatrikelnummer() + ")");
        }
    }


    private void onOk() {
        String titel = titelField.getText().trim();
        String noteText = noteField.getText().trim();
        String datumText = datumField.getText().trim();
        double note;
        try {
            // Prüfen ob der Titel bereits von einem anderen Projekt vergeben ist
            for (Projekt p : projectList) {
                if (p != projekt && p.getTitel() != null && !p.getTitel().isEmpty() &&
                        p.getTitel().equalsIgnoreCase(titel)) {
                    throw new DuplicatedNameException("Der Projekttitel \"" + titel + "\" ist bereits vergeben!");
                }
            }

            projekt.setTitel(titel);
            note = Double.parseDouble(noteText);
            projekt.setNote(note);
            projekt.setAbgabeDatum(datumText);
            // PRÜFUNG: Steht in einem Studentenfeld noch Text? Und soll dieser behalten werden?
            if (!studentNameField.getText().trim().isEmpty() ||
                    !studentBirthField.getText().trim().isEmpty() ||
                    !studentMatField.getText().trim().isEmpty()) {
                int response = JOptionPane.showConfirmDialog(
                        this,
                        "Im Studenten-Eintragsfeld steht noch etwas. Soll dieser Student noch hinzugefügt werden?",
                        "Student hinzufügen?",
                        JOptionPane.YES_NO_OPTION
                );
                if (response == JOptionPane.YES_OPTION) {
                    onAddStudent();
                }
            }

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

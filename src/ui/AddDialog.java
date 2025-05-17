package ui;

import model.Projekt;
import model.Student;
import datastructure.EigeneListe;
import datastructure.BinarySearchTree;
import util.DateValidator;
import util.ProjektFilterUtil;
import exception.EmptyNameException;
import exception.ValidationException;
import exception.DuplicatedNameException;
import exception.DuplicatedMatrikelnummerException;
import util.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;

/**
 * Dialog zum Hinzufügen eines neuen Projekts mit beliebig vielen Studenten (per BinarySearchTree).
 */
public class AddDialog extends JDialog {
    private boolean confirmed = false;

    // Validator Datum
    private final Validator<String> dateValidator = new DateValidator();

    // Projektfelder
    private final JTextField titelField = new JTextField(20);
    private final JTextField noteField = new JTextField(5);
    private final JTextField datumField = new JTextField(10);

    // Studentenfelder
    private final JTextField studentNameField = new JTextField(12);
    private final JTextField studentBirthField = new JTextField(10); // yyyy-MM-dd
    private final JTextField studentMatField = new JTextField(8);
    private final DefaultListModel<String> studentListModel = new DefaultListModel<>();
    private final JList<String> studentListView = new JList<>(studentListModel);
    private final BinarySearchTree<Student> tempStudents =
            new BinarySearchTree<>(Comparator.comparing(Student::getMatrikelnummer));

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

        // Projektdaten
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Titel:"), gbc);
        gbc.gridx = 1; panel.add(titelField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Note (0-100):"), gbc);
        gbc.gridx = 1; panel.add(noteField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Abgabedatum (yyyyMMdd):"), gbc);
        gbc.gridx = 1; panel.add(datumField, gbc);

        // Separator
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);
        gbc.fill = GridBagConstraints.NONE; // Zurücksetzen!
        gbc.gridwidth = 1;

        // Studentenfelder
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1; panel.add(studentNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Geburtsdatum (yyyyMMdd):"), gbc);
        gbc.gridx = 1; panel.add(studentBirthField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Matrikelnummer:"), gbc);
        gbc.gridx = 1; panel.add(studentMatField, gbc);

        JButton addStudentBtn = new JButton("Student hinzufügen");
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; panel.add(addStudentBtn, gbc);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        JScrollPane studentScroll = new JScrollPane(studentListView);
        studentScroll.setPreferredSize(new Dimension(220, 80));
        panel.add(studentScroll, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Abbrechen");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        getContentPane().add(panel, BorderLayout.CENTER);

        addStudentBtn.addActionListener(e -> onAddStudent(projectList));
        okButton.addActionListener((ActionEvent e) -> onOk(projectList));
        cancelButton.addActionListener((ActionEvent e) -> onCancel());
    }

    private void onAddStudent(EigeneListe<Projekt> projectList) {
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

            for (Student s : tempStudents) {
                if (s.getMatrikelnummer().equals(matrikel)) {
                    throw new DuplicatedMatrikelnummerException(
                            "Die Matrikelnummer \"" + matrikel + "\" ist bereits vergeben!");
                }
            }

            Student student = new Student(name, birth, matrikel);
            tempStudents.add(student);
            studentListModel.addElement(name + " (" + matrikel + ")");
            studentNameField.setText(""); studentBirthField.setText(""); studentMatField.setText("");
        } catch (DuplicatedMatrikelnummerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe: " + ex.getMessage());
        }
    }

    private void onOk(EigeneListe<Projekt> projectList) {
        String titel = titelField.getText().trim();
        String noteText = noteField.getText().trim();
        String datumText = datumField.getText().trim();
        double note;

        // PRÜFUNG: Steht in einem Studentenfeld noch Text?
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
                onAddStudent(projectList);
            }
        }

        try {
            // Prüfen, ob der Titel nicht leer ist (leere Projekte ausnehmen)
            if (titel.isEmpty()) {
                throw new EmptyNameException();
            }

            // Prüfen, ob der Titel bereits vergeben ist
            for (Projekt p : projectList) {
                // Titel-Vergleich case-insensitive und keine leeren Titel prüfen
                if (p.getTitel() != null && !p.getTitel().isEmpty() &&
                        p.getTitel().equalsIgnoreCase(titel)) {
                    throw new DuplicatedNameException("Der Projekttitel \"" + titel + "\" ist bereits vergeben!");
                }
            }

            // Note validieren
            try {
                note = Double.parseDouble(noteText);
            } catch (NumberFormatException nfe) {
                throw new ValidationException("Note muss eine Zahl sein: " + noteText);
            }

            // Projekt anlegen und hinzufügen
            Projekt projekt = new Projekt(titel, note, datumText);
            for (Student s : tempStudents) {
                projekt.addStudent(s);
            }

            projectList.add(projekt);
            confirmed = true;
            dispose();
        } catch (DuplicatedNameException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (EmptyNameException ex) {
            JOptionPane.showMessageDialog(this, "Projekttitel darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
        } catch (ValidationException ex) {
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

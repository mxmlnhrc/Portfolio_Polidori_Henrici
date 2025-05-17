package ui;

import model.Projekt;
import model.Student;

import javax.swing.*;
import java.awt.*;

public class DetailsDialog extends JDialog {
    private final Projekt projekt;

    /**
     * Zeigt die Projektdetails an (read-only Ansicht).
     * @param parent Übergeordnetes Fenster
     * @param projekt Das anzuzeigende Projekt
     */
    public DetailsDialog(Window parent, Projekt projekt) {
        super(parent, "Projekt-Details", ModalityType.APPLICATION_MODAL);
        this.projekt = projekt;
        initComponents();
        setSize(400, 350);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Projektdaten anzeigen (6.2.1)
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Titel:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(projekt.getTitel() != null ? projekt.getTitel() : ""), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Note:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(projekt.getNote())), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Abgabedatum:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(projekt.getAbgabeDatum() != null ? projekt.getAbgabeDatum() : ""), gbc);

        // Trennlinie
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.gridwidth = 1;

        // Studentenübersicht (6.2.2)
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Studenten:"), gbc);

        DefaultListModel<String> studentenModel = new DefaultListModel<>();
        for (Student s : projekt.getTeilnehmer()) {
            studentenModel.addElement(s.getName() + " (" + s.getMatrikelnummer() + ")");
        }
        JList<String> studentenList = new JList<>(studentenModel);
        studentenList.setEnabled(false);
        JScrollPane studentenScroll = new JScrollPane(studentenList);
        studentenScroll.setPreferredSize(new Dimension(250, 80));
        gbc.gridx = 1;
        panel.add(studentenScroll, gbc);

        // Button-Panel unten (6.2.3/6.2.4)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 0));
        JButton editButton = new JButton("Bearbeiten");
        JButton deleteButton = new JButton("Löschen");
        JButton closeButton = new JButton("Schließen");
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        // Listener
        closeButton.addActionListener(e -> dispose());
        editButton.addActionListener(e -> {
            new EditDialog((Frame) SwingUtilities.getWindowAncestor(this), projekt).setVisible(true);
            // Nach Bearbeitung neu laden (optional)
            dispose();
        });
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Projekt wirklich löschen?", "Löschen", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Projekt muss aus der Hauptliste entfernt werden! (siehe Hinweis unten)
                dispose();
            }
        });

        getContentPane().add(panel, BorderLayout.CENTER);
    }
}
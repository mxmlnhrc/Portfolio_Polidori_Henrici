package ui;

import model.Projekt;
import datastructure.EigeneListe;
import algorithm.SortAlgorithm;
import model.Student;
import algorithm.HeapSort;
import algorithm.MergeSort;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Panel zur Anzeige und Sortierung der Projektliste.
 */
public class ListPanel extends JPanel {
    private final EigeneListe<Projekt> projects;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JComboBox<String> sortCombo;

    // Aktuellen Sortieralgorithmus initialisieren
    private final algorithm.SortAlgorithm<model.Projekt> mergeSort = new algorithm.MergeSort<>();
    private final algorithm.SortAlgorithm<model.Projekt> heapSort = new algorithm.HeapSort<>();
    private algorithm.SortAlgorithm<model.Projekt> currentSort = mergeSort; // Standard

    // Suchfelder anlegen
    private final JTextField searchField = new JTextField(14);
    private final JButton searchBtn = new JButton("Suchen");
    private final JButton resetBtn = new JButton("Zurücksetzen");

    // Filterfelder anlegen
    private final JTextField gradeSearchField = new JTextField(5);
    private final JButton gradeSearchBtn = new JButton("Suche Note");

    /**
     * Konstruktor für das ListPanel.
     * @param projects - Liste der Projekte
     * @param mergeSort - Sortieralgorithmus für MergeSort
     * @param heapSort - Sortieralgorithmus für HeapSort
     */
    public ListPanel(EigeneListe<Projekt> projects,
                     SortAlgorithm<Projekt> mergeSort,
                     SortAlgorithm<Projekt> heapSort) {
        this.projects = projects;
        this.tableModel = new DefaultTableModel(
                new String[]{"Titel","Note","Abgabedatum","Studenten"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.table = new JTable(tableModel);

        this.sortCombo = new JComboBox<>(new String[]{"Titel","Note","Abgabedatum"});
        initUI();
        refresh();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel control = new JPanel();
        control.add(new JLabel("Sortieren nach:"));
        JButton sortAsc = new JButton("Aufsteigend");
        JButton sortDesc = new JButton("Absteigend");
        add(control, BorderLayout.NORTH);

        // Auswahl der Sortieralgorithmen
        JComboBox<String> algoCombo = new JComboBox<>(new String[]{"MergeSort", "HeapSort"});
        algoCombo.addActionListener(e -> {
            if (algoCombo.getSelectedIndex() == 0) currentSort = mergeSort;
            else currentSort = heapSort;
        });

        // Auswahl der Sortierkriterien
       JPanel sortPanel = new JPanel();
       sortPanel.add(new JLabel("Algorithmus:"));
       sortPanel.add(algoCombo);
       sortPanel.add(new JLabel("Kriterium:"));
       sortPanel.add(sortCombo);
       sortPanel.add(sortAsc);
       sortPanel.add(sortDesc);

       sortAsc.addActionListener(e -> sort(true));
       sortDesc.addActionListener(e -> sort(false));

       // topPanel erstellen um Such- und Sortier- und Filterfelder zu kombinieren
       JPanel topPanel = new JPanel();
       topPanel.setLayout(new BorderLayout());
       topPanel.add(sortPanel, BorderLayout.NORTH);

       // Suchfelder und Buttons
       JPanel searchPanel = new JPanel();
       searchPanel.add(new JLabel("Suche Projekt:"));
       searchPanel.add(searchField);
       searchPanel.add(searchBtn);
       searchPanel.add(resetBtn);

       // Hinzufügen der Such-Buttons
       topPanel.add(searchPanel, BorderLayout.SOUTH);

        // Filterfeld für Note
        JPanel gradeSearchPanel = new JPanel();
        gradeSearchPanel.add(new JLabel("Suche nach Note:"));
        gradeSearchPanel.add(gradeSearchField);
        gradeSearchPanel.add(gradeSearchBtn);

        // Einfügen der Notenfilterung unter der Tabelle
        add(gradeSearchPanel, BorderLayout.SOUTH);


        // ActionListener für die Such-Buttons
        searchBtn.addActionListener(e -> performSearch());
        resetBtn.addActionListener(e -> resetSearch());
        searchField.addActionListener(e -> performSearch()); // Enter im Feld

        add(topPanel, BorderLayout.NORTH);

        /**
         * MouseListener für Doppelklick auf die Tabelle
         * um den DetailsDialog zu öffnen.
         */
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    // Das ausgewählte Projekt holen:
                    Projekt selected = getSelectedProjekt();
                    if (selected != null) {
                        // DetailsDialog öffnen
                        showDetailsDialog(selected);
                    }
                }
            }
        });
    }

    /**
     * Binäre Suche nach Projekten mit exakt passender Note.
     * Die Liste muss vorher sortiert sein!
     */
    private void performGradeSearch() {
        String noteText = gradeSearchField.getText().trim();
        if (noteText.isEmpty()) return;
        double target;
        try {
            target = Double.parseDouble(noteText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ungültige Note!");
            return;
        }

        // Liste der Projekte nach Note sortieren
        List<model.Projekt> all = new ArrayList<>();
        for (model.Projekt p : projects) all.add(p);

        // Sortiere mit deinem Algorithmus (z. B. MergeSort)
        mergeSort.sort(all, Comparator.comparingDouble(model.Projekt::getNote));

        // Binäre Suche
        List<model.Projekt> found = new ArrayList<>();
        int left = 0, right = all.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            double midNote = all.get(mid).getNote();
            if (midNote == target) {
                // Finde alle Projekte mit dieser Note (es können mehrere sein)
                // Gehe nach links und rechts von mid weiter
                int i = mid;
                while (i >= 0 && all.get(i).getNote() == target) {
                    found.add(0, all.get(i));
                    i--;
                }
                i = mid + 1;
                while (i < all.size() && all.get(i).getNote() == target) {
                    found.add(all.get(i));
                    i++;
                }
                break;
            } else if (midNote < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // Tabelle anzeigen
        tableModel.setRowCount(0);
        for (model.Projekt p : found) {
            StringBuilder studenten = new StringBuilder();
            for (model.Student s : p.getTeilnehmer()) {
                if (studenten.length() > 0) studenten.append(", ");
                studenten.append(s.getName());
            }
            tableModel.addRow(new Object[]{
                    p.getTitel(), p.getNote(), p.getAbgabeDatum(), studenten.toString()
            });
        }

        if (found.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kein Projekt mit Note " + target + " gefunden.");
        }
    }

    /**
     * Zeigt nur Projekte, deren Titel den Suchbegriff enthalten.
     * (Case-Insensitive)
     */
    private void performSearch() {
        String term = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0); // Tabelle leeren
        for (model.Projekt p : projects) {
            String titel = p.getTitel() != null ? p.getTitel().toLowerCase() : "";
            if (titel.contains(term)) {
                StringBuilder studenten = new StringBuilder();
                for (model.Student s : p.getTeilnehmer()) {
                    if (studenten.length() > 0) studenten.append(", ");
                    studenten.append(s.getName());
                }
                tableModel.addRow(new Object[]{
                        p.getTitel(), p.getNote(), p.getAbgabeDatum(), studenten.toString()
                });
            }
        }
    }

    /**
     * Setzt die Suche zurück und zeigt alle Projekte an.
     */
    private void resetSearch() {
        searchField.setText("");
        refresh(); // Zeigt wieder alle Projekte an
    }


    /**
     * Sortiert die Projekte in aufsteigender oder absteigender Reihenfolge.
     * @param ascending true für aufsteigend, false für absteigend
     */
    private void sort(boolean ascending) {
        String criteria = (String) sortCombo.getSelectedItem();
        Comparator<model.Projekt> comp;

        switch (criteria) {
            case "Note":
                comp = Comparator.comparingDouble(model.Projekt::getNote);
                break;
            case "Abgabedatum":
                comp = Comparator.comparing(model.Projekt::getAbgabeDatum,
                        Comparator.nullsFirst(String::compareTo));
                break;
            case "Titel":
            default:
                comp = Comparator.comparing(model.Projekt::getTitel,
                        Comparator.nullsFirst(String::compareToIgnoreCase));
        }
        if (!ascending) comp = comp.reversed();

        // Projekte aus Baum in List sammeln
        List<model.Projekt> all = new ArrayList<>();
        for (model.Projekt p : projects) all.add(p);

        // Hier NUR eigenen Algorithmus nutzen!
        currentSort.sort(all, comp);

        // Tabelle neu füllen
        tableModel.setRowCount(0);
        for (model.Projekt p : all) {
            StringBuilder studenten = new StringBuilder();
            for (model.Student s : p.getTeilnehmer()) {
                if (studenten.length() > 0) studenten.append(", ");
                studenten.append(s.getName());
            }
            tableModel.addRow(new Object[]{
                    p.getTitel(), p.getNote(), p.getAbgabeDatum(), studenten.toString()
            });
        }
    }


    private List<Projekt> toList() {
        List<Projekt> list = new ArrayList<>();
        for (Projekt p : projects) {
            list.add(p);
        }
        return list;
    }

    /**
     * Aktualisiert die angezeigten Daten in der Tabelle.
     */
    public void refresh() {
        tableModel.setRowCount(0);
        for (Projekt p : projects) {
            // Leere Felder anzeigen
            String titel = p.getTitel() != null ? p.getTitel() : "";
            String note = p.getNote() != 0 ? String.valueOf(p.getNote()) : "";
            String datum = p.getAbgabeDatum() != null ? p.getAbgabeDatum() : "";
            // Studenten zusammenfassen
            StringBuilder studenten = new StringBuilder();
            for (Student s : p.getTeilnehmer()) {
                if (!studenten.isEmpty()) studenten.append(", ");
                studenten.append(s.getName());
            }
            tableModel.addRow(new Object[]{
                    p.getTitel(), p.getNote(), p.getAbgabeDatum(), studenten.toString()
            });
        }
    }

    /**
     * Gibt das aktuell ausgewählte Projekt zurück.
     * @return ausgewähltes Projekt oder null
     */
    public Projekt getSelectedProjekt() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return null;
        }
        return toList().get(row);
    }

    /**
     * Zeigt den DetailsDialog für das Projekt an.
     * @param projekt das Projekt, dessen Details angezeigt werden sollen
     */
    private void showDetailsDialog(Projekt projekt) {
        DetailsDialog dialog = new DetailsDialog(
                SwingUtilities.getWindowAncestor(this), projekt,
                p -> { projects.remove(p); refresh(); }
        );
        dialog.setVisible(true);
        // Prüfen, ob geändert wurde und dann refresh()
        if (dialog.isModified()) {
            refresh();
        }
    }

}

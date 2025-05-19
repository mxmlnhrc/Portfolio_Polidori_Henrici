package ui;

import model.Projekt;
import datastructure.EigeneListe;
import algorithm.SortAlgorithm;
import model.Student;
import util.ProjektFilterUtil;

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
    private final JTextField gradeFilterField = new JTextField(5);
    private final JButton gradeFilterBtn = new JButton("Filtern");
    private final JComboBox<String> filterCombo;

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

        // Auswahl Sortierkriterien
        this.sortCombo = new JComboBox<>(new String[]{"Titel","Note","Abgabedatum"});

        // Auswahl Filterkriterien
        this.filterCombo = new JComboBox<>(new String[]{"Exakt","Mindestens","Maximal"});
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

       // topPanel erstellen um Such- und Sortier-, Filter- und Suchfelder zu kombinieren
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
       topPanel.add(searchPanel, BorderLayout.CENTER);

        // Filterfeld für Note
        JPanel filterGradePanel = new JPanel();
        filterGradePanel.add(new JLabel("Suche nach Note:"));
        filterGradePanel.add(gradeFilterField);
        filterGradePanel.add(filterCombo);
        filterGradePanel.add(gradeFilterBtn);

        // Einfügen der Notenfilterung unter der Tabelle
        add(filterGradePanel, BorderLayout.SOUTH);


        // ActionListener für die Such-Buttons
        searchBtn.addActionListener(e -> performSearch());
        resetBtn.addActionListener(e -> resetSearch());
        searchField.addActionListener(e -> performSearch()); // Enter im Feld

        // ActionListener für den Noten-Filter
        gradeFilterBtn.addActionListener(e -> filterByGrade());
        gradeFilterField.addActionListener(e -> filterByGrade()); // Enter

        add(topPanel, BorderLayout.NORTH);

        /*
          MouseListener für Doppelklick auf die Tabelle
          um den DetailsDialog zu öffnen.
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
    private void filterByGrade() {
        String valText = gradeFilterField.getText().trim();
        if (valText.isEmpty()) return;

        double value;
        try {
            value = Double.parseDouble(valText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ungültige Eingabe!");
            return;
        }

        String criteria = (String) filterCombo.getSelectedItem();
        tableModel.setRowCount(0);
        List<model.Projekt> found = new ArrayList<>();

        if ("Exakt".equals(criteria)) {
            // Binäre Suche auf sortierter Liste
            List<model.Projekt> all = new ArrayList<>();
            for (model.Projekt p : projects) all.add(p);
            // Sortiere nach Note
            mergeSort.sort(all, Comparator.comparingDouble(model.Projekt::getNote));

            // Binäre Suche nach passender Note (siehe vorherige Antwort)
            int left = 0, right = all.size() - 1;
            while (left <= right) {
                int mid = (left + right) / 2;
                double midNote = all.get(mid).getNote();
                if (midNote == value) {
                    int i = mid;
                    while (i >= 0 && all.get(i).getNote() == value) {
                        found.add(0, all.get(i));
                        i--;
                    }
                    i = mid + 1;
                    while (i < all.size() && all.get(i).getNote() == value) {
                        found.add(all.get(i));
                        i++;
                    }
                    break;
                } else if (midNote < value) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        } else if ("Mindestens".equals(criteria)) {
            for (model.Projekt p : projects) {
                if (p.getNote() >= value) found.add(p);
            }
        } else if ("Maximal".equals(criteria)) {
            for (model.Projekt p : projects) {
                if (p.getNote() <= value) found.add(p);
            }
        }

        // Tabelle befüllen
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
            JOptionPane.showMessageDialog(this, "Kein passendes Projekt gefunden!");
        }
    }


    /**
     * Zeigt nur Projekte, deren Titel den Suchbegriff enthalten.
     * (Case-Insensitive)
     */
    // Perform Search und Filtermethoden sind in ListPanel, da das Umverlagern in eine extra Klasse nicht lohnt.
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
        DetailsDialog dialog = new DetailsDialog(SwingUtilities.getWindowAncestor(this),
                projekt,
                this,
                projects
        );
        dialog.setVisible(true);
        // Prüfen, ob geändert wurde und dann refresh()
        if (dialog.isModified()) {
            refresh();
        }
    }

}

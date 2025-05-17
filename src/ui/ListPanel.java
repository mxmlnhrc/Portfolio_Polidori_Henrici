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

    public ListPanel(EigeneListe<Projekt> projects,
                     SortAlgorithm<Projekt> mergeSort,
                     SortAlgorithm<Projekt> heapSort) {
        this.projects = projects;
//        this.mergeSort = mergeSort;
//        this.heapSort = heapSort;
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
//        control.add(sortCombo);
        JButton sortAsc = new JButton("Aufsteigend");
        JButton sortDesc = new JButton("Absteigend");
//        control.add(sortAsc);
//        control.add(sortDesc);
        add(control, BorderLayout.NORTH);

        // Auswahl der Sortieralgorithmen
        JComboBox<String> algoCombo = new JComboBox<>(new String[]{"MergeSort", "HeapSort"});
        algoCombo.addActionListener(e -> {
            if (algoCombo.getSelectedIndex() == 0) currentSort = mergeSort;
            else currentSort = heapSort;
        });

        JPanel sortPanel = new JPanel();
        sortPanel.add(new JLabel("Algorithmus:"));
        sortPanel.add(algoCombo);
        sortPanel.add(new JLabel("Kriterium:"));
        sortPanel.add(sortCombo);
        sortPanel.add(sortAsc);
        sortPanel.add(sortDesc);

        sortAsc.addActionListener(e -> sort(true));
        sortAsc.addActionListener(e -> sort(false));

        add(sortPanel, BorderLayout.NORTH);

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

        // Projekte aus Tree in List holen
        List<model.Projekt> all = new ArrayList<>();
        for (model.Projekt p : projects) all.add(p);

        // Sortieren mit dem aktuellen Algorithmus
        currentSort.sort(all, comp);

        // Tabelle neu befüllen
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

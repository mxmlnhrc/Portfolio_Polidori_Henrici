package ui;

import model.Projekt;
import datastructure.EigeneListe;
import algorithm.SortAlgorithm;
import model.Student;

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
    private final SortAlgorithm<Projekt> mergeSort;
    private final SortAlgorithm<Projekt> heapSort;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JComboBox<String> sortCombo;

    public ListPanel(EigeneListe<Projekt> projects,
                     SortAlgorithm<Projekt> mergeSort,
                     SortAlgorithm<Projekt> heapSort) {
        this.projects = projects;
        this.mergeSort = mergeSort;
        this.heapSort = heapSort;
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
        control.add(sortCombo);
        JButton sortAsc = new JButton("Aufsteigend");
        JButton sortDesc = new JButton("Absteigend");
        control.add(sortAsc);
        control.add(sortDesc);
        add(control, BorderLayout.NORTH);

        sortAsc.addActionListener(e -> sort(true));
        sortDesc.addActionListener(e -> sort(false));

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
        Comparator<Projekt> comp;
        switch (criteria) {
            case "Note":
                comp = Comparator.comparingDouble(Projekt::getNote);
                break;
            case "Abgabedatum":
                comp = Comparator.comparing(Projekt::getAbgabeDatum);
                break;
            default:
                comp = Comparator.comparing(Projekt::getTitel);
        }
        if (!ascending) comp = comp.reversed();
        // Standardmäßig MergeSort verwenden
        mergeSort.sort(toList(), comp);
        refresh();
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
                p -> { // Das ist das Callback!
                    projects.remove(p);
                    refresh();
                }
        );
        dialog.setVisible(true);
    }
}

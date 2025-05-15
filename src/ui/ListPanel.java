package ui;

import model.Projekt;
import datastructure.EigeneListe;
import algorithm.SortAlgorithm;

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
        this.tableModel = new DefaultTableModel(new String[]{"Titel","Note","Abgabedatum"}, 0) {
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
            tableModel.addRow(new Object[]{p.getTitel(), p.getNote(), p.getAbgabeDatum()});
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
}

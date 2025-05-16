package ui;

import model.Projekt;
import datastructure.EigeneListe;
import algorithm.SortAlgorithm;
import ui.policy.DeletionPolicy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;

/**
 * Hauptfenster der Portfolio-Management-Anwendung.
 */
public class MainFrame extends JFrame {
    private final EigeneListe<Projekt> projectList;
    private final SortAlgorithm<Projekt> mergeSort;
    private final SortAlgorithm<Projekt> heapSort;
    private final DeletionPolicy<Projekt> deletionPolicy;

    private final ListPanel listPanel;

    /**
     * Konstruktor mit notwendiger Abhängigkeiten.
     */
    public MainFrame(EigeneListe<Projekt> projectList,
                     SortAlgorithm<Projekt> mergeSort,
                     SortAlgorithm<Projekt> heapSort,
                     DeletionPolicy<Projekt> deletionPolicy) {
        super("Portfolio Manager");
        this.projectList = projectList;
        this.mergeSort = mergeSort;
        this.heapSort = heapSort;
        this.deletionPolicy = deletionPolicy;

        this.listPanel = new ListPanel(projectList, mergeSort, heapSort);
        initComponents();
    }

    /**
     * Initialisiert UI-Komponenten und Layout.
     */
    private void initComponents() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar());

        add(listPanel, BorderLayout.CENTER);

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Baut die Menüleiste auf.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Datei");

        JMenuItem addItem = new JMenuItem("Hinzufügen");
        addItem.addActionListener(e -> showAddDialog());
        menu.add(addItem);

        JMenuItem deleteItem = new JMenuItem("Löschen");
        deleteItem.addActionListener(e -> showDeleteDialog());
        menu.add(deleteItem);

        menu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Beenden");
        exitItem.addActionListener(e -> onExit());
        menu.add(exitItem);

        menuBar.add(menu);
        return menuBar;
    }

    /**
     * Zeigt das Dialogfenster zum Hinzufügen neuer Projekte an.
     */
    private void showAddDialog() {
        AddDialog dialog = new AddDialog(this, projectList);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            listPanel.refresh();
        }
    }

    /**
     * Zeigt das Passwortdialog vor dem Löschen an.
     */
    private void showDeleteDialog() {
        Projekt selected = listPanel.getSelectedProjekt();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Kein Projekt ausgewählt.", "Fehler", JOptionPane.WARNING_MESSAGE);
            return;
        }
        PasswordDialog pd = new PasswordDialog(this, deletionPolicy, selected);
        pd.setVisible(true);
        if (pd.isDeletionAllowed()) {
            projectList.remove(selected);
            listPanel.refresh();
        }
    }

    /**
     * Beendet die Anwendung nach Rückfrage.
     */
    private void onExit() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Möchten Sie die Anwendung wirklich beenden?",
                "Beenden", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    /**
     * Startpunkt für die Anwendung.
     */
    public static void main(String[] args) {
        // TODO: Dependency-Injection hier konfigurieren
        SwingUtilities.invokeLater(() -> {
            // Beispiel: Projektliste und Algorithmen erstellen
            EigeneListe<Projekt> projects = new datastructure.BinarySearchTree<>(Comparator.comparing(Projekt::getTitel));
            SortAlgorithm<Projekt> merge = new algorithm.MergeSort<>();
            SortAlgorithm<Projekt> heap = new algorithm.HeapSort<>();
            DeletionPolicy<Projekt> policy = new ui.policy.PasswordDeletionPolicy();

            MainFrame frame = new MainFrame(projects, merge, heap, policy);
            frame.setVisible(true);
        });
    }
}

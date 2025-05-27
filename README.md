# Portfolio-Prüfung „Fortgeschrittene Programmierung und Algorithmen und Datenstrukturen“

## Projektbeschreibung
Dieses Desktop-Programm ermöglicht die Verwaltung studentischer Projektarbeiten.  
Jede Projektarbeit besitzt einen Namen, eine Liste von Studierenden, eine Note und ein Abgabedatum. Über eine Java‐Swing-GUI können Projekte hinzugefügt, angezeigt, sortiert, durchsucht und gelöscht werden. Das Löschen ist passwortgeschützt.

## Funktionen
- **Projekt hinzufügen**
- **Projektliste anzeigen** inkl. aller Details
- **Sortierung**
    - Nach Projektname (Alphabet)
    - Nach Note
    - Nach Abgabedatum
- **Löschen von Projekten** (mit Passwort‐Abfrage)
- **Suche**
    - Lineare Suche über Projektnamen
    - Optional: Binäre Suche über Noten
- **Filter** (z. B. nach Note oder Abgabedatum)

## Technische Anforderungen
- Java 8 oder höher
- Swing GUI
- Keine Nutzung von Collections-Bibliotheken für Speicherung und Sortierung (eigene Implementierung erforderlich)

## Projektstruktur
```plaintext
src/
├─ model/
│  ├─ Mensch.java         // Basisklasse für Personen
│  ├─ Student.java        // extends Mensch, mit Matrikelnummer o. Ä.
│  └─ Projekt.java        // referenziert Liste<Student>, Note, Abgabedatum
│
├─ datastructure/
│  ├─ EigeneListe.java    // Interface für Liste-Implementierungen
│  └─ BinarySearchTree.java // konkrete Implementierung (einfacher BinaryTree um Informationen zu speichern)
│
├─ algorithm/
│  ├─ SortAlgorithm.java  // Interface für Sortierstrategien
│  ├─ MergeSort.java      // Divide-and‐Conquer, implementiert SortAlgorithm
│  └─ HeapSort.java       // Selection‐Kategorie, implementiert SortAlgorithm
│
├─ exception/
│  ├─ EmptyNameException.java
│  ├─ DuplicatedNameException.java
│  ├─ InvalidGradeException.java
   ├─ ValidationException.java  // Exception für Validierungsfehler
│  └─ InvalidDateException.java
│
├─ ui/
│  ├─ MainFrame.java      // Startet die GUI
│  ├─ AddDialog.java      // Maske zum Hinzufügen
│  ├─ ListPanel.java      // Anzeige & Sortierung
│  ├─ PasswordDialog.java // Löschen mit Passwort
│  └─ policy/
│     ├─ DeletionPolicy.java        // Interface für Löschregeln
│     └─ PasswordDeletionPolicy.java // implementiert DeletionPolicy für Passwortprüfung
│
└─ util/
   ├─ Validator.java            // Interface für Eingabevalidierung
   ├─ DateValidator.java        // implementiert Validator<String>
   └─ GradeValidator.java       // implementiert Validator<Double>
````

## Klassen & Interfaces
- **Mensch** (abstract)
    - Attribute: `name` (String), `birthDate` (LocalDate)
    - Methoden: Getter/Setter für `name`, nur Getter für `birthDate`
- **Student** extends **Mensch**
    - Zusatz‐Attribut: `matrikelnummer` (String o. Ä.)
- **Projekt**
    - Attribute: `projektName` (String), `students` (EigeneListe<Student>), `grade` (double), `submissionDate` (int im Format JJJJMMTT)
    - Konstruktoren und Setter/Getters zur optionalen Initialisierung
- **IProjektService** (Interface)
    - Methoden zur Verwaltung (add, delete, list, sort, search)

## Datenstruktur
Anstelle von `List`/`Array` kommt eine selbst implementierte **verkettete Liste** (oder ein anderer Datentyp eurer Wahl) zum Einsatz.  
Bereit, die Wahl und Funktionsweise im mündlichen Teil zu erläutern.

## Sortieralgorithmen
- **MergeSort** (Divide-and-Conquer)
  ```java
  // MergeSort: teilt die Liste, sortiert Teillisten und fügt sie zusammen


* **HeapSort** (Selection)

  ```java
  // BubbleSort: baut ein Heap, entfernt sukzessive das Maximum und rekonstruiert das Heap
  ```

*Im Quellcode ist jeder Algorithmus mit seinem Namen kommentiert.*

## Suche

* **Lineare Suche** über `projektName`
* **Optionale Binäre Suche** über `grade` (nur ausführbar, wenn Liste vorher nach Noten sortiert ist)

## Datenvalidierung & Ausnahmebehandlung

Es werden folgende **selbst definierte Exceptions** geworfen und in der GUI abgefangen, um fehlerhafte Eingaben zu verhindern:

* `EmptyNameException` – kein leerer Projekt‐ oder Studentenname
* `DuplicatedNameException` – Name bereits vorhanden (case‐insensitive)
* `InvalidGradeException` – Note außerhalb gültigem Wertebereich
* `InvalidDateException` – Datum unplausibel oder Formatfehler

GUI zeigt bei gefangenen Ausnahmen eine Fehlermeldung und bleibt stabil.

## Kompilieren & Ausführen

```bash
# Kompilieren
javac -d out $(find src -name "*.java")
# Starten
java -cp out ui.MainFrame
```

## Mündliche Prüfung

* **Termin:** voraussichtlich 06.06.2025
* **Inhalt:**

    * Code‐Erklärung
    * Begründung der Design‐Entscheidungen
    * Umgang mit eigenen Algorithmen und Datenstrukturen
* **Gewichtung:** 60 % der Gesamtnote
---
## Autoren
Aurora Polidori & Maximilian Henrici
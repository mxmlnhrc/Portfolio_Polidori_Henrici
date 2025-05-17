# Fahrplan: Projekt-Checkliste

## 1. Exceptions & Utilities

*Grundlagen für sauberes Fehler-Handling und Eingabevalidierung schaffen.*

* [ ] `exception/ValidationException.java` anlegen
* [ ] `exception/EmptyNameException.java` überprüfen
* [ ] `exception/DuplicatedNameException.java` überprüfen
* [ ] `exception/InvalidGradeException.java` überprüfen
* [ ] `exception/InvalidDateException.java` überprüfen
* [ ] `util/Validator.java` implementieren (importiere `exception.ValidationException`)
* [ ] `util/DateValidator.java` implementieren (Validierung von Datum)
* [ ] `util/GradeValidator.java` implementieren (Validierung von Note)

**Grobe Punkte:**

* Festlegen, ob ValidationException checked oder unchecked sein soll
* Einheitliches Format für Fehlermeldungen definieren
* Schnittstelle so gestalten, dass sie später leicht für neue Validatoren erweitert werden kann

## 2. Domain-Model

*Definiere die zentralen Geschäftsobjekte und ihre Validierungslogik.*

* [ ] `model/Mensch.java` erstellen (Attribute: `name`, `birthDate`)
* [ ] `model/Student.java` erstellen (erbt von `Mensch`, ergänzt Matrikelnummer)
* [ ] `model/Projekt.java` erstellen (Felder: `titel`, `List<Student>`, `grade`, `submissionDate`)
* [ ] Validierung im Konstruktor oder in Settern mit `Validator.validate(...)` einbauen

**Grobe Punkte:**

* Nachdenken über Immutability vs. Mutable Entities
* Abhängigkeiten zu util/Validator frühzeitig injizierbar gestalten
* ToString, equals und hashCode für Domain-Objekte definieren

## 3. Datenstruktur

*Implementiere eine eigene Liste für das Speichern von Projekten.*

* [ ] `datastructure/EigeneListe.java` (Interface definieren)
* [ ] `datastructure/SimpleLinkedList.java` (einfache verkettete Liste implementieren)
* [ ] Unit-Test: Hinzufügen, Entfernen, `get()`, `size()` prüfen

**Grobe Punkte:**

* Interface so allgemein halten, dass später weitere Implementierungen möglich sind (z.B. Baum)
* Iterator-Unterstützung für For-Each-Loop
* Fehlerfälle (IndexOutOfBounds, Remove on empty) definieren und testen

## 4. Sortieralgorithmen

*Trenne Sortierstrategien von der Datenstruktur und biete austauschbare Algorithmen.*

* [ ] `algorithm/SortAlgorithm.java` (Interface definieren)
* [ ] `algorithm/MergeSort.java` implementieren
* [ ] `algorithm/HeapSort.java` implementieren
* [ ] Unit-Tests: Sortiere Beispiel-Listen nach Note, Datum, Name

**Grobe Punkte:**

* Generische Implementierung, um auf beliebige Typen und Comparatoren zuzugreifen
* Performance-Messungen in Tests (optional)
* Fehlerbehandlung bei null-Listen oder null-Comparator

## 5. Lösch-Policy

*Kapsle die Löschlogik, um Passwortanforderung modular zu halten.*

* [ ] `ui/policy/DeletionPolicy.java` (Interface definieren)
* [ ] `ui/policy/PasswordDeletionPolicy.java` implementieren
* [ ] Unit-Test: Passwortprüfung korrekt?

**Grobe Punkte:**

* Vorgabe des Passworts über Konfiguration oder UI-Eingabe
* Erlauben später weiterer Policies (Zeitfenster, Rollen)
* Fehlermeldung und Anzahl Fehlversuche handhaben

## 6. GUI-Skelett

*Baue eine einfache Swing-Oberfläche und verknüpfe erste Logikkomponenten.*

* [ ] `ui/MainFrame.java` erstellen (Hauptfenster mit Menü)
* [ ] `ui/AddDialog.java` erstellen (Formular zum Hinzufügen)
* [ ] `ui/ListPanel.java` erstellen (Anzeige & Sortierung der Liste)
* [ ] `ui/PasswordDialog.java` erstellen (Passwort-Eingabe für Deletion)
* [ ] Komponenten verbinden: Liste, SortAlgorithm, DeletionPolicy injizieren

**Grobe Punkte:**

* Grundlegendes Layout (BorderLayout, Panels) planen
* MVC-Pattern oder MVP für klare Trennung GUI/Logik
* Demo-Daten einfügen, um UI-Tests zu erleichtern

## 7. Dokumentation

*Sorge für Qualitätssicherung und Laufzeit-Informationen.*

* [ ] Unit-Tests für alle Core-Klassen schreiben
* [ ] README.md final prüfen und ggf. anpassen
* [ ] JavaDoc-Kommentare ergänzen
* [ ] Projekt kompilieren und manuell testen

**Grobe Punkte:**

* Test-Framework (JUnit 5) konfigurieren
* Code Coverage messen (optional)
* Template für Release-Notes oder Changelog anlegen

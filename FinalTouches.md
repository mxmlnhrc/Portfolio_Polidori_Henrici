# Abschlussarbeiten & Feinschliff für Projektverwaltung

---

### **1. Exception-Handling (Validierung & Eindeutigkeitsprüfungen)**

* [x] **Projekttitel-Eindeutigkeit:**

    * Beim Erstellen eines neuen Projekts prüfen, ob der Titel (außer bei leeren Projekten) bereits in der Projektliste existiert.
    * Falls ja, `DuplicateNameException` werfen.

* [x] **Studenten-Matrikelnummer-Eindeutigkeit:**

    * Beim Hinzufügen eines Studenten prüfen, ob die Matrikelnummer bereits im Projekt (oder global, je nach Logik) vergeben wurde.
    * Falls ja, `DuplicateMatrikelnummerException` werfen.

* [x] **Leerer Projekttitel:**

    * Wird ein Projekt ohne Titel erstellt, `EmptyNameException` werfen.
    * Ausnahme: Vollständig leere Projekte (alle Felder leer) sind erlaubt.

* [x] **Ungültige Note:**

    * Wenn die Note außerhalb des zulässigen Bereichs liegt (z. B. <0 oder >100, je nach Regel), `InvalidGradeException` werfen.

* [x] **Ungültiges Geburtsdatum:**

    * Beim Hinzufügen eines Studenten: Falls das Datum nicht interpretierbar ist oder in der Zukunft liegt, `InvalidDateException` werfen.

---

### **2. Passwortabfrage vor Löschen**
* [x] **Passwortabfrage vor dem Löschen eines Projekts aus der DetailedView**

    * Wenn ein Projekt über den DetailDialog gelöscht wird, muss eine Passwortabfrage erfolgen
    Vor dem eigentlichen Löschen wird ein Passwortdialog geöffnet (PasswordDialog oder ähnliches, wie du ihn auch beim anderen Löschen verwendest).
    Nur wenn das richtige Passwort eingegeben wird, wird das Projekt tatsächlich gelöscht.
    Bei falschem Passwort oder Abbruch: Projekt bleibt erhalten, keine weitere Aktion.

---

## **Umsetzungsideen/Notizen:**

* **Exceptions:**

    * Definiere eigene Exception-Klassen wie `DuplicateNameException`, `DuplicateMatrikelnummerException`, usw. im `exception/`-Package.
    * Prüfe auf Eindeutigkeit direkt beim Anlegen/Hinzufügen (im jeweiligen Model oder Service).
    * Fange Exceptions in den Dialogen und zeige eine sinnvolle Fehlermeldung per `JOptionPane`.

* **Filter-Klasse:**

    * Beispiel-Signatur:

      ```java
      public class ProjektFilterUtil {
          public static List<Projekt> filterByTitel(Collection<Projekt> alle, String suchbegriff) { … }
          public static List<Projekt> filterByNoteExact(Collection<Projekt> alle, double note) { … }
          public static List<Projekt> filterByNoteMin(Collection<Projekt> alle, double minNote) { … }
          public static List<Projekt> filterByNoteMax(Collection<Projekt> alle, double maxNote) { … }
          // … ggf. weitere Filter
      }
      ```
    * Die ListView holt die Ergebnisse und baut damit das TableModel neu auf.
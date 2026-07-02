# JpanelBackground

Eine schlanke Swing-Bibliothek, die es ermöglicht, beliebigen `JFrame`-Fenstern ein skalierbares Hintergrundbild zu geben. Das Bild wird bei jeder Größenänderung des Fensters automatisch neu berechnet.

---

## Voraussetzungen

- Java 11 oder neuer
- Maven (zum Bauen)

---

## Bauen & Installieren

```bash
git clone https://github.com/tstueker/JpanelBackground.git
cd JpanelBackground
mvn install
```

Das JAR landet danach im lokalen Maven-Repository und kann als Abhängigkeit eingebunden werden.

---

## Einbindung ins Projekt

### Maven

Nach `mvn install` folgende Abhängigkeit in die `pom.xml` des Zielprojekts eintragen:

```xml
<dependency>
    <groupId>io.github.thorstenstueker</groupId>
    <artifactId>jpanel-background</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Manuell (JAR)

Das JAR mit `mvn package` bauen:

```bash
mvn package
```

Das erzeugte `target/jpanel-background-1.0.0.jar` in den Classpath des Projekts kopieren (z. B. in einen `lib/`-Ordner) und dort einbinden.

---

## Verwendung

### Import

```java
import de.tstu.swing.background.BackgroundPanel;
import de.tstu.swing.background.ScaleMode;
import de.tstu.swing.background.JFrameBackground;
```

---

### Variante 1 – BackgroundPanel als ContentPane (empfohlen für neue Fenster)

`BackgroundPanel` ist eine direkte Unterklasse von `JPanel` und kann als ContentPane eines `JFrame` gesetzt werden. Alle Komponenten werden wie gewohnt hinzugefügt.

```java
JFrame frame = new JFrame("Mein Fenster");
frame.setSize(800, 600);

BackgroundPanel bgPanel = new BackgroundPanel("pfad/zum/bild.jpg", ScaleMode.FILL);
bgPanel.setLayout(new BorderLayout());   // beliebiger Layout-Manager
frame.setContentPane(bgPanel);

// Komponenten ganz normal hinzufügen:
bgPanel.add(new JButton("OK"), BorderLayout.SOUTH);

frame.setVisible(true);
```

Bildquellen die der Konstruktor akzeptiert:

| Typ | Beispiel |
|---|---|
| Dateipfad (String) | `new BackgroundPanel("bild.jpg", ScaleMode.FIT)` |
| `java.io.File` | `new BackgroundPanel(new File("bild.jpg"), ScaleMode.FIT)` |
| `java.net.URL` | `new BackgroundPanel(getClass().getResource("/bild.jpg"), ScaleMode.FIT)` |
| `java.io.InputStream` | `new BackgroundPanel(stream, ScaleMode.FIT)` |
| `BufferedImage` | `new BackgroundPanel(myImage, ScaleMode.FIT)` |

---

### Variante 2 – Nachträgliches Einbinden in einen bestehenden JFrame

`JFrameBackground.attach()` tauscht die ContentPane aus und übernimmt dabei automatisch alle bereits vorhandenen Kindkomponenten und den Layout-Manager.

```java
JFrame frame = new JFrame("Bestehendes Fenster");
frame.setLayout(new BorderLayout());
frame.add(new JLabel("Hallo"), BorderLayout.CENTER);
frame.add(new JButton("OK"), BorderLayout.SOUTH);
frame.setSize(800, 600);

// Hintergrundbild nachträglich hinzufügen – Komponenten bleiben erhalten:
JFrameBackground.attach(frame, "pfad/zum/bild.jpg", ScaleMode.FIT);

frame.setVisible(true);
```

`attach()` gibt das erzeugte `BackgroundPanel` zurück, falls danach noch Komponenten hinzugefügt werden sollen.

---

### Bild zur Laufzeit wechseln

```java
bgPanel.setImage("neues_bild.png");
bgPanel.setImage(new File("neues_bild.png"));
bgPanel.setImage(bufferedImage);
```

### Skalierungsmodus zur Laufzeit wechseln

```java
bgPanel.setScaleMode(ScaleMode.STRETCH);
```

---

## Skalierungsmodi

| `ScaleMode` | Verhalten |
|---|---|
| `FILL` | Füllt den gesamten Bereich aus, Seitenverhältnis bleibt erhalten, Bildränder werden ggf. abgeschnitten |
| `FIT` | Bild vollständig sichtbar, Seitenverhältnis bleibt erhalten, freie Flächen bleiben transparent (Letterbox/Pillarbox) |
| `STRETCH` | Verzerrt das Bild auf die exakte Fenstergröße, Seitenverhältnis wird ignoriert |
| `CENTER` | Originalgröße, zentriert, kein Skalieren, überstehende Teile werden abgeschnitten |
| `TILE` | Bild in Originalgröße als Kachelmuster wiederholt |

---

## Tipps

**Bild aus dem Classpath laden (empfohlen für eingebettete Ressourcen):**

```java
URL url = MyApp.class.getResource("/images/background.jpg");
BackgroundPanel bg = new BackgroundPanel(url, ScaleMode.FILL);
```

**Durchsichtige Kindkomponenten:** Damit das Hintergrundbild durch Panels hindurchscheint, die über dem `BackgroundPanel` liegen, muss deren Opazität deaktiviert werden:

```java
JPanel inner = new JPanel();
inner.setOpaque(false);
bgPanel.add(inner);
```

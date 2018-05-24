# Data
- package classes
- extends - 

## Inhalt
Diese Klasse dient zur Verwaltung der Daten. Daten können mit Hilfe dieser Klasse geschrieben und gelesen werden.
Darüber hinaus enthählt sie wichtige Informationen darüber, wie und wo die Daten gespeichert werden.

### - <ins>DIR</ins> : String
Dieses Attribut enthält den Pfad, in welchem das Programm seine Daten ablegt. Es ist auf __data__ gesetzt.

---

### - <ins>CHIP_DIR</ins> : String
Dieses Attribut enthält das Verzeichnis, in dem die einzelnen Chips gespeichert werden. Wichtig: Es ist ein Unterverzeichnis von __data__.
Daher muss es immer in der folgenden Kombination auftreten: <pre>DIR + "/" + CHIP_DIR</pre>

---

### + <ins>writeChip(chipToWrite : Chip)</ins> : void
Diese Methode schreibt Chips in eine Datei. Dabei entscheidet diese selbst, wo die Daten letztendlich hin geschrieben werden. Für das Schreiben an sich, beruft sich __writeChip__ auf den __HellwegPrintWriter__.

**Param:** Chip, welcher gespeichert werden soll. Dieser darf nicht (!) null sein. <br/>
**Throws:** IOException

Verwendung (beispielsweise):

	try {
		Data.writeChip(chips);
	} catch(IOException ioe) {
		// Tue etwas..
	}

---

### + <ins>readChips()</ins> : List&lt;Chip&gt;
Diese Methode liest alle Dateien bzw. Chips aus dem __CHIP_DIR__. Dabei beruft sich diese Methode auf den __HellwegBufferedReader__. Es werden keine null-Chips zurückgegeben. 

**Returns:** Eine Liste aller Chips, die gespeichert wurden.<br/>
**Throws:** IOException

Verwendung (beispielsweise):

	try {
		chips = Data.readChips();
	} catch(IOException ioe) {
		// Tue etwas..
	}

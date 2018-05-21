# HellwegBufferedReader
- package classes.io
- extends java.io.BufferedReader

## Inhalt
Diese Readerklasse ist extra auf dieses Projekt zugeschnitten. Sie besitzt Methoden, um Strukturen (z.B. Chips) des Projektes 
direkt lesen zu können. Da der _HellwegBufferedReader_ eine Unterklasse des BufferedReader ist, stehen alle Methoden des BufferedReader 
zur Verfügung.
### + readChip() : Chip
Diese Methode liest einen Chip aus. Sie ist an die Methode _readLine_ angelehnt und kann daher auch so verwendet werden. Im Endeffekt
wird _readLine_ verwendet. Dabei wird jede zeile nach XML-Tags und deren Inhalt durchsucht. So wird ein Chip erzeugt.

**Returns:** Es wird ein Chip zurückgegeben oder null (wenn kein Chip erzeugt werden konnte oder der Stream zu Ende ist).

Nutzung (beispielsweise):

	Chip chip;
	while((chip = hbr.readChip()) != null) {
		// ...
	}

---

### - getContent(line : String, tagName : String) : String
Diese Methode wird von _readChip_ verwendet und gibt den String zwischen &lt;tag&gt; und &lt;/tag&gt; wieder. Von außerhalb ist diese
Methode nicht sichtbar.

# HellwegPrintWriter
- package classes.io
- extents java.io.printWriter

## Inhalt
Diese Writerklasse ist extra auf das Projekt zugeschnitten. Sie besitzt Methoden, um Strukturen (z.B. Chips) des Projekts direkt
lesen zu können. Da _HellwegPrintWriter_ eine Unterklasse des PrintWriter ist, stehen alle Methoden des PrintWriter zur Verfügung.

### + print(chipToPrint : Chip) : void
Diese Methode schreibt einen Chip in eine Datei. Es wird _print_ und _println_ aus der Oberklasse genutzt. Einrückungen werden 
mit \t (Tab) realisiert. Die Ausgabe in eine Datei erfolgt im XML-Format.

**Param:** Ein Chip, welcher gespeichert werden soll. Dieser darf nicht null sein.

Nutzung (beispielsweise):

	hpw.print(chip);
	hpw.print(new Chip("id123deFaulT", "Daniel Jackson"));

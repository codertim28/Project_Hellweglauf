# Data
- package classes
- extends - 

## Inhalt
Diese Klasse dient zur Verwaltung der Daten. Daten können mit Hilfe dieser Klasse geschrieben und gelesen werden.
Darüber hinaus enthählt sie wichtige Informationen darüber, wie und wo die Daten gespeichert werden.

### - <ins>DIR</ins> : String
Dieses Attribut enthält den Pfad, in welchem das Programm seine Daten ablegt. Es ist auf __data__ gesetzt.

### - <ins>CHIP_DIR</ins> : String
Dieses Attribut enthält das Verzeichnis, in dem die einzelnen Chips gespeichert werden. Wichtig: Es ist ein Unterverzeichnis von __data__.
Daher muss es immer in der folgenden Kombination auftreten: <pre>DIR + "/" + CHIP_DIR</pre>

# ChipsController
- package classes.controller
- extends - 

## Inhalt
Diese Klasse ist ein Container für alle Chips. Aus diesem Grund stellt der `ChipsController` verschiedene Methoden zur verfügen, 
welche zur Verwaltung der Chips dienen. Außerem ist dieser einem Wettkampfobjekt untergordnet. 

### + addLap(chipId : String) : int
Diese Methode fügt eine neue Runde einem (gescannten) Chip hinzu. Dabei wird überprüft, ob ein "Doppelscan" vorliegt oder nicht.
Liegt ein "Doppelscan" vor, wird keine neue Runde hinzugefügt. Ein Doppelscan ist tritt ein, wenn ein Chip zweimal (oder öfter) 
innerhalb von 10 Sekunden gescannt wird (bzw. diesem eine Runde hinzugefügt werden soll).

**Param:** 
- chipId: Die ID eines (eben gescannten) Chips

**Returns:**
- 0: Runde konnte erfolgreich hinzugefügt werden.
- -1: Doppelscan
- -2: Sonstiger Fehler (z.B. wenn ein Chip nicht gefunden werden kann)

---

### + getChipById(chipId : String) : Chip
Diese Methode findet einen Chip in der Liste des ChipControllers. Sie wird u.a. von der `addLap(...)` verwendet.

**Param:**
- chipId: Die Id des zu suchenden Chips

**Returns:** Den Chip mit der entsprechenden ID oder `null`.

---

## Siehe auch:

[Chip](https://github.com/tpoerschke/Project_Hellweglauf/blob/dev/doc/Chip.java.md)

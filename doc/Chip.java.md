# Chip
- package classes.model
- extends - 

## Inhalt
Diese Klasse repräsentiert einen Chip, welcher jedem Schüler zugeteilt wird. Somit sind die Chips zentraler Bestandteil, damit die
Software überhaupt arbeiten kann.

### - <ins>LAPCOUNT_START</ins> : int
Gibt an, wo mit dem "Rundenzählen" begonnen wird. -2, weil -1 die "Registrierungsrunde" ist.

---

### + Chip() : void
Dieser Konstruktor wird vom "HellwegBufferedReader" verwendet. Sollte sonst nicht (!) verwendet werden, denn es werden 
dummy Attribute vergeben.

**Param:** void

---

### + Chip(id : String, studentName : String, form : String) : void
Dieser Konstruktor sollte hauptsächlich verwendet werden. Dieser initialisiert die JavaFX-Properties und andere
Eigenschaften des Chips.

**Param:** 
- id: Die ID des Chips
- studentName: Der Name des zugeordneten Schülers
- form: Die Klasse des Schülers

Verwendung (beispielsweise):

```
Chip chip = new Chip("default1234", "Max Mustermann", "9b");
```

---

### + getLapCount() : int
Diese Methode gibt die Rundenanzahl des Chips zurück. An dieser Stelle kann nicht it der Länge der internen
`LinkedList<Lap>` gearbeitet werden, da die erste Runde für die "Reservierungsrunde" (-1) reserviert ist. Somit hat 
diese Liste bereits eine Länge von 1, wenn noch gar kein Wettkampf gestartet wurde. 

**Returns:** Die Anzahl an Runden des Chips (bzw. die Nummer der zuletzt hinzugefügten Runde).

Verwendung (beispielsweise):

```
if(c.getLapCount() < 5) {
  // Do Something
}
```

---

## Siehe auch:

[ChipsController](https://github.com/tpoerschke/Project_Hellweglauf/blob/dev/doc/ChipsController.java.md)

# How to contribute
I'm really glad you're reading this, because I would like to involve volunteer developers to help this project get ahead.

## Submitting Changes
Please send a GitHub Pull Request to me with a clear list of what you've done. Please follow the coding conventions (below)
and make sure all of your commits contain one feature (-> one feature per commit).

```
$ git commit -m "A brief summary of the commit
> 
> A paragraph describing what changed and its impact."
```
## Coding conventions
- I indent using tabs.
- I pursue the idea of the strong view (so you can put logic into the view).
- I **always** put spaces after list items and method parameters 
(`[1, 2, 3]`, not `[1,2,3]`), around operators (`x += 1`, not `x+=1`), and so on.
- I comment in german (but that is not necessary).

Please read my code, before contributing. E.g.:

```
/**
 * Fügt eine Runde hinzu (z.B. in einem Wettkampf)
 * @param chipId Der Chip, dem die Runde hinzugefügt werden soll
 * @return 0: Erfolgreich, -1: Doppelscan (Fehler), -2: allg. Fehler
 */
public int addLap(String chipId) {
  try {
    Chip c = getChipById(chipId);
    Lap lastLap = c.getLaps().getLast();

    // Diese Abfrage verhindert einen "Doppelscan".
    // Zwischen jedem Scan müssen 10 Sekunden vergangen sein.
    if(SECONDS.between(lastLap.getTimestamp(), LocalTime.now()) >= 10) {
      // Runde einhängen
      c.getLaps().add(new Lap(LocalTime.now(), c.getLapCount() + 1));
    } else {
      throw new Exception("Doppescan");
    }
  } catch(Exception ex) {
    if(ex.getMessage().equals("Doppescan")) {
      return -1;
    }
    // Null-Pointer-Exception
    return -2;
  }
  return 0;
}
```

Thanks,<br/>
Tim

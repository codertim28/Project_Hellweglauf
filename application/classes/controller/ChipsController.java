package classes.controller;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;

import classes.Data;
import classes.model.Chip;
import classes.model.Lap;
import classes.repository.ChipRepository;

public class ChipsController {
	
	private ArrayList<Chip> chips;
	private ChipRepository repository;
	
	// Dieser Konstruktor wird in den Einstellungen verwendet,
	// um neue Chips einzutragen.
	public ChipsController() {
		this(Data.DIR + "/" + Data.BASIC_DIR + "/" + Data.CHIPS_FILE);
	}
	
	public ChipsController(String path) {
		this(new ChipRepository(path));
	}
	
	public ChipsController(ChipRepository repository) {
		setChips(new ArrayList<Chip>());
		setRepository(repository);
	}
	
	/**
	 * Fügt eine Runde hinzu (z.B. in einem Wettkampf)
	 * @param chipId Der Chip, dem die Runde hinzugefügt werden soll
	 * @return 0: Erfolgreich, -1: Doppelscan (Fehler), -2: allg. Fehler
	 */
	public int addLap(String chipId) {
		try {
			Chip c = getChipById(chipId);
			
			// Diese Abfrage verhindert einen "Doppelscan".
			// Zwischen jedem Scan müssen 10 Sekunden vergangen sein.
			// ODER Nicht "Doppelscan" werfen, wenn noch keine Runde 
			// vorhanden ist oder nur Runde -1. Die Abfrage oben tut dies nämlich.
			if(c.getLapCount() <= (Chip.LAPCOUNT_START + 1) ||
					SECONDS.between(c.getLaps().getLast().getTimestamp(), LocalTime.now()) >= 10) {
				// Runde einhängen
				c.getLaps().add(new Lap(LocalTime.now(), c.getLapCount() + 1));
			}
			else {
				throw new Exception("Doppelscan");
			}
		} catch(Exception ex) {
			if("Doppelscan".equals(ex.getMessage())) {
				return -1;
			}
			// z.B. Null-Pointer-Exception
			return -2;
		}
		return 0;
	}
	
	/**
	 * Setzt die Runden jedes Chips zurück.
	 */
	public void resetLaps() {
		for(int i = 0; i < chips.size(); i++) {
			chips.get(i).setLaps(new LinkedList<Lap>());
		}
	}
	
	/**
	 * Schreibt alle Chips in eine Datei.
	 */
	public void save() {
		// Nicht speichern, wenn nichts geladen ist v keine Chips vorhanden sind
		if(chips.isEmpty()) {
			return;
		}
		
		try {
			repository.writeAsync(chips);
		} catch(IOException e) {}
	}
	
	/**
	 * Schreibt alle Chips in eine Datei. 
	 * Dabei wird die Methode des Repositories verwendet,
	 * welche auf den Thread wartet.
	 */
	public boolean saveSync() {
		// Nicht speichern, wenn nichts geladen ist v keine Chips vorhanden sind
		if(chips.isEmpty()) {
			return true;
		}
		
		return repository.write(chips);
	}
	
	/**
	 * Das Laden aller Chips wird angestoßen.
	 * Das Resultat wird dem Controller hinzugefügt.
	 */
	public void load() {
		try {
			chips = (ArrayList<Chip>)repository.read();
		} catch (IOException ioe) {}
	}
	
	// GETTER UND SETTER
	public ArrayList<Chip> getChips() {
		return chips;
	}

	public void setChips(ArrayList<Chip> chips) {
		this.chips = chips;
	}
	
	public Chip getChipById(String id) {
		for(int i = 0; i < chips.size(); i++) {
			if(chips.get(i).getId().equals(id)) {
				return chips.get(i);
			}
		}
		return null;
	}
	
	public int getHighestLapCount() {
		int highest = Chip.LAPCOUNT_START;
		for(int i = 0; i < chips.size(); i++) {
			if(chips.get(i).getLapCount() > highest) {
				highest = chips.get(i).getLapCount();
			}
		}
		return highest;
	}

	public ChipRepository getRepository() {
		return repository;
	}

	public void setRepository(ChipRepository repository) {
		this.repository = repository;
	}
}

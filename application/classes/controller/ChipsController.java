package classes.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import classes.Data;
import classes.model.Chip;
import classes.model.Lap;

public class ChipsController {
	
	private List<Chip> chips;
	
	public ChipsController() {
		setChips(new ArrayList<Chip>());
	}
	
	//public ChipsController(String saveFile) {
	//	setChips(new ArrayList<Chip>());
	//}
	
	/**
	 * F�gt eine Runde hinzu (z.B. in einem Wettkampf)
	 * @param chipId Der Chip, dem die Runde hinzugef�gt werden soll
	 * @return 0: Erfolgreich, -1: Fehler
	 */
	public int addRound(String chipId) {
		try {
			Chip c = getChipById(chipId);
			c.getLaps().add(new Lap(LocalTime.now(), c.getLapCount() + 1));
		} catch(Exception ex) {
			// z.B.: null-Pointer-Exception
			return -1;
		}
		return 0;
	}
	
	/**
	 * Schreibt alle Chips in eine Datei.
	 */
	public void save() {
		// TODO: in eigenem Thread schreiben ? K�nnte sonst etwas viel
		// werden, w�hrend eines Wettkampfes. Oder gezielt nur einen Chip schreiben.
		for(int i = 0; i < chips.size(); i++) {
			try {
				Data.writeChip(chips.get(i));
			} catch(IOException ioe) {}
		}	
	}
	
	/**
	 * Das Laden aller Chips wird angesto�en.
	 * Das Resultat wird dem Controller hinzugef�gt.
	 */
	public void load() {
		try {
			chips = Data.readChips();
		} catch (IOException ioe) {}
	}
	
	// GETTER UND SETTER
	public List<Chip> getChips() {
		return chips;
	}

	public void setChips(List<Chip> chips) {
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
}

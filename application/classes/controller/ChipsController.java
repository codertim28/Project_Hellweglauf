package classes.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import classes.Chip;
import classes.Data;

public class ChipsController {
	
	private List<Chip> chips;
	
	public ChipsController() {
		setChips(new ArrayList<Chip>());
	}
	
	public ChipsController(String saveFile) {
		setChips(new ArrayList<Chip>());
	}
	
	/**
	 * Fügt eine Runde hinzu (z.B. in einem Wettkampf)
	 * @param chipId Der Chip, dem die Runde hinzugefügt werden soll
	 * @return 0: Erfolgreich, -1: Fehler
	 */
	public int addRound(String chipId) {
		return -1;
	}
	
	/**
	 * Schreibt die Chips in eine Datei.
	 */
	public void save() {
		for(int i = 0; i < chips.size(); i++) {
			try {
				Data.writeChip(chips.get(i));
			} catch(IOException ioe) {}
		}	
	}
	
	public void load() {
		try {
			chips = Data.readChips();
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
	
	// GETTER UND SETTER
	public List<Chip> getChips() {
		return chips;
	}

	public void setChips(List<Chip> chips) {
		this.chips = chips;
	}
}

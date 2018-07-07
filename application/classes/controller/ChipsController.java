package classes.controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import classes.Data;
import classes.model.Chip;
import classes.model.Lap;

public class ChipsController {
	
	private ArrayList<Chip> chips;
	// Das Arbeitsverzeichnis: "competition", "training" oder "basic" (s. Data)
	private String dir;
	
	public ChipsController() {
		setChips(new ArrayList<Chip>());
		setDir(Data.BASIC_DIR);
	}
	
	public ChipsController(String dir) {
		setChips(new ArrayList<Chip>());
		setDir(dir);
	}
	
	/**
	 * F�gt eine Runde hinzu (z.B. in einem Wettkampf)
	 * @param chipId Der Chip, dem die Runde hinzugef�gt werden soll
	 * @return 0: Erfolgreich, -1: Fehler
	 */
	public int addLap(String chipId) {
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
	 * Setzt die Runden jedes Chips zur�ck.
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
		try {
			Data.writeChips(dir, chips);
		} catch (IOException e) {}
	}
	
	/**
	 * Das Laden aller Chips wird angesto�en.
	 * Das Resultat wird dem Controller hinzugef�gt.
	 */
	public void load() {
		try {
			chips = Data.readChips(dir);
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

	public String getDir() {
		return dir;
	}

	private void setDir(String dir) {
		this.dir = dir;
	}
}

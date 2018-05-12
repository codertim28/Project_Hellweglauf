package classes.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import classes.Chip;
import classes.Data;

public class ChipsController {
	
	private List<Chip> chips;
	
	// Muss als Attribut vorhanden sein.
	// Die Wettkampf- / Trainingsdaten sollen 
	// in eigene Dateien geschrieben werden.
	private String saveFile;
	
	public ChipsController() {
		setChips(new ArrayList<Chip>());
		setSaveFile(Data.DATA_FILE);
	}
	
	public ChipsController(String saveFile) {
		setChips(new ArrayList<Chip>());
		setSaveFile(saveFile);
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
		String data = new String();
		for(int i = 0; i < chips.size(); i++) {
			data += chips.get(i).toString() + "\n";
		}
		
		try {
			Data.writeData(saveFile, data);
		} catch(IOException ioe) {}
	}
	
	public void load() {
		try {
			chips = Data.readData(saveFile);
		} catch (IOException ioe) {
			System.err.println("ERROR: readData");
		}
	}
	
	// GETTER UND SETTER
	public List<Chip> getChips() {
		return chips;
	}

	public void setChips(List<Chip> chips) {
		this.chips = chips;
	}

	public String getSaveFile() {
		return saveFile;
	}

	void setSaveFile(String saveFile) {
		this.saveFile = saveFile;
	}
}

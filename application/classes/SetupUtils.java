package classes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import classes.io.HellwegPrintWriter;
import classes.model.Chip;
import classes.model.Competition;

public final class SetupUtils {

	/**
	 * Diese Methode erstellt das data-Verzeichnis. In dem Data-Verzeichnis arbeitet
	 * das Programm.
	 * @throws IOException 
	 */
	public static void createDataDirIfNotExists() throws IOException {
		File file = new File(Data.DIR);
		// Verzeichnis erstellen
		if (!file.exists()) {
			file.mkdir();
		}
		
		// und das Unterverzeichnis für die Basisdaten
		file = new File(Data.DIR + "/" + Data.BASIC_DIR);
		if (!file.exists()) {
			file.mkdir();
		}
		
		// und eine leere Chipsdatei
		file = new File(Data.DIR + "/" + Data.BASIC_DIR  + "/" + Data.CHIPS_FILE);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		// und alle Klassen speichern
		file = new File(Data.DIR + "/" + Data.BASIC_DIR  + "/" + Data.FORMS_FILE);
		if (!file.exists()) {
			file.createNewFile();
			Data.writeObject(Data.BASIC_DIR + "/" + Data.FORMS_FILE, 
				new ArrayList<String>(Arrays.asList(new String[] {
					"5a", "5b", "5c", "5d", "6a", "6b", "6c", "6d",
					"7a", "7b", "7c", "7d", "8a", "8b", "8c", "8d",
					"9a", "9b", "9c", "9d",
				}))
			);
		}
		
		// und einen StandardWettkampf erzeugen
		file = new File(Data.DIR + "/" + Data.BASIC_DIR  + "/" + Data.COMPETITION_FILE);
		if (!file.exists()) {
			file.createNewFile();
			Competition defaultComp = new Competition();
			defaultComp.setName("Wettkampf");
			defaultComp.setLapCount(2.5);
			defaultComp.setLapLength(400);
			defaultComp.setTime(30 * 60);
			Data.writeComp(Data.BASIC_DIR, defaultComp);
		}
		
		// Auch das Wettkampfverzeichnis erstellen ?
		// Wird momentan nur im CompetitionView erstellt
		// createCompetitionDirIfNotExists();
	}
	
	/**
	 * Diese Methode erstellt das competition-Verzeichnis. 
	 * Dort wird ein Wettkampf verwaltet. 
	 * @return boolean - false, wenn ein Fehlerauftritt; sonst true
	 */
	public static boolean createCompetitionDirIfNotExists() {
		boolean returnValue = false;
		// Das Verzeichnis für den Wettkampf anlegen
		File file = new File(Data.DIR + "/" + Data.COMPETITION_DIR);
		if (!file.exists()) {
			returnValue = file.mkdir();
		}
		
		// Kopieren
		returnValue = copyChips();
		
		// Es kann auch sein, dass das Verzeichnis vorhanden ist, die Wettkampf
		// Datei aber nicht.
		if(!new File(Data.DIR + "/" + Data.COMPETITION_DIR + "/" + Data.COMPETITION_FILE).exists()) {
			// Kopieren. Einen Standard-Wettkampf
			try {
				Competition comp = Data.readComp(Data.BASIC_DIR);
				Data.writeComp(Data.COMPETITION_DIR, comp);
			} catch (IOException e) {
				return false;
			}
		}
			
		return returnValue;
	}
	
	/**
	 * Diese Methode testet, ob eine Datei existiert.
	 * @param file : String, der Pfad zur Datei.
	 * @return Größe der Datei, -1 wenn Datei nicht existiert, -2 wenn eine andere IOException auftritt.
	 */
	public static int testForFile(String file) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(Data.DIR + "/" + file));
			int size = bis.available();
			bis.close();
			return size;
		} catch (FileNotFoundException e) {
			// wenn eine Datei nicht existiert
			return -1;
		} catch (IOException e) {
			return -2;
		}
	}
	
	private static boolean copyChips() {
		try {
			ArrayList<Chip> chips = Data.readChips(Data.BASIC_DIR);
			
			HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(Constants.competitionChipsFilePath()));
			hpw.print(chips);
			hpw.flush();
			hpw.close();
			
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}

package prohell.prohell.classes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import prohell.prohell.classes.io.IOFacade;
import prohell.prohell.classes.model.Competition;
import prohell.prohell.classes.repository.ChipsRepository;

public final class SetupUtils {

	/**
	 * Diese Methode erstellt das data-Verzeichnis. In dem Data-Verzeichnis arbeitet
	 * das Programm.
	 * @throws IOException 
	 */
	public static void createDataDirIfNotExists() throws IOException {
		File file = new File(Constants.DIR);
		// Verzeichnis erstellen
		if (!file.exists()) {
			file.mkdir();
		}
		
		// und eine leere Chipsdatei
		file = new File(Constants.CHIPS_FILE_PATH);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		// und Properties-Datei erstellen
		file = new File(Constants.PROPERTIES_FILE);
		if (!file.exists()) {
			IOFacade.storeProperties(Constants.defaultProperties());
		}
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
		//returnValue = copyChips();
		
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
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
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
	
	@Deprecated
	private static boolean copyChips() {
		try {
			new ChipsRepository(Constants.competitionChipsFilePath()).write(
				new ChipsRepository(Constants.BASIC_CHIPS_FILE_PATH).read());
			
			return true;
		} catch (IOException | IllegalStateException e) {
			return false;
		}
	}
}

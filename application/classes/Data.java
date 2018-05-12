package classes;

import java.io.File;
import java.io.IOException;

public final class Data {

	private final static String DIR = "data";

	public final static String DATA_FILE = "data.txt";
	public final static String COMPETITION_FILE = "competition_data.txt";
	public final static String TRAINING_FILE = "training_data.txt";
	
	/**
	 * Diese Methode erstellt das data-Verzeichnis. In dem Data-Verzeichnis arbeitet
	 * das Programm.
	 */
	public static void createDataDirIfNotExists() {
		File file = new File(DIR);
		// Verzeichnis erstellen
		if (!file.exists()) {
			file.mkdir();
		}
	}

	/**
	 * Erstellt die data-Datei, in der alle Chips gespeichert werden.
	 * 
	 * @return false: Wenn keine Datei erstellt werden konnte.
	 */
	public static boolean createDataFileIfNotExists() {
		File file = new File(DIR + "/" + DATA_FILE);
		// Datei erstellen
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				return false;
			}
		}
		return true;
	}
}

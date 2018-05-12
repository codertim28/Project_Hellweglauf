package classes;

import java.io.*;
import java.util.ArrayList;

public final class Data {

	private final static String DIR = "data";

	public final static String DATA_FILE = "data.txt";
	public final static String COMPETITION_FILE = "competition_data.txt";
	public final static String TRAINING_FILE = "training_data.txt";
	
	public static void writeData(String file, String dataToWrite) throws IOException {		
		Writer writer = new FileWriter(DIR + "/" + file);
		writer.write(dataToWrite);
		writer.close();
		
		// TODO: Chips als JSON speichern ?
	}
	
	public static ArrayList<Chip> readData(String file) throws IOException {
		Reader reader = new FileReader(DIR + "/" + file);
		
		StringBuilder dataStrBui = new StringBuilder();	
		int c;
		while((c = reader.read()) != -1) {
			dataStrBui.append((char)c);
		}
		reader.close();
		
		// TODO: Chip-Objekt parsen, der Liste hinzufügen und 
		// anschließend zurückgeben. 
		
		return null;
	}
	
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

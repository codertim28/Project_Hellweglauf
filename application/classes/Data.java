package classes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import classes.io.*;
import classes.model.Chip;

public final class Data {

	private final static String DIR = "data";
	private final static String CHIP_DIR = "chips";
	
	public final static String BASIC_DIR = "basic";
	public final static String COMPETITION_DIR = "competition";
	public final static String TRAINING_DIR = "training";
	
	private final static String COMPETITION_FILE = "competition_data.txt";
	private final static String TRAINING_FILE = "training_data.txt";
	
	public static void writeChip(String dir,Chip chipToWrite) throws IOException {					
		
		String file = chipToWrite.getId() + ".xml";
		
		HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(DIR + "/" + dir + "/" + CHIP_DIR + "/" + file));
		hpw.print(chipToWrite);
		hpw.flush();
		hpw.close();
	}
	
	/**
	 * Es werden alle Dateien in dem Chipsverzeichnis ausgelesen.
	 * Anschließend werden die so erzeugten Chips in eine Liste geschrieben.
	 * @return Eine Liste aller Chips.
	 * @throws IOException - Falls ein IOError auftritt.
	 */
	public static List<Chip> readChips(String dir) throws IOException {
		
		List<Chip> chipList = new ArrayList<Chip>();
		
		for(final File file : new File(DIR + "/" + dir + "/" + CHIP_DIR).listFiles()) {
	        if(!file.isDirectory()) {
	           
	        	HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(file));   		
	        	Chip c = hbr.readChip();
	        	// Diese kleine Abfrage dient als Idiotenschutz.
	        	// Falls die Datei manipuliert wurde und nicht mehr
	        	// lesbar ist, wird der Chip ignoriert.
	        	// TODO: Fehler in Log-Datei schreiben ?
	        	if(c != null) {
	        		chipList.add(c);
	        	}
	    		hbr.close();
	        } 
	    }		
		return chipList;
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
		// und das Unterverzeichnis für die Basisdaten
		file = new File(DIR + "/" + BASIC_DIR);
		if (!file.exists()) {
			file.mkdir();
		}
		// und das Unterverzeichnis für die Chips
		file = new File(DIR + "/" + BASIC_DIR + "/" + CHIP_DIR);
		if (!file.exists()) {
			file.mkdir();
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
		// und das Unterverzeichnis für die Chips
		File file = new File(DIR + "/" + COMPETITION_DIR);
		if (!file.exists()) {
			returnValue = file.mkdir();
		}
		
		// Falls das Wettkampfverzeichnis eben erstellt wurde, müssen auch die Chips 
		// dorthin kopiert werden.
		// TODO: Chips kopieren
		File temp = new File(DIR + "/" + COMPETITION_DIR + "/" + CHIP_DIR);
		if (!temp.exists()) {
			returnValue = temp.mkdir();
		}
		
		
		return returnValue;
	}

	/**
	 * Erstellt die data-Datei, in der alle Chips gespeichert werden.
	 * (Wird momentan nicht benötigt)
	 * 
	 * @return false: Wenn keine Datei erstellt werden konnte.
	 */
	public static boolean createDataFileIfNotExists() {
//		File file = new File(DIR + "/" + DATA_FILE);
//		// Datei erstellen
//		if (!file.exists()) {
//			try {
//				file.createNewFile();
//			} catch (IOException ioe) {
//				return false;
//			}
//		}
		return true;
	}
}

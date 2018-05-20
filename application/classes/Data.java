package classes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import classes.io.*;

public final class Data {

	private final static String DIR = "data";
	private final static String CHIP_DIR = "chips";
	
	public final static String COMPETITION_DIR = "competition";
	public final static String TRAINING_DIR = "training";

	//public final static String DATA_FILE = "data.txt";
	// Diese Dateien werden nach dem Wettkampf eine Liste enthalten. Die 
	// Liste ist nach Scans geordnet (bzw. nach Runden).
	public final static String COMPETITION_FILE = "competition_data.txt";
	public final static String TRAINING_FILE = "training_data.txt";
	
	public static void writeChip(Chip chipToWrite) throws IOException {					
		
		String file = chipToWrite.getId() + ".xml";
		
		HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(DIR + "/" + CHIP_DIR + "/" + file));
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
	public static List<Chip> readChips() throws IOException {
		
		List<Chip> chipList = new ArrayList<Chip>();
		
		for(final File file : new File(DIR + "/" + CHIP_DIR).listFiles()) {
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
		// und das Unterverzeichnis für die Chips
		file = new File(DIR + "/" + CHIP_DIR);
		if (!file.exists()) {
			file.mkdir();
		}
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

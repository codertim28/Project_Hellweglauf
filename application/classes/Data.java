package classes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class Data {

	private final static String DIR = "data";
	private final static String CHIP_DIR = "chips";
	
	public final static String COMPETITION_DIR = "competition";
	public final static String TRAINING_DIR = "training";

	//public final static String DATA_FILE = "data.txt";
	// Diese Dateien werden nach dem Wettkampf eine Liste enthalten. Die 
	// Liste ist nach Scans geordnet.
	public final static String COMPETITION_FILE = "competition_data.txt";
	public final static String TRAINING_FILE = "training_data.txt";
	
	public static void writeChip(Chip chipToWrite) throws IOException {					
		
		String file = chipToWrite.getId() + ".chip";
		
		Writer writer = new FileWriter(DIR + "/" + CHIP_DIR + "/" + file);
		writer.write(chipToWrite.toString());
		writer.close();	
	}
	
	public static List<Chip> readChips() throws IOException {
		
		List<Chip> chipList = new ArrayList<Chip>();
		
		for(final File file : new File(DIR + "/" + CHIP_DIR).listFiles()) {
	        if(!file.isDirectory()) {
	           
	        	Reader reader = new FileReader(file);
	    		
	    		StringBuilder dataStrBui = new StringBuilder();	
	    		int c;
	    		while((c = reader.read()) != -1) {
	    			dataStrBui.append((char)c);
	    		}
	    		reader.close();
	    		
	    		String sep = "|";
	    		String id = dataStrBui.substring(0, dataStrBui.indexOf(sep));
	    		dataStrBui = dataStrBui.delete(0, dataStrBui.indexOf(sep) + 1);
	    		String studentName = dataStrBui.substring(0, dataStrBui.indexOf(sep));
	    		
	    		// TODO: Runden aus der Datei lesen
	    		
	    		chipList.add(new Chip(id, studentName));
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

package classes;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import classes.io.*;
import classes.model.Chip;

public final class Data {

	private final static String DIR = "data";
	
	public final static String BASIC_DIR = "basic";
	public final static String COMPETITION_DIR = "competition";
	public final static String TRAINING_DIR = "training";
	
	private final static String CHIPS_FILE = "chips.xml";
	private final static String COMPETITION_FILE = "competition_data.prohell";
	private final static String TRAINING_FILE = "training_data.prohell";
	
	public static void writeChips(String dir, ArrayList<Chip> chips) throws IOException {					
		// TODO: in eigenem Thread schreiben ? K�nnte sonst etwas viel
		// werden, w�hrend eines Wettkampfes. Oder gezielt nur einen Chip schreiben.
		HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(DIR + "/" + dir + "/" + CHIPS_FILE));
		for(final Chip chipToWrite : chips) {
			hpw.print(chipToWrite);
		}
		hpw.flush();
		hpw.close();
	}
	
	/**
	 * Es werden alle Chips aus der Chipsdatei gelesen.
	 * Anschlie�end werden die so erzeugten Chips in eine Liste geschrieben.
	 * @return Eine Liste aller Chips.
	 * @throws IOException - Falls ein IOError auftritt.
	 */
	public static ArrayList<Chip> readChips(String dir) throws IOException {
		// TODO: in eigenem Thread lesen ? K�nnte sonst etwas viel
		// werden, w�hrend eines Wettkampfes. Oder gezielt nur einen Chip schreiben.
		ArrayList<Chip> chipList = new ArrayList<Chip>();
		       
    	HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(DIR + "/" + dir + "/" + CHIPS_FILE));   		
    	Chip c;
    	while((c = hbr.readChip()) != null) {
    		chipList.add(c);
    	}
		hbr.close();
	  		
		return chipList;
	}
	
	public static void writeObject(String dir, Object obj) throws IOException {
		if(!(obj instanceof Serializable)) {
			throw new IOException("Given object is not serializable.");
		}
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIR + "/" + dir + "/" + COMPETITION_FILE));
	
		oos.writeObject(obj);
		
		oos.flush();
		oos.close();
	}
	
	public static Object readObject(String dir) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIR + "/" + dir + "/" + COMPETITION_FILE));
		// Das Objekt lesen
		Object obj = ois.readObject();
		ois.close();
		
		return obj;
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
		// und das Unterverzeichnis f�r die Basisdaten
		file = new File(DIR + "/" + BASIC_DIR);
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
		// und das Unterverzeichnis f�r die Chips
		File file = new File(DIR + "/" + COMPETITION_DIR);
		if (!file.exists()) {
			returnValue = file.mkdir();
			// Kopieren. Ist das Wettkampfverzeichnis nicht vorhanden, k�nnen
			// die Chips auch nicht vorhanden sein. 
			copyChips(Data.BASIC_DIR, Data.COMPETITION_DIR);
		}
			
		return returnValue;
	}

	public static boolean copyChips(String from, String to) {
		try {
			ArrayList<Chip> chips = readChips(from);
			HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(DIR + "/" + to + "/" + CHIPS_FILE));
			// TODO: eigene Methode in HellwegPrintWriter, damit 
			// die XML-Tags nicht mehr hier stehen.
			hpw.println("<chips>");
			for(Chip chip : chips) {
				hpw.print(chip);
			}
			hpw.println("</chips>");
			hpw.flush();
			hpw.close();
			return true;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	/**
	 * Erstellt die data-Datei, in der alle Chips gespeichert werden.
	 * (Wird momentan nicht ben�tigt)
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

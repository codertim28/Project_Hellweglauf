package classes;

import java.io.*;
import java.util.ArrayList;

import classes.io.*;
import classes.model.Chip;
import classes.model.Competition;
import tp.Synchronizer;

public final class Data {

	private final static String DIR = "data";
	
	public final static String BASIC_DIR = "basic";
	public final static String COMPETITION_DIR = "competition";
	public final static String TRAINING_DIR = "training";
	
	public final static String CHIPS_FILE = "chips.xml";
	private final static String COMPETITION_FILE = "competition.xml";
	private final static String TRAINING_FILE = "training.xml";
	
	public static void writeChips(String dir, ArrayList<Chip> chips) throws IOException {					
		// Der PrintWriter wird hier erzeugt (wegen throws im Methodenkopf)
		HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(DIR + "/" + dir + "/" + CHIPS_FILE));
		
		Thread writerThread = new Thread(new Runnable() {
			@Override 
			public void run() {
				
				// Dies sorgt dafür, dass der Schreibvorgang 
				// nicht unterbrochen werden kann (z.B. von einem
				// anderen SchreiberThread).
				Synchronizer.sync(new Runnable() {
					@Override
					public void run() {
						for(final Chip chipToWrite : chips) {
							hpw.print(chipToWrite);
						}
					}
				});
				
				hpw.flush();
				hpw.close();
			}
		});
		// Alle Chips schreiben
		writerThread.start();	
	}
	
	/**
	 * Es werden alle Chips aus der Chipsdatei gelesen.
	 * Anschließend werden die so erzeugten Chips in eine Liste geschrieben.
	 * @return Eine Liste aller Chips.
	 * @throws IOException - Falls ein IOError auftritt.
	 */
	public static ArrayList<Chip> readChips(String dir) throws IOException {
		// TODO: in eigenem Thread lesen ? Könnte sonst etwas viel
		// werden, während eines Wettkampfes. Oder gezielt nur einen Chip schreiben.
		ArrayList<Chip> chipList = new ArrayList<Chip>();
		       
    	HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(DIR + "/" + dir + "/" + CHIPS_FILE));   		
    	Chip c;
    	while((c = hbr.readChip()) != null) {
    		chipList.add(c);
    	}
		hbr.close();
	  		
		return chipList;
	}
	
	public static void writeComp(String dir, Competition compToWrite) throws IOException {					
		HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(DIR + "/" + dir + "/" + COMPETITION_FILE));
		hpw.print(compToWrite);
		hpw.flush();
		hpw.close();
	}
	
	public static Competition readComp(String dir) throws IOException {
	
    	HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(DIR + "/" + dir + "/" + COMPETITION_FILE));   		
    	Competition comp = hbr.readCompetition();
		hbr.close();
		return comp;
	}
	
	public static void writeObject(String pathToFile, Object obj) throws IOException {
		if(!(obj instanceof Serializable)) {
			throw new IOException("Given object is not serializable.");
		}
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DIR + "/" + pathToFile));
	
		oos.writeObject(obj);
		
		oos.flush();
		oos.close();
	}
	
	public static Object readObject(String pathToFile) throws IOException, ClassNotFoundException {	
		if(testForFile(pathToFile) <= -1) {
			throw new IOException("No file available.");
		}
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIR + "/" + pathToFile));
		// Das Objekt lesen
		Object obj = ois.readObject();
		ois.close();
		
		return obj;
	}
	
	/**
	 * Diese Methode erstellt das data-Verzeichnis. In dem Data-Verzeichnis arbeitet
	 * das Programm.
	 * @throws IOException 
	 */
	public static void createDataDirIfNotExists() throws IOException {
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
		
		// und eine leere Chipsdatei
		file = new File(DIR + "/" + BASIC_DIR  + "/" + CHIPS_FILE);
		if (!file.exists()) {
			file.createNewFile();
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
		File file = new File(DIR + "/" + COMPETITION_DIR);
		if (!file.exists()) {
			returnValue = file.mkdir();
			// Kopieren. Ist das Wettkampfverzeichnis nicht vorhanden, können
			// die Chips auch nicht vorhanden sein. 
			copyChips(Data.BASIC_DIR, Data.COMPETITION_DIR);
		}
		
		// Es kann auch sein, dass das Verzeichnis vorhanden ist, die Wettkampf
		// Datei aber nicht.
		if(!new File(DIR + "/" + COMPETITION_DIR + "/" + COMPETITION_FILE).exists()) {
			// Kopieren. Einen Standard-Wettkampf
			try {
				Competition comp = readComp(Data.BASIC_DIR);
				writeComp(Data.COMPETITION_DIR, comp);
			} catch (IOException e) {
				return false;
			}
		}
			
		return returnValue;
	}

	public static boolean copyChips(String from, String to) {
		try {
			ArrayList<Chip> chips = readChips(from);
			
			HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(DIR + "/" + to + "/" + CHIPS_FILE));
			hpw.print(chips);
			hpw.flush();
			hpw.close();
			
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Diese Methode testet, ob eine Datei existiert.
	 * @param file : String, der Pfad zur Datei.
	 * @return Größe der Datei, -1 wenn Datei nicht existiert, -2 wenn eine andere IOException auftritt.
	 */
	public static int testForFile(String file) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(DIR + "/" + file));
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
}

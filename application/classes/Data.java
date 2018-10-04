package classes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import classes.io.*;
import classes.model.Chip;
import classes.model.Competition;
import tp.Synchronizer;

// TODO: Aus Data ein Repository machen

public final class Data {

	public final static String DIR = "data";
	
	public final static String BASIC_DIR = "basic";
	public final static String COMPETITION_DIR = "competition";
	public final static String TRAINING_DIR = "training";
	
	public final static String CHIPS_FILE = "chips.xml";
	public final static String COMPETITION_FILE = "competition.xml";
	public final static String TRAINING_FILE = "training.xml";
	public final static String FORMS_FILE = "forms";
	
	@Deprecated
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
	@Deprecated
	public static ArrayList<Chip> readChips(String dir) throws IOException {
		ArrayList<Chip> chipList = new ArrayList<Chip>();
		       
    	HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(DIR + "/" + dir + "/" + CHIPS_FILE));   		
    	Chip c;
    	while((c = hbr.readChip()) != null) {
    		chipList.add(c);
    	}
		hbr.close();
	  		
		return chipList;
	}
	
	@Deprecated
	public static void writeComp(String dir, Competition compToWrite) throws IOException {					
		HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(DIR + "/" + dir + "/" + COMPETITION_FILE));
		hpw.print(compToWrite);
		hpw.flush();
		hpw.close();
	}
	
	@Deprecated
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
	
	public static Object readObject(String pathToFile) throws IOException, ClassNotFoundException, EOFException {	
		if(SetupUtils.testForFile(pathToFile) <= -1) {
			throw new IOException("No file available.");
		}
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DIR + "/" + pathToFile));
		// Das Objekt lesen
		Object obj = ois.readObject();
		ois.close();
		
		return obj;
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
}

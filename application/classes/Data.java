package classes;

import java.io.*;
import java.util.ArrayList;
import classes.io.*;
import classes.model.Chip;
import classes.model.Competition;
import tp.Synchronizer;

// TODO: Data abschaffen

public final class Data {

	public final static String DIR = "data";
	
	public final static String BASIC_DIR = "basic";
	public final static String COMPETITION_DIR = "competition";
	public final static String TRAINING_DIR = "training";
	
	public final static String CHIPS_FILE = "chips.xml";
	public final static String COMPETITION_FILE = "competition.xml";
	public final static String TRAINING_FILE = "training.xml";
	public final static String FORMS_FILE = "forms";
	
	
	/**
	 * Es werden alle Chips aus der Chipsdatei gelesen.
	 * Anschlieﬂend werden die so erzeugten Chips in eine Liste geschrieben.
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
}

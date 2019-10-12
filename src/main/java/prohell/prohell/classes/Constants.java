package prohell.prohell.classes;

public final class Constants {
	public final static String SEPERATOR = "/";
	public final static String DIR = "data";
	
	public final static String BASIC_DIR = "basic";
	public final static String COMPETITION_DIR = "competition";
	public final static String TRAINING_DIR = "training";
	
	public final static String CHIPS_FILE = "chips.xml";
	public final static String COMPETITION_FILE = "competition.xml";
	public final static String COMPETITION_CHIPS_FILE = "competition.chips.xml";
	
	public final static String TRAINING_FILE = "training.xml";
	public final static String FORMS_FILE = "forms";
	
	public final static String LOG_FILE = "log.txt";
	
	public static final String BASIC_CHIPS_FILE_PATH = 
			DIR + SEPERATOR + BASIC_DIR + SEPERATOR + CHIPS_FILE;
	
	public static String competitionFilePath() {
		return DIR + SEPERATOR + COMPETITION_DIR + SEPERATOR + COMPETITION_FILE;
	}
	
	public static String competitionChipsFilePath() {
		return DIR + SEPERATOR + COMPETITION_DIR + SEPERATOR + COMPETITION_CHIPS_FILE;
	}
	
	public static String logFilePath() {
		return DIR + SEPERATOR + LOG_FILE;
	}
}

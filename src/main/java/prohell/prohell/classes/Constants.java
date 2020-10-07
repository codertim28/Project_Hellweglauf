package prohell.prohell.classes;

import java.util.Properties;

public final class Constants {
	public final static String SEPERATOR = "/";
	public final static String DIR = "data";
	
	public final static String BASIC_DIR = "basic";
	public final static String COMPETITION_DIR = "competition";
	public final static String TRAINING_DIR = "training";
	public final static String CHIPS_DIR = "chips";
	
	public final static String PROPERTIES_FILE = "competition.properties";
	public final static String CHIPS_FILE = "chips.csv";
	public final static String COMPETITION_FILE = "competition.xml";
	
	public final static String TRAINING_FILE = "training.xml";
	public final static String FORMS_FILE = "forms";
	
	public final static String LOG_FILE = "log.txt";

	public static final String CHIPS_FILE_PATH = DIR + SEPERATOR + CHIPS_FILE;
	public static final String COMPETITION_FILE_PATH = DIR + SEPERATOR + COMPETITION_FILE;

	// Properties
	public static final String PROPERTIES_FILE_PATH = DIR + SEPERATOR +  PROPERTIES_FILE;

	public static Properties defaultProperties() {
		Properties props = new Properties();
		props.setProperty("competition.lapLength", "400");
		props.setProperty("competition.lapCount", "2.5");
		props.setProperty("competition.time", String.valueOf(30 * 60));
		return props;
	}
	
	public static String logFilePath() {
		return DIR + SEPERATOR + LOG_FILE;
	}
}

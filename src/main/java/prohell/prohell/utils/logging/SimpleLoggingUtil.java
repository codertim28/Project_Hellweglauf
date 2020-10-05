package prohell.prohell.utils.logging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// TODO User benachrichtigen, wenn hier eine Exception auftaucht 
public class SimpleLoggingUtil implements ILoggingUtil {
	
	private File logFile;
	
	public SimpleLoggingUtil(File logFile) {
		this.logFile = logFile;
		
		if(!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) { 
				//e.printStackTrace();
			}
		}
	}
	
	@Override
	public void info(String message) {
		try {
		    Files.write(logFile.toPath(), buildMessage("INFO", message).getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {}
	}

	@Override
	public void warning(String message) {
		try {
		    Files.write(logFile.toPath(), buildMessage("WARNING", message).getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {}
	}
	
	@Override
	public void warning(Exception ex) {
		try {
		    Files.write(logFile.toPath(), buildMessage("WARNING", ex).getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {}
	}

	@Override
	public void error(String message) {

		try {
		    Files.write(logFile.toPath(), buildMessage("ERROR", message).getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {}
	}
	
	@Override
	public void error(Exception ex) {
		
		try {
		    Files.write(logFile.toPath(), buildMessage("ERROR", ex).getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {}
	}
	
	private String buildMessage(String type, Exception ex) {
		String output = getDateTime() + " " + type + " " + ex.getMessage() + "\n";
		for(StackTraceElement ste : ex.getStackTrace()) {
			output += ste.toString() + "\n";
		}
		output += "====================\n";
		return output;
	}
	
	private String buildMessage(String type, String message) {
		String output = getDateTime() + " " + type + " " + message + "\n";
		output += "====================\n";
		return output;
	}
	
	private String getDateTime() {
		return "[" + LocalDateTime.now().format(
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
			.withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault())) + "]";
	}
}

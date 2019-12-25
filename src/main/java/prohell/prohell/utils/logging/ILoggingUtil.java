package prohell.prohell.utils.logging;

public interface ILoggingUtil {
	void info(String message);
	void warning(String message);
	void error(String message);
	
	void error(Exception e);
	void warning(Exception e);
}

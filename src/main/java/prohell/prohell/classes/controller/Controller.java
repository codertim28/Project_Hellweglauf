package prohell.prohell.classes.controller;

import java.io.File;

import prohell.prohell.classes.Constants;
import prohell.prohell.utils.logging.ILoggingUtil;
import prohell.prohell.utils.logging.SimpleLoggingUtil;

public abstract class Controller {
	
	protected ILoggingUtil log = new SimpleLoggingUtil(new File(Constants.logFilePath()));
	
	public abstract boolean save();
	public abstract boolean saveSync();
	public abstract void load();
}

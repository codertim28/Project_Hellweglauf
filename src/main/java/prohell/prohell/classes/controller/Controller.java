package prohell.prohell.classes.controller;

import java.io.File;

import prohell.prohell.classes.Constants;
import prohell.prohell.tp.logging.ILoggingUtil;
import prohell.prohell.tp.logging.SimpleLoggingUtil;

public abstract class Controller {
	
	protected ILoggingUtil log = new SimpleLoggingUtil(new File(Constants.logFilePath()));
	
	public abstract boolean save();
	public abstract boolean saveSync();
	public abstract void load();
}

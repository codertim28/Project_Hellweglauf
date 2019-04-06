package classes.controller;

import java.io.File;

import classes.Constants;
import tp.logging.ILoggingUtil;
import tp.logging.SimpleLoggingUtil;

public abstract class Controller {
	
	protected ILoggingUtil log = new SimpleLoggingUtil(new File(Constants.logFilePath()));
	
	public abstract boolean save();
	public abstract boolean saveSync();
	public abstract void load();
}

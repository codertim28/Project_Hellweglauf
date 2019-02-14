package classes.repository;

import tp.repository.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import classes.Data;
import classes.controller.ChipsController;
import classes.io.HellwegBufferedReader;
import classes.io.HellwegPrintWriter;
import classes.model.Competition;

public class CompetitionRepository extends Repository implements SWriteRead<Competition> {

	public CompetitionRepository(String path) {
		super(path);
	}
	
	@Override
	public boolean write(Competition compToWrite) {	
		boolean success = true;
		
		try {
			HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(path));
			hpw.print(compToWrite);
			hpw.flush();
			hpw.close();
		} catch(IOException e) {
			success = false;
		}	
		
		// Die Chips werden immer mit "compName.chips.xml" gespeichert
//		String chipsPath = path.replaceAll(".xml", ".chips.xml");
//		ChipsController cc = compToWrite.getChipsController();
//		cc.getRepository().setPath(chipsPath);
		
		// Speichern
		// -> entweder per Thread oder auf den Thread wartend.
//		if(waitForThread) {
//			success = cc.saveSync();
//		}
//		else {
//			compToWrite.getChipsController().save();
//		}
		
		return success;
	}
	
	/**
	 * Liest alle Wettkampf- und Chipdaten ein. Diese 
	 * Werden einem Wettkampfobjekt zugewiesen und zur�ckgegeben.
	 * @param userRead So wird entschieden, ob der vom Benutzer eingegebene Pfad beachtet werden soll.
	 * @throws IOException Wenn ein IO-Fehler auftritt.
	 */
	@Override
	public Competition read() throws IOException {
		HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(path));   		
    	Competition comp = hbr.readCompetition();
		hbr.close();
		
		// Auch den dazugeh�rigen ChipsController laden
//		ChipsController cc;
//		String chipsPath = path.replaceAll(".xml", ".chips.xml");
//		cc = new ChipsController(chipsPath);
//		cc.load();
		
		return comp;
	}
}

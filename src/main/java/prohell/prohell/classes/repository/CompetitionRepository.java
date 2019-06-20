package prohell.prohell.classes.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import prohell.prohell.classes.io.HellwegBufferedReader;
import prohell.prohell.classes.io.HellwegPrintWriter;
import prohell.prohell.classes.model.Competition;
import prohell.prohell.tp.repository.Repository;
import prohell.prohell.tp.repository.SWriteRead;

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
	 * Liest alle Wettkampfdaten ein. Diese 
	 * Werden einem Wettkampfobjekt zugewiesen und zurückgegeben.
	 * @param userRead So wird entschieden, ob der vom Benutzer eingegebene Pfad beachtet werden soll.
	 * @throws IOException Wenn ein IO-Fehler auftritt.
	 */
	@Override
	public Competition read() throws IOException {
		HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(path));   		
    	Competition comp = hbr.readCompetition();
		hbr.close();
		
		return comp;
	}
}

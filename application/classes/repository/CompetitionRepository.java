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
		return write(compToWrite, false);
	}
	
	public boolean write(Competition compToWrite, boolean waitForThread) {	
		boolean success = true;
		
		try {
			HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(path));
			hpw.print(compToWrite);
			hpw.flush();
			hpw.close();
		} catch(IOException e) {
			success = false;
		}	
		
		// Speichern
		// -> entweder per Thread oder auf den Thread wartend.
		if(waitForThread) {
			// Wenn auf den Thread gewartet werden soll, dann speichert
			// der Benutzer, also muss hier der gewünschte Pfad gesetzt werden.
			String chipsPath = path.replaceAll(".xml", ".chips.xml");
			ChipsController cc = compToWrite.getChipsController();
			cc.getRepository().setPath(chipsPath);
			success = cc.saveSync();
		}
		else {
			// TODO: Prüfen, ob in korrekten Pfad geschrieben wird (s.o.) +
			// Alle Speicheranweisungen im CompetitionView überarbeiten, da 
			// das Competitionrepository nun die Chips speichert.
			compToWrite.getChipsController().save();
		}
		
		return success;
	}

	@Override
	public Competition read() throws IOException {
		// ganz normal laden
		return read(false);
	}
	
	/**
	 * Liest alle Wettkampf- und Chipdaten ein. Diese 
	 * Werden einem Wettkampfobjekt zugewiesen und zurückgegeben.
	 * @param userRead So wird entschieden, ob der vom Benutzer eingegebene Pfad beachtet werden soll.
	 * @throws IOException Wenn ein IO-Fehler auftritt.
	 */
	public Competition read(boolean userRead) throws IOException {
		HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(path));   		
    	Competition comp = hbr.readCompetition();
		hbr.close();
		
		// Auch den dazugehörigen ChipsController laden
		ChipsController cc;
		if(userRead) {
			cc = new ChipsController(Data.DIR + "/" + Data.COMPETITION_DIR + "/" + Data.CHIPS_FILE);
		}
		else {
			String chipsPath = path.replaceAll(".xml", ".chips.xml");
			cc = new ChipsController(chipsPath);
		}
		cc.load();
		comp.setChipsController(cc);
		
		return comp;
	}
}

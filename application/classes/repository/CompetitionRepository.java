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
			compToWrite.getChipsController().save();
		}
		
		return success;
	}

	@Override
	public Competition read() throws IOException {
		HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(path));   		
    	Competition comp = hbr.readCompetition();
		hbr.close();
		
		// Auch den dazugehörigen ChipsController laden
		// TODO: Pfad anpassen
		ChipsController cc = new ChipsController(Data.DIR + "/" + Data.COMPETITION_DIR + "/" + Data.CHIPS_FILE);
		cc.load();
		comp.setChipsController(cc);
		
		return comp;
	}
}

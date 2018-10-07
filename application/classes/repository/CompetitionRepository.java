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
	public void write(Competition compToWrite) throws IOException {
		HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(path));
		hpw.print(compToWrite);
		hpw.flush();
		hpw.close();	
		
		// ChipsController speichern
		compToWrite.getChipsController().save();
	}

	@Override
	public Competition read() throws IOException {
		HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(path));   		
    	Competition comp = hbr.readCompetition();
		hbr.close();
		
		// Auch den dazugeh�rigen ChipsController laden
		// TODO: Pfad anpassen
		ChipsController cc = new ChipsController(Data.DIR + "/" + Data.COMPETITION_DIR + "/" + Data.CHIPS_FILE);
		cc.load();
		comp.setChipsController(cc);
		
		return comp;
	}
}

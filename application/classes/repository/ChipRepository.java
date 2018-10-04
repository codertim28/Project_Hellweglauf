package classes.repository;

import tp.repository.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import classes.io.HellwegBufferedReader;
import classes.io.HellwegPrintWriter;
import classes.model.Chip;
import tp.Synchronizer;

public class ChipRepository extends Repository implements MWriteRead<Chip> {

	public ChipRepository(String path) {
		super(path);
	}

	@Override
	public void write(List<Chip> chips) throws IOException {
		// Der PrintWriter wird hier erzeugt (wegen throws im Methodenkopf)
		HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(path));
		
		Thread writerThread = new Thread(new Runnable() {
			@Override 
			public void run() {
				
				// Dies sorgt daf�r, dass der Schreibvorgang 
				// nicht unterbrochen werden kann (z.B. von einem
				// anderen SchreiberThread).
				Synchronizer.sync(new Runnable() {
					@Override
					public void run() {
						for(final Chip chipToWrite : chips) {
							hpw.print(chipToWrite);
						}
					}
				});
				
				hpw.flush();
				hpw.close();
			}
		});
		// Alle Chips schreiben
		writerThread.start();		
	}

	/**
	 * Es werden alle Chips aus der Chipsdatei gelesen.
	 * Anschlie�end werden die so erzeugten Chips in eine Liste geschrieben.
	 * @return Eine Liste aller Chips.
	 * @throws IOException - Falls ein IOError auftritt.
	 */
	@Override
	public List<Chip> read() throws IOException {
		ArrayList<Chip> chipList = new ArrayList<Chip>();
	       
    	HellwegBufferedReader hbr = new HellwegBufferedReader(new FileReader(path));   		
    	Chip c;
    	while((c = hbr.readChip()) != null) {
    		chipList.add(c);
    	}
		hbr.close();
	  		
		return chipList;
	}

}

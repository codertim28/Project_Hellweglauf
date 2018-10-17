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
	public void writeAsync(List<Chip> chips) throws IOException {
		createWriterThread(chips).start();	
	}
	
	/**
	 * Schreibt alle Chips. Wartet dabei (mit join) auf 
	 * den schreibenden Thread.
	 */
	@Override
	public boolean write(List<Chip> chips) {
	
		try {
			Thread wt = createWriterThread(chips);
			wt.start();
			wt.join();
		} catch(IOException | InterruptedException e) {
			return false;
		}
		return true;
	}

	private Thread createWriterThread(List<Chip> chips) throws IOException {
		// Der PrintWriter wird hier erzeugt (wegen throws im Methodenkopf)
		HellwegPrintWriter hpw = new HellwegPrintWriter(new FileWriter(path));
		
		return new Thread(new Runnable() {
			@Override 
			public void run() {
				
				// Dies sorgt dafür, dass der Schreibvorgang 
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
	}
	
	/**
	 * Es werden alle Chips aus der Chipsdatei gelesen.
	 * Anschließend werden die so erzeugten Chips in eine Liste geschrieben.
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

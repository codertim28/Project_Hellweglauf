package classes.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import classes.model.Chip;
import classes.model.Lap;

public class HellwegPrintWriter extends PrintWriter {

	public HellwegPrintWriter(Writer writer) {
		super(writer);
	}

	public HellwegPrintWriter(OutputStream os) {
		super(os);
	}
	
	/**
	 * Schreibt einen Chip (im XML-Format).
	 * @param chip Chip, welcher gespeichert werden soll.
	 */
	public void print(Chip chip) {
		println("<chip>");
		
		// Jedes Attribut schreiben.
		println("\t<id>" + chip.getId() + "</id>");
		println("\t<studentName>" + chip.getStudentName() + "</studentName>");
		
		// Runden schreiben
		println("\t<laps>");
		List<Lap> laps = chip.getLaps();
		for(int i = 0; i < laps.size(); i++) {
			println("\t\t<lap number=\"" + laps.get(i).getNumber() + "\" timestamp=\"" + laps.get(i).getTimestamp() + "\" />");
		}
		println("\t</laps>");
		
		println("</chip>");
	}

}

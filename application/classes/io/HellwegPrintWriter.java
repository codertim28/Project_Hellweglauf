package classes.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import classes.Chip;
import classes.Round;

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
		println("\t<rounds>");
		List<Round> rounds = chip.getRounds();
		for(int i = 0; i < rounds.size(); i++) {
			println("\t\t<round timestamp=\"" + rounds.get(i).getTimestamp() + "\" />");
		}
		println("\t</rounds>");
		
		println("</chip>");
	}

}

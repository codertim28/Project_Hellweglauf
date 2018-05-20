package classes.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

import classes.Chip;

public class HellwegPrintWriter extends PrintWriter {

	public HellwegPrintWriter(Writer writer) {
		super(writer);
	}

	public HellwegPrintWriter(OutputStream os) {
		super(os);
	}
	
	public void print(Chip chip) {
		println("<chip>");
		
		// Jedes Attribut schreiben.
		println("\t<id>" + chip.getId() + "</id>");
		println("\t<studentName>" + chip.getStudentName() + "</studentName>");
		
		// Runden schreiben
		println("<rounds>");
		// TODO: Hier die Runden schreiben.
		println("</rounds>");
		
		println("</chip>");
	}

}

package classes.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import classes.CompetitionViewRowData;
import classes.model.Chip;
import classes.model.Competition;
import classes.model.Lap;

public class HellwegPrintWriter extends PrintWriter {

	public HellwegPrintWriter(Writer writer) {
		super(writer);
	}

	public HellwegPrintWriter(OutputStream os) {
		super(os);
	}
	
	/**
	 * Schreibt eine Liste an Chips.
	 * @param chips 
	 */
	public void print(List<Chip> chips) {
		println("<chips>");
		for(Chip chip : chips) {
			print(chip);
		}
		println("</chips>");
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
	
	/**
	 * Schreibt einen Wettkampf (im XML-Format).
	 * @param Competition Wettkampf, welcher gespeichert werden soll (muss SimpleAttributeAccess implementieren).
	 */
	public void print(Competition comp) {
		println("<competition>");
		
		// Jedes Attribut schreiben
		println("\t<name>" + comp.getName() + "</name>");
		println("\t<lapLength>" + comp.getLapLength() + "</lapLength>");
		println("\t<lapCount>" + comp.getLapCount() + "</lapCount>");
		println("\t<time>" + comp.getTime() + "</time>");
		println("\t<state>" + comp.getState() + "</state>");
		
		// DataRows schreiben
		println("\t<rows>");
		List<CompetitionViewRowData> data = comp.getData();
		for(CompetitionViewRowData row : data) {
			println("\t\t<row chipId=\"" + row.chipIdProperty().get() + "\" studentName=\"" + row.studentNameProperty().get()  + "\" lapNumber=\"" + row.lapNumberProperty().get()  + "\" timestamp=\"" + row.timestampProperty().get()  + "\" />");
		}
		println("\t</rows>");
		
		println("</competition>");
	}
}

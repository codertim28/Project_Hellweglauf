package classes.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import classes.Chip;

public class HellwegBufferedReader extends BufferedReader {

	public HellwegBufferedReader(Reader reader) {
		super(reader);
	}
	
	/**
	 * Equivalent zu readLine(). Liest einen Chip komplett, d.h. Chip wird erzeugt
	 * mit all seinen Attributen.
	 * @return Chip oder null (wenn Stream Ende oder kein Chip gefunden)
	 * @throws IOException Falls ein IOError in "readLine" auftitt
	 */
	public Chip readChip() throws IOException {
		
		boolean inChipTag = false;
		
		Chip chip = new Chip();
		
		String line;
		while((line = readLine()) != null) {
			// Wenn ein Chip gefunden wurde
			if(line.indexOf("<chip>") != -1 || inChipTag) {
				// true setzen, damit im nächsten Durchlauf wieder hier 
				// hereingesprungen wird.
				inChipTag = true;
				
				if(line.indexOf("<id>") != -1) {
					chip.setId(getContent(line, "id"));
				}
				
				if(line.indexOf("<studentName>") != -1) {
					chip.setStudentName(getContent(line, "studentName"));
				}
				
				// TODO: Runden lesen
				
				if(line.indexOf("</chip>") != -1) {
					inChipTag = false;
					return chip;
				}
			}
		}
		
		// Ende des Streams
		return null;
	}
	
	private String getContent(String line, String tagName) {
		int a = line.indexOf("<" + tagName + ">") + tagName.length() + 2;
		int e = line.indexOf("</" + tagName + ">");
		
		return line.substring(a, e);
	}

}

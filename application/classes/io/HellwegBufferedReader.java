package classes.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import classes.Chip;
import classes.Lap;

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
		boolean inLapsTag = false;
		
		Chip chip = new Chip();
		
		String line;
		while((line = readLine()) != null) {
			// Wenn ein Chip gefunden wurde
			if(line.indexOf("<chip>") != -1 || inChipTag) {
				// true setzen, damit im nächsten Durchlauf wieder hier 
				// hereingesprungen wird.
				inChipTag = true;
				// TODO: nicht in jedem Durchlauf auf true setzen, sondern
				// zu Beginn der Schleife. Nur über die Variablen in die Verzweigung 
				// springen
				
				if(line.indexOf("<id>") != -1) {
					chip.setId(getContent(line, "id"));
					System.out.println(chip.getId()); // Debug
				}
				
				if(line.indexOf("<studentName>") != -1) {
					chip.setStudentName(getContent(line, "studentName"));
				}
				
				// TODO: Runden lesen
				if(line.indexOf("<laps>") != -1 || inLapsTag) {
					inLapsTag = true;
					// TODO: nicht in jedem Durchlauf auf true setzen (s.o.)
					// Außerdem wieder auf false setzen
					if(line.indexOf("<lap ") != -1) {
						chip.getLaps().add(getLapFromTag(line));
					}
				}
				
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
	
	private Lap getLapFromTag(String line) {
		int numberPos = line.indexOf("number") + "number".length() + 2;
		String number = line.substring(numberPos, line.indexOf('"', numberPos));
		
		int timestampPos = line.indexOf("timestamp") + "timestamp".length() + 2;
		String timestamp = line.substring(timestampPos, line.indexOf('"', timestampPos));
		LocalTime lt = LocalTime.parse(timestamp, DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
		
		// TODO: parse-Exception abfangen ? 
		return new Lap(lt, Integer.parseInt(number));
	}

}

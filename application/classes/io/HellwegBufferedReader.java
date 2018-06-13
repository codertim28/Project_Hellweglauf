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
			
			// Prüfen, wo sich der Reader befindet. 
			// - innerhalb eines Chips
			// - in der Rundenliste (<laps> ... </laps)
			if(line.indexOf("<chip>") != -1) {
				// true setzen, damit im nächsten Durchlauf in die
				// die entsprechende Abfrage gesprungen wird.
				inChipTag = true;
			}
			else if(line.indexOf("<laps>") != -1) {
				// true setzen.. (s.o.)
				inLapsTag = true;
			}
			
			// Wenn ein Chip gefunden wurde
			if(inChipTag) {
				
				if(line.indexOf("<id>") != -1) {
					chip.setId(getContent(line, "id"));
					System.out.println(chip.getId()); // Debug
				}
				
				if(line.indexOf("<studentName>") != -1) {
					chip.setStudentName(getContent(line, "studentName"));
				}
				
				// Diese (und die nächste) Abfrage ist an dieser Stelle nicht nötig.
				// Der Quelltext wird auch ohne diese Abfrage ordnungsgemäß arbeiten.
				// Der Grund weshalb diese Abfragen ihre Berechtigung haben: Ich kann
				// mir vorstellen, dass das Programm hier erweitert wird in Zukunft.
			    // Dann könnte sich diese Abrage als hilfreich erweisen.
				if(inLapsTag) {
					
					if(line.indexOf("<lap ") != -1) {
						chip.getLaps().add(getLapFromTag(line));
					}
				}
				
				if(line.indexOf("</laps>") != -1) {
					inLapsTag = false;
				}
				
				// Wird ein Chip geschlossen, so soll das Objekt
				// zurückgegeben werden.
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

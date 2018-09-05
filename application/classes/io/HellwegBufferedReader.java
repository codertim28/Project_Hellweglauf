package classes.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import classes.CompetitionViewRowData;
import classes.model.Chip;
import classes.model.Competition;
import classes.model.CompetitionState;
import classes.model.Lap;

public class HellwegBufferedReader extends BufferedReader {

	public HellwegBufferedReader(Reader reader) {
		super(reader);
	}
	
	private String getContent(String line, String tagName) {
		int a = line.indexOf("<" + tagName + ">") + tagName.length() + 2;
		int e = line.indexOf("</" + tagName + ">");
		
		return line.substring(a, e);
	}
	
	/**
	 * Equivalent zu readLine(). Liest einen Wettkampf komplett, d.h. Wettkampf (Competition) wird erzeugt
	 * mit all seinen Attributen.
	 * @return Competition oder null (wenn Stream Ende oder kein Wettkampf gefunden)
	 * @throws IOException Falls ein IOError in "readLine" auftitt
	 */
	public Competition readCompetition() throws IOException {
		
		boolean inCompTag = false;
		boolean inRowsTag = false;
		
		Competition comp = new Competition();
		
		String line;
		while((line = readLine()) != null) {

			// Prüfen, wo sich der Reader befindet. 
			// - innerhalb eines Wettkampfes
			// - in der Rowliste (<rows> ... </rows)
			if(line.indexOf("<competition>") != -1) {
				// true setzen, damit im nächsten Durchlauf in die
				// die entsprechende Abfrage gesprungen wird.
				inCompTag = true;
			}
			else if(line.indexOf("<rows>") != -1) {
				// true setzen.. (s.o.)
				inRowsTag = true;
			}
			
			// Wenn ein Wettkampf gefunden wurde
			if(inCompTag) {
				
				if(line.indexOf("<name>") != -1) {
					comp.setName(getContent(line, "name"));
				}
				
				if(line.indexOf("<lapLength>") != -1) {
					comp.setLapLength(Integer.parseInt(getContent(line, "lapLength")));
				}
				
				if(line.indexOf("<lapCount>") != -1) {
					comp.setLapCount(Double.parseDouble(getContent(line, "lapCount")));
				}
				
				if(line.indexOf("<time>") != -1) {
					comp.setTime(Integer.parseInt(getContent(line, "time")));
				}
				
				if(line.indexOf("<state>") != -1) {
					switch(getContent(line, "state")) {
						case "PREPARE": 
							comp.setState(CompetitionState.PREPARE);
							break;
						case "RUNNING": 
							comp.setState(CompetitionState.RUNNING);
							break;
						default: 
							comp.setState(CompetitionState.ENDED);
					}
				}
			
				if(inRowsTag) {	
					if(line.indexOf("<row ") != -1) {
						comp.getData().add(getRowFromTag(line));
					}
				}
				
				if(line.indexOf("</rows>") != -1) {
					inRowsTag = false;
				}
				
				// Wird ein Wettkampf geschlossen, so soll das Objekt
				// zurückgegeben werden.
				if(line.indexOf("</competition>") != -1) {
					inCompTag = false;
					return comp;
				}
			}
		}
		
		// Ende des Streams
		return null;
	}
	
	private CompetitionViewRowData getRowFromTag(String line) {
		
		int chipIdPos = line.indexOf("chipId") + "chipId".length() + 2;
		String chipId = line.substring(chipIdPos, line.indexOf('"', chipIdPos));
		
		int studentNamePos = line.indexOf("studentName") + "studentName".length() + 2;
		String studentName = line.substring(studentNamePos, line.indexOf('"', studentNamePos));
		
		int lapNumberPos = line.indexOf("lapNumber") + "lapNumber".length() + 2;
		int lapNumber = Integer.parseInt(line.substring(lapNumberPos, line.indexOf('"', lapNumberPos)));
		
		int timestampPos = line.indexOf("timestamp") + "timestamp".length() + 2;
		String timestamp = line.substring(timestampPos, line.indexOf('"', timestampPos));
		LocalTime lt = LocalTime.parse(timestamp, DateTimeFormatter.ofPattern("HH:mm:ss"));
		
		return new CompetitionViewRowData(chipId, studentName, lt.format(DateTimeFormatter.ofPattern("HH:mm:ss")), lapNumber);
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
				}
				
				if(line.indexOf("<studentName>") != -1) {
					chip.setStudentName(getContent(line, "studentName"));
				}
				
				if(line.indexOf("<form>") != -1) {
					chip.setForm(getContent(line, "form"));
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

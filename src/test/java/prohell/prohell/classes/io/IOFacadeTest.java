package prohell.prohell.classes.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import prohell.prohell.classes.Constants;
import prohell.prohell.classes.model.Chip;
import prohell.prohell.classes.repository.ChipRepository;

// ACHTUNG: ES WIRD IM RICHTIGEN DATEN-VERZEICHNIS GETESTET!
class IOFacadeTest {

	@Test
	void shouldImportChipsFromCorrectCSVFile() throws IOException {
		String testFilePath = getClass().getResource("/test-csv-data.csv").getPath();
		
		IOFacade.importChipsFromCSV(new File(testFilePath));
		
		List<Chip> importedChips = new ChipRepository(Constants.BASIC_CHIPS_FILE_PATH).read();
		
		//importedChips.forEach(c -> System.out.println(c.getId() + " / " + c.getStudentName()));
		
		List<Chip> refChips = new ArrayList<>();
		refChips.add(new Chip("1001", "Mila Böttcher", "05a"));
		refChips.add(new Chip("1002", "Hannah Harting", "05a"));
		refChips.add(new Chip("1003", "Justus Michel", "05a"));
		refChips.add(new Chip("1004", "Amina Schlüter", "05a"));
		refChips.add(new Chip("1005", "Tarik Tekin", "05a"));
		refChips.add(new Chip("1006", "Erik Thorsen", "05a"));
		refChips.add(new Chip("1007", "Lena van Loon", "05a"));
		refChips.add(new Chip("1008", "Jule Ziegler", "05a"));
		
		for(int i = 0; i < importedChips.size(); i++) {
			assertTrue(importedChips.get(i).getId().equals(refChips.get(i).getId()));
			assertTrue(importedChips.get(i).getStudentName().equals(refChips.get(i).getStudentName()));
			assertTrue(importedChips.get(i).getForm().equals(refChips.get(i).getForm()));
		}
	}

}

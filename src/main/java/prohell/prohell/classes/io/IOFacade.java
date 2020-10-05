package prohell.prohell.classes.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import prohell.prohell.classes.Constants;
import prohell.prohell.classes.model.Chip;
import prohell.prohell.classes.repository.ChipsRepository;
import prohell.prohell.classes.view.MainView;

public class IOFacade {

	public static File chooseFile(String title, ExtensionFilter extFilter) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		// fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().addAll(extFilter);
		File selectedFile = fileChooser.showOpenDialog(MainView.get().getStage());
		return selectedFile;
	}

	public static void importChipsFromCSV(File csvFile) {
		try {
			final CSVParser parser = new CSVParserBuilder().withIgnoreQuotations(true).withSeparator(';').build();
			// Kopfinformationen holen, um die Indize zu ermitteln
			CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile)).withCSVParser(parser).build();
			String[] tableHead = reader.readNext();
			reader.close();

			// Inhalte der Tabelle lesen
			final CSVReader mainContentReader = new CSVReaderBuilder(new FileReader(csvFile)).withCSVParser(parser)
					.withSkipLines(1).build();
			List<String[]> tableContent = mainContentReader.readAll();
			mainContentReader.close();

			int chip = 0, vorname = 0, nachname = 0, form = 0;
			// Indize ermitteln, da der Benutzer die
			// Tabelle nicht sortieren muss
			for (int i = 0; i < tableHead.length; i++) {
				switch (tableHead[i].toLowerCase()) {
					case "chip":
						chip = i;
						break;
					case "vorname":
						vorname = i;
						break;
					case "nachname":
						nachname = i;
						break;
					case "klasse":
						form = i;
						break;
					default:
						break;
				}
			}

			List<Chip> chipList = new LinkedList<>();
			// Chips erstellen
			for (String[] row : tableContent) {
				chipList.add(new Chip(row[chip], row[vorname] + " " + row[nachname]));
			}

			ChipsRepository chipRepo = new ChipsRepository(Constants.CHIPS_FILE_PATH);
			chipRepo.write(chipList);

		} catch (IOException | CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

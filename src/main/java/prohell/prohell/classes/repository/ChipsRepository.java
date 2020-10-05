package prohell.prohell.classes.repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import prohell.prohell.classes.Constants;
import prohell.prohell.classes.io.HellwegBufferedReader;
import prohell.prohell.classes.io.HellwegPrintWriter;
import prohell.prohell.classes.model.Chip;
import prohell.prohell.utils.Synchronizer;
import prohell.prohell.utils.repository.MWriteRead;
import prohell.prohell.utils.repository.Repository;

public class ChipsRepository extends Repository {

	public ChipsRepository(String path) {
		super(path);
	}

	/**
	 * Schreibt alle Chips. Wartet dabei (mit join) auf den schreibenden Thread.
	 */
	public boolean write(List<Chip> chips) {

		try (Writer writer = new FileWriter(path)) {

			StatefulBeanToCsv<Chip> beanToCsv = new StatefulBeanToCsvBuilder<Chip>(writer).withSeparator(';').build();
			beanToCsv.write(chips);

			return true;
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			return false;
		}
	}
	
	/**
	 * Es werden alle Chips aus der Chipsdatei gelesen.
	 * Anschließend werden die so erzeugten Chips in eine Liste geschrieben.
	 * @return Eine Liste aller Chips.
	 * @throws IOException - Falls ein IOError auftritt.
	 */
	public List<Chip> read() throws IOException, IllegalStateException {
		List<Chip> chips = new CsvToBeanBuilder<Chip>(new FileReader(path)).withType(Chip.class).withSeparator(';').build().parse();
		return chips;
	}

}

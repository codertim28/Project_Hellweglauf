package prohell.prohell.classes.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;

public class IOFacade {
	
	public static void importChipsFromCSV(File csvFile) {
		try {
			CSVReader reader = new CSVReader(new FileReader(csvFile));
		
			List<String[]> list = reader.readAll();
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

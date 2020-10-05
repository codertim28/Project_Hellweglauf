package prohell.prohell.classes.io;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.opencsv.bean.AbstractCsvConverter;

import prohell.prohell.classes.model.Lap;

public class TextToLap extends AbstractCsvConverter {

    private static final String SEPERATOR = " ";

    @Override
    public Object convertToRead(String value) {
        String[] split = value.split(SEPERATOR);

        if(split.length < 2) {
            return null;
        }

        LocalTime lt;
		try {
			lt = LocalTime.parse(split[1], DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS"));
		}
		catch(Exception e) {
			lt = LocalTime.parse(split[1], DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        int number = 0;
        try {
            number = Integer.parseInt(split[0]);
        }
        catch(Exception e) {}

        return new Lap(lt, number);
    }

    @Override
    public String convertToWrite(Object value) {
        if(value == null) {
            return new String();
        }

        Lap lap = (Lap)value;
        return lap.getNumber() + SEPERATOR + lap.getTimestamp();
    }
}
package classes.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChipTest {
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		Chip chip;
		// Konstruktor testen
		String id = "1a2b3c4d";
		String name = "Owen Grady";
		chip = new Chip(id, name);
		assertTrue(id.equals(chip.getId()), "id nicht gesetzt.");
		assertTrue(name.equals(chip.getStudentName()), "studentName nicht gesetzt.");
		
		// getLapCount() testen
		assertTrue(chip.getLapCount() == Chip.LAPCOUNT_START, "getLapCount() != -2");
		Lap lap = new Lap(LocalTime.now(), -1);
		chip.getLaps().add(lap);
		assertTrue(chip.getLapCount() == lap.getNumber(), "getLapCount() != -1");
	}

}

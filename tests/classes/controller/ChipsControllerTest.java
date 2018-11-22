package classes.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import classes.model.Chip;
import classes.model.Lap;

class ChipsControllerTest {

	ChipsController cc;
	
	@BeforeEach
	void setUp() throws Exception {
		// Testverzeichnis erstellen
		File file = new File("data/test");
		if (!file.exists()) {
			file.mkdir();
		}
		
		cc = new ChipsController("data/test/chips.xml");
		cc.getChips().addAll(IntStream.range(1000, 1050)
				.mapToObj(i -> {
					Chip c = new Chip("" + i, "Testperson " + (i + 1), "None");
					// es muss noch die Runde -1 hinzugefügt werden, da sonst 
					// zunächst keine Doppelscan-Abfrage stattfindet 
					c.getLaps().add(new Lap(LocalTime.now(), c.getLapCount() + 1));
					return c;
				}).collect(Collectors.toList()));
	}

	@AfterEach
	void tearDown() throws Exception {
		File dir = new File("data/test");
		if (dir.exists()) {
			dir.delete();
		}
	}

	@Test
	void test() {
		// Doppelscan
		cc.addLap("1000");
		int result = cc.addLap("1000");
		assertTrue(result == -1, "Doppelscan wird nicht blockiert.");
		
		// Chip nicht vorhanden
		assertTrue(cc.addLap("blabla") == -2, "Nicht vorhandener Chip wird nicht erkannt.");
		
		// 10 Sekunden warten, dann Chip einfügen
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				fail("Thread wurde gestört.");
			}
		});
		t.start(); 
		try {
			t.join();
		} catch (InterruptedException e) {
			fail("join wurde gestört.");
		}
		
		assertTrue(cc.addLap("1000") == 0);
		// lapCount muss jetzt 1 sein
		assertTrue(cc.getChipById("1000").getLapCount() == 1, "Runden werden falsch gezählt.");
	}

}

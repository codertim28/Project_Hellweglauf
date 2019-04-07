package classes;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

class HellwegTimerTest {
	
	private boolean threadNotStopped = false;

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() throws InterruptedException {

		new JFXPanel(); // -> JavaFX-Platform init
		Label l = new Label();
		HellwegTimer ht = new HellwegTimer(5, l, new ProgressBar(), () -> {
			threadNotStopped();
		});
		
		ht.startTimer();
		System.out.println(l.getText());
		Thread.sleep(2000);
		System.out.println(l.getText());
		ht.stopTimer();
		Thread.sleep(2000);
		System.out.println(l.getText());
		
		Thread.sleep(10 * 1000);
		System.out.println(l.getText());
		
		if(threadNotStopped) {
			fail("Thread wurde nicht gestoppt.");
		}
	}
	
	private void threadNotStopped() {
		threadNotStopped = true;
	}

}

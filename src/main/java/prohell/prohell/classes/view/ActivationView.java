package prohell.prohell.classes.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

// Aktivierungsprozess der Software
public class ActivationView {
	
	@FXML private TextField keyField1, keyField2, keyField3;
	
	
	@FXML
	private void keyFieldOnKeyReleased(KeyEvent ev) {
		TextField target = (TextField)ev.getTarget();
		
		// F�r �bergang bei Tab
		if(target.getText().length() == 4) {
			if(target.equals(keyField1)) {
				keyField2.requestFocus();
			}
			else if(target.equals(keyField2)) {
				keyField3.requestFocus();
			}
		}
		
		// Pr�fen, ob Key vollst�ndig eingegeben ist
		// L�nge eines Schl�ssels ist 3 * 4 + 2 = 14
		if(getKey().length() == 14) {
			System.out.println("KEY VOLLST�NDIG");
		}
	}
	
	private String getKey() {
		return keyField1.getText() 
				+ "-" + keyField2.getText()
				+ "-" + keyField3.getText();
	}
}

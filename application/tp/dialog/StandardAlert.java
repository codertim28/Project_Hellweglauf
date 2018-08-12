package tp.dialog;

import javafx.scene.control.Alert;

public class StandardAlert extends Alert {
	
	public StandardAlert(StandardMessageType smt) {
		// Type wird in der init() gesetzt. 
		// Abhängig vom StandardMessageType
		super(AlertType.INFORMATION); 
		init(smt);
	}

	private void init(StandardMessageType smt) {
		// Initialisiert die Texte zu den einzelnen
		// StandardMessageTypes...
		String text;
		switch(smt) { 
			case SUCCESS: 
				text = "Aktion war erfolgreich!";
				super.setAlertType(AlertType.INFORMATION);
				break;
			case ERROR: 
				text = "Es ist ein Fehler aufgetreten!";
				super.setAlertType(AlertType.ERROR);
				break;
			default:
				text = "Es ist ein Fehler aufgetreten!"; // TODO: Stelle aus 1984 eintragen.
		}
		super.setContentText(text);
	}
}

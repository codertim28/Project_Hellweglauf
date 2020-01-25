package prohell.prohell.classes.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.Getter;
import prohell.prohell.ActivationService;
import prohell.prohell.classes.Constants;
import prohell.prohell.utils.logging.SimpleLoggingUtil;

// Aktivierungsprozess der Software
public class ActivationView {
	
	@FXML private TextField keyField1, keyField2, keyField3;
	
	private boolean isActivating = false;
	@Getter
	private boolean softwareActivated = false;
	
	private ActivationService activationService = ActivationService.get();
	
	@Getter
	private Stage stage;
	
	public ActivationView() throws IOException {
		stage = new Stage();
		FXMLLoader templateLoader = new FXMLLoader(getClass().getResource("/templates/activationView.fxml"));
		templateLoader.setController(this);
		stage.setScene(new Scene(templateLoader.load()));
		stage.setResizable(false);
	}
	
	@FXML
	private void testversionLinkOnClick(ActionEvent ev) {
		// Hier true zurück geben, damit MainView geöffnet wird,
		// eine Abfrage auf dem ActivationService wird jedoch weiterhin 
		// false ergeben...
		softwareActivated = true;
        stage.close();
	}
	
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
		
		// Prüfen, ob Key vollständig eingegeben ist
		// Länge eines Schlüssels ist 3 * 4 + 2 = 14
		if(getKey().length() == 14) {
			doActivation();
		}
	}
	
	private void doActivation() { 
		if(isActivating || softwareActivated) return;
		isActivating = true;
		
		String key = getKey();
		
		try {
			// Key hashen
			final String hashedKey = activationService.getAsHash(key);
            
            // Hash in der Datei des aktuellen Jahres suchen		
            boolean keyFound = activationService.getKeyHashesOfCurrentYear().stream().anyMatch(hash -> hash.equals(hashedKey));
            
            if(!keyFound) return;
            
            String processorId = activationService.getProcessorId();
            
            if(processorId == null) throw new Exception("ProcessorId invalid");
            
            // Aktivierungsdatei generieren
            String activationHash = activationService.getAsHash(hashedKey + processorId);
            SimpleDateFormat formatter = ActivationService.FORMATTER;
            String currentDate = formatter.format(Date.from(
            		LocalDateTime.now().plusDays(ActivationService.ACTIVATION_DURATION).toInstant(ZoneOffset.ofHours(2))));
            
            // in der Aktivierungsdatei steht:
            // hash(hashedKey + processorId) 
            // Datum der Aktivierung
            String fileContent = activationHash + "\n" + currentDate;
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(getClass().getResource("/keys/activation").toURI()));
            writer.write(fileContent);  
            writer.close();
            
            softwareActivated = true;
            // TODO: Benutzer benarichtigen
            stage.close();
		} catch (Exception e) {
			// Wird/Sollte niemals vorkommen, da es den gewählten
			// Algoritmus gibt und dieser weit verbreitet ist...
			new SimpleLoggingUtil(new File(Constants.logFilePath())).error(e);
			e.printStackTrace();
		}
		
		isActivating = false;
	}
	
	private String getKey() {
		return keyField1.getText() 
				+ "-" + keyField2.getText()
				+ "-" + keyField3.getText();
	}

}

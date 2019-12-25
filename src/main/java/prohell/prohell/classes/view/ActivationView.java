package prohell.prohell.classes.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import prohell.prohell.classes.Constants;
import prohell.prohell.utils.logging.SimpleLoggingUtil;

// Aktivierungsprozess der Software
public class ActivationView {
	
	@FXML private TextField keyField1, keyField2, keyField3;
	
	private boolean isActivating = false;
	
	@FXML
	private void keyFieldOnKeyReleased(KeyEvent ev) {
		TextField target = (TextField)ev.getTarget();
		
		// Für Übergang bei Tab
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
		if(isActivating) return;
		isActivating = true;
		
		String key = getKey();
		
		try {
            String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			// Key hashen
			final String hashedKey = getAsHash(key);
            
            // Hash in der Datei des aktuellen Jahres suchen
            URI uri = getClass().getResource("/keys/hashes" + year).toURI();
            List<String> lines = Files.readAllLines(Paths.get(uri), Charset.defaultCharset());
			
            boolean keyFound = lines.stream().anyMatch(hash -> hash.equals(hashedKey));
            
            if(!keyFound) return;
            
            String processorId = getCPUProcessorId();
            
            if(processorId == null) throw new Exception("ProcessorId invalid");
            
            // Aktivierungsdatei generieren
            String activationHash = getAsHash(hashedKey + processorId);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
            String currentDate = formatter.format(new Date());
            
            // in der Aktivierungsdatei steht:
            // hash(hashedKey + processorId) 
            // Datum der Aktivierung
            String fileContent = activationHash + "\n" + currentDate;
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(getClass().getResource("/keys/activation").toURI()));
            writer.write(fileContent);  
            writer.close();
            
		} catch (Exception e) {
			// Wird/Sollte niemals vorkommen, da es den gewählten
			// Algoritmus gibt und dieser weit verbreitet ist...
			new SimpleLoggingUtil(new File(Constants.logFilePath())).error(e);
		}
		
		isActivating = false;
	}
	
	private String getKey() {
		return keyField1.getText() 
				+ "-" + keyField2.getText()
				+ "-" + keyField3.getText();
	}
	
	private String getAsHash(String str) throws NoSuchAlgorithmException {
		// Hash erstellen
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] bytes = md.digest(str.getBytes(StandardCharsets.UTF_8));
		
		StringBuilder sb = new StringBuilder();
        for(int i = 0; i< bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
	
	private String getCPUProcessorId() {

		try {
			InputStream stream = Runtime.getRuntime().exec("wmic cpu get ProcessorId").getInputStream();
		
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
				return reader.lines().collect(Collectors.toList()).stream()
	        			.filter(s -> !s.contains("ProcessorId")).reduce((s1, s2) -> {
	        		return s1 + s2;
	        	}).get();
	        }
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

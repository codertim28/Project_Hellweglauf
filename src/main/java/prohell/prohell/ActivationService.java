package prohell.prohell;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

// Überprüft, ob die Software aktiviert ist
public class ActivationService {
	
	private static ActivationService INSTANCE;
	
	public static final int ACTIVATION_DURATION = 365; // in Tagen
	public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	
	@Getter
	private Date activationUntil;
	
	private ActivationService() { }
	
	public static ActivationService get() {
		if(INSTANCE == null) INSTANCE = new ActivationService();
		return INSTANCE;
	}
	
	public boolean isSoftwareActivated() {
		// TODO: Fehler loggen
		List<String> hashes = getKeyHashesOfCurrentYear();
		final String processorId = getProcessorId();
		
		try {

			// in der 1. Zeile steht der Hash in der zweiten Zeile das Datum
			final List<String> lines = new ArrayList<>(); 
			try (InputStream resource = new FileInputStream("data/activation")) {
				lines.addAll(new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))
					.lines().collect(Collectors.toList()));
			}
				
			if(lines == null || lines.size() == 0) return false;
			
			// Software ist aktiviert, wenn (1)  1 Hash + ProcessorId
			// dem generierten Hash gleichen und (2) das Datum nicht überschritten wurde
			
			// Fall (2) zuerst, dann kann man sich ggf. das Suchen ersparen
			activationUntil = FORMATTER.parse(lines.get(1));
			if(activationUntil.before(new Date())) {
				return false;
			}
			
			// Fall (1)
			return hashes.stream().anyMatch(hash -> {
				return lines.get(0).equals(getAsHash(hash + processorId));
			});
			
		} catch (IOException | ParseException e) {
			// todo
			e.printStackTrace();
		}
		
		return false;		
	}
	
	public List<String> getKeyHashesOfCurrentYear() {
		try {
			String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			try (InputStream resource = getClass().getResourceAsStream("/keys/hashes" + year)) {
				return new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
			}
		} catch (IOException ioe) {
			return null;
		}
	}
	
	public String getAsHash(String str) {
		// Hash erstellen
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytes = md.digest(str.getBytes(StandardCharsets.UTF_8));
			
			StringBuilder sb = new StringBuilder();
	        for(int i = 0; i< bytes.length; i++) {
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public String getProcessorId() {

		try {
			// todo: OS unterscheiden
			//InputStream stream = Runtime.getRuntime().exec("wmic cpu get ProcessorId").getInputStream();
			InputStream stream = Runtime.getRuntime().exec("sysctl -n machdep.cpu.signature").getInputStream();
		
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

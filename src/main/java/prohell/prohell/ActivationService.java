package prohell.prohell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// Überprüft, ob die Software aktiviert ist
public class ActivationService {
	
	private static ActivationService INSTANCE;
	
	public static final int ACTIVATION_DURATION = 365; // in Tagen
	public static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
	
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
			URI uri = getClass().getResource("/keys/activation").toURI();
			List<String> lines = Files.readAllLines(Paths.get(uri), Charset.defaultCharset());
				
			if(lines == null || lines.size() == 0) return false;
			
			// Software ist aktiviert, wenn (1)  1 Hash + ProcessorId
			// dem generierten Hash gleichen und (2) das Datum nicht überschritten wurde
			
			// Fall (2) zuerst, dann kann man sich ggf. das Suchen ersparen
			Date activatitionUntil = FORMATTER.parse(lines.get(1));
			if(activatitionUntil.before(new Date())) {
				return false;
			}
			
			// Fall (1)
			return hashes.stream().anyMatch(hash -> {
				return lines.get(0).equals(getAsHash(hash + processorId));
			});
			
		} catch (URISyntaxException | IOException | ParseException e) {
			// todo
			e.printStackTrace();
		}
		
		return false;		
	}
	
	public List<String> getKeyHashesOfCurrentYear() {
		try {
			String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
			URI uri = getClass().getResource("/keys/hashes" + year).toURI();
			return Files.readAllLines(Paths.get(uri), Charset.defaultCharset());
		} catch (URISyntaxException | IOException e) {
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

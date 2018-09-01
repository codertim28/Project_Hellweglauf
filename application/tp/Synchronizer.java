package tp;

public final class Synchronizer {

	/*
	 * Diese Klasse dient als Monitor Objekt. 
	 * Dabei wird die sync()-Methode verwendet,
	 * um den kritischen Abschnitt auszuführen 
	 * (per Runnabel).
	 */
	
	public static void sync(Runnable criticalSection) {
		criticalSection.run();
	}
}

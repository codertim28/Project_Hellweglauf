package prohell.prohell.utils;

public final class Synchronizer {

	/*
	 * Diese Klasse dient als Monitor Objekt. 
	 * Dabei wird die sync()-Methode verwendet,
	 * um den kritischen Abschnitt auszuf�hren 
	 * (per Runnabel).
	 */
	
	public static void sync(Runnable criticalSection) {
		criticalSection.run();
	}
}

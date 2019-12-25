package prohell.prohell.utils.repository;

import java.io.IOException;

// Single Write Read:
// Wenn ein Repository nur ein Objekt
// pro Schreiben schreibt.
public interface SWriteRead<T> {
	public abstract boolean write(T obj);
	public abstract T read() throws IOException;
}

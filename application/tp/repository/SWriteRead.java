package tp.repository;

import java.io.IOException;

// Single Write Read:
// Wenn ein Repository nur ein Objekt
// pro Schreiben schreibt.
public interface SWriteRead<T> {
	public abstract void write(T obj) throws IOException;
	public abstract T read() throws IOException;
}

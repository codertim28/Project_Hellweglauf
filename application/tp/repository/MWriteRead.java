package tp.repository;

import java.io.IOException;
import java.util.List;

// Multiple Write Read:
// Wenn ein Repository mehrere Objekte
// pro Schreiben schreibt.
public interface MWriteRead<T> {
	public abstract boolean write(List<T> objects);
	public abstract void writeAsync(List<T> objects) throws IOException;
	public abstract List<T> read() throws IOException;
}

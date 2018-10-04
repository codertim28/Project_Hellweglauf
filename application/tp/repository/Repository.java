package tp.repository;

import java.io.IOException;
import java.util.List;

public abstract class Repository {
	
	protected String path;

	public Repository(String path) {
		setPath(path);
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}

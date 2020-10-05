package prohell.prohell.utils.repository;

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

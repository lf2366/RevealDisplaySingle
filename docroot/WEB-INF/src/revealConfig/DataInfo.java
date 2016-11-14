package revealConfig;

public class DataInfo {
	
	private long id;
	private String url;
	private long parentsFolderId;
	private String display;
	private int type;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public DataInfo() {
		id = -1;
		url = null;
		display = null;
	}
	
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public long getId() {
		return id;
	}
	public void setId(long l) {
		this.id = l;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public long getParentsFolderId() {
		return parentsFolderId;
	}

	public void setParentsFolderId(long parentsFolderId) {
		this.parentsFolderId = parentsFolderId;
	}

}



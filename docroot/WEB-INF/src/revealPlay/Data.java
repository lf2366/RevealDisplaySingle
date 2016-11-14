package revealPlay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> display;
	private List<String> URL;
	
	public Data() {
		display = new ArrayList<String>();
		URL = new ArrayList<String>();
	}
	
	public List<String> getDisplay() {
		return display;
	}
	public void setDisplay(List<String> display) {
		this.display = display;
	}
	public List<String> getURL() {
		return URL;
	}
	public void setURL(List<String> uRL) {
		URL = uRL;
	}
	
	public void clear() {
		display.clear();
		URL.clear();
	}
}

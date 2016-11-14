package revealConfig;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import com.liferay.faces.util.portal.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;

@ManagedBean(name = "revealPrefs")
@ViewScoped
public class RevealPreferences {
	private String choose;
	private String playURL;
	private String height;
	private String width;

	public RevealPreferences() {
		PortletRequest request = (PortletRequest) FacesContext.getCurrentInstance()
		.getExternalContext().getRequest();
		choose = "";
		
		try {
			//get portletPreferences
			PortletPreferences prefs = (PortletPreferences) PortletPreferencesUtil.getPortletPreferences(request);
			
			//get uuid
			ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
			String uuid = themeDisplay.getUser().getUserUuid();
			
			//get select item
			String URL = prefs.getValue(uuid + "_revealPlaySingle_URL", "");
			
			playURL = URL;
			
			//get display size
			height = prefs.getValue(uuid + "_revealPlaySingle_height", "420");
			width = prefs.getValue(uuid + "_revealPlaySingle_width", "576");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getChoose() {
		return choose;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}

	public String getPlayURL() {
		return playURL;
	}

	public void setPlayURL(String playURL) {
		this.playURL = playURL;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
}

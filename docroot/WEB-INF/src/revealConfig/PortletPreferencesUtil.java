package revealConfig;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

public class PortletPreferencesUtil {
	public static PortletPreferences getPortletPreferences(PortletRequest request) 
    		throws PortalException, SystemException {
    	PortletPreferences prefs = request.getPreferences();
    	String portletResource = ParamUtil.getString(request, "portletResource");
    	if (Validator.isNotNull(portletResource)) {
    		prefs = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
    	}
    	return prefs;
    }
}

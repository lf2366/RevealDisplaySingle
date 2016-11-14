package revealConfig;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import javax.servlet.http.HttpSession;

import com.liferay.faces.util.portal.WebKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;

import revealConfig.SelectedData;
import revealPlay.Data;

public class ConfigurationAction extends DefaultConfigurationAction {
	
    @Override
    public void processAction(
        PortletConfig portletConfig, ActionRequest actionRequest,
        ActionResponse actionResponse) throws Exception {
    	
    	String type = actionRequest.getParameter("buttonType");

		HttpSession session = PortalUtil.getHttpServletRequest(actionRequest).getSession();
		
        //get object from session
        SelectedData selectedData = (SelectedData) session.getAttribute("selectedDataSingle");
        Data playData = (Data)session.getAttribute("playDataSingle");
            
        if("add".equals(type) || "dblclick".equals(type)) {
        	
        	//get chooseIndex
            String[] chooseValue = actionRequest.getParameter("selectView").split("_");
            int chooseIndex = Integer.parseInt(chooseValue[0]);
            int chooseType = -1;
            
            if(chooseIndex != -1)
            	chooseType = selectedData.getDataList().get(chooseIndex).getType();
        	
        	selectedData.action(chooseIndex);
        	
            //if selected item is file
            if(chooseType == 1) {
            	
            	//add new data to playData
            	playData.clear();
            	playData.getDisplay().add(selectedData.getDataList().get(chooseIndex).getDisplay());
            	playData.getURL().add(selectedData.getDataList().get(chooseIndex).getUrl());
            	
            	String display = playData.getDisplay().get(0);
            	String URL = playData.getURL().get(0);
            	
            	updatePreference(actionRequest, display, URL);

            	session.setAttribute("playDataSingle", playData);
            	
            	SessionMessages.add(actionRequest,
    					PortalUtil.getPortletId(actionRequest) +
    					SessionMessages.KEY_SUFFIX_UPDATED_CONFIGURATION);
            }
            session.setAttribute("selectedDataSingle", selectedData);
        }
    	
    	else if("reSize".equals(type)) {
    		String height = actionRequest.getParameter("revealHeight");
    		String width = actionRequest.getParameter("revealWidth");
    		updatePreferenceOfSize(actionRequest, height, width);
    		
    		SessionMessages.add(actionRequest,
					PortalUtil.getPortletId(actionRequest) +
					SessionMessages.KEY_SUFFIX_UPDATED_CONFIGURATION);
    	}
        
        //redirect
        String redirect = PortalUtil.escapeRedirect(
				ParamUtil.getString(actionRequest, "redirect"));
		if (Validator.isNotNull(redirect))
			actionResponse.sendRedirect(redirect);
    }
    
    private void updatePreference(ActionRequest actionRequest, String display, String URL) throws ReadOnlyException, ValidatorException, IOException, PortalException, SystemException {
    	PortletPreferences prefs = PortletPreferencesUtil.getPortletPreferences(actionRequest);
    	ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
    	String uuid = themeDisplay.getUser().getUserUuid();
    	
    	//save preferences
    	prefs.setValue(uuid + "_revealPlaySingle_display", display);
    	prefs.setValue(uuid + "_revealPlaySingle_URL", URL);
    	prefs.store();
    }
    
    private void updatePreferenceOfSize(ActionRequest actionRequest, String height, String width) throws PortalException, SystemException, ReadOnlyException, ValidatorException, IOException {
    	PortletPreferences prefs = PortletPreferencesUtil.getPortletPreferences(actionRequest);
    	ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
    	String uuid = themeDisplay.getUser().getUserUuid();
    	
    	//save preferences
    	prefs.setValue(uuid + "_revealPlaySingle_height", height);
    	prefs.setValue(uuid + "_revealPlaySingle_width", width);
    	prefs.store();
    }
}

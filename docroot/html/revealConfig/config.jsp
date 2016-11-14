<%@page import="java.util.HashMap"%>
<%@page import="com.liferay.portal.model.PortletPreferences"%>
<%@page import="javax.portlet.faces.preference.Preference"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet"%>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui"%>
<%@ page import="com.liferay.portal.kernel.util.Constants"%>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@ page import="com.liferay.portal.kernel.util.StringPool"%>
<%@ page import="com.liferay.portal.theme.ThemeDisplay" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="javax.portlet.PortletSession" %>

<%@ page import="revealConfig.SelectedData" %>
<%@ page import="revealPlay.Data" %>
<%@ page import="revealConfig.PortletPreferencesUtil" %>

<liferay-theme:defineObjects />
<portlet:defineObjects />

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>

<style type="text/css">
	.css_table {
		display: table;
	}
	.css_tr {
		display: table-row;
	}
	.css_td {
		display: table-cell;
	}
	.css_td_vertical {
		display: table-cell;
		vertical-align: middle;
	}
	.button_width {
		width: 70px;
	}
	.select_height {
		height: 230px;
	}
	.header_bgcolor {
		background: #e5e5e5;
		color: #999;
		padding-left: 8px;
	}
	.border {
		border-width: 1px;
		border-style: solid;
		border-color: #e5e5e5;
	}
	.expandContent:before {
		content: attr(data-content);
	}
	.expandContent1:before {
		content: attr(data-content);
	}
</style>

<%
	SelectedData selectData = null;
	Data playData = null;
	if(session.getAttribute("selectedDataSingle") == null) {
		selectData = new SelectedData(themeDisplay);
		session.setAttribute("selectedDataSingle", selectData);
	}
	else {
		try {
			selectData = (SelectedData)session.getAttribute("selectedDataSingle");
			selectData.update();
		} catch(Exception e) {
			selectData = new SelectedData(themeDisplay);
			session.setAttribute("selectedDataSingle", selectData);
		}
	}
	if(session.getAttribute("playDataSingle") == null) {
		playData = new Data();
		String[] display = (String[])portletPreferences.getValues(themeDisplay.getUser().getUuid() + "_revealPlaySingle_display", null);
		String[] URL = (String[])portletPreferences.getValues(themeDisplay.getUser().getUuid() + "_revealPlaySingle_URL", null);
		
		if(display != null) {
			for(int i=0 ; i<display.length ; i++) {
				playData.getDisplay().add(display[i]);
			}
		}
		if(URL != null) {
			for(int i=0 ; i<URL.length ; i++) {
				playData.getURL().add(URL[i]);
			}
		}
		//save playData to session
		session.setAttribute("playDataSingle", playData);
	}
	else {
		try {
			playData = (Data)session.getAttribute("playDataSingle");
		} catch(Exception e) {
			playData = new Data();
			String[] display = (String[])portletPreferences.getValues(themeDisplay.getUser().getUuid() + "_revealPlaySingle_display", new String[]{""});
			String[] URL = (String[])portletPreferences.getValues(themeDisplay.getUser().getUuid() + "_revealPlaySingle_URL", new String[]{""});
			
			for(int i=0 ; i<display.length ; i++) {
				if(!display[i].equals(""))
					playData.getDisplay().add(display[i]);
			}
			for(int i=0 ; i<URL.length ; i++) {
				if(!URL[i].equals(""))
					playData.getURL().add(URL[i]);
			}
			//save playData to session
			session.setAttribute("playDataSingle", playData);
		}
	}
%>
</head>
	<body>
		<liferay-portlet:actionURL portletConfiguration="true"
			var="configurationURL" />
		
		<div id="configFormToggler" class="border">
			<div class="header_bgcolor" onclick="changeToggle()">
				<h4 class="expandContent">Play List Configuration</h4>
				<h4 class="header header_bgcolor"></h4>
			</div>
			<aui:form cssClass="content" action="<%=configurationURL%>" method="post" name="configForm">
				<aui:input name="buttonType" type="hidden" value="dblclick" />
				<div class="css_table">
					<div class="css_tr">
						<div class="css_td">
							<aui:select size="10" name="selectView" id="selectView" multiple="true" label="Please select a reveal file">
							<aui:option value="-1_0" selected="true" label=".." />
							<%
							for(int i=0 ; i<selectData.getDataList().size() ; i++) { 
							%>
								<!-- value = index_dataType -->
								<%String value = Integer.toString(i) + "_" + selectData.getDataList().get(i).getType(); %>
								<aui:option value="<%=value %>"
								label="<%=selectData.getDataList().get(i).getDisplay() %>" />
							<%
							}
							%>
							</aui:select>
						</div>
					</div>
				</div>
			</aui:form>
		</div>
		
		<div id="sizeFormToggler" class="border">
			<div class="header_bgcolor" onclick="changeToggle()">
				<h4 class="expandContent1">Display Size Configuration</h4>
				<h4 class="header1 header_bgcolor"></h4>
			</div>
			<aui:form cssClass="content1" action="<%=configurationURL%>" method="post" name="sizeForm">
				<aui:input name="revealHeight" type="nnumber" label="Height" />
				<aui:input name="revealWidth" type="nnumber" label="Width" />
				<aui:input name="buttonType" type="hidden" value="reSize" />
				<aui:button name="sizeSave" type="submit" value="save" />
			</aui:form>
		</div>
		
		
		<aui:script>
			AUI().use('aui-base', function(A) {
				A.one("#<portlet:namespace/>selectView").on('dblclick',function(){
					A.one("#<portlet:namespace/>configForm").submit();
				})
			})
			
			AUI().use('aui-toggler', function(A) {
				new A.Toggler({
					animated: true,
					content: '.content',
					header: '.header',
					expanded: true,
				});
				$('.expandContent').attr('data-content', '-');
				
				new A.Toggler({
					animated: true,
					content: '.content1',
					header: '.header1',
					expanded: false,
				});
				$('.expandContent1').attr('data-content', '+');
				
			})
			
			function changeToggle() {
				AUI().use('aui-toggler', function(A) {
					var toggle = A.one('.header').getData('toggler');
					var expanded = toggle.get('expanded');
					if(expanded) {
						$('.expandContent').attr('data-content', '+');
						toggle.collapse();
					}
					else {
						$('.expandContent').attr('data-content', '-');
						toggle.expand();
					}
					
					toggle = A.one('.header1').getData('toggler');
					expanded = toggle.get('expanded');
					if(expanded) {
						$('.expandContent1').attr('data-content', '+');
						toggle.collapse();
					}
					else {
						$('.expandContent1').attr('data-content', '-');
						toggle.expand();
					}
				})
			}
		</aui:script>

	</body>
</html>
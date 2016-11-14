package revealConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import com.liferay.faces.util.portal.WebKeys;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileEntryType;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryTypeLocalServiceUtil;

public class SelectedData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<DataInfo> dataList;
	private long scopeGroupId;
	private ThemeDisplay themeDisplay;
	private Long parentsFolderId;
	private List<Long> parentsFolderIdArray;
	private String URL;
	
	public SelectedData() throws PortalException, SystemException {
		themeDisplay = (ThemeDisplay) FacesContext.getCurrentInstance()
				.getExternalContext().getRequestMap()
				.get(WebKeys.THEME_DISPLAY);
		scopeGroupId = themeDisplay.getScopeGroupId();
		parentsFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
		
		if(parentsFolderIdArray == null)
			parentsFolderIdArray = new ArrayList<Long>();
		parentsFolderIdArray.add(new Long(0));

		setDataList();
		URL = "";
	}
	
	public SelectedData(ThemeDisplay themeDisplay) throws PortalException, SystemException {
		this.themeDisplay = themeDisplay;
		scopeGroupId = themeDisplay.getScopeGroupId();
		parentsFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;
		
		if(parentsFolderIdArray == null)
			parentsFolderIdArray = new ArrayList<Long>();
		parentsFolderIdArray.add(new Long(0));

		setDataList();
		URL = "";
	}
	
	public Long getParentFolderId() {
		return parentsFolderId;
	}

	public void setParentFolderId(Long parentsFolderId) {
		this.parentsFolderId = parentsFolderId;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public List<DataInfo> getDataList() {
		return dataList;
	}

	public void setDataList(List<DataInfo> dataList) {
		this.dataList = dataList;
	}
	
	public void update() throws PortalException, SystemException {
		setDataList();
	}
	
	//initial option data list
	private void setDataList() throws PortalException, SystemException {
		if(dataList == null)
			dataList = new ArrayList<DataInfo>();
		else
			dataList.clear();
		
		List<Folder> folders = null;
		List<FileEntry> files = null;
		
		try {
			folders = DLAppServiceUtil.getFolders(scopeGroupId,
					parentsFolderId);
			files = DLAppServiceUtil.getFileEntries(scopeGroupId,
					parentsFolderId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataInfo data = null;
		
		//抓取資料夾
		for (int index = 0; index < folders.size(); index++) {
			data = new DataInfo();
			data.setDisplay(folders.get(index).getName());
			data.setId(folders.get(index).getFolderId());
			data.setParentsFolderId(parentsFolderId);
			data.setUrl(themeDisplay.getPortalURL()
					+ themeDisplay.getPathContext() + "/documents/"
					+ scopeGroupId + "/" + folders.get(index).getFolderId());
			data.setType(0);
			
			dataList.add(data);
		}
		
		//抓取檔案
		for (int index = 0; index < files.size(); index++) {
			//type是reveal才處理
			if(isRevealFile(files.get(index))) {
				data = new DataInfo();
				data.setDisplay(files.get(index).getTitle());
				data.setId(files.get(index).getFileEntryId());
				data.setParentsFolderId(parentsFolderId);
				data.setUrl(themeDisplay.getPortalURL()
						+ themeDisplay.getPathContext() + "/documents/"
						+ scopeGroupId + "/" + parentsFolderId + "/"
						+ files.get(index).getTitle());
				data.setType(1);
	
				dataList.add(data);
			}
		}
	}
	
	private boolean isRevealFile(FileEntry file) throws PortalException, SystemException {
		DLFileEntry dlFileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(file.getFileEntryId());
		long typeId = dlFileEntry.getFileEntryTypeId();
		DLFileEntryType dlFileEntryType = DLFileEntryTypeLocalServiceUtil.getFileEntryType(typeId);
		String type = dlFileEntryType.getName(dlFileEntryType.getDefaultLanguageId());
		
		//無視大小寫差異
		type = type.toUpperCase();
		if(!"REVEAL".equals(type))	return false;
		
		return true;
	}

	//更改路徑,刷新list
	public void action(int chooseIndex) throws PortalException, SystemException {
		//判斷選擇的是否為回上層
		if(chooseIndex != -1) {
			DataInfo chooseDataInfo = dataList.get(chooseIndex);
			
			//選擇的是資料夾(0) or 檔案(1)
			if(chooseDataInfo.getType() == 0) {
				parentsFolderId = chooseDataInfo.getId();
				parentsFolderIdArray.add(parentsFolderId);
				URL = "";
			}
			else {
				URL = chooseDataInfo.getUrl();
			}
		}
		else {
			URL = "";
			int lastIndex = parentsFolderIdArray.size()-1;
			
			//取得上層資料夾，如果已是最上層(size==1)，則不做事
			if(lastIndex != 0) {
				parentsFolderId = parentsFolderIdArray.get(lastIndex-1);
				parentsFolderIdArray.remove(lastIndex);
			}
			else	return;
		}
		
		setDataList();
	}
}

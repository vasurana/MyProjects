package in.com.app;

import java.util.ArrayList;

import in.com.app.data.LogData;
import in.com.app.data.SignageData;
import in.com.app.domain.DisplayLayoutFile;
import in.com.app.domain.DisplayLayoutFiles;
import in.com.app.storage.caching.sqlight.manager.DataCacheManager;
import in.com.app.utility.Utility;
import in.com.app.wsdl.XMDS;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.os.AsyncTask;
import android.util.Log;

/**
 * This class is wrapper for a asyncTask that downloads list of files used in the current layout from server.
 * @author Ravi@Xvidia
 * @since version 1.0
 *
 */

public class ServerGetFiles extends AsyncTask<String, Void, DisplayLayoutFiles> implements IDisplayLayout{

AppMain appmainInstance = null;
	
ServerGetFiles(AppMain appmain){
		appmainInstance = appmain;
	}

	@Override
	protected DisplayLayoutFiles doInBackground(String... arg0) {

		XMDS xmds = new XMDS(ClientConnectionConfig._SERVERURL);
		String strReqFiles = "";
		if(ClientConnectionConfig._UNIQUE_SERVER_KEY!=null && ClientConnectionConfig._HARDWAREKEY!=null){
			strReqFiles = xmds.RequiredFiles(ClientConnectionConfig._UNIQUE_SERVER_KEY, ClientConnectionConfig._HARDWAREKEY, ClientConnectionConfig._VERSION);

			/////IF 1st REQUEST ASSISGN VALUE FROM DB///////////////////////////////////////////////
			if( (ClientConnectionConfig.LAST_REQUEST_DATA).trim().equals("")){
				ClientConnectionConfig.LAST_REQUEST_DATA =LogData.getInstance().getCurrentDisplayFiles(AppMain.getAppMainContext());//DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_CURRENT_DISPLAY_XML);
				if(!ClientConnectionConfig.LAST_REQUEST_DATA.equals("") &&!ClientConnectionConfig.LAST_REQUEST_DATA.equalsIgnoreCase(LogData.STR_UNKNOWN)){
					StateMachine.gi(appmainInstance).fillResourceMapIfOffline(ClientConnectionConfig.LAST_REQUEST_DATA);
				}
			} 
			//////////////////////////////////////////////////////////////////
			if(ClientConnectionConfig.LAST_REQUEST_DATA.equalsIgnoreCase(strReqFiles)){
				appmainInstance.noChange = true;
			}else{
				ClientConnectionConfig.LAST_REQUEST_DATA = strReqFiles;	
				appmainInstance.downloadFailed = false;
				DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_NEW_DISPLAY_XML, strReqFiles);
				LogData.getInstance().setCurrentDisplayFilesXml(AppMain.getAppMainContext(), strReqFiles);
				DataCacheManager.getInstance(AppMain.getAppMainContext()).saveSettingData(_KEY_XVIDIA_STATE_FILE_COMPLETE, FLAG_TRUE);
			}
		}else if(!ClientConnectionConfig.LAST_REQUEST_DATA.equals("")){
			strReqFiles = ClientConnectionConfig.LAST_REQUEST_DATA;				
		}else{
			return null;
		}
		Log.d(appmainInstance.TAG, " RequiredFiles::: " + strReqFiles);

		Serializer serializer = new Persister();
		DisplayLayoutFiles lstFiles = null;

		try {
			lstFiles = serializer.read(DisplayLayoutFiles.class, strReqFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}

		SignageData.getInstance().setLayoutFiles(lstFiles);
		return lstFiles;
	}

	@Override
	protected void onPostExecute(DisplayLayoutFiles lstFiles) {
		boolean nextStepFlag = false;
		if(lstFiles!=null){
			ArrayList<DisplayLayoutFile> files = lstFiles.getFileList();
			ArrayList<DisplayLayoutFile> filesToDownloadNow = new ArrayList<DisplayLayoutFile>();
			ArrayList<DisplayLayoutFile> filesToDownloadLater= new ArrayList<DisplayLayoutFile>();
			ArrayList<String> filesCurrent = SignageData.getInstance().getCurrentLayoutFiles();
			
			if(filesCurrent != null && files!=null){
				for(DisplayLayoutFile fileObj:files){
					String name = fileObj.getPath();
					boolean downloadNow = false;
					if(name!=null && !name.isEmpty()){
						for(String fileName: filesCurrent){
							if(name.equals(fileName) || fileObj.getType().equals(IDisplayLayout._FILE_TYPE_LAYOUT)){
								downloadNow = true;
								break;
							}
						}
						if(downloadNow){
							if(fileObj.getType().equals(IDisplayLayout._FILE_TYPE_LAYOUT)){
								filesToDownloadNow.add(0,fileObj);	
							}else{
								filesToDownloadNow.add(fileObj);	
							}
						}else{
							filesToDownloadLater.add(fileObj);	
						}
					}
				}
				StateMachine.gi(appmainInstance).receivedDownloadFiles = filesToDownloadNow;
				StateMachine.gi(appmainInstance).receivedFilesDownloadLater = filesToDownloadLater;
				LogData.getInstance().setDownloadPending(appmainInstance, true);
			}else{
				StateMachine.gi(appmainInstance).receivedDownloadFiles = files;
				StateMachine.gi(appmainInstance).receivedFilesDownloadLater = null;
				LogData.getInstance().setDownloadPending(appmainInstance, false);
			}
			nextStepFlag = true;

		}
		boolean individualFileSuccess = Utility.checkBooleanState(DataCacheManager.getInstance(AppMain.getAppMainContext()).readSettingData(_KEY_XVIDIA_STATE_INDIVIDUALFILE_COMPLETE));
		
		if(appmainInstance.noChange && !appmainInstance.downloadFailed && individualFileSuccess){
			StateMachine.gi(appmainInstance).initProcess(nextStepFlag, StateMachine.SHOWDISPLAY);
		}else{
			StateMachine.gi(appmainInstance).initProcess(nextStepFlag, StateMachine.GETINDIVIDUALFILE);
		}
	}
}


package com.tv.ui.CiInformation;

import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.CiUIEventData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.plugin.tvfuncs.CiManager.CiUIEventListener;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog;

public class CIInformationDialog extends TvBaseDialog
{

	private CIInformationView ciInformationView;
	private DialogListener dialogListener;
	
	public CIInformationDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_CI_INFORMATION);
		// TODO Auto-generated constructor stub		
		ciInformationView = new CIInformationView(TvContext.context);
		ciInformationView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(ciInformationView);
		setAutoDismiss(false);		
	}
	
	public void setDismissTimer(boolean is){
		setAutoDismiss(is);
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return true;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		Log.i("gky", getClass().toString() + "-->processCmd");
		Boolean isFromActivity = (Boolean)obj;
		switch (cmd) {
		case DIALOG_SHOW:
			int ciCardStatus = TvConfigTypes.CARD_STATE_NO;
			ciCardStatus = TvPluginControler.getInstance().getCilManager().getCiCardStatus();
			Log.i("gky", getClass().toString() + "-->updateView::ciCardStatus is "+ciCardStatus);
			if(ciCardStatus == TvConfigTypes.CARD_STATE_READY)
			{
				TvPluginControler.getInstance().getCilManager().initListener(ciUIEventListener);
				if(isFromActivity != null && !isFromActivity){
					Log.i("gky", getClass().toString() + "-pxj1->processcmd enterMenu from TvSettingItem");
					TvPluginControler.getInstance().getCilManager().enterMenu();
				}
				if(TvPluginControler.getInstance().getCilManager().isDataExisted())
					TvPluginControler.getInstance().getCilManager().UiDataReady();
			}
			else {
				Log.i("gky", getClass().getSimpleName() + "------------ciCardStatus != TvConfigTypes.CARD_STATE_READY");
				TvUIControler.getInstance().showMniToast("No CI-Card");
				this.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
				
			}
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			Log.i("gky", getClass().getSimpleName() + "----dismiss-----");
			Object[] objects = null;
			TvPluginControler.getInstance().getCilManager().close();
			TvPluginControler.getInstance().getCilManager().releaseListener();
			TvPluginControler.getInstance().getCommonManager().enableHotkey(true);
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			//TvUIControler.getInstance().showMniToast("No CI-Card");
			break;
		default:
			break;
		}
		return false;
	}
	
	public CiUIEventListener ciUIEventListener = new CiUIEventListener() {
		
		@Override
		public void onCiUIEventListener(CiUIEventData data) {
			// TODO Auto-generated method stub
			Log.i("gky", getClass().toString() + "-->onCiUIEventListener data is \n"
					+data.titleString+"\n"
					+data.bottomString+"\n"
					+data.subTitleString+"\n"
					+data.dataList);
			if (data.ciCardStatus == TvConfigTypes.TVCI_UI_DATA_READY) {
				if (data.CIMMI_TYPE == TvConfigTypes.CIMMI_ENQ) {
					Log.d("CIMMI_TYPE","data.CIMMI_TYPE = " + data.CIMMI_TYPE);
				
					CIInfoPwdDialog ciInfoPwdDialog = CIInfoPwdDialog
							.getInstance();
					ciInfoPwdDialog.processCmd(null, DialogCmd.DIALOG_SHOW,
							TvPluginControler.getInstance().getCilManager().getEnqString());
				} else if (data.CIMMI_TYPE == TvConfigTypes.CIMMI_MENU
						|| data.CIMMI_TYPE == TvConfigTypes.CIMMI_LIST){
					if(!CIInformationDialog.this.isShowing())
						CIInformationDialog.this.showUI();
					ciInformationView.updateView(data);
				}
			} else if (data.ciCardStatus == TvConfigTypes.TVCI_UI_CARD_REMOVED) {
				TvUIControler.getInstance().showMniToast(
						TvContext.context.getResources().getString(
								R.string.str_ci_remove));
				processCmd(null, DialogCmd.DIALOG_DISMISS, null);
			}else if(data.ciCardStatus == TvConfigTypes.TVCI_UI_CLOSEMMI){
				Log.i("gky",getClass().getSimpleName() + "----------------TVCI_UI_CLOSEMMI dismiss----------");
				//processCmd(null, DialogCmd.DIALOG_DISMISS, null);
				//CI退出,则直接关闭,不再返回上一级
				TvPluginControler.getInstance().getCilManager().close();
				TvPluginControler.getInstance().getCilManager().releaseListener();
				TvPluginControler.getInstance().getCommonManager().enableHotkey(true);
				CIInformationDialog.this.dismissUI();
			}else if(data.ciCardStatus == TvConfigTypes.TVCI_UI_AUTOTEST_MESSAGE_SHOWN){
				TvUIControler.getInstance().showMniToast(TvContext.context.getResources().getString(R.string.str_ci_autotest));
			}
		}
	};
}

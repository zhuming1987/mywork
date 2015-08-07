package com.tv.ui.network;

import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.data.WifiSelectData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;
import com.tv.ui.utils.LogicTextResource;

public class TvWifiSeletctDialog extends TvBaseDialog{

	private TvWifiSelectView tvWifiSelectView;
	private DialogListener dialogListener;
	
	public TvWifiSeletctDialog() 
	{
		super(TvContext.context, R.style.dialog, TvConfigTypes.TV_DIALOG_WIFI_SELECT_LIST);
		// TODO Auto-generated constructor stub
		tvWifiSelectView = new TvWifiSelectView(TvContext.context);
		tvWifiSelectView.setWifiSelecteDialog(this);
		tvWifiSelectView.setParentDialog(this);
		setAutoDismiss(false);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(tvWifiSelectView);
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) 
	{
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return false;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) 
	{
		// TODO Auto-generated method stub
		switch (cmd) 
		{
		case DIALOG_SHOW:
			super.showUI();
			WifiSelectData selectData = new WifiSelectData();

            for (int i = 0; i < tvWifiSelectView.accessPoints.size(); i++)
            {
            	WifiSelectData data = new WifiSelectData();
                data.setWifiName(tvWifiSelectView.accessPoints.get(i).ssid);
                if(tvWifiSelectView.accessPoints.get(i).security == 0)
                {
                    data.setIsEncrypt(0);
                }
                else
                {
                	data.setIsEncrypt(1);
                }
                
                data.setlevel(tvWifiSelectView.accessPoints.get(i).getLevel());
                selectData.addData(data);
            }
            
            selectData.setTitleBarData("", LogicTextResource.getInstance(TvContext.context).getText(R.string.wifiSetup));  
			tvWifiSelectView.updateWifiSlectView(selectData);
			break;
		case DIALOG_UPDATE:
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
			{
				dialogListener.onDismissDialogDone(objects);
			}
			break;
		}
		return false;
	}

}

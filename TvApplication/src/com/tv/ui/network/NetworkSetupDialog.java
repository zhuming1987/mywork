package com.tv.ui.network;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.framework.data.TvEnumSetData;
import com.tv.framework.data.TvSetData;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvConfigDefs.TV_CFG_NETWORK_SETUP_ETHERNET_GET_CONNECT_STATE_ENUM_TYPE;
import com.tv.framework.define.TvConfigDefs.TV_CFG_NETWORK_SETUP_ETHERNET_GET_DEVICE_STATE_ENUM_TYPE;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ChannelSearch.DTVManualSearchView;
import com.tv.ui.base.TvBaseDialog;
import com.tv.ui.base.TvBaseDialog.DialogListener;

public class NetworkSetupDialog extends TvBaseDialog{

    private NetworkSetupView networkSetupView;
    private DialogListener dialogListener;
    
    public NetworkSetupDialog()
    {
    	super(TvContext.context, R.style.dialog,TvConfigTypes.TV_SETUP_NETWORK);
		// TODO Auto-generated constructor stub
		
    	networkSetupView = new NetworkSetupView(TvContext.context);
    	networkSetupView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(networkSetupView);
		setAutoDismiss(false);	
    }
    
	public NetworkSetupDialog(Context context, int theme, String dialogType) 
	{
		super(context, theme, dialogType);
		// TODO Auto-generated constructor stub
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
//		if(!key.equals(TvConfigTypes.TV_SETUP_NETWORK)) 
//			return false;
		switch (cmd) 
		{
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}

}

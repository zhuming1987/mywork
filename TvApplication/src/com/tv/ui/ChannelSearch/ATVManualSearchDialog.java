package com.tv.ui.ChannelSearch;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.data.TvType;
import com.tv.framework.data.ChannelSearchData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.plugin.tvfuncs.ChannelManager.AtvAutoSearchListener;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog;

public class ATVManualSearchDialog extends TvBaseDialog{
	
	private boolean isChannelChanged=false;
	private ATVManualSearchView ATVManualSearchView;
	private DialogListener dialogListener;
	private QuickKeyListener quickKeyListener;
	public ATVManualSearchDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_ATV_MANUAL_SEARCH);
		// TODO Auto-generated constructor stub
		this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
		ATVManualSearchView = new ATVManualSearchView(TvContext.context);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(ATVManualSearchView);
		setAutoDismiss(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if((event.getFlags()&KeyEvent.FLAG_LONG_PRESS)!=0)
		{
			Log.v("wxj","longpress");
			if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
			{
				isChannelChanged=true;
				if(TvPluginControler.getInstance().getChannelManager().getAtvManualSearchRightStatus())
				{
					TvPluginControler.getInstance().getChannelManager().stopAtvManualSearch();
				}
				TvPluginControler.getInstance().getChannelManager().startAtvManualSearch(atvManualSearchListener,true,false);
				return true;
			}
			if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
			{
				isChannelChanged=true;
				if(TvPluginControler.getInstance().getChannelManager().getAtvManualSearchLeftStatus())
				{
					TvPluginControler.getInstance().getChannelManager().stopAtvManualSearch();
				}
				TvPluginControler.getInstance().getChannelManager().startAtvManualSearch(atvManualSearchListener,false,false);
				return true;
			}
		}
		else
		{
			Log.v("wxj","no longpress");
			if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
			{
				isChannelChanged=true;
				if(TvPluginControler.getInstance().getChannelManager().getAtvManualSearchRightStatus())
				{
					TvPluginControler.getInstance().getChannelManager().stopAtvManualSearch();
				}
				TvPluginControler.getInstance().getChannelManager().startAtvManualSearch(atvManualSearchListener,true,true);
				ATVManualSearchView.updatePrograms();
				return true;
			}
			if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
			{
				isChannelChanged=true;
				if(TvPluginControler.getInstance().getChannelManager().getAtvManualSearchLeftStatus())
				{
					TvPluginControler.getInstance().getChannelManager().stopAtvManualSearch();
				}
				TvPluginControler.getInstance().getChannelManager().startAtvManualSearch(atvManualSearchListener,false,true);
				ATVManualSearchView.updatePrograms();
				return true;
			}			
		}		
		
		if(keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/)
		{
			processCmd(TvConfigTypes.TV_DIALOG_ATV_MANUAL_SEARCH,DialogCmd.DIALOG_DISMISS, null);
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_CHANNEL_UP  
				|| keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN
				|| keyCode == KeyEvent.KEYCODE_DPAD_UP
				|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN
				)
		{
			return true;
		}
		
		return true;
	}
	
	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		if(!key.equals(TvConfigTypes.TV_DIALOG_ATV_MANUAL_SEARCH))
		{
			return false;
		}
		
		switch (cmd) {
		case DIALOG_SHOW:
			isChannelChanged=false;
		case DIALOG_UPDATE:
			super.showUI();
			break;
		case DIALOG_HIDE:
			
			break;
		case DIALOG_DISMISS:
			if(isChannelChanged)
			{
				TvPluginControler.getInstance().getChannelManager().stopAtvManualSearch();
			}
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return true;
	}

	AtvAutoSearchListener atvManualSearchListener = new AtvAutoSearchListener() {
		
		@Override
		public void OnAtvAutoSearchListener(ChannelSearchData data) {
			// TODO Auto-generated method stub
			if(Integer.parseInt(data.percent) >= 99)
			{
				processCmd(TvConfigTypes.TV_DIALOG_ATV_MANUAL_SEARCH,DialogCmd.DIALOG_DISMISS, null);
			}
			else
			{
				ATVManualSearchView.updateView(data);
			}
		}
	};
}

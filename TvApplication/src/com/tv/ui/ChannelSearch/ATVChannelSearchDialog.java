package com.tv.ui.ChannelSearch;

import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.data.TvType;
import com.tv.framework.data.ChannelSearchData;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvContext;
import com.tv.framework.plugin.tvfuncs.ChannelManager.AtvAutoSearchListener;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.utils.LogicTextResource;

public class ATVChannelSearchDialog extends TvBaseDialog
{
	private ATVChannelSearchView ATVChannelSearchView;
	private DialogListener dialogListener;
	private QuickKeyListener quickKeyListener;
	
	private String TAG = "ATVChannelSearchDialog";
	
	public ATVChannelSearchDialog() 
	{
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH);
		// TODO Auto-generated constructor stub
		this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
		ATVChannelSearchView = new ATVChannelSearchView(TvContext.context);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(ATVChannelSearchView);
		setAutoDismiss(false);
	}

	public void changeSource()
	{
		Log.e(TAG, "enter changeSource()");
		
		boolean bDVBSStatus = TvPluginControler.getInstance().getChannelManager().getSearchDVBSStatus();
		boolean bDVBTStatus = TvPluginControler.getInstance().getChannelManager().getSearchDVBTStatus();
		if (bDVBSStatus || bDVBTStatus)
		{
			Log.d(TAG, "changeSource() getSearchDVBSStatus = " + bDVBSStatus + "....getSearchDVBTStatus = " + bDVBTStatus);
			
			if(bDVBSStatus)
			{
				try
				{
					Log.d(TAG, "changeSource() call switchSource() E_INPUT_SOURCE_DTV2");
					TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV2.ordinal());
				}
				catch (RemoteException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					Log.d(TAG, "changeSource() call switchSource() E_INPUT_SOURCE_DTV");
					
					TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV.ordinal());
				} 
				catch (RemoteException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "onKeyDown() ----------keyCode is "+keyCode);
		if(keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/)
		{
			final TvToastFocusDialog dismissATVSearchHint = new TvToastFocusDialog();
			
			dismissATVSearchHint.setOnBtClickListener(new OnBtClickListener() 
			{				
				@Override
				public void onClickListener(boolean flag) 
				{
					// TODO Auto-generated method stub
					Log.d(TAG, "enter onClickListener()");
					if(flag == true)
					{
					//liujian20150312
						processCmd(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, null);
						
						Log.d(TAG, "onClickListener() call changeSource()");
						changeSource();					
					}
					else
					{
						Log.d(TAG, "onClickListener() call resumeAtvAutoSearch()");;
						TvPluginControler.getInstance().getChannelManager().resumeAtvAutoSearch();
					}
					
					((TvToastFocusDialog) dismissATVSearchHint).processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);
				}
			});
			
			dismissATVSearchHint.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW, 
					new TvToastFocusData("", "", LogicTextResource.getInstance(TvContext.context).getText(R.string.are_you_sure_stop_DTV_search),2));	
			
			boolean exitFlag = this.isShowing();
			
			return !exitFlag;
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
	public boolean processCmd(String key, DialogCmd cmd, Object obj) 
	{
		// TODO Auto-generated method stub
		if(!key.equals(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH))
		{
			return false;
		}
		
		switch (cmd) 
		{
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			
			Log.i(TAG, "processCmd() DIALOG_SHOW call startAtvAutoSearch()");
			
			TvPluginControler.getInstance().getChannelManager().startAtvAutoSearch(atvAutoSearchListener);
			
			break;
		case DIALOG_HIDE:
			
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			
			Log.d(TAG, "onClickListener() call stopAtvAutoSearch()");
			TvPluginControler.getInstance().getChannelManager().stopAtvAutoSearch();
			Object[] objects = null;
			if(null != dialogListener)
			{
				dialogListener.onDismissDialogDone(objects);
			}
			
			break;
		}
		
		return false;
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener)
	{
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return true;
	}

	AtvAutoSearchListener atvAutoSearchListener = new AtvAutoSearchListener()
	{
		@Override
		public void OnAtvAutoSearchListener(ChannelSearchData data)
		{
			// TODO Auto-generated method stub
			//Log.i(TAG, "AtvAutoSearchListener::OnAtvAutoSearchListener:atvCount is " + data.atvCount
	    	//		+" Frequency is " + data.Frequency + " band is " + data.band + " percent is " + data.percent);
			
			if(Integer.parseInt(data.percent) >= 99)
			{				
				processCmd(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, null);
				
				changeSource();				
			}
			else
			{
				ATVChannelSearchView.updateView(data);
			}
		}
	};

}

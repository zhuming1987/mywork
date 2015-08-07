package com.tv.ui.ChannelSearch;

import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.framework.data.ChannelSearchData;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvSearchTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.plugin.tvfuncs.ChannelManager.DtvAutoSearchListener;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog;
import com.tv.ui.utils.LogicTextResource;

public class DTVChannelSearchDialog extends TvBaseDialog
{
	private DTVChannelSearchView DTVChannelSearchView;
	private DialogListener dialogListener;
	private QuickKeyListener quickKeyListener;
	
	
	private String TAG = "DTVChannelSearchDialog";
	
	public DTVChannelSearchDialog() 
	{
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH);
		// TODO Auto-generated constructor stub
		this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
		DTVChannelSearchView = new DTVChannelSearchView(TvContext.context);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(DTVChannelSearchView);
		setAutoDismiss(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// TODO Auto-generated method stub
		
		Log.i(TAG, "onKeyDown() ---------- keyCode is "+keyCode);
		
		if(keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/)
		{
			final TvToastFocusDialog dismissATVSearchHint = new TvToastFocusDialog();
			Log.i(TAG,"onKeyDown() call pauseDtvAutoSearch()");
			
			TvPluginControler.getInstance().getChannelManager().pauseDtvAutoSearch();
			
			dismissATVSearchHint.setOnBtClickListener(new OnBtClickListener()
			{				
				@Override
				public void onClickListener(boolean flag) 
				{
					// TODO Auto-generated method stub
					
					Log.d(TAG, "onKeyDown() DTV OnBtClickListener()");
					
					if(flag == true)
					{
						processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, null);
					}
					else
					{
						Log.d(TAG, "onKeyDown() DTV OnBtClickListener() call resumeDtvAutoSearch()");
						
						TvPluginControler.getInstance().getChannelManager().resumeDtvAutoSearch();
					}
					((TvToastFocusDialog) dismissATVSearchHint).processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);
				}
			});
			
			dismissATVSearchHint.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW, 
					new TvToastFocusData("", "",LogicTextResource.getInstance(TvContext.context).getText(R.string.are_you_sure_stop_DTV_search),2));
	
			boolean exitFlag = this.isShowing();			
			return !exitFlag;
		}
		
		return true;
	}
	
	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) 
	{
		// TODO Auto-generated method stub
		if(!key.equals(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH))
		{
			return false;
		}
		
		switch (cmd)
		{
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			
			Log.i(TAG, "processCmd() DIALOG_SHOW call startDtvAutoSearch()");
			TvPluginControler.getInstance().getChannelManager().startDtvAutoSearch(dtvAutoSearchListener);
			
			break;
			
		case DIALOG_HIDE:
			break;
			
		case DIALOG_DISMISS:
			super.dismissUI();
			Log.v(TAG,"processCmd() DIALOG_DISMISS DVBT call stopDtvAutoSearch()");
			TvPluginControler.getInstance().getChannelManager().stopDtvAutoSearch();
			Log.v(TAG, "obj = " + String.valueOf(obj));
			if(String.valueOf(obj).equals("DVBTAutoSearch") && TvPluginControler.getInstance().getChannelManager().getSearchDVBTStatus())
			{				
				try 
				{
					Log.v(TAG,"processCmd() DIALOG_DISMISS call switchSource() E_INPUT_SOURCE_DTV -->>DVB_T");
					
					TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV.ordinal());
				} 
				catch (RemoteException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
				{
					Thread.sleep(2000);
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Log.v(TAG,"processCmd() DIALOG_DISMISS call switchSource() show DTVChannelSearchDialog -->>DVB_T");
				
				DTVChannelSearchDialog dtvChannelSearchDialog = new DTVChannelSearchDialog();
				dtvChannelSearchDialog.setDialogListener(null);
				dtvChannelSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH, DialogCmd.DIALOG_SHOW, "DVBT");
			}
			else if(String.valueOf(obj).equals("DVBCAutoSearch") && TvPluginControler.getInstance().getChannelManager().getSearchDVBCStatus())
			{				
				try 
				{
					Log.v(TAG,"processCmd() DIALOG_DISMISS call switchSource() E_INPUT_SOURCE_DTV -->>DVB_C");
					
					TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_KTV.ordinal());
				} 
				catch (RemoteException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
				{
					Thread.sleep(2000);
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Log.v(TAG,"processCmd() DIALOG_DISMISS call switchSource() show DTVChannelSearchDialog -->>DVB_C");
				
				DTVChannelSearchDialog dtvChannelSearchDialog = new DTVChannelSearchDialog();
				dtvChannelSearchDialog.setDialogListener(null);
				dtvChannelSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH, DialogCmd.DIALOG_SHOW, "DVBC");
			}
			else if(String.valueOf(obj).equals("ATVAutoSearch") && TvPluginControler.getInstance().getChannelManager().getSearchATVStatus())
			{
				
				Log.v(TAG,"processCmd() DIALOG_DISMISS ATV call switchSource() E_INPUT_SOURCE_ATV");
				try
				{
					Log.v("wangxian", "---------start ATV search delay 2S-----------------");
					Thread.sleep(2000);
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try 
				{
					TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_ATV.ordinal());
				}
				catch (RemoteException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
				{
					Thread.sleep(2000);
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.v(TAG,"processCmd() DIALOG_DISMISS ATV call show ATVChannelSearchDialog");
				
				ATVChannelSearchDialog atvChannelSearchDialog = new ATVChannelSearchDialog();
				atvChannelSearchDialog.setDialogListener(null);
				atvChannelSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH, DialogCmd.DIALOG_SHOW, "ATV");
			}
			else
			{
				Object[] objects = null;
				if(null != dialogListener)
				{
					dialogListener.onDismissDialogDone(objects);
				}
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

	DtvAutoSearchListener dtvAutoSearchListener = new DtvAutoSearchListener() 
	{
		@Override
		public void OnDtvAutoSearchListener(ChannelSearchData data) 
		{
			// TODO Auto-generated method stub
			
			Log.e("zhm", "enter OnDtvAutoSearchListener() data.percent: " + Integer.parseInt(data.percent));
			if((Integer.parseInt(data.percent) >= 100 || (Integer.parseInt(data.scanstatus) == 8)
					&& TvSearchTypes.isautosearch))
			{
				Log.v("zhm","OnDtvAutoSearchListener() dtvAutoSearchListener");
				
				if(TvPluginControler.getInstance().getChannelManager().getSearchDVBSStatus()
		    			&& TvSearchTypes.isDVBSAutoSearch)
		    	{
					TvSearchTypes.isDVBSAutoSearch = false;
					if(TvPluginControler.getInstance().getChannelManager().getSearchDVBTStatus())
					{
						Log.v("zhm","isDVBSAutoSearch DIALOG_DISMISS obj: DVBT");
			    		processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, "DVBTAutoSearch");
					}
					else if(TvPluginControler.getInstance().getChannelManager().getSearchDVBCStatus())
					{
						Log.v("zhm","isDVBSAutoSearch DIALOG_DISMISS obj: DVBC");
			    		processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, "DVBCAutoSearch");
					}
					else if(TvPluginControler.getInstance().getChannelManager().getSearchATVStatus())
					{
						Log.v("zhm","isDVBSAutoSearch DIALOG_DISMISS obj: ATV");
						processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, "ATVAutoSearch");
					}
					else
					{
						Log.v("zhm","--DVBS-->>DIALOG_DISMISS obj: null");
						processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, null);
					}
		    	}
				else if(TvPluginControler.getInstance().getChannelManager().getSearchDVBTStatus()
		    			&& TvSearchTypes.isDVBTAutoSearch)
		    	{
					TvSearchTypes.isDVBTAutoSearch = false;
					if(TvPluginControler.getInstance().getChannelManager().getSearchDVBCStatus())
					{
						Log.v("zhm","isDVBTAutoSearch DIALOG_DISMISS obj: DVBT");
						processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, "DVBCAutoSearch");
					}
					else if(TvPluginControler.getInstance().getChannelManager().getSearchATVStatus())
					{
						Log.v("zhm","isDVBTAutoSearch DIALOG_DISMISS obj: ATV");
						processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, "ATVAutoSearch");
					}
					else
					{
						Log.v("zhm","--DVBT-->>DIALOG_DISMISS obj: null");
						processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, null);
					}
		    	}
				else if(TvPluginControler.getInstance().getChannelManager().getSearchDVBCStatus()
		    			&& TvSearchTypes.isDVBCAutoSearch)
		    	{
					TvSearchTypes.isDVBCAutoSearch = false;
					if(TvPluginControler.getInstance().getChannelManager().getSearchATVStatus())
					{
						Log.v("zhm","isDVBCAutoSearch DIALOG_DISMISS obj: ATV");
						processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, "ATVAutoSearch");
					}
					else
					{
						Log.v("zhm","--DVBC-->>DIALOG_DISMISS obj: null");
						processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, null);
					}
		    	}
				else
				{
					Log.v("zhm","OnDtvAutoSearchListener() DIALOG_DISMISS obj: null");
					processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH,DialogCmd.DIALOG_DISMISS, null);
				}
			}
			else
			{
				DTVChannelSearchView.updateView(data);
			}
		}
	};
}

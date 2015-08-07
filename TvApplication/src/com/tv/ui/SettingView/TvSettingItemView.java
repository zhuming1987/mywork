package com.tv.ui.SettingView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.data.DataControler.MenuItemChangeListener;
import com.tv.data.MenuItem;
import com.tv.data.TvType;
import com.tv.framework.data.DiskSelectInfoData;
import com.tv.framework.data.TvEnumSetData;
import com.tv.framework.data.TvLoadingData;
import com.tv.framework.data.TvRangeSetData;
import com.tv.framework.data.TvSetData;
import com.tv.framework.data.TvSwitchSetData;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.EnumChOpType;
import com.tv.framework.define.TvConfigTypes.TvConfigType;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvSearchTypes;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.AudioLanguage.AudioLanguageListViewDialog;
import com.tv.ui.CECDeviceList.CECDeviceListDialog;
import com.tv.ui.ChannelEdit.ChannelEditDialog;
import com.tv.ui.ChannelSearch.ATVChannelSearchDialog;
import com.tv.ui.ChannelSearch.ATVManualSearchDialog;
import com.tv.ui.ChannelSearch.DTVChannelSearchDialog;
import com.tv.ui.ChannelSearch.DTVManualSearchDialog;
import com.tv.ui.ChannelSearch.DVBSManualSearchDialog;
import com.tv.ui.CiInformation.CIInformationDialog;
import com.tv.ui.CountDown.TvCountDownDialog;
import com.tv.ui.LNBSetting.LNBSettingDialog;
import com.tv.ui.LanguageSettingView.LanguageSelectDialog;
import com.tv.ui.ListView.TvListViewDialog;
import com.tv.ui.Loading.TvLoadingDialog;
import com.tv.ui.ParentrolControl.AgeListDialog;
import com.tv.ui.ParentrolControl.PasswordChangeDialog;
import com.tv.ui.ParentrolControl.PasswordMainMenuDialog;
import com.tv.ui.ParentrolControl.PasswordSubMenuDialog;
import com.tv.ui.Pvrsetting.DiskSelectDialog;
import com.tv.ui.SignalInfo.SignalInfoDialog;
import com.tv.ui.Teletext.TvTeletextDialog;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.base.TvMenuConfigDefs;
import com.tv.ui.utils.LogicTextResource;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class TvSettingItemView extends LinearLayout implements OnFocusChangeListener, OnKeyListener{

	private int index;
	private TvSettingView parentView;
	private OnSettingKeyDownListener keyListener;
	private MenuItemChangeListener menuItemChangeListener = null;
	
	public static float resolution = TvUIControler.getInstance().getResolutionDiv();
	public static float dipDiv = TvUIControler.getInstance().getDipDiv();
	public static int FONT_SIZE_TITLE = 36;
	
	public MenuItem data;
	public boolean isEnabled = true;
	public boolean ishide = false;
	
	public LinearLayout bodyLayout;
	public LinearLayout viewLayout;
	public TextView titleView;

	private int curValue = -1;
	
	private static final int E_PVR_USB_SELECT = 1001;
	private static final int E_PVR_USB_FORMAT = 1002;
	private static final int E_PVR_USB_SPEED = 1003;
	private static final int PC_AUTO_ADJUST_START = 1;
	private static final int PC_AUTO_ADJUST_SUCCEED = 2;
	private static final int PC_AUTO_ADJUST_FAIL = 3;
	private static final int PC_AUTO_ADJUST_UIDISMISS = 0;
	
	private String selectedDiskPath;
	boolean retFormat;
	TvLoadingDialog tvLoadingDialog;
	
	private boolean autotune_flag = false;
	
	private String TAG = "TvSettingItemView";
	
	public TvSettingItemView(Context context, int index, TvSettingView parentView, OnSettingKeyDownListener keyListener) {
		super(context);
		// TODO Auto-generated constructor stub
		Log.i("gky","TvSettingItemView init");
		this.index = index;
		this.parentView = parentView;
		this.keyListener = keyListener;
		this.menuItemChangeListener = TvType.getInstance().getDataControler().getMenuItemChangeListener();
		
		this.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams((int)(TvMenuConfigDefs.setting_item_w/resolution), (int)(TvMenuConfigDefs.setting_item_h/resolution));
        itemLayoutParams.setMargins(0, (int) (20 / resolution), 0, (int) (30 / resolution));
        this.setLayoutParams(itemLayoutParams);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setOnKeyListener(this);
        this.setOnFocusChangeListener(this);
        
        titleView = new TextView(context);
        titleView.setTextSize(FONT_SIZE_TITLE/dipDiv);
        titleView.getPaint().setAntiAlias(true);
        titleView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        titleView.setPadding(0, 0, (int) (TvMenuConfigDefs.setting_item_title_padding/resolution), 0);
        titleView.setSingleLine(true);
        titleView.setEllipsize(TruncateAt.MARQUEE);
        titleView.setMarqueeRepeatLimit(-1);
        this.addView(titleView, new LayoutParams((int)((TvMenuConfigDefs.setting_item_title_w/resolution) 
        		+ (TvMenuConfigDefs.setting_item_title_padding/resolution)),
                LayoutParams.MATCH_PARENT));
        
        bodyLayout = new LinearLayout(context);
        bodyLayout.setGravity(Gravity.CENTER);
        bodyLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int)(TvMenuConfigDefs.setting_item_body_w/resolution), 
        		(int)(TvMenuConfigDefs.setting_item_h/resolution));
        this.addView(bodyLayout, layoutParams);
        viewLayout = new LinearLayout(context);
        viewLayout.setGravity(Gravity.CENTER);
        bodyLayout.addView(viewLayout, layoutParams);
        
        tvLoadingDialog = new TvLoadingDialog();
	}

	public void updateView(MenuItem data){
		
		this.data = data;
		this.isEnabled = data.isVisable();
		Log.i("gky", "TvSettingItemView::updateView: id is " + data.getId() + " isEnable is " + this.isEnabled);
		titleView.setText(LogicTextResource.getInstance(TvContext.context).getText(data.getId()));
		if (bodyLayout.hasFocus())
        {
            titleView.setTextColor(0xfff0c40d);
        } else
        {
            if (isEnabled)
            	titleView.setTextColor(Color.WHITE);
            else
            {
            	titleView.setTextColor(Color.GRAY);
            }
        }
	}
	
	public String getDataId(){
		return this.data.getId();
	}
	
	public boolean getIsEnable(){
		return this.isEnabled;
	}
	
	/**
	 *  调用Plugin层，响应设置
	 * @param Value
	 */
	public void OnExecuteSet(final int Value){
		Log.i("gky", getClass().getName() +"-->OnExecuteSet:value is " + Value + " name is " + data.getId());
		long start = System.currentTimeMillis();
		this.curValue = Value;
		if (Value == TvNextItemView.ENTER) // for otherView is null
			showOtherView();
		else {
			//if(mHandler.hasCallbacks(setConfigRunnable)){
			//	mHandler.removeCallbacks(setConfigRunnable);
			//}
			//mHandler.post(setConfigRunnable);
			setConfig(Value);
		}
		long end = System.currentTimeMillis();
		Log.i("gky", getClass().getName() + "--OnExecuteSet end time "+(end-start)+"ms");
	}
	
	@SuppressWarnings("unused")
	private Runnable setConfigRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(Looper.myLooper() == null){
				Looper.prepare();
				setConfig(curValue);
				Looper.loop();
			}else {
				setConfig(curValue);
			}
		}
	};
	
	private void setConfig(int Value) {
		long start = System.currentTimeMillis();
		final TvSetData tempData = data.getItemData();
		switch (data.getItemType()) {
		case TV_CONFIG_ENUM:
		{
			long start1 = System.currentTimeMillis();
			((TvEnumSetData) tempData).setCurrentIndex(Value);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					TvPluginControler.getInstance().setConfigData(data.getId(),tempData);
					menuItemChangeListener.OnMenuItemChangeListener(data.getId(),
							parentView.getSettingViewChangeListener());
				}
			}).start();
			((TvEnumSetData)data.getItemData()).setCurrentIndex(Value);
			long end1 = System.currentTimeMillis();
			Log.i("gky", getClass().getName() + "--refresh UI work time is "+(end1-start1)+"ms");
			break;
		}
		case TV_CONFIG_RANGE:
		{
			long start1 = System.currentTimeMillis();
			((TvRangeSetData) tempData).setCurrent(Value);
			TvPluginControler.getInstance().setConfigData(data.getId(),
					tempData);
			long end = System.currentTimeMillis();
			Log.i("gky", getClass().getName() + "--setConfig work time is  "+(end-start1)+"ms");
			menuItemChangeListener.OnMenuItemChangeListener(data.getId(),
					parentView.getSettingViewChangeListener());
			long end1 = System.currentTimeMillis();
			Log.i("gky", getClass().getName() + "--refresh UI work time is "+(end1-end)+"ms");
			((TvRangeSetData)data.getItemData()).setCurrent(Value);
		}
			break;
		case TV_CONFIG_SWITCH:
			((TvSwitchSetData) tempData).setOn(Value != 0 ? false : true);
			TvPluginControler.getInstance().setConfigData(data.getId(),
					tempData);
			menuItemChangeListener.OnMenuItemChangeListener(data.getId(),
					parentView.getSettingViewChangeListener());
			((TvSwitchSetData)data.getItemData()).setOn(Value != 0 ? false : true);
			break;
		default:
			break;
		}
		long end = System.currentTimeMillis();
		Log.i("gky", getClass().getName() + "--setConfig end time "+(end-start)+"ms");
	}
	
	public void showOtherView(){
		
		parentView.setCurrent_index(index);
		parentView.getParentDialog().dismissUI();
		if(data.getChlidList() != null && data.getChlidList().size() != 0)
		{
			TvSettingDialog settingDialog = new TvSettingDialog();
			settingDialog.setDialogListener(parentView);
			settingDialog.processCmd(TvConfigTypes.TV_DIALOG_SETTING, DialogCmd.DIALOG_SHOW, data);
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_VGA_AUTO_ADJUST))
		{
			Log.i("gky", getClass().getSimpleName() + "----->showOtherView TV_CFG_VGA_AUTO_ADJUST");
			new Thread(pcAutoJustRunnable,"pcAutoJust").start();
			Log.i("gky", getClass().getSimpleName() + "----->showOtherView #######################");
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_START_AUTO_SEARCH))
		{
			if(TvPluginControler.getInstance().getCilManager().isOpMode())
			{
				TvUIControler.getInstance().showMniToast("Forbid to do channel tune under OP mode.\nPlease exit OP mode at first.");
				
				return;
			}
			
			Log.i(TAG, "showOtherView() enter TvConfigDefs.TV_CFG_START_AUTO_SEARCH");
			
			TvSearchTypes.isautosearch = true;
			TvSearchTypes.isDVBSAutoSearch = TvPluginControler.getInstance().getChannelManager().getSearchDVBSStatus();
			TvSearchTypes.isDVBCAutoSearch = TvPluginControler.getInstance().getChannelManager().getSearchDVBCStatus();
			TvSearchTypes.isDVBTAutoSearch = TvPluginControler.getInstance().getChannelManager().getSearchDVBTStatus();
			TvSearchTypes.isATVAutoSearch = TvPluginControler.getInstance().getChannelManager().getSearchATVStatus();
			TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
			int curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
			if(TvPluginControler.getInstance().getChannelManager().getSearchDVBSStatus())
			{
				if(!((curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV) 
						&& (curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS
						|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS2)))
				{
					try
					{
						TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV2.ordinal());
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
				}
				
				//设置卫星参数
				Log.i(TAG, "showOtherView() enter TvConfigDefs.TV_CFG_START_AUTO_SEARCH DVBS call new LNBSettingDialog()");
				LNBSettingDialog lnbSettingDialog = new LNBSettingDialog();
				lnbSettingDialog.setDialogListener(parentView);
				lnbSettingDialog.processCmd(TvConfigTypes.TV_DIALOG_LNB_SETTING, DialogCmd.DIALOG_SHOW, null);
			}
			else if(TvPluginControler.getInstance().getChannelManager().getSearchDVBTStatus())
			{
				if(!((curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV) 
						&& (curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT 
						|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT2)))
				{
					try 
					{
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
				}
				
				Log.i(TAG, "showOtherView() enter TvConfigDefs.TV_CFG_START_AUTO_SEARCH DVBT call new DTVChannelSearchDialog()");
				
				DTVChannelSearchDialog dtvChannelSearchDialog = new DTVChannelSearchDialog();
				dtvChannelSearchDialog.setDialogListener(null);
				dtvChannelSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH, DialogCmd.DIALOG_SHOW, "DVBT");
			}
			else if(TvPluginControler.getInstance().getChannelManager().getSearchDVBCStatus())
			{
				if(!((curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV) 
						&& curDtvRoute == TvConfigTypes.TV_ROUTE_DVBC))
				{
					try 
					{
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
				}
				
				Log.i(TAG, "showOtherView() enter TvConfigDefs.TV_CFG_START_AUTO_SEARCH DVBC call new DTVChannelSearchDialog()");
				
				DTVChannelSearchDialog dtvChannelSearchDialog = new DTVChannelSearchDialog();
				dtvChannelSearchDialog.setDialogListener(null);
				dtvChannelSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH, DialogCmd.DIALOG_SHOW, "DVBC");
			}
			else if(TvPluginControler.getInstance().getChannelManager().getSearchATVStatus())
			{			
				if(!(curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV))
				{
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
				}
				
				Log.i(TAG, "showOtherView() enter TvConfigDefs.TV_CFG_START_AUTO_SEARCH ATV call new ATVChannelSearchDialog()");
				
				ATVChannelSearchDialog atvChannelSearchDialog = new ATVChannelSearchDialog();
				atvChannelSearchDialog.setDialogListener(null);
				atvChannelSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH, DialogCmd.DIALOG_SHOW, "ATV");
			}
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_MANUAL_SEARCH))
		{
			if(TvPluginControler.getInstance().getCommonManager().getCurrentInputSource() == TvEnumInputSource.E_INPUT_SOURCE_DTV)
			{
				int curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
				if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS || curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS2)
				{
					DVBSManualSearchDialog dvbsManualSearchDialog = new DVBSManualSearchDialog();
					dvbsManualSearchDialog.setDialogListener(parentView);
					dvbsManualSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DVBS_MANUAL_SEARCH, DialogCmd.DIALOG_SHOW, null);
				}else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT || curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT2)
				{
					DTVManualSearchDialog dtvManualSearchDialog = new DTVManualSearchDialog();
					dtvManualSearchDialog.setDialogListener(parentView);
					dtvManualSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DTV_MANUAL_SEARCH, DialogCmd.DIALOG_SHOW, null);
				}else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBC)
				{
					DTVManualSearchDialog dtvManualSearchDialog = new DTVManualSearchDialog();
					dtvManualSearchDialog.setDialogListener(parentView);
					dtvManualSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DTV_MANUAL_SEARCH, DialogCmd.DIALOG_SHOW, null);
				}
			}
			else 
			{
				ATVManualSearchDialog atvManualSearchDialog = new ATVManualSearchDialog();
				atvManualSearchDialog.setDialogListener(parentView);
				atvManualSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_ATV_MANUAL_SEARCH, DialogCmd.DIALOG_SHOW, null);
			}
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_CHANNEL_EDIT))
		{
			TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
			if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV
					|| curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV){
				ChannelEditDialog ChannelEditDialog = new ChannelEditDialog();
				ChannelEditDialog.setDialogListener(parentView);
				ChannelEditDialog.processCmd(null, DialogCmd.DIALOG_SHOW, EnumChOpType.E_CH_OPTION_EDIT);
			}
		}		
		else if(data.getId().equals(TvConfigDefs.TV_CFG_LANGUAGE))
		{
			LanguageSelectDialog languageSelectDialog = new LanguageSelectDialog();
			languageSelectDialog.setDialogListener(parentView);
			languageSelectDialog.processCmd(TvConfigTypes.TV_DIALOG_LANGUAGE_SETTING, DialogCmd.DIALOG_SHOW, null);
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_SOUND_SEPERATE))
		{
			TvCountDownDialog countDownDialog = new TvCountDownDialog();
			countDownDialog.setDialogListener(null);
			countDownDialog.processCmd(TvConfigTypes.TV_DIALOG_COUNT_DOWN, DialogCmd.DIALOG_SHOW, null);
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_RECOVERY))
		{
			if(TvPluginControler.getInstance().getParentalControlManager().isSystemLock()
			 &&!PasswordSubMenuDialog.getInstance().getPasswordFlagSubMenu())
			{
				PasswordSubMenuDialog pass= PasswordSubMenuDialog.getInstance();
				pass.setDialogListener(parentView);
				pass.processCmd(TvConfigTypes.TV_DIALOG_INPUT_PASSWORD, DialogCmd.DIALOG_SHOW,this);
				
			}
			else
			{
				TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
				tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() {	
				@Override
				public void onClickListener(boolean flag) {
					// TODO Auto-generated method stub
					Log.i("gky", "onBtClick is " + flag);
					if(flag)
					{
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								TvPluginControler.getInstance().getCommonManager().userReset();
							}
						}).start();
					}
				}});
				tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
					new TvToastFocusData("", "",TvContext.context.getResources().getString(R.string.str_tip_reset_tv),2));
			}
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_HDMID_CEC_DEVICE_LIST_SETTING))
		{
			CECDeviceListDialog cecDeviceListDialog = new CECDeviceListDialog();
			cecDeviceListDialog.setDialogListener(parentView);
			cecDeviceListDialog.processCmd(TvConfigTypes.TV_DIALOG_CEC_DEVICE_LIST, DialogCmd.DIALOG_SHOW, null);
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_TELETEXT_LANGUAGE))
		{
			TvTeletextDialog teletextDialog = new TvTeletextDialog();
			teletextDialog.setDialogListener(null);
			teletextDialog.processCmd(TvConfigTypes.TV_DIALOG_TELETEXT, DialogCmd.DIALOG_SHOW, null);
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_TIME_ZONE_SETTING))
		{
			MenuItem data = new MenuItem(TvConfigDefs.TV_CFG_TIME_ZONE, "system_setting", true, null);
			TvListViewDialog listViewDialog = new TvListViewDialog();
			listViewDialog.setDialogListener(null);
			listViewDialog.processCmd(TvConfigTypes.TV_DIALOG_LISTVIEW, DialogCmd.DIALOG_SHOW, data);
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_START_OFF_TIME))
		{
			long curofftime = 0;
			long cursystime =  System.currentTimeMillis();
			String offtimestringString = TvConfigDefs.TV_OFFTIME_YEAR + "-" + TvConfigDefs.TV_OFFTIME_MONTH + "-" 
					+ TvConfigDefs.TV_OFFTIME_DAY + " " + TvConfigDefs.TV_OFFTIME_HOUR + ":" + TvConfigDefs.TV_OFFTIME_MINUTE 
					+ ":00";
			SimpleDateFormat Gmt = new SimpleDateFormat("YY-MM-dd HH:mm:ss",Locale.ENGLISH);
			try {
				curofftime = Gmt.parse(offtimestringString).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long offtimelong = curofftime - cursystime;
			if(offtimelong > 0)
			{
				Intent intent = new Intent(TvConfigTypes.ACTION_START_OFF_TIMER);
				intent.putExtra("offtimelong", offtimelong);
				TvContext.context.sendBroadcast(intent);
			}
			else {
				TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
				tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() {
					
					@Override
					public void onClickListener(boolean flag) {
						// TODO Auto-generated method stub
						Log.i("gky", "onBtClick is " + flag);
					}
				});
				tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
						new TvToastFocusData("", "", TvContext.context.getResources().getString(R.string.str_tip_off_timer),1));
			}
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_LOCK_INPUT_PASSWORD))
		{
			PasswordMainMenuDialog pass= PasswordMainMenuDialog.getInstance();
			pass.setDialogListener(parentView);
			pass.processCmd(TvConfigTypes.TV_DIALOG_INPUT_PASSWORD, DialogCmd.DIALOG_SHOW, null);
		}else if(data.getId().equals(TvConfigDefs.TV_CFG_LOCK_CHANGE_PASSWORD))
		{
			PasswordChangeDialog pass= PasswordChangeDialog.getInstance();
			pass.setDialogListener(parentView);
			pass.processCmd(TvConfigTypes.TV_DIALOG_CHANGE_PASSWORD, DialogCmd.DIALOG_SHOW, null);
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_LOCK_CHANNEL_LIST))
		{
			ChannelEditDialog channelEditDialog = new ChannelEditDialog();
			channelEditDialog.setDialogListener(parentView);
			channelEditDialog.processCmd(TvConfigTypes.TV_DIALOG_LANGUAGE_SETTING, DialogCmd.DIALOG_SHOW, EnumChOpType.E_CH_OPTION_LOCK);
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_LOCK_AGE_LIST)){
			MenuItem data = new MenuItem(TvConfigDefs.TV_CFG_LOCK_SET_LOCK_PARENTAL_CONTROL_RATING_NUMBER, "system_setting", true, null);
			AgeListDialog listViewDialog = new AgeListDialog();
			listViewDialog.setDialogListener(parentView);
			listViewDialog.processCmd(TvConfigTypes.TV_DIALOG_LISTVIEW, DialogCmd.DIALOG_SHOW, data);
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_PVR_SELECT_USB))
		{
			DiskSelectDialog diskSelectDialog = new DiskSelectDialog();
			diskSelectDialog.setDialogListener(new DialogListener() {
				
				@Override
				public boolean onShowDialogDone(int ID) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public int onDismissDialogDone(Object... obj) {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = E_PVR_USB_SELECT;
					Log.i("gky", "parentView.deserialize(obj) is " + parentView.deserialize(obj));
					msg.obj = parentView.deserialize(obj);
					mHandler.sendMessage(msg);
					return 0;
				}
			});
			diskSelectDialog.processCmd(null, DialogCmd.DIALOG_SHOW, null);
		}
		else if(data.getId().equals(TvConfigDefs.TV_CFG_PVR_USB_FORMAT)){
			DiskSelectInfoData diskSelectInfoData = TvPluginControler.getInstance().getPvrManager().updateDiskSelectInfo();
			if (diskSelectInfoData.usbDriverCount < 1 || TvPluginControler.getInstance().getPvrManager().getSelectDiskPath().equals("")) 
			{
				TvUIControler.getInstance().showMniToast(this.getResources().getString(R.string.str_tip_plug_usb));
			} 
			else 
			{
				TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
				tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() {

							@Override
							public void onClickListener(boolean flag) {
								// TODO Auto-generated method stub
								Log.i("gky", "onBtClick is " + flag);
								if (flag) {
									formatUSB();
								} else {
									Message msg = new Message();
									msg.what = E_PVR_USB_FORMAT;
									msg.obj = null;
									mHandler.sendMessage(msg);
								}
							}
						});
				tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW,
						new TvToastFocusData("", "",TvContext.context.getResources().getString(R.string.str_tip_usb_format), 2));
			}
		}else if(data.getId().equals(TvConfigDefs.TV_CFG_PVR_USB_SPEED)){
			DiskSelectInfoData diskSelectInfoData = TvPluginControler
					.getInstance().getPvrManager().updateDiskSelectInfo();
			if (diskSelectInfoData.usbDriverCount < 1 || TvPluginControler.getInstance().getPvrManager().getSelectDiskPath().equals("")) {
				TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
				tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() {

							@Override
							public void onClickListener(boolean flag) {
								// TODO Auto-generated method stub

							}
						});
				tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW,
						new TvToastFocusData("", "", TvContext.context.getResources().getString(R.string.str_tip_plug_usb),1));
			} else {
				TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
				tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() {

							@Override
							public void onClickListener(boolean flag) {
								// TODO Auto-generated method stub
								Log.i("gky", "onBtClick is " + flag);
								if (flag) {
									checkUSBSpeed();
								} else {
									Message msg = new Message();
									msg.what = E_PVR_USB_SPEED;
									msg.obj = null;
									mHandler.sendMessage(msg);
								}
							}
						});
				tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW,
						new TvToastFocusData("", "",TvContext.context.getResources().getString(R.string.str_tip_warning_usb_checking),2));
			}
		}else if(data.getId().equals(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE)){
			MenuItem data = new MenuItem(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE_SETTING, "sound_setting", true, null);
			AudioLanguageListViewDialog listViewDialog = new AudioLanguageListViewDialog();
			listViewDialog.setDialogListener(parentView);
			listViewDialog.processCmd(TvConfigTypes.TV_DIALOG_LISTVIEW, DialogCmd.DIALOG_SHOW, data);
		}else if(data.getId().equals(TvConfigDefs.TV_CFG_SIGNAL_INFO)){
			SignalInfoDialog signalInfoDialog = new SignalInfoDialog();
			signalInfoDialog.setDialogListener(parentView);
			signalInfoDialog.processCmd(null, DialogCmd.DIALOG_SHOW, null);
		}else if(data.getId().equals(TvConfigDefs.TV_CFG_CI_INFORMATION)){
			TvPluginControler.getInstance().getCilManager().releaseListener();
			TvUIControler.getInstance().reMoveAllDialogs();
			CIInformationDialog ciInformationDialog = new CIInformationDialog();
			ciInformationDialog.setDialogListener(parentView);
			ciInformationDialog.processCmd(null, DialogCmd.DIALOG_SHOW, false);
		}else if(data.getId().equals(TvConfigDefs.TV_CFG_LNB_SETTING)){
			TvSearchTypes.isautosearch = false;
			LNBSettingDialog lnbSettingDialog = new LNBSettingDialog();
			lnbSettingDialog.setDialogListener(parentView);
			lnbSettingDialog.processCmd(TvConfigTypes.TV_DIALOG_LNB_SETTING, DialogCmd.DIALOG_SHOW, null);
		}
	}
	
	public void resetFocus()
    {
        if (bodyLayout != null)
        {
        	Log.i("gky",getClass() + " "+data.getId() +" resetFocus");
            this.setFocusable(true);
            this.requestFocus();
            this.getOnFocusChangeListener().onFocusChange(this, true);
        }
    }
	
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("gky", getClass().getName() + "-->onKey keyCode is " + keyCode +" index is "+ index);
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
			keyListener.setFocus(index + 1, keyCode);
			if (index >= 7) {
				keyListener.turnPage(keyCode, index + 1);
			}
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN) {
			keyListener.setFocus(index - 1, keyCode);
			if (index > 7) {
				keyListener.turnPage(keyCode, index);
			}
			return true;
		}
		if ((keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/)
				&& (event.getAction() == KeyEvent.ACTION_DOWN)) {
			keyListener.OnKeyDownListener(keyCode);
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
				|| keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
		{
			executeKey(view, keyCode, event);
			return true;
		}
		//其他键值不截获，继续分发处理
		Log.e("gky", getClass().getName() + "ERROR-->onKey keyCode is " + keyCode +" other key return false");
		return false;
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		updateBgDrawable(hasFocus);
        if (hasFocus)
        {
            //bodyLayout.animate().scaleX(1.05f).scaleY(1.05f).setDuration(50);
            if (!data.getItemType().equals(TvConfigType.TV_CONFIG_RANGE))
            {
                bodyLayout.setBackgroundResource(R.drawable.yellow_sel_bg);
            }
            titleView.setTextColor(0xfff0c40d);
            titleView.setSelected(true);
        } else
        {
            
            //bodyLayout.animate().scaleX(1.0f).scaleY(1.0f).setDuration(50);
            if (!data.getItemType().equals(TvConfigType.TV_CONFIG_RANGE))
            {
                bodyLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
            }
            if (isEnabled){
                titleView.setTextColor(Color.WHITE);
                titleView.setSelected(false);
            }else
            {
                titleView.setTextColor(Color.GRAY);
                titleView.setSelected(false);
            }
        }
	}
	
	public MenuItem getData() {
		return data;
	}
	
	private Handler mHandler = new Handler(){  
           
        public void handleMessage(Message msg) {
        	switch (msg.what) {  
            case E_PVR_USB_SELECT:
            	parentView.onDismissDialogDone(msg.obj);
            	break;
            case E_PVR_USB_FORMAT:
            	parentView.onDismissDialogDone(msg.obj);
            	break;
            case E_PVR_USB_SPEED:
            	parentView.onDismissDialogDone(msg.obj);
            	break;
            case PC_AUTO_ADJUST_START:
            {
            	tvLoadingDialog.processCmd(null, DialogCmd.DIALOG_SHOW,
    					new TvLoadingData(TvContext.context.getResources().getString(R.string.str_please_wait),
    							TvContext.context.getResources().getString(R.string.str_auto_adjust)));
            }
            break;
            case PC_AUTO_ADJUST_SUCCEED:
            {
            	tvLoadingDialog.processCmd(null, DialogCmd.DIALOG_DISMISS,null);
            	tvLoadingDialog = null;
            	tvLoadingDialog = new TvLoadingDialog();
            	tvLoadingDialog.processCmd(null, DialogCmd.DIALOG_UPDATE,
    					new TvLoadingData("",
    							TvContext.context.getResources().getString(R.string.str_auto_adjust_success)));
            }
            break;
            case PC_AUTO_ADJUST_FAIL:
            {
            	tvLoadingDialog.processCmd(null, DialogCmd.DIALOG_DISMISS,null);
            	tvLoadingDialog = null;
            	tvLoadingDialog = new TvLoadingDialog();
            	tvLoadingDialog.processCmd(null, DialogCmd.DIALOG_UPDATE,
    					new TvLoadingData("",
    							TvContext.context.getResources().getString(R.string.str_auto_adjust_fail)));
            }
            break;
            case PC_AUTO_ADJUST_UIDISMISS:
            {
            	tvLoadingDialog.processCmd(null, DialogCmd.DIALOG_DISMISS,null);
            }
            break;
            }  
       };  
    };        

    private void formatUSB(){
		final TvLoadingDialog tvLoadingDialog = new TvLoadingDialog();
		tvLoadingDialog.processCmd(TvConfigTypes.TV_DIALOG_LOADING,DialogCmd.DIALOG_SHOW,new TvLoadingData(TvContext.context.getResources().getString(R.string.str_please_wait),
				TvContext.context.getResources().getString(R.string.str_tip_unplug_usb)));
		selectedDiskPath = TvPluginControler.getInstance().getPvrManager().getSelectDiskPath();
		retFormat = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (TvPluginControler.getInstance().getPvrManager().getVolumeState(selectedDiskPath)
						.equals(Environment.MEDIA_MOUNTED)) 
				{
					TvPluginControler.getInstance().getPvrManager().unmountVolume(selectedDiskPath,true, false);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startFormatDisk(selectedDiskPath);
				} 
				else if (TvPluginControler.getInstance().getPvrManager().getVolumeState(selectedDiskPath)
						.equals(Environment.MEDIA_UNMOUNTED)) {
					startFormatDisk(selectedDiskPath);
				} 
				else {
					retFormat = false;
				}
				tvLoadingDialog.processCmd(TvConfigTypes.TV_DIALOG_LOADING,DialogCmd.DIALOG_DISMISS,null);
				String message = "";
				if (retFormat) {
					message = "Succeed to format!";
				} else {
					message = "Failed to format!";
				}
				Message msg = new Message();
				msg.what = E_PVR_USB_FORMAT;
				msg.obj = message;
				mHandler.sendMessage(msg);
			}
		}).start();	
		
    }
    
    private void checkUSBSpeed(){
		final TvLoadingDialog tvLoadingDialog = new TvLoadingDialog();
		tvLoadingDialog.processCmd(TvConfigTypes.TV_DIALOG_LOADING,DialogCmd.DIALOG_SHOW,
						new TvLoadingData(TvContext.context.getResources().getString(R.string.str_tip_usb_checking),
								TvContext.context.getResources().getString(R.string.str_tip_unplug_usb)));
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int speed = TvPluginControler.getInstance().getPvrManager().checkUsbSpeed();
				String data = speed + " KB/S";
				tvLoadingDialog.processCmd(TvConfigTypes.TV_DIALOG_LOADING,DialogCmd.DIALOG_DISMISS,null);
				Message msg = new Message();
				msg.what = E_PVR_USB_SPEED;
				msg.obj = data;
				mHandler.sendMessage(msg);
			}
		}).start();
	
    }
    
    private void startFormatDisk(final String path) 
    {
    	boolean result = TvPluginControler.getInstance().getPvrManager().formatVolume(path);
    	if (result) 
		{
			if (TvPluginControler.getInstance().getPvrManager().mountVolume(path)) {
				retFormat = true;
			} else {
				retFormat = false;
			}
		} 
       	else 
		{
			retFormat = false;
		}
    }
    
    private Runnable pcAutoJustRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			/*Message msg = new Message();
			msg.what = PC_AUTO_ADJUST_START;
			mHandler.sendMessage(msg);*/
			Log.i("gky", Thread.currentThread().getName() + " send PC_AUTO_ADJUST_START");
			autotune_flag = TvPluginControler.getInstance().getCommonManager().execPCAutoAdjust();
			menuItemChangeListener.OnMenuItemChangeListener(data.getId(), null);
			Log.i("gky", Thread.currentThread().getName() + " send ###########execPCAutoAdjust##############");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*mHandler.removeMessages(PC_AUTO_ADJUST_START);*/
			if (autotune_flag)
			{
				Message msgSuccess = new Message();
				msgSuccess.what = PC_AUTO_ADJUST_SUCCEED;
				mHandler.sendMessage(msgSuccess);
				Log.i("gky", Thread.currentThread().getName() + " send PC_AUTO_ADJUST_SUCCEED");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				Message msgFail = new Message();
				msgFail.what = PC_AUTO_ADJUST_FAIL;
				mHandler.sendMessage(msgFail);
				Log.i("gky", Thread.currentThread().getName() + " send PC_AUTO_ADJUST_FAIL");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Message msgDismiss = new Message();
			msgDismiss.what = PC_AUTO_ADJUST_UIDISMISS;
			Log.i("gky", Thread.currentThread().getName()+ " send PC_AUTO_ADJUST_UIDISMISS");
			mHandler.sendMessage(msgDismiss);
		}
	};

    /**
	 * 菜单子项按键监听回调
	 * @author Administrator
	 *
	 */
	public interface OnSettingKeyDownListener {
		/**
		 * 按键监听回调
		 * @param keyCode
		 */
		public void OnKeyDownListener(int keyCode);
		/**
		 * 菜单子项的Focus处理
		 * @param index
		 * @param keycode
		 */
		public void setFocus(int index, int keycode);
		/**
		 * 菜单翻页处理
		 * @param keycode
		 * @param index
		 */
		public void turnPage(int keycode, int index);
	}
	
	/**
	 * 更新子控件的状
	 * @param parentHasFocus
	 */
	public abstract void updateBgDrawable(boolean parentHasFocus);
	
	/**
	 * 子控件按键监
	 * @param view
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public abstract boolean executeKey(View view, int keyCode, KeyEvent event);
	
	/**
	 * 子控件刷新
	 */
	public abstract void refreshUI();
	
	/**
	 * 数据更新回调接口
	 * @author gky
	 *
	 */
	public interface SettingViewChangeListener{
		public void OnSettingViewChangeListener(ArrayList<MenuItem> data);
	}
}

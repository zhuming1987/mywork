package com.tv.ui.network;


import java.util.Timer;
import android.os.Handler;
import android.os.Message;
import android.net.wifi.WifiManager;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvEnumSetData;
import com.tv.framework.data.TvSetData;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvConfigDefs.TV_CFG_NETWORK_SETUP_ETHERNET_GET_CONNECT_STATE_ENUM_TYPE;
import com.tv.framework.define.TvConfigDefs.TV_CFG_NETWORK_SETUP_ETHERNET_GET_DEVICE_STATE_ENUM_TYPE;
import com.tv.framework.plugin.TvPluginManager;
import com.tv.plugin.TvPluginControler;

import com.tv.ui.Loading.SkyLoadingDialog;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.network.defs.NetConstant;
import com.tv.ui.utils.LogicTextResource;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NetworkSetupView extends LinearLayout implements DialogListener{

	Context mContext;
    private LeftViewTitleLayout titleLayout;
    private LeftViewBodyLayout bodyLayout;
    float div = TvUIControler.getInstance().getResolutionDiv();
    private TvSetData data;
    NetworkSetupDialog parentDialog = null;
    NetworkSetupView instance = null;
    private WifiManager mWifiManager;
    TvWifiSeletctDialog wifiDialog;
    SkyLoadingDialog loadingDialog;
	private static final int WAIT_ETHERNET_NULL = 0;
	private static final int WAIT_ETHERNET_DHCP = 1;
	private static final int WAIT_ETHERNET_MANUAL_IP = 2;
	private static final int WAIT_ETHERNET_CONNECTIVITY_CHANGE = 3;
	private static final int WAIT_CLOSEWIFI_DHCP = 4;
	private static final int WAIT_CLOSEWIFI_MANUAL_IP = 5;
	private static final int WAIT_CLOSEWIFI_ENABLEETH_DHCP = 6;
	private static final int WAIT_CLOSEWIFI_ENABLEETH_MANUAL_IP = 7;
	private static final int WAIT_ETHERNET_FAILED_AGAIN = 8;
	private int mWaitState = WAIT_ETHERNET_NULL;
	final Button wired,wireless;
	private int ethButtonState = 0;
	private int wirelessButtonState = 0;
	//add by chenc
	Timer mTimer;
    
    public int getEthernetDeviceStatus()
    {
    	data = TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_NETWORK_SETUP_ETHERNET_GET_DEVICE_STATE);
    	String temp = ((TvEnumSetData)data).getCurrent();
    	if(temp == TV_CFG_NETWORK_SETUP_ETHERNET_GET_DEVICE_STATE_ENUM_TYPE.TV_CFG_NETWORK_ETHERNET_CABLE_STATE_CONNECTED.toString())
    	{
    		return 1;
    	}
    	else if(temp == TV_CFG_NETWORK_SETUP_ETHERNET_GET_DEVICE_STATE_ENUM_TYPE.TV_CFG_NETWORK_ETHERNET_CABLE_STATE_DISCONNECT.toString())
    	{
    		return 0;
    	}
    	else
    	{
    		return -1;
    	}
    }
    
	public void closeWifi()
	{
		if (mWifiManager.isWifiEnabled())
		{
			mWifiManager.setWifiEnabled(false);
			Log.i("wangxian", "Disable WIFI OK!");
		} 
		else
		{
			Log.i("wangxian", "WIFI disabled before!");
		}
	}
    
    class Dhcp_Thread extends Thread 
    {
		public void run() 
		{
			TvEnumSetData temp = new TvEnumSetData();
			temp.setCurrentIndex(0);
			TvPluginControler.getInstance().setConfigData("TV_CFG_NETWORK_SETUP_ETHERNET_AUTO_CONNECT", temp);
			Thread.yield();
			wired.setClickable(true);
			wireless.setClickable(true);
		}
	}
    
    public void setParentDialog(NetworkSetupDialog parentDialog) 
    {
		this.parentDialog = parentDialog;
	}


	//add by chenc
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what == 0){
				loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
				TvUIControler.getInstance().showMniToast(TvContext.context.getResources().getString(R.string.wifiScanFailed));
			}
		}
	};
	
	final BroadcastReceiver mWirelessScanFinish = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) 
		{
			// TODO Auto-generated method stub
			if(wirelessButtonState == 1)
			{
			  if(loadingDialog != null && loadingDialog.isShowing())
			      loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
			  wifiDialog.processCmd(TvConfigTypes.TV_DIALOG_WIFI_SELECT_LIST, DialogCmd.DIALOG_SHOW, null);
			  wirelessButtonState = 0;
			 //add by chenc
			  mTimer.cancel();
			}
		}
		
	};
	
	final BroadcastReceiver mEthStateReceiver_ui = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			Log.i("wangxian", "mEthStateReceiver_ui intent.getAction() ="
					+ intent.getAction());
			String message = intent.getStringExtra("msg");
			Log.i("wangxian", "mEthStateReceiver_ui msg =" + message);
			Log.i("wangxian", "mWaitState:" + mWaitState);
			
			if ((mWaitState == WAIT_ETHERNET_DHCP)) 
			{
				mWaitState = WAIT_ETHERNET_NULL;

				if (message.equals("EVENT_INTERFACE_CONFIGURATION_SUCCEEDED")) 
				{
					Log.i("wangxian", "DHCP EVENT_INTERFACE_CONFIGURATION_SUCCEEDED");
					// to wait for android.net.conn.CONNECTIVITY_CHANGE
					mWaitState = WAIT_ETHERNET_CONNECTIVITY_CHANGE;
					
					if(loadingDialog != null && loadingDialog.isShowing())
						loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
					
					if(ethButtonState == 1)
					{
						final TvToastFocusDialog tvSucceedDialog = new TvToastFocusDialog();
						tvSucceedDialog.setOnBtClickListener(new OnBtClickListener() 
						{
							public void onClickListener(boolean flag) 
							{
								// TODO Auto-generated method stub
								tvSucceedDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
								instance.setFocusable(true);
								instance.setEnabled(true);
								instance.setFocusableInTouchMode(true);
								wired.requestFocus();
								
							}
						});
						
						tvSucceedDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
								new TvToastFocusData("", "", LogicTextResource.getInstance(TvContext.context).getText(R.string.SKY_SUCCESS),1));
					}
					
					ethButtonState = 0;
				} 
				else if (message.equals("EVENT_INTERFACE_CONFIGURATION_FAILED")) 
				{
					mWaitState = WAIT_ETHERNET_FAILED_AGAIN;
					
					if(loadingDialog != null && loadingDialog.isShowing())
						loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
					
					if(ethButtonState == 1)
					{
						final TvToastFocusDialog tvFailedDialog = new TvToastFocusDialog();
						tvFailedDialog.setOnBtClickListener(new OnBtClickListener() 
						{
							public void onClickListener(boolean flag) 
							{
								// TODO Auto-generated method stub
								tvFailedDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
								instance.setFocusable(true);
								instance.setEnabled(true);
								instance.setFocusableInTouchMode(true);
								wired.requestFocus();
							}
						});
						
						tvFailedDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
								new TvToastFocusData("", "",LogicTextResource.getInstance(TvContext.context).getText(R.string.SKY_FAILED),1));
					}
					
					ethButtonState = 0;
				}
				else
				{
					if(loadingDialog != null && loadingDialog.isShowing())
						   loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);	
					
					if(ethButtonState == 1)
					{
						instance.setFocusable(true);
						instance.setEnabled(true);
						instance.setFocusableInTouchMode(true);
					    wired.requestFocus();
					}
					
						ethButtonState = 0;
				}
			}
			else if ((mWaitState == WAIT_ETHERNET_FAILED_AGAIN)) 
			{
				if(loadingDialog != null && loadingDialog.isShowing())
					loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
				if(ethButtonState == 1)
				{
					final TvToastFocusDialog tvFailedDialog = new TvToastFocusDialog();
					tvFailedDialog.setOnBtClickListener(new OnBtClickListener() 
					{
						public void onClickListener(boolean flag) 
						{
							// TODO Auto-generated method stub
							tvFailedDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
							instance.setFocusable(true);
							instance.setEnabled(true);
							instance.setFocusableInTouchMode(true);
							wired.requestFocus();
						}
					});
					tvFailedDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
							new TvToastFocusData("", "", LogicTextResource.getInstance(TvContext.context).getText(R.string.SKY_FAILED),1));
				}
				
				ethButtonState = 0;
				Log.i("wangxian", " stop DHCP WAIT_ETHERNET_FAILED_AGAIN");
				mWaitState = WAIT_ETHERNET_NULL;
			}
			else if ((mWaitState == WAIT_ETHERNET_CONNECTIVITY_CHANGE)) 
			{
				Log.i("wangxian", "DHCP WAIT_ETHERNET_CONNECTIVITY_CHANGE");
				if(loadingDialog != null && loadingDialog.isShowing())
					loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);	
				if(ethButtonState == 1)
				{
					instance.setFocusable(true);
					instance.setEnabled(true);
					instance.setFocusableInTouchMode(true);
				    wired.requestFocus();
				}
				ethButtonState = 0;
				mWaitState = WAIT_ETHERNET_NULL;
			 } 
			else
			{
				if(loadingDialog != null && loadingDialog.isShowing())
					loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);	
				if(ethButtonState == 1)
				{
					instance.setFocusable(true);
					instance.setEnabled(true);
					instance.setFocusableInTouchMode(true);
					wired.requestFocus();
				}
				ethButtonState = 0;
			}
		}

	};

	public void unregisterRcver() 
	{
		Log.i("wangxian", "unregisterRcver");
		mContext.unregisterReceiver(mEthStateReceiver_ui);
	}
	
	public NetworkSetupView(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
		instance = this;
		mWifiManager = (WifiManager) (mContext
				.getSystemService(Context.WIFI_SERVICE));
		
		setOrientation(LinearLayout.VERTICAL);
		setFocusable(true);
		titleLayout = new LeftViewTitleLayout(context);
		titleLayout.bindData("", "Net Connection");//R.string.netconnection);
        this.addView(titleLayout);
        
        bodyLayout = new LeftViewBodyLayout(context);
        bodyLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        this.addView(bodyLayout);
        
        RelativeLayout setNetworkLayout = new RelativeLayout(context);
        bodyLayout.addView(setNetworkLayout, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);


        TextView message = new TextView(context);
        message.setText(R.string.netchoise_hint);
        message.setTextSize(NetConstant.FONT_TXT_SIZE_CONTENT);
        message.setTextColor(Color.WHITE);
        message.setPadding(0, (int)(210 / TvUIControler.getInstance().getResolutionDiv()), 0, (int)(65 / TvUIControler.getInstance().getResolutionDiv())); 
        message.setId(1);
        RelativeLayout.LayoutParams l0 = new RelativeLayout.LayoutParams(  
        		LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        l0.addRule(RelativeLayout.CENTER_HORIZONTAL);
        message.setGravity(Gravity.CENTER); 
        setNetworkLayout.addView(message, l0);
        
        LinearLayout btnLayout = new LinearLayout(context); 
        RelativeLayout.LayoutParams btnLayoutLP = new RelativeLayout.LayoutParams( 
        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); 
        btnLayoutLP.topMargin = (int)(385 / TvUIControler.getInstance().getResolutionDiv()); 
        btnLayout.setOrientation(LinearLayout.HORIZONTAL); 
        btnLayout.setGravity(Gravity.CENTER); 
        setNetworkLayout.addView(btnLayout, btnLayoutLP);
        
        LinearLayout.LayoutParams btn1Lp = new LinearLayout.LayoutParams(NetConstant.BTN_WIDTH, 
        		NetConstant.BTN_HEIGHT); 
        LinearLayout.LayoutParams btn2Lp = new LinearLayout.LayoutParams(NetConstant.BTN_WIDTH, 
        		NetConstant.BTN_HEIGHT); 
        btn2Lp.setMargins((int)(80/TvUIControler.getInstance().getResolutionDiv()), 0, 0, 0); 


        
        wired = new Button(context);
        wired.setBackgroundResource(R.drawable.buttonstate);

        wired.setId(2);
        wired.setText(R.string.wiresetup);
        wired.setTextSize(NetConstant.FONT_TXT_SIZE_BTN);
        wired.setEnabled(true);
        wired.setClickable(true);
        wired.setFocusable(true);
        wired.setFocusableInTouchMode(true);
        
        wired.requestFocus();
        btnLayout.addView(wired, btn1Lp);
        
        wireless = new Button(context);
        wireless.setBackgroundResource(R.drawable.buttonstate);
       
        wireless.setText(R.string.wirelesssetup);
        wireless.setTextSize(NetConstant.FONT_TXT_SIZE_BTN);
        wireless.setEnabled(true);
        wireless.setClickable(true);
        wireless.setFocusable(true);
        wireless.setFocusableInTouchMode(true);
        btnLayout.addView(wireless, btn2Lp);
        
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.skyworth.broadcast.network.ethernetConfiguration");
		mContext.registerReceiver(mEthStateReceiver_ui, intentFilter);
		
		IntentFilter intentWirelessScan = new IntentFilter();
		intentWirelessScan.addAction("com.sky.broadcast.net.scan.finish");
		mContext.registerReceiver(mWirelessScanFinish, intentWirelessScan);
        wired.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				ethButtonState = 1;
				if(loadingDialog != null && loadingDialog.isShowing())
				{
					loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
				}
				
				Log.i("wangxian", "*********Ethernet Button Click********");
				boolean wifiState = mWifiManager.isWifiEnabled();
				int ethState = TvPluginControler.getInstance().getNetworkManager().getEthState();
				boolean bNeedSwitch = wifiState;
				
				if(ethState != 1)
				{
					bNeedSwitch = true;
				}
				if (bNeedSwitch)
				{
					mWaitState = WAIT_CLOSEWIFI_DHCP;
					new Thread() 
					{
						public void run() 
						{
							closeWifi();
							ethButtonState = 0;
						};
					}.start();
				}
				else
				{
					int tempi = getEthernetDeviceStatus();
					if(tempi == 0)
					{
						final TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
						tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() 
						{
							public void onClickListener(boolean flag) 
							{
								// TODO Auto-generated method stub
								tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
								wired.requestFocus();
							}
						});
						tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
								new TvToastFocusData("", "",LogicTextResource.getInstance(TvContext.context).getText(R.string.SKY_insert_cable),1));
						ethButtonState = 0;
						//有线网络未插好
					}
					else
					{
						mWaitState = WAIT_ETHERNET_DHCP;
						loadingDialog = new SkyLoadingDialog();
						loadingDialog.setDialogListener(instance);
						
						loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_SHOW, null);
						instance.setFocusable(false);
						instance.setEnabled(false);
						instance.setFocusableInTouchMode(false);
						wired.setClickable(false);
						wireless.setClickable(false);
						TvPluginControler.getInstance().getNetworkManager().setEthEnabled(true);
						new Dhcp_Thread().start();
					}
				}
			}
        	
        });

        wireless.setOnClickListener(new OnClickListener()
        {
        	@Override
			public void onClick(View arg0) 
        	{
				// TODO Auto-generated method stub
				wirelessButtonState = 1;
				if(loadingDialog != null && loadingDialog.isShowing())
				{
					loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
				}
				else
				{
					loadingDialog = new SkyLoadingDialog();
					loadingDialog.setDialogListener(instance);
					loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_SHOW, null);
				}
				
				wifiDialog = new TvWifiSeletctDialog();
				Log.i("wangxian", "*******WIFI Button Click********");

				//add by chenc
				mTimer = new Timer();
				mTimer.schedule(new java.util.TimerTask(){
					public void run(){
						handler.sendEmptyMessage(0);
						wirelessButtonState = 0;
					}
				}, 20*1000);
			}
        	
        });
	}

	@Override
	public boolean onShowDialogDone(int ID) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int onDismissDialogDone(Object... obj) 
	{
		// TODO Auto-generated method stub
		parentDialog.showUI();
		return 0;
	}

}

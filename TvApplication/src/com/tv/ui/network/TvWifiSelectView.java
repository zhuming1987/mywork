package com.tv.ui.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.LinkProperties;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.IpAssignment;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiConfiguration.ProxySettings;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.data.WifiSelectData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.Animation.PageAnimation;
import com.tv.ui.Loading.SkyLoadingDialog;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.installguide.InstallGuideActivity;
import com.tv.ui.network.defs.AccessPoint;
import com.tv.ui.network.defs.NetBgAndTitleLayout;
import com.tv.ui.network.defs.NetConstant;
import com.tv.ui.network.defs.WifiEnabler;
import com.tv.ui.network.defs.WifiItem;
import com.tv.ui.network.defs.WifiListView;
import com.tv.ui.network.defs.WifiItem.WifiItemListener;
import com.tv.ui.utils.LogicTextResource;

@SuppressLint("NewApi")
public class TvWifiSelectView extends LinearLayout implements WifiItemListener, DialogListener
{
    private Context context;
    private float div = TvUIControler.getInstance().getResolutionDiv();
    private String imagePath = "wifi_select/";
    private TvWifiSeletctDialog wifiSelecteDialog;
    private WifiSelectData mWifiSelectData;
    private List<WifiSelectData> list = new ArrayList<WifiSelectData>();
    TvWifiSeletctDialog parentDialog = null;
	private static final int SECURITY_NONE = 0;
	private static final int SECURITY_WEP = 1;
	private static final int SECURITY_PSK = 2;
	private static final int SECURITY_EAP = 3;
    private ConnectivityManager connectivityManager;
    private int curSelectedIndex = 0;
    NetBgAndTitleLayout netBgAndTitle;
    TextView wpsTextView;
    TextView wifiTitleTextView;
    SkyLoadingDialog loadingDialog;
    SkyLoadingDialog loadingDialog2;
    private Button btnWPS;
    private ImageView dividerLine1;
    private ImageView dividerLine2;
    private boolean autoTimerFlag = false;
    Timer autoTimer;
    private ViewFlipper flipper;
    private final int FLIPPER_H = (int) (805 / div);
    private int FLIPPER_SHOW_ITEM_NUM = 6;
    private int pageSize = 1;
    private int curPageNum = 1;
    private TextView curPageNumTv = null;
    private WifiEnabler mWifiEnabler; 
    private WifiManager mWifiManager;
    private static final int WIFI_RESCAN_INTERVAL_MS = 10 * 1000;
    private Scanner mScanner;
	private WifiReceiver receiverWifi;
	private DhcpReceiver receiverdhcp;
	private AtomicBoolean mConnected = new AtomicBoolean(false);
	public static final int WIFI_APPLICATION = 0;
	public static final int WPS_APPLICATION = 1;
	private String state = "", original_state = "";
	private WifiInfo mWifiInfo;
	private DhcpInfo mDhcpInfo;
	private int whichApplication = WIFI_APPLICATION;
	public List<AccessPoint> accessPoints;
	private DetailedState mLastState;
	private WifiInfo mLastInfo;
    public static String currentWifiName = "";
    final int ID_WPS_BTN = -1;
    final int ID_OTHER = -2;
    private String savedPassword = "";
    private int mWaitState = WAIT_NULL;
	private static final int WAIT_NULL = 0;
	private static final int WAIT_SCAN = 1;
	private static final int WAIT_CONNECT_NoPW = 2;
	private static final int WAIT_CONNECT_WithPW_ERROR = 8;

	private static final int WAIT_CONNECT_WithPW = 3;
	private static final int WAIT_DHCP = 4;
	private static final int WAIT_MANUAL_IP = 5;
	private static final int WAIT_OPENWIFI_DHCP = 6;
	private static final int WAIT_OPENWIFI_MANUALIP = 7;
    InputWifiPwdDialog tempPwdDialog;
    String password = "";
    boolean scanFinish = false;
    boolean clickState = false; 
    int curLocked = 0;
	List<WifiItem> list_wifiitem = null;
    
	private class Scanner extends Handler
	{
		private int mRetry = 0;

		void resume()
		{
			if (!hasMessages(0))
			{
				sendEmptyMessage(0);
			}
		}

		void forceScan()
		{
			removeMessages(0);
			sendEmptyMessage(0);
		}

		void pause()
		{
			mRetry = 0;
			removeMessages(0);
		}

		@Override
		public void handleMessage(Message message)
		{
			if (mWifiManager.startScan())//.startScanActive())
			{
				mRetry = 0;
			} else if (++mRetry >= 3)
			{
				mRetry = 0;
				return;
			}
			sendEmptyMessageDelayed(0, WIFI_RESCAN_INTERVAL_MS);
		}
	}
	
	class WifiReceiver extends BroadcastReceiver
	{
		public void onReceive(Context c, Intent intent)
		{
			handleEvent(context, intent);
		}
	}
	
	class DhcpReceiver extends BroadcastReceiver
	{
		public void onReceive(Context c, Intent intent)
		{
			Intent neWifiIntent;
			Bundle bundle = intent.getExtras();
			NetworkInfo nwinfo = bundle.getParcelable("networkInfo");
			original_state = state = (nwinfo.getState()).toString();
			Log.i("wangxian", "DhcpReceiver: State(" + state + ")");
			mWifiInfo = mWifiManager.getConnectionInfo();
			Log.i("wangxian", "wifi state changed state=====>>>" + state);
			mDhcpInfo = mWifiManager.getDhcpInfo();
			if (whichApplication == WIFI_APPLICATION)
			{
				neWifiIntent = new Intent(
						TvConfigTypes.SKY_BCT_NETWORK_WIFI_CONFIGURATION);
				neWifiIntent.putExtra("msg", state);
				(context).sendBroadcast(neWifiIntent);

			} 
			else if (state.equals("CONNECTED"))
			{
				return;
			}

		}
	}
	
	public void onWifiSettingsResume()
	{
		if (mWifiEnabler != null)
		{
			mWifiEnabler.resume();
		}
		registerRcver();
		updateAccessPoints();
	}
	

	public void onWifiSettingsPause()
	{
		Log.i("wangxian", "onWifiSettingsPause");
		pauseWifiScan();
		mWifiEnabler.pause();
		unregisterRcver();
	}

	public void openWifi()
	{
		int wifiApState = mWifiManager.getWifiState();
		if ((wifiApState == mWifiManager.WIFI_STATE_ENABLING)
				|| (wifiApState == mWifiManager.WIFI_STATE_ENABLED))
		{
			Log.i("wangxian", "WIIF enabled before!");
		} else
		{
			mWifiEnabler.wifiChanged(true);
		}

	}

	public void closeWifi()
	{
		if (mWifiManager.isWifiEnabled())
		{
			mWifiManager.setWifiEnabled(false);
			Log.i("wangxian", "Disable WIFI OK!");
		} else
		{
			Log.i("wangxian", "WIFI disabled before!");
		}
	}

	public boolean isWifEnable()
	{
		return mWifiManager.isWifiEnabled();
	}
	
	public String wifiConnectivityStatus()
	{
		String connectivityStatus = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()
				.toString();
		return connectivityStatus;
	}
	
	public boolean bWifiConnected()
	{
		if (isWifEnable() && wifiConnectivityStatus().equals("CONNECTED"))
		{
			return true;
		} 
		else
		{
			return false;
		}
	}
	
	public void disconnectWifiNetId(int netId)
	{
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}

	public void disconnectWifi()
	{
		mWifiManager.disconnect();
	}
	
	public void ClearConfigNetworks()
	{
		List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration wf : configs)
		{
			mWifiManager.removeNetwork(wf.networkId);
		}

	}
	
	public void registerRcver()
	{
		IntentFilter mFilterWifi = new IntentFilter();
		mFilterWifi.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		mFilterWifi.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mFilterWifi.addAction(WifiManager.LINK_CONFIGURATION_CHANGED_ACTION);
		// mFilterWifi.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		// mFilterWifi.addAction(WifiManager.ERROR_ACTION);
		context.registerReceiver(receiverWifi, mFilterWifi);
		context.registerReceiver(receiverdhcp, new IntentFilter(
				WifiManager.NETWORK_STATE_CHANGED_ACTION));
		IntentFilter intentFilterConnect = new IntentFilter();
		intentFilterConnect
				.addAction(TvConfigTypes.SKY_BCT_NETWORK_WIFI_CONFIGURATION);
		context.registerReceiver(mWifiReceiverConnect_ui, intentFilterConnect);
		IntentFilter wifiPwdConnect = new IntentFilter();
		wifiPwdConnect
				.addAction("com.skyworth.wifi.withpassword.connect");
		context.registerReceiver(mWifiWithPwdConnect, wifiPwdConnect);
		
	}

	public void unregisterRcver()
	{
		context.unregisterReceiver(receiverWifi);
		context.unregisterReceiver(receiverdhcp);
		context.unregisterReceiver(mWifiReceiverConnect_ui);
		context.unregisterReceiver(mWifiWithPwdConnect);
	}
	    Handler loadinghandler = new Handler(){
    	public void handleMessage(Message msg)
    	{
    		loadingDialog2 = new SkyLoadingDialog();
    		Log.i("pxj", "handler receiver value"+msg);
    		if(msg.what == 1 )
    		{
    			loadingDialog2.processCmd(null, DialogCmd.DIALOG_SHOW, null);
    		}
    		if(msg.what == 0)
    		{
    			loadingDialog2.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
    		}
    	}
	   };
	final BroadcastReceiver mWifiWithPwdConnect = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			password = intent.getStringExtra("msg");
            Log.i("wangxian", "**************password= " + password);
            new WifiConnect_Thread().start();
            clickState = true;
            autoTimer(38000);
		}
	};
	
	Handler wifi_sucess_handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			Log.i("wangxian", "wifi_sucess_handler===receive==mWaitState====>>" + mWaitState);
			
			if ((mWaitState == WAIT_CONNECT_WithPW))
			{
				autoTimer.cancel();
				mWaitState = WAIT_NULL;
				if(curLocked == 0)
				{
					final TvToastFocusDialog tvSucceedDialog = new TvToastFocusDialog();
					tvSucceedDialog.setOnBtClickListener(new OnBtClickListener() 
					{
						public void onClickListener(boolean flag) 
						{
							// TODO Auto-generated method stub
							tvSucceedDialog.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);
						}
					});
					tvSucceedDialog.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW, 
							new TvToastFocusData("", "", LogicTextResource.getInstance(TvContext.context).getText(R.string.SKY_SUCCESS),1));
					loadingDialog2.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
					loadinghandler.sendEmptyMessage(0);
				}
				else
				{
					/*
					tempPwdDialog.getInputWifiPwdView().getErrorTextView().setTextColor(Color.GREEN);
					tempPwdDialog.getInputWifiPwdView().getErrorTextView().setVisibility(View.VISIBLE);
					tempPwdDialog.getInputWifiPwdView().getErrorTextView().setText(LogicTextResource.getInstance(TvContext.context).getText(R.string.SKY_SUCCESS));
					*/
					//add by chenc 2015/04/30
					tempPwdDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
					Log.i("chenclog","curSelectedIndex = "+curSelectedIndex+"curPageNum = "+curPageNum+
						"pageSize = "+pageSize);
					if(curPageNum>0){
						list.get((curPageNum-1)*FLIPPER_SHOW_ITEM_NUM+curSelectedIndex).setIsSelected(true);
						list_wifiitem.get((curPageNum-1)*FLIPPER_SHOW_ITEM_NUM+curSelectedIndex).
						updateItemData(list.get((curPageNum-1)*FLIPPER_SHOW_ITEM_NUM+curSelectedIndex).
								getIsSelected(), list.get((curPageNum-1)*FLIPPER_SHOW_ITEM_NUM+curSelectedIndex).
								getWifiName(), list.get((curPageNum-1)*FLIPPER_SHOW_ITEM_NUM+curSelectedIndex).
								getIsEncrypt(), list.get((curPageNum-1)*FLIPPER_SHOW_ITEM_NUM+curSelectedIndex).
								getLevel());
					}
			            	
			
					loadingDialog2.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
					loadinghandler.sendEmptyMessage(0);
				}
			}
			super.handleMessage(msg);
		}
	};
	
	final BroadcastReceiver mWifiReceiverConnect_ui = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{

			String message = intent.getStringExtra("msg");
			Log.i("wangxian", "mWifiReceiverConnect_ui state===>>>"
					+ message);

			if (message.equals("CONNECTED"))
			{
				Log.i("wangxian", 
						"mWaitState<<===WAIT_CONNECT_WithPW");
				
				if(scanFinish && clickState)
				{
				   clickState = false;
				   mWaitState = WAIT_CONNECT_WithPW;
				   Message msg = new Message();
				   if(loadingDialog != null && loadingDialog.isShowing()){
					   loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
				   }
				   wifi_sucess_handler.sendMessage(msg);
				}
				Log.i("wangxian", "send wifi_sucess_handler");

			} 
			else
			{
				Log.i("wangxian", 
						"mWaitState<<===WAIT_CONNECT_WithPW_ERROR");
				if(scanFinish)
				{
				   mWaitState = WAIT_CONNECT_WithPW_ERROR;
				}
			}
			
			if (mWaitState == WAIT_CONNECT_NoPW)
			{
				autoTimerCancel();
				if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState().toString().equals("CONNECTED"))
				{
					mWaitState = WAIT_NULL;
					if(loadingDialog != null && loadingDialog.isShowing())
					{
						loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
					}
					final TvToastFocusDialog tvSucceedDialog = new TvToastFocusDialog();
					tvSucceedDialog.setOnBtClickListener(new OnBtClickListener() 
					{
						public void onClickListener(boolean flag) {
							// TODO Auto-generated method stub
							tvSucceedDialog.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);	
						}
					});
					tvSucceedDialog.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW, 
							new TvToastFocusData("", "", LogicTextResource.getInstance(TvContext.context).getText(R.string.SKY_SUCCESS),1));
					
				///获取无线状态,显示成功
				}
			}
		}
	};
	
    public void initWifiConf()
    {
    	mWifiEnabler = new WifiEnabler(context, true);
    	mScanner = new Scanner();
		receiverWifi = new WifiReceiver();
		receiverdhcp = new DhcpReceiver();
		onWifiSettingsResume();
    }
    
    
    public TvWifiSelectView(Context context)
    {
        super(context);
        this.context = context;
        mWifiManager = (WifiManager) ((context)
				.getSystemService(Context.WIFI_SERVICE));
        connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        initWifiConf();
        createView();
        scanFinish = false;
        mWaitState = WAIT_NULL;
        autoTimerFlag = false;
        SetupWifiScan();
    }

	private void updateWifiState(int state)
	{
		switch (state)
		{
		case WifiManager.WIFI_STATE_ENABLED:
			Log.i("wangxian", "WIFI_STATE_ENABLED");
			return; // not break, to avoid the call to pause() below
		case WifiManager.WIFI_STATE_ENABLING:
			Log.i("wangxian", "WIFI_STATE_ENABLING");
			break;
		case WifiManager.WIFI_STATE_DISABLED:
			Log.i("wangxian", "WIFI_STATE_DISABLED");
			break;
		}

		mLastInfo = null;
		mLastState = null;
	}
	
	private String getSecurity(WifiConfiguration config)
	{
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK))
		{
			return "WPA_PSK";
		}
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP)
				|| config.allowedKeyManagement.get(KeyMgmt.IEEE8021X))
		{
			return "WPA_EAP";
		}
		return (config.wepKeys[0] != null) ? "SECURITY_WEP" : "SECURITY_NONE";
	}	
	
	private int getSecurityWithCfg(WifiConfiguration config)
	{
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK))
		{
			return SECURITY_PSK;
		}
		if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP)
				|| config.allowedKeyManagement.get(KeyMgmt.IEEE8021X))
		{
			return SECURITY_EAP;
		}
		return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
	}

	private WifiConfiguration getConfig(WifiInfo currWifiInfo)
	{
		if (-1 == currWifiInfo.getNetworkId() || null == currWifiInfo)
		{
			Log.i("wangxian", "reconnect the network");
			return null;
		}

		WifiConfiguration currentCfg = null;
		WifiConfiguration config = new WifiConfiguration();

		List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
		String ssidStr = "\"" + currWifiInfo.getSSID() + "\"";

		Log.i("wangxian", "===>configs.size() = " + configs.size());
		for (WifiConfiguration cfg : configs)
		{
			if (ssidStr.equals(cfg.SSID))
			{
				currentCfg = cfg;
				Log.i("wangxian", "===>configs.toString() = " + cfg.toString());
				break;
			}
		}

		if (null == currentCfg)
		{
			Log.i("wangxian", "mResult == null");
			return null;
		}

		int security = getSecurityWithCfg(currentCfg);

		switch (security)
		{
		case SECURITY_NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;

		case SECURITY_WEP:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			break;

		case SECURITY_PSK:
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			break;

		case SECURITY_EAP:
			config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
			config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
//			config.eap.setValue(getSecurity(config));
//
//			config.phase2.setValue("");
//			config.ca_cert.setValue("");
//			config.client_cert.setValue("");
//			config.key_id.setValue("");
//			config.identity.setValue("");
//			config.anonymous_identity.setValue("");
			break;
		default:
			return null;
		}

		config.networkId = currWifiInfo.getNetworkId();
		config.proxySettings = ProxySettings.NONE;
		return config;
	}
	public boolean SetupWifiAutoDhcp()
	{
		// TODO Auto-generated method stub
		WifiConfiguration currentConfiguration;
		if (mWifiManager.isWifiEnabled())
		{
			WifiInfo currWifiInfo = mWifiManager.getConnectionInfo();
			WifiConfiguration config = getConfig(currWifiInfo);
			if (null == config)
			{
				Log.i("wangxian", "##===>config is null");
				return false;
			}
			TvPluginControler.getInstance().getNetworkManager().setEthEnabled(false);
			config.ipAssignment = IpAssignment.DHCP;
			Log.i("wangxian", "saveNetwork(config);");
			// mWifiManager.save(mWifiChannel,config,null);
			mWifiManager.save(config, null);
			Log.i("wangxian", "##===>config.toString() = " + config.toString());
			return true;

		} else
		{
			Log.i("wangxian", "###===>.mWifiManager.isWifiEnabled()== false");
			return false;
		}
	}
	public void SetupWifiScan()
	{
		Log.i("wangxian", "SetupWifiScan");
		if (!mWifiManager.isWifiEnabled())
		{
			Log.i("wangxian", "SetupWifiScan2");
			try{
				mWifiManager.setWifiEnabled(true);
			}catch(RuntimeException r){
				Log.i("chenclog","who disable wifi ="+r.toString());
				if(mWifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED){
					try{
						mWifiManager.setWifiEnabled(true);
					}catch(RuntimeException e){
						Log.i("chenclog","who disable wifi1 ="+e.toString());
					}
				}
			}
			mScanner.forceScan();
		} else
		{
			Log.i("wangxian", "SetupWifiScan3");
			mScanner.forceScan();
		}
	}
	
	private List<AccessPoint> constructAccessPoints()
	{
		ArrayList<AccessPoint> accessPoints = new ArrayList<AccessPoint>();

		final List<ScanResult> results = mWifiManager.getScanResults();
		if (results != null)
		{
			Log.i("wangxian", "constructAccessPoints results not null");

			for (ScanResult results_p : results)
			{
				Log.i("wangxian", "results_p.SSID" + results_p.SSID);
				Log.i("wangxian", "results_p.capabilities"
						+ results_p.capabilities);
			}
			
			for (ScanResult result : results)
			{
				if (result.SSID == null || result.SSID.length() == 0
						|| result.capabilities.contains("[IBSS]"))
				{
					continue;
				}
				AccessPoint accessPoint = new AccessPoint(context, result);
				accessPoints.add(accessPoint);
			}
		}
		
		for (AccessPoint accessPoint_p1 : accessPoints)
		{
			Log.i("wangxian", "ssid,level" + accessPoint_p1.ssid
					+ accessPoint_p1.getLevel());
		}
		
		for (AccessPoint accessPoint_p2 : accessPoints)
		{
			Log.i("wangxian", "ssid" + accessPoint_p2.ssid);
			Log.i("wangxian", "level" + accessPoint_p2.getLevel());
		}

		return accessPoints;
	}
	
	public List<AccessPoint> returnAccessPoints()
	{
		final int wifiState = mWifiManager.getWifiState();
		if (wifiState == WifiManager.WIFI_STATE_ENABLED) // || TvPluginControler.getInstance().getNetworkManager().isDongleDeviceExist())
		{
			accessPoints = constructAccessPoints();
			return accessPoints;
		} 
		else
			return null;
	}
	
	void pauseWifiScan()
	{
		if (mWifiManager.isWifiEnabled())
		{
			Log.i("wangxian", "pauseWifiScan isWifiEnabled");
			mScanner.pause();
		} 
		else
		{
			Log.i("wangxian", "pauseWifiScan already disabled");

		}
	}
	
	private void updateAccessPoints()
	{
		final int wifiState = mWifiManager.getWifiState();
		switch (wifiState)
		{
		case WifiManager.WIFI_STATE_ENABLED:
			// AccessPoints are automatically sorted with TreeSet.
			Log.i("wangxian", "WIFI_STATE_ENABLED");
			break;

		case WifiManager.WIFI_STATE_ENABLING:
			Log.i("wangxian", "WIFI_STATE_ENABLING");
			// getPreferenceScreen().removeAll();
			break;

		case WifiManager.WIFI_STATE_DISABLING:
			Log.i("wangxian", "WIFI_STATE_DISABLING");
			// addMessagePreference(R.string.wifi_stopping);
			break;

		case WifiManager.WIFI_STATE_DISABLED:
			Log.i("wangxian", "WIFI_STATE_DISABLED");
			// addMessagePreference(R.string.wifi_empty_list_wifi_off);
			break;
		}
	}
	
	private void handleEvent(Context context, Intent intent)
	{
		String action = intent.getAction();
		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action))
		{
			Log.i("wangxian", "handleEvent WIFI_STATE_CHANGED_ACTION");
			updateWifiState(intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
					WifiManager.WIFI_STATE_UNKNOWN));
		} else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action))
		{
			Log.i("wangxian", 
					"SCAN_RESULTS_AVAILABLE_ACTION");
			Log.i("wangxian", "handleEvent SCAN_RESULTS_AVAILABLE_ACTION");
			if (whichApplication == WIFI_APPLICATION)
			{
				pauseWifiScan();
				updateAccessPoints();
				returnAccessPoints();
				Intent netWifiIntent;
				netWifiIntent = new Intent(
						"com.sky.broadcast.net.scan.finish");
			
				(this.context).sendBroadcast(netWifiIntent);
				scanFinish = true;
			} else if (whichApplication == WPS_APPLICATION)
			{
				//导航不支持WPS................
			}

		} else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action))
		{
			Log.i("wangxian", "handleEvent NETWORK_STATE_CHANGED_ACTION");
			NetworkInfo info = (NetworkInfo) intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			mConnected.set(info.isConnected());
			updateAccessPoints();
		}
	}
	

	public boolean isWifiConnected()
	{
		if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState().toString().equals("CONNECTED") )
		{
			return true;
		}
		else
		{
		    return false;
		}
	}	
	
    public void createView()
    {
        netBgAndTitle = new NetBgAndTitleLayout();
        RelativeLayout layout = new RelativeLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        wifiTitleTextView = new TextView(context);
        RelativeLayout.LayoutParams wifiTitleLP = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        wifiTitleLP.setMargins((int) (32 / div), (int) (160 / div), 0, 0);
        wifiTitleTextView.setText(context.getText(R.string.NET_WIFI_SELECT_LIST));
        wifiTitleTextView.setTextColor(Color.WHITE);
        wifiTitleTextView.setTextSize(NetConstant.FONT_TXT_SIZE_CONTENT);

        curPageNumTv = new TextView(context);
        RelativeLayout.LayoutParams curPageNumTvLP = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        curPageNumTvLP.setMargins(0, (int) (160 / div), (int) (60 / div), 0);
        curPageNumTvLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        curPageNumTv.setTextColor(Color.WHITE);
        curPageNumTv.setTextSize(NetConstant.FONT_TXT_SIZE_CONTENT);

        dividerLine1 = new ImageView(context);
        RelativeLayout.LayoutParams line1_lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (2 / div));
        line1_lp.setMargins(0, (int) (132 / div), 0, 0);
        dividerLine1.setImageResource(R.drawable.divider_line);
        dividerLine1.setScaleType(ScaleType.FIT_XY);
        dividerLine1.setPadding((int) (30 / div), 0, (int) (30 / div), 0);

//        以后调试，留用
//        dividerLine2 = new ImageView(context);
//        RelativeLayout.LayoutParams line2_lp = new RelativeLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT, (int) (2 / div));
//        line2_lp.setMargins(0, (int) (235 / div), 0, 0);
//        dividerLine2.setImageResource(R.drawable.divider_line);
//        dividerLine2.setScaleType(ScaleType.FIT_XY);
//        dividerLine2.setPadding((int) (30 / div), 0, (int) (30 / div), 0);

        initFlipper();
        layout.addView(netBgAndTitle);
        layout.addView(wifiTitleTextView, wifiTitleLP);
        layout.addView(dividerLine1, line1_lp);
//        layout.addView(dividerLine2, line2_lp);  以后调试，留用
        layout.addView(flipper);
        layout.addView(curPageNumTv, curPageNumTvLP);
        this.addView(layout,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        this.setBackgroundResource(R.drawable.setting_bg);
    }

    private void initFlipper()
    {
        if (flipper == null)
        {
            flipper = new ViewFlipper(context);
            RelativeLayout.LayoutParams flipperLp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, FLIPPER_H);
            flipperLp.setMargins(0, (int) (235 / div), 0, 0);
            flipper.setLayoutParams(flipperLp);
        }
    }

    OnClickListener btnWPSClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == ID_WPS_BTN)
            {
                
            }
        }
    };

    OnFocusChangeListener btnOnFocusChangeLis = new OnFocusChangeListener()
    {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            Button btn = (Button) v;
            if (hasFocus)
            {
                btn.setTextColor(Color.BLACK);
            } else
            {
                btn.setTextColor(Color.WHITE);
            }
        }
    };

    public void updateWifiSlectView(WifiSelectData wifiSelectData)
    {
        int listSize = 0;
        curPageNum = 1;
        if (wifiSelectData != null)
        {
        	this.mWifiSelectData = wifiSelectData;
            flipper.removeAllViews();
            netBgAndTitle.update(wifiSelectData.getTitleBarIcon(),
                    wifiSelectData.getTitleBarTitle());
            if (mWifiSelectData.getChildList() != null)
            {
                list = mWifiSelectData.getChildList();
                listSize = list.size() + 1;// 默认最后边有个其他，所以多给一项
                if (listSize % FLIPPER_SHOW_ITEM_NUM == 0)
                {
                    pageSize = listSize / FLIPPER_SHOW_ITEM_NUM;
                } else
                {
                    pageSize = listSize / FLIPPER_SHOW_ITEM_NUM + 1;
                }
                Log.i("wangxian", "list size + 1 = " + listSize + " , page size :" + pageSize);
            }
            for (int i = 0; i < listSize - 2; i++)
            {
            	Log.i("wangxian", "wifi view name & level & encrypt " + i + " : "
                        + list.get(i).getWifiName() + " ,  " + list.get(i).getLevel() + " ,  "
                        + list.get(i).getIsEncrypt());
            }

		list_wifiitem = new ArrayList<WifiItem>();
            for (int i = 0; i < pageSize; i++)
            {
            	Log.i("wangxian", "**********wangxian*********i = " + i);
                WifiListView wifiListView = new WifiListView(context);
                for (int j = 0; j < FLIPPER_SHOW_ITEM_NUM; j++)
                {
                    WifiItem wifiItem = null;
                    Log.i("wangxian", "**********wangxian*********j = " + j);
                    if (i == pageSize - 1)
                    {
                        if (i * FLIPPER_SHOW_ITEM_NUM + j < listSize - 1)
                        {
                            wifiItem = new WifiItem(context, this);
                            list_wifiitem.add(wifiItem);
							
                            wifiItem.updateItemData(list.get(i * FLIPPER_SHOW_ITEM_NUM + j)
                                    .getIsSelected(), list.get(i * FLIPPER_SHOW_ITEM_NUM + j)
                                    .getWifiName(), list.get(i * FLIPPER_SHOW_ITEM_NUM + j)
                                    .getIsEncrypt(), list.get(i * FLIPPER_SHOW_ITEM_NUM + j)
                                    .getLevel());
                            wifiItem.setId(i * FLIPPER_SHOW_ITEM_NUM + j);
                        } 
                        else if (i * FLIPPER_SHOW_ITEM_NUM + j == listSize - 1)
                        {
                        	 /*
                            wifiItem = new WifiItem(context, this);
				list_wifiitem.add(wifiItem);
                            wifiItem.updateOtherData();
                            wifiItem.setId(ID_OTHER);
                            */
                        }
                    } 
                    else
                    {
                        wifiItem = new WifiItem(context, this);
                        list_wifiitem.add(wifiItem);
						
                        Log.i("wangxian", "**********wangxian*********wifiItem.updateItemData");
                        wifiItem.updateItemData(list.get(i * FLIPPER_SHOW_ITEM_NUM + j)
                                .getIsSelected(), list.get(i * FLIPPER_SHOW_ITEM_NUM + j)
                                .getWifiName(), list.get(i * FLIPPER_SHOW_ITEM_NUM + j)
                                .getIsEncrypt(), list.get(i * FLIPPER_SHOW_ITEM_NUM + j).getLevel());
                        wifiItem.setId(i * FLIPPER_SHOW_ITEM_NUM + j);
                    }
                    if (wifiItem != null)
                    {
                        wifiListView.addView(wifiItem);
                    }
                }
                flipper.addView(wifiListView);
            }
            updateCurPageNum();
        }
    }

    public void sendBackCmdToTargetService()
    {
        wifiSelecteDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
    }

    @Override
    public boolean onWifiItemKeyDown(int keyCode, int itemID)
    {
    	Log.i("wangxian", "-------wifi item keydown keycode, id--------" + keyCode
                + ", " + itemID);
        
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
            	parentDialog.processCmd(TvConfigTypes.TV_DIALOG_WIFI_SELECT_LIST, DialogCmd.DIALOG_DISMISS, null);
            case KeyEvent.KEYCODE_DPAD_UP:
                if (itemID == 0)
                {
                    return false;
                }
                if (itemID == ID_OTHER)
                {
                    if (mWifiSelectData != null && mWifiSelectData.getChildList() != null)
                    {
                    	if (0 == mWifiSelectData.getChildList().size() % FLIPPER_SHOW_ITEM_NUM)
                        {
                    		if (mWifiSelectData.getChildList().size() == 0)
                            {
                                return false;
                            }
                            showPrePage();
                            return true;
                        } else
                        {
                            return false;
                        }
                    }
                    return false;
                }
                if (itemID % FLIPPER_SHOW_ITEM_NUM == 0)
                {
                    showPrePage();
                    return true;
                } else
                {
                    return false;
                }
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (itemID % FLIPPER_SHOW_ITEM_NUM == FLIPPER_SHOW_ITEM_NUM - 1)
                {
                    showNextPage();
                    return true;
                } else
                {
                    return false;
                }
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                sendRightDownCmd(itemID);
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            	if(itemID == ID_OTHER)
            	{
            		return true;
            	}
                sendItemClickCmd(itemID, mWifiSelectData.getChildList().get(itemID).getIsEncrypt());
                return true;
            default:
                return false;
        }
    }

    private void showNextPage()
    {
        flipper.setOutAnimation(PageAnimation.getInstance().upOutAnim);
        flipper.setInAnimation(PageAnimation.getInstance().downInAnim);
        flipper.showNext();
        curPageNum = flipper.getDisplayedChild() + 1;
        updateCurPageNum();
    }

    public void showPrePage()
    {
        flipper.setOutAnimation(PageAnimation.getInstance().downOutAnim);
        flipper.setInAnimation(PageAnimation.getInstance().upInAnim);
        flipper.showPrevious();
        WifiListView wifiListView = (WifiListView) flipper.getCurrentView();
        if (wifiListView != null)
        {
            wifiListView.getChildAt(FLIPPER_SHOW_ITEM_NUM - 1).requestFocus();
        }
        curPageNum = flipper.getDisplayedChild() + 1;
        updateCurPageNum();
    }

    private void updateCurPageNum()
    {
        curPageNumTv.setText(curPageNum + "/" + pageSize);
    }

    private void sendRightDownCmd(int selectIndex)
    {
        
    }

	private WifiConfiguration getWifiConfig(AccessPoint mSelectedAccessPoint,
			String password)
	{
		List<WifiConfiguration> mWifiConfiguration = mWifiManager
				.getConfiguredNetworks();
		for (WifiConfiguration wf : mWifiConfiguration)
			mWifiManager.removeNetwork(wf.networkId);
		WifiConfiguration config = new WifiConfiguration();
		if (mSelectedAccessPoint == null)
		{
			Log.i("wangxian", "mSelectedAccessPoint null");
			return config;
		}
	
		int mAccessPointSecurity = (mSelectedAccessPoint == null) ? AccessPoint.SECURITY_NONE
				: mSelectedAccessPoint.security;
		Log.i("wangxian", "getWifiConfig mAccessPointSecurity:"
				+ mAccessPointSecurity);
		config.SSID = AccessPoint.convertToQuotedString(mSelectedAccessPoint.ssid);
		Log.i("wangxian", "getWifiConfig mSelectedAccessPoint.bssid"
				+ mSelectedAccessPoint.bssid);
		config.BSSID = mSelectedAccessPoint.bssid;
		switch (mAccessPointSecurity)
		{
		case AccessPoint.SECURITY_NONE:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			break;
		case AccessPoint.SECURITY_WEP:
			config.allowedKeyManagement.set(KeyMgmt.NONE);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
			config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
			{
				int length = password.length();
				// WEP-40, WEP-104, and 256-bit WEP (WEP-232?)
				if ((length == 10 || length == 26 || length == 58)
						&& password.matches("[0-9A-Fa-f]*"))
				{
					config.wepKeys[0] = password;
				} else
				{
					config.wepKeys[0] = '"' + password + '"';
				}
			}
			break;
		case AccessPoint.SECURITY_PSK:
			config.allowedKeyManagement.set(KeyMgmt.WPA_PSK);
			if (password.length() != 0)
			{
				if (password.matches("[0-9A-Fa-f]{64}"))
				{
					config.preSharedKey = password;
				} else
				{
					config.preSharedKey = '"' + password + '"';
				}
			}
			break;
		case AccessPoint.SECURITY_EAP:
			config.allowedKeyManagement.set(KeyMgmt.WPA_EAP);
			config.allowedKeyManagement.set(KeyMgmt.IEEE8021X);
//			config.eap.setValue(getSecurity(config));
//
//			config.phase2.setValue("");
//			config.ca_cert.setValue("");
//			config.client_cert.setValue("");
//			config.key_id.setValue("");
//			config.identity.setValue("");
//			config.anonymous_identity.setValue("");
//			if (password.length() != 0)
//			{
//				config.password.setValue(password);
//			}
			break;
		default:
			return null;
		}

		config.proxySettings = ProxySettings.NONE;
		;
		config.ipAssignment = IpAssignment.DHCP;
		config.linkProperties = new LinkProperties();

		return config;
	}
	
	public boolean SetupWifiConnectWithPW(AccessPoint mSelectedAccessPoint,
			String password)
	{
		Log.i("pxj", "get into method SetupWifiConnectWithPW111");
		// 关掉有线
		TvPluginControler.getInstance().getNetworkManager().setEthEnabled(false);
		// 打开无线
		openWifi();

		// 得到WifiConfiguration对象
		WifiConfiguration config = getWifiConfig(mSelectedAccessPoint, password);
		if (null != config)
		{
			savedPassword = password;
			config.ipAssignment = IpAssignment.DHCP;
			mWifiManager.connect(config, null);// (mSelectedAccessPoint.getConfig(),null);

			int networkId = mWifiManager.addNetwork(config);
			// config.ipAssignment = IpAssignment.DHCP;
			if (networkId != -1)
			{
				config.networkId = networkId;
				Log.i("wangxian", "before updateNetwork");
				mWifiManager.updateNetwork(config);
				Log.i("wangxian", "after updateNetwork");
				mWifiManager.enableNetwork(networkId, true);
				Log.i("wangxian", "after enableNetwork");
				mWifiManager.saveConfiguration();
			} 
			else
			{
				Log.i("wangxian", "networkId:-1");
			}
		} else
		{
			Log.i("wangxian", "config is null");
		}
		Log.i("pxj", "get into method SetupWifiConnectWithPW222");
		
		return true;
	}
	class WifiConnect_Thread extends Thread
	{
		private boolean ret;

		public void run()
		{
		    loadinghandler.sendEmptyMessage(1);
			AccessPoint accessPoints_current = null;
			accessPoints_current = accessPoints
					.get(curSelectedIndex);
			SetupWifiConnectWithPW(
					accessPoints_current, password);
			// Thread.yield();
		
			
		}
	}
	
	private void autoTimerCancel()
	{
		if (autoTimerFlag)
		{
			autoTimer.cancel();
			autoTimerFlag = false;
		}
	}
	
	private void autoTimer(int delayedms)
	{
		Log.i("wangxian", "sutoTimer");
		autoTimerCancel();
		autoTimer = new Timer();
		autoTimerFlag = true;
		final Handler handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case 1:
					clickState = false;
					Log.i("wangxian", "mWaitState====>>"
							+ mWaitState);
					if (mWaitState == WAIT_CONNECT_NoPW)
					{
						autoTimer.cancel();
						mWaitState = WAIT_NULL;
						if(loadingDialog != null && loadingDialog.isShowing())
						{
							loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
						}
						final TvToastFocusDialog tvFailedDialog = new TvToastFocusDialog();
						tvFailedDialog.setOnBtClickListener(new OnBtClickListener() 
						{
							public void onClickListener(boolean flag) 
							{
								// TODO Auto-generated method stub
								tvFailedDialog.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);	
							}
						});
						tvFailedDialog.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW, 
								new TvToastFocusData("", "",LogicTextResource.getInstance(TvContext.context).getText(R.string.SKY_SUCCESS),1));
						//无密码显示连接失败
						loadingDialog2.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
						loadinghandler.sendEmptyMessage(0);
					} 
					else if (mWaitState == WAIT_CONNECT_WithPW_ERROR)
					{
						Log.i("wangxian", 
								"handler=====WAIT_CONNECT_WithPW_ERROR");
						autoTimer.cancel();
						mWaitState = WAIT_NULL;
						if(loadingDialog != null && loadingDialog.isShowing())
						{
							loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_DISMISS, null);
						}
						if(curLocked == 0)
						{
							final TvToastFocusDialog tvFailedDialog = new TvToastFocusDialog();
							tvFailedDialog.setOnBtClickListener(new OnBtClickListener() 
							{
								public void onClickListener(boolean flag) 
								{
									// TODO Auto-generated method stub
									tvFailedDialog.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);
									loadingDialog2.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
									loadinghandler.sendEmptyMessage(0);
								}
							});
							tvFailedDialog.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW, 
									new TvToastFocusData("", "", LogicTextResource.getInstance(TvContext.context).getText(R.string.SKY_FAILED),1));
							loadingDialog2.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
							loadinghandler.sendEmptyMessage(0);
						}
						else
						{
							tempPwdDialog.getInputWifiPwdView().getErrorTextView().setTextColor(Color.RED);
							tempPwdDialog.getInputWifiPwdView().getErrorTextView().setVisibility(View.VISIBLE);
							tempPwdDialog.getInputWifiPwdView().getErrorTextView().setText(LogicTextResource.getInstance(TvContext.context).getText(R.string.SKY_FAILED));
							loadingDialog2.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
							loadinghandler.sendEmptyMessage(0);
						}
						//有密码显示连接失败
					} 
					else if (mWaitState == WAIT_CONNECT_WithPW)
					{
						Log.i("wangxian", 
								"handler=====WAIT_CONNECT_WithPW");
						autoTimer.cancel();
						mWaitState = WAIT_NULL;
						//未完，有密码显示连接成功
					}
					break;
				}
				super.handleMessage(msg);
			}
		};

		TimerTask task = new TimerTask()
		{
			public void run()
			{
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
			}
		};

		autoTimer.schedule(task, delayedms);
	}
	
    private void sendItemClickCmd(int itemID, int locked)
    {
    	curSelectedIndex = itemID;
    	curLocked = locked;

		
    	Log.i("wangxian", "**********wangxian***********curLocked = " + curLocked);
        if(curLocked == 0)
        {
        	password = "";
        	Log.i("wangxian", "********no_password_net_click...........");
        	clickState = true;
        	loadingDialog = new SkyLoadingDialog();		
    		loadingDialog.processCmd(TvConfigTypes.TV_LOADING_DIALOG, DialogCmd.DIALOG_SHOW, null);
        	new WifiConnect_Thread().start();
        	autoTimer(38000);
    		mWaitState = WAIT_CONNECT_NoPW;
        	//无密码直接连接
        }
        else
        {
        	Log.i("wangxian", "*******with_password_net_click...........");
        	tempPwdDialog = new InputWifiPwdDialog(InstallGuideActivity.ContextTemp);
        	tempPwdDialog.setDialogListener(this);
        	tempPwdDialog.processCmd(TvConfigTypes.INPUT_NETWORK_PASSWORD, DialogCmd.DIALOG_SHOW, null);
        	//有密码输入密码框连接
        }
    }

	public void setWifiSelecteDialog(TvWifiSeletctDialog wifiSelecteDialog) 
	{
		this.wifiSelecteDialog = wifiSelecteDialog;
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
		return 0;
	}
	
    public void setParentDialog(TvWifiSeletctDialog parentDialog) 
    {
		this.parentDialog = parentDialog;
	}
    
}

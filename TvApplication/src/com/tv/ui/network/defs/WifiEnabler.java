/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tv.ui.network.defs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


//import com.android.settings.WirelessSettings;

import java.util.concurrent.atomic.AtomicBoolean;



public class WifiEnabler  {
    private final Context mContext;
    private AtomicBoolean mConnected = new AtomicBoolean(false);
	private static final String DebugString = "";

    private final WifiManager mWifiManager;
    private boolean mStateMachineEvent;
    private final IntentFilter mIntentFilter;
    
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() 
    {
        @Override
        public void onReceive(Context context, Intent intent) 
        {
            String action = intent.getAction();
            Log.i("wangxian", DebugString +  "onReceive");					
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) 
            {
                handleWifiStateChanged(intent.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN));
            } 
            else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) 
            {
                NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(
                        WifiManager.EXTRA_NETWORK_INFO);
                mConnected.set(info.isConnected());
            }
        }
    };
    
    public WifiEnabler(Context context,boolean bEnable) 
    {
        mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mIntentFilter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }
    
    public void wifiChanged(boolean isChecked) 
    {
    	if (mWifiManager.setWifiEnabled(isChecked)) 
   		{
    		Log.i("wangxian", DebugString +  "success");	
			int statea =mWifiManager.getWifiState();
			Log.i("wangxian", DebugString +  "statea:"+statea);
		} 
   		else 
   		{
            // Error
   			Log.i("wangxian", DebugString +  "error");		   
        }
    }
    
    public void resume() 
    {
        // Wi-Fi state is sticky, so just let the receiver update UI
    	mContext.registerReceiver(mReceiver, mIntentFilter);
    }

    public void pause() 
    {
    	mContext.unregisterReceiver(mReceiver);
    }

    private void handleWifiStateChanged(int state) 
    {
    	switch (state) 
        {
    	case WifiManager.WIFI_STATE_ENABLING:
    		Log.i("wangxian", DebugString +  "WIFI_STATE_ENABLING");		
            break;
        case WifiManager.WIFI_STATE_ENABLED:
            Log.i("wangxian", DebugString +  "WIFI_STATE_ENABLED");		
            break;
        case WifiManager.WIFI_STATE_DISABLING:
            Log.i("wangxian", DebugString +  "WIFI_STATE_DISABLING");		
            break;
        case WifiManager.WIFI_STATE_DISABLED:           
            Log.i("wangxian", DebugString +  "WIFI_STATE_DISABLED");		
            break;
        default:       
            Log.i("wangxian", DebugString +  "default");		
            break;
        }
    }
}

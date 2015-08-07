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


import android.content.Context;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;


public class AccessPoint {
    static final String TAG = "";
    private Context mContext = null;
    private static final String KEY_DETAILEDSTATE = "key_detailedstate";
    private static final String KEY_WIFIINFO = "key_wifiinfo";
    private static final String KEY_SCANRESULT = "key_scanresult";
    private static final String KEY_CONFIG = "key_config";
    private static final int[] STATE_NONE = {};

    /** These values are matched in string arrays -- changes must be kept in sync */
    public static final int SECURITY_NONE = 0;
    public static  final int SECURITY_WEP = 1;
    public  static final int SECURITY_PSK = 2;
    public static final int SECURITY_EAP = 3;

    enum PskType 
    {
        UNKNOWN,
        WPA,
        WPA2,
        WPA_WPA2
    }

    public String ssid;
    public String bssid;
    public int security;
    int networkId;
    boolean wpsAvailable = false;
    PskType pskType = PskType.UNKNOWN;
    private WifiConfiguration mConfig;
    ScanResult mScanResult;
    private int mRssi;
    private WifiInfo mInfo;
    private DetailedState mState;

    static int getSecurity(WifiConfiguration config) 
    {
    	if (config.allowedKeyManagement.get(KeyMgmt.WPA_PSK)) 
        {
            return SECURITY_PSK;
        }
    	
        if (config.allowedKeyManagement.get(KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(KeyMgmt.IEEE8021X)) 
        {
            return SECURITY_EAP;
        }
        
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }

    private static int getSecurity(ScanResult result) 
    {
        if (result.capabilities.contains("WEP")) 
        {
            return SECURITY_WEP;
        } 
        else if (result.capabilities.contains("PSK")) 
        {
            return SECURITY_PSK;
        } 
        else if (result.capabilities.contains("EAP")) 
        {
            return SECURITY_EAP;
        }
        return SECURITY_NONE;
    }

    private static PskType getPskType(ScanResult result) 
    {
        boolean wpa = result.capabilities.contains("WPA-PSK");
        boolean wpa2 = result.capabilities.contains("WPA2-PSK");
        if (wpa2 && wpa) 
        {
            return PskType.WPA_WPA2;
        } 
        else if (wpa2) 
        {
            return PskType.WPA2;
        } 
        else if (wpa) 
        {
            return PskType.WPA;
        } 
        else 
        {
            return PskType.UNKNOWN;
        }
    }

    AccessPoint(Context context, WifiConfiguration config) 
    {
		mContext = 	context;	
        loadConfig(config);
    }

    public AccessPoint(Context context, ScanResult result) 
    {
		mContext = 	context;			
        loadResult(result);
     }

    AccessPoint(Context context, Bundle savedState) 
    {
       	mContext = 	context;	
        mConfig = savedState.getParcelable(KEY_CONFIG);
        if (mConfig != null) 
        {
            loadConfig(mConfig);
        }
        mScanResult = (ScanResult) savedState.getParcelable(KEY_SCANRESULT);
        if (mScanResult != null) 
        {
            loadResult(mScanResult);
        }
        mInfo = (WifiInfo) savedState.getParcelable(KEY_WIFIINFO);
        if (savedState.containsKey(KEY_DETAILEDSTATE)) 
        {
            mState = DetailedState.valueOf(savedState.getString(KEY_DETAILEDSTATE));
        }
       update(mInfo, mState);
    }

    public void saveWifiState(Bundle savedState) 
    {
        savedState.putParcelable(KEY_CONFIG, mConfig);
        savedState.putParcelable(KEY_SCANRESULT, mScanResult);
        savedState.putParcelable(KEY_WIFIINFO, mInfo);
        if (mState != null) 
        {
        	savedState.putString(KEY_DETAILEDSTATE, mState.toString());
        }
    }

    private void loadConfig(WifiConfiguration config) 
    {
        ssid = (config.SSID == null ? "" : removeDoubleQuotes(config.SSID));
        bssid = config.BSSID;
        security = getSecurity(config);
        networkId = config.networkId;
        mRssi = Integer.MAX_VALUE;
        mConfig = config;

        Log.i("wangxian", TAG + "loadConfig : ssid" +ssid);
        Log.i("wangxian", TAG + "loadConfig : bssid" +bssid);	
        Log.i("wangxian", TAG + "loadConfig : security" +security);
        Log.i("wangxian", TAG + "loadConfig : networkId" +networkId);	
        Log.i("wangxian", TAG + "loadConfig : mRssi" +mRssi);	

    }
    private void loadResult(ScanResult result) 
    {
        ssid = result.SSID;
        bssid = result.BSSID;
        security = getSecurity(result);
        wpsAvailable = security != SECURITY_EAP && result.capabilities.contains("WPS");
        if (security == SECURITY_PSK)
            pskType = getPskType(result);
        networkId = -1;
        mRssi = result.level;
        mScanResult = result;
    }


    public int compareTo(AccessPoint other) 
    {
        if (other ==null) 
        {
            return 1;
        }
        // Active one goes first.
        if (mInfo != other.mInfo) 
        {
            return (mInfo != null) ? -1 : 1;
        }
        // Reachable one goes before unreachable one.
        if ((mRssi ^ other.mRssi) < 0) 
        {
            return (mRssi != Integer.MAX_VALUE) ? -1 : 1;
        }
        // Configured one goes before unconfigured one.
        if ((networkId ^ other.networkId) < 0) 
        {
            return (networkId != -1) ? -1 : 1;
        }
        // Sort by signal strength.
        int difference = WifiManager.compareSignalLevel(other.mRssi, mRssi);
        if (difference != 0) 
        {
            return difference;
        }
        // Sort by ssid.
        return ssid.compareToIgnoreCase(other.ssid);
    }

    boolean update(ScanResult result) 
    {
        if (ssid.equals(result.SSID) && security == getSecurity(result)) 
        {
            if (WifiManager.compareSignalLevel(result.level, mRssi) > 0) 
            {
                int oldLevel = getLevel();
                mRssi = result.level;
                if (getLevel() != oldLevel) 
                {
                   ; // notifyChanged();
                }
            }
            // This flag only comes from scans, is not easily saved in config
            if (security == SECURITY_PSK) 
            {
                pskType = getPskType(result);
            }
            
            return true;
        }
        return false;
    }

    void update(WifiInfo info, DetailedState state) 
    {
        boolean reorder = false;
        if (info != null && networkId != WifiConfiguration.INVALID_NETWORK_ID
                && networkId == info.getNetworkId()) {
            reorder = (mInfo == null);
            mRssi = info.getRssi();
            mInfo = info;
            mState = state;
          } else if (mInfo != null) {
            reorder = true;
            mInfo = null;
            mState = null;
        }
        if (reorder) {
//            notifyHierarchyChanged();
        }
    }

    public int getLevel() 
    {
        if (mRssi == Integer.MAX_VALUE) 
        {
            return -1;
        }
        return WifiManager.calculateSignalLevel(mRssi, 5);
    }

    WifiConfiguration getConfig() 
    {
        return mConfig;
    }

    WifiInfo getInfo() 
    {
        return mInfo;
    }

    DetailedState getState() 
    {
        return mState;
    }

    static String removeDoubleQuotes(String string) 
    {
        int length = string.length();
        if ((length > 1) && (string.charAt(0) == '"')
                && (string.charAt(length - 1) == '"')) 
        {
            return string.substring(1, length - 1);
        }
        return string;
    }

    public static String convertToQuotedString(String string) 
    {
        return "\"" + string + "\"";
    }

    

    /**
     * Generate and save a default wifiConfiguration with common values.
     * Can only be called for unsecured networks.
     * @hide
     */
    protected void generateOpenNetworkConfig() 
    {
        if (security != SECURITY_NONE)
            throw new IllegalStateException();
        if (mConfig != null)
            return;
        
        mConfig = new WifiConfiguration();
        mConfig.SSID = AccessPoint.convertToQuotedString(ssid);
        mConfig.allowedKeyManagement.set(KeyMgmt.NONE);

        Log.i("wangxian", TAG + "generateOpenNetworkConfig : ssid" +mConfig.SSID);
	
    }
}

package com.tv.factoryHotkey;


import com.tv.plugin.TvPluginControler;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.ui.utils.LogicTextResource;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tv.app.R;
import com.tv.app.TvApplication;

public class FactoryHotKeyActivity extends Activity{
	public final static String OPKEY = "FactoryState";
	public final static int M_MODE = 1;
	public final static int BUSOFF = 2;
	public final static int BARCODE = 3;
    private static final String MACADDR = "141";
    private static final String BARCODEADDR = "160";
    private static final String TAG = "FactoryHotKeyActivity";
	
	FrameLayout factoryHotKeyLayout;
	TextView textAgeMode;
	TextView textBusOff;
	LinearLayout barCodeLayout;
	TextView textBarCode1;
	TextView textBarCode2;
	
	private int opKey = 0;
	
	private void initView()
	{
		factoryHotKeyLayout = new FrameLayout(this);
		FrameLayout.LayoutParams hotkeyLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		factoryHotKeyLayout.setLayoutParams(hotkeyLayoutParams);
		
		textAgeMode = new TextView(this);
		FrameLayout.LayoutParams ageLayoutParams = new FrameLayout.LayoutParams(177, LayoutParams.WRAP_CONTENT);
		ageLayoutParams.gravity = Gravity.RIGHT;
		textAgeMode.setLayoutParams(ageLayoutParams);
		textAgeMode.setTextSize(150);
		textAgeMode.setBackgroundColor(Color.argb(0, 255, 0, 0));
		textAgeMode.setTextColor(Color.argb(255, 255, 0, 0));
		textAgeMode.setText(R.string.str_age_mode);
		factoryHotKeyLayout.addView(textAgeMode);
		
		textBusOff = new TextView(this);
		FrameLayout.LayoutParams busOffLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		busOffLayoutParams.gravity = Gravity.CENTER;
		textBusOff.setLayoutParams(busOffLayoutParams);
		textBusOff.setTextSize(250);
		textBusOff.setTextColor(Color.argb(255, 0, 255, 0));
		textBusOff.setText(R.string.str_bus_off);
		textBusOff.setVisibility(View.INVISIBLE);
		factoryHotKeyLayout.addView(textBusOff);
		
		barCodeLayout = new LinearLayout(this);
		LinearLayout.LayoutParams barCodeLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		barCodeLayoutParams.gravity = Gravity.CENTER;
		barCodeLayout.setLayoutParams(barCodeLayoutParams);
		barCodeLayout.setOrientation(LinearLayout.VERTICAL);
		barCodeLayout.setGravity(Gravity.CENTER);
		barCodeLayout.setVisibility(View.INVISIBLE);
		barCodeLayout.setBackgroundColor(Color.argb(255, 0, 0, 0));
		factoryHotKeyLayout.addView(barCodeLayout);
		
		textBarCode1 = new TextView(this);
		LinearLayout.LayoutParams textBarCode1Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textBarCode1Params.gravity = Gravity.LEFT;
		textBarCode1.setLayoutParams(textBarCode1Params);
		textBarCode1.setTextColor(Color.argb(255, 0, 0, 255));
		textBarCode1.setTextSize(50);
		
		textBarCode2 = new TextView(this);
		LinearLayout.LayoutParams textBarCode2Params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textBarCode2Params.gravity = Gravity.LEFT;
		textBarCode2.setLayoutParams(textBarCode2Params);
		textBarCode2.setTextColor(Color.argb(255, 0, 0, 255));
		textBarCode2.setTextSize(50);
		
		barCodeLayout.addView(textBarCode1);
		barCodeLayout.addView(textBarCode2);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		setContentView(factoryHotKeyLayout);
		
		Intent it = this.getIntent();
		opKey = it.getIntExtra(OPKEY, 0);
		
		HandleOpKey();
		
		((TvApplication)getApplication()).addActivity(this);
	}

	private void HandleOpKey()
	{
		switch(opKey){
		case M_MODE:
			Log.d(TAG, "aging mode HandleOpKey...");
			matureMode();
			break;
		case BUSOFF:
			Log.d(TAG, "bus off HandleOpKey...");
			busOffHandle();
			break;
		case BARCODE:
			Log.d(TAG, "bar code HandleOpKey...");
			barCodeShow();
			break;
		default:
			finish();
			break;
		}
	}
	
	private void matureMode()
	{
		TvPluginControler.getInstance().getCommonManager().enableHotkey(false);
		TvPluginControler.getInstance().getCommonManager().enableAgingMode();
	}
	
	private void busOffHandle()
	{
		TvPluginControler.getInstance().getCommonManager().enableHotkey(false);
		TvPluginControler.getInstance().getCommonManager().enableBusOff();
		textAgeMode.setVisibility(View.INVISIBLE);
		textBusOff.setVisibility(View.VISIBLE);
	}
	
	private String getMACAddr()
	{
        short[] macaddr;
        macaddr = TvPluginControler.getInstance().getCommonManager().readFromEeprom(Short.parseShort(MACADDR,16),6);
        return ( 
                Integer.toHexString(macaddr[0])
          + ":"+Integer.toHexString(macaddr[1])
          + ":"+Integer.toHexString(macaddr[2])
          + ":"+Integer.toHexString(macaddr[3])
          + ":"+Integer.toHexString(macaddr[4])
          + ":"+Integer.toHexString(macaddr[5]));
	}
	
	private String getBarCode()
	{
        short[] EEPBarcode;
        EEPBarcode = TvPluginControler.getInstance().getCommonManager().readFromEeprom(Short.parseShort(BARCODEADDR,16),32);
        String SBarcode = "";
        for(int i = 0;i<EEPBarcode.length;i++)
        {
            SBarcode+= (char)EEPBarcode[i];
            Log.d("wangxian", "SBarcode["+i+"]"+SBarcode);
        }
        return SBarcode;
	}
	
	private void barCodeShow()
	{
		TvPluginControler.getInstance().getCommonManager().enableHotkey(false);
		textBarCode1.setText(LogicTextResource.getInstance(TvContext.context).getText(R.string.str_mac_addr) + getMACAddr());
		textBarCode2.setText(LogicTextResource.getInstance(TvContext.context).getText(R.string.str_bar_code) + getBarCode());
		barCodeLayout.setVisibility(View.VISIBLE);
		textAgeMode.setVisibility(View.INVISIBLE);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((TvApplication)getApplication()).removeActivity(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG,"FactoryHotKeyActivity.onKeyDown >> keyCode : " + keyCode);
		if(opKey == BARCODE)
		{
			if( keyCode == 255 || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode== KeyEvent.KEYCODE_VOLUME_DOWN )
			{
				return false;
			}
			TvPluginControler.getInstance().getCommonManager().enableHotkey(true);
			finish();
			return true;
		}
			
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_BACK:
			if(opKey != M_MODE)
				break;
			else
			{
				if(TvPluginControler.getInstance().getCommonManager().isAgingMode())
					return true;
			}
		case KeyEvent.KEYCODE_FACTORY_AGING_MODE:
		case KeyEvent.KEYCODE_MEDIA_STOP:
			if(opKey != M_MODE)
				break;
			TvPluginControler.getInstance().getCommonManager().disableAgeMode();
			TvPluginControler.getInstance().getCommonManager().enableHotkey(true);
			finish();
			break;
		case KeyEvent.KEYCODE_FACTORY_BUS_OFF:
			if(opKey != BUSOFF)
				break;
			TvPluginControler.getInstance().getCommonManager().disableBusOff();
			TvPluginControler.getInstance().getCommonManager().setEnvironment("ethaddr", getMACAddr());
			TvPluginControler.getInstance().getCommonManager().enableHotkey(true);
			finish();
			break;
		default:
			break;
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}

package com.tv.ui.LNBSetting;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvSearchTypes;
import com.tv.framework.define.TvSearchTypes.SATELLITE_OPERATION_TYPE;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SatelliteItemView extends RelativeLayout implements OnFocusChangeListener, OnTouchListener, OnKeyListener
{
	private String tag = "SatelliteItemView";
    private static float div = TvUIControler.getInstance().getResolutionDiv();
    private static float dip = TvUIControler.getInstance().getDipDiv();
    private ImageView checkedView;
    private TextView numView,nameView,bandView;
    private Context mContext;
    private boolean isItemCheck = false;
    private boolean hasFocus = false;
    private final static int ID_CHECKVIEW= 100;
    private final static int ID_NUMTEXT= 101;
    private final static int ID_NAMETEXT= 102;
    private LNBSettingView parentView;
    private LNBSettingDialog parenDialog;

    public SatelliteItemView(Context context, LNBSettingView parentView ,LNBSettingDialog parenDialog)
    {
        super(context);
        mContext = context;
        this.setOnKeyListener(this);
        this.setOnFocusChangeListener(this);
        this.parentView = parentView;
        this.parenDialog = parenDialog;
        initViews();
    }

    private void initViews()
    {
        this.setGravity(Gravity.CENTER_VERTICAL);
        
        RelativeLayout.LayoutParams checkedViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        checkedViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        checkedViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        checkedViewParams.leftMargin = (int)(30/div);
        
        checkedView = new ImageView(mContext);
        checkedView.setId(ID_CHECKVIEW);
        this.addView(checkedView, checkedViewParams);
        
        RelativeLayout.LayoutParams numViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        numViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        numViewParams.addRule(RelativeLayout.RIGHT_OF, ID_CHECKVIEW);
        numViewParams.leftMargin = (int)(40/div);
        
        RelativeLayout.LayoutParams nameViewParams = new RelativeLayout.LayoutParams(
        		(int)(470/div), LayoutParams.WRAP_CONTENT);
        nameViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        nameViewParams.addRule(RelativeLayout.RIGHT_OF, ID_NUMTEXT);
        nameViewParams.leftMargin = (int)(40/div);
        
        RelativeLayout.LayoutParams bandViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        bandViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        bandViewParams.addRule(RelativeLayout.RIGHT_OF, ID_NAMETEXT);
        
        numView = new TextView(mContext);
        numView.setTextColor(Color.WHITE);
        numView.setTextSize(36 / dip);
        numView.setId(ID_NUMTEXT);
        this.addView(numView, numViewParams);
        
        nameView = new TextView(mContext);
        nameView.setTextColor(Color.WHITE);
        nameView.setTextSize(36 / dip);
        nameView.setId(ID_NAMETEXT);
        nameView.getPaint().setAntiAlias(true);
        nameView.setSingleLine(true);
        nameView.setEllipsize(TruncateAt.MARQUEE);
        nameView.setMarqueeRepeatLimit(-1);
        this.addView(nameView, nameViewParams);
        
        bandView = new TextView(mContext);
        bandView.setTextColor(Color.WHITE);
        bandView.setTextSize(36 / dip);
        this.addView(bandView, bandViewParams);
        this.setFocusable(true);
        this.setClickable(true);
        if (isItemCheck)
        {
            checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
        } else
        {
            checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
        }
    }

    public void update(String textStr,String textStr2,String textStr3)
    {
        numView.setText(textStr);
        nameView.setText(textStr2);
        bandView.setText(textStr3);
    }

    public String getNameString()
    {
        return nameView.getText().toString();
    }
    
    public String getNumberString()
    {
        return numView.getText().toString();
    }
    
    public void setChecked(boolean isChecked)
    {
        isItemCheck = isChecked;
        upDateRadioImage();
    }

    public void setFocused(boolean isFoucused)
    {
        hasFocus = isFoucused;
    }

    public boolean getFocused()
    {
        return hasFocus;
    }

    public boolean getchecked()
    {
        return isItemCheck;
    }

    private void upDateRadioImage()
    {
        if (isItemCheck)
        {
            checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
        } 
        else
        {
            if (hasFocus)
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_focus_icon);
                nameView.setTextColor(Color.BLACK);
                numView.setTextColor(Color.BLACK);
                bandView.setTextColor(Color.BLACK);
            } else
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
                nameView.setTextColor(Color.WHITE);
                numView.setTextColor(Color.WHITE);
                bandView.setTextColor(Color.WHITE);
            }
        }
    }

    public void setBackGroundColorUser(boolean ishasFocus)
    {
        if (ishasFocus)
        {
        	this.setBackgroundColor(Color.rgb(53,152,220));

        	nameView.setTextColor(Color.BLACK);
            numView.setTextColor(Color.BLACK);
            bandView.setTextColor(Color.BLACK);
        	
            if (isItemCheck)
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
            } else
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_focus_icon);
            }
            hasFocus = true;
        } else
        {
        	nameView.setTextColor(Color.WHITE);
            numView.setTextColor(Color.WHITE);
            bandView.setTextColor(Color.WHITE);
            if (!isItemCheck)
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
            }
            this.setBackgroundDrawable(null);
            hasFocus = false;
        }
    }
    
    @Override
	public boolean onKey(View view, int keyCode, KeyEvent event) 
    {
		// TODO Auto-generated method stub
    	if(keyCode == KeyEvent.KEYCODE_PROG_YELLOW && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			 // for delete
			if(parentView.getSatelliteItemNum() > 1)
			{
				final TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
				Log.d("wangxian", "toast 1");
				tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() {				
					@Override
					public void onClickListener(boolean flag) {
						// TODO Auto-generated method stub
						Log.d("wangxian", "on click");		
						if(flag == true)
						{
							TvPluginControler.getInstance().getChannelManager().deleteSatellite(parentView.getCurrentFocusedItem());
							parentView.updateView();
			
						}	
						((TvToastFocusDialog) tvToastFocusDialog).processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);
					}
				});
				tvToastFocusDialog.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW, 
						new TvToastFocusData("", "", this.getResources().getString(R.string.sky_pvr_record_list_delete_tip),2));		     
			}
			return true;
    		
		}
		else if(keyCode == KeyEvent.KEYCODE_PROG_RED)
		{	
			TvSearchTypes.SATELLITE_SETTING_OPERATION_TYPE = SATELLITE_OPERATION_TYPE.SATELLITE_OPERATION_ADD;
			parenDialog.dismissUI();
			SatelliteOperationDialog satelliteSignalEditDialog = new SatelliteOperationDialog();
			satelliteSignalEditDialog.setDialogListener(null);
			satelliteSignalEditDialog.processCmd(TvConfigTypes.TV_DIALOG_SATELLITE_EDIT, DialogCmd.DIALOG_SHOW, parentView.getCurrentFocusedItem());
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_PROG_GREEN)
		{
			TvSearchTypes.SATELLITE_SETTING_OPERATION_TYPE = SATELLITE_OPERATION_TYPE.SATELLITE_OPERATION_EDIT;
			parenDialog.dismissUI();
			SatelliteOperationDialog satelliteSignalEditDialog = new SatelliteOperationDialog();
			satelliteSignalEditDialog.setDialogListener(null);
			satelliteSignalEditDialog.processCmd(TvConfigTypes.TV_DIALOG_SATELLITE_EDIT, DialogCmd.DIALOG_SHOW, parentView.getCurrentFocusedItem());
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_MEDIA_STOP)
		{
			Log.i("wangxian", "keycode 0");
			parentView.getSignalInfo();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_BACK 
		 || keyCode == KeyEvent.KEYCODE_MENU
		 || keyCode == KeyEvent.KEYCODE_PROG_BLUE
	     || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
	     || keyCode == KeyEvent.KEYCODE_DPAD_LEFT ) 
		{
			parentView.onKeyDown(keyCode, event);
			return true;
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP
			  || keyCode == KeyEvent.KEYCODE_DPAD_DOWN
			  &&event.getAction()==KeyEvent.ACTION_DOWN)
		{
			parentView.onKeyDown(keyCode, event);
			return true;
		}
		
		
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
}

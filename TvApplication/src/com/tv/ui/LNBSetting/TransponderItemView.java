package com.tv.ui.LNBSetting;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.data.TvTransponderListData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvSearchTypes;
import com.tv.framework.define.TvSearchTypes.TRANSPONDER_OPERATION_TYPE;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
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

public class TransponderItemView extends RelativeLayout implements OnFocusChangeListener, OnTouchListener, OnKeyListener
{
	private static float div = TvUIControler.getInstance().getResolutionDiv();
    private static float dip = TvUIControler.getInstance().getDipDiv();
    private ImageView checkedView;
    private TextView numView,frequencyView,symbolRateView,polarityView;
    private Context mContext;
    private boolean isItemCheck = false;
    private boolean hasFocus = false;
    private int viewId = -1;
    private final static int ID_CHECKVIEW= 100;
    private final static int ID_NUMTEXT= 101;
    private final static int ID_FREQUENCYTEXT= 102;
    private final static int ID_SYMBOLREATETEXT= 103;
    
    private LNBSettingView parentView;
    private LNBSettingDialog parenDialog;

	public TransponderItemView(Context context, LNBSettingView parentView ,LNBSettingDialog parenDialog) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
        this.setOnKeyListener(this);
        this.setOnFocusChangeListener(this);
        this.parentView = parentView;
        this.parenDialog = parenDialog;
        initViews();
	}
	
	public void initViews(){
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
        
        RelativeLayout.LayoutParams frequencyViewParams = new RelativeLayout.LayoutParams(
        		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        frequencyViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        frequencyViewParams.addRule(RelativeLayout.RIGHT_OF, ID_NUMTEXT);
        frequencyViewParams.leftMargin = (int)(40/div);
        
        RelativeLayout.LayoutParams symbolRateViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        symbolRateViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        symbolRateViewParams.addRule(RelativeLayout.RIGHT_OF, ID_FREQUENCYTEXT);
        symbolRateViewParams.leftMargin = (int)(40/div);
        
        RelativeLayout.LayoutParams bandViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        bandViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        bandViewParams.addRule(RelativeLayout.RIGHT_OF, ID_SYMBOLREATETEXT);
        bandViewParams.leftMargin = (int)(40/div);
        
        numView = new TextView(mContext);
        numView.setTextColor(Color.WHITE);
        numView.setTextSize(36 / dip);
        numView.setId(ID_NUMTEXT);
        this.addView(numView, numViewParams);
        
        frequencyView = new TextView(mContext);
        frequencyView.setTextColor(Color.WHITE);
        frequencyView.setTextSize(36 / dip);
        frequencyView.setId(ID_FREQUENCYTEXT);
        frequencyView.getPaint().setAntiAlias(true);
        frequencyView.setSingleLine(true);
        frequencyView.setEllipsize(TruncateAt.MARQUEE);
        frequencyView.setMarqueeRepeatLimit(-1);
        this.addView(frequencyView, frequencyViewParams);
        
        symbolRateView = new TextView(mContext);
        symbolRateView.setTextColor(Color.WHITE);
        symbolRateView.setTextSize(36 / dip);
        symbolRateView.setId(ID_SYMBOLREATETEXT);
        symbolRateView.getPaint().setAntiAlias(true);
        symbolRateView.setSingleLine(true);
        symbolRateView.setEllipsize(TruncateAt.MARQUEE);
        symbolRateView.setMarqueeRepeatLimit(-1);        
        this.addView(symbolRateView, symbolRateViewParams);
        
        polarityView = new TextView(mContext);
        polarityView.setTextColor(Color.WHITE);
        polarityView.setTextSize(36 /dip);
        this.addView(polarityView,bandViewParams);
        
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
	
	public void updateView(TvTransponderListData data){
		
		numView.setText(data.getNumber());
		frequencyView.setText(data.getFrequency());
		symbolRateView.setText(data.getSymbolRate());
		polarityView.setText(data.getPolarType());
	}
	
	private void updateRadioImage(){
        if (isItemCheck)
        {
            checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
        } 
        else
        {
            if (hasFocus)
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_focus_icon);
            } else
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
            }
        }
	}
	
	public void setBkGroundColor(boolean hasFocus){
		if (hasFocus)
        {
        	this.setBackgroundColor(Color.rgb(53,152,220));
        	numView.setTextColor(Color.BLACK);
    		frequencyView.setTextColor(Color.BLACK);
    		symbolRateView.setTextColor(Color.BLACK);
    		polarityView.setTextColor(Color.BLACK);
        } else
        {
        	this.setBackgroundDrawable(null);
        	numView.setTextColor(Color.WHITE);
    		frequencyView.setTextColor(Color.WHITE);
    		symbolRateView.setTextColor(Color.WHITE);
    		polarityView.setTextColor(Color.WHITE);
        }
	}
	
	public void setViewId(int viewId){
		this.viewId = viewId;
	}
	
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return getViewId();
	}
	
	public int getViewId(){
		return viewId;
	}
	
	public void setChecked(boolean 	isChecked){
		isItemCheck = isChecked;
		updateRadioImage();
	}
	
	public boolean getChecked(){
		return isItemCheck;
	}
	
	public void setFocused(boolean isFocused){
		hasFocus = isFocused;
	}
	
	public boolean getFocused(){
		return hasFocus;
	}

	public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_PROG_YELLOW && arg2.getAction()==KeyEvent.ACTION_DOWN)
		{
			 // for delete
			TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
			tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() {				
				@Override
				public void onClickListener(boolean flag) {
					// TODO Auto-generated method stub
					if(flag == true)
					{
						TvPluginControler.getInstance().getChannelManager().deleteTransponder(parentView.getCurrentTPFocusedItem());
						parentView.updateTransponderList();
					}					
				}
			});
			tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
					new TvToastFocusData("", "", this.getResources().getString(R.string.sky_pvr_record_list_delete_tip),2));		     
		    return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_PROG_RED && arg2.getAction()==KeyEvent.ACTION_DOWN)
		{	
			TvSearchTypes.TRANSPONDERE_SETTING_OPERATION_TYPE = TRANSPONDER_OPERATION_TYPE.TRANSPONDER_OPERATION_ADD;
			parenDialog.dismissUI();
			TransponderOperationDialog TransponderAddDialog = new TransponderOperationDialog();
			TransponderAddDialog.setDialogListener(null);
			TransponderAddDialog.processCmd(TvConfigTypes.TV_DIALOG_SATELLITE_EDIT, DialogCmd.DIALOG_SHOW, parentView.getCurrentTPFocusedItem());
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_PROG_GREEN && arg2.getAction()==KeyEvent.ACTION_DOWN)
		{
			TvSearchTypes.TRANSPONDERE_SETTING_OPERATION_TYPE = TRANSPONDER_OPERATION_TYPE.TRANSPONDER_OPERATION_EDIT;
			parenDialog.dismissUI();
			TransponderOperationDialog TransponderEditDialog = new TransponderOperationDialog();
			TransponderEditDialog.setDialogListener(null);
			TransponderEditDialog.processCmd(TvConfigTypes.TV_DIALOG_SATELLITE_EDIT, DialogCmd.DIALOG_SHOW, parentView.getCurrentTPFocusedItem());
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU
				|| keyCode == KeyEvent.KEYCODE_PROG_BLUE || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) 
		{
			parentView.onKeyDown(keyCode, arg2);
			return true;
		}
		else if ((keyCode == KeyEvent.KEYCODE_BACK 
		  || keyCode == KeyEvent.KEYCODE_MENU
		  || keyCode == KeyEvent.KEYCODE_PROG_BLUE
	      || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
	      || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
	      || keyCode == KeyEvent.KEYCODE_DPAD_UP
	 	  || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
	 	  && arg2.getAction()==KeyEvent.ACTION_DOWN)		
		  {
			parentView.onKeyDown(keyCode, arg2);
			return true;
		  }
		return false;
	}

	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	
	 public void setItemTransponderFocusChange(){
		 this.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			public void onFocusChange(View view, boolean hasFocus) {
				// TODO Auto-generated method stub
				((TransponderItemView) view).setBkGroundColor(hasFocus);
			}
		});
	 }
	 
	public int getTPNumber()
	{
		return Integer.parseInt(numView.getText().toString());
	}
}
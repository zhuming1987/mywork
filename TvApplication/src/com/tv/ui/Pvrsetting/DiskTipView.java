package com.tv.ui.Pvrsetting;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvUIControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.framework.define.TvConfigTypes;
import com.tv.ui.SettingView.TvSettingItemView.OnSettingKeyDownListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;

public class DiskTipView extends LinearLayout implements OnSettingKeyDownListener, DialogListener
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip = TvUIControler.getInstance().getDipDiv();
	private QuickKeyListener quickKeyListener;
	private DiskSelectDialog parentDialog = null;
	private LinearLayout mainLayout;
    private TextView tipText;
    private Button okButton;
 
    public DiskTipView(Context context)
    {
        super(context);
        mContext = context;
        initViews();
        this.setBackgroundColor(Color.rgb(30, 31, 32));
    }

    private void initViews()
    {
    	this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();	
		this.setGravity(Gravity.CENTER);
		setOrientation(LinearLayout.VERTICAL);
        setFocusable(false);
		
		mainLayout = new LinearLayout(mContext);
        mainLayout.setOrientation(VERTICAL);
        mainLayout.setGravity(Gravity.CENTER);
        this.addView(mainLayout, new LayoutParams((int) (640 / div), (int) (360 / div)));
		
		tipText=new TextView(mContext);     
		tipText.setTextSize((int) (60 / dip));
		tipText.setTextColor(Color.rgb(255, 255, 255));
		tipText.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams tipParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        tipParams.topMargin = (int)(80 / div);
        mainLayout.addView(tipText,tipParams);
		
        okButton = new Button(mContext);     
        okButton.setTextSize((int) (40 / dip));
        okButton.setBackgroundColor(Color.BLUE);
        okButton.setTextColor(Color.rgb(255, 255, 255));
        okButton.setGravity(Gravity.CENTER_HORIZONTAL);
        okButton.setFocusable(true);
        okButton.setFocusableInTouchMode(true);
        okButton.requestFocus();
        okButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				parentDialog.processCmd(TvConfigTypes.TV_DIALOG_DISK_SELECT,DialogCmd.DIALOG_DISMISS, null);
			}
		});    
        LinearLayout.LayoutParams okParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        okParams.topMargin = (int)(80 / div);
        mainLayout.addView(okButton,okParams);
    }

    public void update()
    {
    	tipText.setText(this.getResources().getString(R.string.str_tip_plug_usb));
    	okButton.setText("I known");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)
        {
        	Log.d("yc", "setting item  keyback");
        	parentDialog.processCmd(TvConfigTypes.TV_DIALOG_DISK_SELECT,DialogCmd.DIALOG_DISMISS, null);
			return true;
        }
        return quickKeyListener.onQuickKeyDownListener(keyCode, event);
    }

    public void setParentDialog(DiskSelectDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	@Override
	public boolean onShowDialogDone(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int onDismissDialogDone(Object...obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void OnKeyDownListener(int keyCode) {
		// TODO Auto-generated method stub
    
	}

	@Override
	public void setFocus(int index, int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnPage(int keycode, int index) {
		// TODO Auto-generated method stub
		
	}

}

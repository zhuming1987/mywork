package com.tv.ui.SettingView;

import com.tv.app.R;
import com.tv.data.MenuItem;
import com.tv.framework.data.TvStandardTime;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class TvNextItemView extends TvSettingItemView{

	private TextView contentText;

    public final int textHeight = 60;
    public final int FONT_SIZE = 30;
    public static final int ENTER = 1001;
    
	public TvNextItemView(Context context, int index, TvSettingView parentView, OnSettingKeyDownListener keyListener) {
		super(context, index, parentView, keyListener);
		// TODO Auto-generated constructor stub
		contentText = new TextView(context);
        contentText.getPaint().setAntiAlias(true);
        contentText.setTextColor(Color.WHITE);

        contentText.setBackgroundResource(R.drawable.setting_unselect_bg);
        contentText.setTextSize(FONT_SIZE/dipDiv);
        contentText.setGravity(Gravity.CENTER);
        viewLayout.addView(contentText, new LayoutParams(LayoutParams.MATCH_PARENT, (int)(textHeight/resolution)));
	}

	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void updateView(MenuItem data) {
		// TODO Auto-generated method stub
		super.updateView(data);
		if(data.getId().equals(TvConfigDefs.TV_CFG_CURRENT_TIME))
		{
			TvStandardTime time = TvPluginControler.getInstance().getCommonManager().getCurTimer();
			contentText.setText(String.format("%4d/%02d/%02d %2d:%02d", time.year, time.month+1, time.monthDay,
					time.hour, time.minute));
		}
		else 
		{
			contentText.setText(TvContext.context.getResources().getString(R.string.str_enter));
		}
		if (isEnabled) {
			contentText.setTextColor(Color.WHITE);
			this.setFocusable(true);
		} else {
			contentText.setTextColor(Color.GRAY);
			this.setFocusable(false);
		}
		bodyLayout.setFocusable(isEnabled);
		refreshUI();
	}
	
	public void updateView(String data){
		if(data != null)
			contentText.setText(data);
		refreshUI();
	}

	@Override
	public void updateBgDrawable(boolean parentHasFocus) {
		// TODO Auto-generated method stub
		Log.d("gky", "nextlevel updateBgDrawable parentHasFocus=" + parentHasFocus);
		if (parentHasFocus) {
			contentText.setBackgroundResource(R.drawable.yellow_sel_bg);
			contentText.setTextColor(Color.BLACK);
		} else {
			contentText.setBackgroundResource(R.drawable.setting_unselect_bg);
			contentText.setTextColor(Color.WHITE);
		}
	}

	@Override
	public boolean executeKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_UP)
			return false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			OnExecuteSet(ENTER);
			return true;
		}
		return true;
	}

	@Override
	public void refreshUI() {
		// TODO Auto-generated method stub
		
	}

}

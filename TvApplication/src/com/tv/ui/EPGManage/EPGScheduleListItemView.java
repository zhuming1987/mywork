package com.tv.ui.EPGManage;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvEpgSchedulesData;
import com.tv.framework.data.TvEpgSchedulesData.EPD_MODE;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EPGScheduleListItemView  extends LinearLayout implements OnFocusChangeListener, OnKeyListener{

	private Context mContext;
	private LinearLayout schedulechanneLayout;
	private TextView channelnumberTextView,channelnameTextView;
	private TextView programenameTextView,modeTextView,daTextView,timeTextView;
	private int index = -1;
	private OnItemKeyLister onItemKeyListener = null;
	
	public static float div = TvUIControler.getInstance().getResolutionDiv();
	public static float dipDiv = TvUIControler.getInstance().getDipDiv();
	
	public EPGScheduleListItemView(Context context, int index, OnItemKeyLister onItemKeyListener) {
		super(context);
        this.setOnKeyListener(this);
        mContext = context;
        this.index = index;
        this.onItemKeyListener = onItemKeyListener;
        this.setBackgroundColor(Color.rgb(25, 26, 28));
        this.setOnFocusChangeListener(this);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        
        schedulechanneLayout = new LinearLayout(context);
        schedulechanneLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        schedulechanneLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.addView(schedulechanneLayout, new LayoutParams((int) (300 / div), (int) (80 / div)));
        
        channelnumberTextView = new TextView(mContext);
        channelnumberTextView.setTextSize((int) (32 / dipDiv));
        channelnumberTextView.setTextColor(Color.WHITE);
        channelnumberTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        channelnumberTextView.setPadding((int) (20 / div), 0, 0, 0);
        schedulechanneLayout.addView(channelnumberTextView, new LayoutParams((int) (130 / div), (int) (80 / div)));
        
        channelnameTextView = new TextView(mContext);
        channelnameTextView.setTextSize((int) (32 / dipDiv));
        channelnameTextView.setTextColor(Color.WHITE);
        channelnameTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        channelnameTextView.getPaint().setAntiAlias(true);
        channelnameTextView.setSingleLine(true);
        channelnameTextView.setEllipsize(TruncateAt.MARQUEE);
        channelnameTextView.setMarqueeRepeatLimit(-1);
        channelnameTextView.setSelected(true);
        schedulechanneLayout.addView(channelnameTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, (int) (80 / div)));
        
        programenameTextView = new TextView(mContext);
        programenameTextView.setTextSize((int) (32 / dipDiv));
        programenameTextView.setTextColor(Color.WHITE);
        programenameTextView.setGravity(Gravity.CENTER);
        programenameTextView.getPaint().setAntiAlias(true);
        programenameTextView.setSingleLine(true);
        programenameTextView.setEllipsize(TruncateAt.MARQUEE);
        programenameTextView.setMarqueeRepeatLimit(-1);
        programenameTextView.setSelected(true);
        programenameTextView.setPadding((int) (20 / div), 0, 0, 0);
        this.addView(programenameTextView, new LayoutParams((int) (300 / div), (int) (80/ div)));
        
        modeTextView = new TextView(mContext);
        modeTextView.setTextSize((int) (32 / dipDiv));
        modeTextView.setTextColor(Color.WHITE);
        modeTextView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        this.addView(modeTextView, new LayoutParams((int) (200 / div), (int) (80 / div)));
        
        daTextView = new TextView(mContext);
        daTextView.setTextSize((int) (32 / dipDiv));
        daTextView.setTextColor(Color.WHITE);
        daTextView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        this.addView(daTextView, new LayoutParams((int) (200 / div), (int) (80 / div)));
        
        timeTextView = new TextView(mContext);
        timeTextView.setTextSize((int) (32 / dipDiv));
        timeTextView.setTextColor(Color.WHITE);
        timeTextView.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
        this.addView(timeTextView, new LayoutParams((int) (200 / div), (int) (80/ div)));
	}
	
	public void InitScheduleData(String channelnumber,String channelname,String programename,
			TvEpgSchedulesData.EPD_MODE mode,String date,String time)
	{
		channelnumberTextView.setText("CH " + channelnumber);
		channelnameTextView.setText(channelname);
		programenameTextView.setText(programename);
		daTextView.setText(date);
		timeTextView.setText(time);
		if(mode == EPD_MODE.EPG_MODE_REMIND)
		{
			modeTextView.setText(mContext.getString(R.string.epg_mode_reminder));
		}
		else if (mode == EPD_MODE.EPG_MODE_RECORD)
		{
			modeTextView.setText(mContext.getString(R.string.epg_mode_recorder));
		}
		else
		{
			modeTextView.setText(null);
		}
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_UP){
			return false;
		}
		Log.i("gky", getClass().getName() + "-->onKey keycode is "+keyCode);
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_PROG_RED:
			case KeyEvent.KEYCODE_PROG_YELLOW:
				if(onItemKeyListener != null)
					onItemKeyListener.onItemKeyDownLister(keyCode, index);
				return true;
			default:
				break;
		}
		return false;
	}
	
	public void resetFocus()
    {
        if (this != null)
        {
            this.setFocusable(true);
            this.requestFocus();
            this.getOnFocusChangeListener().onFocusChange(this, true);
        }
    }

	@Override
	public void onFocusChange(View V, boolean isFocus) {
		// TODO Auto-generated method stub
		if(isFocus)
		{
			this.setBackgroundColor(Color.rgb(30, 70, 100));
		}
		else
		{
			this.setBackgroundColor(Color.rgb(25, 26, 28));
		}
	}
	
	/**
	 * 按键回调接口
	 * @author gky
	 *
	 */
	public interface OnItemKeyLister{
		public void onItemKeyDownLister(int keyCode, int index);
	}
}

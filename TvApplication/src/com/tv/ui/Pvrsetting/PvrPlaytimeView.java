package com.tv.ui.Pvrsetting;

import com.tv.app.R;
import com.tv.app.TvUIControler;
//import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvContext;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;

public class PvrPlaytimeView extends LinearLayout implements OnClickListener
{
	private LinearLayout mainlayout;
    private Button sureButton, cancelButton;
    private final float resolution = TvUIControler.getInstance().getResolutionDiv();
    private float disDiv = TvUIControler.getInstance().getDipDiv();
    private int buttonWith = (int) (241 / resolution);
    private int buttonHeigth = (int) (60 / resolution);
    private int TimeChooserWith = (int) (150 / resolution);
    private int TimeChooserHeigth = (int) (60 / resolution);
    private  int width = (int) (1170/resolution);
    private  int height = (int) (600/resolution);
    public static final int SUREBUTTONID = 1;
    public static final int CANCELBUTTONID = 2;
    public int TIMELAYOUT_ID = 1122;
 
    private OnDlgClickListener onBtClickListener = null;
    private PvrPlaytimeDialog parentDialog = null;
    
    final private int hour = 0;
    final private int minute = 1;
    final private int second = 2;
    
    private TimeChooser hourText;
    private TimeChooser minuteText;
    private TimeChooser secondText;
    
	public PvrPlaytimeView(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.setBackgroundResource(R.drawable.dialog_bg);
		this.setLayoutParams(new LayoutParams(width, height));
		this.setFocusable(true);
        this.requestFocus();
        this.setFocusableInTouchMode(true);
        this.setGravity(Gravity.CENTER);
        
        mainlayout = new LinearLayout(context);
        mainlayout.setOrientation(LinearLayout.VERTICAL);
        mainlayout.setGravity(Gravity.CENTER);
        
        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        
        LinearLayout.LayoutParams titleTextParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        TextView titleText = new TextView(context);
        titleText.setText(TvContext.context.getResources().getString(R.string.str_player_time));
        titleText.setTextSize(50 / disDiv);
        titleText.setGravity(Gravity.CENTER_HORIZONTAL);
        titleTextParams.topMargin = (int) (20 / disDiv);
        mainlayout.addView(titleText, titleTextParams);
         
        LinearLayout.LayoutParams timelayoutParams = new LinearLayout.LayoutParams(
        		TimeChooserWith, TimeChooserHeigth);
        
        LinearLayout timelayout = new LinearLayout(context);
        timelayout.setGravity(Gravity.CENTER);
        timelayout.setId(TIMELAYOUT_ID);
        hourText = new TimeChooser(context, hour);
		hourText.setInputType(InputType.TYPE_CLASS_NUMBER);
		hourText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		hourText.setHint(TvContext.context.getResources().getString(R.string.str_time_hour));
		hourText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)}); 
		timelayout.addView(hourText, timelayoutParams);
		
		minuteText = new TimeChooser(context, minute);
		minuteText.setInputType(InputType.TYPE_CLASS_NUMBER);
		minuteText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		minuteText.setHint(TvContext.context.getResources().getString(R.string.str_time_minute));
		minuteText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)}); 
		timelayout.addView(minuteText, timelayoutParams);
		
		secondText = new TimeChooser(context, second);
		secondText.setInputType(InputType.TYPE_CLASS_NUMBER);
		secondText.setImeOptions(EditorInfo.IME_ACTION_DONE);
		secondText.setHint(TvContext.context.getResources().getString(R.string.str_time_second));
		secondText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)}); 
		timelayout.addView(secondText, timelayoutParams);
		mainlayout.addView(timelayout);
        
        this.addView(mainlayout, mainLayoutParams);

        LinearLayout buttomLayout = new LinearLayout(context);
        buttomLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams sureParams = new LinearLayout.LayoutParams(buttonWith,
                buttonHeigth);
        sureParams.rightMargin = (int) (60 / resolution);
        sureButton = new Button(context);
        sureButton.setTextColor(Color.WHITE);
        sureButton.setBackgroundResource(R.drawable.dialog_button);
        sureButton.setId(SUREBUTTONID);
        sureButton.setTextSize(35 / disDiv);
        sureButton.setOnClickListener(this);
        sureButton.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                // TODO Auto-generated method stub
                if (KeyEvent.ACTION_UP == event.getAction())
                {
                    return false;
                }

                return false;
            }
        });
        buttomLayout.addView(sureButton, sureParams);

        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(buttonWith,
                buttonHeigth);
        cancelButton = new Button(context);
        cancelButton.setTextColor(Color.WHITE);
        cancelButton.setBackgroundResource(R.drawable.dialog_button);
        cancelButton.setId(CANCELBUTTONID);
        cancelButton.setTextSize(35 / disDiv);
        cancelButton.setOnClickListener(this);
        cancelButton.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                // TODO Auto-generated method stub
                if (KeyEvent.ACTION_UP == event.getAction())
                {
                    return false;
                }

                return false;
            }
        });
        buttomLayout.addView(cancelButton, cancelParams);

        LinearLayout.LayoutParams buttomParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        buttomParams.topMargin = (int) (40 / resolution);
        mainlayout.addView(buttomLayout, buttomParams);
        
        cancelButton.setFocusable(true);
        cancelButton.setFocusableInTouchMode(true);
        sureButton.setFocusable(true);
        sureButton.setFocusableInTouchMode(true);
        sureButton.requestFocus();
	}

	public void updateView()
	{
		sureButton.setText(TvContext.context.getResources().getString(R.string.button_ok_str));
		cancelButton.setText(TvContext.context.getResources().getString(R.string.button_cancel_str));		
	}
	
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch (v.getId()) 
		{
		case SUREBUTTONID:
			onBtClickListener.onClickListener(true);
			break;
		case CANCELBUTTONID:
			onBtClickListener.onClickListener(false);
			break;
		}
	}

	public void setParentDialog(PvrPlaytimeDialog parentDialog) 
	{
		this.parentDialog = parentDialog;
	}

	public void setOnBtClickListener(OnDlgClickListener onBtClickListener)
	{
		this.onBtClickListener = onBtClickListener;
	}

	public interface OnDlgClickListener
	{
		public void onClickListener(boolean flag);
	}
	
	public int GetHour()
	{
		return hourText.getValue();
	}
	
	public int GetMinute()
	{
		return minuteText.getValue();
	}
	
	public int GetSecond()
	{
		return secondText.getValue();
	}
	
	public void ClearValue()
	{
		hourText.setText("0");
		minuteText.setText("0");
		secondText.setText("0");
	}
}

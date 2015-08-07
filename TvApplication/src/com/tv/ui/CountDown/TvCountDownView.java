package com.tv.ui.CountDown;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TvCountDownView extends LinearLayout{

	private LeftViewTitleLayout titleLayout;
	private LeftViewBodyLayout bodyLayout;
    private LinearLayout layout;
    private TextView t_content;
    private TextView t_time;
    private TextView t_hint;
    
    private int width = 615;
    private int height = 1080 - 140;
    private int countdownNum = 60;
    private float resolution = TvUIControler.getInstance().getResolutionDiv();
    private float dpidiv = TvUIControler.getInstance().getDipDiv();
    
    private final int UPDATE = 1;
    private final int DISMISS = 2;
    private TvCountFunc func;
    
    private TvCountDownDialog parentDialog = null;
    
	public TvCountDownView(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		initUI(context);
	}
	
	public TvCountDownView(TvCountFunc func ,Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		initUI(context);
		this.func=func;
	}
	
	private void initUI(Context context)
	{
		this.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams laytoutLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(laytoutLp);
        this.setBackgroundResource(R.drawable.setting_bg);
        this.setGravity(Gravity.CENTER);
        this.setFocusable(true);
        this.requestFocus();
        
        titleLayout = new LeftViewTitleLayout(context);
        this.addView(titleLayout);
        
        bodyLayout = new LeftViewBodyLayout(context);
        bodyLayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(bodyLayout);
        
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(
                (int) (width / resolution), (int) (height / resolution));

        t_content = new TextView(context);
        t_content.setPadding(0, (int) (80 / resolution), 0, 0);
        t_content.setTextSize(36 / dpidiv);
        t_content.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);

        t_time = new TextView(context);
        t_time.setTextSize(400 / dpidiv);
        t_time.setPadding(0, (int) (-80 / resolution), 0, 0);
        t_time.setGravity(Gravity.CENTER);

        t_hint = new TextView(context);
        t_hint.setTextSize((int) (36 / resolution) / dpidiv);
        t_hint.setGravity(Gravity.CENTER);

        layout.addView(t_content, new LayoutParams(LayoutParams.MATCH_PARENT,
                (int) (150 / resolution)));
        layout.addView(t_time, new LayoutParams(LayoutParams.MATCH_PARENT, (int) (400 / resolution)));
        layout.addView(t_hint, new LayoutParams(LayoutParams.MATCH_PARENT, (int) (150 / resolution)));
        
        bodyLayout.addView(layout,itemLayoutParams);
        
	}
	
	
	public void updataView()
	{
		titleLayout.bindData("common_setting.png", "CommonSetting");
		t_content.setText(R.string.TV_CFG_SLEEP_TIMER_COUNT_DOWN_DIALOG_TITLE);
		t_time.setText(""+countdownNum);
		t_hint.setText(R.string.TV_CFG_SLEEP_TIMER_COUNT_DOWN_DIALOG_TIP_TEXT);
		waitToHide();
	}

	ScheduledThreadPoolExecutor countdownScheduler;
    Runnable countdownRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            //TODO 
        	Log.d("wxj","countdownRunnable countdownNum = " + countdownNum);
            if (countdownNum == 0)
            {
                handler.sendEmptyMessage(DISMISS);
            } else
            {
                countdownNum--;
                handler.sendEmptyMessage(UPDATE);
            }
        }
    };
    
    @SuppressLint("HandlerLeak")
	private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case UPDATE:
                    t_time.setText(countdownNum + "");
                    t_time.invalidate();
                    break;
                case DISMISS:
                	Log.d("wxj", "countdownNum = " + countdownNum);
                	if(countdownNum == 0)
                	{
                		countdownNum = -1;
	                	parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, 0);
	                	finishCountDown();
                	}
                	else 
                	{
                		countdownNum = -1;
						parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, 1);
                	}
                	
                    break;
            }
        }
    };

    private void waitToHide()
    {
        shutdownCountScheduler();
        countdownScheduler = new ScheduledThreadPoolExecutor(1);
        countdownScheduler.scheduleAtFixedRate(countdownRunnable, 1, 1, TimeUnit.SECONDS);
    }

    public void shutdownCountScheduler()
    {
        if (countdownScheduler != null)
        {
            if (!countdownScheduler.isShutdown())
                countdownScheduler.shutdown();
            countdownScheduler = null;
        }
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("wxj","count down key down");
		if(event.getAction() == KeyEvent.ACTION_UP)
			return false;
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_BACK:
			Log.d("wxj", "count down back exit");
			break;
		}
		
		countdownNum = -1;
    	parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, 0);
		return true;
	}

	public void setParentDialog(TvCountDownDialog parentDialog) {
		this.parentDialog = parentDialog;
	}
	
	public void finishCountDown()
	{
		if(func!=null)
		{
			func.CountFinishFunction();
		}
	}

}

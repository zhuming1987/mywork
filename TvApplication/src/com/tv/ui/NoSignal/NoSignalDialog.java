package com.tv.ui.NoSignal;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.app.TvUIControler;
import com.tv.data.TvType;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.CountDown.TvCountDownDialog;
import com.tv.ui.MainMenu.TvMenuDialog;
import com.tv.ui.base.TvBaseDialog;

@SuppressLint("HandlerLeak")
public class NoSignalDialog extends TvBaseDialog{

	private NoSignalTextView noSignalTextView;
	private DialogListener dialogListener;
	private QuickKeyListener mQuickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
	private ScheduledThreadPoolExecutor aniTimer;
	private int width = TvUIControler.getInstance().getDisplayWidth();
    private int height = TvUIControler.getInstance().getDisplayHeight();
    private float resolutionDiv = TvUIControler.getInstance().getResolutionDiv();
    private final int DIALOG_WIDTH = 414;
    private final int DIALOG_HEIGHT = 240;
    
    SharedPreferences sp = TvContext.context.getSharedPreferences(TvConfigDefs.TV_CFG_SLEEP_TIMER, Context.MODE_PRIVATE);
   
    Handler nosignaltimehandler = new Handler();
    
    Runnable nosignaltimetimerunnable=new Runnable(){
		@Override
		public void run() {
		    // TODO Auto-generated method stub
			ShowTvCountDownDialog(nosignaltimehandler,this,TvConfigDefs.NO_SIGNAL_SLEEP_COUNT);
			//sleeptimehandler.postDelayed(this, 2000);
		}
	};
    
	public NoSignalDialog() {
		super(TvContext.context, R.style.dialog, TvConfigTypes.TV_DIALOG_NO_SIGNAL);
		// TODO Auto-generated constructor stub
		noSignalTextView = new NoSignalTextView(TvContext.context);
		setDialogAttributes(Gravity.TOP|Gravity.LEFT,  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(noSignalTextView);
		setAutoDismiss(false);
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return false;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		switch (cmd) {
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			//noSignalTextView.updateView(obj.toString());
			startAniTimer();
			if(sp.getInt(TvConfigDefs.NO_SIGNAL_SLEEP_COUNT, 0) != 0)
			{
				nosignaltimehandler.postDelayed(nosignaltimetimerunnable, sp.getInt(TvConfigDefs.NO_SIGNAL_SLEEP_COUNT, 9999*60*60*1000));
			}
			super.showUI();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			Log.i("gky", "NoSignalDialog::processCmd:DISMISS");
			super.dismissUI();
			cleanAniTimer();
			if(sp.getInt(TvConfigDefs.NO_SIGNAL_SLEEP_COUNT, 0) != 0)
			{
				nosignaltimehandler.removeCallbacks(nosignaltimetimerunnable);
			}
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("gky", "NoSignalDialog::onKeyDown:keyCode is " + keyCode);
		if (event.getAction() == KeyEvent.ACTION_UP) {
			return false;
		}
		if(sp.getInt(TvConfigDefs.NO_SIGNAL_SLEEP_COUNT, 0) != 0)
		{
			nosignaltimehandler.removeCallbacks(nosignaltimetimerunnable);
			nosignaltimehandler.postDelayed(nosignaltimetimerunnable, sp.getInt(TvConfigDefs.NO_SIGNAL_SLEEP_COUNT, 9999*60*60*1000));
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_CHANNEL_UP:
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_CHANNEL_DOWN:
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_MEDIA_BOOKING://TEXT
		case KeyEvent.KEYCODE_FACTORY_SOURCE_ADD://SUBTITLE
		case KeyEvent.KEYCODE_PROG_RED://REC.
		case KeyEvent.KEYCODE_PROG_BLUE://BLUE
		case KeyEvent.KEYCODE_PROG_YELLOW://YELLOW
		case KeyEvent.KEYCODE_PROG_GREEN://GREEN
		case KeyEvent.KEYCODE_INFO://INFO
		case KeyEvent.KEYCODE_SHUTTLE_RIGHT_SPEED_2://RADIO
		case KeyEvent.KEYCODE_SHUTTLE_RIGHT_SPEED_3://A.D
		case KeyEvent.KEYCODE_MEDIA_REWIND://EPG
		case KeyEvent.KEYCODE_ENTER:
			return mQuickKeyListener.onQuickKeyDownListener(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	public void initPlay()
    {
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.x = (int) (Math.random() * ((width - DIALOG_WIDTH) / resolutionDiv));
		layoutParams.y = (int) (Math.random() * ((height - DIALOG_HEIGHT) / resolutionDiv));
		//Log.i("gky", "NoSignalDialog::initPlay:x is " + layoutParams.x + " y is " + layoutParams.y);
		layoutParams.width = DIALOG_WIDTH;
		layoutParams.height = DIALOG_HEIGHT;
		getWindow().setAttributes(layoutParams);
    }

	public void startAniTimer()
    {
        cleanAniTimer();
        aniTimer = new ScheduledThreadPoolExecutor(1);
        aniTimer.scheduleAtFixedRate(new Runnable()
        {
            private int count = 0;

            @Override
            public void run()
            {
                count++;
                if (count >= 3)
                {
                    mHandler.sendEmptyMessage(0);
                    count = 0;
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void cleanAniTimer()
    {
        if (aniTimer != null)
        {
        	Log.i("gky", "cleanAniTimer");
            aniTimer.shutdownNow();
            aniTimer = null;
        }
    }
    
    private Handler mHandler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
            initPlay();
        }
    };
    
    public void ShowTvCountDownDialog(Handler handler, Runnable runnable, String Type)
	{
		final Handler mHandler = handler;
		final Runnable mRunnable = runnable;
		final String mTimerTypeString = Type;
		TvCountDownDialog CountDownDialog = new TvCountDownDialog();
		CountDownDialog.setDialogListener(new DialogListener() {
			
			@Override
			public boolean onShowDialogDone(int ID) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int onDismissDialogDone(Object... obj) {
				// TODO Auto-generated method stub
				int ret = (Integer) obj[0];
				if(ret != 0)
				{
					mHandler.removeCallbacks(mRunnable);
					mHandler.postDelayed(mRunnable, sp.getInt(mTimerTypeString, 0));
				}
				return 0;
			}
		});
		CountDownDialog.processCmd(TvConfigTypes.TV_DIALOG_COUNT_DOWN, DialogCmd.DIALOG_SHOW, null);
	}
}

package com.tv.ui.Pvrsetting;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.RecordProgramData;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PVRChannelListView extends LinearLayout implements OnTouchListener, OnKeyListener
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip = TvUIControler.getInstance().getDipDiv();
	private static int TIME_UPDATE = 1;
	private static int TIME_CLEAR = 2;
	private static int DIALOG_CLEAR = 3;
	private Context context;
	
	private PVRChannelListDialog parentDialog = null;

    private LinearLayout mainLayout;
    private LinearLayout aboveLayout;
    private LinearLayout bottomLayout;
    private LinearLayout frontabove;
    private LinearLayout behindabove;
    private LinearLayout channelLayout;    
    private LinearLayout buttonLayout;
    private LinearLayout listlayout;    
    private ScrollView scrollView;
    private SurfaceView pvrPlaybackView; 
    private LinearLayout.LayoutParams itemParams;
    
    private TextView titleText; 
    private TextView channelNumbText; 
    private TextView channelNameText; 
    private TextView progNameText; 
    private TextView timeText; 
    private TextView deleteText; 
    private TextView sortText; 
    private TextView indexText; 
    
    private ImageView fullScreenImage;
    private ImageView redImageView; 
    private ImageView greenImageView;
    private ImageView indexImageView;
    private ImageView yellowImageView;
    private ImageView diskItemDivider;   
    
    private int frontWidth = (int) (1270 / div);
    private int behindWidth = (int) (650 / div);
    private int titleheight = (int) (150 / div);
    private int bottomheight = (int) (930 / div);
    
    private ArrayList<RecordProgramData> data = new ArrayList<RecordProgramData>();
    private ArrayList<RecordListItemView> selecteItems; 
    
    String diskPath = "";
    String diskLabel = "";
    int orderIndex = 2;
    int orderNum = 0;
    int pos = 0;
    int[] playBackTime = new int[2];
    int curTime = 0;
    int totalTime = 1; 
    int data_count = 0;
    
    private Timer timer;
    private UsbReceiver usbReceiver;
	
    //录制盘的
	private class UsbReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			
			if (action.equals(Intent.ACTION_MEDIA_MOUNTED)
					|| action.equals(Intent.ACTION_MEDIA_EJECT))
			{
			}
			else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED))
			{
				//当前U盘被移出，退出播放列表界面
				TvPluginControler.getInstance().getPvrManager().exitPlayBack();
				timeHandler.sendEmptyMessage(DIALOG_CLEAR);
			}
		}
	}
	
	public void registerUSBDetector()
	{
		IntentFilter iFilter;
		iFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
		iFilter.addDataScheme("file");
		TvContext.context.registerReceiver(usbReceiver, iFilter);
		
		iFilter = new IntentFilter(Intent.ACTION_MEDIA_EJECT);
		iFilter.addDataScheme("file");
		TvContext.context.registerReceiver(usbReceiver, iFilter);
		
		iFilter = new IntentFilter(Intent.ACTION_MEDIA_UNMOUNTED);
		iFilter.addDataScheme("file");
		TvContext.context.registerReceiver(usbReceiver, iFilter);
	}
    
    public PVRChannelListView(Context context)
    {
        super(context);
        this.context = context;
        this.setOnKeyListener(this);
        initViews();   
        
        usbReceiver =  new UsbReceiver();
		registerUSBDetector();
    }
    
    private void initViews()
    {
    	setOrientation(LinearLayout.VERTICAL);
        setFocusable(false);

        mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(VERTICAL);
        this.addView(mainLayout, new LayoutParams((int) (1920 / div), (int) (1080 / div)));

        aboveLayout = new LinearLayout(context);
        aboveLayout.setOrientation(HORIZONTAL);
        aboveLayout.setGravity(Gravity.CENTER);
        aboveLayout.setBackgroundColor(Color.rgb(53,152,220));
        LinearLayout.LayoutParams aboveParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, titleheight);
        mainLayout.addView(aboveLayout, aboveParams);
        
        titleText=new TextView(context);     
        titleText.setTextSize((int) (76 / dip));
        titleText.setTextColor(Color.rgb(255, 255, 255));
        titleText.setGravity(Gravity.CENTER);
        aboveLayout.addView(titleText);
        
        bottomLayout = new LinearLayout(context);
        bottomLayout.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams bottomParams = new LayoutParams(LayoutParams.MATCH_PARENT,bottomheight);   
        mainLayout.addView(bottomLayout,bottomParams);
        
        frontabove = new LinearLayout(context);
        frontabove.setBackgroundColor(Color.rgb(25, 26, 28));
        frontabove.setOrientation(VERTICAL);
        LinearLayout.LayoutParams frontParams = new LayoutParams(frontWidth,LayoutParams.MATCH_PARENT); 
        bottomLayout.addView(frontabove,frontParams);
                
        channelLayout = new LinearLayout(context);
        channelLayout.setOrientation(HORIZONTAL);
        channelLayout.setBackgroundColor(Color.rgb(50, 51, 53));
        channelLayout.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams channelParams = new LayoutParams(LayoutParams.MATCH_PARENT,(int) (120 / div));
        frontabove.addView(channelLayout,channelParams);
        
        channelNumbText=new TextView(context);     
        channelNumbText.setTextSize((int) (48 / dip));
        channelNumbText.setTextColor(Color.rgb(255, 255, 255));
        channelNumbText.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams channelNumbParams = new LayoutParams((int)(240 / div),LayoutParams.MATCH_PARENT);
        channelNumbParams.leftMargin = (int)(100 / div);
        channelLayout.addView(channelNumbText,channelNumbParams);
        
        channelNameText=new TextView(context);     
        channelNameText.setTextSize((int) (48 / dip));
        channelNameText.setTextColor(Color.rgb(255, 255, 255));
        channelNameText.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams channelNameParams = new LayoutParams((int)(420 / div),LayoutParams.MATCH_PARENT);
        channelLayout.addView(channelNameText,channelNameParams);
        
        progNameText=new TextView(context);     
        progNameText.setTextSize((int) (48 / dip));
        progNameText.setTextColor(Color.rgb(255, 255, 255));
        progNameText.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams progNameParams = new LayoutParams((int)(520 / div),LayoutParams.MATCH_PARENT);
        channelLayout.addView(progNameText,progNameParams);
        
        scrollView = new ScrollView(context);
		scrollView.setVerticalScrollBarEnabled(true);
		scrollView.setHorizontalScrollBarEnabled(false);
		scrollView.setScrollbarFadingEnabled(true);
		scrollView.setAlwaysDrawnWithCacheEnabled(true);
		scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		frontabove.addView(scrollView);
		listlayout = new LinearLayout(mContext);
        listlayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(listlayout, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
        
		behindabove = new LinearLayout(context);
		behindabove.setOrientation(VERTICAL);
		behindabove.setBackgroundColor(Color.rgb(0, 0, 0));
        LinearLayout.LayoutParams behindParams = new LayoutParams(behindWidth,LayoutParams.MATCH_PARENT); 
        bottomLayout.addView(behindabove,behindParams);
        
        pvrPlaybackView = new SurfaceView(context);
        LinearLayout.LayoutParams viewParams = new LayoutParams((int)(608 / div),(int)(342 / div)); 
        viewParams.topMargin = (int)(50 / div);
        viewParams.leftMargin = (int)(21 / div);
        viewParams.rightMargin = (int)(21 / div);
        pvrPlaybackView.setBackgroundColor(Color.BLUE);
        behindabove.addView(pvrPlaybackView , viewParams);
        
        timeText=new TextView(context);     
        timeText.setTextSize((int) (30 / dip));
        timeText.setTextColor(Color.rgb(255, 255, 255));
        timeText.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams timeParams = new LayoutParams(LayoutParams.MATCH_PARENT,(int)(50 / div));
        timeParams.topMargin = (int)(20 / div);
        behindabove.addView(timeText,timeParams);
        
        buttonLayout = new LinearLayout(context);
        buttonLayout.setOrientation(HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams buttonParams = new LayoutParams(LayoutParams.MATCH_PARENT,(int)(60 / div)); 
        buttonParams.topMargin = (int)(400 / div);
        behindabove.addView(buttonLayout,buttonParams);
        
        redImageView = new ImageView(context);
        redImageView.setBackgroundResource(R.drawable.epg_red);
        LinearLayout.LayoutParams redbuttonParams = new LayoutParams((int)(30 / div),(int)(30 / div)); 
        redbuttonParams.leftMargin = (int)(40 / div);
        buttonLayout.addView(redImageView, redbuttonParams);
        
        deleteText = new TextView(context);
        deleteText.setTextSize((int) (36 / dip));
        deleteText.setTextColor(Color.rgb(255, 255, 255));
        LinearLayout.LayoutParams deleteParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT); 
        buttonLayout.addView(deleteText, deleteParams);        
        
        greenImageView = new ImageView(context);
        greenImageView.setBackgroundResource(R.drawable.epg_green);
        LinearLayout.LayoutParams greenbuttonParams = new LayoutParams((int)(30 / div),(int)(30 / div)); 
        greenbuttonParams.leftMargin = (int)(20 / div);
        buttonLayout.addView(greenImageView, greenbuttonParams);
        
        sortText = new TextView(context);
        sortText.setTextSize((int) (36 / dip));
        sortText.setTextColor(Color.rgb(255, 255, 255));
        LinearLayout.LayoutParams orderParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT); 
        buttonLayout.addView(sortText, orderParams);
        
        indexImageView = new ImageView(context);
        indexImageView.setBackgroundResource(R.drawable.index);
        LinearLayout.LayoutParams indexbuttonParams = new LayoutParams((int)(60 / div),(int)(30 / div)); 
        indexbuttonParams.leftMargin = (int)(20 / div);
        buttonLayout.addView(indexImageView, indexbuttonParams);
        
        indexText = new TextView(context);
        indexText.setTextSize((int) (36 / dip));
        indexText.setTextColor(Color.rgb(255, 255, 255));
        LinearLayout.LayoutParams indexParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT); 
        buttonLayout.addView(indexText, indexParams);
      
        yellowImageView = new ImageView(context);
        yellowImageView.setBackgroundResource(R.drawable.epg_yellow);
        LinearLayout.LayoutParams yellowbuttonParams = new LayoutParams((int)(30 / div),(int)(30 / div)); 
        yellowbuttonParams.leftMargin = (int)(20 / div);
        buttonLayout.addView(yellowImageView, yellowbuttonParams);
        
        fullScreenImage = new ImageView(context);
        fullScreenImage.setBackgroundResource(R.drawable.full_screen);
        LinearLayout.LayoutParams fsImageParams = new LayoutParams((int)(50 / div),(int)(50 / div)); 
        fsImageParams.leftMargin = (int)(10 / div);
        buttonLayout.addView(fullScreenImage, fsImageParams);
        
        itemParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (120 / div));
        itemParams.gravity = Gravity.CENTER_VERTICAL;               
    }
    
    public void update(String path,String Label) 
    {
		// TODO Auto-generated method stub
    	diskPath = path;
    	diskLabel = Label;
    	
    	this.setVisibility(View.VISIBLE);
        
        titleText.setText(R.string.sky_pvr_record_list);                 
        channelNumbText.setText(R.string.sky_pvr_record_list_lcn);
        channelNameText.setText(R.string.sky_pvr_record_list_channel);
        progNameText.setText(R.string.sky_pvr_record_list_program);
        deleteText.setText(R.string.sky_pvr_record_list_delete);
        indexText.setText(R.string.sky_pvr_record_list_index);
        if(orderIndex == 2)
		{
			channelNumbText.setTextColor(Color.RED);
			progNameText.setTextColor(Color.WHITE);			
		}
		else if(orderIndex == 3)
		{
			channelNameText.setTextColor(Color.RED);
			channelNumbText.setTextColor(Color.WHITE);
		}
		else if(orderIndex == 4)
		{
			progNameText.setTextColor(Color.RED);
			channelNameText.setTextColor(Color.WHITE);
		}
        if(orderNum == 1)
		{
			sortText.setText(R.string.sky_pvr_record_list_descend);
		}
		else
		{
			sortText.setText(R.string.sky_pvr_record_list_ascend);
		}
        
        data = TvPluginControler.getInstance().getPvrManager().getRecorderList(diskPath);
    	selecteItems = new ArrayList<RecordListItemView>();

    	data_count = data.size();
        listlayout.removeAllViews();
        if (data_count > 0)
        {
            for (int i = 0; i < data_count; i++)
            {            	
            	selecteItems.add(new RecordListItemView(mContext, this));
            	diskItemDivider = new ImageView(mContext);
            	diskItemDivider.setBackgroundResource(R.drawable.setting_line);
                listlayout.addView(diskItemDivider, new LayoutParams(LayoutParams.MATCH_PARENT,(int) (2 / div)));
                selecteItems.get(i).setId(i);
                listlayout.addView(selecteItems.get(i), itemParams);
                selecteItems.get(i).setOnClickListener(new LisenerItem());
                selecteItems.get(i).setOnFocusChangeListener(new LisenerFocus());
                
    			RecordListItemView itemView = selecteItems.get(i);
    			itemView.update(data.get(i).getPvrProgLcn(),data.get(i).getPvrProgChannelName(),data.get(i).getPvrProgEventName());   		
            }
        }
        else 
        {
        	pvrPlaybackView.setBackgroundColor(Color.BLUE);
		}
        if(pos > data_count -1 )
        {
        	pos = 0;
        }
        if (data_count > 0)
        {
        	selecteItems.get(pos).requestFocus();
        } 
	} 
    
    class LisenerItem implements OnClickListener
    {
        public void onClick(View view)
        {                   	
        	pvrPlaybackView.setBackground(null);
        	TvPluginControler.getInstance().getPvrManager().createPVRPlaybackView(pvrPlaybackView,pos);
	
    		if(timer != null)
    		{
    			timer.cancel();
    		}
    		timer = new Timer();   
            timer.schedule(new timeTimerTask(), 0, 1000);
            checkedSelectedItem(pos);
        }
    }
    
    class LisenerFocus implements OnFocusChangeListener
    {
        public void onFocusChange(View view, boolean hasFocus)
        {
        	pos = view.getId();
        	((RecordListItemView) view).setBackGroundColorUser(hasFocus);
        }
    }
    
    public class timeTimerTask extends TimerTask
    {   
        @Override  
    	public void run() 
        {   
    	    // TODO Auto-generated method stub   
			if(curTime < totalTime)
			{
				timeHandler.sendEmptyMessage(TIME_UPDATE);	
			}
			else
			{
				timeHandler.sendEmptyMessage(TIME_CLEAR);
				timer.cancel();
				timeTimerTask.this.cancel();				
			}
    	}   
    }  
    
    private Handler timeHandler = new Handler() 
    {
		public void handleMessage(Message msg) 
		{
			if (msg.what == TIME_UPDATE) 
			{
    			playBackTime = TvPluginControler.getInstance().getPvrManager().getPlayBackTime(pos);
				curTime = playBackTime[0];
				totalTime = playBackTime[1];
				SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
				format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
				long time1 = (long)(curTime*1000);
				long time2 = totalTime*1000;
				Date data1 = new Date(time1);
				Date data2 = new Date(time2);
				String curTm = format.format(data1);
				String totTm = format.format(data2);
				timeText.setText(curTm+"/"+totTm);	    		
			}
			else if (msg.what == TIME_CLEAR) 
			{
				curTime = 0;
				totalTime = 1;
				timeText.setText("--:--:--/--:--:--");
			}
			else if (msg.what == DIALOG_CLEAR) 
			{
				parentDialog.processCmd(TvConfigTypes.TV_DIALOG_PVR_CHANNELLIST,DialogCmd.DIALOG_DISMISS, null);
			}
		}
	};
	
	private void checkedSelectedItem(int id)
    {
        if (data_count == 0)
        {
            return;
        }
        for (int i = 0; i < data_count; i++)
        {
        	RecordListItemView view = selecteItems.get(i);
            boolean isChecked = view.getchecked();
            if (view.getId() == id)
            {
                if (!isChecked)
                {
                    view.setChecked(true);
                } 
            } else
            {
                view.setChecked(false);
            }
        }
    }
    
    public void setParentDialog(PVRChannelListDialog parentDialog) 
    {
		this.parentDialog = parentDialog;
	}   

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) 
	{
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)
        {
            timeHandler.sendEmptyMessageDelayed(DIALOG_CLEAR, 500);
			return true;
        }
		else if (keyCode == KeyEvent.KEYCODE_PROG_RED)
	    { 
		    // for delete
			TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
			tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() {				
				@Override
				public void onClickListener(boolean flag) {
					// TODO Auto-generated method stub
					if(flag == true)
					{
						TvPluginControler.getInstance().getPvrManager().deletePVRRecordFile(pos);
						update(diskPath,diskLabel);
						if(data_count == 0)
						{
							parentDialog.processCmd(TvConfigTypes.TV_DIALOG_PVR_CHANNELLIST,DialogCmd.DIALOG_DISMISS, null);
						}
					}					
				}
			});
			tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
					new TvToastFocusData("", "", this.getResources().getString(R.string.sky_pvr_record_list_delete_tip),2));		     
		    return true;
	    }
		else if (keyCode == KeyEvent.KEYCODE_MEDIA_FUNCTION)
		{
			orderIndex = TvPluginControler.getInstance().getPvrManager().getPVRRecordListIndex();
			update(diskPath,diskLabel);
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_PROG_GREEN)
		{			
			orderNum = (++orderNum > 1)?0:(orderNum);
			if(orderNum == 1)
			{
				TvPluginControler.getInstance().getPvrManager().sortPVRRecordList(orderIndex,true);
			}
			else
			{
				TvPluginControler.getInstance().getPvrManager().sortPVRRecordList(orderIndex,false);
			}
			update(diskPath,diskLabel);
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_PROG_YELLOW)
		{
        	parentDialog.processCmd(TvConfigTypes.TV_DIALOG_PVR_CHANNELLIST,DialogCmd.DIALOG_DISMISS, "fullScreen");
        	PvrTimeshitfDlg dlg = new PvrTimeshitfDlg(); 
        	dlg.selectPath = diskPath;
			dlg.selectLabel = diskLabel;
        	dlg.processCmd(TvConfigTypes.TV_DIALOG_PVR_TIMESHITH_DLG, DialogCmd.DIALOG_SHOW, "playback");
        	return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_TV_INPUT || keyCode == KeyEvent.KEYCODE_HOME)
		{
			parentDialog.processCmd(TvConfigTypes.TV_DIALOG_PVR_CHANNELLIST,DialogCmd.DIALOG_DISMISS, null);
			return true;
		}
        return true;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}

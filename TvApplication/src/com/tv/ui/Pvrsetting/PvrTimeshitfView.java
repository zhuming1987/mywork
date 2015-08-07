package com.tv.ui.Pvrsetting;

import com.tv.app.R;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.RelativeLayout;

import com.tv.app.TvUIControler;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.plugin.tvfuncs.PvrManager.EnumPvrRecordStatus;
import com.tv.framework.plugin.tvfuncs.PvrManager.PVR_MODE;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.SettingView.TvSettingItemView.OnSettingKeyDownListener;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.Pvrsetting.PvrPlaytimeDialog;
import com.tv.ui.Pvrsetting.PvrPlaytimeView.OnDlgClickListener;

public class PvrTimeshitfView extends LinearLayout implements OnSettingKeyDownListener, DialogListener
{
	private String TAG = "PvrTimeshitfView";
	
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip = TvUIControler.getInstance().getDipDiv();
	
	private int TextSize=(int)(30/dip);
	
	private int weith_ButtonMenuBar=(int) (1920 / div);//底部整个menubar的宽度
	private int hight_ButtonMenuBar=(int) (400 / div); //底部整个menubar的高度
	
	private int weith_MenuItem=(int) (192 / div);	   //底部MenuItem的宽度，图片加字符串
	private int hight_MenuItem=(int) (300 / div);	   //底部MenuItem的高度，图片加字符串
	
	private int weith_MenuImage=(int) (120 / div);	   //底部图片的高度
	private int hight_MenuImage=(int) (120 / div);	   //底部图片的宽度
	
	private int weith_TextImage=(int) (192 / div);	   //底部图片的宽度
	private int hight_Textmage=(int) (130 / div);	   //底部图片的高度
	
	private int rightMargin=(int) (50 / div);
	private int topMargin=(int) (30 / div);
	private int bottomMargin=(int) (15 / div);
	
	
	
	private Context context;
	
	private LinearLayout mainLayout;
	private LinearLayout recordStatusLayout;
	private LinearLayout usbStatusLayout;
	private LinearLayout statusLayout;
	private LinearLayout menuLayout;
	private RelativeLayout lineartitleLayout;
	
	private LinearLayout serviceNameLayout;
	private int serviceNameLayoutID = 11;	
	private TextView serviceNameText;
	private int serviceNameTextID = 12;
	
	private LinearLayout eventNameLayout;
	private int eventNameLayoutID = 13;	
	private TextView eventNameText;
	private int eventNameTextID = 14;
	
	private LinearLayout linearrecordertimeLayout;
	private int linearrecordertimeID = 15;	
	private TextView current_timeText;
	private int current_timeID = 16;
	
	private TextView current_timeTagText;
	private int current_timeTagID = 17;
	
	private TextView record_timeText;
	private int record_timeID = 18;
	
	private RelativeLayout ProgressBarLayout;
	private TextProgressBar recordBar;
	private ProgressBar repeatBar;
	
	private LinearLayout linearmenuLayout;
	private int linearmenuLayoutID = 19;
	
	private LinearLayout  player_pauseLayout;
	private int player_pauseLayoutID = 20;
	private ImageButton player_pauseImage;
	private int player_pauseImageID = 21;
	private TextView player_pauseText;
	private int player_pauseTextID = 22;
	
	private LinearLayout  player_stopLayout;
	private int player_stopLayoutID = 23;
	private ImageButton player_stopImage;
	private int player_stopImageID = 25;
	private TextView player_stopText;
	private int player_stopTextID = 26;
	
	private LinearLayout  player_ABRepeatLayout;
	private int player_ABRepeatLayoutID = 27;
	private ImageButton player_ABRepeatImage;
	private int player_ABRepeatImageID = 28;
	private TextView player_ABRepeatText;
	private int player_ABRepeatTextID = 29;
	
	private LinearLayout  player_revLayout;
	private int player_revLayoutID = 30;
	public ImageButton player_revImage;
	private int player_revImageID = 31;
	private TextView player_revText;
	private int player_revTextID = 32;
	
	private LinearLayout  player_ffLayout;
	private int player_ffLayoutID = 33;
	public ImageButton player_ffImage;
	private int player_ffImageID = 34;
	private TextView player_ffText;
	private int player_ffTextID = 35;
	
	private LinearLayout  player_timeLayout;
	private int player_timeLayoutID = 36;
	private ImageButton player_timeImage;
	private int player_timeImageID = 37;
	private TextView player_timeText;
	private int player_timeTextID = 38;
	
	private LinearLayout  player_backwardLayout;
	private int player_backwardLayoutID = 39;
	private ImageButton player_backwardImage;
	private int player_backwardImageID = 40;
	private TextView player_backwardText;
	private int player_backwardTextID = 41;
	
	private LinearLayout  player_forwardLayout;
	private int player_forwardLayoutID = 42;
	private ImageButton player_forwardImage;
	private int player_forwardImageID = 43;
	private TextView player_forwardText;
	private int player_forwardTextID = 44;
	
	public RelativeLayout record_status_view;
	public ImageView record_statusImage;
	public int record_statusImageID = 45;
	public TextView record_statusText;
	public int record_statusTextID = 46;
	
	public RelativeLayout usb_status_view;
	public int usb_status_viewID = 47;
	public ImageView usb_status_viewImage;
	public int  usb_status_viewImageID = 48;
	public ProgressBar usb_status_progressbar;
	public int usb_status_progressbarID = 49;
	public TextView usbFreeSpace;
	public int usbFreeSpaceID = 50;
	public TextView usbLabelName;
	public int usbLabelNameID = 51;
	
	
	public PvrTimeshitfDlg parentDialog;
	
	private android.widget.RelativeLayout.LayoutParams lp4LoopAB;
	
	public boolean paly_pause_flag = true;
	
	//记录REV使用的图片
	public  int revpicture = -1;
	
	//记录FF使用的图片
	public  int ffpicture = -1;
	
	//记录播放AB面的图片
	public  int ablooppicture = -1;
	
	//左上角标题
	private String statuTitle="";
	
	PvrPlaytimeDialog playTimeDlg;
	
	public PvrTimeshitfView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
        this.context = context;
        initViews(); 
		setOrientation(LinearLayout.VERTICAL);
	}
	
	private void initViews()
	{
		setOrientation(LinearLayout.VERTICAL);
		FrameLayout frameLayout = new FrameLayout(context);
        this.addView(frameLayout, new LayoutParams((int) (1920 / div), (int) (1080 / div)));
        
        
        //左上角的recording或者TimeShift的提示（透明，闪烁）
        FrameLayout.LayoutParams PvrTimshitStatusParams = new FrameLayout.LayoutParams(
        		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.TOP);
        PvrTimshitStatusParams.leftMargin = (int)(36/div);
        recordStatusLayout = new LinearLayout(context);
        recordStatusLayout.setOrientation(LinearLayout.HORIZONTAL);
        recordStatusLayout.setGravity(Gravity.CENTER);
      
        record_statusImage = new ImageView(context);
        record_statusImage.setId(record_statusImageID);
        recordStatusLayout.addView(record_statusImage);
//        record_statusImage.setBackgroundResource(R.drawable.idle_img_press_ststus_pvr);
        record_statusText = new TextView(context);
        record_statusText.setText(statuTitle);
        record_statusText.setTextSize((int) (TextSize));
        recordStatusLayout.addView(record_statusText);
        frameLayout.addView(recordStatusLayout, PvrTimshitStatusParams);
        
        FrameLayout.LayoutParams usbStatusLayoutParams = new FrameLayout.LayoutParams(
        		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.TOP);
        usbStatusLayout = new LinearLayout(context);
        
        
        usb_status_view = new RelativeLayout(context);//layout.inflate(R.layout.usb_status, null);
        RelativeLayout.LayoutParams usb_status_viewParams = new RelativeLayout.LayoutParams((int)(100/div), ViewGroup.LayoutParams.WRAP_CONTENT);
        usb_status_view.setId(usb_status_viewID);
        
        usb_status_viewImage = new ImageView(context);
        RelativeLayout.LayoutParams usb_status_viewImageParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int)(60/div));
        usb_status_viewImage.setId(usb_status_viewImageID);
        usb_status_viewImage.setBackgroundResource(R.drawable.status_ex_storage);
        usb_status_view.addView(usb_status_viewImage, usb_status_viewImageParams);

        RelativeLayout.LayoutParams usbFreeSpaceParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        usbFreeSpaceParams.addRule(RelativeLayout.BELOW, usb_status_viewImageID);
        usbFreeSpaceParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        usbFreeSpace = new TextView(context);
        usbFreeSpace.setText("usbFreeSpace");
        usbFreeSpace.setId(usbFreeSpaceID);
        usb_status_view.addView(usbFreeSpace, usbFreeSpaceParams);
        
        RelativeLayout.LayoutParams usb_status_progressbarParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        usb_status_progressbarParams.addRule(RelativeLayout.BELOW, usbFreeSpaceID);
        usb_status_progressbarParams.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
        usb_status_progressbar = new ProgressBar(context,  null,android.R.attr.progressBarStyleHorizontal);
        usb_status_progressbar.setId(usb_status_progressbarID);
        usb_status_view.addView(usb_status_progressbar, usb_status_progressbarParams);
        
        RelativeLayout.LayoutParams usbLabelNameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        usbLabelNameParams.addRule(RelativeLayout.BELOW, usb_status_progressbarID);
        usbLabelNameParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        usbLabelName = new TextView(context);
        usbLabelName.setText(" ");
        usb_status_view.addView(usbLabelName, usbLabelNameParams);
        
        usbStatusLayout.addView(usb_status_view, usb_status_viewParams);
		
        frameLayout.addView(usbStatusLayout, usbStatusLayoutParams);
        
        menuLayout = new LinearLayout(context);
        FrameLayout.LayoutParams menuLayoutParams = new FrameLayout.LayoutParams(
        		weith_ButtonMenuBar, hight_ButtonMenuBar,  Gravity.BOTTOM);
        menuLayout.setOrientation(LinearLayout.VERTICAL);
        menuLayout.setGravity(Gravity.CENTER);
        
        lineartitleLayout = new RelativeLayout(context);
        
        menuLayout.addView(lineartitleLayout);
        
        serviceNameLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams serviceNameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        serviceNameParams.leftMargin = (int)(36/div);
        serviceNameParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        serviceNameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        serviceNameLayout.setId(serviceNameLayoutID);
        serviceNameText = new TextView(context);
        serviceNameText.setTextSize((int) (TextSize));
        serviceNameText.setId(serviceNameTextID);
        serviceNameText.setText("");
        serviceNameLayout.addView(serviceNameText);
        lineartitleLayout.addView(serviceNameLayout, serviceNameParams);
        
        eventNameLayout = new LinearLayout(context);
        RelativeLayout.LayoutParams eventNameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        eventNameParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        eventNameParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        eventNameLayout.setId(eventNameLayoutID);
        eventNameText = new TextView(context);
        eventNameText.setTextSize((int) (TextSize));
        eventNameText.setId(eventNameTextID);
        eventNameText.setText("");
        eventNameLayout.addView(eventNameText);
        lineartitleLayout.addView(eventNameLayout, eventNameParams);
        
        linearrecordertimeLayout = new LinearLayout(context);
        linearrecordertimeLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams linearrecordertimeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearrecordertimeParams.rightMargin = (int)(36/div);
        linearrecordertimeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        linearrecordertimeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        linearrecordertimeLayout.setId(linearrecordertimeID);
        current_timeText = new TextView(context);
        current_timeText.setTextSize((int) (TextSize));
        current_timeText.setId(current_timeID);
        current_timeText.setText("00:00:00");
        linearrecordertimeLayout.addView(current_timeText);
        
        current_timeTagText = new TextView(context);
        current_timeTagText.setTextSize((int) (TextSize));
        current_timeTagText.setId(current_timeTagID);
        current_timeTagText.setText("\\");
        linearrecordertimeLayout.addView(current_timeTagText);
        
        record_timeText = new TextView(context);
        record_timeText.setTextSize((int) (TextSize));
        record_timeText.setId(record_timeID);
        record_timeText.setText("00:00:00");
        linearrecordertimeLayout.addView(record_timeText);
        
        lineartitleLayout.addView(linearrecordertimeLayout, linearrecordertimeParams);
        
        ProgressBarLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams ProgressBarLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        menuLayout.addView(ProgressBarLayout, ProgressBarLayoutParams);
        recordBar = new TextProgressBar(context, null,android.R.attr.progressBarStyleHorizontal);
        recordBar.setSecondaryProgress(100);
        LinearLayout.LayoutParams recordBarParams =new LinearLayout.LayoutParams((int)(1920/div),(int)(30/div));
        repeatBar = new ProgressBar(context, null,android.R.attr.progressBarStyleHorizontal);
        repeatBar.setProgress(0);
        LinearLayout.LayoutParams repeatBarParams =new LinearLayout.LayoutParams((int)(1920/div),(int)(30/div));
        ProgressBarLayout.addView(recordBar, recordBarParams);
        ProgressBarLayout.addView(repeatBar, repeatBarParams);
        
        linearmenuLayout = new LinearLayout(context);
        linearmenuLayout.setId(linearmenuLayoutID);
        linearmenuLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearmenuLayout.setGravity(Gravity.CENTER);
//        LinearLayout.LayoutParams linearmenuLayoutParams = 
//        		new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        menuLayout.addView(linearmenuLayout, linearmenuLayoutParams);
//        
//        LinearLayout.LayoutParams menuImageLayoutParams = 
//        		new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        menuImageLayoutParams.bottomMargin = 36;
//            
//        LinearLayout.LayoutParams menuButtonLayoutParams = 
//        		new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        menuButtonLayoutParams.rightMargin = 84;
//        menuButtonLayoutParams.topMargin = 46;
//        menuButtonLayoutParams.bottomMargin = 36;
               
        menuLayout.addView(linearmenuLayout,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        LinearLayout.LayoutParams linearmenuLayoutParams = new LinearLayout.LayoutParams(150,100);
        linearmenuLayoutParams.gravity=Gravity.CENTER_HORIZONTAL;
        
        
        LinearLayout.LayoutParams menuImageLayoutParams = new LinearLayout.LayoutParams(weith_MenuImage, hight_MenuImage);
        menuImageLayoutParams.gravity=Gravity.CENTER_HORIZONTAL;
         
        
        LinearLayout.LayoutParams menuButtonLayoutParams = new LinearLayout.LayoutParams(weith_MenuItem, hight_MenuItem);
        menuButtonLayoutParams.rightMargin = rightMargin;
        menuButtonLayoutParams.topMargin = topMargin;
        menuButtonLayoutParams.bottomMargin = bottomMargin;
        
        
        player_pauseLayout = new LinearLayout(context);
        player_pauseLayout.setId(player_pauseLayoutID);
        player_pauseLayout.setOrientation(LinearLayout.VERTICAL);
        player_pauseLayout.setGravity(Gravity.CENTER);
        linearmenuLayout.addView(player_pauseLayout, menuButtonLayoutParams);
        
        player_pauseImage = new ImageButton(context);
        player_pauseImage.setFocusable(true);
        player_pauseImage.setId(player_pauseImageID);
        player_pauseImage.setBackgroundResource(R.drawable.player_play_focus);
        player_pauseText = new TextView(context);
        player_pauseText.setTextSize((int) (TextSize));
        player_pauseText.setGravity(Gravity.CENTER);
        player_pauseText.setId(player_pauseTextID);
        player_pauseText.setText(getResources().getString(R.string.str_player_play));
        player_pauseLayout.addView(player_pauseImage, menuImageLayoutParams);
        player_pauseLayout.addView(player_pauseText, linearmenuLayoutParams);
        
        player_pauseImage.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
            	parentDialog.onClickPlayPause();
            }
        });
        
        player_pauseImage.setOnFocusChangeListener(new OnFocusChangeListener()
    	{
			@Override
			public void onFocusChange(View arg0, boolean arg1)
			{
				// TODO Auto-generated method stub
				if(arg1)
				{
					if(paly_pause_flag)
					{
						player_pauseImage.setBackgroundResource(R.drawable.player_play_focus);
						player_pauseText.setText(getResources().getString(R.string.str_player_play));
					}
					else
					{
						player_pauseImage.setBackgroundResource(R.drawable.pausefocus);
						player_pauseText.setText(getResources().getString(R.string.str_player_pause));
					}
				}
				else
				{
					if(paly_pause_flag)
					{
						player_pauseImage.setBackgroundResource(R.drawable.player_play);
						player_pauseText.setText(getResources().getString(R.string.str_player_play));
					}
					else
					{
						player_pauseImage.setBackgroundResource(R.drawable.pauseunfocus);
						player_pauseText.setText(getResources().getString(R.string.str_player_pause));
					}
				}
			}
    	});
        
        player_stopLayout = new LinearLayout(context);
        player_stopLayout.setId(player_stopLayoutID);
        player_stopLayout.setOrientation(LinearLayout.VERTICAL);
        player_stopLayout.setGravity(Gravity.CENTER);
        linearmenuLayout.addView(player_stopLayout, menuButtonLayoutParams);
        
        player_stopImage = new ImageButton(context);
        player_stopImage.setFocusable(true);
        player_stopImage.setId(player_stopImageID);
        player_stopImage.setBackgroundResource(R.drawable.player_stop);
        player_stopText = new TextView(context);
        player_stopText.setTextSize((int) (TextSize));
        player_stopText.setGravity(Gravity.CENTER);
        player_stopText.setId(player_stopTextID);
        
        
        
        player_stopText.setText(getResources().getString(R.string.str_player_stop));
        player_stopLayout.addView(player_stopImage, menuImageLayoutParams);
        player_stopLayout.addView(player_stopText, linearmenuLayoutParams);
        
        player_stopImage.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
            	parentDialog.onClickStop();
            }
        });
        
        player_stopImage.setOnFocusChangeListener(new OnFocusChangeListener()
    	{
			@Override
			public void onFocusChange(View arg0, boolean arg1)
			{
				// TODO Auto-generated method stub
				if(arg1)
				{
					arg0.setBackgroundResource(R.drawable.player_stop_focus);
				}
				else
				{
					arg0.setBackgroundResource(R.drawable.player_stop);
				}
			}
    	});
        
        player_ABRepeatLayout = new LinearLayout(context);
        player_ABRepeatLayout.setId(player_ABRepeatLayoutID);
        player_ABRepeatLayout.setOrientation(LinearLayout.VERTICAL);
        player_ABRepeatLayout.setGravity(Gravity.CENTER);
        linearmenuLayout.addView(player_ABRepeatLayout, menuButtonLayoutParams);
        
        player_ABRepeatImage = new ImageButton(context);
        player_ABRepeatImage.setFocusable(false); 
        player_ABRepeatImage.setId(player_ABRepeatImageID);
        player_ABRepeatImage.setBackgroundResource(R.drawable.abpaly_seta_unselect_gray);
        player_ABRepeatText = new TextView(context);
        player_ABRepeatText.setTextSize((int) (TextSize));
        player_ABRepeatText.setGravity(Gravity.CENTER);
        player_ABRepeatText.setId(player_ABRepeatTextID);
        player_ABRepeatText.setText(getResources().getString(R.string.str_player_playab));
    
        player_ABRepeatLayout.addView(player_ABRepeatImage, menuImageLayoutParams);
        player_ABRepeatLayout.addView(player_ABRepeatText, linearmenuLayoutParams);
        
        player_ABRepeatImage.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
            	parentDialog.onClickPlayABLoop();
            }
        });
        
        player_ABRepeatImage.setOnFocusChangeListener(new OnFocusChangeListener()
    	{
			@Override
			public void onFocusChange(View arg0, boolean arg1)
			{
				// TODO Auto-generated method stub
				if(arg1)
				{
					if(-1 == ablooppicture || 0 == ablooppicture || 3 == ablooppicture)
					{
						Log.v("yangcheng", "onFocusChange::if -1 0 3.enter");
						arg0.setBackgroundResource(R.drawable.abpaly_seta_select);
					}
					else if(1 == ablooppicture)
					{
						arg0.setBackgroundResource(R.drawable.abpaly_setb_select);
					}
					else if(2 == ablooppicture)
					{
						arg0.setBackgroundResource(R.drawable.abpaly_exitab_select);
					}
					else
					{
						Log.v("yangcheng", "onFocusChange::else.enter");
						arg0.setBackgroundResource(R.drawable.abpaly_seta_select);
					}
				}
				else
				{
					if(-1 == ablooppicture || 0 == ablooppicture || 3 == ablooppicture)
					{
						arg0.setBackgroundResource(R.drawable.abpaly_seta_unselect);
					}
					else if(1 == ablooppicture)
					{
						arg0.setBackgroundResource(R.drawable.abpaly_setb_unselect);
					}
					else if(2 == ablooppicture)
					{
						arg0.setBackgroundResource(R.drawable.abpaly_exitab_unselect);
					}
					else
					{
						arg0.setBackgroundResource(R.drawable.abpaly_seta_unselect);
					}
				}
			}
    	});
        
        player_revLayout = new LinearLayout(context);
        player_revLayout.setId(player_revLayoutID);
        player_revLayout.setOrientation(LinearLayout.VERTICAL);
        player_revLayout.setGravity(Gravity.CENTER);
        linearmenuLayout.addView(player_revLayout, menuButtonLayoutParams);
        
        player_revImage = new ImageButton(context);
        player_revImage.setFocusable(false);
        player_revImage.setId(player_revImageID);
        player_revImage.setBackgroundResource(R.drawable.pre_unselect_gray);
        player_revText = new TextView(context);
        player_revText.setGravity(Gravity.CENTER);
        player_revText.setId(player_revTextID);
        player_revText.setText(getResources().getString(R.string.str_player_rev));
        player_revText.setTextSize((int) (TextSize));
        player_revLayout.addView(player_revImage, menuImageLayoutParams);
        player_revLayout.addView(player_revText, linearmenuLayoutParams);
        
        player_revImage.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
            	parentDialog.OnkeyPlayRev();
            }
        });
        
        player_revImage.setOnFocusChangeListener(new OnFocusChangeListener()
    	{
			@Override
			public void onFocusChange(View arg0, boolean arg1)
			{
				// TODO Auto-generated method stub
				if(arg1)
				{
					if(revpicture == -1)
					{
						player_revImage.setBackgroundResource(R.drawable.pre_selected);
					}
					else if(revpicture == 1)
					{
						player_revImage.setBackgroundResource(R.drawable.pre_selected2);
					}
					else if(revpicture == 2)
					{
						player_revImage.setBackgroundResource(R.drawable.pre_selected4);
					}
					else if(revpicture == 3)
					{
						player_revImage.setBackgroundResource(R.drawable.pre_selected8);
					}
					else if(revpicture == 4)
					{
						player_revImage.setBackgroundResource(R.drawable.pre_selected16);
					}
					else
					{
						player_revImage.setBackgroundResource(R.drawable.pre_selected);
					}	
				}
				else
				{
					if(revpicture == -1)
					{
						arg0.setBackgroundResource(R.drawable.pre_unselect);
					}
					else if(revpicture == 1)
					{
						arg0.setBackgroundResource(R.drawable.pre_unselect2);
					}
					else if(revpicture == 2)
					{
						arg0.setBackgroundResource(R.drawable.pre_unselect4);
					}
					else if(revpicture == 3)
					{
						arg0.setBackgroundResource(R.drawable.pre_unselect8);
					}
					else if(revpicture == 4)
					{
						arg0.setBackgroundResource(R.drawable.pre_unselect16);
					}
					else
					{
						arg0.setBackgroundResource(R.drawable.pre_unselect);
					}
				}
			}
    	});
        
        player_ffLayout = new LinearLayout(context);
        player_ffLayout.setId(player_ffLayoutID);
        player_ffLayout.setOrientation(LinearLayout.VERTICAL);
        player_ffLayout.setGravity(Gravity.CENTER);
        linearmenuLayout.addView(player_ffLayout, menuButtonLayoutParams);
        
        player_ffImage = new ImageButton(context);
        player_ffImage.setFocusable(false);
        player_ffImage.setId(player_ffImageID);
        player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus_gray);
        player_ffText = new TextView(context);
        player_ffText.setTextSize((int) (TextSize));
        player_ffText.setGravity(Gravity.CENTER);
        player_ffText.setId(player_ffTextID);
        player_ffText.setText(getResources().getString(R.string.str_player_ff));
        player_ffLayout.addView(player_ffImage, menuImageLayoutParams);
        player_ffLayout.addView(player_ffText, linearmenuLayoutParams);
        
        player_ffImage.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
            	parentDialog.OnkeyPlayFF();
            }
        });
        
        player_ffImage.setOnFocusChangeListener(new OnFocusChangeListener()
    	{
			@Override
			public void onFocusChange(View arg0, boolean arg1)
			{
				// TODO Auto-generated method stub
				if(arg1)
				{
					if(ffpicture == -1)
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_focus);
					}
					else if(ffpicture == 1)
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_focus2);
					}
					else if(ffpicture == 2)
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_focus4);
					}
					else if(ffpicture == 3)
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_focus8);
					}
					else if(ffpicture == 4)
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_focus16);
					}
					else
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_focus);
					}
				}
				else
				{
					if(ffpicture == -1)
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus);
					}
					else if(ffpicture == 1)
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus2);
					}
					else if(ffpicture == 2)
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus4);
					}
					else if(ffpicture == 3)
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus8);
					}
					else if(ffpicture == 4)
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus16);
					}
					else
					{
						player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus);
					}
				}
			}
    	});
        
        player_timeLayout = new LinearLayout(context);
        player_timeLayout.setId(player_timeLayoutID);
        player_timeLayout.setOrientation(LinearLayout.VERTICAL);
        player_timeLayout.setGravity(Gravity.CENTER);
        linearmenuLayout.addView(player_timeLayout, menuButtonLayoutParams);
        
        player_timeImage = new ImageButton(context);
        player_timeImage.setFocusable(false);
        player_timeImage.setId(player_timeImageID);
        player_timeImage.setBackgroundResource(R.drawable.playertime_gray);
        player_timeText = new TextView(context);
        player_timeText.setTextSize((int) (TextSize));
        player_timeText.setGravity(Gravity.CENTER);
        player_timeText.setId(player_timeTextID);
        player_timeText.setText(getResources().getString(R.string.str_player_time));
        player_timeLayout.addView(player_timeImage, menuImageLayoutParams);
        player_timeLayout.addView(player_timeText, linearmenuLayoutParams);
        
        player_timeImage.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
            	onKeyGoToTimePlay();
            }
        });
        
        player_timeImage.setOnFocusChangeListener(new OnFocusChangeListener()
    	{
			@Override
			public void onFocusChange(View arg0, boolean arg1)
			{
				// TODO Auto-generated method stub
				if(arg1)
				{
					arg0.setBackgroundResource(R.drawable.player_time_focus);
				}
				else
				{
					arg0.setBackgroundResource(R.drawable.playertime);
				}
			}
    	});
        
        player_backwardLayout = new LinearLayout(context);
        player_backwardLayout.setId(player_backwardLayoutID);
        player_backwardLayout.setOrientation(LinearLayout.VERTICAL);
        player_backwardLayout.setGravity(Gravity.CENTER);
        linearmenuLayout.addView(player_backwardLayout, menuButtonLayoutParams);
        
        player_backwardImage = new ImageButton(context);
        player_backwardImage.setFocusable(false);
        player_backwardImage.setId(player_backwardImageID);
        player_backwardImage.setBackgroundResource(R.drawable.player_backward_gray);
        player_backwardText = new TextView(context);
        player_backwardText.setTextSize((int) (TextSize));
        player_backwardText.setGravity(Gravity.CENTER);
        player_backwardText.setId(player_backwardTextID);
        player_backwardText.setText(getResources().getString(R.string.str_player_forward));
        player_backwardLayout.addView(player_backwardImage, menuImageLayoutParams);
        player_backwardLayout.addView(player_backwardText, linearmenuLayoutParams);
        
        player_backwardImage.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
            	parentDialog.onBackward();
            }
        });
        
        player_backwardImage.setOnFocusChangeListener(new OnFocusChangeListener()
    	{
			@Override
			public void onFocusChange(View arg0, boolean arg1)
			{
				// TODO Auto-generated method stub
				if(arg1)
				{
					arg0.setBackgroundResource(R.drawable.player_backward_focus);
				}
				else
				{
					arg0.setBackgroundResource(R.drawable.player_backward);
				}
			}
    	});
        
        player_forwardLayout = new LinearLayout(context);
        player_forwardLayout.setId(player_forwardLayoutID);
        player_forwardLayout.setOrientation(LinearLayout.VERTICAL);
        player_forwardLayout.setGravity(Gravity.CENTER);
        linearmenuLayout.addView(player_forwardLayout, menuButtonLayoutParams);
        
        player_forwardImage = new ImageButton(context);
        player_forwardImage.setFocusable(false);
        player_forwardImage.setId(player_forwardImageID);
        player_forwardImage.setBackgroundResource(R.drawable.player_forward_gray);
        player_forwardText = new TextView(context);
        player_forwardText.setTextSize((int) (TextSize));
        player_forwardText.setGravity(Gravity.CENTER);
        player_forwardText.setId(player_forwardTextID);
        player_forwardText.setText(getResources().getString(R.string.str_player_backward));
        player_forwardLayout.addView(player_forwardImage, menuImageLayoutParams);
        player_forwardLayout.addView(player_forwardText, linearmenuLayoutParams);
        
        player_forwardImage.setOnClickListener(new OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {
            	parentDialog.onForward();
            }
        });
        
        player_forwardImage.setOnFocusChangeListener(new OnFocusChangeListener()
    	{
			@Override
			public void onFocusChange(View arg0, boolean arg1)
			{
				// TODO Auto-generated method stub
				if(arg1)
				{
					arg0.setBackgroundResource(R.drawable.player_forward_focus);
				}
				else
				{
					arg0.setBackgroundResource(R.drawable.player_forward);
				}
			}
    	});
        
        menuLayout.setBackgroundResource(R.drawable.bodyback);
        menuLayout.invalidate();
        frameLayout.addView(menuLayout, menuLayoutParams);
        
        lp4LoopAB = new android.widget.RelativeLayout.LayoutParams(0,TvPluginControler.getInstance().getPvrManager().dip2px(context, 23));
        lp4LoopAB.topMargin = 5;
	}

	public void UpdateUsbInfo(int percent,String usbName)
	{
		usbFreeSpace.setText(percent + "%");
        usb_status_progressbar.setProgress(percent);
        usbLabelName.setText(usbName);
	}
	
	public void SetRecordProgressbarMax(int max)
	{
		recordBar.setMax(max);
	}
	
	public int GetRecordProgressWidth()
	{
		return recordBar.getWidth();
	}
	
	public int GetRecordProgress()
	{
		return recordBar.getProgress();
	}
	
	public int GetRecordProgressMax()
	{
		return recordBar.getMax();
	}
	
	public void SetRecordTextProgressbar(String text, int progress)
	{
		recordBar.setTextProgress(text, progress);
	}
	
	public void SetCurrentTimeText(String text)
	{
		current_timeText.setText(text);
	}
	
	public void SetEventNameText(String text)
	{
		eventNameText.setText(text);
	}
	
	public void SetServiceNameText(String text)
	{
		serviceNameText.setText(text);
	}
	
	public void SetTotalRecordTimeText(String text)
	{
		record_timeText.setText(text);
	}
	
	public void SetABLoopProgress(int progress)
	{
		repeatBar.setProgress(progress);
	}
	
	public void SetABLoopProgressbarMax(int max)
	{
		repeatBar.setMax(max);
	}
	
	public void SetABLoopProgressWidth(int width)
	{
		lp4LoopAB.width = width;
	}
	
	public void SetABLoopProgressTopMargin(int topMargin)
	{
		lp4LoopAB.topMargin = topMargin;
	}
	
	public void SetABLoopProgressLeftMargin(int leftMargin)
	{
		lp4LoopAB.leftMargin = leftMargin;
	}
	
	public void SetABLoopProgressLayoutParams()
	{
		repeatBar.setLayoutParams(lp4LoopAB);
	}
	
	public void SetABLoopProgressGone()
	{
		repeatBar.setVisibility(View.GONE);
	}
	
	public void SetABLoopProgressVisibility()
	{
		repeatBar.setVisibility(View.VISIBLE);
	}
	
	public void setParentDialog(PvrTimeshitfDlg parentDialog)
	{
		this.parentDialog = parentDialog;
	}
	
	public void SetNotPlaybackUnfocus()
	{
		player_ABRepeatImage.setFocusable(false);
		player_revImage.setFocusable(false);
		player_ffImage.setFocusable(false);
		player_timeImage.setFocusable(false);
		player_backwardImage.setFocusable(false);
		player_forwardImage.setFocusable(false);
	}

	// 设置 player_pauseImage 按钮图标	
	public void setPlayer_PauseImage_playfocus()
	{
		//player_pauseImage.requestFocus();
		player_ABRepeatImage.setFocusable(false);
		player_revImage.setFocusable(false);
		player_ffImage.setFocusable(false);
		player_timeImage.setFocusable(false);
		player_backwardImage.setFocusable(false);
		player_forwardImage.setFocusable(false);
		player_pauseImage.setBackgroundResource(R.drawable.player_play_focus);
		player_pauseText.setText(getResources().getString(R.string.str_player_play));
	}
	
	public void SetPlayer_PauseImage_requestFocus()
	{
		player_pauseImage.requestFocus();
	}
	
	public void setPlayer_PauseImage_playunfocus()
	{
		//player_pauseImage.requestFocus();
		player_pauseImage.setBackgroundResource(R.drawable.player_play);
		player_pauseText.setText(getResources().getString(R.string.str_player_play));
	}
	
	public void setPlayer_PauseImage_pausefocus()
	{
		//player_pauseImage.requestFocus();
		Log.v("yangcheng", "setPlayer_PauseImage_pausefocus.enter");
		player_ABRepeatImage.setFocusable(true);
		player_ABRepeatImage.setBackgroundResource(R.drawable.abpaly_seta_unselect);
		player_revImage.setFocusable(true);
		player_revImage.setBackgroundResource(R.drawable.pre_unselect);
		player_ffImage.setFocusable(true);
		player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus);
		player_timeImage.setFocusable(true);
		player_timeImage.setBackgroundResource(R.drawable.playertime);
		player_backwardImage.setFocusable(true);
		player_backwardImage.setBackgroundResource(R.drawable.player_backward);
		player_forwardImage.setFocusable(true);
		player_forwardImage.setBackgroundResource(R.drawable.player_forward);
		player_pauseImage.setBackgroundResource(R.drawable.pausefocus);
		player_pauseText.setText(getResources().getString(R.string.str_player_pause));
	}
	
	public void setPlayer_PauseImage_pauseunfocus()
	{
		//player_pauseImage.requestFocus();

		player_pauseImage.setBackgroundResource(R.drawable.pauseunfocus);
		player_pauseText.setText(getResources().getString(R.string.str_player_pause));
	}
	
	public void setPlayer_revImagefocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_selected);
	}
	
	public void setPlayer_revImage1Xfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_selected1);
	}
	
	public void setPlayer_revImage2Xfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_selected2);
	}
	
	public void setPlayer_revImage4Xfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_selected4);
	}
	
	public void setPlayer_revImage8Xfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_selected8);
	}
	
	public void setPlayer_revImage16Xfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_selected16);
	}	
	
	public void setPlayer_revImageunfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_unselect);
	}
	
	public void setPlayer_revImage1xunfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_unselect1);
	}
	
	public void setPlayer_revImage2xunfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_unselect2);
	}
	
	public void setPlayer_revImage4xunfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_unselect4);
	}
	
	public void setPlayer_revImage8xunfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_unselect8);
	}
	
	public void setPlayer_revImage16xunfocus()
	{
		player_revImage.setBackgroundResource(R.drawable.pre_unselect16);
	}
	
	public void setPlayer_ffImagefocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_focus);
	}
	
	public void setPlayer_ffImage1Xfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_focus1);
	}
	
	public void setPlayer_ffImage2Xfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_focus2);
	}
	
	public void setPlayer_ffImage4Xfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_focus4);
	}
	
	public void setPlayer_ffImage8Xfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_focus8);
	}
	
	public void setPlayer_ffImage16Xfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_focus16);
	}
			
	public void setPlayer_ffImageunfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus);
	}
	
	public void setPlayer_ffImage1Xunfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus1);
	}
	
	public void setPlayer_ffImage2Xunfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus2);
	}
	
	public void setPlayer_ffImage4Xunfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus4);
	}
	
	public void setPlayer_ffImageun8Xfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus8);
	}
	
	public void setPlayer_ffImage16Xunfocus()
	{
		player_ffImage.setBackgroundResource(R.drawable.player_ff_unfocus16);
	}
	
	public void setPlayer_ABRepeatImageSetAFocus()
	{
		ablooppicture = -1;
		player_ABRepeatImage.setBackgroundResource(R.drawable.abpaly_seta_select);
	}
	
	public void setPlayer_ABRepeatImageSetAUnFocus()
	{
		ablooppicture = -1;
		player_ABRepeatImage.setBackgroundResource(R.drawable.abpaly_seta_unselect);
	}
	
	public void setPlayer_ABRepeatImageSetBFocus()
	{
		ablooppicture = 1;
		player_ABRepeatImage.setBackgroundResource(R.drawable.abpaly_setb_select);
	}
	
	public void setPlayer_ABRepeatImageSetBUnFocus()
	{
		ablooppicture = 1;
		player_ABRepeatImage.setBackgroundResource(R.drawable.abpaly_setb_unselect);
	}
	
	public void setPlayer_ABRepeatImageSetExitABFocus()
	{
		ablooppicture = 2;
		player_ABRepeatImage.setBackgroundResource(R.drawable.abpaly_exitab_select);
	}
	
	public void setPlayer_ABRepeatImageSetExitABUnFocus()
	{
		ablooppicture = 2;
		player_ABRepeatImage.setBackgroundResource(R.drawable.abpaly_exitab_unselect);
	}
	
	public void setPlayer_ABRepeatImageSetAFocusAb3()
	{
		Log.v("yangcheng", "PlayBackProgress::handler::setPlayer_ABRepeatImageSetAFocusAb3.enter");
		ablooppicture = 3;
		player_ABRepeatImage.setBackgroundResource(R.drawable.abpaly_seta_select);
	}
	
	public void setPlayer_ABRepeatImageSetAUnFocusAb3()
	{
		ablooppicture = 3;
		player_ABRepeatImage.setBackgroundResource(R.drawable.abpaly_seta_unselect);
	}
	
	public void OnHandleExit(PVR_MODE curPvrMode)
	{
		if(curPvrMode != PVR_MODE.E_PVR_MODE_NONE)
		{
			final TvToastFocusDialog stopDlg = new TvToastFocusDialog();
			stopDlg.setOnBtClickListener(new OnBtClickListener()
			{
				public void onClickListener(boolean flag)
				{
					// TODO Auto-generated method stub
					stopDlg.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);
					
					if(flag)
					{
						parentDialog.OnHandleExitokButton();
						Log.e(TAG, "lijinjin OnHandleExit()");
						parentDialog.dismissUI();
					}
					else
					{
						
					}
				}				
			});
			
			String msg = "";
			if(curPvrMode==PVR_MODE.E_PVR_MODE_RECORD)
			{
				msg = getResources().getString(R.string.str_pvr_record_exit_confirm);
			}
			else if(curPvrMode==PVR_MODE.E_PVR_MODE_TIME_SHIFT)
			{
				msg = getResources().getString(R.string.str_pvr_timeshift_exit_confirm);
			}
			else
			{
				msg = getResources().getString(R.string.str_pvr_program_saving);
			}
			
			stopDlg.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW,
					new TvToastFocusData("", "", msg, 2));			
		}
		else
		{
		}
	}
	
	public void OnHandleRemoveDist()
	{
		parentDialog.OnHandleExitokButton();
		parentDialog.dismissUI();
	}
	
	public void OnHandleUnSupportDist()
	{
		final TvToastFocusDialog unSupportDistDlg = new TvToastFocusDialog();
		unSupportDistDlg.setOnBtClickListener(new OnBtClickListener()
		{
			public void onClickListener(boolean flag)
			{
				// TODO Auto-generated method stub
				unSupportDistDlg.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);
				parentDialog.dismissUI();
			}				
		});
		
		String msg = getResources().getString(R.string.sky_pvr_support_disk_format);
		
		unSupportDistDlg.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW,
				new TvToastFocusData("", "", msg, 1));
	}
	
	public void OnNoDist()
	{
		final TvToastFocusDialog unSupportDistDlg = new TvToastFocusDialog();
		unSupportDistDlg.setOnBtClickListener(new OnBtClickListener()
		{
			public void onClickListener(boolean flag)
			{
				// TODO Auto-generated method stub
				unSupportDistDlg.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);
				parentDialog.dismissUI();
			}				
		});
		
		String msg = getResources().getString(R.string.str_pvr_insert_usb);
		
		unSupportDistDlg.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW,
				new TvToastFocusData("", "", msg, 1));
	}
	
	public void OnRecordFalse(PVR_MODE curPvrMode, EnumPvrRecordStatus pvrStatus)
	{
		final TvToastFocusDialog RecordFalseDlg = new TvToastFocusDialog();
		RecordFalseDlg.setOnBtClickListener(new OnBtClickListener()
		{
			public void onClickListener(boolean flag)
			{
				// TODO Auto-generated method stub
				RecordFalseDlg.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);
				parentDialog.dismissUI();
			}				
		});
		
		String msg = "";
		if(EnumPvrRecordStatus.E_ERROR_OUT_OF_DISK_SPACE == pvrStatus)
		{
			msg = context.getResources().getString(R.string.sky_pvr_disk_outofspace);
		}
		else if(EnumPvrRecordStatus.E_ERROR_UNSUPPORTED_FILE_SYSTEM == pvrStatus)
		{
			msg = context.getResources().getString(R.string.sky_pvr_filesystem_unsupported);		
		}
		else if(EnumPvrRecordStatus.E_ERROR_READ_ONLY_FILE_SYSTEM == pvrStatus)
		{
			msg = context.getResources().getString(R.string.sky_pvr_filesystem_unsupported);		
		}
		else if(EnumPvrRecordStatus.E_ERROR_CI_PLUS_UNSUPPORT == pvrStatus)
		{
			if(curPvrMode == PVR_MODE.E_PVR_MODE_TIME_SHIFT)
			{
				msg = context.getResources().getString(R.string.sky_pvr_support_timeshift_fail);				
			}
			else if(curPvrMode == PVR_MODE.E_PVR_MODE_RECORD)
			{
				msg = context.getResources().getString(R.string.sky_pvr_support_record_fail);			
			}			
		}
		else if(EnumPvrRecordStatus.E_ERROR == pvrStatus)
		{
			if(curPvrMode == PVR_MODE.E_PVR_MODE_TIME_SHIFT)
			{
				msg = context.getResources().getString(R.string.sky_pvr_timeshift_false);				
			}
			else if(curPvrMode == PVR_MODE.E_PVR_MODE_RECORD)
			{
				msg = context.getResources().getString(R.string.sky_pvr_record_false);				
			}			
		}
		
		RecordFalseDlg.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW,
				new TvToastFocusData("", "", msg, 1));
	}
	
	public void showmenu(boolean flag)
	{
		if(flag)
		{
			menuLayout.setVisibility(View.VISIBLE);
		}
		else
		{
			menuLayout.setVisibility(View.GONE);
		}
	}
	
	public void SetRecordStatusLayoutVisible(boolean flag)
	{
		if(flag)
		{
			recordStatusLayout.setVisibility(View.VISIBLE);
		}
		else
		{
			recordStatusLayout.setVisibility(View.GONE);
		}
	}
	
	public void player_stopImageRequestFocus()
	{
		player_stopImage.requestFocus();
	}
	
	public void player_ffImageRequestFocus()
	{
		player_ffImage.requestFocus();
	}
	
	public void player_revImageRequestFocus()
	{
		player_revImage.requestFocus();
	}
	
	public void player_backwardImageRequestFocus()
	{
		player_backwardImage.requestFocus();
	}
	
	public void player_forwardImageRequestFocus()
	{
		player_forwardImage.requestFocus();
	}
	
	public void onKeyGoToTimePlay()
	{
		playTimeDlg = new PvrPlaytimeDialog();
		playTimeDlg.setOnBtClickListener(new OnDlgClickListener()
		{
			@Override
			public void onClickListener(boolean flag)
			{
				// TODO Auto-generated method stub
				if(flag)
				{
					boolean jumpflag = parentDialog.onJumpPlaybackTime();
					if(jumpflag)
					{
						playTimeDlg.processCmd(TvConfigTypes.TV_DIALOG_PVRPLAYTIME, DialogCmd.DIALOG_DISMISS,"");
					}
					else
					{
						playTimeDlg.ClearValue();
					}
				}
				else
				{
					playTimeDlg.processCmd(TvConfigTypes.TV_DIALOG_PVRPLAYTIME, DialogCmd.DIALOG_DISMISS,"");
				}				
			}	
		});
		
		playTimeDlg.processCmd(TvConfigTypes.TV_DIALOG_PVRPLAYTIME, DialogCmd.DIALOG_SHOW,"");
	}
	
	public boolean onHandleOnkeydownStopRecord(PVR_MODE curPvrMode, final KeyEvent tEvent)
	{
		if(curPvrMode != PVR_MODE.E_PVR_MODE_NONE)
		{
			int keyCode = tEvent.getKeyCode();
			if ((keyCode != KeyEvent.KEYCODE_CHANNEL_UP) &&
					(keyCode != KeyEvent.KEYCODE_CHANNEL_DOWN) &&
					/*(keyCode != MKeyEvent.KEYCODE_CHANNEL_RETURN)&&*/
					(keyCode !=KeyEvent.KEYCODE_TV_INPUT) && 
					(keyCode !=KeyEvent.KEYCODE_HOME) && 
					(keyCode != KeyEvent.KEYCODE_BACK) &&
					(keyCode != KeyEvent.KEYCODE_ENTER_EPG) &&
					(keyCode != KeyEvent.KEYCODE_MEDIA_DELETE))
			{
				return false;        
			}
			
			final TvToastFocusDialog stopDlg = new TvToastFocusDialog();
			stopDlg.setOnBtClickListener(new OnBtClickListener()
			{
				public void onClickListener(boolean flag)
				{
					// TODO Auto-generated method stub
					stopDlg.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_DISMISS, null);
					
					if(flag)
					{
						parentDialog.CheckNeedToStopRecord(tEvent);
						
						//parentDialog.dismissUI();
					}
					else
					{
						
					}
				}				
			});
			
			String msg = "";
			if(curPvrMode==PVR_MODE.E_PVR_MODE_RECORD)
			{
				msg = getResources().getString(R.string.str_pvr_record_exit_confirm);
			}
			else if(curPvrMode == PVR_MODE.E_PVR_MODE_TIME_SHIFT)
			{
				msg = getResources().getString(R.string.str_pvr_timeshift_exit_confirm);
			}
			else if(curPvrMode == PVR_MODE.E_PVR_MODE_PLAYBACK)
			{
				msg = getResources().getString(R.string.str_info_pvr_playback_exit_confirm);
			}
			
			stopDlg.processCmd(TvConfigTypes.TV_DIALOG_TOAST_FOCUS, DialogCmd.DIALOG_SHOW,
					new TvToastFocusData("", "", msg, 2));			
		}
		else
		{
		}
		
		return true;
	}

	@Override
	public boolean onShowDialogDone(int ID) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int onDismissDialogDone(Object... obj) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void OnKeyDownListener(int keyCode) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocus(int index, int keycode)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnPage(int keycode, int index) 
	{
		// TODO Auto-generated method stub
		
	}
	
	public void setTitle(PVR_MODE curPvrMode)
	{
		if(curPvrMode==PVR_MODE.E_PVR_MODE_RECORD)
		{
			statuTitle=getResources().getString(R.string.str_pvr_mode_recorde);
		}
		else if(curPvrMode==PVR_MODE.E_PVR_MODE_TIME_SHIFT)
		{
			statuTitle=getResources().getString(R.string.str_pvr_mode_timeshift);
		}
		 record_statusText.setText(statuTitle);
	}
}

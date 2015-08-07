package com.tv.ui.EPGManage;

import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvEpgSchedulesData;
import com.tv.framework.data.TvStandardTime;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.EPGManage.EPGScheduleListItemView.OnItemKeyLister;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class EPGScheduleListView extends LinearLayout implements OnItemKeyLister{

	private Context mContext;
	private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dipDiv = TvUIControler.getInstance().getDipDiv();
    private LinearLayout overallLayout;
    private LinearLayout typeLayout;
    private TextView chlistTextView,programTextView,setmodeTextView,dateTextView,timeTextView,curTimeTextView;
    private LinearLayout titleLayout = null;
	private TextView titleTextView = null;
	private ImageView titleImageView = null;
    private ScrollView bodyScrollview;
    private LinearLayout bodyLayout;
    private ArrayList<EPGScheduleListItemView> epgschedulelistItemView = new ArrayList<EPGScheduleListItemView>();
    private ImageView epgscheduleItemDivider;
    private EPGScheduleListDialog parentDialog = null;
    
	public EPGScheduleListView(Context context) {
		super(context);
		mContext = context;
        this.setOrientation(LinearLayout.VERTICAL);
        
        overallLayout = new LinearLayout(context);
        overallLayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(overallLayout, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        
        titleLayout = new LinearLayout(context);
		titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		titleLayout.setPadding((int)(25/div), (int)(5/div), 0, (int)(5/div));
		titleLayout.setGravity(Gravity.CENTER_VERTICAL);
		titleLayout.setBackgroundColor(Color.parseColor("#3598DC"));
		titleImageView = new ImageView(context);
		titleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.tv_string_setting));
		LayoutParams titleImgParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleImgParams.topMargin = (int)(15/div);
		titleImageView.setLayoutParams(titleImgParams);
		titleLayout.addView(titleImageView);
		titleTextView = new TextView(context);
		titleTextView.setTextSize((int)(52/dipDiv));
		titleTextView.setTextColor(Color.WHITE);
		titleTextView.setText(context.getResources().getString(R.string.epgtipsschedule));
		titleTextView.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams titletxtParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		titletxtParams.leftMargin = (int)(25/div);
		titletxtParams.gravity = Gravity.CENTER_VERTICAL;
		titleTextView.setLayoutParams(titletxtParams);
		titleLayout.addView(titleTextView);
		LinearLayout tipLayout = new LinearLayout(context);
		tipLayout.setOrientation(LinearLayout.VERTICAL);
		titleLayout.addView(tipLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		curTimeTextView = new TextView(context);
		curTimeTextView.setTextSize((int)(32/dipDiv));
		curTimeTextView.setTextColor(Color.WHITE);
		curTimeTextView.setGravity(Gravity.CENTER);
		TvStandardTime time = TvPluginControler.getInstance().getCommonManager().getCurTimer();
		curTimeTextView.setText(String.format("%4d/%02d/%02d %2d:%02d", time.year, time.month+1, time.monthDay,
				time.hour, time.minute));
		tipLayout.addView(curTimeTextView, new LayoutParams(LayoutParams.MATCH_PARENT, (int)(70/div)));
		LinearLayout tip = new LinearLayout(context);
		tip.setOrientation(LinearLayout.HORIZONTAL);
		tip.setGravity(Gravity.CENTER);
		tipLayout.addView(tip, new LayoutParams(LayoutParams.MATCH_PARENT, (int)(70/div)));
		TextView redTextView = new TextView(context);
		redTextView.setTextSize((int)(32/dipDiv));
		redTextView.setText(context.getString(R.string.str_epg_schedule_r_delete));
		redTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.channellist_ic_red, 0, 0, 0);
		redTextView.setCompoundDrawablePadding((int)(5/div));
		redTextView.setTextColor(Color.WHITE);
		redTextView.setGravity(Gravity.CENTER);
		LayoutParams redParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		redParams.rightMargin = (int)(15/div);
		tip.addView(redTextView, redParams);
		TextView yellowTextView = new TextView(context);
		yellowTextView.setTextSize((int)(32/dipDiv));
		yellowTextView.setText(context.getString(R.string.str_epg_schedule_y_back));
		yellowTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.channellist_ic_yellow, 0, 0, 0);
		yellowTextView.setCompoundDrawablePadding((int)(5/div));
		yellowTextView.setTextColor(Color.WHITE);
		yellowTextView.setGravity(Gravity.CENTER);
		tip.addView(yellowTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		overallLayout.addView(titleLayout, new LayoutParams((int)(1200/div), (int)(140/div)));
        
		typeLayout = new LinearLayout(context);
        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
        typeLayout.setBackgroundColor(Color.parseColor("#323232"));
        overallLayout.addView(typeLayout, (int)(1200/div) , (int)(100/div));
        
        chlistTextView = initTextView(context.getString(R.string.epgchannellist), (int) (38 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(chlistTextView, (int)(300/div) , (int)(100/div));
        
        programTextView = initTextView(context.getString(R.string.epgprogramname), (int) (38 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(programTextView, (int)(300/div) , (int)(100/div));
        
        setmodeTextView = initTextView(context.getString(R.string.epgsettingmode), (int) (38 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(setmodeTextView, (int)(200/div) , (int)(100/div));
        
        dateTextView = initTextView(context.getString(R.string.epgchanneldate), (int) (38 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(dateTextView, (int)(200/div) , (int)(100/div));
        
        timeTextView = initTextView(context.getString(R.string.epgsettingtime), (int) (38 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(timeTextView, (int)(200/div) , (int)(100/div));
        
        bodyScrollview = new ScrollView(mContext);
        bodyScrollview.setBackgroundColor(Color.parseColor("#191919"));
        bodyScrollview.setVerticalScrollBarEnabled(true);
        bodyScrollview.setHorizontalScrollBarEnabled(false);
        bodyScrollview.setScrollbarFadingEnabled(true);
        bodyScrollview.setAlwaysDrawnWithCacheEnabled(true);
        bodyScrollview.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        bodyLayout = new LinearLayout(mContext);
        bodyLayout.setOrientation(LinearLayout.VERTICAL);
        bodyScrollview.addView(bodyLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));		
        overallLayout.addView(bodyScrollview, (int)(1200/div) , (int)(945/div));
	}

	public void updateView() {
		ArrayList<TvEpgSchedulesData> scheduleList = TvPluginControler.getInstance().getEpgManager().getScheduleListdata();
		if(scheduleList != null && scheduleList.size() <= 0){
			bodyLayout.removeAllViews();
			epgschedulelistItemView.clear();
			TvUIControler.getInstance().showMniToast("\rNo Schedule!\r");
			return;
		}
		int index = 0;
		bodyLayout.removeAllViews();
		epgschedulelistItemView.clear();
		for(TvEpgSchedulesData scheduleListItem : scheduleList)
		{
			EPGScheduleListItemView view = new EPGScheduleListItemView(mContext,index,this);
			view.InitScheduleData(scheduleListItem.channelnumber, scheduleListItem.channelname,
					scheduleListItem.programename, scheduleListItem.mode, scheduleListItem.date, scheduleListItem.time);
			bodyLayout.addView(view, LayoutParams.MATCH_PARENT, (int) (80 / div));
			
			epgscheduleItemDivider = new ImageView(mContext);
			epgscheduleItemDivider.setBackgroundResource(R.drawable.setting_line);
			bodyLayout.addView(epgscheduleItemDivider, new LayoutParams(LayoutParams.MATCH_PARENT,(int) (1 / div)));
			
			epgschedulelistItemView.add(view);
			index ++;
		}
		
		if(epgschedulelistItemView.size() > 0)
		{
			epgschedulelistItemView.get(0).resetFocus();
		}
	}

	private TextView initTextView(String str, int textSize, int textColor, int gravity)
    {
        TextView text = new TextView(mContext);
        text.setText(str);
        text.setTextSize(textSize);
        text.setTextColor(textColor);
        text.setGravity(gravity);
        return text;
    }

	public void setParentDialog(EPGScheduleListDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	@Override
	public void onItemKeyDownLister(int keyCode, int index) {
		// TODO Auto-generated method stub
		Log.i("gky", getClass().getName() + "-->onItemKeyDownLister keyCode is "+keyCode
				+" index is "+index);
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_PROG_YELLOW:
				if(parentDialog != null)
					parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);			
				break;
			case KeyEvent.KEYCODE_PROG_RED:
				if(index != -1){
					boolean ret = TvPluginControler.getInstance().getEpgManager().delEpgEvent(index);
					if(ret){
						updateView();
						TvUIControler.getInstance().showMniToast("\rDelete Successfully!\r");
					}else {
						TvUIControler.getInstance().showMniToast("\rDelete Failed!\r");
					}
				}
			default:
				break;
		}
	}
}

package com.tv.ui.Pvrsetting;

import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecordListItemView extends RelativeLayout implements OnTouchListener, OnKeyListener
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip = TvUIControler.getInstance().getDipDiv();
	private ImageView checkedView;
	private TextView lcnText;
    private TextView channelText;
    private TextView programText;
    private Context mContext;
    
    private boolean isItemCheck = false;
    
    private static final int CHECK_ID = 1000;
    private static final int LCN_ID = 1001;
    private static final int CHANNEL_ID = 1002;
    
    private PVRChannelListView parentView;

    public RecordListItemView(Context context, PVRChannelListView parentView)
    {
        super(context);
        this.setOnKeyListener(this);
        mContext = context;
        this.parentView = parentView;
        initViews();
    }

    private void initViews()
    {
    	RelativeLayout.LayoutParams mainLayout = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(mainLayout);
        
        RelativeLayout.LayoutParams checkedViewParams = new RelativeLayout.LayoutParams((int) (50 / div), (int) (50 / div));
        checkedViewParams.rightMargin = (int) (20 / div);
        checkedViewParams.leftMargin = (int) (20 / div);
        checkedViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        checkedView = new ImageView(mContext);
        checkedView.setId(CHECK_ID);
        this.addView(checkedView, checkedViewParams);
        
        RelativeLayout.LayoutParams lcnTextParams = new RelativeLayout.LayoutParams(
                (int)(240 / div), LayoutParams.WRAP_CONTENT);
        lcnTextParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        lcnTextParams.addRule(RelativeLayout.RIGHT_OF, checkedView.getId());
        lcnTextParams.addRule(RelativeLayout.LEFT_OF);
        lcnTextParams.leftMargin = (int)(30 / div);
        lcnText = new TextView(mContext);
        lcnText.setTextColor(Color.WHITE);
        lcnText.setTextSize(36 / dip);
        lcnText.setId(LCN_ID);
        this.addView(lcnText, lcnTextParams);
        
        RelativeLayout.LayoutParams channelTextParams = new RelativeLayout.LayoutParams(
        		(int)(420 / div), LayoutParams.WRAP_CONTENT);
        channelTextParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        channelTextParams.addRule(RelativeLayout.RIGHT_OF, lcnText.getId());
        channelText = new TextView(mContext);
        channelText.setTextColor(Color.WHITE);
        channelText.setTextSize(36 / dip); 
        channelText.setId(CHANNEL_ID);
        channelText.setEllipsize(TruncateAt.MARQUEE);
        channelText.setFocusable(true);
        channelText.setFocusableInTouchMode(true);
        channelText.setMarqueeRepeatLimit(-1);
        channelText.setHorizontallyScrolling(true);
        channelText.setSingleLine(true);
        this.addView(channelText, channelTextParams);
        
        RelativeLayout.LayoutParams programTextParams = new RelativeLayout.LayoutParams(
        		(int)(520 / div), LayoutParams.WRAP_CONTENT);
        programTextParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        programTextParams.addRule(RelativeLayout.RIGHT_OF, channelText.getId());
        programText = new TextView(mContext);
        programText.setTextColor(Color.WHITE);
        programText.setTextSize(36 / dip); 
        programText.setEllipsize(TruncateAt.MARQUEE);
        programText.setFocusable(true);
        programText.setFocusableInTouchMode(true);
        programText.setMarqueeRepeatLimit(-1);
        programText.setHorizontallyScrolling(true);
        programText.setSingleLine(true);
        this.addView(programText, programTextParams);
        
        if (isItemCheck)
        {
            checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
        } else
        {
            checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
        }
        
        this.setFocusable(true);
        this.setClickable(true);
    }

    public void update(String textStr, String textStr2, String textStr3)
    {
    	lcnText.setText(textStr);
    	channelText.setText(textStr2);
    	programText.setText(textStr3);
    }
    
    public void setBackGroundColorUser(boolean hasFocus)
    {
		// TODO Auto-generated method stub
		if (hasFocus)
        {
			this.setBackgroundColor(Color.rgb(53,152,220));       	
            hasFocus = true;
        } 
		else
        {
        	this.setBackgroundColor(Color.rgb(25, 26, 28));
            hasFocus = false;
        }
	}
    
    public void setChecked(boolean isChecked)
    {
        isItemCheck = isChecked;
        setCurPlayItem();
    }

    public boolean getchecked()
    {
        return isItemCheck;
    }
    
    public void setCurPlayItem()
    {
        if (isItemCheck)
        {
            checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
        } else
        {
//            if (hasFocus)
//            {
//                checkedView.setImageResource(R.drawable.leftlist_souce_focus_icon);
//            } else
//            {
//                checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
//            }
        	checkedView.setImageResource(R.drawable.leftlist_souce_focus_icon);
        }
    }
    
    @Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
    	if(event.getAction() == KeyEvent.ACTION_DOWN)
    	{
    		parentView.onKey(view,keyCode, null);
    	}
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}

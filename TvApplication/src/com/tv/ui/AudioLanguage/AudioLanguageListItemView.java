package com.tv.ui.AudioLanguage;

import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvContext;
import com.tv.framework.plugin.TvPluginManager;
import com.tv.framework.plugin.tvfuncs.CommonManager;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvMenuConfigDefs;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnClickListener;

public class AudioLanguageListItemView extends RelativeLayout implements OnKeyListener, OnFocusChangeListener,OnClickListener{
	
	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
    private ImageView checkedView;
    private ImageView leftView;
    private ImageView rightView;
    private TextView textView;
    private TextView CurrentMode;
    
    private int ID_checkedView=1;
    private int ID_textView=2;  
    private int ID_leftView=3;
    private int ID_rightView=4;
    private int ID_CurrentMode=5;
    
    boolean checked=false;
    int AudioMode=0;
    
    private onKeyDownListener keyDownListener;
    public  int index;
	public String LR[]={TvContext.context.getString(R.string.TV_CFG_AUDIO_LANGUAGE_SETTING_AUDIO_MODE_LR),
						TvContext.context.getString(R.string.TV_CFG_AUDIO_LANGUAGE_SETTING_AUDIO_MODE_LL),
						TvContext.context.getString(R.string.TV_CFG_AUDIO_LANGUAGE_SETTING_AUDIO_MODE_RR)};
    public AudioLanguageListItemView(Context context,onKeyDownListener keyDownListener, int index) {
		super(context);
		// TODO Auto-generated constructor stub
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setClickable(true);
        this.setOnKeyListener(this);
        this.setOnClickListener(this);
        this.setOnFocusChangeListener(this);
        this.setEnabled(true);
        this.keyDownListener = keyDownListener;
        this.index = index;
		RelativeLayout.LayoutParams mainLayout = new RelativeLayout.LayoutParams(
                (int)((TvMenuConfigDefs.setting_item_w+100)/div), LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(mainLayout);
        
        RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(
                (int)(45/div), (int)(45/div));
        imageViewParams.rightMargin = (int) (20 / div);
        imageViewParams.leftMargin = (int) (50 / div);
        imageViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        
        checkedView = new ImageView(context);
        checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
        checkedView.setId(ID_checkedView);
        this.addView(checkedView, imageViewParams);
        
        
        
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
        		(int)(350/div), LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        textViewParams.addRule(RelativeLayout.RIGHT_OF, checkedView.getId());
        textViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //textViewParams.leftMargin = (int) (80 / div);
        textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(36 / dip);
        textView.setSingleLine(true);
        textView.setEllipsize(TruncateAt.MARQUEE);
        textView.setMarqueeRepeatLimit(-1);
        textView.setId(ID_textView);
        this.addView(textView, textViewParams);
        
        
        RelativeLayout.LayoutParams leftViewParams = new RelativeLayout.LayoutParams(
                (int)(21/div), (int)(21/div));
        leftViewParams.rightMargin = (int) (20 / div);
        leftViewParams.leftMargin = (int) (20 / div);
        leftViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        leftViewParams.addRule(RelativeLayout.RIGHT_OF, textView.getId());
        leftViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        
        
        leftView = new ImageView(context);
        leftView.setImageResource(R.drawable.left_arrow);
        leftView.setId(ID_leftView);
        this.addView(leftView, leftViewParams);
        
        
        RelativeLayout.LayoutParams CurrentModeParams = new RelativeLayout.LayoutParams(
                (int)(200/div), LayoutParams.WRAP_CONTENT);
        CurrentModeParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        CurrentModeParams.addRule(RelativeLayout.RIGHT_OF, textView.getId());
        CurrentModeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        CurrentModeParams.leftMargin = (int) (40 / div); 
        CurrentMode = new TextView(context);
        CurrentMode.setId(ID_CurrentMode);
        CurrentMode.setTextColor(Color.WHITE);
        CurrentMode.setTextSize(36 / dip);
        CurrentMode.setGravity(Gravity.CENTER);
        CurrentMode.setSingleLine(true);
        CurrentMode.setEllipsize(TruncateAt.MARQUEE);
        CurrentMode.setMarqueeRepeatLimit(-1);
        this.addView(CurrentMode, CurrentModeParams);
        
        RelativeLayout.LayoutParams rightViewParams = new RelativeLayout.LayoutParams(
        		(int)(21/div), (int)(21/div));
        rightViewParams.rightMargin = (int) (20 / div);
        rightViewParams.leftMargin = (int) (20 / div);
        rightViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rightViewParams.addRule(RelativeLayout.RIGHT_OF, CurrentMode.getId());
        rightViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rightView = new ImageView(context);
        rightView.setImageResource(R.drawable.right_arrow);
        rightView.setId(ID_rightView);
        this.addView(rightView, rightViewParams);
        ArrayList list=TvPluginControler.getInstance().getCommonManager().getAudioMode();
        AudioMode=(Integer) list.get(index);
	}

    public void updateView(String data)
    {
    	textView.setText(data);
        CurrentMode.setText(LR[AudioMode]);
    }
    
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub	
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			keyDownListener.keyDownListener(keyCode, event);
			return true;
		}
		if(event.getAction()==KeyEvent.ACTION_DOWN)
		{
			if(this.checked==true)
			{
				if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
				{
					AudioMode--;
					if(AudioMode<0)
					{
						AudioMode=2;
					}
					TvPluginControler.getInstance().getCommonManager().setAudioMode(AudioMode);
					
				}
				else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
				{
					AudioMode++;
					if(AudioMode>2)
					{
						AudioMode=0;
					}
					TvPluginControler.getInstance().getCommonManager().setAudioMode(AudioMode);
				}
				updateAudio(AudioMode);
			}
		}
		return false;
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvListItemView::onFocusChange:hasFocus is " + hasFocus);
		if (hasFocus)
        {
			this.setBackgroundResource(R.drawable.yellow_sel_bg);
            //checkedView.setImageResource(R.drawable.leftlist_souce_focus_icon);
            textView.setTextColor(Color.BLACK);
            CurrentMode.setTextColor(Color.BLACK);
            leftView.setVisibility(View.VISIBLE);
            rightView.setVisibility(View.VISIBLE);
            
            this.invalidate();
        } else
        {
        	this.setBackgroundDrawable(null);
            //checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
            textView.setTextColor(Color.WHITE);
            CurrentMode.setTextColor(Color.WHITE);
            leftView.setVisibility(View.GONE);
            rightView.setVisibility(View.GONE);
            this.invalidate();
        }
	}
	
	public void setChecked(boolean flag)
	{
		if(flag)
		{
			checked=true;
			checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
		}
		else 
		{
			checked=false;
			checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
		}
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		setChecked(true);
		this.invalidate();
		keyDownListener.ItemClickListener(index);
	}
	
	public interface onKeyDownListener
	{
		public void keyDownListener(int keyCode, KeyEvent event);
		
		public void ItemClickListener(int index);
	}
	
	public void updateAudio(int index)
	{
		CurrentMode.setText(LR[index]);
	}
	
}

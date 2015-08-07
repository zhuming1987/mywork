package com.tv.ui.ChannelEdit;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvConfigTypes.EnumChOpType;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SearchView;


public class SortChannelView extends LinearLayout {
	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	private Context mContext;
	private LinearLayout titleLayout = null;
	private TextView titleTextView = null;
	public LinearLayout bodyLayout = null;
	public TextView  sortTypeFreq = null;
	private ImageView divider1 = null;
	private ImageView divider2 = null;
	public TextView sortTypeName = null;
	public TextView sortTypeEncrypt = null;
	public static int BYFREQ = 10;
	public static int BYNAME = 11;
	public static int BYENCRYPT = 12;
	
	public SortChannelView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams totalLayout = new LinearLayout.LayoutParams((int)(350/div), (int)(342/div));
		totalLayout.gravity = Gravity.BOTTOM;
		totalLayout.leftMargin = (int)(10 / div);
		totalLayout.bottomMargin = (int)(10 / div);
		this.setLayoutParams(totalLayout);
		this.setBackgroundColor(Color.parseColor("#191919"));
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		
		titleLayout = new LinearLayout(context);
		titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		titleLayout.setPadding((int)(25/div), (int)(5/div), 0, (int)(5/div));
		titleLayout.setGravity(Gravity.CENTER_VERTICAL);
		titleLayout.setBackgroundResource(R.drawable.title_bg_yellow);
		
		titleTextView = new TextView(context);
		titleTextView.setTextSize((int)(38/dip));
		titleTextView.setTextColor(Color.BLACK);
		titleTextView.setText(R.string.sortType);//context.getResources().getString(R.string.TV_CFG_CHANNEL_EDIT));
		titleTextView.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams titletxtParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		titletxtParams.leftMargin = (int)(25/div);
		titletxtParams.gravity = Gravity.CENTER_VERTICAL;
		titleTextView.setLayoutParams(titletxtParams);
		titleLayout.addView(titleTextView);
		this.addView(titleLayout, new LayoutParams((int)(350/div), (int)(100/div)));
		
		bodyLayout = new LinearLayout(context);
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		bodyLayout.setFocusable(true);
		bodyLayout.setFocusableInTouchMode(true);
		
//		StateListDrawable drawable = new StateListDrawable();
//		drawable.addState(new int[]{android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.yellow_sel_bg));
//		drawable.addState(new int[]{android.R.attr.state_focused, android.R.attr.state_selected, -android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.yellow_sel_bg));
//		drawable.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.yellow_sel_bg));
//	    drawable.addState(new int[]{android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.yellow_sel_bg));
//	     		
		sortTypeFreq = new TextView(context);
		sortTypeFreq.setId(BYFREQ);
		sortTypeFreq.setTextSize((float)(28/dip));
		sortTypeFreq.setTextColor(Color.WHITE);
		sortTypeFreq.setText(R.string.sortByFreq);
		sortTypeFreq.setSingleLine(true);
		sortTypeFreq.setFocusable(true);
		sortTypeFreq.setFocusableInTouchMode(true);
		sortTypeFreq.setEllipsize(TruncateAt.MARQUEE);
		sortTypeFreq.setMarqueeRepeatLimit(-1);
		sortTypeFreq.setGravity(Gravity.CENTER);
//		sortTypeFreq.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
		LayoutParams sortParams = new LayoutParams((int)(350/div), (int)(80/div));
		sortTypeFreq.setLayoutParams(sortParams);
		sortTypeFreq.setBackgroundResource(R.drawable.sortitem);
		bodyLayout.addView(sortTypeFreq);
		
		divider1 = new ImageView(context);
		divider1.setBackground(context.getResources().getDrawable(R.drawable.divider_line));
		bodyLayout.addView(divider1, new LayoutParams((int)(350/div), (int)(1/div)));
   
		sortTypeName = new TextView(context);
		sortTypeName.setId(BYNAME);
		sortTypeName.setTextSize((float)(28/dip));
		sortTypeName.setTextColor(Color.WHITE);
		sortTypeName.setText(R.string.sortByServiceName);
		sortTypeName.setSingleLine(true);
		sortTypeName.setEllipsize(TruncateAt.MARQUEE);
		sortTypeName.setMarqueeRepeatLimit(-1);
		sortTypeName.setFocusable(true);
		sortTypeName.setFocusableInTouchMode(true);
//		sortTypeName.setSelected(true);
		sortTypeName.setGravity(Gravity.CENTER);
		sortTypeName.setLayoutParams(sortParams);
		sortTypeName.setBackgroundResource(R.drawable.sortitem);
//		sortTypeName.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
		bodyLayout.addView(sortTypeName);
		
		divider2 = new ImageView(context);
		divider2.setBackground(context.getResources().getDrawable(R.drawable.divider_line));
		bodyLayout.addView(divider2, new LayoutParams((int)(350/div), (int)(1/div)));
		
		sortTypeEncrypt = new TextView(context);
		sortTypeEncrypt.setId(BYENCRYPT);
		sortTypeEncrypt.setTextSize((float)(28/dip));
		sortTypeEncrypt.setTextColor(Color.WHITE);
		sortTypeEncrypt.setFocusable(true);
		sortTypeEncrypt.setFocusableInTouchMode(true);
		sortTypeEncrypt.setText(R.string.sortByEncrypt);
		sortTypeEncrypt.setSingleLine(true);
		sortTypeEncrypt.setEllipsize(TruncateAt.MARQUEE);
		sortTypeEncrypt.setMarqueeRepeatLimit(-1);
//		sortTypeEncrypt.setSelected(true);
		sortTypeEncrypt.setGravity(Gravity.CENTER);
		sortTypeEncrypt.setLayoutParams(sortParams);
		sortTypeEncrypt.setBackgroundResource(R.drawable.sortitem);
//		sortTypeEncrypt.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
		bodyLayout.addView(sortTypeEncrypt);
		
		this.addView(bodyLayout, new LayoutParams((int)(350/div), (int)(300/div)));
	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			View curFocus = findFocus();
			Log.i("gky", "curFocus == " + curFocus);
			switch(event.getKeyCode())
			{
			case KeyEvent.KEYCODE_BACK:
				this.setVisibility(View.GONE);
				return true;
			default:
				break;
			}
		}
		return super.dispatchKeyEvent(event);
	}

}

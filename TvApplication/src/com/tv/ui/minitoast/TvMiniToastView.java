package com.tv.ui.minitoast;

import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TvMiniToastView extends LinearLayout{

	private TextView toastTextView;
	private Context mContext;
	
	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
	public TvMiniToastView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		toastTextView = new TextView(mContext);
        toastTextView.setBackgroundResource(R.drawable.minitoast_shape);
        int padding = (int)(10/div);
        toastTextView.setPadding(padding,padding,padding,padding);
        toastTextView.setTextSize((int)(36/dip));
        toastTextView.setTextColor(Color.WHITE);
        toastTextView.setGravity(Gravity.CENTER);
        this.addView(toastTextView);
	}
	
	public void updateView(String content){
		toastTextView.setText(content);
	}

}

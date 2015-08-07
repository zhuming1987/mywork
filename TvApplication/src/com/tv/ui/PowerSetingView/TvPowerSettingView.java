package com.tv.ui.PowerSetingView;

import java.util.ArrayList;

import com.tv.app.R;
import com.tv.framework.data.TvSetData;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvMenuConfigDefs;
import com.tv.ui.widgets.EnumItemView;
import com.tv.ui.widgets.EnumItemView.EnumItemListener;
import com.tv.ui.widgets.ProgressItemView;
import com.tv.ui.widgets.ProgressItemView.ProgressItemListener;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TvPowerSettingView extends LinearLayout{

	private TvPowerSettingDialog parentDialog;
	
	private LeftViewTitleLayout titleLayout;
	private LeftViewBodyLayout bodyLayout;
	private TextView logoTextView;
	private TextView musicTextView;
	private EnumItemView powerLogoEnumItemView;
	private ProgressItemView powerMusicVolumeProgressItemView;
	
	private final float resolution = (float)1.0;
	
	
	public TvPowerSettingView(Context context) {
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LayoutParams(TvMenuConfigDefs.setting_view_bg_w, 1080));
		this.setBackgroundResource(R.drawable.setting_bg);

		titleLayout = new LeftViewTitleLayout(context);
		titleLayout.setGravity(Gravity.CENTER);
		this.addView(titleLayout);
		bodyLayout = new LeftViewBodyLayout(context);
		bodyLayout.setPadding(50, 50, 50, 50);
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		this.addView(bodyLayout);
		
		LinearLayout linearLayout1 = new LinearLayout(context);
		linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout1.setGravity(Gravity.CENTER);
		bodyLayout.addView(linearLayout1);
		logoTextView = new TextView(context);
		logoTextView.setTextSize(36);
		logoTextView.setTextColor(Color.WHITE);
		powerLogoEnumItemView = new EnumItemView(context, 500, 60);
		powerLogoEnumItemView.setItemListener(powerLogoListener);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, 30, 0);
		linearLayout1.addView(logoTextView, lp);
		linearLayout1.addView(powerLogoEnumItemView);
		ImageView line1 = new ImageView(context);
		line1.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(
				(int) (790 / resolution), 1);
		lineLp.setMargins((int) (30 / resolution), 30,
				(int) (30 / resolution), 30);
		bodyLayout.addView(line1, lineLp);
		
		LinearLayout linearLayout2 = new LinearLayout(context);
		linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
		linearLayout2.setGravity(Gravity.CENTER);
		bodyLayout.addView(linearLayout2);
		musicTextView = new TextView(context);
		musicTextView.setTextSize(36);
		musicTextView.setTextColor(Color.WHITE);
		powerMusicVolumeProgressItemView = new ProgressItemView(context, 500, 60);
		powerMusicVolumeProgressItemView.setItemListener(powerMusicVolumeListener);
		linearLayout2.addView(musicTextView,lp);
		linearLayout2.addView(powerMusicVolumeProgressItemView);
		ImageView line2 = new ImageView(context);
		line2.setBackgroundResource(R.drawable.setting_line);
		bodyLayout.addView(line2, lineLp);
		
		powerLogoEnumItemView.requestFocus();
		
		
	}

	public void updateView(TvSetData data){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add("On");
		temp.add("Off");
		titleLayout.bindData("common_setting.png", "DemoMenu");
		logoTextView.setText("Logo");
		musicTextView.setText("Music");
		powerLogoEnumItemView.setBindData(0, temp);
		powerMusicVolumeProgressItemView.setBindData(0, 100, 50);
	}

	public TvPowerSettingDialog getParentDialog() {
		return parentDialog;
	}

	public void setParentDialog(TvPowerSettingDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	public EnumItemListener powerLogoListener = new EnumItemListener() {
		
		@Override
		public void onEnumItemChangeListener(String data, int index) {
			// TODO Auto-generated method stub
		}
	};
	
	public ProgressItemListener powerMusicVolumeListener = new ProgressItemListener() {
		
		@Override
		public void OnProgressItemChangeListener(int value) {
			// TODO Auto-generated method stub
		}
	};
}

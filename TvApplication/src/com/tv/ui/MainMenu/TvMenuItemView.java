package com.tv.ui.MainMenu;

import java.io.IOException;
import java.io.InputStream;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvUIControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.data.MenuItem;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvMenuConfigDefs;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.utils.LogicTextResource;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;

public class TvMenuItemView extends LinearLayout 
	implements OnFocusChangeListener, OnKeyListener{

	private Context context;
	private TextView txtView;
	private ImageView iconView;
	private LinearLayout.LayoutParams layoutParams;
	private OnMenuItemClickListener onMCListener;
	private OnMenuItemKeyListener onMKListener;
	private QuickKeyListener quickKeyListener;
	private int index;
	private TvMenuView parentView;
	private String ItemName;
	
	private final static float resolution = TvUIControler.getInstance().getResolutionDiv();
	private final static float dipDiv = TvUIControler.getInstance().getDipDiv();
	boolean[] factoryShow={false,false,false,false};
	boolean[] hotelShow={false,false,false,false};
	
	
	public TvMenuItemView(Context context, int index, TvMenuView parentView,
			OnMenuItemClickListener onMCListener, OnMenuItemKeyListener onMKListener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.index = index;
		this.onMCListener = onMCListener;
		this.onMKListener = onMKListener;
		this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
		this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setOnFocusChangeListener(this);
        this.setOnKeyListener(this);
        this.parentView = parentView;
        layoutParams = new LinearLayout.LayoutParams((int)(TvMenuConfigDefs.menu_item_w/resolution), 
        		(int)(TvMenuConfigDefs.menu_item_h/resolution));
        layoutParams.setMargins((int)(TvMenuConfigDefs.menu_item_left_padding/resolution), 
        		(int)(TvMenuConfigDefs.menu_item_top_padding/resolution), 
        		(int)(TvMenuConfigDefs.menu_item_left_padding/resolution), 
        		(int)(TvMenuConfigDefs.menu_item_top_padding/resolution));
        this.setLayoutParams(layoutParams);
        this.setBackgroundResource(R.drawable.gray_bg);
        
        iconView = new ImageView(context);
        LayoutParams iconLp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        iconLp.leftMargin = (int) (30/resolution);
        iconLp.topMargin = (int) (10/resolution);
        this.addView(iconView,iconLp);
        
        txtView = new TextView(context);
        txtView.setTextSize(TvMenuConfigDefs.menu_item_txt_size/dipDiv);
        txtView.getPaint().setAntiAlias(true);
        txtView.setTextColor(Color.WHITE);
        txtView.setGravity(Gravity.CENTER);
        txtView.setSingleLine(true);
        txtView.setEllipsize(TruncateAt.MARQUEE);
        txtView.setMarqueeRepeatLimit(-1);
        this.addView(txtView);
	}
	
	public void updateView(MenuItem data){
		//Log.i("gky", "TvMenuItemView::updateView");
		this.ItemName = data.getId();
		if(TvConfigDefs.TV_CFG_VGA_SETTING.equals(ItemName))
		{
			if(TvPluginControler.getInstance().getCommonManager().isPCorScart())
				txtView.setText(LogicTextResource.getInstance(TvContext.context).getText(data.getId()));
			else
				txtView.setText(TvContext.context.getResources().getString(R.string.TV_CFG_SCART_SETTING));
		}
		else{
			txtView.setText(LogicTextResource.getInstance(TvContext.context).getText(data.getId()));
		}
		iconView.setImageBitmap(loadBitmapFromAssets(context, data.getName()+".png"));;
	}

	//此会响应鼠标点击事件，进入二级菜单，菜单是不需要反人类的鼠标操作的
	/*@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvMenuItemView::onClick index is " + index
				+ " ItemName is " + ItemName);
		onMCListener.OnMenuItemClickListener(index, ItemName);
	}*/

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		Log.i("gky", "onFocusChange hasFocus is " + hasFocus);
        if (hasFocus)
        {
            this.setBackgroundResource(R.drawable.yellow_sel_bg);
            txtView.setTextColor(Color.BLACK);
            txtView.setSelected(true);
            //this.animate().scaleX(1.1f).scaleY(1.1f).setDuration(50);
        } else
        {
            this.setBackgroundResource(R.drawable.gray_bg);
            txtView.setTextColor(Color.WHITE);
            txtView.setSelected(false);
            //this.animate().scaleX(1.0f).scaleY(1.0f).setDuration(50);
        }
	}
	
	void factoryShow(int Keycode)
	{
		switch(Keycode)
		{
			case KeyEvent.KEYCODE_3:
				Log.i("wangxian", "case3");
				
				if(factoryShow[0] == false)
				{
					Log.v("wangxian", "case3 in");
					factoryShow[0] = true;
				}
				else
				{
					for(int i = 0; i < factoryShow.length; i++)
					{
						factoryShow[i] = false;
					}
				}
				break;
			case KeyEvent.KEYCODE_1:
				Log.i("wangxian","case1");
				
				if(factoryShow[0] == true && factoryShow[1] == false)
				{
					Log.i("wangxian", "case1 in");
					factoryShow[1] = true;
				}
				else
				{
					for(int i = 0; i < factoryShow.length; i++)
					{
						factoryShow[i] = false;
					}
				}
				break;
			case KeyEvent.KEYCODE_9:
				Log.i("wangxian","case9");
				
				if(factoryShow[0] == true && factoryShow[1] == true && factoryShow[2] == false)
				{
					Log.i("wangxian","case9 in");
					factoryShow[2] = true;
				}
				else
				{
					for(int i = 0; i < factoryShow.length; i++)
					{
						factoryShow[i] = false;
					}
				}
				break;
			case KeyEvent.KEYCODE_5:
				Log.i("wangxian","case5");
				
				if(factoryShow[0] == true && factoryShow[1] == true && factoryShow[2] == true && factoryShow[3] == false)
				{
					Log.i("wangxian","case5 in");
					factoryShow[3] = true;
					for(int i = 0; i < factoryShow.length; i++)
					{
						factoryShow[i] = false;
					}
					Log.i("wangxian","MainMenuActivity.java i want to start SkyFactoryActivity");
					Intent it = new Intent();
					it.setComponent(new ComponentName("com.skyworth.factorymenu","com.skyworth.factorymenu.SkyFactoryActivity"));
					it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					it.putExtra("StartScene", "Factory");
					TvContext.context.startActivity(it);
					this.parentView.getParentDialog().processCmd(TvConfigTypes.TV_DIALOG_MAIN_MENU, DialogCmd.DIALOG_DISMISS, null);
				}
				else
				{
					for(int i = 0; i < factoryShow.length; i++)
					{
						factoryShow[i] = false;
					}
				}
				break;
		}
	}
	
	public void HotelFunShow(int keycode)
	{
		switch(keycode)
		{
			case KeyEvent.KEYCODE_2:
				if(hotelShow[0]==false)
				{
					hotelShow[0]=true;
				}
				else
				{
					for(int i=0;i<hotelShow.length;i++)
					{
						hotelShow[i]=false;
					}
				}
				break;
			case KeyEvent.KEYCODE_5:
				if(hotelShow[0]==true&&hotelShow[1]==false)
				{
					hotelShow[1]=true;
				}
				else
				{
					for(int i=0;i<hotelShow.length;i++)
					{
						hotelShow[i]=false;
					}
				}
				break;
			case KeyEvent.KEYCODE_8:
				if(hotelShow[0]==true&&hotelShow[1]==true&&hotelShow[2]==false)
				{
					hotelShow[2]=true;
				}
				else
				{
					for(int i=0;i<hotelShow.length;i++)
					{
						hotelShow[i]=false;
					}
				}
				break;
			case KeyEvent.KEYCODE_0:
				if(hotelShow[0]==true&&hotelShow[1]==true&&hotelShow[2]==true&&hotelShow[3]==false)
				{
					hotelShow[3]=true;
					for(int i=0;i<hotelShow.length;i++)
					{
						hotelShow[i]=false;
					}
					Intent it = new Intent();
					it.setComponent(new ComponentName("com.skyworth.factorymenu","com.skyworth.factorymenu.SkyFactoryActivity"));
					it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					it.putExtra("StartScene", "Hotel");
					it.putExtra("Hotel", true);
					TvContext.context.startActivity(it);
					this.parentView.getParentDialog().processCmd(TvConfigTypes.TV_DIALOG_MAIN_MENU, DialogCmd.DIALOG_DISMISS, null);
				}
				else
				{
					for(int i=0;i<hotelShow.length;i++)
					{
						hotelShow[i]=false;
					}
				}
				break;
		}
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvMenuItemView::onKey keycode is " + keyCode);
		if(event.getAction() == KeyEvent.ACTION_UP) 
			return false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_1:
			factoryShow(KeyEvent.KEYCODE_1);
			return true;
		case KeyEvent.KEYCODE_3:
			factoryShow(KeyEvent.KEYCODE_3);
			return true;
		case KeyEvent.KEYCODE_5:
			factoryShow(KeyEvent.KEYCODE_5);
			HotelFunShow(KeyEvent.KEYCODE_5);
			return true;
		case KeyEvent.KEYCODE_9:
			factoryShow(KeyEvent.KEYCODE_9);
			return true;
		case KeyEvent.KEYCODE_2:
			HotelFunShow(KeyEvent.KEYCODE_2);
			return true;
		case KeyEvent.KEYCODE_8:
			HotelFunShow(KeyEvent.KEYCODE_8);
			return true;
		case KeyEvent.KEYCODE_0:
			HotelFunShow(KeyEvent.KEYCODE_0);
			return true;
		/*case KeyEvent.KEYCODE_MENU:*/
		case KeyEvent.KEYCODE_BACK:
			return onMKListener.onKeyDownExecute(keyCode, event);
		case KeyEvent.KEYCODE_DPAD_DOWN:
			onMKListener.onRequestFocus(index+1);
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			onMKListener.onRequestFocus(index-1);
			return true;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			onMCListener.OnMenuItemClickListener(index, ItemName);
			return true;
		case KeyEvent.KEYCODE_MENU://避免重复初始化主菜单,后续应将所有窗口类做成单利
			return true;
		case KeyEvent.KEYCODE_CHANNEL_DOWN:
			return true;
		case KeyEvent.KEYCODE_CHANNEL_UP:
			return true;
		}
		return quickKeyListener.onQuickKeyDownListener(keyCode, event);
	}
	
	public synchronized static Bitmap loadBitmapFromAssets(Context c, String image_path)
    {
        AssetManager assetManager = c.getAssets();
        if(resolution == 1.5f)
        	image_path = "720p/setting" + "/" + image_path;
        else 
        	image_path = "1080p/setting" + "/" + image_path;
        InputStream is = null;
        Bitmap bm = null;
        Log.e("gky", "image_path is " + image_path);
        try
        {
            is = assetManager.open(image_path);
            bm = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e)
        {
            Log.e("gky", "e is " + e.getMessage());
            e.printStackTrace();
        }

        return bm;
    }
	
	/**
	 * 菜单项单击回调接
	 * @author Administrator
	 *
	 */
	public interface OnMenuItemClickListener{
		/**
		 * 菜单项单击事件回调接
		 * @param ID
		 * @param name
		 */
		public void OnMenuItemClickListener(int index,String name);
	}

	/**
	 * 菜单项按键回调接
	 * @author Administrator
	 *
	 */
	public interface OnMenuItemKeyListener{
		/**
		 * 菜单项按键回调接口函
		 * @param keyCode
		 * @param event
		 * @return
		 */
		public boolean onKeyDownExecute(int keyCode, KeyEvent event);
		
		/**
		 * 菜单项Focus接口
		 * @param index
		 */
		public void onRequestFocus(int index);
	}
}

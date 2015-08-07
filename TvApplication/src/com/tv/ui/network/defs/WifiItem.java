/**
 * Copyright (C) 2012 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *           2013-12-16          heni
 *
 */

package com.tv.ui.network.defs;

import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class WifiItem extends RelativeLayout implements OnFocusChangeListener
{
    private Context context;
    private float div = TvUIControler.getInstance().getResolutionDiv();
    private ImageView selectIcon;
    private TextView wifiName;
    private ImageView encryptIcon;
    private ImageView wifiLevel;
    private ImageView rightArrow;
    private ImageView dividerLine;
    private RelativeLayout subLayoutFocus;
    private WifiItemListener wifiItemListener;
    private boolean canSendKeyevent = true; // 标识是否可转换键值，若两次之间间隔太近则丢弃
    private int keyInterval = 200; // 最快间隔多久发一次命令

    public WifiItem(Context context, WifiItemListener listener)
    {
        super(context);
        this.context = context;
        this.wifiItemListener = listener;
        createView();
        this.setFocusableInTouchMode(true);
        this.setOnClickListener(itemOnClickListener);
        this.setOnGenericMotionListener(itemOnGenericMotionListener);
    }

    public WifiItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        createView();
        this.setFocusableInTouchMode(true);
        this.setOnClickListener(itemOnClickListener);
        this.setOnGenericMotionListener(itemOnGenericMotionListener);
    }

    public interface WifiItemListener
    {
    	public boolean onWifiItemKeyDown(int keyCode, int itemID);
    }

    public void setWifiItemListener(WifiItemListener listener)
    {
        wifiItemListener = listener;
    }

    private void createView()
    {
        RelativeLayout.LayoutParams layoutLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (117 / div));
        RelativeLayout.LayoutParams wifi_icon_Lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams wifi_name_Lp = new RelativeLayout.LayoutParams(
                (int) (260 / div), RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams wifi_lock_Lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams wifi_level_Lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams wifi_right_Lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams divider_line_Lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (2 / div));
        RelativeLayout.LayoutParams subLayout_Lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (117 / div));

        wifi_icon_Lp.setMargins((int) (32 / div), 0, 0, 0);
        wifi_name_Lp.setMargins((int) (115 / div), 0, 0, 0);
        wifi_lock_Lp.setMargins((int) (380 / div), 0, 0, 0);
        wifi_level_Lp.setMargins((int) (445 / div), 0, 0, 0);
        wifi_right_Lp.setMargins((int) (510 / div), 0, 0, 0);

        wifi_icon_Lp.addRule(RelativeLayout.CENTER_VERTICAL);
        wifi_name_Lp.addRule(RelativeLayout.CENTER_VERTICAL);
        wifi_lock_Lp.addRule(RelativeLayout.CENTER_VERTICAL);
        wifi_level_Lp.addRule(RelativeLayout.CENTER_VERTICAL);
        wifi_right_Lp.addRule(RelativeLayout.CENTER_VERTICAL);
        divider_line_Lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        selectIcon = new ImageView(context);

        wifiName = new TextView(context);
        wifiName.setText("sky-srt");
        wifiName.setTextColor(Color.WHITE);
        wifiName.setTextSize(32 / TvUIControler.getInstance().getDipDiv());

        encryptIcon = new ImageView(context);
        encryptIcon.setVisibility(View.VISIBLE);
        wifiLevel = new ImageView(context);
        wifiLevel.setVisibility(View.VISIBLE);
        rightArrow = new ImageView(context);
        rightArrow.setImageResource(R.drawable.wifi_right_arrow);

        dividerLine = new ImageView(context);
        dividerLine.setImageResource(R.drawable.divider_line);
        dividerLine.setScaleType(ScaleType.FIT_XY);
        dividerLine.setPadding((int) (30 / div), 0, (int) (30 / div), 0);

        subLayoutFocus = new RelativeLayout(context);
        subLayout_Lp.setMargins((int) (40 / div), (int) (10 / div), (int) (40 / div),
                (int) (10 / div));

        subLayoutFocus.addView(selectIcon, wifi_icon_Lp);
        subLayoutFocus.addView(wifiName, wifi_name_Lp);
        subLayoutFocus.addView(encryptIcon, wifi_lock_Lp);
        subLayoutFocus.addView(wifiLevel, wifi_level_Lp);
        subLayoutFocus.addView(rightArrow, wifi_right_Lp);
        this.setFocusable(true);
        this.setOnFocusChangeListener(this);
        this.addView(dividerLine, divider_line_Lp);
        this.addView(subLayoutFocus, subLayout_Lp);
        this.setLayoutParams(layoutLp);
    }

    public void updateItemData(boolean _isSelect, String _wifiName, int _isEncrypt, int _wifiLevel)
    {
    	Log.i("wangxian", " " + _isSelect + " " + _wifiName + " " + _isEncrypt + " " + _wifiLevel);
        
    	if (_isSelect)
        {
            selectIcon.setImageResource(R.drawable.wifi_icon_sel);
        } 
    	else
        {
            selectIcon.setImageResource(R.drawable.wifi_icon_unsel);
        }
    	
        wifiName.setText(_wifiName);
        
        if (1 == _isEncrypt)
        {
            encryptIcon.setImageResource(R.drawable.wifi_lock);
        } 
        else if (0 == _isEncrypt)
        {
            encryptIcon.setImageDrawable(null);
        }
        
        switch (_wifiLevel)
        {
        case 0:
        	wifiLevel.setImageResource(R.drawable.wifi_level_0);
        	break;
        case 1:
            wifiLevel.setImageResource(R.drawable.wifi_level_1);
            break;
        case 2:
            wifiLevel.setImageResource(R.drawable.wifi_level_1);
            break;
        case 3:
            wifiLevel.setImageResource(R.drawable.wifi_level_2);
            break;
        case 4:
            wifiLevel.setImageResource(R.drawable.wifi_level_3);
            break;
        case 5:
            wifiLevel.setImageResource(R.drawable.wifi_level_3);
            break;
        default:
            break;
        }
    }

    public void updateOtherData()
    {
        selectIcon.setVisibility(View.GONE);
        wifiName.setText(context.getText(R.string.NET_WIFI_SELECT_OTHER_WIFI));
        encryptIcon.setVisibility(View.GONE);
        wifiLevel.setVisibility(View.GONE);
        rightArrow.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (hasFocus)
        {
            subLayoutFocus.setBackgroundResource(R.drawable.btnbg_focus);
            wifiName.setTextColor(Color.BLACK);
        } 
        else
        {
            subLayoutFocus.setBackgroundColor(Color.TRANSPARENT);
            wifiName.setTextColor(Color.WHITE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (wifiItemListener != null)
            {
                return wifiItemListener.onWifiItemKeyDown(keyCode, this.getId());
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    OnClickListener itemOnClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            if (wifiItemListener != null)
            {
            	Log.i("wangxian", "on click");
                wifiItemListener.onWifiItemKeyDown(KeyEvent.KEYCODE_DPAD_CENTER, v.getId());
            }
        }
    };

    OnGenericMotionListener itemOnGenericMotionListener = new OnGenericMotionListener()
    {
        @Override
        public boolean onGenericMotion(View v, MotionEvent event)
        {
            switch (event.getAction())
            {
            // process the scroll wheel movement...处理滚轮事件
            case MotionEvent.ACTION_SCROLL:
                    // 获得垂直坐标上的滚动方向,也就是滚轮向下滚
            	if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f)
            	{
            		Log.i("wangxian", "MotionEvent.AXIS_VSCROLL  down");
            		if(canSendKeyevent)
            		{
            			sendKeyCode(KeyEvent.KEYCODE_DPAD_DOWN);
            			canSendKeyevent = false;
            			if (delayHandler.hasMessages(0))
            			{
            				delayHandler.removeMessages(0);
            			}
            			delayHandler.sendEmptyMessageDelayed(0, keyInterval);
            		}
            		return true;
            	}
                    // 获得垂直坐标上的滚动方向,也就是滚轮向上滚
            	else
            	{
            		Log.i("wangxian", "MotionEvent.AXIS_VSCROLL  up");
            		if(canSendKeyevent)
            		{
            			sendKeyCode(KeyEvent.KEYCODE_DPAD_UP);
            			canSendKeyevent = false;
            			if (delayHandler.hasMessages(0))
            			{
            				delayHandler.removeMessages(0);
            			}
            			delayHandler.sendEmptyMessageDelayed(0, keyInterval);
            		}
            		return true;
            	}
            }
            return false;
        }
    };
    
    private void sendKeyCode(final int keyCode)
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(keyCode);
                } 
                catch (Exception e)
                {
                	Log.i("wangxian", "exception when sendpointersync " + e.toString());
                }
            }
        }.start();
    }

    Handler delayHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 0)
            {
                canSendKeyevent = true; // 过了这个interval之后就可以
            }
            super.handleMessage(msg);
        }
    };

}

package com.tv.ui.ParentrolControl;

import java.util.ArrayList;
import java.util.List;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvContext;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class PasswordView
{
	@SuppressLint({ "NewApi", "ShowToast" })
	
	FrameLayout layout;
	final int edit_Weith;
	final int edit_Height;
	final int passwardimg_Weith;
	final int passwardimg_Height;

	final int PASSWORDSIZE;
	float password_X=0;
	float password_Y=0;
	float titleTextSize=30;
	float passwordTextSize=30;
	EditText editerPassword;
	TextView titleTextView;
	TextView FramTextView;
	private int tempInputPassword=0;
	int start_x=0;
	int start_y=0;
	int colorTextViewframe=Color.parseColor("#1B4bbf");
	int colorPasswordText=Color.parseColor("#3495DA");
	int colorPasswordEditBox=Color.parseColor("#2D2D2D");
	int colorTitleBackground=Color.parseColor("#1E1E20");

	int margin_v;
	int margin_h;
	
    int password;
    String title="";
	OnKeyListener editerNumberListenr;
	TextWatcher onediterChangeListenr;
	int keycode_count =0;
	Context self;
	List<TextView> PasswordList=null;
	List<TextView> passwordEditBoximgList=null;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public
	PasswordView(Context self,int start_x,int start_y,int Weith,int Height,int margin_v,int margin_h,int password,String title,float titleTextSize,float passwordTextSize,int PASSWORDSIZE) 
	{	
		this.self=self;
		this.margin_h=margin_h;
		this.margin_v=margin_v;
		this.edit_Weith=Weith-margin_v*2;
		this.edit_Height=Height-margin_h*2;
		this.passwardimg_Weith=(this.edit_Weith-margin_v*3/2)/4;
		this.passwardimg_Height=passwardimg_Weith;
		this.PASSWORDSIZE=PASSWORDSIZE;
		this.password_X=password_X;
		this.password_Y=password_Y;
		this.password=password;
		this.title=title;
		this.titleTextSize=titleTextSize;
		this.passwordTextSize=passwordTextSize;
	    this.start_x=start_x;
	    this.start_y=start_y;
		Resources res = self.getResources();
		
		LayoutParams titleparams=new LayoutParams(edit_Weith,edit_Height/2,Gravity.CENTER);
		titleTextView=new TextView(self);
		titleTextView.setLayoutParams(titleparams);
		titleTextView.setGravity(Gravity.CENTER);
		titleTextView.setX(start_x+margin_v);
		titleTextView.setY(start_y+margin_h);
		titleTextView.setTextSize(titleTextSize);
		titleTextView.setText(this.title);
		titleTextView.setBackgroundColor(colorTitleBackground);
		titleTextView.setTextColor(0xffffffff);
		titleTextView.setSelected(true);
		titleTextView.setSingleLine(true);
		titleTextView.setEllipsize(TruncateAt.MARQUEE);
		titleTextView.setMarqueeRepeatLimit(-1);
		
		LayoutParams FramTextViewparams=new LayoutParams(edit_Weith+margin_v*2,edit_Height+edit_Height/2+margin_h*2,Gravity.CENTER);
		FramTextView=new TextView(self);
		FramTextView.setLayoutParams(FramTextViewparams);
		FramTextView.setBackgroundColor(colorTextViewframe);
		FramTextView.setX(start_x);
		FramTextView.setY(start_y);
		
		
		LayoutParams params=new LayoutParams(edit_Weith,edit_Height,Gravity.CENTER);
        editerPassword=new EditText(self);
        editerPassword.setLayoutParams(params); 
        editerPassword.setGravity(Gravity.CENTER);
        editerPassword.setX(start_x+margin_v);
        editerPassword.setY(start_y+margin_h+edit_Height/2);
        //Drawable background=res.getDrawable(R.drawable.password_button_state);
        editerPassword.setBackgroundColor(colorTitleBackground);
        //editerPassword.setBackground(background);
        editerPassword.setTextColor(00000000);
        editerPassword.setCursorVisible(false);
        
        PasswordList=new ArrayList<TextView>();
        passwordEditBoximgList=new ArrayList<TextView>();
        for(int i=1;i<=PASSWORDSIZE;i++)
        {
        	final TextView passwordTextView=passwordimg(start_x,start_y+margin_h*3+edit_Height/2,i);
        	PasswordList.add(passwordTextView);
        }  
        
        for(int i=1;i<=PASSWORDSIZE;i++)
        {
        	final TextView passwordTextView=passwordEditBoximg(start_x,start_y+margin_h*3+edit_Height/2,i);
        	passwordEditBoximgList.add(passwordTextView);
        } 
        
        editerNumberListenr=new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				Log.v("wxj","PasswordEdit.java onKeyDown="+keyCode);
				switch(keyCode)
				{	
					case KeyEvent.KEYCODE_0:
					case KeyEvent.KEYCODE_1:
					case KeyEvent.KEYCODE_2:
					case KeyEvent.KEYCODE_3:
					case KeyEvent.KEYCODE_4:
					case KeyEvent.KEYCODE_5:
					case KeyEvent.KEYCODE_6:
					case KeyEvent.KEYCODE_7:
					case KeyEvent.KEYCODE_8:
					case KeyEvent.KEYCODE_9:
						return false;
					case KeyEvent.KEYCODE_DPAD_CENTER:
						return true;
					case 178:
						Intent i=new Intent();
						i.setAction("com.android.sky.SendHotKey");
						i.putExtra("specialKey", 178);
						TvContext.context.sendBroadcast(i);
					default:
						keycode_count=0;
						return false;
				}
			}
		};
        editerPassword.setOnKeyListener(editerNumberListenr);
        final int PassWordSizetemp=PASSWORDSIZE;
        onediterChangeListenr=new TextWatcher(){
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()<=PasswordList.size())
				{
					setPasswordImg(PasswordList,s.length());
				}
				if(s.length()==PassWordSizetemp)
				{
					int input=Integer.parseInt(s.toString());
					tempInputPassword=input;
					Log.v("gky","password View ,tempInputPassword="+tempInputPassword);
					Log.v("gky","password View ,PasswordView.this.password="+PasswordView.this.password);
					if(input==PasswordView.this.password)
					{						
						passwordright(String.valueOf(input));
					}
					else if(input==8899)
					{						
						passwordsuper(String.valueOf(input));
					}
					else 
					{
						passwordwrong(String.valueOf(input));
					}
				}
				else if(s.length()>PassWordSizetemp)
				{
					editerPassword.setText(editerPassword.getText().subSequence(0,PassWordSizetemp));
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		};
        editerPassword.addTextChangedListener(onediterChangeListenr);
        layout=new FrameLayout(self);
        
        layout.addView(FramTextView);
        layout.addView(titleTextView);
        layout.addView(editerPassword);
        for(int i=0;i<PasswordList.size();i++)
        {
        	layout.addView(passwordEditBoximgList.get(i));
        	layout.addView(PasswordList.get(i));
        }

	}

	@SuppressLint("NewApi")
	TextView passwordimg(float x,float y,int count)
	{
		TextView passwordimg=new TextView(self);
		Resources res=self.getResources();
//		Drawable passwordDrawable = res.getDrawable(R.drawable.button);
		Drawable passwordDrawable = new BitmapDrawable(createCircleImage(20));
		LayoutParams params=new LayoutParams(passwardimg_Weith,passwardimg_Height,Gravity.CENTER);
		float password_X=x+(count-1)*(params.width+margin_v/2)+margin_v;
		float password_Y=y;
		
		passwordimg.setBackground(passwordDrawable);
//		passwordimg.setBackgroundColor(colorPasswordText);
		passwordimg.setBackgroundResource(R.drawable.password);
		
		passwordimg.setX(password_X);
		passwordimg.setY(password_Y);
		passwordimg.setLayoutParams(params);
		passwordimg.setVisibility(View.GONE);
		return passwordimg;	
	}
	
	
	@SuppressLint("NewApi")
	TextView passwordEditBoximg(float x,float y,int count)
	{
		TextView passwordEditBoximg=new TextView(self);
		Resources res=self.getResources();
//		Drawable passwordDrawable = res.getDrawable(R.drawable.button);
		Drawable passwordDrawable = new BitmapDrawable(createCircleImage(20));
		LayoutParams params=new LayoutParams(passwardimg_Weith,passwardimg_Height,Gravity.CENTER);
//		float password_X=x+(count-1)*(params.width+edit_Weith/6/3)+edit_Weith/6/2;
		float password_X=x+(count-1)*(params.width+margin_v/2)+margin_v;
		float password_Y=y;
		
		passwordEditBoximg.setBackground(passwordDrawable);
		passwordEditBoximg.setBackgroundColor(colorPasswordEditBox);
		passwordEditBoximg.setX(password_X);
		passwordEditBoximg.setY(password_Y);
		passwordEditBoximg.setLayoutParams(params);
		passwordEditBoximg.setVisibility(View.VISIBLE);
		return passwordEditBoximg;	
	}
	
	void setPasswordImg(List<TextView>passwordlist ,int position)
	{
		for(int i=0;i<position;i++)
		{
			passwordlist.get(i).setVisibility(View.VISIBLE);
		}
		for(int i=position;i<passwordlist.size();i++)
		{
			passwordlist.get(i).setVisibility(View.GONE);
		}
	}
	
	public void passwordright(String input)
	{	
		Log.v("wxj","passwordright");
		clearPassword();
	}
	public void passwordsuper(String input)
	{
		Log.v("wxj","super");
		passwordright(input);
	}
	public void passwordwrong(String input)
	{
		Log.v("wxj","passwordwrong");
	}

	public void passwordwrongInforText()
	{
		Log.v("chencong","passwordwrongInforText");
		TvUIControler.getInstance().showMniToast(TvContext.context.getResources().getString(R.string.wifiInvialidPwd));
//		titleTextView.setText(R.string.TV_CFG_LOCK_INPUT_ERROR);
//		titleTextView.setTextColor(Color.RED);
	}
	public FrameLayout getlayout()
	{
		return layout;
	}
	
	public void clearPassword()
	{
		Log.i("wxj","clearPassword");
		editerPassword.setText("");
		for(int i=0;i<PasswordList.size();i++)
		{
			PasswordList.get(i).setVisibility(View.GONE);
		}
	}
	
	public void setPassword(int password)
	{
		this.password=password;
	}
	
	public int getTempInputPassword()
	{
		return tempInputPassword;
	}
	
//	/** 
//     * 根据原图和变长绘制圆形图片 
//     *  
//     * @param source 
//     * @param min 
//     * @return 
//     */  
//    private Bitmap createCircleImage(Bitmap source, int min)  
//    {  
//        final Paint paint = new Paint();  
//        paint.setAntiAlias(true);  
//        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);  
//        /** 
//         * 产生一个同样大小的画布 
//         */  
//        Canvas canvas = new Canvas(target);  
//        /** 
//         * 首先绘制圆形 
//         */  
//        canvas.drawCircle(min / 2, min / 2, min / 2, paint);  
//        /** 
//         * 使用SRC_IN 
//         */  
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  
//        /** 
//         * 绘制图片 
//         */  
//        canvas.drawBitmap(source, 0, 0, paint);  
//        
//        return target;  
//    } 
	
	public void setTitle(String title)
	{
		titleTextView.setText(title);
		titleTextView.setTextColor(0xffffffff);
	}
    /** 
     * 根据原图和变长绘制圆形图片 
     *  
     * @param source 
     * @param min 
     * @return 
     */  
    private Bitmap createCircleImage(int min)  
    {  
    	Bitmap source=Bitmap.createBitmap(min, min, Config.ARGB_8888);
        Paint paint = new Paint();  
        paint.setColor(Color.WHITE);
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);  
        /** 
         * 产生一个同样大小的画布 
         */  
        Canvas canvas = new Canvas(target);  
        /** 
         * 首先绘制圆形 
         */  
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);  
        /** 
         * 使用SRC_IN 
         */  
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  
        /** 
         * 绘制图片 
         */  
        canvas.drawBitmap(source, 0, 0, paint);  
        
        return target;  
    } 
}

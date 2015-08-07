package com.tv.ui.network.defs;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvContext;

public class NetBgAndTitleLayout extends LinearLayout
{
    public ImageView title_icon;
    public TextView title;
    public float resolution;

    public NetBgAndTitleLayout()
    {
        super(TvContext.context);
        resolution = TvUIControler.getInstance().getResolutionDiv();
        RelativeLayout layout = new RelativeLayout(TvContext.context);
        layout.setLayoutParams(new LinearLayout.LayoutParams((int) (845 / TvUIControler.getInstance()
                .getResolutionDiv()), LayoutParams.MATCH_PARENT));
        // 上边的黄色部分
        LinearLayout upLayout = new LinearLayout(TvContext.context);
        RelativeLayout.LayoutParams upLp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (140 / resolution));
        upLayout.setBackgroundColor(0xfff1c40e);
        // 下边的灰色部分
        LinearLayout downLayout = new LinearLayout(TvContext.context);
        RelativeLayout.LayoutParams downLp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        downLp.setMargins(0, (int) (140 / resolution), 0, 0);
        downLayout.setBackgroundColor(0xe11e1e1e);
        // icon
        title_icon = new ImageView(TvContext.context);
        RelativeLayout.LayoutParams iconLp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        iconLp.setMargins((int) (40 / resolution), (int) (20 / resolution), 0, 0);
        // title
        title = new TextView(TvContext.context);
        RelativeLayout.LayoutParams titleLp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleLp.setMargins((int) (170 / resolution), (int) (40 / resolution), 0, 0);
        title.setTextColor(Color.BLACK);
        title.setTextSize(NetConstant.FONT_TXT_SIZE_TITLE_BAR);
        title.setText(R.string.wifiSetup);
        layout.addView(upLayout, upLp);
        layout.addView(downLayout, downLp);
        layout.addView(title_icon, iconLp);
        layout.addView(title, titleLp);
        this.addView(layout);
    }

    public void update(String res, String _title)
    {
        if (res != null && !res.equals(""))
        {
            title_icon.setImageBitmap(loadBitmapFromAssets(TvContext.context,res));
        }
        if (title != null && !title.equals(""))
        {
            title.setText(_title);
        }
    }
    
    public synchronized static Bitmap loadBitmapFromAssets(Context c, String image_path)
    {
        AssetManager assetManager = c.getAssets();
        image_path = "1080p/setting" + "/" + image_path;
        InputStream is = null;
        Bitmap bm = null;
        Log.i("wangxian", "image_path is " + image_path);
        try
        {
            is = assetManager.open(image_path);
            bm = BitmapFactory.decodeStream(is);
            is.close();
        } 
        catch (IOException e)
        {
        	Log.i("wangxian", "e is " + e.getMessage());
            e.printStackTrace();
        }

        return bm;
    }
}

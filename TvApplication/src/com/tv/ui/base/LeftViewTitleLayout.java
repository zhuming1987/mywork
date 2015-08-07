package com.tv.ui.base;

import java.io.IOException;
import java.io.InputStream;

import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.content.Context;
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

public class LeftViewTitleLayout extends LinearLayout
{
    private ImageView mImgViewIcon;
    private TextView mTxtViewTitle;

    private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dipDiv = TvUIControler.getInstance().getDipDiv();

    private final int width = (int) (845 / div);
    private final int height = (int) (135 / div);

    private Context mContext;

    private int textsize = (int) (48 / dipDiv);

    public LeftViewTitleLayout(Context context)
    {
        super(context);
        mContext = context;
        Log.e("gky", "LeftViewTitleLayout init");
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
        setOrientation(LinearLayout.HORIZONTAL);
        setFocusable(false);
        setBackgroundResource(R.drawable.title_bg_yellow);
        this.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);

        mImgViewIcon = new ImageView(context);

        mTxtViewTitle = new TextView(context);
        mTxtViewTitle.setTextSize(textsize);
        mTxtViewTitle.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        mTxtViewTitle.setTextColor(Color.BLACK);
        mTxtViewTitle.getPaint().setAntiAlias(true);
        mTxtViewTitle.setSingleLine(true);
        mTxtViewTitle.setEllipsize(TruncateAt.MARQUEE);
        mTxtViewTitle.setMarqueeRepeatLimit(-1);
        mTxtViewTitle.setSelected(true);

        LayoutParams iconParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        iconParams.leftMargin = (int) (35 / div);
        iconParams.topMargin = (int) (15 / div);
        iconParams.rightMargin = (int)(10 / div);
        addView(mImgViewIcon, iconParams);
        LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        titleParams.topMargin = (int) (15 / div);
        titleParams.rightMargin = (int)(30 / div);
        addView(mTxtViewTitle,titleParams);

    }

    public void bindData(String strIcon, String strTitle)
    {
        Bitmap bitmap = loadBitmapFromAssets(mContext, strIcon);
        mImgViewIcon.setImageBitmap(bitmap);
        mTxtViewTitle.setText(strTitle);
    }

    public synchronized static Bitmap loadBitmapFromAssets(Context c, String image_path)
    {
        AssetManager assetManager = c.getAssets();
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
}

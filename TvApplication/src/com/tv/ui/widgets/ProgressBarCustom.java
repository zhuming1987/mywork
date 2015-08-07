package com.tv.ui.widgets;

import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ProgressBarCustom extends FrameLayout
{
	private static float div = TvUIControler.getInstance().getResolutionDiv();
	
    private ImageView barImage;
    private ImageView barBgImage;
    private int min = 0, max = 100;
    private int progress = 0;

    int width = (int) (400 / div);
    int height = (int) (15 / div);
    private int barPicResId = R.drawable.progress_green;
    private int barBgPicResId = R.drawable.progressbg;

    public ProgressBarCustom(Context context)
    {
        super(context);
        barImage = new ImageView(context);
        barImage.setBackgroundResource(barPicResId);
        barBgImage = new ImageView(context);
        barBgImage.setBackgroundResource(barBgPicResId);
        this.addView(barBgImage, new LayoutParams(width, height, Gravity.CENTER));
        this.addView(barImage, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.CENTER_VERTICAL));
    }

    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.updateViewLayout(barBgImage, new LayoutParams(width, height, Gravity.CENTER));
    }

    public void setDrawableResId(int barId, int barBgId)
    {
        this.barPicResId = barId;
        this.barBgPicResId = barBgId;
        barImage.setBackgroundResource(barPicResId);
        barBgImage.setBackgroundResource(barBgPicResId);
    }

    public void setRange(int min, int max)
    {
        this.min = min;
        this.max = max;
        refreshView();
    }

    public void setProgress(int progress)
    {
        if (progress >= min && progress <= max)
        {
            this.progress = progress;
            refreshView();
        }
    }

    private void refreshView()
    {
        float percent = (float) (progress - min) / (float) (max - min);
        this.updateViewLayout(barImage, new LayoutParams((int) (percent * width),
                LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.CENTER_VERTICAL));
    }
}

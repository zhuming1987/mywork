package com.tv.ui.CiInformation;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.ui.widgets.ItemView;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 窗口主干
 * @author Administrator
 *
 */
public class CIInfobodyLayout extends LinearLayout
{
	private float div = TvUIControler.getInstance().getResolutionDiv();
	private int height = (int) (945 / div);
    protected Context context;

    public CIInfobodyLayout(Context context)
    {
        super(context);
        this.context = context;
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
        this.setBackgroundResource(R.drawable.bodyback);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setPadding(0, (int) (0 / div), 0, 0);
    }
    
    public void addDivider()
    {
        ImageView divider = new ImageView(context);
        divider.setBackgroundResource(R.drawable.setting_line);
        this.addView(divider, new LayoutParams(ItemView.width - (int) (30 / div), (int) (2 / div)));
    }

    public void addView(View view, boolean addDivider)
    {
        this.addView(view);
        if (addDivider)
            this.addDivider();
    }
}

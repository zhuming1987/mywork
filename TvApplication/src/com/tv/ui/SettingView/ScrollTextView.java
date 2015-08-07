package com.tv.ui.SettingView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

public class ScrollTextView extends View
{
    private boolean mFocus = true;
    private float mTextLen = 0f;// 文本长度
    private float mOffSetX = 0f;// 文字的横坐标
    private float mOffSetY = 36f;// 文字的纵坐标
    private String mText = "";// 文本内容
    private Paint mPaint = new Paint();
    private int mViewWidth = 180;
    private int mAnimDirection = 0;
    private boolean mNeedAnim = false;
    private static final int LEFT_DIRECTION = 0;
    private static final int RIGHT_DIRECTION = 1;
    private int SCROLL_STEP = 2;
    private int gravity = Gravity.CENTER;

    public ScrollTextView(Context context)
    {
        super(context);
        init();
    }

    public ScrollTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        mPaint.setTextSize(30);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
    }

    public void setText(String txt)
    {
        mText = txt;
        mTextLen = mPaint.measureText(mText);
        invalidate();
    }

    public void setColor(int color)
    {
        mPaint.setColor(color);
    }

    public void setTextSize(float size)
    {
        mPaint.setTextSize(size);
        invalidate();
    }

    public void ResetOffsetX(int _mOffSetX)
    {
        mOffSetX = _mOffSetX;
    }

    public void setFocus(boolean focus)
    {
        mFocus = focus;
        if (mFocus)
        {
            if (mTextLen > mViewWidth)
            {
                mNeedAnim = true;
            } else
            {
                mNeedAnim = false;
            }

            startScroll();
        } else
        {
            stopScroll();
        }
    }

    private void startScroll()
    {
        invalidate();
    }

    public void setGravity(int _gravity)
    {
        gravity = _gravity;
        setGravityPosition();
    }

    public void setGravityPosition()
    {
        switch (gravity)
        {
            case Gravity.CENTER:
                mOffSetX = (mViewWidth - mTextLen) / 2;
                break;
            case Gravity.LEFT:
                mOffSetX = 0;
                break;
            default:
                mOffSetX = (mViewWidth - mTextLen) / 2;
                break;
        }
        invalidate();
    }

    private void stopScroll()
    {
        if (mTextLen < mViewWidth)
        {
            setGravityPosition();
            mNeedAnim = false;
        } else
        {
            mOffSetX = 0;
            mNeedAnim = true;
        }
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        canvas.drawText(mText, mOffSetX, mOffSetY, mPaint);
        if (mFocus && mNeedAnim)
        {
            caluateScrollData();
        }
    }

    private void caluateScrollData()
    {
        if (mAnimDirection == LEFT_DIRECTION)
        {
            if (mOffSetX > mViewWidth / 2 - mTextLen)
            {
                mOffSetX -= SCROLL_STEP;
            } else
            {
                mAnimDirection = RIGHT_DIRECTION;
            }
        } else
        {
            if (mOffSetX < mViewWidth / 2)
            {
                mOffSetX += SCROLL_STEP;
            } else
            {
                mAnimDirection = LEFT_DIRECTION;
            }
        }
        invalidate();
    }

    public void setWidth(int ViewWidth)
    {
        mViewWidth = ViewWidth;
    }

    public void setOffsetY(int f)
    {
        mOffSetY = f;
    }

    public void setStep(int step)
    {
        SCROLL_STEP = step;
    }

    public void setInvalidata(boolean isInvalidata)
    {
        if (isInvalidata)
        {
            mFocus = true;
            invalidate();
        } else
        {
            mFocus = false;
        }
    }
}
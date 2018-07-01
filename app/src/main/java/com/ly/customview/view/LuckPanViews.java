package com.ly.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;


public class LuckPanViews extends android.support.v7.widget.AppCompatImageView {

    private int mItemCount = 8;

    private String[] mStrs = new String[]{"直通车", "10TNB", "100TNB", "100IQB",
            "10CDC", "100CDC", "100XMX", "1ETH"};
    private volatile float mStartAngle = 0;
    private RectF mRange = new RectF();
    /**
     * 圆的直径
     */
    private int mRadius;

    /**
     * 绘制文字的画笔
     */
    private Paint mTextPaint;
    /**
     * 文字的大小
     */
    private float mTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics());

    public LuckPanViews(Context context) {
        super(context);
        initView();
    }

    public LuckPanViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LuckPanViews(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        // 获取圆形的直径
        mRadius = width - getPaddingLeft() - getPaddingRight();
        // 圆弧的绘制范围
        mRange = new RectF(getPaddingLeft(), getPaddingLeft(), mRadius
                + getPaddingLeft(), mRadius + getPaddingLeft());
        setMeasuredDimension(width, width);
    }

    private void initView() {
        // 初始化绘制文字的画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#722E00"));
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float tmpAngle = mStartAngle;
        float sweepAngle = (float) (360 / mItemCount);
        for (int i = 0; i < mItemCount; i++) {
            drawText(canvas, tmpAngle, sweepAngle, mStrs[i]);
            tmpAngle += sweepAngle;
        }
    }

    /**
     * 绘制文本
     *
     * @param startAngle
     * @param sweepAngle
     * @param string
     */
    private void drawText(Canvas canvas, float startAngle, float sweepAngle, String string) {
        Path path = new Path();
        path.addArc(mRange, startAngle, sweepAngle);
        float textWidth = mTextPaint.measureText(string);
        // 利用水平偏移让文字居中
        float hOffset = (float) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);// 水平偏移
        float vOffset = mRadius / 2 / 6 + 20;// 垂直偏移
        canvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
    }
}

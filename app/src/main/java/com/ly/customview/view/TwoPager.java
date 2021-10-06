package com.ly.customview.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

public class TwoPager extends ViewGroup {
    float downX;
    float downY;
    float downScrollX;
    boolean scrolling;
    float minVelocity;
    float maxVelocity;
    OverScroller overScroller;
    ViewConfiguration viewConfiguration;
    VelocityTracker velocityTracker = VelocityTracker.obtain();//速度识别器

    public TwoPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        overScroller = new OverScroller(context);
        viewConfiguration = ViewConfiguration.get(context);
        maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();//系统的最小速度
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //viewpager比较特殊，自view和自己的宽高一样
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        int childTop = 0;
        int childRight = getWidth();
        int childBottom = getHeight();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(childLeft, childTop, childRight, childBottom);
            childLeft += getWidth();
            childRight += getWidth();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(ev);

        boolean result = false;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                scrolling = false;
                downX = ev.getX();
                downY = ev.getY();
                downScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!scrolling) {//第一个滑动
                    float dx = downX - ev.getX();
                    if (Math.abs(dx) > viewConfiguration.getScaledPagingTouchSlop()) {
                        scrolling = true;
                        //可能viewpager外是个scrollview，所以需要请求父控件不拦截自己的事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                        result = true;//拦截子view自己处理
                    }
                }
                break;
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = downX - event.getX() + downScrollX;
                if (dx > getWidth()) {//边界值
                    dx = getWidth();
                } else if (dx < 0) {
                    dx = 0;
                }
                scrollTo((int) (dx), 0);
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                float vx = velocityTracker.getXVelocity();
                int scrollX = getScrollX();
                int targetPage;
                if (Math.abs(vx) < minVelocity) {
                    targetPage = scrollX > getWidth() / 2 ? 1 : 0;
                } else {
                    targetPage = vx < 0 ? 1 : 0;
                }
                int scrollDistance = targetPage == 1 ? (getWidth() - scrollX) : - scrollX;
                overScroller.startScroll(getScrollX(), 0, scrollDistance, 0);
                postInvalidateOnAnimation();//会导致computeScroll执行，然后computeScroll里再刷新
                break;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            postInvalidateOnAnimation();
        }
    }
}

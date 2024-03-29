package com.ly.customview.view.drag;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import com.ly.customview.R;


public class DragUpDownLayout extends FrameLayout {
    View view;
    ViewDragHelper dragHelper;
    ViewDragHelper.Callback dragListener = new DragCallback();
    ViewConfiguration viewConfiguration;

    public DragUpDownLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        dragHelper = ViewDragHelper.create(this, dragListener);
        viewConfiguration = ViewConfiguration.get(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view = findViewById(R.id.view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    class DragCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == view;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return top;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if (Math.abs(yvel) > viewConfiguration.getScaledMinimumFlingVelocity()) {
                if (yvel > 0) {
                    dragHelper.settleCapturedViewAt(0, getHeight() - releasedChild.getHeight());
                } else {
                    dragHelper.settleCapturedViewAt(0, 0);
                }
            } else {
                if (releasedChild.getTop() < getHeight() - releasedChild.getBottom()) {
                    dragHelper.settleCapturedViewAt(0, 0);
                } else {
                    dragHelper.settleCapturedViewAt(0, getHeight() - releasedChild.getHeight());
                }
            }
            postInvalidateOnAnimation();
        }
    }

}

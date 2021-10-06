package com.ly.customview.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;


import com.ly.customview.utils.Utils;

public class ScalableImageView extends View {
    private static final float IMAGE_WIDTH = Utils.dp2px(300);
    private static final float OVER_SCALE_FACTOR = 1.5f;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;

    float originalOffsetX;
    float originalOffsetY;
    float offsetX;
    float offsetY;
    float bigScale;
    float smallScale;
    boolean big;
    float currentScale;
    float maxOffsetX;
    float maxOffsetY;

    GestureDetectorCompat detector;
    ObjectAnimator scaleAnimator;
    OverScroller scroller;
    GestureDetector.OnGestureListener henGestureListener = new HenGestureListener();
    Runnable henAnimationRunner = new HenRunner();
    ScaleGestureDetector scaleDetector;
    ScaleGestureDetector.OnScaleGestureListener henScaleListener = new HenScaleListener();

    public ScalableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bitmap = Utils.getAvatar(getResources(), (int) IMAGE_WIDTH);
        detector = new GestureDetectorCompat(context, henGestureListener);
        //用于自动计算滑动的偏移
        //常用于 onFling() 方法中，调⽤用 OverScroller.fling() ⽅法来启动惯性滑动的计算
        scroller = new OverScroller(context);
        scaleDetector = new ScaleGestureDetector(context, henScaleListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        originalOffsetX = (getWidth() - bitmap.getWidth()) / 2f;
        originalOffsetY = (getHeight() - bitmap.getHeight()) / 2f;

        if ((float) bitmap.getWidth() / bitmap.getHeight() > (float) getWidth() / getHeight()) {
            smallScale = (float) getWidth() / bitmap.getWidth();
            bigScale = (float) getHeight() / bitmap.getHeight() * OVER_SCALE_FACTOR;
        } else {
            smallScale = (float) getHeight() / bitmap.getHeight();
            bigScale = (float) getWidth() / bitmap.getWidth() * OVER_SCALE_FACTOR;
        }
        currentScale = smallScale;
        maxOffsetX = (bitmap.getWidth() * bigScale - getWidth()) / 2;
        maxOffsetY = (bitmap.getHeight() * bigScale - getHeight()) / 2;
    }

    public float getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
        invalidate();
    }

    private ObjectAnimator getScaleAnimator() {
        if (scaleAnimator == null) {
            scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", 0);
        }
        scaleAnimator.setFloatValues(smallScale, bigScale);
        return scaleAnimator;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = scaleDetector.onTouchEvent(event);//双击
        if (!scaleDetector.isInProgress()) {
            result = detector.onTouchEvent(event);//双指缩放
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float scaleFraction = (currentScale - smallScale) / (bigScale - smallScale);
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction);//移动
        canvas.scale(currentScale, currentScale, getWidth() / 2f, getHeight() / 2f);//放缩
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint);
    }

    private void fixOffsets() {
        offsetX = Math.max(Math.min(offsetX, maxOffsetX), -maxOffsetX);
        offsetY = Math.max(Math.min(offsetY, maxOffsetY), -maxOffsetY);
    }

    private class HenGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            // 每次 ACTION_DOWN 事件出现的时候都会被调⽤，必须在这里返回true
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // ⽤用户按下 100ms 不松⼿后会被调⽤，⽤于标记「可以显示按下状态了」
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // ⽤户单击时被调⽤(支持长按时长按后松⼿不会调⽤、双击的第⼆下时不会被调⽤
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // 用户滑动时被调⽤
            // 第一个事件是⽤户按下时的 ACTION_DOWN 事件，第⼆个事件是当前事件
            // 偏移是按下时的位置 - 当前事件的位置
            if (big) {
                offsetX -= distanceX;
                offsetY -= distanceY;
                fixOffsets();
                invalidate();
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // ⽤户长按（按下 500ms 不松⼿）后会被调⽤
            // 这个 500ms 在 GestureDetectorCompat 中变成了 600ms(？？？)
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // ⽤于滑动时迅速抬起时被调⽤，⽤于⽤户希望控件进行惯性滑动的场景
            if (big) {
                // 初始化滑动
                scroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY,
                        -(int) maxOffsetX, (int) maxOffsetX, -(int) maxOffsetY, (int) maxOffsetY,
                        (int) Utils.dp2px(50), (int) Utils.dp2px(50));
                // 下一帧刷新
                ViewCompat.postOnAnimation(ScalableImageView.this, henAnimationRunner);
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // ⽤户单击时被调⽤
            // 和 onSingltTapUp() 的区别在于，⽤户的一次点击不会立即调用这个方法，
            // 而是在⼀定时间后（300ms），确认用户没有进行双击，这个⽅法才会被调用
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // ⽤用户双击时被调⽤
            // 注意：第二次触摸到屏幕时就调用，⽽不是抬起时
            big = !big;
            if (big) {
                offsetX = (e.getX() - getWidth() / 2f) * (1 - bigScale / smallScale);
                offsetY = (e.getY() - getHeight() / 2f) * (1 - bigScale / smallScale);
                fixOffsets();
                getScaleAnimator().start();
            } else {
                getScaleAnimator().reverse();
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            // ⽤户双击第⼆次按下时、第⼆次按下后移动时、第⼆次按下后抬起时都会被调⽤
            // 常用于「双击拖拽」的场景
            return false;
        }
    }

    private class HenRunner implements Runnable {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            // 计算此时的位置，并且如果滑动已经结束，就停⽌
            if (scroller.computeScrollOffset()) {
                // 把此时的位置应⽤用于界⾯
                offsetX = scroller.getCurrX();
                offsetY = scroller.getCurrY();
                invalidate();
                // 下⼀帧刷新
                postOnAnimation(this);
            }
        }
    }

    private class HenScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
        private float initialCurrentScale;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            currentScale = initialCurrentScale * detector.getScaleFactor();
            invalidate();
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initialCurrentScale = currentScale;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }
}

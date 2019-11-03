package com.ly.customview.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义 ViewGroup onMeasure
 */
public class TagLayout extends ViewGroup {
    List<Rect> childrenBounds = new ArrayList<>();

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthUsed = 0;
        int heightUsed = 0;
        int lineWidthUsed = 0;
        int lineHeight = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            //遍历每个子 View，用 measureChildWithMargins() 测量子 View
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            if (widthMode != MeasureSpec.UNSPECIFIED &&
                    lineWidthUsed + child.getMeasuredWidth() > widthSize) {//换行
                lineWidthUsed = 0;
                heightUsed += lineHeight;
                //需要重新测量
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed);
            }

            Rect childBounds;
            if (childrenBounds.size() <= i) {
                childBounds = new Rect();
                childrenBounds.add(childBounds);
            } else {
                childBounds = childrenBounds.get(i);
            }
            // 保存⼦ View 的位置和尺⼨
            childBounds.set(lineWidthUsed, heightUsed, lineWidthUsed + child.getMeasuredWidth(),
                    heightUsed + child.getMeasuredHeight());

            lineWidthUsed += child.getMeasuredWidth();
            widthUsed = Math.max(lineWidthUsed, widthUsed);
            lineHeight = Math.max(lineHeight, child.getMeasuredHeight());
        }
        //计算⾃己的尺寸，并保存
        int measuredWidth = widthUsed;
        heightUsed += lineHeight;
        int measuredHeight = heightUsed;
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect childBounds = childrenBounds.get(i);
            //遍历每个⼦ View，调用它们的 layout() ⽅法来将位置和尺寸传给它们
            child.layout(childBounds.left, childBounds.top, childBounds.right, childBounds.bottom);
        }
    }

    /**
     * 需要重写 generateLayoutParams() 并返回 MarginLayoutParams
     * 才能使用 measureChildWithMargins() ⽅法
     * @param attrs
     * @return
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

}

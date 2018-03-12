package com.ly.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by liuyu1 on 2018/3/12.
 * 拖拽view的四种方式
 */
public class DragView extends View {
    private int lastX;
    private int lastY;

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent event) {

        //获取到手指处的横坐标和纵坐标
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算移动的距离
                int offX = x - lastX;
                int offY = y - lastY;
                /**
                 * 第一种
                 * 调用layout方法来重新放置它的位置
                 */
                layout(getLeft() + offX, getTop() + offY,
                        getRight() + offX, getBottom() + offY);
//                /**
//                 * 第二种
//                 * 这两个方法分别是对左右移动和上下移动的封装，传入的就是 偏移量
//                 */
//                offsetLeftAndRight(offX);
//                offsetTopAndBottom(offY);
//                /**
//                 * 第三种
//                 */
//                ViewGroup.MarginLayoutParams mlp =
//                        (ViewGroup.MarginLayoutParams) getLayoutParams();
//                mlp.leftMargin = getLeft() + offX;
//                mlp.topMargin = getTop() + offY;
//                setLayoutParams(mlp);
//                /**
//                 * 第四种
//                 * sceollTo(x,y)传入的应该是移动的终点坐标
//                 * scrollBy(dx,dy)传入的是移动的增量。
//                 * 通过scrollBy传入的值应该是你需要的那个增量的相反数
//                 */
//                ((View) getParent()).scrollBy(-offX, -offY);
                break;
        }
        return true;
    }
}

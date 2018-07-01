package com.ly.customview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ly.customview.R;

public class LuckPanLayout extends RelativeLayout {

    private LuckPanViews luckPanViews;
    /**
     * 初始角度，也是360/8   40
     */
    private int InitAngle = 0;
    /**
     * 半径
     */
    private int radius = 0;
    /**
     * 弧的角度,计算具体的x,y的值的时候要根据弧度去计算，不能根据角度。
     */
    private int verPanRadius;
    /**
     * 初始化是弧度的一半，45/2
     */
    private int diffRadius;
    int panNum = 8;
    //旋转一圈所需要的时间
    private static final long ONE_WHEEL_TIME = 1000;
    private RelativeLayout rl_luck_content;


    public LuckPanLayout(Context context) {
        super(context);
        initView();
    }

    public LuckPanLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LuckPanLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        verPanRadius = 360 / panNum;
        diffRadius = verPanRadius / 2;

        View view = View.inflate(getContext(), R.layout.layout_pan, null);
        addView(view);
        luckPanViews = (LuckPanViews) view.findViewById(R.id.luckpan_view);
        ImageView luck_go = (ImageView) view.findViewById(R.id.luck_go);
        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        rl_luck_content = (RelativeLayout) view.findViewById(R.id.rl_luck_content);
        luck_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRotate(7);
            }
        });
        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_luck_content.setVisibility(GONE);
            }
        });
    }


    private void startRotate(int pos) {
        int lap = 5;
        //Rotate angle.根据要到达的position计算最后一圈实际旋转的角度
        int angle = 0;
        if (pos < 0) {
            angle = (int) (Math.random() * 360);
        } else {
            int initPos = queryPosition();//3
            if (pos > initPos) {
                angle = (pos - initPos) * verPanRadius;
                lap -= 1;
                angle = 360 - angle;
            } else if (pos < initPos) {
                angle = (initPos - pos) * verPanRadius;
            } else {
                //nothing to do.
            }
        }

        //All of the rotate angle.要旋转的角度
        int increaseDegree = lap * 360 + angle;
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;//1500
        /**
         *  要旋转到的角度(原来的角度+要旋转旋转的角度)
         */
        int DesRotate = increaseDegree + InitAngle;

        //为了每次都能旋转到转盘的中间位置
        int offRotate = DesRotate % 360 % verPanRadius;
        DesRotate -= offRotate;
        DesRotate += diffRadius;

        ObjectAnimator animtor = ObjectAnimator.ofFloat(luckPanViews, "rotation", InitAngle, DesRotate);
        animtor.setInterpolator(new AccelerateDecelerateInterpolator());
        animtor.setDuration(time);
        animtor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float updateValue = (float) animation.getAnimatedValue();
                int updateValueint = (int) (updateValue + 0.5f);
                InitAngle = ((updateValueint % 360 + 360) % 360);
            }
        });

        animtor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                int i = queryPosition();

            }
        });
        animtor.start();

    }

    /**
     * 计算所在位置
     *
     * @return
     */
    private int queryPosition() {
        InitAngle = (InitAngle % 360 + 360) % 360;
        int pos = InitAngle / verPanRadius - 1;
        return calcumAngle(pos);
    }


    private int calcumAngle(int pos) {
        if (pos >= 0 && pos <= panNum / 2) {
            pos = panNum / 2 - pos;
        } else {
            pos = (panNum - pos) + panNum / 2;
        }
        return pos;
    }


}

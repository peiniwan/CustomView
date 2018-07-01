package com.ly.customview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ly.customview.utils.DrawableUtils;
import com.ly.customview.utils.UiUtils;
import com.ly.customview.view.FlowlayoutView;

import java.util.Random;

public class MainActivity extends Activity {


    private String[] mStrs = new String[]{"Scrollview回弹效果", "视差特效", "拖拽view", "圆环进度条",
            "转盘", "转盘2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout flContent = (FrameLayout) findViewById(R.id.flContent);

        ScrollView scrollView = new ScrollView(UiUtils.getContext());
        scrollView.setBackgroundResource(R.drawable.grid_item_bg_normal);
        FlowlayoutView layout = new FlowlayoutView(UiUtils.getContext());
        int padding = UiUtils.dip2px(13);
        layout.setPadding(padding, padding, padding, padding);
        // layout.setOrientation(LinearLayout.VERTICAL);// 设置线性布局的方向

        int backColor = 0xffcecece;
        Drawable pressedDrawable = DrawableUtils.createShape(backColor);// 按下显示的图片


        for (int i = 0; i < mStrs.length; i++) {
            final TextView textView = new TextView(UiUtils.getContext());
            String str = mStrs[i];
            textView.setText(str);
            Random random = new Random(); // 创建随机
            int red = random.nextInt(200) + 22;
            int green = random.nextInt(200) + 22;
            int blue = random.nextInt(200) + 22;// 有可能都是0或255成白色或者黑色了
            int color = Color.rgb(red, green, blue);// 范围 0-255
            GradientDrawable createShape = DrawableUtils.createShape(color); // 默认显示的图片
            StateListDrawable createSelectorDrawable = DrawableUtils.createSelectorDrawable(pressedDrawable, createShape);// 创建状态选择器
            textView.setBackgroundDrawable(createSelectorDrawable);
            textView.setTextColor(Color.WHITE);
//             textView.setTextSize(UiUtils.dip2px(14));

            int textPaddingV = UiUtils.dip2px(4);
            int textPaddingH = UiUtils.dip2px(7);
            textView.setPadding(textPaddingH, textPaddingV, textPaddingH,
                    textPaddingV);
//            //padding设置了一样还是不居中
//            textView.setPadding(textPaddingV, textPaddingV, textPaddingV,
//                    textPaddingV);
//            textView.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);

            textView.setClickable(true);// 设置textView可以被点击
            textView.setOnClickListener(new View.OnClickListener() { // 设置点击事件

                @Override
                public void onClick(View v) {
                    String s = textView.getText().toString();
                    toActivity(s);
                }
            });
            layout.addView(textView, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, -2));// -2 包裹内容
        }

        scrollView.addView(layout);
        flContent.addView(scrollView);
    }

    private void toActivity(String s) {
        Intent intent = new Intent();
        if (s.equals(mStrs[0])) {
            intent.setClass(MainActivity.this, TestScrollviewActivity.class);
            startActivity(intent);
        } else if (s.equals(mStrs[1])) {
            intent.setClass(MainActivity.this, TestParallaxActivity.class);
            startActivity(intent);
        } else if (s.equals(mStrs[2])) {
            intent.setClass(MainActivity.this, TestDargViewActivity.class);
            startActivity(intent);
        } else if (s.equals(mStrs[3])) {
            intent.setClass(MainActivity.this, TestRoundProgressActivity.class);
            startActivity(intent);
        } else if (s.equals(mStrs[4])) {
            intent.setClass(MainActivity.this, LuckPanActivity.class);
            startActivity(intent);
        } else if (s.equals(mStrs[5])) {
            intent.setClass(MainActivity.this, LuckPanActivity2.class);
            startActivity(intent);
        }

    }
}

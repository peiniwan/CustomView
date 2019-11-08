package com.ly.customview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ly.customview.utils.UiUtils;
import com.ly.customview.view.ColoredTextView;
import com.ly.customview.view.TagLayout;

public class MainActivity extends Activity {


    private String[] mStrs = new String[]{"Scrollview回弹效果", "视差特效", "拖拽view", "圆环进度条",
            "转盘", "转盘2", "MaterialEditText", "自定义drawabl，练习onMeasure","点赞","缩放图片"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout flContent = (FrameLayout) findViewById(R.id.flContent);
        ScrollView scrollView = new ScrollView(UiUtils.getContext());
        scrollView.setBackgroundResource(R.drawable.grid_item_bg_normal);
//        FlowlayoutView layout = new FlowlayoutView(UiUtils.getContext());
        TagLayout layout = new TagLayout(UiUtils.getContext());
//        Drawable pressedDrawable = DrawableUtils.createShape(backColor);// 按下显示的图片
        for (int i = 0; i < mStrs.length; i++) {
            final ColoredTextView textView = new ColoredTextView(UiUtils.getContext());
            String str = mStrs[i];
            textView.setText(str);
            int textPaddingV = UiUtils.dip2px(4);
            int textPaddingH = UiUtils.dip2px(7);
            textView.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);
            textView.setClickable(true);// 设置textView可以被点击
            textView.setOnClickListener(new View.OnClickListener() { // 设置点击事件

                @Override
                public void onClick(View v) {
                    String s = textView.getText().toString();
                    toActivity(s);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, -2);// -2 包裹内容
            layoutParams.setMargins(textPaddingH, textPaddingV, textPaddingH, textPaddingV);
            layout.addView(textView, layoutParams);
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
        } else if (s.equals(mStrs[6])) {
            intent.setClass(MainActivity.this, MaterialEditActivity.class);
            startActivity(intent);
        } else if (s.equals(mStrs[7])) {
            intent.setClass(MainActivity.this, CustomDrawableActivity.class);
            startActivity(intent);
        }else if (s.equals(mStrs[8])) {
            intent.setClass(MainActivity.this, LikeActivity.class);
            startActivity(intent);
        }else if (s.equals(mStrs[9])) {
            intent.setClass(MainActivity.this, ScalImageActivity.class);
            startActivity(intent);
        }
    }
}

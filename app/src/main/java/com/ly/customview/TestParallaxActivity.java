package com.ly.customview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.ly.customview.view.ParallaxListView;

/**
 * Created by liuyu1 on 2018/3/7.
 */

public class TestParallaxActivity extends Activity {

    private String[] NAMES = {"小王", "小李", "乘胜", "等等", "搜索", "啊啊", "嗯嗯", "问问", "让人", "天天", "一样", "咋做", "谢谢", "测测"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_2);

        final ParallaxListView mListView = (ParallaxListView) findViewById(R.id.lv);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        // 加Header
        final View mHeaderView = View.inflate(TestParallaxActivity.this, R.layout.view_header, null);
        final ImageView mImage = (ImageView) mHeaderView.findViewById(R.id.iv);
        mListView.addHeaderView(mHeaderView);

        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // 当布局填充结束之后, 此方法会被调用
                mListView.setParallaxImage(mImage);
                mHeaderView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        // 填充数据
        mListView.setAdapter(new ArrayAdapter<String>(TestParallaxActivity.this, android.R.layout.simple_list_item_1, NAMES));

    }

}

package com.ly.customview;

import android.app.Activity;
import android.os.Bundle;

import com.ly.customview.view.RoundProgress;

/**
 * Created by liuyu1 on 2018/3/7.
 */

public class TestRoundProgressActivity extends Activity {


    private RoundProgress progress;
    private int totalProgress = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_4);
        progress = (RoundProgress) findViewById(R.id.p_progresss);
        new Thread(runnable).start();

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int tempProgress = 0;
            try {
                while (tempProgress <= totalProgress) {
                    progress.setProgress(tempProgress);
                    tempProgress++;
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

}

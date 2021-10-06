package com.ly.customview;

import android.app.Activity;
import android.os.Bundle;

import com.ly.customview.view.large.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

public class LargeImageViewActivity extends Activity {

    private LargeImageView mLargeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image_view);

        mLargeImageView = findViewById(R.id.id_largetImageview);
        try {
            InputStream inputStream = getAssets().open("big.jpg");
            mLargeImageView.setInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

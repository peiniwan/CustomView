package com.ly.customview;

import android.app.Activity;
import android.os.Bundle;

import com.ly.customview.view.MaterialEditText;

public class MaterialEditActivity extends Activity {

    MaterialEditText met;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_edit);

        met = findViewById(R.id.met);
    }
}

package com.ly.customview;

import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ly.customview.utils.GifAdapter;

import java.util.ArrayList;
import java.util.List;

public class GIFActivity extends AppCompatActivity {
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        list.add("https://n.sinaimg.cn/tech/transform/769/w500h269/20200107/587b-imvsvyz4462987.gif");
        list.add("https://f.sinaimg.cn/tech/transform/73/w418h455/20200107/0497-imvsvyz4461438.gif");
        list.add("https://n.sinaimg.cn/tech/transform/38/w337h501/20200107/60f5-imvsvyz4460687.gif");
        list.add("https://n.sinaimg.cn/tech/transform/183/w605h378/20200107/9398-imvsvyz4460162.gif");

        RecyclerView rv= findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        GifAdapter gifAdapter = new GifAdapter();
        rv.setAdapter(gifAdapter);
        gifAdapter.updateAndNotify(list);
    }
}

package com.ly.customview.utils;

import android.support.annotation.NonNull;
import android.support.rastermill.FrameSequenceDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ly.customview.R;
import com.ly.customview.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.GifHolder> {
    private List<String> list = new ArrayList<>();

    public void updateAndNotify(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GifHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return GifHolder.onCreate(viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull GifHolder gifHolder, int i) {
        gifHolder.onBind(list.get(i));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class GifHolder extends RecyclerView.ViewHolder {

        public GifHolder(@NonNull View itemView) {
            super(itemView);
        }

        public static GifHolder onCreate(ViewGroup viewGroup) {
            return new GifHolder(LayoutInflater
                    .from(viewGroup.getContext())
                    .inflate(R.layout.item_gif, viewGroup, false));
        }

        public void onBind(String s) {
            ImageView iv = itemView.findViewById(R.id.iv);
//            Glide.with(itemView.getContext()).asGif().load(s).into(iv);
            GlideApp.with(itemView.getContext()).as(FrameSequenceDrawable.class).load(s).into(iv);
        }
    }
}

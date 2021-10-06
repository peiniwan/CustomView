package com.ly.customview.glide;

import android.support.annotation.NonNull;
import android.support.rastermill.FrameSequenceDrawable;


import com.bumptech.glide.load.resource.drawable.DrawableResource;

public class GifResouce extends DrawableResource<FrameSequenceDrawable> {
    public GifResouce(FrameSequenceDrawable drawable) {
        super(drawable);
    }

    @NonNull
    @Override
    public Class<FrameSequenceDrawable> getResourceClass() {
        return FrameSequenceDrawable.class;
    }

    @Override
    public int getSize() {
        return drawable.getIntrinsicHeight()*drawable.getIntrinsicWidth();
    }

    @Override
    public void recycle() {
        drawable.stop();
        drawable.destroy();
    }
}

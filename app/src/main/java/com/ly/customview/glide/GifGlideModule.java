package com.ly.customview.glide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.rastermill.FrameSequenceDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

@GlideModule
public class GifGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        registry.append(Registry.BUCKET_GIF,
                InputStream.class,
                FrameSequenceDrawable.class, new GifDecoder(glide.getBitmapPool()));
    }
}
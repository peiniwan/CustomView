package com.ly.customview.glide;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.rastermill.FrameSequence;
import android.support.rastermill.FrameSequenceDrawable;


import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.io.IOException;
import java.io.InputStream;


public class GifDecoder implements ResourceDecoder<InputStream, FrameSequenceDrawable> {
    private BitmapPool mBitmapPool;

    public GifDecoder(BitmapPool bitmapPool) {
        this.mBitmapPool = bitmapPool;
    }

    /**
     *
     * @param source
     * @param options
     * @return true 能够处理这个 source
     * @throws IOException
     */
    @Override
    public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
        return true;
    }

    @Nullable
    @Override
    public Resource<FrameSequenceDrawable> decode(@NonNull InputStream source, int width, int height, @NonNull Options options) throws IOException {
        FrameSequence frameSequence = FrameSequence.decodeStream(source);
        FrameSequenceDrawable frameSequenceDrawable =
                new FrameSequenceDrawable(frameSequence,
                        new FrameSequenceDrawable.BitmapProvider() {
                            @Override
                            public Bitmap acquireBitmap(int minwidth, int minHeight) {
                                //利用GLide的复用池
                                Bitmap bitmap = mBitmapPool.get(minwidth, minHeight, Bitmap.Config.ARGB_8888);
                                return bitmap;
                            }

                            @Override
                            public void releaseBitmap(Bitmap bitmap) {
                                mBitmapPool.put(bitmap);
                            }
                        });

//        frameSequenceDrawable.setLoopCount(100);
        frameSequenceDrawable.setLoopBehavior(FrameSequenceDrawable.LOOP_INF);
        return new GifResouce(frameSequenceDrawable);
    }
}

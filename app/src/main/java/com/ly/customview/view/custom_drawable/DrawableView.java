package com.ly.customview.view.custom_drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;



public class DrawableView extends View {
  Drawable drawable = new MeshDrawable();

  public DrawableView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    drawable.setBounds(50, 50, getWidth() - 50, getHeight() - 50);
    drawable.draw(canvas);
  }
}

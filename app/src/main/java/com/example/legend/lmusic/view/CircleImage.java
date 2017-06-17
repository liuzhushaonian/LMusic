package com.example.legend.lmusic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;



/**
 *
 * Created by legend on 2017/6/7.
 */

public class CircleImage extends AppCompatImageView {

    private Paint paint;
    private int radius;
    private float scale;
    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size=Math.min(getMeasuredWidth(),getMeasuredHeight());
        radius=size/2;
        setMeasuredDimension(size,size);
    }

    public CircleImage(Context context) {
        super(context);
    }

    public CircleImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        bitmap=drawableToBitmap(getDrawable());
        paint=new Paint();
        BitmapShader bitmapShader=new BitmapShader(bitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        scale=(radius*2.0f)/Math.min(bitmap.getHeight(),bitmap.getWidth());
        Matrix matrix=new Matrix();
        matrix.setScale(scale,scale);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(radius,radius,radius,paint);
    }

    protected Bitmap drawableToBitmap(Drawable drawable){
        if (drawable instanceof BitmapDrawable){
            BitmapDrawable bitmapDrawable= (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }
        int w=drawable.getIntrinsicWidth();
        int h=drawable.getIntrinsicHeight();
        Bitmap bitmap=Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        drawable.draw(canvas);
        return bitmap;
    }

    protected Bitmap getBitmap(){
        return bitmap;
    }


}

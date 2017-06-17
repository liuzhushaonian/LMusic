package com.example.legend.lmusic.view;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.example.legend.lmusic.R;

/**
 *弹动view
 * Created by legend on 2017/6/9.
 */

public class CircleView extends View{

    private Paint paint;
    private float offSet;
    private Path path;
    private int circleRadius;
    private int normalColor;
    private Bitmap bitmap;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public CircleView(Context context) {
//        this(context,null);
        super(context);
        System.out.println("BezierView.BezierView1");
    }



    public CircleView(Context context, @Nullable AttributeSet attrs) {

        super(context,attrs);
//        this(context,attrs,0);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        circleRadius= (int) typedArray.getDimension(R.styleable.CircleView_radius,50);
        normalColor=typedArray.getColor(R.styleable.CircleView_normalColor,0x03A9F4);
        typedArray.recycle();//关闭资源

        path=new Path();
    }


    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        System.out.println("绘制成圆形~~~~~~~~~");

        paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(normalColor);
        paint.setAntiAlias(true);
       canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,circleRadius,paint);

        if (bitmap!=null){
//            canvas.drawBitmap(bitmap,);
        }
    }

    private int getSpaceWidth(){
        return getMeasuredWidth()/2-circleRadius;
    }

    private int getSpaceHeight(){
        return getMeasuredHeight()/2-circleRadius;
    }


}

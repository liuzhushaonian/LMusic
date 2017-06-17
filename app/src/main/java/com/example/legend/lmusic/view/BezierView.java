package com.example.legend.lmusic.view;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;


import com.example.legend.lmusic.R;
import com.example.legend.lmusic.model.LApplication;

/**
 *弹动view
 * Created by legend on 2017/6/9.
 */

public class BezierView extends View implements ValueAnimator.AnimatorUpdateListener{

    private Paint paint;
    private float offSet;
    private Path path;
    private int circleRadius;
    private int extra;
    private float factor;
    private Bitmap bitmap;
    private int animationDuration;
    private int normalColor;
    public boolean isStatus;


    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }


    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public BezierView(Context context) {
//        this(context,null);
        super(context);
        System.out.println("BezierView.BezierView1");
    }



    public BezierView(Context context, @Nullable AttributeSet attrs) {

        super(context,attrs);
//        this(context,attrs,0);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.BezierView);

        circleRadius= (int) typedArray.getDimension(R.styleable.BezierView_circleRadius,50);
        animationDuration=typedArray.getInteger(R.styleable.BezierView_animationDuration,1000);
        normalColor=typedArray.getColor(R.styleable.BezierView_buttonNormalColor,0x03A9F4);
        isStatus=typedArray.getBoolean(R.styleable.BezierView_isStatus,false);
        typedArray.recycle();//关闭资源
        paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(normalColor);
        path=new Path();

//        System.out.println("得到了父布局的宽度---------------="+getMeasuredWidth());


        offSet=2*circleRadius/3.6f;

        extra=(int)(circleRadius*2*factor/6);
        factor=0;
        System.out.println("BezierView.BezierView2");


    }

    public void setFactor(float factor) {
        this.factor = factor;
        invalidate();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//

        System.out.println("BezierView.BezierView3");

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        System.out.println("绘制一次啦~~~~~~~~~");
        path.reset();

        extra = (int)(circleRadius * 2 * factor / 8);
        int xA =0,xB =0,xC =0,xD =0,yA =0,yB =0,yC =0,yD =0;


        switch (LApplication.controlMode) {

            case CONTROL_HORIZONTAL:
                switch (VDHLayout.viewPosition) {
                    case RIGHT_DOWN:
                    xA = circleRadius;
                    xB = circleRadius * 2;
                    xC = circleRadius;
                    xD = 0 - extra;
                    yA = extra;
                    yB = circleRadius;
                    yC = circleRadius * 2 - extra;
                    yD = circleRadius;
                        if (isStatus){
                            xA = circleRadius;
                            xB = circleRadius * 2 - extra;
                            xC = circleRadius;
                            xD = 0+extra;
                            yA = -extra;
                            yB = circleRadius;
                            yC = circleRadius * 2;
                            yD = circleRadius;
                        }


                    break;
                    case RIGHT_UP:
                        xA = circleRadius;
                        xB = circleRadius * 2;
                        xC = circleRadius;
                        xD = 0 - extra;
                        yA = extra;
                        yB = circleRadius;
                        yC = circleRadius * 2 - extra;
                        yD = circleRadius;

                        if (isStatus){
                            xA = circleRadius;
                            xB = circleRadius * 2 - extra;
                            xC = circleRadius;
                            xD = extra;
                            yA = 0;
                            yB = circleRadius;
                            yC = circleRadius * 2+extra;
                            yD = circleRadius;
                        }

                        break;
                    case LEFT_DOWN:
                    xA = circleRadius;
                    xB = circleRadius * 2 + extra;
                    xC = circleRadius;
                    xD = 0;
                    yA = extra;
                    yB = circleRadius;
                    yC = circleRadius * 2 - extra;
                    yD = circleRadius;

                        if (isStatus){
                            xA = circleRadius;
                            xB = circleRadius * 2 - extra;
                            xC = circleRadius;
                            xD = 0+extra;
                            yA = -extra;
                            yB = circleRadius;
                            yC = circleRadius * 2;
                            yD = circleRadius;
                        }
                    break;
                    case LEFT_UP:
                        xA = circleRadius;
                        xB = circleRadius * 2 + extra;
                        xC = circleRadius;
                        xD = 0;
                        yA = extra;
                        yB = circleRadius;
                        yC = circleRadius * 2 - extra;
                        yD = circleRadius;

                        if (isStatus){
                            xA = circleRadius;
                            xB = circleRadius * 2 - extra;
                            xC = circleRadius;
                            xD = extra;
                            yA = 0;
                            yB = circleRadius;
                            yC = circleRadius * 2+extra;
                            yD = circleRadius;
                        }

                        break;
                    default:
                        break;

                }

//                if (isStatus){
//                    xA = circleRadius;
//                    xB = circleRadius * 2 - extra;
//                    xC = circleRadius;
//                    xD = extra;
//                    yA = 0;
//                    yB = circleRadius;
//                    yC = circleRadius * 2+extra;
//                    yD = circleRadius;
//                }


                break;
            case CONTROL_VERTICAL:
                switch (VDHLayout.viewPosition) {
                    case RIGHT_DOWN:
                        xA = circleRadius;
                        xB = circleRadius * 2 - extra;
                        xC = circleRadius;
                        xD = 0+extra;
                        yA = -extra;
                        yB = circleRadius;
                        yC = circleRadius * 2;
                        yD = circleRadius;



//                        xA = circleRadius;
//                        xB = circleRadius * 2;
//                        xC = circleRadius;
//                        xD = 0 - extra;
//                        yA = extra;
//                        yB = circleRadius;
//                        yC = circleRadius * 2 - extra;
//                        yD = circleRadius;
                        if (isStatus){
//                            xA = circleRadius;
//                            xB = circleRadius * 2 - extra;
//                            xC = circleRadius;
//                            xD = 0+extra;
//                            yA = -extra;
//                            yB = circleRadius;
//                            yC = circleRadius * 2;
//                            yD = circleRadius;


                            xA = circleRadius;
                            xB = circleRadius * 2;
                            xC = circleRadius;
                            xD = 0 - extra;
                            yA = extra;
                            yB = circleRadius;
                            yC = circleRadius * 2 - extra;
                            yD = circleRadius;
                        }


                        break;
                    case RIGHT_UP:
                        xA = circleRadius;
                        xB = circleRadius * 2 - extra;
                        xC = circleRadius;
                        xD = extra;
                        yA = 0;
                        yB = circleRadius;
                        yC = circleRadius * 2+extra;
                        yD = circleRadius;


//                        xA = circleRadius;
//                        xB = circleRadius * 2;
//                        xC = circleRadius;
//                        xD = 0 - extra;
//                        yA = extra;
//                        yB = circleRadius;
//                        yC = circleRadius * 2 - extra;
//                        yD = circleRadius;

                        if (isStatus){
                            xA = circleRadius;
                            xB = circleRadius * 2;
                            xC = circleRadius;
                            xD = 0 - extra;
                            yA = extra;
                            yB = circleRadius;
                            yC = circleRadius * 2 - extra;
                            yD = circleRadius;

//
//                            xA = circleRadius;
//                            xB = circleRadius * 2 - extra;
//                            xC = circleRadius;
//                            xD = extra;
//                            yA = 0;
//                            yB = circleRadius;
//                            yC = circleRadius * 2+extra;
//                            yD = circleRadius;
                        }

                        break;
                    case LEFT_DOWN:
                        xA = circleRadius;
                        xB = circleRadius * 2 - extra;
                        xC = circleRadius;
                        xD = 0+extra;
                        yA = -extra;
                        yB = circleRadius;
                        yC = circleRadius * 2;
                        yD = circleRadius;



//                        xA = circleRadius;
//                        xB = circleRadius * 2 + extra;
//                        xC = circleRadius;
//                        xD = 0;
//                        yA = extra;
//                        yB = circleRadius;
//                        yC = circleRadius * 2 - extra;
//                        yD = circleRadius;

                        if (isStatus){
                            xA = circleRadius;
                            xB = circleRadius * 2 + extra;
                            xC = circleRadius;
                            xD = 0;
                            yA = extra;
                            yB = circleRadius;
                            yC = circleRadius * 2 - extra;
                            yD = circleRadius;


//                            xA = circleRadius;
//                            xB = circleRadius * 2 - extra;
//                            xC = circleRadius;
//                            xD = 0+extra;
//                            yA = -extra;
//                            yB = circleRadius;
//                            yC = circleRadius * 2;
//                            yD = circleRadius;
                        }
                        break;
                    case LEFT_UP:
                        xA = circleRadius;
                        xB = circleRadius * 2 - extra;
                        xC = circleRadius;
                        xD = extra;
                        yA = 0;
                        yB = circleRadius;
                        yC = circleRadius * 2+extra;
                        yD = circleRadius;

//
//                        xA = circleRadius;
//                        xB = circleRadius * 2 + extra;
//                        xC = circleRadius;
//                        xD = 0;
//                        yA = extra;
//                        yB = circleRadius;
//                        yC = circleRadius * 2 - extra;
//                        yD = circleRadius;

                        if (isStatus){
                            xA = circleRadius;
                            xB = circleRadius * 2 + extra;
                            xC = circleRadius;
                            xD = 0;
                            yA = extra;
                            yB = circleRadius;
                            yC = circleRadius * 2 - extra;
                            yD = circleRadius;


//                            xA = circleRadius;
//                            xB = circleRadius * 2 - extra;
//                            xC = circleRadius;
//                            xD = extra;
//                            yA = 0;
//                            yB = circleRadius;
//                            yC = circleRadius * 2+extra;
//                            yD = circleRadius;
                        }

                        break;
                    default:
                        break;
                }
                break;
        }



        Rect mRect = new Rect(getBitmapLeft(), getBitmapTop(), getBitmapWidth()+extra+getBitmapLeft(), getBitmapHeight()+getBitmapTop() );




        offSet=circleRadius*2/3.6f;
        path.moveTo(xA+getSpaceWidth(),yA+getSpaceWidth());
        path.cubicTo(xA +offSet+getSpaceWidth(),yA+getSpaceWidth(),xB+getSpaceWidth(),yB-offSet+getSpaceWidth(),xB+getSpaceWidth(),yB+getSpaceWidth());
        path.cubicTo(xB+getSpaceWidth(),yB+getSpaceWidth() +offSet,xC+getSpaceWidth() +offSet,yC+getSpaceWidth(),xC+getSpaceWidth(),yC+getSpaceWidth());
        path.cubicTo(xC+getSpaceWidth() -offSet,yC+getSpaceWidth(),xD+getSpaceWidth(),yD+getSpaceWidth()+offSet,xD+getSpaceWidth(),yD+getSpaceWidth());
        path.cubicTo(xD+getSpaceWidth(),yD +getSpaceWidth()-offSet,xA+getSpaceWidth()-offSet,yA+getSpaceWidth(),xA+getSpaceWidth(),yA+getSpaceWidth());

        canvas.drawPath(path,paint);


        if (bitmap!=null){
            canvas.drawBitmap(bitmap,null,mRect,paint);
        }


    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        factor= (float) animation.getAnimatedValue();
        invalidate();
    }

    public void startAnimation(long time,float startValue,float endValue){
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new FloatEvaluator(time,1,0),1,0);
        valueAnimator.addUpdateListener(this);
        valueAnimator.setDuration(time);
        valueAnimator.start();
    }

    public void startFactorAnimation(final long time, final float startValue, final float endValue) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startValue, endValue);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startAnimation(time * 5, endValue, startValue);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.addUpdateListener(this);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setDuration(time);
        valueAnimator.start();
    }



    public static class FloatEvaluator implements TypeEvaluator {

        private float[] value;
        private float dampingFactor = 5.f;
        private float velocityFactor = 30.f;
        private int sum;


        public FloatEvaluator(long time, float startValue, float endValue) {
            sum = (int)time * 60 / 1000;
            float diff = endValue - startValue;
            value = new float[sum];
            float x;
            for (int i = 0; i < sum; i++) {
                x = i * 1.0f / sum;
                value[i] = endValue - (float)(diff * Math.pow(Math.E, -1 * dampingFactor * x) * Math.cos(velocityFactor * x));
            }
        }



        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            return value[(int)((sum - 1) * fraction)];
        }
    }


    public void setBitmap(Bitmap bitmap,boolean refresh) {

        this.bitmap = bitmap;
        if (refresh){
            postInvalidate();
        }
    }

    private int getBitmapWidth(){
        return bitmap.getWidth();
    }

    private int getBitmapHeight(){
        return bitmap.getHeight();
    }

    private int getBitmapLeft(){
        return (getMeasuredWidth()-getBitmapWidth())/2;
    }

    private int getBitmapTop(){
        return (getMeasuredHeight()-getBitmapHeight())/2;
    }

    private int getSpaceWidth(){
        return getMeasuredWidth()/2-circleRadius;
    }

    private int getSpaceHeight(){
        return getMeasuredHeight()/2-circleRadius;
    }


}

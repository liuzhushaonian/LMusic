package com.example.legend.lmusic.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;

import com.example.legend.lmusic.model.LApplication;
import com.example.legend.lmusic.view.BezierView;
import com.example.legend.lmusic.view.HomeActivity;
import com.example.legend.lmusic.view.VDHLayout;
import com.example.legend.lmusic.view.ViewPosition;

import java.util.ArrayList;

/**
 *
 * Created by legend on 2017/6/11.
 */

public class AnimationUtil {

    private static final String HORIZONTAL="translationX";
    private static final String VERTICAL="translationY";


    public void openMenu(ViewPosition viewPosition, ArrayList<View> views){

        for (int i=0;i<views.size();i++) {
            ObjectAnimator objectAnimator1=null;
            ObjectAnimator objectAnimator2=null;

            switch (viewPosition) {
                case RIGHT_DOWN:
                    if (i!=3) {
                         objectAnimator1 = ObjectAnimator.ofFloat(views.get(i), HORIZONTAL, 0, -100 * i);
                    }
//                    System.out.println("这里执行了1--------="+objectAnimator2.toString());

                    if (i==3){
                         objectAnimator2=ObjectAnimator.ofFloat(views.get(3),VERTICAL,0,-100);

                    }
                    break;
                case RIGHT_UP:
                    if (i!=3) {
                        objectAnimator1 = ObjectAnimator.ofFloat(views.get(i), HORIZONTAL, 0, -100 * i);
                    }
                    if (i==3){
                        objectAnimator2=ObjectAnimator.ofFloat(views.get(3),VERTICAL,0,-100);

                    }

                    break;
                case LEFT_DOWN:

                    if (i!=3) {
                        objectAnimator1 = ObjectAnimator.ofFloat(views.get(i), HORIZONTAL, 0, -100 * i);
                    }
                    if (i==3){
                        objectAnimator2=ObjectAnimator.ofFloat(views.get(3),VERTICAL,0,-100);

                    }
                    break;
                case LEFT_UP:

                    if (i!=3) {
                        objectAnimator1 = ObjectAnimator.ofFloat(views.get(i), HORIZONTAL, 0, -100 * i);
                    }
                    if (i==3){
                        objectAnimator2=ObjectAnimator.ofFloat(views.get(3),VERTICAL,0,-100);

                    }
                    break;
            }

            objectAnimator1.setDuration(500);
            objectAnimator2.setDuration(500);
            AnimatorSet animatorSet=new AnimatorSet();
            animatorSet.setDuration(500);
            animatorSet.playTogether(objectAnimator1,objectAnimator2);
//            animatorSet.start();
            objectAnimator1.start();
            objectAnimator2.start();
        }

    }

    public void openMenuHorizontal(View view,int positopn){

        BezierView bezierView= (BezierView) view;
        int distance=view.getMeasuredHeight()+20;

        ViewPropertyAnimator propertyAnimator=view.animate();

        switch (LApplication.controlMode){
            case CONTROL_HORIZONTAL:
                switch (VDHLayout.viewPosition){
                    case RIGHT_DOWN:

                        if (bezierView.isStatus){//如果是状态按钮，则与其他动画方向不同，根据当前控制器位置选择弹出方向
                            propertyAnimator.translationY(-distance);

                        }else {
                            propertyAnimator.translationX(-distance*(positopn));
                        }
                        propertyAnimator.setDuration(500);
                        break;
                    case RIGHT_UP:
                        if (bezierView.isStatus){//如果是状态按钮，则与其他动画方向不同，根据当前控制器位置选择弹出方向
                            propertyAnimator.translationY(distance);
                        }else {
                            propertyAnimator.translationX(-distance*(positopn));
                        }

                        propertyAnimator.setDuration(500);
//                        propertyAnimator.translationX(-distance*(positopn+1));
                        break;
                    case LEFT_DOWN:

                        if (bezierView.isStatus){//如果是状态按钮，则与其他动画方向不同，根据当前控制器位置选择弹出方向
                            propertyAnimator.translationY(-distance);
                        }else {
                            propertyAnimator.translationX(distance*(positopn));
                        }

                        propertyAnimator.setDuration(500);
                        break;



                    case LEFT_UP:
                        if (bezierView.isStatus){//如果是状态按钮，则与其他动画方向不同，根据当前控制器位置选择弹出方向
                            propertyAnimator.translationY(distance);
                        }else {
                            propertyAnimator.translationX(distance*(positopn));
                        }


                        propertyAnimator.setDuration(500);
//                        propertyAnimator.translationX(distance*(positopn+1));
                        break;
                }

                break;
            case CONTROL_VERTICAL:
                switch (VDHLayout.viewPosition){
                    case RIGHT_DOWN:

                        if (bezierView.isStatus){
                            propertyAnimator.translationX(-distance);
                        }else {
                            propertyAnimator.translationY(-distance*(positopn));
                        }
                        propertyAnimator.setDuration(500);


                        break;
                    case RIGHT_UP:
                        if (bezierView.isStatus){
                            propertyAnimator.translationX(-distance);
                        }else {
                            propertyAnimator.translationY(distance*(positopn));
                        }
                        propertyAnimator.setDuration(500);
                        break;
                    case LEFT_DOWN:
                        if (bezierView.isStatus){
                            propertyAnimator.translationX(distance);
                        }else {
                            propertyAnimator.translationY(-distance*(positopn));
                        }
                        propertyAnimator.setDuration(500);
                        break;



                    case LEFT_UP:
                        if (bezierView.isStatus){
                            propertyAnimator.translationX(distance);
                        }else {
                            propertyAnimator.translationY(distance*(positopn));
                        }
                        propertyAnimator.setDuration(500);
                        break;
                }
                break;
            default:
                break;

        }

        propertyAnimator.start();
    }

    public void openMenuVertical(ViewPosition viewPosition,View view){

    }

    public void closeMenu(View view,int position){

        ViewPropertyAnimator propertyAnimator=view.animate();
        switch (VDHLayout.viewPosition){
            case RIGHT_DOWN:
            case RIGHT_UP:
                break;
            case LEFT_DOWN:
            case LEFT_UP:
                break;

        }
        propertyAnimator.setDuration(500);
        propertyAnimator.translationY(0);

        propertyAnimator.translationX(0);

        propertyAnimator.start();
    }


    /**
     * 旋转动画实现
     * @param view 旋转的view
     * @param isStart 是否旋转，判断是否应该暂停
     */
    public void startRotate(View view,boolean isStart){
        final ViewPropertyAnimator propertyAnimator=view.animate();
        propertyAnimator.rotationBy(359).setDuration(10000).setInterpolator(new LinearInterpolator());
        propertyAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                propertyAnimator.rotationBy(359);
            }
        });
        propertyAnimator.start();
        if (!isStart){
            propertyAnimator.cancel();
        }
    }

    public void popupSetting(View view){
        final ViewPropertyAnimator viewPropertyAnimator=view.animate();
        viewPropertyAnimator.translationY(-view.getMeasuredHeight()).setDuration(500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                viewPropertyAnimator.rotationBy(360).setDuration(20000);
            }

        });

        viewPropertyAnimator.start();
    }

    public void closeSetting(View view){
        ViewPropertyAnimator viewPropertyAnimator=view.animate();
        viewPropertyAnimator.translationY(0).setDuration(500);
        viewPropertyAnimator.start();
    }



}

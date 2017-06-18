package com.example.legend.lmusic.view;


import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.legend.lmusic.R;
import com.example.legend.lmusic.model.LApplication;


/**
 * 管理可滑动的Controller，提供组件所在屏幕的位置，以选择弹出方式
 * Created by legend on 2017/6/8.
 */

public class VDHLayout extends FrameLayout {

    private ViewDragHelper viewDragHelper;
    private View viewControl, viewPlay, viewPrevious, viewNext, viewStatus, viewSetting;
    private Point point = new Point();
    private PositionStatus p1 = new PositionStatus();
    private PositionStatus p2 = new PositionStatus();
    private PositionStatus p3 = new PositionStatus();
    private PositionStatus p4 = new PositionStatus();
    private PositionStatus p5 = new PositionStatus();
    public static ViewPosition viewPosition = ViewPosition.RIGHT_DOWN;


    private boolean isFirst = true;


    public VDHLayout(Context context) {
        super(context);
    }

    public VDHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        viewDragHelper = ViewDragHelper.create(this, 10.0f, new ViewDragHelper.Callback() {

            /**
             * 设置可以移动的view
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == viewControl;
            }

            /**
             * 抬起手指后触发
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (viewControl == releasedChild) {
                    point.y = releasedChild.getTop();
                    point.x = releasedChild.getLeft();

                    int left = getMeasuredWidth() - point.x;
                    int top = getMeasuredHeight() - point.y;
                    if (left >= getMeasuredWidth() / 2 && top >= getMeasuredHeight() / 2) {
                        viewPosition = ViewPosition.LEFT_UP;

                    }
                    if (left > getMeasuredWidth() / 2 && top < getMeasuredHeight() / 2) {
                        viewPosition = ViewPosition.LEFT_DOWN;

                    }
                    if (left < getMeasuredWidth() / 2 && top > getMeasuredHeight() / 2) {

                        viewPosition = ViewPosition.RIGHT_UP;

                    }
                    if (left <= getMeasuredWidth() / 2 && top <= getMeasuredHeight() / 2) {

                        viewPosition = ViewPosition.RIGHT_DOWN;

                    }
                }


            }


            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (left < 0) {
                    left = 0;

                } else if (left > getMeasuredWidth() - child.getMeasuredWidth()) {
                    left = getMeasuredWidth() - child.getMeasuredWidth();
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if (top < 0) {
                    top = 0;

                } else if (top > getMeasuredHeight() - child.getMeasuredHeight()) {
                    top = getMeasuredHeight() - child.getMeasuredHeight();
                }
                return top;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (changedView == viewControl) {


                    viewStatus.layout(viewStatus.getLeft() + dx, viewStatus.getTop() + dy, viewStatus.getRight() + dx, viewStatus.getBottom() + dy);
                    p4.setLeft(viewStatus.getLeft() + dx);
                    p4.setTop(viewStatus.getTop() + dy);

                    viewNext.layout(viewNext.getLeft() + dx, viewNext.getTop() + dy, viewNext.getRight() + dx, viewNext.getBottom() + dy);
                    p3.setLeft(viewNext.getLeft() + dx);
                    p3.setTop(viewNext.getTop() + dy);
                    viewPlay.layout(viewPlay.getLeft() + dx, viewPlay.getTop() + dy, viewPlay.getRight() + dx, viewPlay.getBottom() + dy);
                    p2.setLeft(viewPlay.getLeft() + dx);
                    p2.setTop(viewPlay.getTop() + dy);

                    viewPrevious.layout(viewPrevious.getLeft() + dx, viewPrevious.getTop() + dy, viewPrevious.getRight() + dx, viewPrevious.getBottom() + dy);
                    p1.setLeft(viewPrevious.getLeft() + dx);
                    p1.setTop(viewPrevious.getTop() + dy);

                    viewSetting.layout(viewSetting.getLeft() + dx, viewSetting.getTop() + dy, viewSetting.getRight() + dx, viewSetting.getBottom() + dy);
                    p5.setLeft(viewSetting.getLeft() + dx);
                    p5.setTop(viewSetting.getTop() + dy);
                }
            }
        });

        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

    }


    public VDHLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
//            invalidate();
            ViewCompat.postInvalidateOnAnimation(VDHLayout.this);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        viewPlay = getChildAt(3);
        viewPrevious = getChildAt(2);
        viewNext = getChildAt(4);
        viewStatus = getChildAt(5);
        viewSetting = getChildAt(6);
        viewControl = getChildAt(7);
//        viewControl= (BezierView) findViewById(R.id.controller);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


        int mViewGroupWidth = getMeasuredWidth();
        int mViewGroupHeight = getMeasuredHeight();

        int margin = LApplication.getMargin();

        int width = viewControl.getMeasuredWidth();
        int height = viewControl.getMeasuredHeight();

        int positionL = mViewGroupWidth - width - margin;
        int positionT = mViewGroupHeight - height - margin;

        int positionR = mViewGroupWidth - margin;

        int positionB = mViewGroupHeight - margin;

//        viewNext.layout(positionL, positionT, positionR, positionB);
//        viewPlay.layout(positionL, positionT, positionR, positionB);
//        viewPrevious.layout(positionL, positionT, positionR, positionB);
//        viewStatus.layout(positionL, positionT, positionR, positionB);

        //防止重布局后viewControl回到原位
        //如果是第一次布局，则默认记录下当前位置，并将第一次布局改为true，随后的布局将不再记录，但会记录point的位置，且以point的位置确定当前位置
        //
        if (isFirst) {
            viewNext.layout(positionL, positionT, positionR, positionB);
            viewPlay.layout(positionL, positionT, positionR, positionB);
            viewPrevious.layout(positionL, positionT, positionR, positionB);
            viewStatus.layout(positionL, positionT, positionR, positionB);
            viewControl.layout(positionL, positionT, positionR, positionB);
            viewSetting.layout(positionL, positionT, positionR, positionB);

            point.x = viewControl.getLeft();
            point.y = viewControl.getTop();
            p1.setLeft(viewPrevious.getLeft());
            p1.setTop(viewPrevious.getTop());
            p2.setLeft(viewPlay.getLeft());
            p2.setTop(viewPlay.getTop());
            p3.setLeft(viewNext.getLeft());
            p3.setTop(viewNext.getTop());
            p4.setLeft(viewStatus.getLeft());
            p4.setTop(viewStatus.getTop());
            p5.setLeft(viewSetting.getLeft());
            p5.setTop(viewSetting.getTop());


            isFirst = false;
//            viewControl.addOnLayoutChangeListener(this);
//            viewNext.addOnLayoutChangeListener(this);
//            viewPlay.addOnLayoutChangeListener(this);
//            viewPrevious.addOnLayoutChangeListener(this);
//            viewStatus.addOnLayoutChangeListener(this);
        } else {


            viewControl.layout(point.x, point.y, point.x + viewControl.getMeasuredWidth(), point.y + viewControl.getMeasuredHeight());
            viewPrevious.layout(p1.getLeft(), p1.getTop(), p1.getLeft() + viewPrevious.getMeasuredWidth(), p1.getTop() + viewPrevious.getMeasuredHeight());
            viewPlay.layout(p2.getLeft(), p2.getTop(), p2.getLeft() + viewPlay.getMeasuredWidth(), p2.getTop() + viewPlay.getMeasuredHeight());
            viewNext.layout(p3.getLeft(), p3.getTop(), p3.getLeft() + viewNext.getMeasuredWidth(), p3.getTop() + viewNext.getMeasuredHeight());
            viewStatus.layout(p4.getLeft(), p4.getTop(), p4.getLeft() + viewStatus.getMeasuredWidth(), p4.getTop() + viewStatus.getMeasuredHeight());
            viewSetting.layout(p5.getLeft(), p5.getTop(), p5.getLeft() + viewSetting.getMeasuredWidth(), p5.getTop() + viewSetting.getMeasuredHeight());


        }
//        viewControl.layout(positionL, positionT, positionR, positionB);
//        viewNext.layout(positionL, positionT, positionR, positionB);
//        viewPlay.layout(positionL, positionT, positionR, positionB);
//        viewPrevious.layout(positionL, positionT, positionR, positionB);
//        viewStatus.layout(positionL, positionT, positionR, positionB);


//        System.out.println("宽度是-----------="+width);
//        System.out.println("宽度是2-----------="+mViewGroupWidth);
//        System.out.println("1------------="+positionL);
//        System.out.println("2-------------="+positionT);
//        System.out.println("3------------="+positionR);
//        System.out.println("4--------------="+positionB);


    }

    static class PositionStatus {

        int top, left;

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

    }


}

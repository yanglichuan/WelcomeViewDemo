package com.example.administrator.welcomeviewdemo;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class MyWelcomeView extends RelativeLayout {
    private  Scroller mScroller;
    private ScrollerCompat scrollCompat;

    public MyWelcomeView(Context context) {
        super(context);
    }

    public MyWelcomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scrollCompat = ScrollerCompat.create(context);
        mScroller = new Scroller(context);
    }

    public MyWelcomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int getPx(int idp) {
        DisplayMetrics ccc = new DisplayMetrics();
        ((Activity) getContext())
                .getWindowManager().getDefaultDisplay().getMetrics(ccc);
        return (int) (ccc.density * (float)idp + 0.5f);
    }
    private ViewGroup child1;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        child1 = (ViewGroup) findViewById(R.id.child1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    int iRecordY = 0;

    private boolean isTouchPointInView(View view, float x, float y) {
        int[] locations = new int[2];

        view.getLocationInWindow(locations);

        int left = locations[0];
        int top = locations[1];
        int right = locations[0] + view.getMeasuredWidth();
        int bottom = locations[1] + view.getMeasuredHeight();
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    private boolean bOpen = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                iRecordY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getY();
                int deltaY = moveY - iRecordY;

                child1.setY(child1.getY()+ deltaY);

                iRecordY = moveY;

                int currentScrollY = (int) child1.getY();
                if(currentScrollY <= -child1.getHeight()){
                    child1.setY(-child1.getHeight());
                }else if(currentScrollY >= 0){
                    child1.setY(0);
                }

                break;

            case MotionEvent.ACTION_UP:
                currentScrollY = (int) child1.getY();
                if(!bOpen && currentScrollY <= -child1.getHeight()/10){
                    createValueAnimator(currentScrollY,-child1.getHeight());
                    bOpen = true;
                }else if(!bOpen && currentScrollY > -child1.getHeight()/10){
                    createValueAnimator(currentScrollY, 0);
                    bOpen = false;
                }else if(bOpen && currentScrollY > -child1.getHeight()*9/10){
                    createValueAnimator(currentScrollY, 0);
                    bOpen = false;
                }else if(bOpen && currentScrollY <= -child1.getHeight()/10){
                    createValueAnimator(currentScrollY,-child1.getHeight());
                    bOpen = true;
                }
                break;
        }
        return true;
    }

    private void createValueAnimator(int from, int to){
        final int distance = 1000;
        int dis = Math.abs(from - to);
        int xx = (int) ((float)dis/(float)distance);



        ValueAnimator valueAnimator = ValueAnimator.ofInt(from,to);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.setDuration(xx*300);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                child1.setY((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

//    @Override
//    public void computeScroll() {
//        super.computeScroll();
//        if(mScroller.computeScrollOffset()){
//            child1.scrollTo(0,mScroller.getCurrY());
//            child1.invalidate();
//        }
//    }
}

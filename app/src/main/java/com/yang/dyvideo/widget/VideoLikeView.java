package com.yang.dyvideo.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yang.dyvideo.R;
import com.yang.dyvideo.presenter.MyClickListener;

import java.util.Random;



/**
 * @author yangzc
 * @data 2019/5/31 13:13
 * @desc
 */
public class VideoLikeView extends RelativeLayout {
    private Context mContext;
    float[] num = {-30, -20, 0, 20, 30};//随机心形图片角度
    //记录上一次的点击时间
    private long  lastClickTime = 0;
    //点击的时间间隔
    private long INTERVAL = 200;
    private MyClickListener.MyClickCallBack onClickListener;

    public VideoLikeView(Context context) {
        super(context);
        initView(context);
    }

    public VideoLikeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VideoLikeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //获取点击时间
                long currTime = System.currentTimeMillis();
                //判断点击之间的时间差
                long  interval = currTime - lastClickTime;
                lastClickTime = currTime;
                if(interval <INTERVAL ){
                    final ImageView imageView = new ImageView(mContext);
                    //设置展示的位置，需要在手指触摸的位置上方，即触摸点是心形的右下角的位置
                    LayoutParams params = new LayoutParams(300, 300);
                    params.leftMargin = (int) event.getX() - 150;
                    params.topMargin = (int) event.getY() - 300;
                    //设置图片资源
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_heart));
                    imageView.setLayoutParams(params);
                    //把IV添加到父布局当中
                    addView(imageView);
                    //设置控件的动画
                    AnimatorSet animatorSet = new AnimatorSet();
                    //缩放动画，X轴2倍缩小至0.9倍
                    animatorSet.play(scale(imageView, "scaleX", 2f, 0.9f, 100, 0))
                            //缩放动画，Y轴2倍缩放至0.9倍
                            .with(scale(imageView, "scaleY", 2f, 0.9f, 100, 0))
                            //旋转动画，随机旋转角
                            .with(rotation(imageView, 0, 0, num[new Random().nextInt(4)]))
                            //渐变透明动画，透明度从0-1
                            .with(alpha(imageView, 0, 1, 100, 0))
                            //缩放动画，X轴0.9倍缩小至
                            .with(scale(imageView, "scaleX", 0.9f, 1, 50, 150))
                            //缩放动画，Y轴0.9倍缩放至
                            .with(scale(imageView, "scaleY", 0.9f, 1, 50, 150))
                            //位移动画，Y轴从0上移至600
                            .with(translationY(imageView, 0, -600, 800, 400))
                            //透明动画，从1-0
                            .with(alpha(imageView, 1, 0, 300, 400))
                            //缩放动画，X轴1至3倍
                            .with(scale(imageView, "scaleX", 1, 3f, 700, 400))
                            //缩放动画，Y轴1至3倍
                            .with(scale(imageView, "scaleY", 1, 3f, 700, 400));
                    //开始动画
                    animatorSet.start();
                    //设置动画结束监听
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //当动画结束以后，需要把控件从父布局移除
                            removeViewInLayout(imageView);
                        }
                    });
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }
    /**
     * 缩放动画
     * @param view
     * @param propertyName
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    public static ObjectAnimator scale(View view, String propertyName, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , propertyName
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }
    /**
     * 位移动画
     * @param view
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    public static ObjectAnimator translationX(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationX"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }
    public static ObjectAnimator translationY(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationY"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }
    /**
     * 透明度动画
     * @param view
     * @param from
     * @param to
     * @param time
     * @param delayTime
     * @return
     */
    public static ObjectAnimator alpha(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "alpha"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }
    public static ObjectAnimator rotation(View view, long time, long delayTime, float... values) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", values);
        rotation.setDuration(time);
        rotation.setStartDelay(delayTime);
        rotation.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return input;
            }
        });
        return rotation;
    }
    public void setOnClickListener(MyClickListener.MyClickCallBack onClickListener) {
        this.onClickListener = onClickListener;
    }
    public MyClickListener.MyClickCallBack getOnClickListener() {
        return onClickListener;
    }
}

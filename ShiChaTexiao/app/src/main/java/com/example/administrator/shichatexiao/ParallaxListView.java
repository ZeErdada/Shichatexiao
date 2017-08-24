package com.example.administrator.shichatexiao;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/8/16.
 * 1、继承ListView，复写构造方法
 * 2、复写overScrollBy方法，重点关注deltay，isToucEvent方法
 * 3、暴露一个方法，得到外界的ImageView，测量ImageView的高度
 * 4、复写OnTouchEvent方法
 */

public class ParallaxListView extends ListView{
    private ImageView iv_header;
    private int drawableHeight;
    private int orignalHeight;

    public ParallaxListView(Context context) {
        this(context,null);
    }
    public ParallaxListView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIv_header(ImageView iv_header) {
        this.iv_header = iv_header;
        //获取图片原始高度
        drawableHeight = iv_header.getDrawable().getIntrinsicHeight();
        //获取ImageView控件的原始高度，以便回弹时，回弹到原始高度
        orignalHeight = iv_header.getHeight();
    }

    /**
     * 滑动到ListView两端才调用
     * @param deltaY        $$
     * @param scrollY
     * @param scrollRangeY
     * @param maxOverScrollY
     * @param isTouchEvent      $$
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        System.out.println("deltaY=="+deltaY+"========="+"scrollY=="+scrollY+"========="+"scrollRangeY=="+scrollRangeY+"isTouchEvent=="+isTouchEvent);
        //顶部下拉，用户触摸操作才执行视差效果
        if (deltaY<0 && isTouchEvent){
            //deltay是负值，我们要改为绝对值，累计给我们的iv_header高度
            int newHeight = iv_header.getHeight()+ Math.abs(deltaY);
            //避免图片无限放大，使其最大不能超过本身的高度
            if (newHeight <= drawableHeight) {
                //把新的高度值赋值给控件，改变控件的高度
                iv_header.getLayoutParams().height = newHeight;
                iv_header.requestLayout();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                //把当前的头布局的高度恢复初始高度
                int currentHeight = iv_header.getHeight();
                //属性动画，改变高度的值，把我们当前的头布局的高度，改为原始的高度
                final ValueAnimator animator =  ValueAnimator.ofInt(currentHeight, orignalHeight);
                //动画更新的监听
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //获取动画执行中的分度值
                        float fraction = animator.getAnimatedFraction();
                        //获取中间值，并赋给控件的新高度，可以使控件有一个平稳回弹的效果
                        Integer animatedValue = (Integer) animator.getAnimatedValue();
                        //让新高度值生效
                        iv_header.getLayoutParams().height = animatedValue;
                        iv_header.requestLayout();
                    }
                });
                //动画回弹效果，值越大，回弹越厉害
                animator.setInterpolator(new OvershootInterpolator(2));
                //设置动画执行时间,单位毫秒
                animator.setDuration(2000);
                //动画执行
                animator.start();
                break;
        }
        return super.onTouchEvent(ev);
    }
}

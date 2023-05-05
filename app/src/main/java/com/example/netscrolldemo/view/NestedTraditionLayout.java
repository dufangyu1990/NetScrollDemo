package com.example.netscrolldemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.netscrolldemo.R;

/**
 * @author GMU
 * @date 2023/5/4 15:50
 * 传统处理嵌套滑动的方式，如果父控件拦截，根据传统事件分发机制，如果父控件确定拦截事件，那么在同一事件序列中，子控件是没有办法获取到事件，
 * 在下面的例子中，如果是同一事件序列中滑动导致headView隐藏，那么除非手指抬起，不然子控件是不能响应事件的。
 */
public class NestedTraditionLayout extends LinearLayout {
    private int mLastY;
    private boolean isHeadHide;
    private View mHeadView;
    private View mNavView;
    private ViewPager mViewPager;
    private final String TAG = "NestedTraditionLayout";
    private int mHeadTopHeight;
    public NestedTraditionLayout(Context context) {
        super(context);
    }

    public NestedTraditionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedTraditionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction()&MotionEvent.ACTION_MASK;
        int y = (int)ev.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = mLastY-y;
                // 根据传统事件传递机制，如果父控件确定拦截事件，那么在同一事件序列中，子控件是没有办法获取到事件的。
                if(Math.abs(dy) > ViewConfiguration.getTouchSlop()){
                    //如果手指向上滑动，且档期那的headView没有隐藏，那么拦截
                    if(dy>0 && !isHeadHide){
                        Log.d(TAG, "onInterceptTouchEvent: 开始向上拦截");
                        return true;
                    }else if(dy<0 && isHeadHide){
                        // 如果手指向下滑动，且headView已经隐藏，那么拦截
                        Log.d(TAG, "onInterceptTouchEvent: 开始向下拦截");
                        return true;
                    }
                }
                break;
        }
        // 不拦截事件，把事件让给子控件
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        int y = (int) event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = mLastY-y;
                if (Math.abs(dy) > ViewConfiguration.getTouchSlop()) {
                    scrollBy(0, dy);
                }
                mLastY = y;
                break;
        }
        return super.onTouchEvent(event);
    }
    /**
     * 重新scrollTo方法，因为scrollBy最终会调用，scrollTo方法
     */
    @Override
    public void scrollTo(int x, int y) {
        if(y<0){
            y=0;
        }
        if (y > mHeadTopHeight) {
            y = mHeadTopHeight;
        }
        super.scrollTo(x, y);
        //判断当前head是否已经隐藏了
        isHeadHide = getScrollY() == mHeadTopHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //ViewPager修改后的高度= 总高度-导航栏高度
        ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
        layoutParams.height = getMeasuredHeight() - mNavView.getMeasuredHeight();
        mViewPager.setLayoutParams(layoutParams);
        //当ViewPager修改高度后，重新开始测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取headView高度
        mHeadTopHeight = mHeadView.getMeasuredHeight();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeadView = findViewById(R.id.iv_head_image);
        mNavView = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        if (!(mViewPager instanceof ViewPager)) {
            throw new RuntimeException("id view_pager should be viewpager!");
        }
    }
}

package com.handsome.xrecyclerview.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.handsome.xrecyclerview.creator.LoadMoreViewCreator;
import com.handsome.xrecyclerview.creator.RefreshViewCreator;
import com.handsome.xrecyclerview.listener.OnLoadMoreListener;
import com.handsome.xrecyclerview.listener.OnRefreshListener;

/**
 * =====作者=====
 * 许英俊
 * =====时间=====
 * 2017/10/22.
 */

public class RefreshAndLoadMoreRecyclerView extends HeaderAndFooterRecyclerView {

    //------下拉刷新相关------
    // 下拉刷新的辅助类
    private RefreshViewCreator mRefreshCreator;
    // 下拉刷新头部的高度
    private int mRefreshViewHeight = 0;
    // 下拉刷新的头部View
    private View mRefreshView;
    // 手指按下的Y位置
    private int mFingerDownY;
    // 手指拖拽的阻力指数
    private float mDragIndex = 0.35f;
    // 当前是否正在拖动
    private boolean mCurrentDrag = false;
    // 当前是否满足可以刷新
    private boolean mIsRefreshNow = false;

    // 当前的状态
    private int mCurrentRefreshStatus;
    // 默认状态
    public static final int REFRESH_STATUS_NORMAL = 0x0011;
    // 下拉刷新状态
    public static final int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开刷新状态
    public static final int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;
    // 正在刷新状态
    public static final int REFRESH_STATUS_REFRESHING = 0x0044;

    private OnRefreshListener mRefreshListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
    }

    //------上拉加载相关------
    private LoadMoreViewCreator mLoadMoreCreator;
    private int mLoadMoreViewHeight = 0;
    private View mLoadMoreView;
    private boolean mIsLoadNow = false;

    // 当前的状态
    private int mCurrentLoadStatus;
    // 默认状态
    public static final int LOAD_STATUS_NORMAL = 0x0055;
    // 上拉加载更多状态
    public static final int LOAD_STATUS_PULL_DOWN_REFRESH = 0x0066;
    // 松开加载更多状态
    public static final int LOAD_STATUS_LOOSEN_LOADING = 0x0077;
    // 正在加载更多状态
    public static final int LOAD_STATUS_LOADING = 0x0088;

    private OnLoadMoreListener mLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    //------构造方法------
    public RefreshAndLoadMoreRecyclerView(Context context) {
        super(context);
    }

    public RefreshAndLoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshAndLoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 1、添加Creator
     *
     * @param refreshCreator
     */
    public void addRefreshViewCreator(RefreshViewCreator refreshCreator) {
        this.mRefreshCreator = refreshCreator;
        addRefreshView();
    }

    public void addLoadMoreViewCreator(LoadMoreViewCreator loadMoreCreator) {
        this.mLoadMoreCreator = loadMoreCreator;
        addLoadMoreView();
    }

    /**
     * 2、添加RefreshView为HeaderView
     */
    private void addRefreshView() {
        Adapter adapter = getAdapter();
        if (adapter != null && mRefreshCreator != null) {
            View refreshView = mRefreshCreator.getRefreshView(getContext(), this);
            if (refreshView != null) {
                addHeaderView(refreshView);
                this.mRefreshView = refreshView;
            }
        }
    }

    private void addLoadMoreView() {
        Adapter adapter = getAdapter();
        if (adapter != null && mLoadMoreCreator != null) {
            View loadMoreView = mLoadMoreCreator.getLoadView(getContext(), this);
            if (loadMoreView != null) {
                addFooterView(loadMoreView);
                this.mLoadMoreView = loadMoreView;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (mRefreshView != null && mRefreshViewHeight <= 0) {
                mRefreshViewHeight = mRefreshView.getMeasuredHeight();
                if (mRefreshViewHeight > 0) {
                    /**
                     * 3、通过设置负数的marginTop隐藏RefreshView，多留出1px防止无法判断是不是滚动到头部问题
                     */
                    setRefreshViewMarginTop(-mRefreshViewHeight + 1);
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /**
                 * 4、记录第一次按下的位置
                 */
                mFingerDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * 4、重置当前刷新状态
                 */
                if (mCurrentDrag) {
                    //这里的逻辑只执行一次
                    restoreRefreshView();
                    restoreLoadView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                /**
                 * 5、确定当前是否滑到了顶部
                 */
                mIsRefreshNow = (!(canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING)) && mRefreshListener != null;
                mIsLoadNow = (!(canScrollDown() || mCurrentLoadStatus == LOAD_STATUS_LOADING)) && mLoadMoreListener != null;

                if (!mIsRefreshNow && !mIsLoadNow) {
                    Log.e("TAG", "处于正常滑行");
                    return super.onTouchEvent(e);
                } else if (mIsRefreshNow) {
                    Log.e("TAG", "应该下拉刷新");
                    //6、滑到了顶部，并且手指已经松开，则不要在惯性的作用下实现下拉刷新
                    if (mCurrentDrag) {
                        scrollToPosition(0);
                    }
                    /**
                     * 7、滑到了顶部，获取手指触摸拖拽的距离，加上阻尼系数是因为太小的滑动距离则不处理
                     */
                    int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                    if (distanceY > 0) {
                        int marginTop = distanceY - mRefreshViewHeight;
                        setRefreshViewMarginTop(marginTop);
                        updateRefreshStatus(marginTop);
                        mCurrentDrag = true;
                        return false;
                    }
                    break;
                } else if (mIsLoadNow) {
                    Log.e("TAG", "应该上拉加载");

                    if (mCurrentDrag) {
                        scrollToPosition(getAdapter().getItemCount() - 1);
                    }

                    int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                    if (distanceY < 0) {
                        int marginBottom = -distanceY;
                        setLoadViewMarginBottom(marginBottom);
                        updateLoadStatus(marginBottom);
                        mCurrentDrag = true;
                        return true;
                    }
                }
        }
        return super.onTouchEvent(e);
    }

    /**
     * 重置当前刷新状态
     */
    private void restoreRefreshView() {
        int currentTopMargin = ((MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        int finalTopMargin = -mRefreshViewHeight + 1;
        // 松开刷新状态
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            // 将RefreshView显示出来
            finalTopMargin = 0;
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;

            if (mRefreshCreator != null) {
                mRefreshCreator.onRefreshing();
            }
            if (mRefreshListener != null) {
                mRefreshListener.onRefresh();
            }
        }
        int distance = currentTopMargin - finalTopMargin;

        // 回弹到指定位置
        ValueAnimator animator = ObjectAnimator.ofFloat(currentTopMargin, finalTopMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setRefreshViewMarginTop((int) currentTopMargin);
            }
        });
        animator.start();
        // 结束滑动
        mCurrentDrag = false;
    }

    private void restoreLoadView() {
        int currentBottomMargin = ((MarginLayoutParams) mLoadMoreView.getLayoutParams()).bottomMargin;
        int finalBottomMargin = 0;
        if (mCurrentLoadStatus == LOAD_STATUS_LOOSEN_LOADING) {
            mCurrentLoadStatus = LOAD_STATUS_LOADING;
            if (mLoadMoreCreator != null) {
                mLoadMoreCreator.onLoading();
            }
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore();
            }
        }
        int distance = currentBottomMargin - finalBottomMargin;

        ValueAnimator animator = ObjectAnimator.ofFloat(currentBottomMargin, finalBottomMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setLoadViewMarginBottom((int) currentTopMargin);
            }
        });
        animator.start();
        mCurrentDrag = false;
    }

    /**
     * 更新刷新的状态
     */
    private void updateRefreshStatus(int marginTop) {
        if (marginTop <= -mRefreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (marginTop < 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }

        if (mRefreshCreator != null) {
            mRefreshCreator.onPull(marginTop, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    private void updateLoadStatus(int marginBottom) {
        //特殊处理：获取高度
        mLoadMoreViewHeight = mLoadMoreView.getMeasuredHeight();

        if (marginBottom <= 0) {
            mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        } else if (marginBottom < mLoadMoreViewHeight) {
            mCurrentLoadStatus = LOAD_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentLoadStatus = LOAD_STATUS_LOOSEN_LOADING;
        }

        if (mLoadMoreCreator != null) {
            mLoadMoreCreator.onPull(marginBottom, mLoadMoreViewHeight, mCurrentLoadStatus);
        }
    }

    /**
     * 设置RefreshView的marginTop
     */
    private void setRefreshViewMarginTop(int marginTop) {
        MarginLayoutParams params = (MarginLayoutParams) mRefreshView.getLayoutParams();
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1;
        }
        params.topMargin = marginTop;
        mRefreshView.setLayoutParams(params);
    }

    public void setLoadViewMarginBottom(int marginBottom) {
        MarginLayoutParams params = (MarginLayoutParams) mLoadMoreView.getLayoutParams();
        if (marginBottom < 0) {
            marginBottom = 0;
        }
        params.bottomMargin = marginBottom;
        mLoadMoreView.setLayoutParams(params);
    }

    /**
     * 判断是否可以往上滑
     */
    private boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, -1);
        }
    }

    private boolean canScrollDown() {
        return ViewCompat.canScrollVertically(this, 1);
    }

    /**
     * 停止刷新
     */
    public void onStopRefresh() {
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        restoreRefreshView();
        if (mRefreshCreator != null) {
            mRefreshCreator.onStopRefresh();
        }
    }

    public void onStopLoad() {
        mCurrentLoadStatus = LOAD_STATUS_NORMAL;
        restoreLoadView();
        if (mLoadMoreCreator != null) {
            mLoadMoreCreator.onStopLoad();
        }
    }

}

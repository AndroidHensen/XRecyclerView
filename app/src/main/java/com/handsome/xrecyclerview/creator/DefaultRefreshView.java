package com.handsome.xrecyclerview.creator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.handsome.xrecyclerview.R;
import com.handsome.xrecyclerview.view.RefreshAndLoadMoreRecyclerView;

/**
 * =====作者=====
 * 许英俊
 * =====时间=====
 * 2017/10/22.
 */

public class DefaultRefreshView extends RefreshViewCreator {

    private ImageView mRefreshImageView;
    private TextView mRefreshTextView;

    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.item_refresh, parent, false);
        this.mRefreshImageView = (ImageView) refreshView.findViewById(R.id.iv_refresh);
        this.mRefreshTextView = (TextView) refreshView.findViewById(R.id.tv_refresh);
        this.mRefreshTextView.setText("下拉刷新");
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {

        float rotate = ((float) currentDragHeight) / refreshViewHeight;
        mRefreshImageView.setRotation(rotate * 180);

        if (currentRefreshStatus == RefreshAndLoadMoreRecyclerView.REFRESH_STATUS_LOOSEN_REFRESHING) {
            mRefreshTextView.setText("松开刷新");
        } else {
            mRefreshTextView.setText("下拉刷新");
        }
    }

    @Override
    public void onRefreshing() {
        RotateAnimation animation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);
        animation.setDuration(2000);
        mRefreshImageView.startAnimation(animation);
    }

    @Override
    public void onStopRefresh() {
        mRefreshTextView.setText("下拉刷新");
        mRefreshImageView.setRotation(0);
        mRefreshImageView.clearAnimation();
    }
}

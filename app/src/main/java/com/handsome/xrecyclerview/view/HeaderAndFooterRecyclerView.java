package com.handsome.xrecyclerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.handsome.xrecyclerview.listener.OnItemClickListener;

/**
 * =====作者=====
 * 许英俊
 * =====时间=====
 * 2017/10/21.
 */
public class HeaderAndFooterRecyclerView extends RecyclerView {

    private HeaderAndFooterRecyclerAdapter mHeaderAndFooterRecyclerAdapter;

    public HeaderAndFooterRecyclerView(Context context) {
        super(context);
    }

    public HeaderAndFooterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderAndFooterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null && adapter instanceof HeaderAndFooterRecyclerAdapter) {
            mHeaderAndFooterRecyclerAdapter = (HeaderAndFooterRecyclerAdapter) adapter;
        } else {
            throw new IllegalArgumentException("Adapter should be HeaderAndFooterRecyclerAdapter");
        }
        super.setAdapter(mHeaderAndFooterRecyclerAdapter);
    }

    /**
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mHeaderAndFooterRecyclerAdapter.setOnItemClickListener(listener);
    }

    /**
     * @param view
     */
    public void addHeaderView(View view) {
        if (mHeaderAndFooterRecyclerAdapter != null) {
            mHeaderAndFooterRecyclerAdapter.addHeaderView(view);
        }
    }

    /**
     * @param view
     */
    public void addFooterView(View view) {
        if (mHeaderAndFooterRecyclerAdapter != null) {
            mHeaderAndFooterRecyclerAdapter.addFooterView(view);
        }
    }

    /**
     * @param view
     */
    public void removeHeaderView(View view) {
        if (mHeaderAndFooterRecyclerAdapter != null) {
            mHeaderAndFooterRecyclerAdapter.removeHeaderView(view);
        }
    }

    /**
     * @param view
     */
    public void removeFooterView(View view) {
        if (mHeaderAndFooterRecyclerAdapter != null) {
            mHeaderAndFooterRecyclerAdapter.removeFooterView(view);
        }
    }

    /**
     *
     */
    public void notifyDataSetChanged() {
        if (mHeaderAndFooterRecyclerAdapter != null) {
            mHeaderAndFooterRecyclerAdapter.notifyDataSetChanged();
        }
    }

    /**
     *
     */
    public void adjustSpanSize() {
        mHeaderAndFooterRecyclerAdapter.adjustSpanSize(this);
    }

}

package com.handsome.xrecyclerview.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handsome.xrecyclerview.listener.OnItemClickListener;

import java.util.List;

/**
 * =====作者=====
 * 许英俊
 * =====时间=====
 * 2017/10/21.
 */
public abstract class HeaderAndFooterRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {

    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;
    private static int BASE_ITEM_TYPE_HEADER = 100000;
    private static int BASE_ITEM_TYPE_FOOTER = 200000;

    protected LayoutInflater inflater;
    protected Context context;
    protected List<T> list;
    protected int layoutId;

    public HeaderAndFooterRecyclerAdapter(Context context, List<T> list, int layoutId) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;

        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // HeaderView and FooterView
        if (isHeaderViewType(viewType)) {
            View headerView = mHeaderViews.get(viewType);
            return createHeaderFooterViewHolder(headerView);
        }
        if (isFooterViewType(viewType)) {
            View footerView = mFooterViews.get(viewType);
            return createHeaderFooterViewHolder(footerView);
        }
        // Normal View
        View itemView = inflater.inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        //For click event
        itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // HeaderView and FooterView
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }
        // Normal View
        position = position - mHeaderViews.size();
        convert(holder, list.get(position));
        //For click event
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemViewType(int position) {
        // Key for HeaderView and FooterView
        if (isHeaderPosition(position)) {
            return mHeaderViews.keyAt(position);
        }
        if (isFooterPosition(position)) {
            position = position - mHeaderViews.size() - list.size();
            return mFooterViews.keyAt(position);
        }
        // Normal View
        position = position - mHeaderViews.size();
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size() + mHeaderViews.size() + mFooterViews.size();
    }

    /**
     * Fill the Normal View
     *
     * @param viewHolder
     * @param item
     */
    public abstract void convert(ViewHolder viewHolder, T item);

    /**
     * Fill the HeaderView and FooterView
     * <p>
     * But the HeaderView and FooterView use the addHeaderView and addFooterView to fill
     *
     * @param view
     * @return
     */
    private ViewHolder createHeaderFooterViewHolder(View view) {
        return new ViewHolder(view);
    }


    /**
     * @param view
     */
    public void addHeaderView(View view) {
        int position = mHeaderViews.indexOfValue(view);
        if (position < 0) {
            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view);
        }
        notifyDataSetChanged();
    }

    /**
     * @param view
     */
    public void addFooterView(View view) {
        int position = mFooterViews.indexOfValue(view);
        if (position < 0) {
            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, view);
        }
        notifyDataSetChanged();
    }

    /**
     *
     */
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     *
     */
    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     *
     */
    private boolean isHeaderViewType(int viewType) {
        int position = mHeaderViews.indexOfKey(viewType);
        return position >= 0;
    }

    /**
     *
     */
    private boolean isFooterViewType(int viewType) {
        int position = mFooterViews.indexOfKey(viewType);
        return position >= 0;
    }

    /**
     *
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }

    /**
     *
     */
    private boolean isFooterPosition(int position) {
        return position >= (mHeaderViews.size() + list.size());
    }


    /**
     * Click Evnet
     */
    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    /**
     * 调整GridLayoutManager时候，HeaderView和FooterView始终为一行
     *
     * @param recycler
     */
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }
}

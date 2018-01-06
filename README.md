## 前言

RecyclerView已经完全替代了ListView的开发，其强大之处在于性能、内存、使用、灵活、丰富的API、动画、事件处理上都可以完美的体现出来，对于开发者来说RecyclerView是必须精通的一关，其主要的学习可以分为下面几大部分，在这几大部分中，还有很多需要解决的Bug和坑等着你，幸好的是有很多前辈已经实现大部分的功能和越过大部分的坑，学习这部分的内容完全就是去靠开源框架阅读来实现的，所以要成为高手，就必须学会阅读别人的代码，这里建议最好自己一行行写，然后手动调试每个关卡，你就会慢慢的熟悉RecyclerView，文末我会给出源码下载，大家可以进行阅读

* RecyclerView的基本使用
* RecyclerView的分割线
* RecyclerView的加载动画
* RecyclerView的点击事件
* RecyclerView与GridLayoutManager实现混排效果
* RecyclerView与ItemTouchHelper实现拖拽删除与拖拽排序
* RecyclerView与HorizontalScrollerView实现左滑删除菜单
* RecyclerView自定义增加Header和Footer
* RecyclerView自定义增加下拉刷新和上拉加载
* RecyclerView自定义LayoutManager实现不同的排版
* RecyclerView源码分析

## 框架内容

本文内容以下面内容为主

* 增加通用的Adapter
* 增加Item的点击事件
* 增加HeaderView和FooterView
* 增加下拉刷新和上拉加载

## 框架结构

![这里写图片描述](http://img.blog.csdn.net/20180107014820373?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzAzNzk2ODk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## 效果展示

![这里写图片描述](http://img.blog.csdn.net/20180107014842531?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzAzNzk2ODk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## 框架实现

框架实现的步骤

* 搭建通用Adapter
* 在通用Adapter的基础上增加Header和Footer的方法
* 自定义可以增加Header和Footer的HeaderAndFooterRecyclerView
* 继承HeaderAndFooterRecyclerView，增加下拉刷新和上拉加载功能

### 主界面实现

项目中通过自定义的RecyclerView和自定义的Adapter模拟本地数据进行加载

```
public class RefreshActivity extends AppCompatActivity implements OnItemClickListener {

    private RefreshAndLoadMoreRecyclerView recyclerView;
    private static List<Student> studentList;

 	/**
     * 模拟本地数据
     */
    static {
        studentList = new ArrayList<>();
        studentList.add(new Student("Jasper", "************", R.drawable.boy));
        studentList.add(new Student("小山竹", "************", R.drawable.girl));
        studentList.add(new Student("嗯哼大王", "************", R.drawable.boy));
        studentList.add(new Student("小泡芙", "************", R.drawable.girl));
        studentList.add(new Student("Max", "************", R.drawable.boy));
        studentList.add(new Student("neinei", "************", R.drawable.girl));
        studentList.add(new Student("Jasper", "************", R.drawable.boy));
        studentList.add(new Student("小山竹", "************", R.drawable.girl));
        studentList.add(new Student("嗯哼大王", "************", R.drawable.boy));
        studentList.add(new Student("小泡芙", "************", R.drawable.girl));
        studentList.add(new Student("Max", "************", R.drawable.boy));
        studentList.add(new Student("neinei", "************", R.drawable.girl));
        studentList.add(new Student("Jasper", "************", R.drawable.boy));
        studentList.add(new Student("小山竹", "************", R.drawable.girl));
        studentList.add(new Student("嗯哼大王", "************", R.drawable.boy));
        studentList.add(new Student("小泡芙", "************", R.drawable.girl));
        studentList.add(new Student("Max", "************", R.drawable.boy));
        studentList.add(new Student("neinei", "************", R.drawable.girl));
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    recyclerView.onStopRefresh();
                    break;
                case 1:
                    studentList.add(new Student("Jasper", "************", R.drawable.boy));
                    studentList.add(new Student("小山竹", "************", R.drawable.girl));
                    recyclerView.notifyDataSetChanged();
                    recyclerView.onStopLoad();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        recyclerView = (RefreshAndLoadMoreRecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new recyclerAdapter(this, studentList, R.layout.item_student));
        recyclerView.setOnItemClickListener(this);
        //Add the RefreshView
        recyclerView.addRefreshViewCreator(new DefaultRefreshView());
        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageDelayed(0, 2000);
            }
        });
        //Add the LoadMoreView
        recyclerView.addLoadMoreViewCreator(new DefaultLoadMoreView());
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mHandler.sendEmptyMessageDelayed(1, 2000);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "position:" + position, Toast.LENGTH_SHORT).show();
    }

    /**
     *
     */
    public class recyclerAdapter extends HeaderAndFooterRecyclerAdapter<Student> {

        public recyclerAdapter(Context context, List list, int layoutId) {
            super(context, list, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, Student item) {
            ImageView iv_avater = viewHolder.getView(R.id.iv_avater);
            TextView tv_username = viewHolder.getView(R.id.tv_username);
            TextView tv_phone = viewHolder.getView(R.id.tv_phone);

            iv_avater.setBackgroundResource(item.getAvater());
            tv_username.setText(item.getUsername());
            tv_phone.setText(item.getPhone());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "添加HeaderView");
        menu.add(0, 2, 0, "添加FooterView");
        menu.add(0, 3, 0, "切换LinearLayoutManager");
        menu.add(0, 4, 0, "切换GridLayoutManager");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                recyclerView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header, recyclerView, false));
                break;
            case 2:
                recyclerView.addFooterView(LayoutInflater.from(this).inflate(R.layout.item_footer, recyclerView, false));
                break;
            case 3:
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case 4:
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                recyclerView.adjustSpanSize();
                break;
        }
        return true;
    }
}
```

### HeaderView和FooterView

由于搭建通用的Adapter已经在ListView的时候文章中讲过了，RecyclerView的通用的Adapter大同小异，只要改一小段代码即可，这里就直接忽略了，如果想看ListView的通用Adapter请在博客中查找快速开发之打造万能适配器，如果需要单独抽取通用Adapter，可以将关于Header和Footer的代码删掉即可

这里需要掌握的知识点有

* SparseArray的正确使用方法
* getItemViewType、onBindViewHolder、onCreateViewHolder的调用时机
* GridLayoutManager的处理
* 点击事件的处理

```
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
```

其中使用到的ViewHolder

```
public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> views;

    public ViewHolder(View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }

    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
}
```

最后，我们自定义HeaderAndFooterRecyclerView，将所有的方法包裹在RecyclerView中

```
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
```

这样我们就可以在Activity中简单的使用带有Header和Footer的RecyclerView了

```
recyclerView = (HeaderAndFooterRecyclerView) findViewById(R.id.rv);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
recyclerView.setAdapter(new recyclerAdapter(this, studentList, R.layout.item_student));
recyclerView.setOnItemClickListener(this);

recyclerView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header, recyclerView, false));
recyclerView.addFooterView(LayoutInflater.from(this).inflate(R.layout.item_footer, recyclerView, false));
```

### 下拉刷新和上拉加载

下拉刷新和上拉加载是基于Header和Footer的RecyclerView实现的,首先我们抽象出即将加入的Header的刷新View

```
public abstract class RefreshViewCreator {

    /**
     * 获取下拉刷新的View
     *
     * @param context 上下文
     * @param parent  RecyclerView
     */
    public abstract View getRefreshView(Context context, ViewGroup parent);

    /**
     * 正在下拉
     *
     * @param currentDragHeight    当前拖动的高度
     * @param refreshViewHeight    总的刷新高度
     * @param currentRefreshStatus 当前状态
     */
    public abstract void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus);

    /**
     * 正在刷新中
     */
    public abstract void onRefreshing();

    /**
     * 停止刷新
     */
    public abstract void onStopRefresh();
}
```

实现默认的刷新View

```
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
```

这里我将上拉加载的代码去掉，为了方便理解，请按步骤提示阅读

```
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
}
```

这样我们就可以直接界面中直接使用下拉刷新了

```
recyclerView = (RefreshAndLoadMoreRecyclerView) findViewById(R.id.rv);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
recyclerView.setAdapter(new recyclerAdapter(this, studentList, R.layout.item_student));
recyclerView.setOnItemClickListener(this);
//Add the RefreshView
recyclerView.addRefreshViewCreator(new DefaultRefreshView());
recyclerView.setOnRefreshListener(new OnRefreshListener() {
    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }
});
```

上拉加载的代码与下拉刷新的大同小异，相信你会了下拉刷新后，上拉加载是很简单的

### 源码下载

[源码下载](https://github.com/AndroidHensen/XRecyclerView)
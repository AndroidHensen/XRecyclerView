## ǰ��

RecyclerView�Ѿ���ȫ�����ListView�Ŀ�������ǿ��֮���������ܡ��ڴ桢ʹ�á����ḻ��API���������¼������϶��������������ֳ��������ڿ�������˵RecyclerView�Ǳ��뾫ͨ��һ�أ�����Ҫ��ѧϰ���Է�Ϊ���漸�󲿷֣����⼸�󲿷��У����кܶ���Ҫ�����Bug�Ϳӵ����㣬�Һõ����кܶ�ǰ���Ѿ�ʵ�ִ󲿷ֵĹ��ܺ�Խ���󲿷ֵĿӣ�ѧϰ�ⲿ�ֵ�������ȫ����ȥ����Դ����Ķ���ʵ�ֵģ�����Ҫ��Ϊ���֣��ͱ���ѧ���Ķ����˵Ĵ��룬���ｨ������Լ�һ����д��Ȼ���ֶ�����ÿ���ؿ�����ͻ���������ϤRecyclerView����ĩ�һ����Դ�����أ���ҿ��Խ����Ķ�

* RecyclerView�Ļ���ʹ��
* RecyclerView�ķָ���
* RecyclerView�ļ��ض���
* RecyclerView�ĵ���¼�
* RecyclerView��GridLayoutManagerʵ�ֻ���Ч��
* RecyclerView��ItemTouchHelperʵ����קɾ������ק����
* RecyclerView��HorizontalScrollerViewʵ����ɾ���˵�
* RecyclerView�Զ�������Header��Footer
* RecyclerView�Զ�����������ˢ�º���������
* RecyclerView�Զ���LayoutManagerʵ�ֲ�ͬ���Ű�
* RecyclerViewԴ�����

## �������

������������������Ϊ��

* ����ͨ�õ�Adapter
* ����Item�ĵ���¼�
* ����HeaderView��FooterView
* ��������ˢ�º���������

## ��ܽṹ

![����дͼƬ����](http://img.blog.csdn.net/20180107014820373?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzAzNzk2ODk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## Ч��չʾ

![����дͼƬ����](http://img.blog.csdn.net/20180107014842531?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzAzNzk2ODk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## ���ʵ��

���ʵ�ֵĲ���

* �ͨ��Adapter
* ��ͨ��Adapter�Ļ���������Header��Footer�ķ���
* �Զ����������Header��Footer��HeaderAndFooterRecyclerView
* �̳�HeaderAndFooterRecyclerView����������ˢ�º��������ع���

### ������ʵ��

��Ŀ��ͨ���Զ����RecyclerView���Զ����Adapterģ�Ȿ�����ݽ��м���

```
public class RefreshActivity extends AppCompatActivity implements OnItemClickListener {

    private RefreshAndLoadMoreRecyclerView recyclerView;
    private static List<Student> studentList;

 	/**
     * ģ�Ȿ������
     */
    static {
        studentList = new ArrayList<>();
        studentList.add(new Student("Jasper", "************", R.drawable.boy));
        studentList.add(new Student("Сɽ��", "************", R.drawable.girl));
        studentList.add(new Student("�źߴ���", "************", R.drawable.boy));
        studentList.add(new Student("С��ܽ", "************", R.drawable.girl));
        studentList.add(new Student("Max", "************", R.drawable.boy));
        studentList.add(new Student("neinei", "************", R.drawable.girl));
        studentList.add(new Student("Jasper", "************", R.drawable.boy));
        studentList.add(new Student("Сɽ��", "************", R.drawable.girl));
        studentList.add(new Student("�źߴ���", "************", R.drawable.boy));
        studentList.add(new Student("С��ܽ", "************", R.drawable.girl));
        studentList.add(new Student("Max", "************", R.drawable.boy));
        studentList.add(new Student("neinei", "************", R.drawable.girl));
        studentList.add(new Student("Jasper", "************", R.drawable.boy));
        studentList.add(new Student("Сɽ��", "************", R.drawable.girl));
        studentList.add(new Student("�źߴ���", "************", R.drawable.boy));
        studentList.add(new Student("С��ܽ", "************", R.drawable.girl));
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
                    studentList.add(new Student("Сɽ��", "************", R.drawable.girl));
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
        menu.add(0, 1, 0, "���HeaderView");
        menu.add(0, 2, 0, "���FooterView");
        menu.add(0, 3, 0, "�л�LinearLayoutManager");
        menu.add(0, 4, 0, "�л�GridLayoutManager");
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

### HeaderView��FooterView

���ڴͨ�õ�Adapter�Ѿ���ListView��ʱ�������н����ˣ�RecyclerView��ͨ�õ�Adapter��ͬС�죬ֻҪ��һС�δ��뼴�ɣ������ֱ�Ӻ����ˣ�����뿴ListView��ͨ��Adapter���ڲ����в��ҿ��ٿ���֮���������������������Ҫ������ȡͨ��Adapter�����Խ�����Header��Footer�Ĵ���ɾ������

������Ҫ���յ�֪ʶ����

* SparseArray����ȷʹ�÷���
* getItemViewType��onBindViewHolder��onCreateViewHolder�ĵ���ʱ��
* GridLayoutManager�Ĵ���
* ����¼��Ĵ���

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
     * ����GridLayoutManagerʱ��HeaderView��FooterViewʼ��Ϊһ��
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

����ʹ�õ���ViewHolder

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

��������Զ���HeaderAndFooterRecyclerView�������еķ���������RecyclerView��

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

�������ǾͿ�����Activity�м򵥵�ʹ�ô���Header��Footer��RecyclerView��

```
recyclerView = (HeaderAndFooterRecyclerView) findViewById(R.id.rv);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
recyclerView.setAdapter(new recyclerAdapter(this, studentList, R.layout.item_student));
recyclerView.setOnItemClickListener(this);

recyclerView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header, recyclerView, false));
recyclerView.addFooterView(LayoutInflater.from(this).inflate(R.layout.item_footer, recyclerView, false));
```

### ����ˢ�º���������

����ˢ�º����������ǻ���Header��Footer��RecyclerViewʵ�ֵ�,�������ǳ�������������Header��ˢ��View

```
public abstract class RefreshViewCreator {

    /**
     * ��ȡ����ˢ�µ�View
     *
     * @param context ������
     * @param parent  RecyclerView
     */
    public abstract View getRefreshView(Context context, ViewGroup parent);

    /**
     * ��������
     *
     * @param currentDragHeight    ��ǰ�϶��ĸ߶�
     * @param refreshViewHeight    �ܵ�ˢ�¸߶�
     * @param currentRefreshStatus ��ǰ״̬
     */
    public abstract void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus);

    /**
     * ����ˢ����
     */
    public abstract void onRefreshing();

    /**
     * ֹͣˢ��
     */
    public abstract void onStopRefresh();
}
```

ʵ��Ĭ�ϵ�ˢ��View

```
public class DefaultRefreshView extends RefreshViewCreator {

    private ImageView mRefreshImageView;
    private TextView mRefreshTextView;

    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.item_refresh, parent, false);
        this.mRefreshImageView = (ImageView) refreshView.findViewById(R.id.iv_refresh);
        this.mRefreshTextView = (TextView) refreshView.findViewById(R.id.tv_refresh);
        this.mRefreshTextView.setText("����ˢ��");
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {

        float rotate = ((float) currentDragHeight) / refreshViewHeight;
        mRefreshImageView.setRotation(rotate * 180);

        if (currentRefreshStatus == RefreshAndLoadMoreRecyclerView.REFRESH_STATUS_LOOSEN_REFRESHING) {
            mRefreshTextView.setText("�ɿ�ˢ��");
        } else {
            mRefreshTextView.setText("����ˢ��");
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
        mRefreshTextView.setText("����ˢ��");
        mRefreshImageView.setRotation(0);
        mRefreshImageView.clearAnimation();
    }
}
```

�����ҽ��������صĴ���ȥ����Ϊ�˷�����⣬�밴������ʾ�Ķ�

```
public class RefreshAndLoadMoreRecyclerView extends HeaderAndFooterRecyclerView {

    //------����ˢ�����------
    // ����ˢ�µĸ�����
    private RefreshViewCreator mRefreshCreator;
    // ����ˢ��ͷ���ĸ߶�
    private int mRefreshViewHeight = 0;
    // ����ˢ�µ�ͷ��View
    private View mRefreshView;
    // ��ָ���µ�Yλ��
    private int mFingerDownY;
    // ��ָ��ק������ָ��
    private float mDragIndex = 0.35f;
    // ��ǰ�Ƿ������϶�
    private boolean mCurrentDrag = false;
    // ��ǰ�Ƿ��������ˢ��
    private boolean mIsRefreshNow = false;

    // ��ǰ��״̬
    private int mCurrentRefreshStatus;
    // Ĭ��״̬
    public static final int REFRESH_STATUS_NORMAL = 0x0011;
    // ����ˢ��״̬
    public static final int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // �ɿ�ˢ��״̬
    public static final int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;
    // ����ˢ��״̬
    public static final int REFRESH_STATUS_REFRESHING = 0x0044;

    private OnRefreshListener mRefreshListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
    }

    //------���췽��------
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
     * 1�����Creator
     *
     * @param refreshCreator
     */
    public void addRefreshViewCreator(RefreshViewCreator refreshCreator) {
        this.mRefreshCreator = refreshCreator;
        addRefreshView();
    }

    /**
     * 2�����RefreshViewΪHeaderView
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
                     * 3��ͨ�����ø�����marginTop����RefreshView��������1px��ֹ�޷��ж��ǲ��ǹ�����ͷ������
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
                 * 4����¼��һ�ΰ��µ�λ��
                 */
                mFingerDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * 4�����õ�ǰˢ��״̬
                 */
                if (mCurrentDrag) {
                    //������߼�ִֻ��һ��
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
                 * 5��ȷ����ǰ�Ƿ񻬵��˶���
                 */
                mIsRefreshNow = (!(canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING)) && mRefreshListener != null;
                mIsLoadNow = (!(canScrollDown() || mCurrentLoadStatus == LOAD_STATUS_LOADING)) && mLoadMoreListener != null;

                if (!mIsRefreshNow && !mIsLoadNow) {
                    Log.e("TAG", "������������");
                    return super.onTouchEvent(e);
                } else if (mIsRefreshNow) {
                    Log.e("TAG", "Ӧ������ˢ��");
                    //6�������˶�����������ָ�Ѿ��ɿ�����Ҫ�ڹ��Ե�������ʵ������ˢ��
                    if (mCurrentDrag) {
                        scrollToPosition(0);
                    }
                    /**
                     * 7�������˶�������ȡ��ָ������ק�ľ��룬��������ϵ������Ϊ̫С�Ļ��������򲻴���
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
                    Log.e("TAG", "Ӧ����������");

                }
        }
        return super.onTouchEvent(e);
    }

    /**
     * ���õ�ǰˢ��״̬
     */
    private void restoreRefreshView() {
        int currentTopMargin = ((MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        int finalTopMargin = -mRefreshViewHeight + 1;
        // �ɿ�ˢ��״̬
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            // ��RefreshView��ʾ����
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

        // �ص���ָ��λ��
        ValueAnimator animator = ObjectAnimator.ofFloat(currentTopMargin, finalTopMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setRefreshViewMarginTop((int) currentTopMargin);
            }
        });
        animator.start();
        // ��������
        mCurrentDrag = false;
    }

    /**
     * ����ˢ�µ�״̬
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
     * ����RefreshView��marginTop
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
     * �ж��Ƿ�������ϻ�
     */
    private boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, -1);
        }
    }

    /**
     * ֹͣˢ��
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

�������ǾͿ���ֱ�ӽ�����ֱ��ʹ������ˢ����

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

�������صĴ���������ˢ�µĴ�ͬС�죬�������������ˢ�º����������Ǻܼ򵥵�

### Դ������

[Դ������](https://github.com/AndroidHensen/XRecyclerView)
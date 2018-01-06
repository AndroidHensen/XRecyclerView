package com.handsome.xrecyclerview.sample;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.handsome.xrecyclerview.R;
import com.handsome.xrecyclerview.bean.Student;
import com.handsome.xrecyclerview.creator.DefaultLoadMoreView;
import com.handsome.xrecyclerview.creator.DefaultRefreshView;
import com.handsome.xrecyclerview.listener.OnItemClickListener;
import com.handsome.xrecyclerview.listener.OnLoadMoreListener;
import com.handsome.xrecyclerview.listener.OnRefreshListener;
import com.handsome.xrecyclerview.view.HeaderAndFooterRecyclerAdapter;
import com.handsome.xrecyclerview.view.RefreshAndLoadMoreRecyclerView;
import com.handsome.xrecyclerview.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

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

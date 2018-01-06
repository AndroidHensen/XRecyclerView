package com.handsome.xrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.handsome.xrecyclerview.R;
import com.handsome.xrecyclerview.sample.HeaderAndFooterActivity;
import com.handsome.xrecyclerview.sample.RefreshActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void go2HeaderAndFooter(View view) {
        startActivity(new Intent(this, HeaderAndFooterActivity.class));
    }

    public void go2Refresh(View view) {
        startActivity(new Intent(this, RefreshActivity.class));
    }
}

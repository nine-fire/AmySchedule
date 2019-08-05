package com.wbu.xiaowei.amyschedule.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wbu.xiaowei.amyschedule.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

package com.wbu.xiaowei.amyschedule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wbu.xiaowei.amyschedule.R;
import com.wbu.xiaowei.amyschedule.adapter.ScoreListAdapter;
import com.wbu.xiaowei.amyschedule.bean.ScoreBase;
import com.wbu.xiaowei.amyschedule.net.ScoreBeanListCallback;
import com.wbu.xiaowei.amyschedule.other.SimpleDividerItemDecoration;
import com.wbu.xiaowei.amyschedule.util.NetWorkUtils;
import com.wbu.xiaowei.amyschedule.util.ScheduleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

public class MyScoreActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = MyScoreActivity.class.getSimpleName();

    // 控件
    private LinearLayout errorAlertView;
    private TextView errorInfoTv;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView scoreRecyclerView;

    private List<ScoreBase> scoreList;
    private ScoreListAdapter scoreListAdapter;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ScheduleUtils.SCORE_LIST_OK:
                    // 显示成绩
                    List<ScoreBase> list = (List<ScoreBase>) msg.obj;
                    if (list != null && !list.isEmpty()) {
                        Collections.reverse(list);
                        scoreList.clear();
                        scoreList.addAll(list);
                        scoreListAdapter.notifyItemRangeChanged(0, list.size());
                    }
                    refreshLayout.setRefreshing(false);
                    break;
                case ScheduleUtils.NETWORK_UNAVAILABLE:
                    refreshLayout.setRefreshing(false);
                    showErrorInfo("网络出现问题 请稍后再试哒~");
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_score);
        initView();
        getScore();
    }

    /**
     * 初始化
     */
    private void initView() {
        ImageButton returnBtn = findViewById(R.id.back_btn_in_my_score_page);
        returnBtn.setColorFilter(getResources().getColor(R.color.white));
        returnBtn.setOnClickListener(this);

        refreshLayout = findViewById(R.id.swipe_refresh_in_my_score_page);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getScore();
            }
        });

        errorAlertView = findViewById(R.id.net_err_alert);
        errorInfoTv = findViewById(R.id.err_info_tv);
        scoreRecyclerView = findViewById(R.id.score_recycler_view);

        scoreList = new ArrayList<>();
        scoreListAdapter = new ScoreListAdapter(scoreList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 设置布局管理器
        scoreRecyclerView.setLayoutManager(layoutManager);
        // 设置为垂直布局
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        // 设置 Adapter
        scoreRecyclerView.setAdapter(scoreListAdapter);
        // 设置分割线
        scoreRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        // 设置增加或删除条目的动画
        scoreRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn_in_my_score_page:
                finish();
                break;
        }
    }

    /**
     * 获取课表
     */
    private void getScore() {
        refreshLayout.setRefreshing(true);
        boolean isNetWorkAvailable = NetWorkUtils.isNetWorkAvailable(this);
        if (!isNetWorkAvailable) {
            refreshLayout.setRefreshing(false);
            showErrorInfo("你别逗我了 请打开网络先");
            return;
        }

        // 获取成绩
        int flag = ScheduleUtils.getScore(new ScoreBeanListCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Message msg = Message.obtain();
                msg.what = ScheduleUtils.NETWORK_UNAVAILABLE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(List<ScoreBase> scoreList, int id) {
                Message msg = Message.obtain();
                msg.what = ScheduleUtils.SCORE_LIST_OK;
                msg.obj = scoreList;
                handler.sendMessage(msg);
            }
        });

        switch (flag) {
            case ScheduleUtils.NOT_LOGIN:
                Log.d(TAG, "getScore: case ScheduleUtils.NOT_LOGIN: ScheduleUtils.userInfo == null: 没有用户信息...");
                refreshLayout.setRefreshing(false);
                showErrorInfo("你还尚未登录 2秒后去登录");
                toLoginActivity(); // 跳转到登录界面
                break;
            case ScheduleUtils.PERFECT:
                Log.d(TAG, "getScore: case ScheduleUtils.PERFECT");
                break;
        }
    }

    /**
     * 显示错误提示
     *
     * @param text 错误提示文字
     */
    private void showErrorInfo(String text) {
        Snackbar.make(scoreRecyclerView, text, Snackbar.LENGTH_INDEFINITE);
    }

    /**
     * 跳转到登录界面
     */
    private void toLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                intent.setClass(MyScoreActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

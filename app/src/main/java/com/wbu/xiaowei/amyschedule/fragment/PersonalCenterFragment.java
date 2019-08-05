package com.wbu.xiaowei.amyschedule.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wbu.xiaowei.amyschedule.R;
import com.wbu.xiaowei.amyschedule.activity.AboutActivity;
import com.wbu.xiaowei.amyschedule.activity.LoginActivity;
import com.wbu.xiaowei.amyschedule.activity.MyScoreActivity;
import com.wbu.xiaowei.amyschedule.activity.SettingsActivity;
import com.wbu.xiaowei.amyschedule.activity.PersonalInformationActivity;
import com.wbu.xiaowei.amyschedule.adapter.PersonalCenterListAdapter;
import com.wbu.xiaowei.amyschedule.util.ScheduleUtils;
import com.wbu.xiaowei.amyschedule.other.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class PersonalCenterFragment extends Fragment {

    private RecyclerView moreRecyclerView;
    private LinearLayout loginBtn;
    private ImageView icChevronRight;
    private TextView studentNameTv;
    private TextView usernameTv;
    private TextView infoViewTipText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);

        moreRecyclerView = view.findViewById(R.id.more_recycler_view);
        loginBtn = view.findViewById(R.id.login_logout_btn);
        icChevronRight = view.findViewById(R.id.ic_chevron_right);
        studentNameTv = view.findViewById(R.id.student_name_tv);
        usernameTv = view.findViewById(R.id.username_tv);
        infoViewTipText = view.findViewById(R.id.info_view_tip_text);

        initView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ScheduleUtils.userInfo != null) {
            studentNameTv.setText(ScheduleUtils.userInfo.studentName);
            usernameTv.setText(ScheduleUtils.userInfo.username);
            infoViewTipText.setText("查看更多");
        } else {
            studentNameTv.setText("未连接教务系统");
            usernameTv.setText("*********");
            infoViewTipText.setText("点击连接");
        }
    }

    private void initView() {
        icChevronRight.setColorFilter(getResources().getColor(R.color.secondary_text));
        loginBtn.setOnClickListener(onClickListener);

        List<MyItem> myItems = new ArrayList<>();
        myItems.add(new MyItem(R.drawable.ic_score, "我的成绩单"));
        myItems.add(new MyItem(R.drawable.ic_settings, "设置"));
        myItems.add(new MyItem(R.drawable.ic_help, "帮助与反馈"));
        myItems.add(new MyItem(R.drawable.ic_about, "关于"));

        // 创建数据适配器
        PersonalCenterListAdapter personalCenterListAdapter = new PersonalCenterListAdapter(myItems);

        // 添 item 加点击事件
        personalCenterListAdapter.setOnItemClickListener(onItemClickListener);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // 设置布局管理器
        moreRecyclerView.setLayoutManager(layoutManager);

        // 设置为垂直布局
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        // 设置 Adapter
        moreRecyclerView.setAdapter(personalCenterListAdapter);

        // 设置分割线
        moreRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        // 设置增加或删除条目的动画
        moreRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    // 按钮点击事件处理
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.login_logout_btn:
                    if (studentNameTv.getText().toString().equals("未连接教务系统")) {
                        intent = new Intent(getActivity(), LoginActivity.class);
                    } else {
                        intent = new Intent(getActivity(), PersonalInformationActivity.class);
                    }
                    break;
                default:
                    return;
            }
            startActivity(intent);
        }
    };

    // 列表项点击事件处理
    PersonalCenterListAdapter.OnItemClickListener onItemClickListener = new PersonalCenterListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            Intent intent = null;
            switch (position) {
                case 0: // 我的成绩单
                    intent = new Intent(getActivity(), MyScoreActivity.class);
                    break;
                case 1: // 设置
                    intent = new Intent(getActivity(), SettingsActivity.class);
                    break;
                case 2: // 帮助与反馈
                    intent = new Intent(getActivity(), SettingsActivity.class);
                    break;
                case 3: // 关于
                    intent = new Intent(getActivity(), AboutActivity.class);
                    break;
                default:
                    return;
            }
            startActivity(intent);
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
            }
            return false;
        }
    });

    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static class MyItem {
        public int iconId;
        public String text;

        MyItem(int id, String t) {
            this.iconId = id;
            this.text = t;
        }

        public MyItem() {
        }
    }

}

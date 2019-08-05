package com.wbu.xiaowei.amyschedule.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wbu.xiaowei.amyschedule.R;

import java.util.Objects;

public class LoadingAnimationDialog extends Dialog {
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setDimAmount(0.3F);
        setContentView(R.layout.dialog_loading_anim);

        TextView titleTv = findViewById(R.id.title_text_in_progress_dialog);

        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        } else {
            titleTv.setVisibility(View.GONE);
        }

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public LoadingAnimationDialog(Context context) {
        super(context);
    }

    /**
     * 加载动画对话框
     *
     * @param context Context
     * @param title   加载动画的标题
     */
    public LoadingAnimationDialog(Context context, String title) {
        super(context);
        this.title = title;
    }
}

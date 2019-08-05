package com.wbu.xiaowei.amyschedule.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wbu.xiaowei.amyschedule.R;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.util.List;

/**
 * 滚动单选对话框
 */
public class WheelViewDialog extends Dialog {
    private LoopView loopView;
    private TextView titleTv;
    private Button positiveButton;
    private Button negativeButton;

    private int position = -1; // 记录当前选择的位置
    private OnSelectedListener listener;
    private List<String> items;
    private Integer initPosition; // 默认选择位置
    private boolean isLoop = true;
    private Integer centerTextColor;
    private Integer outerTextColor;
    private Integer dividerColor;
    private Integer itemsVisibleCount;
    private Float textSize;
    private Float lineSpacingMultiplier;
    private Float scaleX;
    private String titleText = "请选择";
    private String positiveButtonText = "确定";
    private String negativeButtonText = "取消";
    private boolean canceledOnTouchOutside; // 设置显示 dialog 后，触屏屏幕是否会使 dialog 消失
    private boolean noNegativeButton; // 不显示取消按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wheel_view);
        init();
    }

    private void init() {
        loopView = findViewById(R.id.loop_view_in_wheel_view_dialog);
        titleTv = findViewById(R.id.title_text_in_wheel_view_dialog);
        positiveButton = findViewById(R.id.positive_btn);
        negativeButton = findViewById(R.id.negative_btn);
        titleTv.setText(titleText);
        positiveButton.setText(positiveButtonText);
        negativeButton.setText(negativeButtonText);
        setCanceledOnTouchOutside(canceledOnTouchOutside);

        // 设置选项数据
        loopView.setItems(items);

        // 滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                position = index; // 记录选择的位置
            }
        });

        position = items != null ? items.size() / 2 : 0;
        if (initPosition != null) {
            loopView.setInitPosition(initPosition);
            position = initPosition;
        }
        if (!isLoop) {
            loopView.setNotLoop();
        }
        if (centerTextColor != null) {
            loopView.setCenterTextColor(centerTextColor);
        }
        if (outerTextColor != null) {
            loopView.setOuterTextColor(outerTextColor);
        }
        if (dividerColor != null) {
            loopView.setDividerColor(dividerColor);
        }
        if (itemsVisibleCount != null) {
            loopView.setItemsVisibleCount(itemsVisibleCount);
        }
        if (textSize != null) {
            loopView.setTextSize(textSize);
        }
        if (lineSpacingMultiplier != null) {
            loopView.setLineSpacingMultiplier(lineSpacingMultiplier);
        }
        if (scaleX != null) {
            loopView.setScaleX(scaleX);
        }
        if (noNegativeButton) {
            deleteNegativeButton();
        }

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSelected(position);
                }
                dismiss();
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Log.d("WheelViewDialog", "初始 position: " + position);
    }

    // 删除取消按钮
    private void deleteNegativeButton() {
        negativeButton.setVisibility(View.GONE);
    }

    public WheelViewDialog(Context context, List<String> items) {
        super(context, R.style.alert_dialog);
        this.items = items;
    }

    public WheelViewDialog(Context context) {
        super(context);
    }

    public interface OnSelectedListener {
        void onSelected(int position);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public void setInitPosition(int position) {
        this.initPosition = position;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public void setCenterTextColor(Integer centerTextColor) {
        this.centerTextColor = centerTextColor;
    }

    public void setOuterTextColor(Integer outerTextColor) {
        this.outerTextColor = outerTextColor;
    }

    public void setDividerColor(Integer dividerColor) {
        this.dividerColor = dividerColor;
    }

    public void setItemsVisibleCount(Integer itemsVisibleCount) {
        this.itemsVisibleCount = itemsVisibleCount;
    }

    public void setTextSize(Float textSize) {
        this.textSize = textSize;
    }

    public void setLineSpacingMultiplier(Float lineSpacingMultiplier) {
        this.lineSpacingMultiplier = lineSpacingMultiplier;
    }

    public void setScaleX(Float scaleX) {
        this.scaleX = scaleX;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public void setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }

    public void setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
    }

    public void setNoNegativeButton(boolean noNegativeButton) {
        this.noNegativeButton = noNegativeButton;
    }
}

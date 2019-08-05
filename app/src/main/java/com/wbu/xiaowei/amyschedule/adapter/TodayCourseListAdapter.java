package com.wbu.xiaowei.amyschedule.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbu.xiaowei.amyschedule.R;
import com.zhuangfei.timetable.model.Schedule;
import com.zhuangfei.timetable.model.ScheduleColorPool;
import com.zhuangfei.timetable.model.ScheduleSupport;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TodayCourseListAdapter extends RecyclerView.Adapter<TodayCourseListAdapter.VH> {
    private List<Schedule> schedules;
    private Context context;
    private LayoutInflater inflater;

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_today_course, parent, false);
        return new TodayCourseListAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Schedule schedule = schedules.get(position);
        ScheduleColorPool colorPool = new ScheduleColorPool(context);
        holder.courseLogoTv.setText(schedule.getName().substring(0, 1));
        holder.courseLogoTv.setBackgroundColor(colorPool.getColorAuto(schedule.getColorRandom()));
        holder.courseNameTv.setText(schedule.getName());
        holder.placeTv.setText(schedule.getRoom());
        holder.timeTv.setText(schedule.getStart() + " ~ " + (schedule.getStart() + schedule.getStep() - 1) + " 节");
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    /**
     * TodayCourseListAdapter 构造器
     *
     * @param context   Context
     * @param schedules 课程列表
     */
    public TodayCourseListAdapter(Context context, List<Schedule> schedules) {
        this.context = context;
        this.schedules = schedules;
    }

    /**
     * ViewHolder
     */
    public static class VH extends RecyclerView.ViewHolder {

        private TextView courseLogoTv;
        private TextView courseNameTv;
        private TextView placeTv;
        private TextView timeTv;

        public VH(View v) {
            super(v);
            courseLogoTv = v.findViewById(R.id.course_logo_text_view);
            courseNameTv = v.findViewById(R.id.course_name_text_view);
            placeTv = v.findViewById(R.id.course_place_text_view);
            timeTv = v.findViewById(R.id.course_time_text_view);
        }
    }
}

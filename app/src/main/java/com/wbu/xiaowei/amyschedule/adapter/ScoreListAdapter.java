package com.wbu.xiaowei.amyschedule.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wbu.xiaowei.amyschedule.R;
import com.wbu.xiaowei.amyschedule.bean.ScoreBase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.VH> {

    private List<ScoreBase> scoreList;

    public ScoreListAdapter(List<ScoreBase> scoreList) {
        this.scoreList = scoreList;
    }

    @NotNull
    @Override
    public VH onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NotNull VH holder, int position) {
        holder.scoreIndexTv.setText(position + 1 + "");
        holder.scoreNameTv.setText(scoreList.get(position).getName());
        holder.fractionTv.setText(scoreList.get(position).getFraction());
        holder.fractionTv.setTextColor(holder.prim);

        String fractionStr = scoreList.get(position).getFraction();
        float fraction = 75;
        try {
            fraction = Float.parseFloat(fractionStr);
        } catch (Exception ignored) {
        }
        if ("合格".equals(fractionStr) || "优".equals(fractionStr)) {
            holder.fractionTv.setTextColor(holder.pass);
        } else if (fraction >= 80) {
            holder.fractionTv.setTextColor(holder.pass);
        } else if (fraction >= 60 && fraction < 70) {
            holder.fractionTv.setTextColor(holder.warning);
        } else if (fraction < 60) {
            holder.fractionTv.setTextColor(holder.noPass);
        }
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    /**
     * ViewHolder
     */
    public static class VH extends RecyclerView.ViewHolder {

        private TextView scoreIndexTv;
        private TextView scoreNameTv;
        private TextView fractionTv;

        private int prim;
        private int pass;
        private int warning;
        private int noPass;

        public VH(View v) {
            super(v);
            scoreIndexTv = (TextView) v.findViewById(R.id.score_index_tv);
            scoreNameTv = (TextView) v.findViewById(R.id.score_name_tv);
            fractionTv = (TextView) v.findViewById(R.id.fraction_tv);

            prim = v.getResources().getColor(R.color.primary_text);
            pass = v.getResources().getColor(R.color.score_pass);
            warning = v.getResources().getColor(R.color.score_warning);
            noPass = v.getResources().getColor(R.color.score_no_pass);
        }
    }
}

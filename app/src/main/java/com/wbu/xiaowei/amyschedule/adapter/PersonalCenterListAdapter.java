package com.wbu.xiaowei.amyschedule.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wbu.xiaowei.amyschedule.R;
import com.wbu.xiaowei.amyschedule.fragment.PersonalCenterFragment;

import java.util.List;

public class PersonalCenterListAdapter extends RecyclerView.Adapter<PersonalCenterListAdapter.VH> {
    private OnItemClickListener onItemClickListener;
    List<PersonalCenterFragment.MyItem> myItems;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_personal_center, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        holder.setText(myItems.get(position).text);
        holder.setIconImage(myItems.get(position).iconId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myItems.size();
    }

    public PersonalCenterListAdapter(List<PersonalCenterFragment.MyItem> myItems) {
        this.myItems = myItems;
    }

    public static class VH extends RecyclerView.ViewHolder {

        private ImageView iconImage;
        private TextView text;

        public VH(View v) {
            super(v);
            iconImage = (ImageView) v.findViewById(R.id.more_page_list_icon);
            text = (TextView) v.findViewById(R.id.more_page_list_text);
            iconImage.setColorFilter(v.getResources().getColor(R.color.primary));
        }

        private void setIconImage(int id) {
            iconImage.setImageResource(id);
        }

        private void setText(String t) {
            text.setText(t);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}

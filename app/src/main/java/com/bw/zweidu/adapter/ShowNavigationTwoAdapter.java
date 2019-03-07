package com.bw.zweidu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bw.zweidu.R;
import com.bw.zweidu.bean.ShowNavigationMsgBean;
import com.bw.zweidu.bean.ShowNavigationTwoBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ShowNavigationTwoAdapter extends RecyclerView.Adapter<ShowNavigationTwoAdapter.ViewHolder> {
    private List<ShowNavigationTwoBean.ResultBean> list;
    private Context context;

    public ShowNavigationTwoAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<ShowNavigationTwoBean.ResultBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recy_navigation_two, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.getdata(list.get(i),context,i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_two_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_two_name= itemView.findViewById(R.id.item_two_name);
        }

        public void getdata(final ShowNavigationTwoBean.ResultBean resultBean, Context context, int i) {
            item_two_name.setText(resultBean.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ShowNavigationMsgBean(resultBean.getId(),"two"));
                }
            });

        }
    }
}

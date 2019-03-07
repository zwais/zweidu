package com.bw.zweidu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bw.zweidu.R;
import com.bw.zweidu.bean.MoneyBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.ViewHolder> {
    private List<MoneyBean.resultBean.DetailListBean> list;
    private Context context;

    public MoneyAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<MoneyBean.resultBean.DetailListBean> mlist) {
        list.clear();
        if (mlist!=null){
            list.addAll(mlist);
        }
        notifyDataSetChanged();
    }

    public void addList(List<MoneyBean.resultBean.DetailListBean> mlist) {
        if (mlist!=null){
            list.addAll(mlist);
        }
        notifyDataSetChanged();
    }
    public MoneyBean.resultBean.DetailListBean getItem(int position){
        return list.get(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.xrecy_money_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.getdata(getItem(i),context,i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text_price;
        private TextView text_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_price=itemView.findViewById(R.id.item_money_price);
            text_time=itemView.findViewById(R.id.item_money_time);

        }

        public void getdata(MoneyBean.resultBean.DetailListBean item, Context context, int i) {
            text_price.setText(item.getAmount()+"");
            String times = new SimpleDateFormat("yyyy-MM-dd").format(
                    new java.util.Date(item.getCreateTime()));
            text_time.setText(times);
        }
    }
}

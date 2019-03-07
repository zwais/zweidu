package com.bw.zweidu.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bw.zweidu.R;
import com.bw.zweidu.bean.FootBean;
import com.facebook.drawee.view.SimpleDraweeView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FootAdapter extends RecyclerView.Adapter<FootAdapter.ViewHolder> {
    private List<FootBean.ResultBean> list;
    private Context context;

    public FootAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();

    }

    public void setList(List<FootBean.ResultBean> mlist) {
        list.clear();
        if (mlist!=null){
            list.addAll(mlist);
        }
        notifyDataSetChanged();
    }
    public FootBean.ResultBean getItem(int position){
        return list.get(position);
    }
    public void addList(List<FootBean.ResultBean> mlist) {
        if (mlist!=null){
            list.addAll(mlist);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.xrecy_item_foot, viewGroup, false);
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
        private SimpleDraweeView imageView;
        private TextView text_name;
        private TextView text_price;
        private TextView text_liulan;
        private TextView text_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_foot_image);
            text_name=itemView.findViewById(R.id.item_foot_name);
            text_price=itemView.findViewById(R.id.item_foot_price);
            text_liulan=itemView.findViewById(R.id.item_foot_liulan);
            text_time=itemView.findViewById(R.id.item_foot_time);
        }

        public void getdata(FootBean.ResultBean item, Context context, int i) {
            imageView.setImageURI(Uri.parse(item.getMasterPic()));
            text_name.setText(item.getCommodityName());
            text_price.setText("￥"+item.getPrice());
            text_liulan.setText("已浏览"+item.getBrowseNum()+"次");
            String times = new SimpleDateFormat("yyyy-MM-dd").format(
                    new java.util.Date(item.getBrowseTime()));
            text_time.setText(times);
        }
    }
}

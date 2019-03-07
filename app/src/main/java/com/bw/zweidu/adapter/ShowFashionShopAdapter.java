package com.bw.zweidu.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.zweidu.R;
import com.bw.zweidu.base.ShowShopBean;
import com.bw.zweidu.bean.ComMsgBean;
import com.facebook.drawee.view.SimpleDraweeView;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ShowFashionShopAdapter extends RecyclerView.Adapter<ShowFashionShopAdapter.ViewHolder>{
    private List<ShowShopBean.ResultBean.MlssBean.CommodityListBeanXX> list;
    private Context context;

    public ShowFashionShopAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<ShowShopBean.ResultBean.MlssBean.CommodityListBeanXX> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public ShowShopBean.ResultBean.MlssBean.CommodityListBeanXX getItem(int position){
        return list.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recy_fashion_shop, viewGroup, false);
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
        private SimpleDraweeView new_image;
        private TextView new_name;
        private TextView new_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            new_image=itemView.findViewById(R.id.item_fashion_image);
            new_name=itemView.findViewById(R.id.item_fashion_name);
            new_price=itemView.findViewById(R.id.item_fashion_price);
        }

        public void getdata(final ShowShopBean.ResultBean.MlssBean.CommodityListBeanXX item, Context context, int i) {
            new_image.setImageURI(Uri.parse(item.getMasterPic()));
            new_name.setText(item.getCommodityName());
            new_price.setText("￥"+item.getPrice());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EventBus.getDefault().post(new ComMsgBean(item.getCommodityId(),"par"));
                }
            });
        }
    }
}

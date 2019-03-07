package com.bw.zweidu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bw.zweidu.R;
import com.bw.zweidu.bean.SelectShopBean;
import com.bw.zweidu.customview.CustomRelalayout;
import java.util.ArrayList;
import java.util.List;

public class SelectShopAdapter extends RecyclerView.Adapter<SelectShopAdapter.ViewHolder> {
    private List<SelectShopBean.ResultBean> list;
    private Context context;

    public SelectShopAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<SelectShopBean.ResultBean> mlist) {
        if (mlist!=null){
            list.addAll(mlist);
        }
        notifyDataSetChanged();
    }
    public SelectShopBean.ResultBean getItem(int position){
        return list.get(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recy_select_item, viewGroup, false);
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
        private CheckBox box_all;
        private ImageView item_image;
        private TextView text_name;
        private TextView text_price;
        private TextView text_delete;
        private CustomRelalayout text_num;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            box_all=itemView.findViewById(R.id.item_select_box_all);
            item_image=itemView.findViewById(R.id.item_select_image);
            text_name=itemView.findViewById(R.id.item_select_text_name);
            text_price=itemView.findViewById(R.id.item_select_text_price);
            text_delete=itemView.findViewById(R.id.item_select_text_delete);
            text_num=itemView.findViewById(R.id.item_custom_num);
        }

        public void getdata(final SelectShopBean.ResultBean item, Context context, final int i) {
            Glide.with(context).load(item.getPic()).into(item_image);
            text_name.setText(item.getCommodityName());
            text_price.setText("￥"+item.getPrice());
            box_all.setChecked(item.isIscheck());
            text_num.setnum(item);
            text_num.setOnCallBack(new CustomRelalayout.CallBackListener() {
                @Override
                public void callBack() {
                    if (mshopClick!=null){
                        mshopClick.shopPrice(list);
                    }
                }
            });
            //改变复选框的状态
            box_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setIscheck(isChecked);
                    if (mshopClick!=null){
                        mshopClick.shopPrice(list);
                    }
                }
            });
            text_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(i);
                    notifyDataSetChanged();
                    if (mRemoveCallBack!=null){
                        mRemoveCallBack.removeposition(list,i);
                    }
                }
            });
        }
    }

    public ShopClick mshopClick;
    public void setOnClick(ShopClick mshopClick){
        this.mshopClick=mshopClick;
    }
    public interface ShopClick{
        void shopPrice(List<SelectShopBean.ResultBean> list);
    }
    public RemoveCallBack mRemoveCallBack;
    public void setRemove(RemoveCallBack mRemoveCallBack){
        this.mRemoveCallBack=mRemoveCallBack;
    }
    public interface RemoveCallBack{
        void removeposition(List<SelectShopBean.ResultBean> list,int position);
    }
}

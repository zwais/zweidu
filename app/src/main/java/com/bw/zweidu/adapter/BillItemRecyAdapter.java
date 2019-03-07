package com.bw.zweidu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bw.zweidu.R;
import com.bw.zweidu.bean.BillShopBean;

import java.util.ArrayList;
import java.util.List;

public class BillItemRecyAdapter extends RecyclerView.Adapter<BillItemRecyAdapter.ViewHodler> {
    private List<BillShopBean.OrderListBean.DetailListBean> list;
    private Context context;
    private int status;
    private final int STATUS_PAY=1;
    private final int STATUS_TASK=2;
    private final int STATUS_APPRAISE=3;
    private final int STATUS_FINISH=9;
    private final int STATUS_TEN=10;
    public BillItemRecyAdapter(Context context, int status) {
        this.context = context;
        this.status = status;
        list=new ArrayList<>();
    }

    public void setList(List<BillShopBean.OrderListBean.DetailListBean> mlist) {
        if (mlist!=null){
            list.addAll(mlist);
        }
        notifyDataSetChanged();
    }
    public BillShopBean.OrderListBean.DetailListBean getItem(int position){
        return list.get(position);
    }
    @Override
    public int getItemViewType(int position) {
        if (status==STATUS_PAY){
            return STATUS_PAY;
        }else if (status==STATUS_TASK){
            return STATUS_TASK;
        }else if (status==STATUS_APPRAISE){
            return STATUS_APPRAISE;
        }else if (status==STATUS_FINISH){
            return STATUS_FINISH;
        }else{
            return STATUS_TEN;
        }
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==STATUS_PAY){
            View payView = LayoutInflater.from(context).inflate(R.layout.recy_item_pay_item, viewGroup, false);
            return new ViewHodler(payView);
        }else if (i==STATUS_TASK){
            View taskView = LayoutInflater.from(context).inflate(R.layout.recy_item_task_item, viewGroup, false);
            return new ViewHodler(taskView);
        }else if (i==STATUS_APPRAISE){
            View appraiseView = LayoutInflater.from(context).inflate(R.layout.recy_item_appraise_item, viewGroup, false);
            return new ViewHodler(appraiseView);
        }else if (i==STATUS_FINISH){
            View finishView = LayoutInflater.from(context).inflate(R.layout.recy_item_finish_item, viewGroup, false);
            return new ViewHodler(finishView);
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler viewHodler, int i) {
        viewHodler.getdata(getItem(i),context,i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHodler extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView text_name;
        private TextView text_price;
        private TextView text_num;
        private Button button_evaluate;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.recy_item_bill_image);
            text_name=itemView.findViewById(R.id.recy_item_bill_text_name);
            text_price=itemView.findViewById(R.id.recy_item_bill_text_price);
            text_num=itemView.findViewById(R.id.recy_item_bill_text_num);
            button_evaluate=itemView.findViewById(R.id.recy_item_bill_button_evaluate);
        }

        public void getdata(BillShopBean.OrderListBean.DetailListBean item, Context context, int i) {

            String[] split = item.getCommodityPic().split("\\,");
            Glide.with(context).load(split[0]).into(imageView);
            text_name.setText(item.getCommodityName());
            text_price.setText("￥"+item.getCommodityPrice());
            if (text_num!=null){
                text_num.setText("* "+item.getCommodityCount()+" *");
            }
            if (button_evaluate!=null){
                button_evaluate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mClickEvaluate!=null){
                            mClickEvaluate.setEvaluat(list,getAdapterPosition());
                        }
                    }
                });
            }
        }
    }
    public ClickEvaluate mClickEvaluate;
    public void setEva(ClickEvaluate mClickEvaluate){
        this.mClickEvaluate=mClickEvaluate;
    }
    public interface ClickEvaluate{
        void setEvaluat( List<BillShopBean.OrderListBean.DetailListBean> list, int position );
    }
}

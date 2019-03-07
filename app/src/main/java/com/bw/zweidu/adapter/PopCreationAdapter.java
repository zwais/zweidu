package com.bw.zweidu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bw.zweidu.R;
import com.bw.zweidu.bean.AddressListBean;
import com.bw.zweidu.bean.SelectAddressBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PopCreationAdapter extends RecyclerView.Adapter<PopCreationAdapter.ViewHolder> {
    private List<SelectAddressBean.ResultBean> list;
    private Context context;

    public PopCreationAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<SelectAddressBean.ResultBean> mlist) {
        if (mlist != null) {
            list.addAll(mlist);
        }
        notifyDataSetChanged();;
    }
    public SelectAddressBean.ResultBean getItem(int position){
        return list.get(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pop_recy_creation, viewGroup, false);
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
        private TextView item_name;
        private TextView item_phone;
        private TextView item_address;
        private TextView item_check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name=itemView.findViewById(R.id.item_pop_creation_text_name);
            item_phone=itemView.findViewById(R.id.item_pop_creation_text_phone);
            item_address=itemView.findViewById(R.id.item_pop_creation_text_address);
            item_check=itemView.findViewById(R.id.item_pop_creation_text_check);

        }

        public void getdata(final SelectAddressBean.ResultBean item, Context context, int i) {
            item_name.setText(item.getRealName());
            item_phone.setText(item.getPhone());
            AddressListBean addressListBean = new Gson().fromJson(item.getAddress(), AddressListBean.class);
            item_address.setText(addressListBean.getCity()+addressListBean.getAddress());
            item_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickChange!=null){
                        mClickChange.onClick(item.getId());
                    }
                }
            });
        }
    }
    private ClickChange mClickChange;
    public void setChange(ClickChange mClickChange){
        this.mClickChange=mClickChange;
    }
    public interface ClickChange{
        void onClick( int id );
    }
}

package com.bw.zweidu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bw.zweidu.R;
import com.bw.zweidu.bean.AddressListBean;
import com.bw.zweidu.bean.SelectAddressBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectAdapter extends RecyclerView.Adapter<AddressSelectAdapter.ViewHolder> {
    private List<SelectAddressBean.ResultBean> list;
    private Context context;

    public AddressSelectAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }

    public void setList(List<SelectAddressBean.ResultBean> mlist) {
        if (mlist!=null){
            list.addAll(mlist);
        }
        notifyDataSetChanged();

    }
    public SelectAddressBean.ResultBean getItem(int position){
        return list.get(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recy_address_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.getdata(getItem(i),context,i);
    }
    //改变默认值
    public  void setAllunCheck(int position) {
        int size = list.size();
        for (int i=0;i<size;i++){
            if(i==position){
                list.get(i).setWhetherDefault(1);
            }else{
                list.get(i).setWhetherDefault(2);
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setChang(int whetherDefault,int i,List<SelectAddressBean.ResultBean> List) {
        for (int y = 0; y < list.size(); y++) {
            if (i == y) {
                list.get(i).setWhetherDefault(1);
            } else {
                list.get(i).setWhetherDefault(2);
            }
        }
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_name;
        private TextView item_phone;
        private TextView item_content;
        private TextView item_update;
        private TextView item_delete;
        private RadioButton item_radio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.recy_item_address_text_name);
            item_phone = itemView.findViewById(R.id.recy_item_address_text_phone);
            item_content = itemView.findViewById(R.id.recy_item_address_text_content);
            item_update = itemView.findViewById(R.id.recy_item_address_text_update);
            item_delete = itemView.findViewById(R.id.recy_item_address_text_delete);
            item_radio = itemView.findViewById(R.id.recy_item_address_radio);
        }

        public void getdata(final SelectAddressBean.ResultBean item, Context context, final int i) {
            item_name.setText(item.getRealName());
            item_phone.setText(item.getPhone());
            AddressListBean addressListBean = new Gson().fromJson(item.getAddress(), AddressListBean.class);
            item_content.setText(addressListBean.getCity() + addressListBean.getAddress());
            //设置默认地址
            if (item.getWhetherDefault() == 1) {
                item_radio.setChecked(true);
            } else {
                item_radio.setChecked(false);
            }
            item_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickUpdate != null) {
                        mClickUpdate.update(list, i);
                    }
                }
            });
            item_radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //设置默认值
                    int posistion = i;
                    if(mClickChange!=null){
                        mClickChange.Change(item.getId(),i);
                    }
                }
            });
        }
    }
    private ClickUpdate mClickUpdate;
    public void setUpdate(ClickUpdate mClickUpdate){
         this.mClickUpdate=mClickUpdate;
    }
    public interface ClickUpdate{
        void update( List<SelectAddressBean.ResultBean> list, int position );
    }
    private ClickChange mClickChange;
    public void setChange(ClickChange mClickChange){
        this.mClickChange=mClickChange;
    }
    public interface ClickChange{
        void Change( int id, int i );
    }
}

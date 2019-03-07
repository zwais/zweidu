package com.bw.zweidu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bw.zweidu.R;
import com.bw.zweidu.bean.CircleListBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CircleListAdapter extends RecyclerView.Adapter<CircleListAdapter.ViewHolder> {
    private List<CircleListBean.ResultBean> list;
    private Context context;

    public CircleListAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
    }
    public CircleListBean.ResultBean getItem(int position){
        return list.get(position);
    }
    public void setList(List<CircleListBean.ResultBean> mlist) {
        list.clear();
        if (mlist!=null||mlist.size()!=0){
            list.addAll(mlist);
            notifyDataSetChanged();
        }

    }
    public void addList(List<CircleListBean.ResultBean> mlist) {
        if (mlist != null || mlist.size() != 0) {
            list.addAll(mlist);
            notifyDataSetChanged();
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.xrecy_circle, viewGroup, false);
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
    //点赞改变
    public void getlike(int position){
        list.get(position).setWhetherGreat(1);
        list.get(position).setGreatNum(list.get(position).getGreatNum()+1);
        notifyDataSetChanged();
    }
    //取消点赞改变
    public void getcancel(int position){
        list.get(position).setWhetherGreat(2);
        list.get(position).setGreatNum(list.get(position).getGreatNum()-1);
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView circle_image_header;
        private ImageView circle_image_content;
        private ImageView circle_image_like;
        private TextView circle_text_name;
        private TextView circle_text_time;
        private TextView circle_text_content;
        private TextView circle_text_num;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circle_image_header=itemView.findViewById(R.id.item_circle_image_header);
            circle_image_content=itemView.findViewById(R.id.item_circle_image_content);
            circle_image_like=itemView.findViewById(R.id.item_circle_image_like);
            circle_text_name=itemView.findViewById(R.id.item_circle_text_name);
            circle_text_time=itemView.findViewById(R.id.item_circle_text_time);
            circle_text_content=itemView.findViewById(R.id.item_circle_text_content);
            circle_text_num=itemView.findViewById(R.id.item_circle_text_num);

        }
        public void getdata(final CircleListBean.ResultBean item, Context context, final int i) {
            Glide.with(context).load(item.getHeadPic()).into(circle_image_header);
            if (item.getImage()!=null){
                String[] split = item.getImage().split("\\,");
                Glide.with(context).load(split[0]).into(circle_image_content);
            }
            if (item.getWhetherGreat()==1){

                Glide.with(context).load(R.mipmap.common_btn_prise_s).into(circle_image_like);
            }else{

                Glide.with(context).load(R.mipmap.common_btn_prise_n).into(circle_image_like);
            }
            String date = new SimpleDateFormat("yyyy-MM-dd").format(
                    new java.util.Date(item.getCreateTime()));
            circle_text_time.setText(date);
            circle_text_name.setText(item.getNickName());
            circle_text_content.setText(item.getContent());
            circle_text_num.setText(item.getGreatNum()+"");
            circle_image_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClick!=null){
                        mOnClick.getdata(item.getId(),item.getWhetherGreat(),i);
                    }
                }
            });
    }
    }
    private OnClick mOnClick;
    public void setOnClick(OnClick mOnClick){
        this.mOnClick=mOnClick;
    }


    public interface OnClick{
        void getdata( int id, int great, int position );
    }

}

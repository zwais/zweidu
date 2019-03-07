package com.bw.zweidu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bw.zweidu.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<Object> list;
    private Context context;

    public ImageAdapter(Context context) {
        this.context = context;
        list=new ArrayList<>();
        list.add(list.size(),R.mipmap.common_btn_camera_blue);
    }

    public void setList(List<Object> mlist) {
        if (mlist!=null){
            list.addAll(mlist);
        }
        if (list.size()>10){
            return ;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, viewGroup, false);
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
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.en_image);
        }

        public void getdata( Object object, Context context, int i) {
                if (i>0){
                    imageView.setImageBitmap((Bitmap)object);
                }
                if (i==0){
                    imageView.setBackgroundResource((Integer)object);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mImageClick!=null){
                                mImageClick.getdata();
                            }
                        }
                    });
                }
        }
    }
    private ImageClick mImageClick;
    public void getImage(ImageClick mImageClick){
        this.mImageClick=mImageClick;
    }
    public interface ImageClick{
        void getdata();
    }
}

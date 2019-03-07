package com.bw.zweidu.activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.zweidu.R;
import com.bw.zweidu.adapter.ImageAdapter;
import com.bw.zweidu.base.BaseActivity;
import com.bw.zweidu.bean.CommentBean;
import com.bw.zweidu.bean.NextCircleBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.facebook.drawee.view.SimpleDraweeView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 评价的模板

public class EvaluatActivity extends BaseActivity implements View.OnClickListener,IView {


    private SimpleDraweeView eva_image;
    private TextView text_name;
    private TextView text_price;
    private Button button_next;
    private EditText text_content;
    private String id;
    private Dialog first_dialog;
    private IpresenterImpl mIpresenterImpl;

    private final int PHOTO_FLAG=1;
    private final int CAMERA_FLAG=2;
    private final int CAIJIAN_FLAG=3;

    private String path=Environment.getExternalStorageDirectory()+"/header_image.png";
    private File first_file;
    private RecyclerView image_recy;

    private ImageAdapter imageAdapter;
    private List<File> file_list=new ArrayList<>();
    private CheckBox tongbu;
    private String orderId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_evaluat;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //互绑
        initIpresenter();
        //获取资源ID
        eva_image = findViewById(R.id.eva_image);
        text_name = findViewById(R.id.eva_text_name);
        text_price = findViewById(R.id.eva_text_price);
        button_next = findViewById(R.id.eva_button_next);
        text_content = findViewById(R.id.eva_text_content);
        tongbu = findViewById(R.id.en_tongbu);
        image_recy = findViewById(R.id.image_recy);
    }

    @Override
    protected void initData() {
        //获取值
        initIntent();
        initRecy();
        //点击发送
        clickSend();
    }

    private void initRecy() {
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        gridLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        image_recy.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(this);
        image_recy.setAdapter(imageAdapter);
        imageAdapter.getImage(new ImageAdapter.ImageClick() {
            @Override
            public void getdata() {
                firstShow();
            }
        });
    }

    private void clickSend() {
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String text = text_content.getText().toString();
                Map<String,String> params_com=new HashMap<>();
                params_com.put("commodityId",id);
                params_com.put("orderId",orderId);
                params_com.put("content",text);
                mIpresenterImpl.postDuoConRequestIpresenter(Apis.SHOW_NEXT_COMMENT_URL,params_com,file_list,CommentBean.class);
                if (tongbu.isChecked()){
                    Map<String,String> params=new HashMap<>();
                    params.put("commodityId",id);
                    params.put("content",text);
                    mIpresenterImpl.postDuoConRequestIpresenter(Apis.SHOW_NEXT_CIRCLE_URL,params,file_list,NextCircleBean.class);
                }

            }
        });
    }

    private void initIntent() {
        Intent intent=getIntent();
        String image = intent.getStringExtra("image");
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        orderId = intent.getStringExtra("orderId");
        id = intent.getStringExtra("id");
        eva_image.setImageURI(Uri.parse(image));
        text_name.setText(name);
        text_price.setText(price);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //打开相机
            case R.id.first_takePhoto:
                getPhoto();
                first_dialog.dismiss();
                break;
            //打开相册
            case R.id.first_choosePhoto:
                getCamera();
                first_dialog.dismiss();
                break;
            //取消
            case R.id.first_btn_cancel:
                first_dialog.dismiss();
                break;
        }
    }

    @Override
    public void success(Object object) {
        if (object instanceof NextCircleBean){
            NextCircleBean nextCircleBean= (NextCircleBean) object;
            if (nextCircleBean.getStatus().equals("0000")){
                Toast.makeText(this, nextCircleBean.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, nextCircleBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (object instanceof CommentBean){
            CommentBean commentBean= (CommentBean) object;
            if (commentBean.getStatus().equals("0000")){
                Toast.makeText(this, commentBean.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, commentBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void failure(String error) {

    }

   //打开相机
    private void getPhoto(){
        Intent intent_takePhoto=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasSdcard()){//判断SD卡是否可用
            //存放到内存中
            intent_takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        }
        startActivityForResult(intent_takePhoto, PHOTO_FLAG);
    }

    //打开相册
    private void getCamera(){
        Intent intent_choosePhoto=new Intent(Intent.ACTION_PICK);
        intent_choosePhoto.setType("image/*");
        startActivityForResult(intent_choosePhoto,CAMERA_FLAG);
    }

   //裁剪图片
    private void crop(Uri uri){
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        //支持裁剪
        intent.putExtra("CROP",true);
        //裁剪的比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX",250);
        intent.putExtra("outputY",250);
        //将图片返回给data
        intent.putExtra("return-data",true);
        startActivityForResult(intent,CAIJIAN_FLAG);
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==PHOTO_FLAG&&resultCode==RESULT_OK){//相机返回的数据
            if (hasSdcard()){
                crop(Uri.fromFile(new File(path)));
            }else{
                Toast.makeText(EvaluatActivity.this, "未找到存储啦，无法存储照片", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode==CAMERA_FLAG&&resultCode==RESULT_OK){//相册返回的数据
            //得到图片的全路径
            if (data!=null){
                Uri uri = data.getData();
                crop(uri);
            }
        }else if(requestCode==CAIJIAN_FLAG&&resultCode==RESULT_OK){//剪切回来的照片数据
            if (data!=null){
                Bitmap bitmap = data.getParcelableExtra("data");
                 List<Object> list=new ArrayList<>();
                list.add(bitmap);
                imageAdapter.setList(list);
                String img_path = bitmapToString(bitmap);
                first_file = new File(img_path);

                file_list.add(first_file);
            }

         }
     super.onActivityResult(requestCode, resultCode, data);
    }

    //bitMap转换为file
      public String bitmapToString(Bitmap bitmap){
        //将bitmap转换为uri
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);

        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        actualimagecursor.moveToFirst();

        String img_path = actualimagecursor.getString(actual_image_column_index);
        return img_path;
    }

    //互绑
    private void initIpresenter() {
        mIpresenterImpl=new IpresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        mIpresenterImpl.deatch();
    }

    public void firstShow() {
        first_dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.first_dialog_layout, null);
        //初始化控件
        inflate.findViewById(R.id.first_choosePhoto).setOnClickListener(this);
        inflate.findViewById(R.id.first_takePhoto).setOnClickListener(this);
        inflate.findViewById(R.id.first_btn_cancel).setOnClickListener(this);
        //将布局设置给Dialog
        first_dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = first_dialog.getWindow();
        if(dialogWindow == null){
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        first_dialog.show();//显示对话框
    }

    //判断SD卡是否挂载
    public boolean hasSdcard(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }


}

package com.bw.zweidu.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.zweidu.MainActivity;
import com.bw.zweidu.R;
import com.bw.zweidu.activity.AddressActivity;
import com.bw.zweidu.activity.CircleActivity;
import com.bw.zweidu.activity.DataActivity;
import com.bw.zweidu.activity.FootActivity;
import com.bw.zweidu.activity.MoneyActivity;
import com.bw.zweidu.base.BaseFragment;
import com.bw.zweidu.bean.ImageBean;
import com.bw.zweidu.bean.LoginBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;

public class MyFragment extends BaseFragment implements View.OnClickListener,IView {

    private TextView text_personal;
    private TextView text_circle;
    private TextView text_foot;
    private TextView text_wallet;
    private TextView text_site;
    private SimpleDraweeView my_image_header;
    private IpresenterImpl mIpresenterImpl;
    private LoginBean.ResultBean result;
    private PopupWindow popupWindow;
    private Dialog dialog;
    private static final int PHOTO_REQUEST_CAREMA=1;//拍照
    private static final int PHOTO_REQUEST_GALLERY=2;//从相册中选择
    private static final int PHOTO_REQUEST_CUT=3;//裁剪之后
    private static final String PHOTO_FILE_MAME="header_image.jpg";//临时文件名
    private File file;
    private TextView text_photo;
    private TextView text_camera;
    private String path=Environment.getExternalStorageDirectory()+"/header_image.png";
    @Override
    protected int getlayoutResId() {
        return R.layout.my_fragment;
    }

    @Override
    protected void initView(View view) {
        //获取资源ID
        text_personal = view.findViewById(R.id.my_text_personal);
        text_circle = view.findViewById(R.id.my_text_circle);
        text_foot = view.findViewById(R.id.my_text_foot);
        text_wallet = view.findViewById(R.id.my_text_wallet);
        text_site = view.findViewById(R.id.my_text_address);
        my_image_header = view.findViewById(R.id.my_image_header);
        TextView text_name=view.findViewById(R.id.my_text_name);

        //互绑
        initPresenter();
        text_personal.setOnClickListener(this);
        text_circle.setOnClickListener(this);
        text_foot.setOnClickListener(this);
        text_wallet.setOnClickListener(this);
        text_site.setOnClickListener(this);
        my_image_header.setOnClickListener(this);
        my_image_header.setImageURI(Uri.parse("res://com.onenice.www/" + R.mipmap.bg_wdf));
        result = ((MainActivity) getActivity()).getResult();
        //设置登录名
        text_name.setText(result.getNickName());
        my_image_header.setImageURI(Uri.parse(result.getHeadPic()));

    }



    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            //个人资料
            case R.id.my_text_personal:
                Intent intent =new Intent(getActivity(),DataActivity.class);
                intent.putExtra("result",result);
                startActivity(intent);
                break;
            //朋友圈
            case R.id.my_text_circle:
                Intent intent2 =new Intent(getActivity(),CircleActivity.class);
                startActivity(intent2);
                break;
            //足迹
            case R.id.my_text_foot:
                Intent intent3=new Intent(getActivity(),FootActivity.class);
                startActivity(intent3);
                break;
            //钱包
            case R.id.my_text_wallet:
                Intent intent4=new Intent(getActivity(),MoneyActivity.class);
                startActivity(intent4);
                break;
            //地址
            case R.id.my_text_address:
                Intent intent5=new Intent(getActivity(),AddressActivity.class);
                startActivity(intent5);
                break;
            case R.id.my_image_header:
                show();
                break;
            case R.id.takePhoto:
                        //打开相机
                        Intent intent_takePhoto=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (hasSdcard()){//判断SD卡是否可用
                            file=new File(Environment.getExternalStorageDirectory(),PHOTO_FILE_MAME);
                            //存放到内存中
                            intent_takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));

                        }
                        startActivityForResult(intent_takePhoto, PHOTO_REQUEST_CAREMA);

                dialog.dismiss();
                break;
            case R.id.choosePhoto:
                Intent intent_choosePhoto=new Intent(Intent.ACTION_PICK);
                intent_choosePhoto.setType("image/*");
                startActivityForResult(intent_choosePhoto,PHOTO_REQUEST_GALLERY);

                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;

        }
    }
    public void show() {
        dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        //初始化控件
        inflate.findViewById(R.id.choosePhoto).setOnClickListener(this);
        inflate.findViewById(R.id.takePhoto).setOnClickListener(this);
        inflate.findViewById(R.id.btn_cancel).setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
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
        dialog.show();//显示对话框
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
        startActivityForResult(intent,PHOTO_REQUEST_CUT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==PHOTO_REQUEST_CAREMA&&resultCode==getActivity().RESULT_OK){//相机返回的数据
            if (hasSdcard()){
                crop(Uri.fromFile(new File(path)));
            }else{
                Toast.makeText(getActivity(), "未找到存储啦，无法存储照片", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode==PHOTO_REQUEST_GALLERY&&resultCode==getActivity().RESULT_OK){//相册返回的数据
            //得到图片的全路径
            if (data!=null){
                Uri uri = data.getData();
                crop(uri);
            }
        }else if(requestCode==PHOTO_REQUEST_CUT&&resultCode==getActivity().RESULT_OK){//剪切回来的照片数据
            if (data!=null){
                Bitmap bitmap = data.getParcelableExtra("data");
                //将bitmap转换为uri
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null,null));

                String[] proj = { MediaStore.Images.Media.DATA };

                Cursor actualimagecursor = getActivity().managedQuery(uri,proj,null,null,null);

                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                actualimagecursor.moveToFirst();

                String img_path = actualimagecursor.getString(actual_image_column_index);

                File file = new File(img_path);
                List<File> list=new ArrayList<>();
                list.add(file);
                //mIpresenterImpl.postImageRequestIpresenter(Apis.SHOW_IMAGE_URL,list,ImageBean.class);
                mIpresenterImpl.postimageRequestIpresenter(Apis.SHOW_IMAGE_URL,file,ImageBean.class);
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initPresenter() {
        mIpresenterImpl=new IpresenterImpl(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑
        mIpresenterImpl.deatch();
    }

    @Override
    public void success(Object object) {
        if (object instanceof ImageBean){
            ImageBean imageBean= (ImageBean) object;
            if (imageBean.getStatus().equals("0000")){
                Toast.makeText(getActivity(), imageBean.getMessage(), Toast.LENGTH_SHORT).show();
                my_image_header.setImageURI(Uri.parse(imageBean.getHeadPath()));
            }else{
                Toast.makeText(getActivity(), imageBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void failure(String error) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getFocus();
    }

    private long exitTime=0;
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                    //双击退出
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        exitTime = System.currentTimeMillis();
                    } else {
                        getActivity().finish();
                        System.exit(0);
                    }
                    return true;
                }

                return false;
            }
        });
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

package com.bw.zweidu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.zweidu.R;
import com.bw.zweidu.base.BaseActivity;
import com.bw.zweidu.bean.DataBean;
import com.bw.zweidu.bean.LoginBean;
import com.bw.zweidu.bean.NameBean;
import com.bw.zweidu.bean.PassBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.HashMap;
import java.util.Map;

public class DataActivity extends BaseActivity implements IView {


    private LoginBean.ResultBean result;
    private SimpleDraweeView image_header;
    private TextView text_name;
    private TextView text_pass;
    private IpresenterImpl mIpresenterImpl;
    private EditText edit_name;
    private EditText edit_sureName;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_data;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //获取资源ID
        image_header = findViewById(R.id.data_image_header);
        text_name = findViewById(R.id.data_text_name);
        text_pass = findViewById(R.id.data_text_pass);
    }

    @Override
    protected void initData() {
        //互绑
        initPresenter();
        //获取用户的信息
        initUserUrl();
        //点击修改姓名
        clickName();
        //点击修改密码
        ClickPass();
    }

    private void ClickPass() {
        text_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPass();
            }
        });
    }

    private void initPass() {

        View passView=View.inflate(DataActivity.this,R.layout.alert_pass,null);
        final EditText edit_pass = passView.findViewById(R.id.alert_edit_pass);
        final EditText edit_surepass = passView.findViewById(R.id.alert_edit_surepass);

        final AlertDialog.Builder builder=new AlertDialog
                .Builder(this)
                .setView(passView);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);

        //确定事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    Map<String,String> params=new HashMap<>();
                    params.put("oldPwd",edit_pass.getText().toString());
                    params.put("newPwd",edit_surepass.getText().toString());
                    mIpresenterImpl.putRequestIpresenter(Apis.SHOW_UPDATE_PASS_URL,params,PassBean.class);
                    dialog.dismiss();


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();


    }

    private void clickName() {
        text_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框修改姓名
                initAlert();

            }
        });
    }
    //弹出对话框修改姓名
    private void initAlert() {
        View nameView=View.inflate(DataActivity.this,R.layout.alert_name,null);
        edit_name = nameView.findViewById(R.id.alert_edit_name);
        edit_sureName = nameView.findViewById(R.id.alert_edit_sure);

        final AlertDialog.Builder builder=new AlertDialog
                .Builder(this)
                .setView(nameView);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);

        //确定事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edit_name.getText().toString().equals(edit_sureName.getText().toString())){
                    Map<String,String> params=new HashMap<>();
                    params.put("nickName",edit_name.getText().toString());
                    mIpresenterImpl.putRequestIpresenter(Apis.SHOW_UPDATE_NAME_URL,params,NameBean.class);
                    dialog.dismiss();
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void initUserUrl() {
        mIpresenterImpl.getRequestIpresenter(Apis.SHOW_SELECT_ID_URL,DataBean.class);
    }

    //互绑
    private void initPresenter() {
        mIpresenterImpl=new IpresenterImpl(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        mIpresenterImpl.deatch();
    }

    @Override
    public void success(Object object) {
        if (object instanceof DataBean){
            DataBean dataBean= (DataBean) object;
            if (dataBean.getStatus().equals("0000")){
                image_header.setImageURI(Uri.parse(dataBean.getResult().getHeadPic()));
                text_name.setText(dataBean.getResult().getNickName());
                text_pass.setText(dataBean.getResult().getPassword()+"");
            }
        }
        if (object instanceof NameBean){
            NameBean nameBean= (NameBean) object;
            if (nameBean.getStatus().equals("0000")){
                Toast.makeText(this, nameBean.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, nameBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (object instanceof PassBean){
            PassBean passBean= (PassBean) object;
            if (passBean.getStatus().equals("0000")){
                Toast.makeText(this, passBean.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, passBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void failure(String error) {

    }
}

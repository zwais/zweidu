package com.bw.zweidu.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.zweidu.R;
import com.bw.zweidu.adapter.AddressSelectAdapter;
import com.bw.zweidu.base.BaseActivity;
import com.bw.zweidu.bean.AddressListBean;
import com.bw.zweidu.bean.DefaultBean;
import com.bw.zweidu.bean.SelectAddressBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressActivity extends BaseActivity implements View.OnClickListener,IView {
    private IpresenterImpl mIpresenterImpl;
    private TextView text_finish;
    private RecyclerView address_recy;
    private Button button_add;
    private AddressSelectAdapter addressSelectAdapter;
    private int index;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_address;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //互绑
        initPresenter();
        //获取资源ID
        text_finish = findViewById(R.id.address_text_finish);
        address_recy = findViewById(R.id.address_recy);
        button_add = findViewById(R.id.address_button_add);
        text_finish.setOnClickListener(this);
        button_add.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //初始化recy
        initRecy();
        //查询地址
        initAddressUrl();
    }

    private void initAddressUrl() {
        mIpresenterImpl.getRequestIpresenter(Apis.SHOW_SELECT_ADDRESS_URL,SelectAddressBean.class);
    }

    private void initRecy() {
        //布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        address_recy.setLayoutManager(linearLayoutManager);
        //设置适配器
        addressSelectAdapter = new AddressSelectAdapter(this);
        address_recy.setAdapter(addressSelectAdapter);
        addressSelectAdapter.setUpdate(new AddressSelectAdapter.ClickUpdate() {
            @Override
            public void update(List<SelectAddressBean.ResultBean> list, int position) {
                Intent intent=new Intent(AddressActivity.this,UpdateAddressActivity.class);
                intent.putExtra("name",list.get(position).getRealName());
                intent.putExtra("phone",list.get(position).getPhone());
                AddressListBean addressListBean = new Gson().fromJson(list.get(position).getAddress(), AddressListBean.class);
                intent.putExtra("city",addressListBean.getCity());
                intent.putExtra("address",addressListBean.getAddress());
                intent.putExtra("zipCode",list.get(position).getZipCode());
                intent.putExtra("Id",list.get(position).getId()+"");
                startActivity(intent);
                finish();
            }
        });
        addressSelectAdapter.setChange(new AddressSelectAdapter.ClickChange() {
            @Override
            public void Change(int id,int position) {
                index=position;
                //设置默认地址
                Map<String,String> params=new HashMap<>();
                params.put("id",String.valueOf(id));
                mIpresenterImpl.postRequestIpresenter(Apis.SHOW_DEFAULT_ADDRESS_URL,params,DefaultBean.class);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.address_text_finish:
                finish();
                break;
            case R.id.address_button_add:
                Intent intent=new Intent(AddressActivity.this,NewAddressActivity.class);
                startActivity(intent);
                finish();
                break;
        }
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
        if (object instanceof SelectAddressBean){
            SelectAddressBean selectAddressBean= (SelectAddressBean) object;
            List<SelectAddressBean.ResultBean> result = selectAddressBean.getResult();

            addressSelectAdapter.setList(result);
        }
        if (object instanceof DefaultBean){
            DefaultBean defaultBean= (DefaultBean) object;
            if (defaultBean.getStatus().equals("0000")){
                addressSelectAdapter.setAllunCheck(index);
                Toast.makeText(this, defaultBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void failure(String error) {

    }
}

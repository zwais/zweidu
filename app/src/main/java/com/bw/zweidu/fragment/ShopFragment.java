package com.bw.zweidu.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bw.zweidu.R;
import com.bw.zweidu.activity.CreationActivity;
import com.bw.zweidu.adapter.SelectShopAdapter;
import com.bw.zweidu.base.BaseFragment;
import com.bw.zweidu.bean.AddShopBean;
import com.bw.zweidu.bean.SelectShopBean;
import com.bw.zweidu.bean.ShopSelectListBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopFragment extends BaseFragment implements IView {

    private RecyclerView shop_recy;
    private IpresenterImpl mIpresenterImpl;
    private SelectShopAdapter selectShopAdapter;
    private CheckBox box_all;
    private TextView text_allprice;
    private TextView text_go;
    private double all_price=0;
    private List<SelectShopBean.ResultBean> creation_bill;
    private Map<String, String> map;

    @Override
    protected int getlayoutResId() {
        return R.layout.shop_fragment;
    }

    @Override
    protected void initView(View view) {
        shop_recy = view.findViewById(R.id.shop_recy);
        box_all = view.findViewById(R.id.shop_box_all);
        text_allprice = view.findViewById(R.id.shop_text_allprice);
        text_go = view.findViewById(R.id.shop_text_go);
    }

    @Override
    protected void initData() {

        SharedPreferences user = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        String userId = user.getString("userId", "");
        String sessionId = user.getString("sessionId", "");
        map = new HashMap<>();
        map.put("userId",userId+"");
        map.put("sessionId",sessionId+"");
        //互绑
        initPresenter();
        //发送请求
        initUrl();
        //初始化RecyclerView
        initRecy();
        //结算总价格,数量
        getpriceCount();
        box_all.setChecked(false);
        //点击复选框进行判断
        onClickCheckAll();
        //点击跳转进行创建订单
        onClickCreation();
    }

    private void onClickCreation() {
        text_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String all_price = text_allprice.getText().toString();

                if (!(all_price.equals("0"))&&!(all_price.equals("0.0"))){
                    Intent intent=new Intent(getActivity(),CreationActivity.class);
                    creation_bill = new ArrayList<>();
                    //判断商品是否被选中
                    //如果被选中就放到集合里，通过intent传到activity中
                    for (int i=0;i<shop_list.size();i++)
                        if (shop_list.get(i).isIscheck()) {
                            creation_bill.add(new SelectShopBean.ResultBean(
                                    shop_list.get(i).getCommodityId(),
                                    shop_list.get(i).getCommodityName(),
                                    shop_list.get(i).getCount(),
                                    shop_list.get(i).getPic(),
                                    shop_list.get(i).getPrice()
                            ));
                        }
                    intent.putExtra("creation_bill", (Serializable) creation_bill);
                    startActivity(intent);
                }
            }
        });
    }

    private void onClickCheckAll() {
        box_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断全选时商品的状态
                checkAll(box_all.isChecked());
                selectShopAdapter.notifyDataSetChanged();
            }
        });
    }
    List<SelectShopBean.ResultBean> shop_list=new ArrayList<>();
    private void checkAll(boolean checked) {
        double totalPrice=0;
        int num=0;

        for (int i=0;i<shop_list.size();i++){
            //遍历商品，改变状态
            shop_list.get(i).setIscheck(checked);
            totalPrice=totalPrice+(shop_list.get(i).getPrice()*shop_list.get(i).getCount());
            num=num+shop_list.get(i).getCount();
        }

        if (checked){
            text_allprice.setText(""+totalPrice);
            text_go.setText("去结算("+num+")");
        }else{
            text_allprice.setText("0");
            text_go.setText("去结算");
        }
    }


    private void getpriceCount() {
        selectShopAdapter.setOnClick(new SelectShopAdapter.ShopClick() {
            @Override
            public void shopPrice(List<SelectShopBean.ResultBean> list) {
                //在这里重新遍历已经改变状态后的数据
                //这里不能break跳出，因为还有需要计算后面点击商品的价格和数量，所以必须跑完整个循环
                double totalPrice=0;
                //勾选商品的数量，不是该商品购买的数量
                int num=0;
                //所有商品总数，和上面的数量做比对，如果两者相等，则说明全选
                int totalNum=0;
                for (int i=0;i<list.size();i++){
                    totalNum=totalNum+list.get(i).getCount();
                    if (list.get(i).isIscheck()){
                        totalPrice=totalPrice+list.get(i).getPrice()*list.get(i).getCount();
                        num=num+list.get(i).getCount();
                    }

                }
                if (num<totalNum){
                    box_all.setChecked(false);
                }else{
                    box_all.setChecked(true);
                }
                text_allprice.setText(""+totalPrice);
                text_go.setText("去结算("+num+")");

                if (list.size()==0){
                    box_all.setChecked(false);
                }
            }
        });
        selectShopAdapter.setRemove(new SelectShopAdapter.RemoveCallBack() {
            @Override
            public void removeposition(List<SelectShopBean.ResultBean> list, int position) {
                //在这里重新遍历已经改变状态后的数据
                //这里不能break跳出，因为还有需要计算后面点击商品的价格和数量，所以必须跑完整个循环
                double totalPrice=0;
                //勾选商品的数量，不是该商品购买的数量
                int num=0;
                //所有商品总数，和上面的数量做比对，如果两者相等，则说明全选
                int totalNum=0;
                for (int i=0;i<list.size();i++){
                    totalNum=totalNum+list.get(i).getCount();
                    if (list.get(i).isIscheck()){
                        totalPrice=totalPrice+list.get(i).getPrice()*list.get(i).getCount();
                        num=num+list.get(i).getCount();
                    }

                }
                if (num<totalNum){
                    box_all.setChecked(false);
                }else{
                    box_all.setChecked(true);
                }
                shop_list.remove(position);
                text_allprice.setText(""+totalPrice);
                text_go.setText("去结算("+num+")");
                //添加购物车的集合
                List<ShopSelectListBean> addlist=new ArrayList<>();
                for (int i=0;i<list.size();i++){
                    int commodityId = list.get(i).getCommodityId();
                    int count = list.get(i).getCount();
                    addlist.add(new ShopSelectListBean(Integer.valueOf(commodityId),count));
                }
                String data="[";
                for (ShopSelectListBean bean : addlist){
                    data+="{\"commodityId\":"+bean.getCommodityId()+",\"count\":"+bean.getCount()+"},";
                }
                String substring = data.substring(0, data.length() - 1);
                substring+="]";
                Map<String,String> params = new HashMap<>();
                params.put("data",substring);

                mIpresenterImpl.putRequestIpresenter(Apis.SHOW_ADD_SHOP_URL,params,AddShopBean.class);
                if (list.size()==0){
                    box_all.setChecked(false);
                }
            }
        });
    }

    private void initUrl() {
        mIpresenterImpl.setShopcart_list(Apis.SHOW_SELECT_SHOP_URL,map,SelectShopBean.class);
    }

    private void initRecy() {
        //布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        shop_recy.setLayoutManager(linearLayoutManager);
        //设置适配器
        selectShopAdapter = new SelectShopAdapter(getActivity());
        shop_recy.setAdapter(selectShopAdapter);
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
        Log.i("========",object.toString());
        if (object instanceof SelectShopBean){
            SelectShopBean selectShopBean= (SelectShopBean) object;
            Toast.makeText(getActivity(), ""+selectShopBean.getMessage(), Toast.LENGTH_SHORT).show();
            selectShopAdapter.setList(selectShopBean.getResult());
            shop_list=selectShopBean.getResult();
            box_all.setChecked(false);
        }
    }

    @Override
    public void failure(String error) {

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

    @Override
    public void onResume() {
        super.onResume();
        getFocus();
    }
}

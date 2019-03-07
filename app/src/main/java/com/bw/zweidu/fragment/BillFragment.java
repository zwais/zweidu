package com.bw.zweidu.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bw.zweidu.R;
import com.bw.zweidu.activity.EvaluatActivity;
import com.bw.zweidu.activity.PayActivity;
import com.bw.zweidu.adapter.BillXrecyAdapter;
import com.bw.zweidu.base.BaseFragment;
import com.bw.zweidu.bean.BillShopBean;
import com.bw.zweidu.bean.CreationMsgBean;
import com.bw.zweidu.bean.DeleteBillBean;
import com.bw.zweidu.bean.NextBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BillFragment extends BaseFragment implements IView,View.OnClickListener {

    private XRecyclerView bill_xrecy;
    private IpresenterImpl mIpresenterImpl;
    private int page;
    private int status=0;
    private int COUNT_ITEM=5;
    private ImageView image_allbill;
    private ImageView image_pay;
    private ImageView image_task;
    private ImageView image_appraise;
    private ImageView image_finish;
    private BillXrecyAdapter billXrecyAdapter;
    private int all_staus;
    private boolean bill_flag;
    private int index;
    private LinearLayout lin_group;

    @Override
    protected int getlayoutResId() {
        return R.layout.bill_fragment;
    }

    @Override
    protected void initView(View view) {
        bill_xrecy = view.findViewById(R.id.bill_xrecy);
        image_allbill = view.findViewById(R.id.bill_image_allbill);
        image_pay = view.findViewById(R.id.bill_image_pay);
        image_task = view.findViewById(R.id.bill_image_task);
        image_appraise = view.findViewById(R.id.bill_image_appraise);
        image_finish = view.findViewById(R.id.bill_image_finish);
        lin_group = view.findViewById(R.id.bill_group);
        image_allbill.setOnClickListener(this);
        image_pay.setOnClickListener(this);
        image_task.setOnClickListener(this);
        image_appraise.setOnClickListener(this);
        image_finish.setOnClickListener(this);
        //注册
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        //互绑
        initPresenter();
        //初始化Xrecy
        initXrecy();

    }

    private void initXrecy() {
        page=1;
        //布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        bill_xrecy.setLayoutManager(linearLayoutManager);
        //设置适配器
        billXrecyAdapter = new BillXrecyAdapter(getActivity());
        bill_xrecy.setAdapter(billXrecyAdapter);
        //支持刷新加载
        bill_xrecy.setPullRefreshEnabled(true);
        bill_xrecy.setLoadingMoreEnabled(true);
        bill_xrecy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                initBilllUrl(all_staus,page);
            }

            @Override
            public void onLoadMore() {
                if (bill_flag){
                    bill_xrecy.loadMoreComplete();
                }else{
                    bill_xrecy.setLoadingMoreEnabled(true);
                }

                initBilllUrl(all_staus,page);
            }
        });
        initBilllUrl(status,page);

        //删除订单
        billXrecyAdapter.setDelete(new BillXrecyAdapter.ClickDelete() {
            @Override
            public void delete(final String orderId, final int position) {

                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("确定删除订单吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIpresenterImpl.deleteRequestIpresenter(String.format(Apis.SHOW_DELETE_BILL_URL,orderId),DeleteBillBean.class);
                        index = position;
                    }
                });
                builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

            }
        });
        //付款
        billXrecyAdapter.setGo(new BillXrecyAdapter.ClickGo() {
            @Override
            public void go(String orderId,String all_price) {
                Intent intent=new Intent(getActivity(),PayActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("all_price",all_price);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            //查看全部
            case R.id.bill_image_allbill:
                bill_xrecy.setBackgroundResource(R.drawable.quanbu);
                lin_group.setBackgroundResource(R.drawable.quanbu);
                status=0;
                page=1;
                initBilllUrl(status,page);
                break;
            //查询待付款
            case R.id.bill_image_pay:
                bill_xrecy.setBackgroundResource(R.drawable.fukaun);
                lin_group.setBackgroundResource(R.drawable.fukaun);
                status=1;
                page=1;
                initBilllUrl(status,page);
                break;
            //查看待收货
            case R.id.bill_image_task:

                break;
            //查看带评价
            case R.id.bill_image_appraise:

                break;
            //查看已完成
            case R.id.bill_image_finish:

                break;
        }
    }

    //根据订单的状态查询订单
    private void initBilllUrl(int status, int page) {
        all_staus = status;
        mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_BILL_SHOP_URL,status,page,COUNT_ITEM),BillShopBean.class);
    }

    //互绑
    private void initPresenter() {
        mIpresenterImpl=new IpresenterImpl(this);
    }
    //打开未支付
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getPay(CreationMsgBean msgBean){
        if (msgBean.getFlag().equals("open")){
            mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_BILL_SHOP_URL,1,page,COUNT_ITEM),BillShopBean.class);
            EventBus.getDefault().removeStickyEvent(new CreationMsgBean());
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //反注册
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑
        mIpresenterImpl.deatch();
    }

    @Override
    public void success(Object object) {
        if (object instanceof BillShopBean){
            BillShopBean billShopBean= (BillShopBean) object;
            if (page==1){
                billXrecyAdapter.setList(billShopBean.getOrderList());
            }else{
                billXrecyAdapter.addList(billShopBean.getOrderList());
            }
            //停止刷新加载
            bill_xrecy.refreshComplete();
            bill_xrecy.loadMoreComplete();
            page++;
            if (billShopBean.getOrderList().size()==0){
                bill_flag=true;
            }else{
                bill_flag=false;
            }
        }
        if (object instanceof DeleteBillBean){
            DeleteBillBean deleteBillBean= (DeleteBillBean) object;
            if (deleteBillBean.getStatus().equals("0000")){
                billXrecyAdapter.deleteBill(index);
                Toast.makeText(getActivity(), deleteBillBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (object instanceof NextBean) {
            NextBean nextBean= (NextBean) object;
            if (nextBean.getStatus().equals("0000")){
                mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_BILL_SHOP_URL,2,page,COUNT_ITEM),BillShopBean.class);
                Toast.makeText(getActivity(), nextBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
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

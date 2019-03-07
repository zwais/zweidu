package com.bw.zweidu.fragment;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bw.zweidu.R;
import com.bw.zweidu.base.BaseFragment;
import com.bw.zweidu.bean.AddShopBean;
import com.bw.zweidu.bean.AddShopJsonBean;
import com.bw.zweidu.bean.ComMsgBean;
import com.bw.zweidu.bean.ParticularsBean;
import com.bw.zweidu.bean.SelectShopBean;
import com.bw.zweidu.bean.ShopSelectListBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeParticularsFragment extends BaseFragment implements IView {
    private IpresenterImpl mIpresenterImpl;

    private XBanner xBanner;
    private List<String> imageurl;
    private TextView text_price;
    private TextView text_num;
    private TextView text_content;
    private TextView text_kg;
    private TextView text_name;
    private WebView webView;
    private ImageView image_back;
    private ImageView image_add;
    private ImageView image_buy;
    private int addId;
    @Override
    protected int getlayoutResId() {
        return R.layout.home_particulars_fragment;
    }

    @Override
    protected void initView(View view) {
        //注册
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        //获取资源ID
        xBanner = view.findViewById(R.id.par_xbanner);
        text_price = view.findViewById(R.id.par_text_price);
        text_num = view.findViewById(R.id.par_text_num);
        text_content = view.findViewById(R.id.par_text_content);
        text_kg = view.findViewById(R.id.par_text_kg);
        text_name = view.findViewById(R.id.par_text_name);
        webView = view.findViewById(R.id.par_webview);
        image_back = view.findViewById(R.id.par_image_back);
        image_add = view.findViewById(R.id.par_image_addshop);
        image_buy = view.findViewById(R.id.par_image_buy);
        initPresenter();
    }

    @Override
    protected void initData() {
        //初始化xbanner
        initxBanner();
        //点击返回
        initBack();
        //点击添加购物车
        onClickAdd();

    }

    private void onClickAdd() {

        image_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //添加购物车之前先查询购物车
               initSelectShop();

            }
        });
    }

    private void initSelectShop() {
        mIpresenterImpl.getRequestIpresenter(Apis.SHOW_SELECT_SHOP_URL,SelectShopBean.class);

    }

    private void initBack() {
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ComMsgBean(0,"back"));
            }
        });
    }

    private void initxBanner() {
        //设置间隔时间
        xBanner.setAutoPalyTime(3000);
        xBanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(getActivity()).load(imageurl.get(position)).into((ImageView) view);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void getComid(ComMsgBean msgBean){
        if (msgBean.getFlag().equals("par")){
            int commodityId = msgBean.getCommodityId();
            addId=commodityId;
            setUrl(commodityId);
            //清除缓存
            EventBus.getDefault().removeStickyEvent(ComMsgBean.class);
    }
    }

    private void setUrl(int commodityId) {
        mIpresenterImpl.getRequestIpresenter(String.format(Apis.SHOW_Particulars_URL,commodityId),ParticularsBean.class);
    }

    //互绑
    private void initPresenter() {
        mIpresenterImpl=new IpresenterImpl(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        xBanner.startAutoPlay();
    }
    @Override
    public void onStop() {
        super.onStop();
        xBanner.stopAutoPlay();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑
        mIpresenterImpl.deatch();
        if (webView != null) {
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //反注册
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void success(Object object) {
        //商品详情
        if (object instanceof ParticularsBean){
            ParticularsBean particularsBean= (ParticularsBean) object;
            ParticularsBean.ResultBean result = particularsBean.getResult();
            text_price.setText("￥"+result.getPrice());
            text_num.setText("已售"+result.getCommentNum()+"件");
            text_name.setText(result.getCommodityName());
            text_content.setText(result.getDescribe());
            text_kg.setText(result.getWeight()+"kg");
            webView.loadDataWithBaseURL(null,result.getDetails(),"text/html","utf-8",null);
            imageurl=new ArrayList<>();
            //把图片的Url放在list集合中
            String[] images= particularsBean.getResult().getPicture().split("\\,");
            for (int i=0;i<images.length;i++){
                imageurl.add(images[i]);
            }
            xBanner.setData(imageurl,null);
        }
        //查询购物车
        if (object instanceof SelectShopBean){
            SelectShopBean selectShopBean= (SelectShopBean) object;
            //添加购物车的集合
            List<ShopSelectListBean> list=new ArrayList<>();
            //得到查询购物车的集合
            List<SelectShopBean.ResultBean> result = selectShopBean.getResult();
            //遍历放到添加购物车的集合中
            for (int i=0;i<result.size();i++){
                list.add(new ShopSelectListBean(result.get(i).getCommodityId(),result.get(i).getCount()));
            }
            //添加购物车时进行判断
            addShopList(list);
        }

        if (object instanceof AddShopBean){
            AddShopBean addShopBean= (AddShopBean) object;
            if (addShopBean.getStatus().equals("0000")){
                Toast.makeText(getActivity(), addShopBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addShopList(List<ShopSelectListBean> list) {
        String data="[";
        for (int i=0;i<list.size();i++){
            //判断如果加入商品的id和集合里有相同的就count+1
            if (Integer.valueOf(addId)==list.get(i).getCommodityId()){
                int count = list.get(i).getCount();
                count++;
                list.get(i).setCount(count);
                break;
                //如果遍历完毕没有相同的商品，就把当前的商品加入到购物车
            }else if (i==list.size()-1){
                list.add(new ShopSelectListBean(Integer.valueOf(addId),1));
                break;
            }
        }
        for (ShopSelectListBean bean : list){
            data+="{\"commodityId\":"+bean.getCommodityId()+",\"count\":"+bean.getCount()+"},";
        }
        String substring = data.substring(0, data.length() - 1);
        substring+="]";
        Map<String,String> params = new HashMap<>();
        params.put("data",substring);
        mIpresenterImpl.putRequestIpresenter(Apis.SHOW_ADD_SHOP_URL,params,AddShopBean.class);

    }

    @Override
    public void failure(String error) {

        if (error.equals("HTTP 500")){
            AddShopJsonBean creationJsonBean=new AddShopJsonBean();
            creationJsonBean.setCommodityId(addId);
            creationJsonBean.setCount(1);
            List<AddShopJsonBean> list=new ArrayList<>();
            list.add(creationJsonBean);
            String data = new Gson().toJson(list);
            Map<String,String> params = new HashMap<>();
            params.put("data",data);
            mIpresenterImpl.putRequestIpresenter(Apis.SHOW_ADD_SHOP_URL,params,AddShopBean.class);
        }else{
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
        }


    }
}

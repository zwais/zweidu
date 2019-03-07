package com.bw.zweidu.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bw.zweidu.R;
import com.bw.zweidu.ShowViewPagerTransformer;
import com.bw.zweidu.adapter.ShowBannerAdapter;
import com.bw.zweidu.adapter.ShowFashionShopAdapter;
import com.bw.zweidu.adapter.ShowLiveAdapter;
import com.bw.zweidu.adapter.ShowNewShopAdapter;
import com.bw.zweidu.base.BaseFragment;
import com.bw.zweidu.base.ShowShopBean;
import com.bw.zweidu.bean.ShowBannerBean;
import com.bw.zweidu.bean.ShowShopMsgBean;
import com.bw.zweidu.presenter.IpresenterImpl;
import com.bw.zweidu.util.Apis;
import com.bw.zweidu.view.IView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class HomeShowFragment extends BaseFragment implements View.OnClickListener,IView {
    private ImageView show_navigation;
    private ImageView show_search;
    private ViewPager show_viewpager;
    private IpresenterImpl mIpresenterImpl;
    private ShowBannerAdapter banneradapter;
    private RecyclerView show_new_recy;
    private ShowNewShopAdapter showNewShopAdapter;
    private RecyclerView show_fashion_recy;
    private ShowFashionShopAdapter showFashionShopAdapter;
    private RecyclerView show_live_recy;
    private final int COUNT_ITEM=2;

    private List<ShowShopBean.ResultBean.MlssBean> fashionlist;
    private List<ShowShopBean.ResultBean.PzshBean> livelist;
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            show_viewpager.setCurrentItem(show_viewpager.getCurrentItem()+1);
            sendEmptyMessageDelayed(0,2000);
        }
    };
    private ShowLiveAdapter showLiveAdapter;
    private ImageView show_image_new;
    private ImageView show_image_fashion;
    private ImageView show_image_live;
    private ShowShopBean showShopBean;
    private List<ShowShopBean.ResultBean.RxxpBean> newlist;
    private int newid;
    private int fashionid;
    private int liveid;

    @Override
    protected int getlayoutResId() {
        return R.layout.home_show_fragment;
    }

    @Override
    protected void initView(View view) {
        //获取资源ID
        show_navigation = view.findViewById(R.id.show_navigation);
        show_search = view.findViewById(R.id.show_search);
        show_viewpager = view.findViewById(R.id.show_viewpager);
        show_new_recy = view.findViewById(R.id.show_new_recy);
        show_fashion_recy = view.findViewById(R.id.show_fashion_recy);
        show_live_recy = view.findViewById(R.id.show_live_recy);
        show_image_new = view.findViewById(R.id.show_image_new);
        show_image_fashion = view.findViewById(R.id.show_image_fashion);
        show_image_live = view.findViewById(R.id.show_image_live);
        setOnClick();
        //设置轮播图
        initBanner(view);

    }

    private void setOnClick() {
        show_image_new.setOnClickListener(this);
        show_image_fashion.setOnClickListener(this);
        show_image_live.setOnClickListener(this);
        show_navigation.setOnClickListener(this);
        show_search.setOnClickListener(this);
    }

    private void initBanner(View view) {
        show_viewpager.setOffscreenPageLimit(5);//设置预加载数量
        show_viewpager.setPageMargin(20);//控制两幅图之间的间距,尽量以屏幕的宽度来确定
        show_viewpager.setPageTransformer(true,new ShowViewPagerTransformer());//3D画廊模式
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(show_viewpager.getCurrentItem(),2000);
        //viewPager左右两边滑动无效的处理
        view.findViewById(R.id.linone).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return show_viewpager.dispatchTouchEvent(motionEvent);
            }
        });

    }

    @Override
    protected void initData() {
        initPresenter();
        initBannerUrl();
        //发送商品的请求
        initShopUrl();
        //热销商品
        initNewRecy();
        //魔力时尚
        initFashionRecy();
        //品质生活
        iniyLiveRecy();
    }

    private void iniyLiveRecy() {
        //布局管理器
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),COUNT_ITEM);
        gridLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        show_live_recy.setLayoutManager(gridLayoutManager);
        //设置适配器
        showLiveAdapter = new ShowLiveAdapter(getActivity());
        show_live_recy.setAdapter(showLiveAdapter);
    }

    private void initFashionRecy() {
        //布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        show_fashion_recy.setLayoutManager(linearLayoutManager);
        //设置适配器
        showFashionShopAdapter = new ShowFashionShopAdapter(getActivity());
        show_fashion_recy.setAdapter(showFashionShopAdapter);
    }

    private void initNewRecy() {
        //布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        show_new_recy.setLayoutManager(linearLayoutManager);
        //设置适配器
        showNewShopAdapter = new ShowNewShopAdapter(getActivity());
        show_new_recy.setAdapter(showNewShopAdapter);

    }

    private void initShopUrl() {
        mIpresenterImpl.getRequestIpresenter(Apis.SHOW_SHOP_URL,ShowShopBean.class);
    }
    //轮播图
    private void initBannerUrl() {
        mIpresenterImpl.getRequestIpresenter(Apis.SHOW_BANNER_URL,ShowBannerBean.class);
    }

    @Override
    public void success(Object object) {
        //轮播图
        if (object instanceof ShowBannerBean){
            ShowBannerBean showBannerBean= (ShowBannerBean) object;
            List<ShowBannerBean.ResultBean> result = showBannerBean.getResult();
            banneradapter = new ShowBannerAdapter(result,getActivity());
            show_viewpager.setAdapter(banneradapter);
            //设置下标
            show_viewpager.setCurrentItem(2000);
            //按下停止切换,抬起继续轮播
            banneradapter.setImageOnTouch(new ShowBannerAdapter.ImageOnTouch() {
                @Override
                public void down(int position) {
                    handler.removeCallbacksAndMessages(null);
                }

                @Override
                public void up(int position) {
                    handler.sendEmptyMessageDelayed(position,2000);
                }

            });

        }
        //商品信息
        if (object instanceof ShowShopBean){
            showShopBean = (ShowShopBean) object;
            newlist=new ArrayList<>();
            fashionlist=new ArrayList<>();
            livelist=new ArrayList<>();
            newid = showShopBean.getResult().getRxxp().getId();
            fashionid = showShopBean.getResult().getMlss().getId();
            liveid = showShopBean.getResult().getPzsh().getId();
            List<ShowShopBean.ResultBean.RxxpBean.CommodityListBean> rxxp = showShopBean.getResult().getRxxp().getCommodityList();
            List<ShowShopBean.ResultBean.MlssBean.CommodityListBeanXX> mlss = showShopBean.getResult().getMlss().getCommodityList();
            List<ShowShopBean.ResultBean.PzshBean.CommodityListBeanX> pzsh = showShopBean.getResult().getPzsh().getCommodityList();
            //新品热销
            showNewShopAdapter.setList(rxxp);
            //魔力时尚
            showFashionShopAdapter.setList(mlss);
            //品质生活
            showLiveAdapter.setList(pzsh);
        }

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            //新品热销更多
            case R.id.show_image_new:
              //  int newid = newlist.get(0).getId();
                EventBus.getDefault().post(new ShowShopMsgBean(String.valueOf(newid),"new"));
                break;
            //魔力时尚更多
            case R.id.show_image_fashion:
                //int fashionid = fashionlist.get(0).getId();
                EventBus.getDefault().post(new ShowShopMsgBean(String.valueOf(fashionid),"fashion"));
                break;
            //品质生活更多
            case R.id.show_image_live:
              //  int liveid = livelist.get(0).getId();
                EventBus.getDefault().post(new ShowShopMsgBean(String.valueOf(liveid),"live"));

                break;
            //导航搜索
            case R.id.show_navigation:
                EventBus.getDefault().post(new ShowShopMsgBean(v,"navigation"));
                break;
            //搜索框搜索
            case R.id.show_search:
                EventBus.getDefault().post(new ShowShopMsgBean("search","search"));
                break;
            default:break;
        }
    }

    //互绑
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
    public void failure(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

}

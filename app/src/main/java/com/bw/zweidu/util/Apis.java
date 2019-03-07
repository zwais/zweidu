package com.bw.zweidu.util;
public class Apis {

    //登录接口
    public static final String LOGIN_URL="user/v1/login";
    //注册接口
    public static final String REGISTER_URL="user/v1/register";
    //首页轮播图的接口
    public static final String SHOW_BANNER_URL="commodity/v1/bannerShow";
    //首页商品的接口
    public static final String SHOW_SHOP_URL="commodity/v1/commodityList";
    //首页商品详细信息的接口
    public static final String SHOW_CLASSIFY_URL="commodity/v1/findCommodityListByLabel?labelId=%s&page=%d&count=%d";
    //商品查询的接口
    public static final String SHOW_SEARCH_URL="commodity/v1/findCommodityByKeyword?keyword=%s&page=%d&count=%d";

    //查询一级列表分类的接口
    public static final String SHOW_NAV_ONE_URL="commodity/v1/findFirstCategory";
    //查询二级列表分类的接口
    public static final String SHOW_NAV_TWO_URL="commodity/v1/findSecondCategory?firstCategoryId=%s";
    //查询二级商品的接口
    public static final String SHOW_NAV_TWO_SHOP_URL="commodity/v1/findCommodityByCategory?categoryId=%s&page=%d&count=%d";
    //查询圈子列表接口
    public static final String SHOW_CIRCLE_LIST_URL="circle/v1/findCircleList?page=%d&count=%d";
    //圈子点赞接口
    public static final String SHOW_CIRCLE_LIKE_URL="circle/verify/v1/addCircleGreat";
    //圈子取消点赞接口
    public static final String SHOW_CIRCLE_CANCEL_URL="circle/verify/v1/cancelCircleGreat?circleId=%d";
    //商品详情接口
    public static final String SHOW_Particulars_URL="commodity/v1/findCommodityDetailsById?commodityId=%d";
    //加入购物车的接口
    public static final String SHOW_ADD_SHOP_URL="order/verify/v1/syncShoppingCart";
    //查询购物车的接口
    public static final String SHOW_SELECT_SHOP_URL="order/verify/v1/findShoppingCart";
    //查询收货地址的接口
    public static final String SHOW_SELECT_ADDRESS_URL="user/verify/v1/receiveAddressList";
    //创建订单的接口
    public static final String SHOW_CREATION_SHOP_URL="order/verify/v1/createOrder";
    //查询订单的接口
    public static final String SHOW_BILL_SHOP_URL="order/verify/v1/findOrderListByStatus?status=%d&page=%d&count=%d";
    //删除订单的接口
    public static final String SHOW_DELETE_BILL_URL="order/verify/v1/deleteOrder?orderId=%s";
    //修改收货地址的接口
    public static final String SHOW_UPDATE_ADDRESS_URL="user/verify/v1/changeReceiveAddress";
    //新增收货地址的接口
    public static final String SHOW_ADD_ADDRESS_URL="user/verify/v1/addReceiveAddress";
    //设置默认地址的接口
    public static final String SHOW_DEFAULT_ADDRESS_URL="user/verify/v1/setDefaultReceiveAddress";
    //去支付的接口
    public static final String SHOW_PAY_SHOP_URL="order/verify/v1/pay";
    //收货的接口
    public static final String SHOW_NEXT_SHOP_URL="order/verify/v1/confirmReceipt";
    //我的足迹的接口
    public static final String SHOW_FOOT_SHOP_URL="commodity/verify/v1/browseList?page=%d&count=%d";
    //查询钱包的接口
    public static final String SHOW_SELECT_MONEY_URL="user/verify/v1/findUserWallet?page=%d&count=%d";
    //查询用户的接口
    public static final String SHOW_SELECT_ID_URL="user/verify/v1/getUserById";
    //修改昵称的接口
    public static final String SHOW_UPDATE_NAME_URL="user/verify/v1/modifyUserNick";
    //修改密码的接口
    public static final String SHOW_UPDATE_PASS_URL="user/verify/v1/modifyUserPwd";
    //上传头像的接口
    public static final String SHOW_IMAGE_URL="user/verify/v1/modifyHeadPic";
    //展示我的圈子的接口
    public static final String SHOW_SELECT_CIRCLE_URL="circle/verify/v1/findMyCircleById?page=%d&count=%d";
    //发布圈子的接口
    public static final String SHOW_NEXT_CIRCLE_URL="circle/verify/v1/releaseCircle";
    //删除圈子的接口
    public static final String SHOW_DELETE_CIRCLE_URL="circle/verify/v1/deleteCircle?circleId=%d";
    //发布评论的接口
    public static final String SHOW_NEXT_COMMENT_URL="commodity/verify/v1/addCommodityComment";
}

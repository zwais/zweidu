package com.bw.zweidu.model;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface Imodel {

    //get请求
    void getRequestModel( String url,Class clazz, ModelCallBack callBack );

    void getShopcart_list( String url,Map<String,String> map,Class clazz ,ModelCallBack callBack);
    //post请求
    void postRequestModel( String url, Map<String, String> params, Class clazz, ModelCallBack callBack );
    //delete请求
    void deleteRequestModel( String url, Class clazz, ModelCallBack callBack );
    //put请求
    void putRequestModel( String url, Map<String, String> params, Class clazz, ModelCallBack callBack );
    //上传图片
    void postImageRequestModel( String url, List<File> image_list, Class clazz, ModelCallBack callBack );
    void postimageRequestModel( String url, File file, Class clazz, ModelCallBack callBack );
    //上传图文
    void postImageConRequestModel( String url, Map<String, String> params, File file, Class clazz, ModelCallBack callBack );
    //多图
    void postDuoConRequestModel( String url, Map<String, String> params, List<File> list, Class clazz, ModelCallBack callBack );


}

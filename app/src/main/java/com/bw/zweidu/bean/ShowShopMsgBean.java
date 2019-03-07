package com.bw.zweidu.bean;
public class ShowShopMsgBean {
    private Object object;

    private String flag;

    public ShowShopMsgBean(Object object, String flag) {
        this.object = object;
        this.flag = flag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}

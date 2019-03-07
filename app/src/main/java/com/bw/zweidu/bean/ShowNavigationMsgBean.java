package com.bw.zweidu.bean;

public class ShowNavigationMsgBean {
    private Object object;
    private String flag;

    public ShowNavigationMsgBean(Object object, String flag) {
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

package com.bw.zweidu.bean;

public class ComMsgBean {
    private int commodityId;
    private String flag;

    public ComMsgBean(int commodityId, String flag) {
        this.commodityId = commodityId;
        this.flag = flag;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}

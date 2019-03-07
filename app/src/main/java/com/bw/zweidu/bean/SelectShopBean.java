package com.bw.zweidu.bean;

import java.io.Serializable;
import java.util.List;

public class SelectShopBean implements Serializable {
    private String message;
    private String status;
    private List<ResultBean> result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        private int commodityId;
        private String commodityName;
        private int count;
        private String pic;
        private int price;
        private boolean ischeck=false;

        public ResultBean(int commodityId, String commodityName, int count, String pic, int price) {
            this.commodityId = commodityId;
            this.commodityName = commodityName;
            this.count = count;
            this.pic = pic;
            this.price = price;
        }

        public ResultBean() {
        }

        public boolean isIscheck() {
            return ischeck;
        }

        public void setIscheck(boolean ischeck) {
            this.ischeck = ischeck;
        }

        public int getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(int commodityId) {
            this.commodityId = commodityId;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }
}

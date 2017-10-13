package com.cnsunway.saas.wash.model;

/**
 * Created by LL on 2016/1/14.
 */
public class Marketing {

    String marketingUrl;
    String picUrl;
    String name;
    boolean needLogin;

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarketingUrl() {
        return marketingUrl;
    }

    public void setMarketingUrl(String marketingUrl) {
        this.marketingUrl = marketingUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}

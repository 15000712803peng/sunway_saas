package com.cnsunway.saas.wash.model;

/**
 * Created by LL on 2015/12/4.
 */
public class User {

    String headPortraitUrl="";
    String token;
    String wxNickName;
    String mobile;
    boolean hasNapaCard = true;
    String hxPwd;

    public String getHxPwd() {
        return hxPwd;
    }

    public void setHxPwd(String hxPwd) {
        this.hxPwd = hxPwd;
    }

    public boolean isHasNapaCard() {
        return hasNapaCard;
    }

    public void setHasNapaCard(boolean hasNapaCard) {
        this.hasNapaCard = hasNapaCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeadPortraitUrl() {
        return headPortraitUrl;
    }

    public void setHeadPortraitUrl(String headPortraitUrl) {
        this.headPortraitUrl = headPortraitUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWxNickName() {
        return wxNickName;
    }

    public void setWxNickName(String wxNickName) {
        this.wxNickName = wxNickName;
    }
}

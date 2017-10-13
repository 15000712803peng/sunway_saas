package com.cnsunway.saas.wash.model;

/**
 * Created by LL on 2015/12/4.
 */
public class User {
    String headPortraitUrl = "";
    String wxNickName = "";
    boolean hasNapaCard = true;
    String hxPwd = "";

    String id;
    boolean isTester;
    String lastLoginChannel;
    String lastLoginDate;
    String mobile;
    String registerChannel ;
    String reginsterDate;
    String token;
    String userOid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTester() {
        return isTester;
    }

    public void setTester(boolean tester) {
        isTester = tester;
    }

    public String getLastLoginChannel() {
        return lastLoginChannel;
    }

    public void setLastLoginChannel(String lastLoginChannel) {
        this.lastLoginChannel = lastLoginChannel;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }


    public String getRegisterChannel() {
        return registerChannel;
    }

    public void setRegisterChannel(String registerChannel) {
        this.registerChannel = registerChannel;
    }

    public String getReginsterDate() {
        return reginsterDate;
    }

    public void setReginsterDate(String reginsterDate) {
        this.reginsterDate = reginsterDate;
    }


    public String getUserOid() {
        return userOid;
    }

    public void setUserOid(String userOid) {
        this.userOid = userOid;
    }


    /*  "id":22,
        "isTester":false,
        "lastLoginChannel":3,
        "lastLoginDate":"2017-10-13 11:47:23",
        "mobile":"13661449137",
        "registerChannel":3,
        "registerDate":"2017-10-13 11:47:23",
        "token":"12d5a78c0f0b4ce0a00774c838a996f6",
        "userOid":"98d548f99b424721a2840033c138e6b8"*/

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

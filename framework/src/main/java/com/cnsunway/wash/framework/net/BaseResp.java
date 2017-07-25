package com.cnsunway.wash.framework.net;

/**
 * Created by LL on 2015/10/21.
 */
public class BaseResp {
    protected int responseCode = Integer.MAX_VALUE;
    protected String responseMsg;
    protected String now;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

}


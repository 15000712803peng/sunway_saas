package com.cnsunway.saas.wash.model;

/**
 * Created by LL on 2016/2/25.
 */
public class WxRechargeInfo {

    WepayConfig params;
    String outTradeNo;

    public WepayConfig getParams() {
        return params;
    }

    public void setParams(WepayConfig params) {
        this.params = params;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}

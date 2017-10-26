package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2017/10/26 0026.
 */

public class WePrePayInfo {
    String outTradeNo;
    WepayConfig params;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public WepayConfig getParams() {
        return params;
    }

    public void setParams(WepayConfig params) {
        this.params = params;
    }
}

package com.cnsunway.saas.wash.model;

/**
 * Created by LL on 2016/2/25.
 */
public class WxRechargeInfo {

    WepayConfig payInfo;
    String depositOrderNo;

    public WepayConfig getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(WepayConfig payInfo) {
        this.payInfo = payInfo;
    }

    public String getDepositOrderNo() {
        return depositOrderNo;
    }

    public void setDepositOrderNo(String depositOrderNo) {
        this.depositOrderNo = depositOrderNo;
    }
}

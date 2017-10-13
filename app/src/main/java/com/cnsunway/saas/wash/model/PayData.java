package com.cnsunway.saas.wash.model;

/**
 * Created by LL on 2015/12/14.
 */
public class PayData {
    private int payChannel;
    private String amount;
    private String couponNo;

    public PayData(int payChannel, String amount, String couponNo) {
        this.payChannel = payChannel;
        this.amount = amount;
        this.couponNo = couponNo;
    }

    public PayData(int payChannel, String amount) {
        this.payChannel = payChannel;
        this.amount = amount;
    }

    public PayData(int payChannel) {
        this.payChannel = payChannel;

    }

    public PayData(){

    }

    public int getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(int payChannel) {
        this.payChannel = payChannel;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PayData)) return false;

        PayData payData = (PayData) o;

        return getPayChannel() == payData.getPayChannel();

    }

    @Override
    public int hashCode() {
        return getPayChannel();
    }
}

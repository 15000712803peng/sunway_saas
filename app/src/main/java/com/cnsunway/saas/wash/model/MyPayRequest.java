package com.cnsunway.saas.wash.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LL on 2015/12/14.
 */
public class MyPayRequest {

    String orderNo;
    List<PayData> payList = new ArrayList<>();

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<PayData> getPayList() {
        return payList;
    }

    public void setPayList(List<PayData> payList) {
        this.payList = payList;
    }
}

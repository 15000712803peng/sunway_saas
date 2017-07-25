package com.cnsunway.wash.model;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class OrderMessage {
    String orderNo;
    String ticker;
    String text;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "orderNo='" + orderNo + '\'' +
                ", ticker='" + ticker + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

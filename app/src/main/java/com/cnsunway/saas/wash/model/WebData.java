package com.cnsunway.saas.wash.model;

/**
 * Created by LL on 2016/4/25.
 */
public class WebData {
    String type;
    String price;
    String callback;
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}

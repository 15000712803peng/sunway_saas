package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class ActivityMessage {

    int type;
    String ticker;
    String text;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //    String
}

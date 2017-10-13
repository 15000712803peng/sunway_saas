package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ConfirmReceive {
    String orderNo;
    int operScore;
    int washingScore;
    String comment;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOperScore() {
        return operScore;
    }

    public void setOperScore(int operScore) {
        this.operScore = operScore;
    }

    public int getWashingScore() {
        return washingScore;
    }

    public void setWashingScore(int washingScore) {
        this.washingScore = washingScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

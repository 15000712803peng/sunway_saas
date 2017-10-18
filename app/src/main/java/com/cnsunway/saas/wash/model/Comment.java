package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public class Comment {

    String comment;
    String createdDate;
    int fetcherId;
    String fetcherScore;
    String id;
    String orderId;
    String storeId;
    int storeScore;
    String updatedDate;
    String userId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getFetcherId() {
        return fetcherId;
    }

    public void setFetcherId(int fetcherId) {
        this.fetcherId = fetcherId;
    }

    public String getFetcherScore() {
        return fetcherScore;
    }

    public void setFetcherScore(String fetcherScore) {
        this.fetcherScore = fetcherScore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public int getStoreScore() {
        return storeScore;
    }

    public void setStoreScore(int storeScore) {
        this.storeScore = storeScore;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

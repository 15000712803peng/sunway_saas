package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2015/12/10.
 */
public class StoreBalance {
    String amount;
    String deposit ;
    String id ;
    String lockAmount ;
    String lockDeposit ;
    String storeId;
    String storeName ;
    String totalAmount ;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLockAmount() {
        return lockAmount;
    }

    public void setLockAmount(String lockAmount) {
        this.lockAmount = lockAmount;
    }

    public String getLockDeposit() {
        return lockDeposit;
    }

    public void setLockDeposit(String lockDeposit) {
        this.lockDeposit = lockDeposit;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId ;
}




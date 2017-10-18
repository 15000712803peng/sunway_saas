package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2015/12/10.
 */
public class Account {
    int couponCount;
    String balance;
    String createdBy;
    String deletedFlag;
    String deposit ;
    String id ;
    String lockAmount ;
    String lockDeposit ;
    String  offset;
    String orderBy ;
    String  page;
    String rows ;
    String storeId;
    String storeName ;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
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

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    String totalAmount ;
    String updatedBy ;
    String updatedDate ;
    String userId ;
    String version ;
    /* "couponCount": 0,
      "createdBy": "string",
      "createdDate": "2017-10-17T03:34:24.923Z",
      "deletedFlag": true,
      "deposit": 0,
      "id": 0,
      "lockAmount": 0,
      "lockDeposit": 0,
      "offset": 0,
      "orderBy": "string",
      "page": 0,
      "rows": 0,
      "storeId": 0,
      "storeName": "string",
      "totalAmount": 0,
      "updatedBy": "string",
      "updatedDate": "2017-10-17T03:34:24.923Z",
      "userId": 0,
      "version": 0
    }
  ], */


    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }


    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}

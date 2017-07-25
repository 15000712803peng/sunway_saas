package com.cnsunway.wash.model;

import java.io.Serializable;

/**
 * Created by peter on 15/11/19.
 */
public class Bag implements Serializable{

    public static final int STATUS_UNREADY = 1;
    public static final int STATUS_READY = 0;
    public static final int STATUS_WASHING = 10;
    public static final int STATUS_WASHDONE = 20;
    public static final int STATUS_WAITOUTSTORE = 30;
    public static final int STATUS_OUTSTORE = 40;
    public String getBagCode() {
        return bagCode;
    }

    public void setBagCode(String bagCode) {
        this.bagCode = bagCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    String bagCode;
    int id;
    String memo;
    int status;
}

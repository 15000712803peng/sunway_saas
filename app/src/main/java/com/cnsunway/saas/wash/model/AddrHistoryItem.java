package com.cnsunway.saas.wash.model;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class AddrHistoryItem {
    String addr;
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddrHistoryItem that = (AddrHistoryItem) o;

        return addr != null ? addr.equals(that.addr) : that.addr == null;

    }

    @Override
    public int hashCode() {
        return addr != null ? addr.hashCode() : 0;
    }

    public AddrHistoryItem(String addr) {
        this.addr = addr;
    }

    public AddrHistoryItem(){

    }
}

package com.cnsunway.wash.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class HistoryItems {
    List<AddrHistoryItem> itemList = new ArrayList<>();

    public List<AddrHistoryItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<AddrHistoryItem> itemList) {
        this.itemList = itemList;
    }
}

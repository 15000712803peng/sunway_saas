package com.cnsunway.saas.wash.model;

import java.util.List;

/**
 * Created by LL on 2016/3/27.
 */
public class DayBalanceDetail {
    String date;
    List<BalanceDetail> logs;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<BalanceDetail> getLogs() {
        return logs;
    }

    public void setLogs(List<BalanceDetail> logs) {
        this.logs = logs;
    }
}

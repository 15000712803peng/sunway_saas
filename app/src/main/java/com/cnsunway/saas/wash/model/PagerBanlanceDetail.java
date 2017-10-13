package com.cnsunway.saas.wash.model;

import java.util.List;

/**
 * Created by LL on 2016/3/27.
 */
public class PagerBanlanceDetail {

    int totalDays;
    int days;
    int start;
    List<DayBalanceDetail> dailyLogs;

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<DayBalanceDetail> getDailyLogs() {
        return dailyLogs;
    }

    public void setDailyLogs(List<DayBalanceDetail> dailyLogs) {
        this.dailyLogs = dailyLogs;
    }
}

package com.cnsunway.wash.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class HistoryCoupons {

    List<Coupon> results;
    Paginator paginator;

    public List<Coupon> getResults() {
        return results;
    }

    public void setResults(List<Coupon> results) {
        this.results = results;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }
}

package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.OrderSearchData;

/**
 * Created by peter on 16/3/24.
 */
public class SearchResp extends BaseResp {
    OrderSearchData data;

    public OrderSearchData getData() {
        return data;
    }

    public void setData(OrderSearchData data) {
        this.data = data;
    }

}

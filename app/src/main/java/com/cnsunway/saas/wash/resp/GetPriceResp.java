package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.GetPayData;

/**
 * Created by LL on 2015/12/11.
 */
public class GetPriceResp extends BaseResp {

    GetPayData data;

    public GetPayData getData() {
        return data;
    }

    public void setData(GetPayData data) {
        this.data = data;
    }
}

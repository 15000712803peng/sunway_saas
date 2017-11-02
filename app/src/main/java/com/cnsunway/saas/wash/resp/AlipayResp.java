package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.AliPayData;

/**
 * Created by LL on 2015/12/14.
 */
public class AlipayResp extends BaseResp {
    AliPayData data;

    public AliPayData getData() {
        return data;
    }

    public void setData(AliPayData data) {
        this.data = data;
    }
}

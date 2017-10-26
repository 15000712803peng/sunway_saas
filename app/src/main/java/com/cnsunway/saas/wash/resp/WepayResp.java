package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.WePayData;
import com.cnsunway.saas.wash.model.WepayConfig;

/**
 * Created by LL on 2015/12/14.
 */
public class WepayResp extends BaseResp{
    WePayData data;
    public WePayData getData() {
        return data;
    }

    public void setData(WePayData data) {
        this.data = data;
    }
}

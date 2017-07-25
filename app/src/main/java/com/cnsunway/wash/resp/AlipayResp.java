package com.cnsunway.wash.resp;

import com.cnsunway.wash.framework.net.BaseResp;

/**
 * Created by LL on 2015/12/14.
 */
public class AlipayResp extends BaseResp {
    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

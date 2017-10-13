package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.KeyInfo;

/**
 * Created by Administrator on 2017/5/25 0025.
 */

public class ShareKeyResp extends BaseResp{
    KeyInfo data;

    public KeyInfo getData() {
        return data;
    }

    public void setData(KeyInfo data) {
        this.data = data;
    }
}

package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.StoreDetail;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public class StoreDetailResp extends BaseResp{

    StoreDetail data;

    public StoreDetail getData() {
        return data;
    }

    public void setData(StoreDetail data) {
        this.data = data;
    }
}

package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.StoreLocation;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5 0005.
 */

public class AllStoresResp extends BaseResp{
    List<StoreLocation> data;

    public List<StoreLocation> getData() {
        return data;
    }

    public void setData(List<StoreLocation> data) {
        this.data = data;
    }
}

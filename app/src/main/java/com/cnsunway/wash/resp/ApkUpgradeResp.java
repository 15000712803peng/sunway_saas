package com.cnsunway.wash.resp;

import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.model.ApkUpdateData;

/**
 * Created by LL on 2015/12/4.
 */
public class ApkUpgradeResp extends BaseResp {
    ApkUpdateData data;
    public ApkUpdateData getData() {
        return data;
    }

    public void setData(ApkUpdateData data) {
        this.data = data;
    }
}

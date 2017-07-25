package com.cnsunway.wash.resp;

import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.model.ServiceCity;

import java.util.List;

/**
 * Created by LL on 2016/1/11.
 */
public class AllCityResp extends BaseResp {

    List<ServiceCity> data;

    public List<ServiceCity> getData() {
        return data;
    }

    public void setData(List<ServiceCity> data) {
        this.data = data;
    }
}

package com.cnsunway.wash.resp;

import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.model.Marketing;

import java.util.List;

/**
 * Created by LL on 2016/1/14.
 */
public class GetImagesResp extends BaseResp {
     List<Marketing> data;
    public List<Marketing> getData() {
        return data;
    }

    public void setData(List<Marketing> data) {
        this.data = data;
    }
}

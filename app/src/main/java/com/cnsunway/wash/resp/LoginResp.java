package com.cnsunway.wash.resp;

import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.model.User;

/**
 * Created by LL on 2015/12/4.
 */
public class LoginResp extends BaseResp {

    User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }


}

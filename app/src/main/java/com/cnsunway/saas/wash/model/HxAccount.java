package com.cnsunway.saas.wash.model;

import com.cnsunway.saas.wash.framework.net.BaseResp;

/**
 * Created by Administrator on 2017/8/14 0014.
 */

public class HxAccount extends BaseResp{
    String password;
    String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

package com.cnsunway.saas.wash.resp;


import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.UpdateUser;
import com.cnsunway.saas.wash.model.User;

public class UpdateUserResp extends BaseResp {
	
	User data;

	public User getData() {
		return data;
	}

	public void setData(User data) {
		this.data = data;
	}
}

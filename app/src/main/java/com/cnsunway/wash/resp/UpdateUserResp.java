package com.cnsunway.wash.resp;


import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.model.UpdateUser;
import com.cnsunway.wash.model.User;

public class UpdateUserResp extends BaseResp {
	
	User data;

	public User getData() {
		return data;
	}

	public void setData(User data) {
		this.data = data;
	}
}

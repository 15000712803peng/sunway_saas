package com.cnsunway.saas.wash.resp;


import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.UploadToken;

public class UpdateTokenResp extends BaseResp {
	
	UploadToken data;

	public UploadToken getData() {
		return data;
	}

	public void setData(UploadToken data) {
		this.data = data;
	}

	
	

}

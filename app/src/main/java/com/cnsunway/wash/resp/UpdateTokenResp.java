package com.cnsunway.wash.resp;


import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.model.UploadToken;

public class UpdateTokenResp extends BaseResp {
	
	UploadToken data;

	public UploadToken getData() {
		return data;
	}

	public void setData(UploadToken data) {
		this.data = data;
	}

	
	

}

package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.ApkUpdateData;


public class CheckUpgradeResp extends BaseResp {

	ApkUpdateData data;

	public ApkUpdateData getData() {
		return data;
	}

	public void setData(ApkUpdateData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CheckUpgradeRsp [data=" + data + "]";
	}

}

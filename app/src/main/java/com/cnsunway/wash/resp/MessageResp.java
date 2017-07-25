package com.cnsunway.wash.resp;

import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.model.AllMessage;
import com.cnsunway.wash.model.Paginator;

import java.util.List;

/**
 * Created by hp on 2017/6/23.
 */
public class MessageResp extends BaseResp {
    List<AllMessage> results;
    Paginator paginator;

    public List<AllMessage> getResults() {
        return results;
    }

    public void setResults(List<AllMessage> results) {
        this.results = results;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }
}

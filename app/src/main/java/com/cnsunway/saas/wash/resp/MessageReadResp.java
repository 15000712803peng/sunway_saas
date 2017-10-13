package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.model.Paginator;
import com.cnsunway.saas.wash.model.ReadMessage;

import java.util.List;

/**
 * Created by hp on 2017/6/27.
 */
public class MessageReadResp {
    List<ReadMessage> results;
    Paginator paginator;

    public List<ReadMessage> getResults() {
        return results;
    }

    public void setResults(List<ReadMessage> results) {
        this.results = results;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }
}

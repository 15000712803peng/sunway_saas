package com.cnsunway.saas.wash.util;

import android.content.Context;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class HistorySearchHelper {

    static  HistorySearchHelper instance;

    private HistorySearchHelper(Context context){

    }

    public HistorySearchHelper getInstance(Context context){
        if(instance == null){
            instance = new HistorySearchHelper(context);
        }
        return  instance;
    }
}

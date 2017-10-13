package com.cnsunway.saas.wash.framework.utils;

import java.util.List;

/**
 * Created by LL on 2015/11/16.
 */
public class ParamsParser {


    public static String parse(List<Object> params) {
        String endFix = "";
        if (params == null || params.size() == 0) {
            return endFix;
        }

        for(Object p: params){
            endFix += "/" + p;
        }

        return endFix;
    }
}

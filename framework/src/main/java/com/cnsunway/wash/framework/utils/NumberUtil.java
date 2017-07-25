package com.cnsunway.wash.framework.utils;

import java.text.DecimalFormat;

/**
 * Created by LL on 2016/3/2.
 */
public class NumberUtil {

    public static String formatNumber(String number){
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        if(Float.compare(Float.parseFloat(number),0.0f) == 0){
            number = "0.00";
        }else{
            number= decimalFormat.format(Float.parseFloat(number));
            if(number.startsWith(".")){
                number = "0" +number;
            }

        }

        return number;
    }
}

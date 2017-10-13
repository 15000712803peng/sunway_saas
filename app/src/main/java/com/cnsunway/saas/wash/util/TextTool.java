package com.cnsunway.saas.wash.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

/**
 * Created by Administrator on 2017/7/24 0024.
 */

public class TextTool {

    public static SpannableString changeNumberBig(String str){
        SpannableString spannableString = new SpannableString(str);
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                spannableString.setSpan(new AbsoluteSizeSpan(16,true), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                spannableString.setSpan(new AbsoluteSizeSpan(25,true), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    public static String addLetterSpace(String str){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < str.length(); i++){

            stringBuilder.append(str.charAt(i));
            if(i == str.length() - 1){
                continue;
            }
            if(Character.isDigit(str.charAt(i)) && Character.isDigit(str.charAt(i + 1))){
               continue;
            }
            stringBuilder.append(" ");

        }

        return stringBuilder.toString();
    }


}

package com.cnsunway.wash.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.cnsunway.wash.R;

/**
 * Created by hp on 2017/4/26.
 */

public class EditTextUtils {
    static IEditTextChangeListener mChangeListener;
    public static void setChangeListener(IEditTextChangeListener changeListener) {
        mChangeListener = changeListener;
    }
    public static class ButtonChangeListener {
        private Button button;
        private EditText[] editTexts;
        public ButtonChangeListener(Button button){
            this.button=button;
        }
        public ButtonChangeListener addAllEditText(EditText... editTexts){
            this.editTexts=editTexts;
            initEditListener();
            return this;
        }
        private void initEditListener() {
            for (EditText editText:editTexts){
                editText.addTextChangedListener(new TextChange());
            }
        }

        private class TextChange implements TextWatcher {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkAllEdit()){
                    mChangeListener.textChange(true);
                }else {
                    mChangeListener.textChange(false);
                }

                if(checkAllLengthEdit()){
                    mChangeListener.lengthChange(true);
                }else {
                    mChangeListener.lengthChange(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        }



        private boolean checkAllLengthEdit(){
            if(editTexts == null || editTexts.length < 2){
                return false;
            }
            if ((editTexts[0].getText().length()!=11)||(editTexts[1].getText().length()!=6)){
                return false;
            }
            return true;

        }
        private boolean checkAllEdit() {
          for (EditText editText:editTexts){
                if (!TextUtils.isEmpty(editText.getText() + "")){
                    continue;
                }else {
                    return false;
                }
            }
            return true;

        }
    }
}



package com.cnsunway.saas.wash.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.dialog.LoadingDialog;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.inter.LoadingDialogInterface;

/**
 * Created by LL on 2015/10/20.
 */
public abstract class InitActivity extends BaseActivity implements LoadingDialogInterface{

    String operationMsg;

    public String getOperationMsg() {
        return operationMsg;
    }

    public void setOperationMsg(String operationMsg) {
        this.operationMsg = operationMsg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        operationMsg = getString(R.string.loading);
        initData();
        initViews();
    }

    protected LoadingDialog loadingDialog;

    protected abstract  void initData();

    protected abstract void initViews();

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (isFinishing()) {
                return;
            }
            handlerMessage(msg);
        }
    };

    public Handler getHandler() {
        return handler;
    }

    protected abstract void handlerMessage(Message msg);

    protected void showLoadingDialog(String message) {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        loadingDialog = new LoadingDialog(this, message);
        loadingDialog.setCancelable(false);
        loadingDialog.show();

    }

    protected void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
    }

    public void showLoading(){
        showLoadingDialog(operationMsg);
    }
    public void  hideLoading(){
        hideLoadingDialog();
    }

    protected void showMessageToast(String message) {
        OperationToast.showOperationResult(getApplicationContext(), message, 0);
    }

    protected void showMessageToast(int message) {
        OperationToast.showOperationResult(getApplicationContext(), message);
    }

    protected void showImageToast(String message, int image) {
        OperationToast.showOperationResult(getApplicationContext(), message,
                image);
    }
}

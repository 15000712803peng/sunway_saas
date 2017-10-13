package com.cnsunway.saas.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;

/**
 * Created by Administrator on 2016/4/8.
 */
public class CouponExchangeDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;

    private Context context;
    LinearLayout container;
    Display display;
    Dialog dialog;
    View view;
    TextView titleText, contentText, cancelBtn;
    String title, content;

    public CouponExchangeDialog builder(String title, String content) {
        this.title = title;
        this.content = content;
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_cancel, null);
        container = (LinearLayout) view.findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);
        titleText = (TextView) container.findViewById(R.id.tv_dialog_title);
        contentText = (TextView) container.findViewById(R.id.tv_dialog_content);
        cancelBtn = (TextView) view.findViewById(R.id.btn_cancel);
        titleText.setText(title);
        if(!TextUtils.isEmpty(content)){
            contentText.setText(content);
        }else{
            contentText.setVisibility(View.GONE);
        }

        cancelBtn.setOnClickListener(this);
        dialog.setContentView(view);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        container.setLayoutParams(params);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == cancelBtn) {
            dialog.cancel();
        }
    }

    public CouponExchangeDialog(Context context) {
        this.context = context;

        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private LoadingDialog getLoadingDialog(String message) {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        loadingDialog = new LoadingDialog(context, message);
        return loadingDialog;

    }
}

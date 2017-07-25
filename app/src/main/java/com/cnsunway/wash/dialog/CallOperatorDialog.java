package com.cnsunway.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;

/**
 * Created by Administrator on 2016/4/8.
 */
public class CallOperatorDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;
    private Context context;
    LinearLayout container;
    Display display;
    Dialog dialog;
    View view;
    TextView title, content, okBtn, cancelBtn;
    String mobile;


    public CallOperatorDialog builder() {
        view = LayoutInflater.from(context).inflate(
                R.layout.dialog_cancel_ok, null);
        container = (LinearLayout) view.findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);

        title = (TextView) view.findViewById(R.id.tv_dialog_title);
        content = (TextView) view.findViewById(R.id.tv_dialog_content);
        okBtn = (TextView) view.findViewById(R.id.btn_ok);
        cancelBtn = (TextView) view.findViewById(R.id.btn_cancel);
        title.setText("拨打取送员");
        content.setText("确定拨打电话：" + mobile + " 吗？");
        okBtn.setText("确定");
        okBtn.setOnClickListener(this);
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
    public void onClick(View view) {
        if (view == okBtn) {
            dialog.cancel();
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
            context.startActivity(intent);

        } else if (view == cancelBtn) {
            cancel();
        }
    }

    public CallOperatorDialog(final Context context, String mobile) {
        this.mobile = mobile;
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

    public void cancel() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }
}

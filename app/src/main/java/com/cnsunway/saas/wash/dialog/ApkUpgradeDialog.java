package com.cnsunway.saas.wash.dialog;

import android.app.Dialog;
import android.content.Context;
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
import com.cnsunway.saas.wash.sharef.UserInfosPref;

/**
 * Created by Administrator on 2016/1/7.
 */
public class ApkUpgradeDialog implements View.OnClickListener {
    private Context context;
    LinearLayout container;
    Display display;
    Dialog dialog;
    TextView okBtn;
    TextView cancelBtn;
    TextView versionText, updateDetail;
    String appVersion, note;
    boolean forceUpgrade;

    private String apkUrl;

    OnUpgradeOkClickedListener okClickedListener;

    public void setOkClickedListener(
            OnUpgradeOkClickedListener okClickedListener) {
        this.okClickedListener = okClickedListener;
    }

    public interface OnUpgradeOkClickedListener {
        public void okClicked(String apkUrl);
    }

    public ApkUpgradeDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }


    public ApkUpgradeDialog builder(String appVersion, String note, boolean forceUpgrade) {
        this.appVersion = appVersion;
        this.note = note;
        this.forceUpgrade = forceUpgrade;
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_apk_update, null);
        container = (LinearLayout) view
                .findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);

        versionText = (TextView) view.findViewById(R.id.tv_new_version);
        versionText.setText("版本号" + appVersion);
        updateDetail = (TextView) view.findViewById(R.id.tv_update_detail);
        updateDetail.setText(note.replace("\\n", "\n"));

        okBtn = (TextView) view.findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(this);
        cancelBtn = (TextView) view.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);

        if (forceUpgrade) {
            cancelBtn.setVisibility(View.GONE);
            dialog.setCancelable(false);
        }

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
            if (okClickedListener != null) {
                okClickedListener.okClicked(apkUrl);
            }
            dialog.cancel();
        } else if (view == cancelBtn) {
            UserInfosPref.getInstance(context).setUpdateCheck(false);
            dialog.cancel();

        }

    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
}

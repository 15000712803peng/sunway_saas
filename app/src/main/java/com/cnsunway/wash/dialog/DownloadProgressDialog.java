package com.cnsunway.wash.dialog;

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
import android.widget.ProgressBar;

import com.cnsunway.wash.R;


public class DownloadProgressDialog {

	private Context context;
	Display display;
	LinearLayout container;
	ProgressBar progressBar;
	Dialog dialog;

	public DownloadProgressDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public DownloadProgressDialog builder() {

		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_apk_download_progress, null);

		dialog = new Dialog(context, R.style.CustomDialog2);
		container = (LinearLayout) view
				.findViewById(R.id.ll_download_dialog_container);
		progressBar = (ProgressBar) view
				.findViewById(R.id.pb_download_progress);
		dialog.setCancelable(false);

		dialog.setContentView(view);

		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		container.setLayoutParams(params);
		dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
		return this;
	}

	public void show() {
		if (dialog != null && !dialog.isShowing()) {
			dialog.show();
		}
	}

	public void cancel() {
		if (dialog != null) {
			dialog.cancel();
		}
	}

}

package com.cnsunway.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.cnsunway.wash.R;
import com.cnsunway.wash.model.ShareInfo;
import com.cnsunway.wash.util.WeShare;

public class WayOfShareDialog implements OnClickListener {
	private Context context;
	Display display;
	Dialog dialog;
	TextView cancelText;
	ShareInfo shareInfo;
	String key;

	public WayOfShareDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();


	}

	public ShareInfo getShareInfo() {
		return shareInfo;
	}

	public void setShareInfo(ShareInfo shareInfo) {
		this.shareInfo = shareInfo;
	}

	public void setShareInfo(ShareInfo shareInfo,String key) {
		this.shareInfo = shareInfo;
		this.key = key;
	}

	RelativeLayout container;
//	ImageView cancelImage;
	RelativeLayout shareFriendParent;
	RelativeLayout shareSessionParent;

	public WayOfShareDialog builder() {

		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_way_of_share2, null);
		container = (RelativeLayout) view.findViewById(R.id.rl_dialog_container);
//		cancelImage = (ImageView) view.findViewById(R.id.image_dialog_cancel);
//		cancelImage.setOnClickListener(this);
		dialog = new Dialog(context, R.style.CustomDialog3);
		shareFriendParent = (RelativeLayout) view.findViewById(R.id.share_friend_parent);
		shareFriendParent.setOnClickListener(this);
		shareSessionParent = (RelativeLayout) view.findViewById(R.id.share_session_parent);
		shareSessionParent.setOnClickListener(this);
		dialog.setContentView(view);
//        dialog.setCancelable(false);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
				.getWidth()), ViewGroup.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM;
		container.setLayoutParams(params);
		dialog.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		return this;
	}


	@Override
	public void onClick(View view) {
		if(view == shareFriendParent){
			new WeShare(context,shareInfo).share(1,key);
		}else if(view == shareSessionParent){
			new WeShare(context,shareInfo).share(2,key);
		}else if(view == cancelText){
			cancel();
		}
		cancel();
	}

	public void show() {
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	public void cancel(){
		if (dialog.isShowing()) {
			dialog.cancel();
		}
	}



	protected void showMessageToast(String message) {
		OperationToast.showOperationResult(context.getApplicationContext(),
				message, 0);
	}

	protected void showImageToast(String message, int image) {
		OperationToast.showOperationResult(context.getApplicationContext(),
				message, image);
	}
}

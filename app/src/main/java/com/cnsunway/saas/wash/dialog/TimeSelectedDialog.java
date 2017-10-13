package com.cnsunway.saas.wash.dialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.framework.utils.DateUtil;
import com.cnsunway.saas.wash.view.wheelview.ArrayWheelAdapter;
import com.cnsunway.saas.wash.view.wheelview.OnWheelChangedListener;
import com.cnsunway.saas.wash.view.wheelview.WheelView;

import java.util.Arrays;


public class TimeSelectedDialog {
	private LoadingDialog loadingDialog;
	private Context context;
	LinearLayout container;
	Display display;
	Dialog dialog;
	WheelView dateWheel;
	WheelView timewheel;
	OnWheelChangedListener listener;
	private SelectedTimeOkListener okListener;
    TextView okText;
	public void setOkListener(SelectedTimeOkListener okListener) {
		this.okListener = okListener;
	}

	public interface  SelectedTimeOkListener{
		public void selectedTimeOk(String time);
	}


	public TimeSelectedDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();


	}



	String[] hours;
	String[] dates = new String[7];
	String date;
	String hour;
	public TimeSelectedDialog builder() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.bottom_date_picker, null);
		container = (LinearLayout) view.findViewById(R.id.rl_dialog_container);
		dialog = new Dialog(context, R.style.CustomDialog);
		dateWheel = (WheelView) view.findViewById(R.id.date_wheel);
		timewheel = (WheelView) view.findViewById(R.id.time_wheel);
		dialog.setContentView(view);
		dialog.setCancelable(false);
        okText = (TextView) view.findViewById(R.id.btn_date_ok);
		okText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (okListener != null) {
					okListener.selectedTimeOk(date + "&" + hour);
					cancel();
				}
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
		hours = context.getResources().getStringArray(R.array.hoursnew);
//		dates = context.getResources().getStringArray(R.array.dates);
		dates[0] =  DateUtil.getTodayDate();
		dates[1] = DateUtil.getTomorrowDate();
		dates[2] = DateUtil.getOneAfterTomorrowDate();
		dates[3] = DateUtil.getFourDate();
		dates[4] = DateUtil.getFiveDate();
		dates[5] = DateUtil.getSixDate();
		dates[6] = DateUtil.getSevenDate();
		date = dates[0];
//		generateHours();
		generateHours();
		timewheel.setViewAdapter(new DatasAdapter(context, hours, -1));
		dateWheel.setViewAdapter(new DatasAdapter(context, dates, -1));
		listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (wheel == dateWheel) {
					date = dates[newValue];
					if (date.equals(dates[0])) {
						generateHours();
						timewheel.setViewAdapter(new DatasAdapter(
								context, hours, -1));
						timewheel.setCurrentItem(0);
					} else if (date.equals(dates[1])) {
						hours = context.getResources().getStringArray(R.array.hoursnew);
						timewheel.setViewAdapter(new DatasAdapter(
								context, hours, -1));
						timewheel.setCurrentItem(0);
					} else if (date.equals(dates[2])) {
						hours = context.getResources().getStringArray(R.array.hoursnew);
						timewheel.setViewAdapter(new DatasAdapter(
								context, hours, -1));
						timewheel.setCurrentItem(0);
					}else if (date.equals(dates[3])) {
						hours = context.getResources().getStringArray(R.array.hoursnew);
						timewheel.setViewAdapter(new DatasAdapter(
								context, hours, -1));
						timewheel.setCurrentItem(0);
					}else if (date.equals(dates[4])) {
						hours = context.getResources().getStringArray(R.array.hoursnew);
						timewheel.setViewAdapter(new DatasAdapter(
								context, hours, -1));
						timewheel.setCurrentItem(0);
					}else if (date.equals(dates[5])) {
						hours = context.getResources().getStringArray(R.array.hoursnew);
						timewheel.setViewAdapter(new DatasAdapter(
								context, hours, -1));
						timewheel.setCurrentItem(0);
					}else if (date.equals(dates[6])) {
						hours = context.getResources().getStringArray(R.array.hoursnew);
						timewheel.setViewAdapter(new DatasAdapter(
								context, hours, -1));
						timewheel.setCurrentItem(0);
					}
				} else if (wheel == timewheel) {
					hour = hours[newValue];
				}
			}
		};
		timewheel.addChangingListener(listener);
		dateWheel.addChangingListener(listener);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
				.getWidth()), LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM;
		container.setLayoutParams(params);
		dialog.getWindow().setGravity(Gravity.BOTTOM);
		return this;
	}


//	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
//	private void generateHours(){
//		int calHour = DateUtil.getCurrentHourAfterHalf();
//		Log.e("------------","cal hour:" + calHour);
//		hours = context.getResources().getStringArray(R.array.hoursnew);
//		if (calHour == 9) {
//			hours = Arrays.copyOfRange(hours, 1, hours.length);
//		}else if (calHour == 10) {
//			hours = Arrays.copyOfRange(hours, 2, hours.length);
//		} else if (calHour == 11) {
//			hours = Arrays.copyOfRange(hours, 3, hours.length);
//		} else if (calHour == 12) {
//			hours = Arrays.copyOfRange(hours, 4, hours.length);
//		} else if (calHour == 13) {
//			hours = Arrays.copyOfRange(hours, 5, hours.length);
//		} else if (calHour == 14) {
//			hours = Arrays.copyOfRange(hours, 6, hours.length);
//		} else if (calHour == 15) {
//			hours = Arrays.copyOfRange(hours, 7, hours.length);
//		} else if (calHour == 16) {
//			hours = Arrays.copyOfRange(hours, 8, hours.length);
//		} else if (calHour == 17) {
//			hours = Arrays.copyOfRange(hours, 9, hours.length);
//		} else if (calHour == 18) {
//			hours = Arrays.copyOfRange(hours, 10, hours.length);
//		} else if (calHour == 19) {
//			hours = Arrays.copyOfRange(hours, 11, hours.length);
//		} else if (calHour >= 20) {
//			hours = new String[1];
//			hours[0] = "";
//		}
//		hour = hours[0];
//		Log.e("--------------------","hour:" + hour);
//
//			if(TextUtils.isEmpty(hour)){
//				dates = new String[6];
//				hours = context.getResources().getStringArray(R.array.hoursnew);
//				hour = hours[0];
//				dates[0] = DateUtil.getTomorrowDate();
//				dates[1] = DateUtil.getOneAfterTomorrowDate();
//				dates[2] = DateUtil.getFourDate();
//				dates[3] = DateUtil.getFiveDate();
//				dates[4] = DateUtil.getSixDate();
//				dates[5] = DateUtil.getSevenDate();
//				date = dates[0];
//			}
//	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void generateHours() {
		int calHour = DateUtil.getCurrentHourAfterHalf();
		hours = context.getResources().getStringArray(R.array.hoursnew);
		if (calHour == 9) {
			hours = Arrays.copyOfRange(hours, 1, hours.length);
		} else if (calHour == 10) {
			hours = Arrays.copyOfRange(hours, 2, hours.length);
		} else if (calHour == 11) {
			hours = Arrays.copyOfRange(hours, 3, hours.length);
		} else if (calHour == 12) {
			hours = Arrays.copyOfRange(hours, 4, hours.length);
		} else if (calHour == 13) {
			hours = Arrays.copyOfRange(hours, 5, hours.length);
		} else if (calHour == 14) {
			hours = Arrays.copyOfRange(hours, 6, hours.length);
		} else if (calHour == 15) {
			hours = Arrays.copyOfRange(hours, 7, hours.length);
		} else if (calHour == 16) {
			hours = Arrays.copyOfRange(hours, 8, hours.length);
		} else if (calHour == 17) {
			hours = Arrays.copyOfRange(hours, 9, hours.length);
		} else if (calHour == 18) {
			hours = Arrays.copyOfRange(hours, 10, hours.length);
		} else if (calHour == 19 || calHour == 0) {
			hours = new String[]{""};
		}
		hour = hours[0];
		if (TextUtils.isEmpty(hour)) {
			date = dates[1];
			hours = context.getResources().getStringArray(R.array.hoursnew);
			hour = hours[0];
		}

	}

	class DatasAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		public DatasAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(18);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFFFFFF);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
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

	private LoadingDialog getLoadingDialog(String message) {

		if (loadingDialog != null) {
			loadingDialog.cancel();
		}
		loadingDialog = new LoadingDialog(context, message);
        return  loadingDialog;

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

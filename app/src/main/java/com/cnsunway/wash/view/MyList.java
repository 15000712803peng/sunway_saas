package com.cnsunway.wash.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyList extends ListView{


	public MyList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyList(Context context) {
		super(context);
	}

	public MyList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// Log.d(TAG, "on touch event");
		return super.onTouchEvent(ev);
	}
}

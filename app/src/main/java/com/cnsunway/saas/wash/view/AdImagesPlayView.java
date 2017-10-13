package com.cnsunway.saas.wash.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AdImagesPlayView extends LinearLayout {
	private Context mContext;
	private DrInnerViewPager mViewPager;
	private LayoutParams layoutParamsFF;
	private LayoutParams layoutParamsFW;
	private LayoutParams pageLineLayoutParams;
	private LinearLayout mPageLineLayoutParent;
	private LinearLayout pageLineLayout;
	private MyViewPagerAdapter myPagerAdapter;
	private Bitmap displayImage, hideImage;
	ArrayList<View> mViews;
	private int count, position;
	public interface OnStayPages{
		public void stayPage(int index);
	}

	OnStayPages stayPages;

	public void setStayPages(OnStayPages stayPages) {
		this.stayPages = stayPages;
	}

	public AdImagesPlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public AdImagesPlayView(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		mContext = context;
		displayImage = getBitmapFormSrc("dvh.png");
		hideImage = getBitmapFormSrc("dvi.png");
		layoutParamsFF = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParamsFW = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		pageLineLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setOrientation(LinearLayout.VERTICAL);
//		this.setBackgroundColor(Color.rgb(255, 255, 255));

		//---
		LinearLayout mRelativeLayout = new LinearLayout(context);
		mRelativeLayout.setOrientation(LinearLayout.VERTICAL);

		mViewPager = new DrInnerViewPager(context);

//		mViewPager.setId(ID);

		mPageLineLayoutParent = new LinearLayout(context);
		mPageLineLayoutParent.setPadding(0, 5, 0, 5);
		mPageLineLayoutParent.setOrientation(LinearLayout.HORIZONTAL);
		pageLineLayout = new LinearLayout(context);
		pageLineLayout.setPadding(15, 1, 15, 1);
		pageLineLayout.setOrientation(LinearLayout.HORIZONTAL);
//		pageLineLayout.setVisibility(View.INVISIBLE);
		mPageLineLayoutParent.addView(pageLineLayout,
				new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));




		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				0);
		lp1.weight = 1;
		mRelativeLayout.addView(mViewPager, lp1);

		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp2.gravity = Gravity.CENTER_HORIZONTAL;
		mRelativeLayout.addView(mPageLineLayoutParent, lp2);


		addView(mRelativeLayout, layoutParamsFW);



		mViews = new ArrayList<View>();
		myPagerAdapter = new MyViewPagerAdapter(context, mViews);
		mViewPager.setAdapter(myPagerAdapter);
		mViewPager.setFadingEdgeLength(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				makesurePosition();
				if(stayPages != null){
					stayPages.stayPage(position);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
									   int positionOffsetPixels) {
			}

		});

	}

	public void addViews(List<View> views) {
		mViews.clear();
		mViews.addAll(views);
		myPagerAdapter.notifyDataSetChanged();
		creatIndex();
	}

	private void creatIndex() {
		if (mViews.size() == 0 || mViews.size() == 1) {
			return;
		}
		pageLineLayout.removeAllViews();
		mPageLineLayoutParent.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		pageLineLayout.setGravity(Gravity.CENTER);
		count = mViews.size();
		for (int j = 0; j < count; j++) {
			ImageView imageView = new ImageView(mContext);
			pageLineLayoutParams.setMargins(6, 6, 6, 6);
			imageView.setLayoutParams(pageLineLayoutParams);
			if (j == 0) {
				imageView.setImageBitmap(displayImage);
			} else {
				imageView.setImageBitmap(hideImage);
			}
			pageLineLayout.addView(imageView, j);
		}
	}

	private void makesurePosition() {
		position = mViewPager.getCurrentItem();
		for (int j = 0; j < count; j++) {
			if (position == j) {
				((ImageView) pageLineLayout.getChildAt(position))
						.setImageBitmap(displayImage);
			} else {
				((ImageView) pageLineLayout.getChildAt(j))
						.setImageBitmap(hideImage);
			}
		}
	}

	private Bitmap getBitmapFormSrc(String name) {
		Bitmap bitmap = null;

		try {
			InputStream is = getResources().getAssets().open(name);
			bitmap = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
		}
		return bitmap;
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private ArrayList<View> mListViews = null;

		public MyViewPagerAdapter(Context context, ArrayList<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			View v = mListViews.get(position);
			((ViewPager) container).addView(v);
			return v;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

	}

	public void recycle(){
//		for(int i = 0 ; i < mViewPager.getAdapter().getCount();++i){
//
//		}
//		mViewPager.getAdapter().getCount();
//		mViewPager.setCurrentItem();
		handler.sendEmptyMessageDelayed(MSG_MOVE_NEXST,2000);

	}

	final int MSG_MOVE_NEXST = 1;

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == MSG_MOVE_NEXST){
				moveNext();
				sendEmptyMessageDelayed(MSG_MOVE_NEXST,2000);
			}
		}
	};

	public void remoevRecycle(){
		handler.removeMessages(MSG_MOVE_NEXST);
	}

	private void moveNext(){
		int position = mViewPager.getCurrentItem();
		if(position == mViewPager.getChildCount() - 1){
			mViewPager.setCurrentItem(0,false);

		}else {
			mViewPager.setCurrentItem(position + 1 );
		}
	}

}

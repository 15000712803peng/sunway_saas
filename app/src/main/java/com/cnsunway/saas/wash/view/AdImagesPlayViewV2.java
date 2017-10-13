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
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cnsunway.saas.wash.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.util.Log.e;

public class AdImagesPlayViewV2 extends LinearLayout {
	private Context mContext;
	private DrInnerViewPager mViewPager;
	private LayoutParams layoutParamsFF;
	private LayoutParams layoutParamsFW;
	private LayoutParams pageLineLayoutParams;
	private LinearLayout mPageLineLayoutParent;
	private LinearLayout pageLineLayout;
	private MyViewPagerAdapter myPagerAdapter;
//	private ImageAdapter myPagerAdapter;
	private int displayImage, hideImage;
	ArrayList<View> mViews;
	private int count, position;
	public interface OnStayPages{
		public void stayPage(int index);
	}

	public interface ItemClickedListener{
		void itemClicked(int position);
	}

	ItemClickedListener itemClickedListener;



	public void setItemClickedListener(ItemClickedListener itemClickedListener) {
		this.itemClickedListener = itemClickedListener;
	}

	OnStayPages stayPages;

	public void setStayPages(OnStayPages stayPages) {
		this.stayPages = stayPages;
	}

	public AdImagesPlayViewV2(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public AdImagesPlayViewV2(Context context) {
		super(context);
		initViews(context);
	}

	int startX = 0;
	int startY = 0;
	int endX = 0;
	int endY = 0;
	boolean moveNext = false;


	private void initViews(Context context) {
		mContext = context;
//		displayImage = R.drawable.white_dot;
//		hideImage = R.drawable.black_dot;
		displayImage = R.drawable.black_dot;
		hideImage = R.drawable.white_dot;
		layoutParamsFF = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParamsFW = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		pageLineLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setOrientation(LinearLayout.VERTICAL);
//		this.setBackgroundColor(Color.rgb(255, 255, 255));

		//---
		RelativeLayout mRelativeLayout = new RelativeLayout(context);
//		mRelativeLayout.setOrientation(LinearLayout.VERTICAL);


		mViewPager = new DrInnerViewPager(context);

		mViewPager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						startX = (int) event.getX();
						startY = (int) event.getY();
						break;
					case MotionEvent.ACTION_MOVE:
						break;
					case MotionEvent.ACTION_UP:
						endX = (int) event.getX();
						endY = (int) event.getY();
						if (Math.abs(endX - startX) < 30 && Math.abs(endY - startY) < 30) {
							if(itemClickedListener != null){
								if(mViewPager.getCurrentItem() >= 0){
									itemClickedListener.itemClicked(mViewPager.getCurrentItem());
								}
							}
						}
						break;
				}
				return false;
			}
		});

//		mViewPager.setId(ID);
		mPageLineLayoutParent = new LinearLayout(context);
		mPageLineLayoutParent.setPadding(0, 5, 0, 5);
		mPageLineLayoutParent.setOrientation(LinearLayout.HORIZONTAL);
		pageLineLayout = new LinearLayout(context);
		pageLineLayout.setPadding(15, 1, 15, 1);
		pageLineLayout.setOrientation(LinearLayout.HORIZONTAL);
//		pageLineLayout.setVisibility(View.INVISIBLE);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.dot_margin_bottom);
		mPageLineLayoutParent.addView(pageLineLayout,
				params);

		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams
				(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);;
		mRelativeLayout.addView(mViewPager, lp1);

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams
				(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		lp2.gravity = Gravity.CENTER_HORIZONTAL;

		lp2.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);

		mRelativeLayout.addView(mPageLineLayoutParent, lp2);

		addView(mRelativeLayout, layoutParamsFW);



		mViews = new ArrayList<View>();
		myPagerAdapter = new MyViewPagerAdapter(context, mViews);
//		myPagerAdapter = new ImageAdapter(mViews);
		mViewPager.setAdapter(myPagerAdapter);
		mViewPager.setFadingEdgeLength(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
//				Log.e("view pager","onPageSelected:"+ position);
				makesurePosition();
				if(stayPages != null){
					stayPages.stayPage(position);
				}
			}
			@Override
			public void onPageScrollStateChanged(int state) {
//				Log.e("view pager","onPageScrollStateChanged:"+ state);
				if (state == 1){
					removeRecycle();
					moveNext = true;
				}else if (state ==0){
					recycle();
					moveNext = false;
				}else if(state == 2){
					moveNext = false;
				}

			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
									   int positionOffsetPixels) {
//				Log.e("view pager","onPageScrolled"+ position);
				if(position == mViewPager.getAdapter().getCount() - 1){
					if(moveNext){

						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								mViewPager.setCurrentItem(0,false);
							}
						},100);
					}
					moveNext = false;

				}

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
				imageView.setImageResource(displayImage);
			} else {
				imageView.setImageResource(hideImage);
			}
			pageLineLayout.addView(imageView, j);
		}
	}

	private void makesurePosition() {
		position = mViewPager.getCurrentItem();
		Log.e("---------","position:" + position);
		for (int j = 0; j < count; j++) {
			if (position == j) {
				((ImageView) pageLineLayout.getChildAt(position))
						.setImageResource(displayImage);
			} else {
				((ImageView) pageLineLayout.getChildAt(j))
						.setImageResource(hideImage);
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



//	private class ImageAdapter extends PagerAdapter{
//
//		ArrayList<View> mListViews;
//
//		public ImageAdapter(ArrayList<View> mListViews) {
//			this.mListViews = mListViews;
//		}
//
//		@Override
//		public int getCount() {
//			//设置成最大，使用户看不到边界
//			return Integer.MAX_VALUE;
//		}
//
//		@Override
//		public boolean isViewFromObject(View arg0, Object arg1) {
//			return arg0==arg1;
//		}
//		@Override
//		public void destroyItem(ViewGroup container, int position,
//								Object object) {
//			//Warning：不要在这里调用removeView
//			((ViewPager) container).removeView((View) object);
//		}
//		@Override
//		public Object instantiateItem(ViewGroup container, int position) {
//			//对ViewPager页号求模取出View列表中要显示的项
//			position %= mListViews.size();
//			if (position<0){
//				position = mListViews.size()+position;
//			}
//			View  view = mListViews.get(position);
//			//如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
//			ViewParent vp =view.getParent();
//			if (vp!=null){
//				ViewGroup parent = (ViewGroup)vp;
//				parent.removeView(view);
//			}
//			container.addView(view);
//			//add listeners here if necessary
//			return view;
//		}
//	}

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
		removeRecycle();
		handler.sendEmptyMessageDelayed(MSG_MOVE_NEXST,4000);

	}

	final int MSG_MOVE_NEXST = 1;

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == MSG_MOVE_NEXST){
				moveNext();
				sendEmptyMessageDelayed(MSG_MOVE_NEXST,4000);
			}
		}
	};

	public void removeRecycle(){
		handler.removeMessages(MSG_MOVE_NEXST);
	}

	private void moveNext(){

		int position = mViewPager.getCurrentItem();
		if(position == mViewPager.getAdapter().getCount() - 1){
			mViewPager.setCurrentItem(0,false);

		}else {
			mViewPager.setCurrentItem(position + 1);
		}
	}

}

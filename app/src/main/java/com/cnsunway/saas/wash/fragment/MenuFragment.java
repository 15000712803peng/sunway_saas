package com.cnsunway.saas.wash.fragment;

public abstract class MenuFragment extends BaseFragment{
	
	public interface OpenLeftMenuListener{
		void openLeftMenu();
	};
	
	public interface OpenRightMenuListener{
		void openRightMenu();
	}

	OpenLeftMenuListener openLeftMenuListener;
	
	OpenRightMenuListener openRightMenuListener;

	public OpenLeftMenuListener getOpenLeftMenuListener() {
		return openLeftMenuListener;
	}

	public void setOpenLeftMenuListener(OpenLeftMenuListener openLeftMenuListener) {
		this.openLeftMenuListener = openLeftMenuListener;
	}

	public OpenRightMenuListener getOpenRightMenuListener() {
		return openRightMenuListener;
	}

	public void setOpenRightMenuListener(OpenRightMenuListener openRightMenuListener) {
		this.openRightMenuListener = openRightMenuListener;
	}
	
	
	
	

}

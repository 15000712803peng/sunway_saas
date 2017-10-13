//package com.cnsunway.saas.wash.activity;
//
//import android.os.Bundle;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.util.TypedValue;
//
//import com.cnsunway.saas.wash.fragment.LeftMenuFragment;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
//import com.cnsunway.saas.wash.R;
//
//
///**
// * Created by LL on 2016/1/5.
// */
//public abstract class MenuActivity extends SlidingFragmentActivity implements LeftMenuFragment.OnLeftMenuItemClickedListener {
//
//    private LeftMenuFragment leftMenu;
//    FragmentManager fragmentMgr;
//
//    public interface OpenLeftMenuLinstenr {
//        void openLeftMenu();
//    }
//
//
//    public LeftMenuFragment getLeftMenu() {
//        return leftMenu;
//    }
//
//    public void setLeftMenu(LeftMenuFragment leftMenu) {
//
//        this.leftMenu = leftMenu;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initMenu(savedInstanceState);
//    }
//
//    private void initMenu(Bundle savedInstanceState) {
//        fragmentMgr = getSupportFragmentManager();
//        //SlidingMenu
//        SlidingMenu menu = getSlidingMenu();
//        menu.setMode(SlidingMenu.LEFT);
//        menu.setBehindOffset(dp2px(90));
//        menu.setFadeEnabled(true);
//        menu.setBehindScrollScale(0.35f);
//        menu.setFadeDegree(1.0f);
//        setBehindContentView(R.layout.frame_left_menu);
//        if (savedInstanceState == null) {
//            FragmentTransaction ft = fragmentMgr.beginTransaction();
//            leftMenu = new LeftMenuFragment();
//            leftMenu.setMenuItemClickedListener(this);
//            menu.setOnOpenedListener(leftMenu);
//            ft.replace(R.id.left_menu_frame, leftMenu);
//            ft.commit();
//        } else {
//            leftMenu = (LeftMenuFragment) fragmentMgr
//                    .findFragmentById(R.id.left_menu_frame);
//        }
//    }
//
//    protected void toggleLeftMenu() {
//        getSlidingMenu().showMenu();
//    }
//
//    private int dp2px(int dp) {
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
//                getResources().getDisplayMetrics());
//    }
//
//    protected void menuClosed() {
//
//        getSlidingMenu().toggle();
//    }
//
//}

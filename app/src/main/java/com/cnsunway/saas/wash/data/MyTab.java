package com.cnsunway.saas.wash.data;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.fragment.HomeFragment3;
import com.cnsunway.saas.wash.fragment.MineFragment;
import com.cnsunway.saas.wash.fragment.OrderFragment;


public enum  MyTab {

    /*Map(0, R.string.map, R.drawable.main_tab_map,
            OderMapFragment.class),*/

    HOME(1, R.string.home, R.drawable.tab_home,
            HomeFragment3.class),

    ORDER(2, R.string.order, R.drawable.tab_order,
            OrderFragment.class),

    MINE(3, R.string.mine, R.drawable.tab_mine,
            MineFragment.class);

    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    private MyTab(int idx, int resName, int resIcon, Class<?> clz) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}

package com.jmarkstar.mfc.module.gallery;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.List;

/**
 * Created by jmarkstar on 30/01/2017.
 */
class ItemTypeVpAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;

    ItemTypeVpAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override public void restoreState(Parcelable state, ClassLoader loader) {}

    @Override public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override public int getCount() {
        if(mFragments == null) return 0;
        else return mFragments.size();
    }
}

package com.app.fit;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class FragmentAdapter extends FragmentPagerAdapter {


    // add provider also


    private int values[] = {0, 1, 2, 3};
    private String[] titles = {"1", "2", "3", "4"};


    public Fragment getNextFragment(int i){
        Fragment[] fragmentList = {
                EnterFragment.newInstance(values[i]),
                FingerprintFragment.newInstance(values[i]),
                SelectPaymentFragment.newInstance(values[i]),
                ConfirmationFragment.newInstance(values[i]),
        };
       return fragmentList[i];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    private int pagerCount = 4;
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }


    // this is where is the fragment is created, just pass in a parameter to create fragment
    @Override
    public Fragment getItem(int i) {
        return getNextFragment(values[i]);
    }


    @Override
    public int getCount() {
        return pagerCount;
    }

//    @Override
//    public int getPageIconResId(int position) {
//        return tabIcons[position];
//    }



}




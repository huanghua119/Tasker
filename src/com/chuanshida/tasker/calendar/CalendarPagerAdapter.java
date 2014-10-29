package com.chuanshida.tasker.calendar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

    public CalendarPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CalendarPagerFragment.create(position);
    }

    @Override
    public int getCount() {
        int years = LunarCalendar.getMaxYear() - LunarCalendar.getMinYear();
        return years * 12;
    }

}

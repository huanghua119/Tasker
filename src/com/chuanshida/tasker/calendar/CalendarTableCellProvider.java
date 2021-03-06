package com.chuanshida.tasker.calendar;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.chuanshida.tasker.R;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarTableCellProvider {

    private long firstDayMillis = 0;
    private int solarTerm1 = 0;
    private int solarTerm2 = 0;
    private DateFormatter fomatter;
    private Calendar mDate;
    private int[] MONTH_LENGTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30,
            31 };
    private int[] MONTH_LENGTH2 = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30,
            31 };

    public CalendarTableCellProvider(Resources resources, int monthIndex) {
        int year = LunarCalendar.getMinYear() + (monthIndex / 12);
        int month = monthIndex % 12;
        mDate = new GregorianCalendar(year, month, 1);
        int offset = 1 - mDate.get(Calendar.DAY_OF_WEEK);
        mDate.add(Calendar.DAY_OF_MONTH, offset);
        firstDayMillis = mDate.getTimeInMillis();
        solarTerm1 = LunarCalendar.getSolarTerm(year, month * 2 + 1);
        solarTerm2 = LunarCalendar.getSolarTerm(year, month * 2 + 2);
        fomatter = new DateFormatter(resources);
    }

    public int getMonthRow() {
        LunarCalendar date = new LunarCalendar(firstDayMillis
                + (0 - LunarCalendar.WEEK_OFFSET) * LunarCalendar.DAY_MILLIS);
        int gregorianDay = date.getGregorianDate(Calendar.DAY_OF_MONTH);
        
        
        int year = mDate.get(Calendar.YEAR);
        int month = mDate.get(Calendar.MONTH) - 1;
        int mPreMonth = month - 1;
        if (month == -1) {
            mPreMonth = 11;
        }
        
        int length = MONTH_LENGTH2[mDate.get(Calendar.MONTH)];
        int offset = MONTH_LENGTH2[mPreMonth] - gregorianDay;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            length = MONTH_LENGTH[month];
            offset = MONTH_LENGTH[mPreMonth] - gregorianDay;
        }
        if (gregorianDay == 1) {
            offset = 0;
        } else {
            offset = offset + 1;
        }
        int rom = 0;
        for (int position = 0 + offset; position < length; position++) {
            if (position % 7 == 0) {
                  rom++;
            }
        }
        return rom;
    }

    public View getView(int position, LayoutInflater inflater,
            ViewGroup container) {
        ViewGroup rootView;
        LunarCalendar date = new LunarCalendar(firstDayMillis
                + (position - (position / 8) - LunarCalendar.WEEK_OFFSET) * LunarCalendar.DAY_MILLIS);
        if (position % 8 == 0) {
            return null;
        }

        // 开始日期处理
        boolean isFestival = false, isSolarTerm = false;
        rootView = (ViewGroup) inflater.inflate(
                R.layout.view_calendar_day_cell, container, false);
        TextView txtCellGregorian = (TextView) rootView
                .findViewById(R.id.txtCellGregorian);
        ImageView imgCellHint = (ImageView) rootView
                .findViewById(R.id.imgCellHint);

        int gregorianDay = date.getGregorianDate(Calendar.DAY_OF_MONTH);
        // 判断是否为本月日期
        boolean isOutOfRange = ((position % 8 != 0)
                && (position < 8 && gregorianDay > 7) || (position > 8 && gregorianDay < position - 7 - 6));
        if (!isOutOfRange) {
            txtCellGregorian.setText(String.valueOf(gregorianDay));
            rootView.setClickable(true);
        } else {
            txtCellGregorian.setText("");
            rootView.setClickable(false);
            rootView.setBackgroundResource(0);
        }
        if (LunarCalendar.DATE_SELECT_OFFSET != 0) {
            int offset = (date.getGregorianDate(Calendar.YEAR) - LunarCalendar.getMinYear())
                    * 12 + date.getGregorianDate(Calendar.MONTH) + gregorianDay;
            if (offset == LunarCalendar.DATE_SELECT_OFFSET) {
                rootView.setFocusable(true);
            }
        }

        /*
        // 农历节日 > 公历节日 > 农历月份 > 二十四节气 > 农历日
        int index = date.getLunarFestival();
        if (index >= 0) {
            // 农历节日
            txtCellLunar.setText(fomatter.getLunarFestivalName(index));
            isFestival = true;
        } else {
            index = date.getGregorianFestival();
            if (index >= 0) {
                // 公历节日
                txtCellLunar.setText(fomatter.getGregorianFestivalName(index));
                isFestival = true;
            } else if (date.getLunar(LunarCalendar.LUNAR_DAY) == 1) {
                // 初一,显示月份
                txtCellLunar.setText(fomatter.getMonthName(date));
            } else if (!isOutOfRange && gregorianDay == solarTerm1) {
                // 节气1
                txtCellLunar.setText(fomatter.getSolarTermName(date
                        .getGregorianDate(Calendar.MONTH) * 2));
                isSolarTerm = true;
            } else if (!isOutOfRange && gregorianDay == solarTerm2) {
                // 节气2
                txtCellLunar.setText(fomatter.getSolarTermName(date
                        .getGregorianDate(Calendar.MONTH) * 2 + 1));
                isSolarTerm = true;
            } else {
                txtCellLunar.setText(fomatter.getDayName(date));
            }
        }

        // set style
        Resources resources = container.getResources();
        if (isOutOfRange) {
            rootView.setBackgroundResource(R.drawable.selector_calendar_outrange);
            txtCellGregorian.setTextColor(resources
                    .getColor(R.color.color_calendar_outrange));
            txtCellLunar.setTextColor(resources
                    .getColor(R.color.color_calendar_outrange));
        } else if (isFestival) {
            txtCellLunar.setTextColor(resources
                    .getColor(R.color.color_calendar_festival));
        } else if (isSolarTerm) {
            txtCellLunar.setTextColor(resources
                    .getColor(R.color.color_calendar_solarterm));
        }
        if (position % 8 == 1 || position % 8 == 7) {
            rootView.setBackgroundResource(R.drawable.selector_calendar_weekend);
        }*/
        if (date.isToday() && !isOutOfRange) {
            imgCellHint.setBackgroundResource(R.drawable.img_hint_today);
        }

        // store date into tag
        rootView.setTag(date);

        return rootView;
    }

}

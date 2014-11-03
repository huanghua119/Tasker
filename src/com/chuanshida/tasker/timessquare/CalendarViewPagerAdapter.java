package com.chuanshida.tasker.timessquare;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chuanshida.tasker.R;
import com.chuanshida.tasker.fragment.FragmentBase;
import com.chuanshida.tasker.timessquare.MonthCellDescriptor.RangeState;

public class CalendarViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private SparseArray<MonthView> mViews = null;

    public Context getContext() {
        return mContext;
    }

    public CalendarViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        Resources res = context.getResources();
        dividerColor = res.getColor(R.color.transparent);
        dayBackgroundResId = R.drawable.custom_calendar_bg_selector;
        dayTextColorResId = R.color.custom_calendar_text_selector;
        titleTextColor = res.getColor(R.color.custom_calendar_text_selector);
        displayHeader = false;
        headerTextColor = res.getColor(R.color.custom_header_text);

        locale = Locale.getDefault();
        today = Calendar.getInstance(locale);
        minCal = Calendar.getInstance(locale);
        maxCal = Calendar.getInstance(locale);
        monthCounter = Calendar.getInstance(locale);
        monthNameFormat = new SimpleDateFormat(
                context.getString(R.string.month_name_format), locale);
        weekdayNameFormat = new SimpleDateFormat(
                context.getString(R.string.day_name_format), locale);
        fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        mViews = new SparseArray<MonthView>();

    }

    @Override
    public Fragment getItem(int position) {
        CalendarPagerFragment fragment = new CalendarPagerFragment(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return months.size();
    }

    public class CalendarPagerFragment extends FragmentBase {

        private int mMonthIndex;

        public CalendarPagerFragment(int monthIndex) {
            mMonthIndex = monthIndex;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            MonthView view = MonthView.create(container, inflater,
                    weekdayNameFormat, listener, today, dividerColor,
                    dayBackgroundResId, dayTextColorResId, titleTextColor,
                    displayHeader, headerTextColor);
            view.init(months.get(mMonthIndex), cells.get(mMonthIndex),
                    displayOnly, titleTypeface, dateTypeface);
            mViews.put(mMonthIndex, view);
            return view;
        }

    }

    private ViewPager mPager;

    public enum SelectionMode {
        /**
         * Only one date will be selectable. If there is already a selected date
         * and you select a new one, the old date will be unselected.
         */
        SINGLE,
        /**
         * Multiple dates will be selectable. Selecting an already-selected date
         * will un-select it.
         */
        MULTIPLE,
        /**
         * Allows you to select a date range. Previous selections are cleared
         * when you either:
         * <ul>
         * <li>Have a range selected and select another date (even if it's in
         * the current range).</li>
         * <li>Have one date selected and then select an earlier date.</li>
         * </ul>
         */
        RANGE
    }

    private final List<List<List<MonthCellDescriptor>>> cells = new ArrayList<List<List<MonthCellDescriptor>>>();
    final MonthView.Listener listener = new CellClickedListener();
    final List<MonthDescriptor> months = new ArrayList<MonthDescriptor>();
    final List<MonthCellDescriptor> selectedCells = new ArrayList<MonthCellDescriptor>();
    final List<MonthCellDescriptor> highlightedCells = new ArrayList<MonthCellDescriptor>();
    final List<Calendar> selectedCals = new ArrayList<Calendar>();
    final List<Calendar> highlightedCals = new ArrayList<Calendar>();
    private Locale locale;
    private DateFormat monthNameFormat;
    private DateFormat weekdayNameFormat;
    private DateFormat fullDateFormat;
    private Calendar minCal;
    private Calendar maxCal;
    private Calendar monthCounter;
    private boolean displayOnly;
    SelectionMode selectionMode;
    Calendar today;
    private int dividerColor;
    private int dayBackgroundResId;
    private int dayTextColorResId;
    private int titleTextColor;
    private boolean displayHeader;
    private int headerTextColor;
    private Typeface titleTypeface;
    private Typeface dateTypeface;

    private OnDateSelectedListener dateListener;
    private DateSelectableFilter dateConfiguredListener;
    private OnInvalidDateSelectedListener invalidDateListener = new DefaultOnInvalidDateSelectedListener();

    public FluentInitializer init(Date minDate, Date maxDate, Locale locale,
            ViewPager pager) {
        if (minDate == null || maxDate == null) {
            throw new IllegalArgumentException(
                    "minDate and maxDate must be non-null.  "
                            + dbg(minDate, maxDate));
        }
        if (minDate.after(maxDate)) {
            throw new IllegalArgumentException(
                    "minDate must be before maxDate.  " + dbg(minDate, maxDate));
        }
        if (minDate.getTime() == 0 || maxDate.getTime() == 0) {
            throw new IllegalArgumentException(
                    "minDate and maxDate must be non-zero.  "
                            + dbg(minDate, maxDate));
        }
        if (locale == null) {
            throw new IllegalArgumentException("Locale is null.");
        }

        // Make sure that all calendar instances use the same locale.
        this.locale = locale;
        today = Calendar.getInstance(locale);
        minCal = Calendar.getInstance(locale);
        maxCal = Calendar.getInstance(locale);
        monthCounter = Calendar.getInstance(locale);
        monthNameFormat = new SimpleDateFormat(getContext().getString(
                R.string.month_name_format), locale);
        for (MonthDescriptor month : months) {
            month.setLabel(monthNameFormat.format(month.getDate()));
        }
        weekdayNameFormat = new SimpleDateFormat(getContext().getString(
                R.string.day_name_format), locale);
        fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        this.selectionMode = SelectionMode.SINGLE;
        // Clear out any previously-selected dates/cells.
        selectedCals.clear();
        selectedCells.clear();
        highlightedCals.clear();
        highlightedCells.clear();

        // Clear previous state.
        cells.clear();
        months.clear();
        minCal.setTime(minDate);
        maxCal.setTime(maxDate);
        setMidnight(minCal);
        setMidnight(maxCal);
        displayOnly = false;

        // maxDate is exclusive: bump back to the previous day so if maxDate is
        // the first of a month,
        // we don't accidentally include that month in the view.
        maxCal.add(MINUTE, -1);

        // Now iterate between minCal and maxCal and build up our list of months
        // to show.
        monthCounter.setTime(minCal.getTime());
        final int maxMonth = maxCal.get(MONTH);
        final int maxYear = maxCal.get(YEAR);
        while ((monthCounter.get(MONTH) <= maxMonth // Up to, including the
                                                    // month.
                || monthCounter.get(YEAR) < maxYear) // Up to the year.
                && monthCounter.get(YEAR) < maxYear + 1) { // But not > next yr.
            Date date = monthCounter.getTime();
            MonthDescriptor month = new MonthDescriptor(
                    monthCounter.get(MONTH), monthCounter.get(YEAR), date,
                    monthNameFormat.format(date));
            cells.add(getMonthCells(month, monthCounter));
            Logr.d("Adding month %s", month);
            months.add(month);
            monthCounter.add(MONTH, 1);
        }
        mPager = pager;

        validateAndUpdate();
        return new FluentInitializer();
    }

    /**
     * Both date parameters must be non-null and their {@link Date#getTime()}
     * must not return 0. Time of day will be ignored. For instance, if you pass
     * in {@code minDate} as 11/16/2012 5:15pm and {@code maxDate} as 11/16/2013
     * 4:30am, 11/16/2012 will be the first selectable date and 11/15/2013 will
     * be the last selectable date ({@code maxDate} is exclusive).
     * <p>
     * This will implicitly set the {@link SelectionMode} to
     * {@link SelectionMode#SINGLE}. If you want a different selection mode, use
     * {@link FluentInitializer#inMode(SelectionMode)} on the
     * {@link FluentInitializer} this method returns.
     * <p>
     * The calendar will be constructed using the default locale as returned by
     * {@link java.util.Locale#getDefault()}. If you wish the calendar to be
     * constructed using a different locale, use
     * {@link #init(java.util.Date, java.util.Date, java.util.Locale)}.
     * 
     * @param minDate
     *            Earliest selectable date, inclusive. Must be earlier than
     *            {@code maxDate}.
     * @param maxDate
     *            Latest selectable date, exclusive. Must be later than
     *            {@code minDate}.
     */
    public FluentInitializer init(Date minDate, Date maxDate, ViewPager pager) {
        return init(minDate, maxDate, Locale.getDefault(), pager);
    }

    public class FluentInitializer {
        /**
         * Override the {@link SelectionMode} from the default (
         * {@link SelectionMode#SINGLE}).
         */
        public FluentInitializer inMode(SelectionMode mode) {
            selectionMode = mode;
            validateAndUpdate();
            return this;
        }

        /**
         * Set an initially-selected date. The calendar will scroll to that date
         * if it's not already visible.
         */
        public FluentInitializer withSelectedDate(Date selectedDates) {
            return withSelectedDates(Arrays.asList(selectedDates));
        }

        /**
         * Set multiple selected dates. This will throw an
         * {@link IllegalArgumentException} if you pass in multiple dates and
         * haven't already called {@link #inMode(SelectionMode)}.
         */
        public FluentInitializer withSelectedDates(
                Collection<Date> selectedDates) {
            if (selectionMode == SelectionMode.SINGLE
                    && selectedDates.size() > 1) {
                throw new IllegalArgumentException(
                        "SINGLE mode can't be used with multiple selectedDates");
            }
            if (selectedDates != null) {
                for (Date date : selectedDates) {
                    selectDate(date);
                }
            }
            scrollToSelectedDates();

            validateAndUpdate();
            return this;
        }

        public FluentInitializer withHighlightedDates(Collection<Date> dates) {
            highlightDates(dates);
            return this;
        }

        public FluentInitializer withHighlightedDate(Date date) {
            return withHighlightedDates(Arrays.asList(date));
        }

        public FluentInitializer setShortWeekdays(String[] newShortWeekdays) {
            DateFormatSymbols symbols = new DateFormatSymbols(locale);
            symbols.setShortWeekdays(newShortWeekdays);
            weekdayNameFormat = new SimpleDateFormat(getContext().getString(
                    R.string.day_name_format), symbols);
            return this;
        }

        public FluentInitializer displayOnly() {
            displayOnly = true;
            return this;
        }
    }

    private void validateAndUpdate() {
        if (mPager.getAdapter() == null) {
            mPager.setAdapter(this);
        }
        notifyDataSetChanged();
    }

    private void scrollToSelectedMonth(final int selectedIndex) {
        scrollToSelectedMonth(selectedIndex, false);
    }

    private void scrollToSelectedMonth(final int selectedIndex,
            final boolean smoothScroll) {
        mPager.post(new Runnable() {
            @Override
            public void run() {
                mPager.setCurrentItem(selectedIndex, smoothScroll);
            }
        });
    }

    private void scrollToSelectedDates() {
        Integer selectedIndex = null;
        Integer todayIndex = null;
        Calendar today = Calendar.getInstance(locale);
        for (int c = 0; c < months.size(); c++) {
            MonthDescriptor month = months.get(c);
            if (selectedIndex == null) {
                for (Calendar selectedCal : selectedCals) {
                    if (sameMonth(selectedCal, month)) {
                        selectedIndex = c;
                        break;
                    }
                }
                if (selectedIndex == null && todayIndex == null
                        && sameMonth(today, month)) {
                    todayIndex = c;
                }
            }
        }
        if (selectedIndex != null) {
            scrollToSelectedMonth(selectedIndex);
        } else if (todayIndex != null) {
            scrollToSelectedMonth(todayIndex);
        }
    }

    public void setTitleTypeface(Typeface titleTypeface) {
        this.titleTypeface = titleTypeface;
        validateAndUpdate();
    }

    /**
     * Sets the typeface to be used within the date grid.
     */
    public void setDateTypeface(Typeface dateTypeface) {
        this.dateTypeface = dateTypeface;
        validateAndUpdate();
    }

    /**
     * Sets the typeface to be used for all text within this calendar.
     */
    public void setTypeface(Typeface typeface) {
        setTitleTypeface(typeface);
        setDateTypeface(typeface);
    }

    public Date getSelectedDate() {
        return (selectedCals.size() > 0 ? selectedCals.get(0).getTime() : null);
    }

    public List<Date> getSelectedDates() {
        List<Date> selectedDates = new ArrayList<Date>();
        for (MonthCellDescriptor cal : selectedCells) {
            selectedDates.add(cal.getDate());
        }
        Collections.sort(selectedDates);
        return selectedDates;
    }

    /** Returns a string summarizing what the client sent us for init() params. */
    private static String dbg(Date minDate, Date maxDate) {
        return "minDate: " + minDate + "\nmaxDate: " + maxDate;
    }

    /** Clears out the hours/minutes/seconds/millis of a Calendar. */
    static void setMidnight(Calendar cal) {
        cal.set(HOUR_OF_DAY, 0);
        cal.set(MINUTE, 0);
        cal.set(SECOND, 0);
        cal.set(MILLISECOND, 0);
    }

    private class CellClickedListener implements MonthView.Listener {
        @Override
        public void handleClick(MonthCellDescriptor cell) {
            Date clickedDate = cell.getDate();

            if (!betweenDates(clickedDate, minCal, maxCal)
                    || !isDateSelectable(clickedDate)) {
                if (invalidDateListener != null) {
                    invalidDateListener.onInvalidDateSelected(clickedDate);
                }
            } else {
                boolean wasSelected = doSelectDate(clickedDate, cell);

                if (dateListener != null) {
                    if (wasSelected) {
                        dateListener.onDateSelected(clickedDate);
                    } else {
                        dateListener.onDateUnselected(clickedDate);
                    }
                }
            }
        }
    }

    /**
     * Select a new date. Respects the {@link SelectionMode} this
     * CalendarPickerView is configured with: if you are in
     * {@link SelectionMode#SINGLE}, the previously selected date will be
     * un-selected. In {@link SelectionMode#MULTIPLE}, the new date will be
     * added to the list of selected dates.
     * <p>
     * If the selection was made (selectable date, in range), the view will
     * scroll to the newly selected date if it's not already visible.
     * 
     * @return - whether we were able to set the date
     */
    public boolean selectDate(Date date) {
        return selectDate(date, false);
    }

    /**
     * Select a new date. Respects the {@link SelectionMode} this
     * CalendarPickerView is configured with: if you are in
     * {@link SelectionMode#SINGLE}, the previously selected date will be
     * un-selected. In {@link SelectionMode#MULTIPLE}, the new date will be
     * added to the list of selected dates.
     * <p>
     * If the selection was made (selectable date, in range), the view will
     * scroll to the newly selected date if it's not already visible.
     * 
     * @return - whether we were able to set the date
     */
    public boolean selectDate(Date date, boolean smoothScroll) {
        validateDate(date);

        MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(date);
        if (monthCellWithMonthIndex == null || !isDateSelectable(date)) {
            return false;
        }
        boolean wasSelected = doSelectDate(date, monthCellWithMonthIndex.cell);
        if (wasSelected) {
            scrollToSelectedMonth(monthCellWithMonthIndex.monthIndex,
                    smoothScroll);
        }
        return wasSelected;
    }

    private void validateDate(Date date) {
        if (date == null) {
            throw new IllegalArgumentException(
                    "Selected date must be non-null.");
        }
        if (date.getTime() == 0) {
            throw new IllegalArgumentException(
                    "Selected date must be non-zero.  " + date);
        }
        if (date.before(minCal.getTime()) || date.after(maxCal.getTime())) {
            throw new IllegalArgumentException(String.format(
                    "SelectedDate must be between minDate and maxDate."
                            + "%nminDate: %s%nmaxDate: %s%nselectedDate: %s",
                    minCal.getTime(), maxCal.getTime(), date));
        }
    }

    private boolean doSelectDate(Date date, MonthCellDescriptor cell) {
        Calendar newlySelectedCal = Calendar.getInstance(locale);
        newlySelectedCal.setTime(date);
        // Sanitize input: clear out the hours/minutes/seconds/millis.
        setMidnight(newlySelectedCal);

        // Clear any remaining range state.
        for (MonthCellDescriptor selectedCell : selectedCells) {
            selectedCell.setRangeState(RangeState.NONE);
        }

        switch (selectionMode) {
        case RANGE:
            if (selectedCals.size() > 1) {
                // We've already got a range selected: clear the old one.
                clearOldSelections();
            } else if (selectedCals.size() == 1
                    && newlySelectedCal.before(selectedCals.get(0))) {
                // We're moving the start of the range back in time: clear the
                // old start date.
                clearOldSelections();
            }
            break;

        case MULTIPLE:
            date = applyMultiSelect(date, newlySelectedCal);
            break;

        case SINGLE:
            clearOldSelections();
            break;
        default:
            throw new IllegalStateException("Unknown selectionMode "
                    + selectionMode);
        }

        if (date != null) {
            // Select a new cell.
            if (selectedCells.size() == 0 || !selectedCells.get(0).equals(cell)) {
                selectedCells.add(cell);
                cell.setSelected(true);
            }
            selectedCals.add(newlySelectedCal);

            if (selectionMode == SelectionMode.RANGE
                    && selectedCells.size() > 1) {
                // Select all days in between start and end.
                Date start = selectedCells.get(0).getDate();
                Date end = selectedCells.get(1).getDate();
                selectedCells.get(0).setRangeState(
                        MonthCellDescriptor.RangeState.FIRST);
                selectedCells.get(1).setRangeState(
                        MonthCellDescriptor.RangeState.LAST);

                for (List<List<MonthCellDescriptor>> month : cells) {
                    for (List<MonthCellDescriptor> week : month) {
                        for (MonthCellDescriptor singleCell : week) {
                            if (singleCell.getDate().after(start)
                                    && singleCell.getDate().before(end)
                                    && singleCell.isSelectable()) {
                                singleCell.setSelected(true);
                                singleCell
                                        .setRangeState(MonthCellDescriptor.RangeState.MIDDLE);
                                selectedCells.add(singleCell);
                            }
                        }
                    }
                }
            }
        }

        // Update the adapter.
        int position = mPager.getCurrentItem();
        MonthView view = (MonthView) mViews.get(position);
        if (view != null) {
            view.init(months.get(position), cells.get(position),
                    displayOnly, titleTypeface, dateTypeface);
        }
        validateAndUpdate();
        return date != null;
    }

    public String getCurrentDate() {
        int position = mPager.getCurrentItem();
        return months.get(position).getLabel();
    }

    private void clearOldSelections() {
        for (MonthCellDescriptor selectedCell : selectedCells) {
            // De-select the currently-selected cell.
            selectedCell.setSelected(false);
        }
        selectedCells.clear();
        selectedCals.clear();
    }

    private Date applyMultiSelect(Date date, Calendar selectedCal) {
        for (MonthCellDescriptor selectedCell : selectedCells) {
            if (selectedCell.getDate().equals(date)) {
                // De-select the currently-selected cell.
                selectedCell.setSelected(false);
                selectedCells.remove(selectedCell);
                date = null;
                break;
            }
        }
        for (Calendar cal : selectedCals) {
            if (sameDate(cal, selectedCal)) {
                selectedCals.remove(cal);
                break;
            }
        }
        return date;
    }

    public void highlightDates(Collection<Date> dates) {
        for (Date date : dates) {
            validateDate(date);

            MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(date);
            if (monthCellWithMonthIndex != null) {
                Calendar newlyHighlightedCal = Calendar.getInstance();
                newlyHighlightedCal.setTime(date);
                MonthCellDescriptor cell = monthCellWithMonthIndex.cell;

                highlightedCells.add(cell);
                highlightedCals.add(newlyHighlightedCal);
                cell.setHighlighted(true);
            }
        }

        notifyDataSetChanged();
        mPager.setAdapter(this);
    }

    /** Hold a cell with a month-index. */
    private static class MonthCellWithMonthIndex {
        public MonthCellDescriptor cell;
        public int monthIndex;

        public MonthCellWithMonthIndex(MonthCellDescriptor cell, int monthIndex) {
            this.cell = cell;
            this.monthIndex = monthIndex;
        }
    }

    /** Return cell and month-index (for scrolling) for a given Date. */
    private MonthCellWithMonthIndex getMonthCellWithIndexByDate(Date date) {
        int index = 0;
        Calendar searchCal = Calendar.getInstance(locale);
        searchCal.setTime(date);
        Calendar actCal = Calendar.getInstance(locale);

        for (List<List<MonthCellDescriptor>> monthCells : cells) {
            for (List<MonthCellDescriptor> weekCells : monthCells) {
                for (MonthCellDescriptor actCell : weekCells) {
                    actCal.setTime(actCell.getDate());
                    if (sameDate(actCal, searchCal) && actCell.isSelectable()) {
                        return new MonthCellWithMonthIndex(actCell, index);
                    }
                }
            }
            index++;
        }
        return null;
    }


    List<List<MonthCellDescriptor>> getMonthCells(MonthDescriptor month,
            Calendar startCal) {
        Calendar cal = Calendar.getInstance(locale);
        cal.setTime(startCal.getTime());
        List<List<MonthCellDescriptor>> cells = new ArrayList<List<MonthCellDescriptor>>();
        cal.set(DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(DAY_OF_WEEK);
        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
        if (offset > 0) {
            offset -= 7;
        }
        cal.add(Calendar.DATE, offset);

        Calendar minSelectedCal = minDate(selectedCals);
        Calendar maxSelectedCal = maxDate(selectedCals);

        while ((cal.get(MONTH) < month.getMonth() + 1 || cal.get(YEAR) < month
                .getYear()) //
                && cal.get(YEAR) <= month.getYear()) {
            Logr.d("Building week row starting at %s", cal.getTime());
            List<MonthCellDescriptor> weekCells = new ArrayList<MonthCellDescriptor>();
            cells.add(weekCells);
            for (int c = 0; c < 7; c++) {
                Date date = cal.getTime();
                boolean isCurrentMonth = cal.get(MONTH) == month.getMonth();
                boolean isSelected = isCurrentMonth
                        && containsDate(selectedCals, cal);
                boolean isSelectable = isCurrentMonth
                        && betweenDates(cal, minCal, maxCal)
                        && isDateSelectable(date);
                boolean isToday = sameDate(cal, today);
                boolean isHighlighted = containsDate(highlightedCals, cal);
                int value = cal.get(DAY_OF_MONTH);

                MonthCellDescriptor.RangeState rangeState = MonthCellDescriptor.RangeState.NONE;
                if (selectedCals.size() > 1) {
                    if (sameDate(minSelectedCal, cal)) {
                        rangeState = MonthCellDescriptor.RangeState.FIRST;
                    } else if (sameDate(maxDate(selectedCals), cal)) {
                        rangeState = MonthCellDescriptor.RangeState.LAST;
                    } else if (betweenDates(cal, minSelectedCal, maxSelectedCal)) {
                        rangeState = MonthCellDescriptor.RangeState.MIDDLE;
                    }
                }

                weekCells.add(new MonthCellDescriptor(date, isCurrentMonth,
                        isSelectable, isSelected, isToday, isHighlighted,
                        value, rangeState));
                cal.add(DATE, 1);
            }
        }
        return cells;
    }

    private static boolean containsDate(List<Calendar> selectedCals,
            Calendar cal) {
        for (Calendar selectedCal : selectedCals) {
            if (sameDate(cal, selectedCal)) {
                return true;
            }
        }
        return false;
    }

    private static Calendar minDate(List<Calendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(0);
    }

    private static Calendar maxDate(List<Calendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(selectedCals.size() - 1);
    }

    private static boolean sameDate(Calendar cal, Calendar selectedDate) {
        return cal.get(MONTH) == selectedDate.get(MONTH)
                && cal.get(YEAR) == selectedDate.get(YEAR)
                && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
    }

    private static boolean betweenDates(Calendar cal, Calendar minCal,
            Calendar maxCal) {
        final Date date = cal.getTime();
        return betweenDates(date, minCal, maxCal);
    }

    static boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
        final Date min = minCal.getTime();
        return (date.equals(min) || date.after(min)) // >= minCal
                && date.before(maxCal.getTime()); // && < maxCal
    }

    private static boolean sameMonth(Calendar cal, MonthDescriptor month) {
        return (cal.get(MONTH) == month.getMonth() && cal.get(YEAR) == month
                .getYear());
    }

    private boolean isDateSelectable(Date date) {
        return dateConfiguredListener == null
                || dateConfiguredListener.isDateSelectable(date);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        dateListener = listener;
    }

    /**
     * Set a listener to react to user selection of a disabled date.
     * 
     * @param listener
     *            the listener to set, or null for no reaction
     */
    public void setOnInvalidDateSelectedListener(
            OnInvalidDateSelectedListener listener) {
        invalidDateListener = listener;
    }

    /**
     * Set a listener used to discriminate between selectable and unselectable
     * dates. Set this to disable arbitrary dates as they are rendered.
     * <p>
     * Important: set this before you call {@link #init(Date, Date)} methods. If
     * called afterwards, it will not be consistently applied.
     */
    public void setDateSelectableFilter(DateSelectableFilter listener) {
        dateConfiguredListener = listener;
    }

    /**
     * Interface to be notified when a new date is selected or unselected. This
     * will only be called when the user initiates the date selection. If you
     * call {@link #selectDate(Date)} this listener will not be notified.
     * 
     * @see #setOnDateSelectedListener(OnDateSelectedListener)
     */
    public interface OnDateSelectedListener {
        void onDateSelected(Date date);

        void onDateUnselected(Date date);
    }

    /**
     * Interface to be notified when an invalid date is selected by the user.
     * This will only be called when the user initiates the date selection. If
     * you call {@link #selectDate(Date)} this listener will not be notified.
     * 
     * @see #setOnInvalidDateSelectedListener(OnInvalidDateSelectedListener)
     */
    public interface OnInvalidDateSelectedListener {
        void onInvalidDateSelected(Date date);
    }

    /**
     * Interface used for determining the selectability of a date cell when it
     * is configured for display on the calendar.
     * 
     * @see #setDateSelectableFilter(DateSelectableFilter)
     */
    public interface DateSelectableFilter {
        boolean isDateSelectable(Date date);
    }

    private class DefaultOnInvalidDateSelectedListener implements
            OnInvalidDateSelectedListener {
        @Override
        public void onInvalidDateSelected(Date date) {
            String errMessage = getContext().getResources().getString(R.string.invalid_date,
                    fullDateFormat.format(minCal.getTime()),
                    fullDateFormat.format(maxCal.getTime()));
            Toast.makeText(getContext(), errMessage, Toast.LENGTH_SHORT).show();
        }
    }
}

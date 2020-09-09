package com.potato.datepicker.datepicker;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.potato.datepicker.R;
import com.potato.datepicker.datepicker.adapters.NumericWheelAdapter;
import com.potato.datepicker.datepicker.config.ScrollerConfig;
import com.potato.datepicker.datepicker.data.source.TimeRepository;
import com.potato.datepicker.datepicker.utils.DateConstants;
import com.potato.datepicker.datepicker.utils.Utils;
import com.potato.datepicker.datepicker.wheel.OnWheelChangedListener;
import com.potato.datepicker.datepicker.wheel.WheelView;

import java.util.Calendar;

/**
 * 时间滚轮, 用于控制时间数据
 */
class TimeWheel {
    private Context mContext;
    private TextView mTvLineView;
    private WheelView mYearView, mMonthView, mDayView, mHourView, mMinuteView, mFinishYearView, mFinishMonthView, mFinishDayView, mFinishHourView, mFinishMinuteView; // 滚动视图
    private NumericWheelAdapter mYearAdapter, mMonthAdapter, mDayAdapter, mHourAdapter, mMinuteAdapter;

    private ScrollerConfig mScrollerConfig;
    private TimeRepository mRepository;

    private OnWheelChangedListener mYearListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateMonths();

            // 如果开始年份大于结束年份
            if (mYearView.getCurrentItem() > mFinishYearView.getCurrentItem()) {
                // 则结束年份马上更新到相同年份
                mFinishYearView.setCurrentItem(mYearView.getCurrentItem());

                // 当前月份大于结束月份
                if (mMonthView.getCurrentItem() > mFinishMonthView.getCurrentItem()) {
                    // 结束月份设置为开始月份
                    mFinishMonthView.setCurrentItem(mMonthView.getCurrentItem());
                }
            }
        }
    };

    private OnWheelChangedListener mFinishYearListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateFinishMonths();

            // 如果结束年份小于结束年份
            if (mFinishYearView.getCurrentItem() < mYearView.getCurrentItem()) {
                // 则开始年份马上更新到相同年份
                mYearView.setCurrentItem(mFinishYearView.getCurrentItem());

                // 当前结束月份大于开始月份
                if (mFinishMonthView.getCurrentItem() < mMonthView.getCurrentItem()) {
                    // 开始月份设置为结束月份
                    mMonthView.setCurrentItem(mFinishMonthView.getCurrentItem());

                    // 开始日大于结束日
                    if (mDayView.getCurrentItem() > mFinishDayView.getCurrentItem()) {
                        // 结束日设置为开始日
                        mFinishDayView.setCurrentItem(mDayView.getCurrentItem());
                    }
                }
            }
        }
    };
    private OnWheelChangedListener mMonthListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateDays();

            // 开始月份大于结束月份
            if (mMonthView.getCurrentItem() > mFinishMonthView.getCurrentItem() && mYearView.getCurrentItem() == mFinishYearView.getCurrentItem()) {
                // 结束月份设置为开始月份
                mFinishMonthView.setCurrentItem(mMonthView.getCurrentItem());
            }
        }
    };
    private OnWheelChangedListener mFinishMonthListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateFinishDays();

            // 当前结束月份大于开始月份
            if (mFinishMonthView.getCurrentItem() < mMonthView.getCurrentItem() && mYearView.getCurrentItem() == mFinishYearView.getCurrentItem()) {
                // 开始月份设置为结束月份
                mMonthView.setCurrentItem(mFinishMonthView.getCurrentItem());
            }
        }
    };

    // 开始日监听
    private OnWheelChangedListener mDayListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
//            updateHours();

            // 开始日大于结束日
            if (mDayView.getCurrentItem() > mFinishDayView.getCurrentItem() && mFinishMonthView.getCurrentItem() == mMonthView.getCurrentItem()) {
                // 结束日设置为开始日
                mFinishDayView.setCurrentItem(mDayView.getCurrentItem());
            }
        }
    };

    // 结束日监听
    private OnWheelChangedListener mFinishDayListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            // 开始日大于结束日
            if (mFinishDayView.getCurrentItem() < mDayView.getCurrentItem() && mFinishMonthView.getCurrentItem() == mMonthView.getCurrentItem()) {
                // 结束日设置为开始日
                mDayView.setCurrentItem(mFinishDayView.getCurrentItem());
            }
        }
    };

    private OnWheelChangedListener mHourListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateMinutes();
            // 开始月份大于结束月份
            if (mHourView.getCurrentItem() > mFinishHourView.getCurrentItem() && mYearView.getCurrentItem() == mFinishYearView.getCurrentItem() && mMonthView.getCurrentItem() == mFinishMonthView.getCurrentItem()) {
                // 结束月份设置为开始月份
                mFinishHourView.setCurrentItem(mHourView.getCurrentItem());
            }
        }
    };

    /**
     * 设置视图与参数
     *
     * @param view           视图
     * @param scrollerConfig 滚动参数
     */
    TimeWheel(View view, ScrollerConfig scrollerConfig) {
        mScrollerConfig = scrollerConfig;
        mRepository = new TimeRepository(scrollerConfig);
        mContext = view.getContext();
        initialize(view);
    }

    /**
     * 初始化与设置视图
     *
     * @param view 视图
     */
    private void initialize(View view) {
        initView(view); // 初始化视图

        // 初始化年份和结束年
        initYearView();
        initMonthView();
        initDayView();
        initHourView();
        initMinuteView();

        // 初始化结束月
        initFinishMonthView();
        // 初始化结束日
        initFinishDayView();
        initFinishHourView();
        initFinishMinuteView();
    }


    /**
     * 初始化视图
     *
     * @param view 视图
     */
    private void initView(View view) {
        mYearView = (WheelView) view.findViewById(R.id.year);
        mMonthView = (WheelView) view.findViewById(R.id.month);
        mDayView = (WheelView) view.findViewById(R.id.day);
        mHourView = (WheelView) view.findViewById(R.id.hour);
        mMinuteView = (WheelView) view.findViewById(R.id.minute);
        mTvLineView=view.findViewById(R.id.line);
        mFinishYearView = (WheelView) view.findViewById(R.id.finish_year);
        mFinishMonthView = (WheelView) view.findViewById(R.id.finish_month);
        mFinishDayView = (WheelView) view.findViewById(R.id.finish_day);
        mFinishHourView = view.findViewById(R.id.finish_hour);
        mFinishMinuteView = view.findViewById(R.id.finish_minute);

        switch (mScrollerConfig.mMode){
            case DOUBLE_MODE:
                Utils.showViews(mTvLineView,mFinishYearView,mFinishMonthView,mFinishDayView,mFinishHourView,mFinishMinuteView);
                break;
            case SINGLE_MODE:
                Utils.hideViews(mTvLineView,mFinishYearView,mFinishMonthView,mFinishDayView,mFinishHourView,mFinishMinuteView);
                break;
        }

        // 根据类型, 设置隐藏位置
        switch (mScrollerConfig.mType) {
            case ALL:
                break;
            case YEAR_MONTH_DAY:
                Utils.hideViews(mHourView, mMinuteView, mFinishHourView, mFinishMinuteView);
                break;
            case YEAR_MONTH:
                Utils.hideViews(mDayView, mHourView, mMinuteView, mFinishDayView, mFinishHourView, mFinishMinuteView);
                break;
            case MONTH_DAY_HOUR_MIN:
                Utils.hideViews(mYearView, mFinishYearView);
                break;
            case HOURS_MINS:
                Utils.hideViews(mYearView, mMonthView, mDayView, mFinishYearView, mFinishMonthView, mFinishDayView);
                break;
            case YEAR:
                Utils.hideViews(mMonthView, mDayView, mHourView, mMinuteView, mFinishMonthView, mFinishDayView, mFinishHourView, mFinishMinuteView);
                break;
        }

        mYearView.addChangingListener(mYearListener);
        mYearView.addChangingListener(mMonthListener);
        mYearView.addChangingListener(mDayListener);
        mYearView.addChangingListener(mHourListener);

        mFinishYearView.addChangingListener(mFinishYearListener);
        mFinishYearView.addChangingListener(mMonthListener);
        mFinishYearView.addChangingListener(mDayListener);
        mFinishYearView.addChangingListener(mHourListener);

        mMonthView.addChangingListener(mMonthListener);
        mMonthView.addChangingListener(mDayListener);
        mMonthView.addChangingListener(mHourListener);

        mFinishMonthView.addChangingListener(mFinishMonthListener);

        mDayView.addChangingListener(mDayListener);
        mDayView.addChangingListener(mHourListener);

        mFinishDayView.addChangingListener(mDayListener);

        mHourView.addChangingListener(mHourListener);

        mFinishHourView.addChangingListener(mHourListener);
    }

    /**
     * 初始化年视图, 年是最高级
     */
    private void initYearView() {
        int minYear = mRepository.getMinYear();
        int maxYear = mRepository.getMaxYear();

        mYearAdapter = new NumericWheelAdapter(mContext, minYear, maxYear,
                DateConstants.FORMAT, mScrollerConfig.mYear);
        mYearAdapter.setConfig(mScrollerConfig);

        mYearView.setViewAdapter(mYearAdapter);
        mFinishYearView.setViewAdapter(mYearAdapter);

        mYearView.setCurrentItem(mRepository.getDefaultCalendar().year - minYear);
        mFinishYearView.setCurrentItem(mRepository.getDefaultFinishCalendar().year - minYear);
    }

    /**
     * 初始化月视图
     */
    private void initMonthView() {
        updateMonths();
        int curYear = getCurrentYear();
        int minMonth = mRepository.getMinMonth(curYear);
        mMonthView.setCurrentItem(mRepository.getDefaultCalendar().month - minMonth);
    }

    /**
     * 初始化结束月视图
     */
    private void initFinishMonthView() {
        updateFinishMonths();
        int curYear = getCurrentFinishYear();
        int minMonth = mRepository.getMinMonth(curYear);
        mFinishMonthView.setCurrentItem(mRepository.getDefaultFinishCalendar().month - minMonth);
    }

    /**
     * 初始化日视图
     */
    private void initDayView() {
        updateDays();
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();

        int minDay = mRepository.getMinDay(curYear, curMonth);
        mDayView.setCurrentItem(mRepository.getDefaultCalendar().day - minDay);
    }

    /**
     * 初始化结束日视图
     */
    private void initFinishDayView() {
        updateFinishDays();
        int curYear = getCurrentFinishYear();
        int curMonth = getCurrentFinishMonth();

        int minDay = mRepository.getMinDay(curYear, curMonth);
        mFinishDayView.setCurrentItem(mRepository.getDefaultFinishCalendar().day - minDay);
    }

    /**
     * 初始化小时视图
     */
    private void initHourView() {
        updateHours();
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();

        int minHour = mRepository.getMinHour(curYear, curMonth, curDay);
        mHourView.setCurrentItem(mRepository.getDefaultCalendar().hour - minHour);
    }


    private void initFinishHourView() {
        updateFinishHours();
        int curYear = getCurrentFinishYear();
        int curMonth = getCurrentFinishMonth();
        int curDay = getCurrentFinishDay();

        int minHour = mRepository.getMinHour(curYear, curMonth, curDay);
        mFinishHourView.setCurrentItem(mRepository.getDefaultFinishCalendar().hour - minHour);
    }


    /**
     * 初始化分钟视图
     */
    private void initMinuteView() {
        updateMinutes();
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        int curHour = getCurrentHour();
        int minMinute = mRepository.getMinMinute(curYear, curMonth, curDay, curHour);

        mMinuteView.setCurrentItem(mRepository.getDefaultCalendar().minute - minMinute);
    }

    private void initFinishMinuteView() {
        updateFinishMinutes();
        int curYear = getCurrentFinishYear();
        int curMonth = getCurrentFinishMonth();
        int curDay = getCurrentFinishDay();
        int curHour = getCurrentFinishHour();
        int minMinute = mRepository.getMinMinute(curYear, curMonth, curDay, curHour);

        mFinishMinuteView.setCurrentItem(mRepository.getDefaultFinishCalendar().minute - minMinute);

    }

    /**
     * 更新月份
     */
    private void updateMonths() {
        if (mMonthView.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int minMonth = mRepository.getMinMonth(curYear);
        int maxMonth = mRepository.getMaxMonth(curYear);
        mMonthAdapter = new NumericWheelAdapter(mContext, minMonth, maxMonth,
                DateConstants.FORMAT, mScrollerConfig.mMonth);
        mMonthAdapter.setConfig(mScrollerConfig);
        mMonthView.setViewAdapter(mMonthAdapter);
        mMonthView.setCyclic(mScrollerConfig.cyclic);

        // 当滚动数量不足时, 需要避免循环
        if (maxMonth - minMonth < mScrollerConfig.mMaxLines) {
            mMonthView.setCyclic(false);
        }

        if (mRepository.isMinYear(curYear)) {
            mMonthView.setCurrentItem(0, false);
        }
    }

    /**
     * 更新结束月份
     */
    private void updateFinishMonths() {
        if (mFinishMonthView.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentFinishYear();
        int minMonth = mRepository.getMinMonth(curYear);
        int maxMonth = mRepository.getMaxMonth(curYear);
        mMonthAdapter = new NumericWheelAdapter(mContext, minMonth, maxMonth,
                DateConstants.FORMAT, mScrollerConfig.mMonth);
        mMonthAdapter.setConfig(mScrollerConfig);
        mFinishMonthView.setViewAdapter(mMonthAdapter);
        mFinishMonthView.setCyclic(mScrollerConfig.cyclic);

        // 当滚动数量不足时, 需要避免循环
        if (maxMonth - minMonth < mScrollerConfig.mMaxLines) {
            mFinishMonthView.setCyclic(false);
        }

        if (mRepository.isMinYear(curYear)) {
            mFinishMonthView.setCurrentItem(0, false);
        }
    }

    /**
     * 更新日
     */
    private void updateDays() {
        if (mDayView.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + mYearView.getCurrentItem());
        calendar.set(Calendar.MONTH, curMonth);

        int maxDay = mRepository.getMaxDay(curYear, curMonth);
        int minDay = mRepository.getMinDay(curYear, curMonth);
        mDayAdapter = new NumericWheelAdapter(mContext, minDay, maxDay, DateConstants.FORMAT, mScrollerConfig.mDay);
        mDayAdapter.setConfig(mScrollerConfig);
        mDayView.setViewAdapter(mDayAdapter);
        mDayView.setCyclic(mScrollerConfig.cyclic); // 是否循环

        // 当滚动数量不足时, 需要避免循环
        if (maxDay - minDay < mScrollerConfig.mMaxLines) {
            mDayView.setCyclic(false);
        }

        if (mRepository.isMinMonth(curYear, curMonth)) {
            mDayView.setCurrentItem(0, true);
        }

        int dayCount = mDayAdapter.getItemsCount();
        if (mDayView.getCurrentItem() >= dayCount) {
            mDayView.setCurrentItem(dayCount - 1, true);
        }
    }

    /**
     * 更新结束日
     */
    private void updateFinishDays() {
        if (mFinishDayView.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentFinishYear();
        int curMonth = getCurrentFinishMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + mFinishDayView.getCurrentItem());
        calendar.set(Calendar.MONTH, curMonth);

        int maxDay = mRepository.getMaxDay(curYear, curMonth);
        int minDay = mRepository.getMinDay(curYear, curMonth);
        mDayAdapter = new NumericWheelAdapter(mContext, minDay, maxDay, DateConstants.FORMAT, mScrollerConfig.mDay);
        mDayAdapter.setConfig(mScrollerConfig);
        mFinishDayView.setViewAdapter(mDayAdapter);
        mFinishDayView.setCyclic(mScrollerConfig.cyclic); // 是否循环

        // 当滚动数量不足时, 需要避免循环
        if (maxDay - minDay < mScrollerConfig.mMaxLines) {
            mFinishDayView.setCyclic(false);
        }

        if (mRepository.isMinMonth(curYear, curMonth)) {
            mFinishDayView.setCurrentItem(0, true);
        }

        int dayCount = mDayAdapter.getItemsCount();
        if (mFinishDayView.getCurrentItem() >= dayCount) {
            mFinishDayView.setCurrentItem(dayCount - 1, true);
        }
    }

    /**
     * 更新小时
     */
    private void updateHours() {
        if (mHourView.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();

        int minHour = mRepository.getMinHour(curYear, curMonth, curDay);
        int maxHour = mRepository.getMaxHour(curYear, curMonth, curDay);

        mHourAdapter = new NumericWheelAdapter(mContext, minHour, maxHour, DateConstants.FORMAT, mScrollerConfig.mHour);
        mHourAdapter.setConfig(mScrollerConfig);
        mHourView.setViewAdapter(mHourAdapter);
        mHourView.setCyclic(mScrollerConfig.cyclic);

        // 当滚动数量不足时, 需要避免循环
        if (maxHour - minHour < mScrollerConfig.mMaxLines) {
            mHourView.setCyclic(false);
        }

        if (mRepository.isMinDay(curYear, curMonth, curDay))
            mHourView.setCurrentItem(0, false);
    }

    private void updateFinishHours() {
        if (mFinishHourView.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();

        int minHour = mRepository.getMinHour(curYear, curMonth, curDay);
        int maxHour = mRepository.getMaxHour(curYear, curMonth, curDay);

        mHourAdapter = new NumericWheelAdapter(mContext, minHour, maxHour, DateConstants.FORMAT, mScrollerConfig.mHour);
        mHourAdapter.setConfig(mScrollerConfig);
        mFinishHourView.setViewAdapter(mHourAdapter);
        mFinishHourView.setCyclic(mScrollerConfig.cyclic);

        // 当滚动数量不足时, 需要避免循环
        if (maxHour - minHour < mScrollerConfig.mMaxLines) {
            mFinishHourView.setCyclic(false);
        }

        if (mRepository.isMinDay(curYear, curMonth, curDay))
            mFinishHourView.setCurrentItem(0, false);

    }

    /**
     * 更新分钟
     */
    private void updateMinutes() {
        if (mMinuteView.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        int curHour = getCurrentHour();

        int minMinute = mRepository.getMinMinute(curYear, curMonth, curDay, curHour);
        int maxMinute = mRepository.getMaxMinute(curYear, curMonth, curDay, curHour);

        mMinuteAdapter = new NumericWheelAdapter(mContext, minMinute, maxMinute, DateConstants.FORMAT, mScrollerConfig.mMinute);
        mMinuteAdapter.setConfig(mScrollerConfig);
        mMinuteView.setViewAdapter(mMinuteAdapter);
        mMinuteView.setCyclic(mScrollerConfig.cyclic);

        // 当滚动数量不足时, 需要避免循环
        if (maxMinute - minMinute < mScrollerConfig.mMaxLines) {
            mMinuteView.setCyclic(false);
        }

        if (mRepository.isMinHour(curYear, curMonth, curDay, curHour))
            mMinuteView.setCurrentItem(0, false);
    }

    private void updateFinishMinutes() {
        if (mFinishMinuteView.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        int curHour = getCurrentHour();

        int minMinute = mRepository.getMinMinute(curYear, curMonth, curDay, curHour);
        int maxMinute = mRepository.getMaxMinute(curYear, curMonth, curDay, curHour);

        mMinuteAdapter = new NumericWheelAdapter(mContext, minMinute, maxMinute, DateConstants.FORMAT, mScrollerConfig.mMinute);
        mMinuteAdapter.setConfig(mScrollerConfig);
        mFinishMinuteView.setViewAdapter(mMinuteAdapter);
        mFinishMinuteView.setCyclic(mScrollerConfig.cyclic);

        // 当滚动数量不足时, 需要避免循环
        if (maxMinute - minMinute < mScrollerConfig.mMaxLines) {
            mFinishMinuteView.setCyclic(false);
        }

        if (mRepository.isMinHour(curYear, curMonth, curDay, curHour))
            mFinishMinuteView.setCurrentItem(0, false);
    }

    int getCurrentYear() {
        return mYearView.getCurrentItem() + mRepository.getMinYear();
    }

    int getCurrentFinishYear() {
        return mFinishYearView.getCurrentItem() + mRepository.getMinYear();
    }

    int getCurrentMonth() {
        int curYear = getCurrentYear();
        return mMonthView.getCurrentItem() + +mRepository.getMinMonth(curYear);
    }

    int getCurrentFinishMonth() {
        int curYear = getCurrentFinishYear();
        return mFinishMonthView.getCurrentItem() + +mRepository.getMinMonth(curYear);
    }

    int getCurrentDay() {
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        return mDayView.getCurrentItem() + mRepository.getMinDay(curYear, curMonth);
    }

    int getCurrentFinishDay() {
        int curYear = getCurrentFinishYear();
        int curMonth = getCurrentFinishMonth();
        return mFinishDayView.getCurrentItem() + mRepository.getMinDay(curYear, curMonth);
    }

    int getCurrentHour() {
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        return mHourView.getCurrentItem() + mRepository.getMinHour(curYear, curMonth, curDay);
    }

    int getCurrentFinishHour() {
        int curYear = getCurrentFinishYear();
        int curMonth = getCurrentFinishMonth();
        int curDay = getCurrentFinishDay();
        return mFinishHourView.getCurrentItem() + mRepository.getMinHour(curYear, curMonth, curDay);
    }

    int getCurrentMinute() {
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        int curHour = getCurrentHour();

        return mMinuteView.getCurrentItem() + mRepository.getMinMinute(curYear, curMonth, curDay, curHour);
    }

    int getCurrentFinishMinute() {
        int curYear = getCurrentFinishYear();
        int curMonth = getCurrentFinishMonth();
        int curDay = getCurrentFinishDay();
        int curHour = getCurrentFinishHour();
        return mFinishMinuteView.getCurrentItem() + mRepository.getMinMinute(curYear, curMonth, curDay,curHour);
    }
}

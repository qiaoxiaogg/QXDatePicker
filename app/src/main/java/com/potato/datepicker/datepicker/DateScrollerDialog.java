package com.potato.datepicker.datepicker;

import android.app.Dialog;
import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.potato.datepicker.R;
import com.potato.datepicker.datepicker.config.ScrollerConfig;
import com.potato.datepicker.datepicker.data.Mode;
import com.potato.datepicker.datepicker.data.Type;
import com.potato.datepicker.datepicker.data.WheelCalendar;
import com.potato.datepicker.datepicker.listener.OnDateSetListener;

import java.util.Calendar;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * 时间选择框, 设置若干参数
 *
 * @author C.L.Wang
 */
public class DateScrollerDialog extends DialogFragment implements View.OnClickListener {
    private ScrollerConfig mScrollerConfig;
    private TimeWheel mTimeWheel;
    private long mCurrentMilliseconds;
    private long mCurrentFinishMilliseconds;

    // 实例化参数, 传入数据
    private static DateScrollerDialog newInstance(ScrollerConfig scrollerConfig) {
        DateScrollerDialog dateScrollerDialog = new DateScrollerDialog();
        dateScrollerDialog.initialize(scrollerConfig);
        return dateScrollerDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getActivity().getWindow();
        // 隐藏软键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Dialog的位置置底
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
        }
    }

    /**
     * 初始化参数, 来源{@link #newInstance(ScrollerConfig)}
     *
     * @param scrollerConfig 滚动参数
     */
    private void initialize(ScrollerConfig scrollerConfig) {
        mScrollerConfig = scrollerConfig;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.Dialog_NoTitle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true); // 后退键取消
        dialog.setCanceledOnTouchOutside(true); // 点击外面被取消
        dialog.setContentView(initView()); // 设置View
        return dialog;
    }

    /**
     * 初始化视图
     *
     * @return 当前视图
     */
    private View initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final ViewGroup nullParent = null;
        View view = inflater.inflate(R.layout.timepicker_layout, nullParent);

        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this); // 设置取消按钮
        TextView sure = (TextView) view.findViewById(R.id.tv_sure);
        sure.setOnClickListener(this); // 设置确认按钮
        TextView title = (TextView) view.findViewById(R.id.tv_title);

        // 设置顶部栏
        View toolbar = view.findViewById(R.id.toolbar); // 头部视图
        toolbar.setBackgroundResource(mScrollerConfig.mToolbarBkgColor);
        title.setText(mScrollerConfig.mTitleString); // 设置文字
        cancel.setText(mScrollerConfig.mCancelString);
        sure.setText(mScrollerConfig.mSureString);

        mTimeWheel = new TimeWheel(view, mScrollerConfig); // 设置滚动参数
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            dismiss(); // 取消
        } else if (i == R.id.tv_sure) {
            onSureClicked();
        }
    }

    /**
     * 返回当前的选择秒数
     *
     * @return 当前秒数
     */
    public long getCurrentMilliseconds() {
        if (mCurrentMilliseconds == 0)
            return System.currentTimeMillis();

        return mCurrentMilliseconds;
    }

    /**
     * 确认按钮, 回调秒数
     */
    private void onSureClicked() {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        //这里根据项目实际需求 自定义设置属性参数
        switch (mScrollerConfig.mMode){
            case DOUBLE_MODE:
                switch (mScrollerConfig.mType) {
                    case HOURS_MINS:
                        calendar.set(mTimeWheel.getCurrentYear(), mTimeWheel.getCurrentMonth() - 1, mTimeWheel.getCurrentDay(), mTimeWheel.getCurrentHour(), mTimeWheel.getCurrentMinute(), 0);
                        calendar2.set(mTimeWheel.getCurrentFinishYear(), mTimeWheel.getCurrentFinishMonth() - 1, mTimeWheel.getCurrentFinishDay(), mTimeWheel.getCurrentFinishHour(), mTimeWheel.getCurrentFinishMinute(), 59);

                        break;
                    default:
                        calendar.set(mTimeWheel.getCurrentYear(), mTimeWheel.getCurrentMonth() - 1, mTimeWheel.getCurrentDay(), 0, 0, 0);
                        calendar2.set(mTimeWheel.getCurrentFinishYear(), mTimeWheel.getCurrentFinishMonth() - 1, mTimeWheel.getCurrentFinishDay(), 23, 59, 59);
                        break;
                }
                mCurrentMilliseconds = calendar.getTimeInMillis();
                mCurrentFinishMilliseconds = calendar2.getTimeInMillis();
                if (mScrollerConfig.mCallback != null) {
                    mScrollerConfig.mCallback.onDoubleDateSet(this, mCurrentMilliseconds, mCurrentFinishMilliseconds);
                }
                break;
            case SINGLE_MODE:
                switch (mScrollerConfig.mType){
                    case ALL:
                        calendar.set(mTimeWheel.getCurrentYear(), mTimeWheel.getCurrentMonth() - 1, mTimeWheel.getCurrentDay(), mTimeWheel.getCurrentHour(), mTimeWheel.getCurrentMinute(), 0);
                        break;
                }
                mCurrentMilliseconds = calendar.getTimeInMillis();
                if (mScrollerConfig.mCallback != null) {
                    mScrollerConfig.mCallback.onSingleDateSet(this, mCurrentMilliseconds);
                }
                break;
        }
        dismiss();
    }

    @SuppressWarnings("unused")
    public static class Builder {
        ScrollerConfig mScrollerConfig;

        public Builder() {
            mScrollerConfig = new ScrollerConfig();
        }

        public Builder setMode(Mode mode) {
            mScrollerConfig.mMode = mode;
            return this;
        }

        public Builder setType(Type type) {
            mScrollerConfig.mType = type;
            return this;
        }

        public Builder setThemeColor(@ColorRes int color) {
            mScrollerConfig.mToolbarBkgColor = color;
            return this;
        }

        public Builder setCancelStringId(String left) {
            mScrollerConfig.mCancelString = left;
            return this;
        }

        public Builder setSureStringId(String right) {
            mScrollerConfig.mSureString = right;
            return this;
        }

        public Builder setTitleStringId(String title) {
            mScrollerConfig.mTitleString = title;
            return this;
        }

        public Builder setToolBarTextColor(int color) {
            mScrollerConfig.mToolBarTVColor = color;
            return this;
        }

        public Builder setWheelItemTextNormalColor(int color) {
            mScrollerConfig.mWheelTVNormalColor = color;
            return this;
        }

        public Builder setWheelItemTextSelectorColor(int color) {
            mScrollerConfig.mWheelTVSelectorColor = color;
            return this;
        }

        public Builder setWheelItemTextSize(int size) {
            mScrollerConfig.mWheelTVSize = size;
            return this;
        }

        public Builder setCyclic(boolean cyclic) {
            mScrollerConfig.cyclic = cyclic;
            return this;
        }

        public Builder setMinMilliseconds(long milliseconds) {
            mScrollerConfig.mMinCalendar = new WheelCalendar(milliseconds);
            return this;
        }

        public Builder setMaxMilliseconds(long milliseconds) {
            mScrollerConfig.mMaxCalendar = new WheelCalendar(milliseconds);
            return this;
        }

        public Builder setCurMilliseconds(long milliseconds, long milliFinishSeconds) {
            mScrollerConfig.mCurCalendar = new WheelCalendar(milliseconds);
            mScrollerConfig.mCurFinishCalendar = new WheelCalendar(milliFinishSeconds);
            return this;
        }

        public Builder setYearText(String year) {
            mScrollerConfig.mYear = year;
            return this;
        }

        public Builder setMonthText(String month) {
            mScrollerConfig.mMonth = month;
            return this;
        }

        public Builder setDayText(String day) {
            mScrollerConfig.mDay = day;
            return this;
        }

        public Builder setHourText(String hour) {
            mScrollerConfig.mHour = hour;
            return this;
        }

        public Builder setMinuteText(String minute) {
            mScrollerConfig.mMinute = minute;
            return this;
        }

        public Builder setCallback(OnDateSetListener listener) {
            mScrollerConfig.mCallback = listener;
            return this;
        }

        public DateScrollerDialog build() {
            return newInstance(mScrollerConfig);
        }
    }
}

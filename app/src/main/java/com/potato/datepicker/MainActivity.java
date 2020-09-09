package com.potato.datepicker;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.potato.datepicker.datepicker.DateScrollerDialog;
import com.potato.datepicker.datepicker.data.Mode;
import com.potato.datepicker.datepicker.data.Type;
import com.potato.datepicker.datepicker.listener.OnDateSetListener;
import com.potato.datepicker.datepicker.utils.DateUtil;

public class MainActivity extends AppCompatActivity {

    //双选
    @BindView(R.id.tv_picker_year_month_day)
    TextView mTvPickerYearMonthDay;
    @BindView(R.id.tv_picker_year_month)
    TextView mTvPickerYearMonth;
    @BindView(R.id.tv_picker_hour_minute)
    TextView mTvPickerHourMinute;

    //单选
    @BindView(R.id.tv_picker_year_month_day_hour_minute)
    TextView mTvPickerYearMonthDAYHourMinute;

    //记录上次筛选时间
    private long mLastStartTime = System.currentTimeMillis();
    private long mLastEndTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_picker_year_month_day, R.id.ll_picker_year_month, R.id.ll_picker_hour_minute,R.id.ll_picker_year_month_day_hour_minute})
    public void onUClick(View v) {
        switch (v.getId()) {
            case R.id.ll_picker_year_month_day: {
                DateScrollerDialog dialog = new DateScrollerDialog.Builder()
                        .setMode(Mode.DOUBLE_MODE)
                        .setType(Type.YEAR_MONTH_DAY)
                        .setTitleStringId("请选择日期")
                        .setMinMilliseconds(System.currentTimeMillis() - DateUtil.HUNDRED_YEARS)
                        .setMaxMilliseconds(System.currentTimeMillis() + DateUtil.HUNDRED_YEARS)
                        .setCurMilliseconds(mLastStartTime, mLastEndTime)
                        .setCallback(new OnDateSetListener() {
                            @Override
                            public void onDoubleDateSet(DateScrollerDialog timePickerView, long milliseconds, long milliFinishSeconds) {
                                mLastStartTime = milliseconds;
                                mLastEndTime = milliFinishSeconds;
                                String mStartTime = DateUtil.millis2String(milliseconds, DateUtil.DATE_SHORT_PATTERN);
                                String mEndTime = DateUtil.millis2String(milliFinishSeconds, DateUtil.DATE_SHORT_PATTERN);
                                String result = String.format("%s-%s", mStartTime, mEndTime);
                                mTvPickerYearMonthDay.setText(result);
                            }

                            @Override
                            public void onSingleDateSet(DateScrollerDialog timePickerView, long selectMillSeconds) {

                            }
                        }).build();
                if (dialog != null) {
                    if (!dialog.isAdded()) {
                        dialog.show(getSupportFragmentManager(), "DatePickerDialog");
                    }
                }
                break;
            }
            case R.id.ll_picker_year_month: {
                DateScrollerDialog dialog = new DateScrollerDialog.Builder()
                        .setMode(Mode.DOUBLE_MODE)
                        .setType(Type.YEAR_MONTH)
                        .setTitleStringId("请选择日期")
                        .setMinMilliseconds(System.currentTimeMillis() - DateUtil.HUNDRED_YEARS)
                        .setMaxMilliseconds(System.currentTimeMillis() + DateUtil.HUNDRED_YEARS)
                        .setCurMilliseconds(mLastStartTime, mLastEndTime)
                        .setCallback(new OnDateSetListener() {
                            @Override
                            public void onDoubleDateSet(DateScrollerDialog timePickerView, long milliseconds, long milliFinishSeconds) {
                                mLastStartTime = milliseconds;
                                mLastEndTime = milliFinishSeconds;
                                String mStartTime = DateUtil.millis2String(milliseconds, DateUtil.DATE_YEAR_MONTH);
                                String mEndTime = DateUtil.millis2String(milliFinishSeconds, DateUtil.DATE_YEAR_MONTH);
                                String result = String.format("%s-%s", mStartTime, mEndTime);
                                mTvPickerYearMonth.setText(result);
                            }

                            @Override
                            public void onSingleDateSet(DateScrollerDialog timePickerView, long selectMillSeconds) {

                            }
                        })
                        .build();
                if (dialog != null) {
                    if (!dialog.isAdded()) {
                        dialog.show(getSupportFragmentManager(), "DatePickerDialog");
                    }
                }
                break;
            }
            case R.id.ll_picker_hour_minute: {
                DateScrollerDialog dialog = new DateScrollerDialog.Builder()
                        .setMode(Mode.DOUBLE_MODE)
                        .setType(Type.HOURS_MINS)
                        .setTitleStringId("请选择日期")
                        .setMinMilliseconds(System.currentTimeMillis() - DateUtil.HUNDRED_YEARS)
                        .setMaxMilliseconds(System.currentTimeMillis() + DateUtil.HUNDRED_YEARS)
                        .setCurMilliseconds(mLastStartTime, mLastEndTime)
                        .setCallback(new OnDateSetListener() {
                            @Override
                            public void onDoubleDateSet(DateScrollerDialog timePickerView, long milliseconds, long milliFinishSeconds) {
                                mLastStartTime = milliseconds;
                                mLastEndTime = milliFinishSeconds;
                                String mStartTime = DateUtil.millis2String(milliseconds, DateUtil.DATE_HOUR_AND_MINUTE_PATTERN);
                                String mEndTime = DateUtil.millis2String(milliFinishSeconds, DateUtil.DATE_HOUR_AND_MINUTE_PATTERN);
                                String result = String.format("%s-%s", mStartTime, mEndTime);
                                mTvPickerHourMinute.setText(result);
                            }

                            @Override
                            public void onSingleDateSet(DateScrollerDialog timePickerView, long selectMillSeconds) {

                            }
                        })
                        .build();
                if (dialog != null) {
                    if (!dialog.isAdded()) {
                        dialog.show(getSupportFragmentManager(), "DatePickerDialog");
                    }
                }
                break;
            }
            case R.id.ll_picker_year_month_day_hour_minute: {
                DateScrollerDialog dialog = new DateScrollerDialog.Builder()
                        .setMode(Mode.SINGLE_MODE)
                        .setType(Type.ALL)
                        .setTitleStringId("请选择日期")
                        .setMinMilliseconds(System.currentTimeMillis() - DateUtil.HUNDRED_YEARS)
                        .setMaxMilliseconds(System.currentTimeMillis() + DateUtil.HUNDRED_YEARS)
                        .setCurMilliseconds(mLastStartTime, mLastEndTime)
                        .setCallback(new OnDateSetListener() {
                            @Override
                            public void onDoubleDateSet(DateScrollerDialog timePickerView, long milliseconds, long milliFinishSeconds) {

                            }

                            @Override
                            public void onSingleDateSet(DateScrollerDialog timePickerView, long selectMillSeconds) {
                                String result = DateUtil.millis2String(selectMillSeconds, DateUtil.DATE_MINUTE_PATTERN);
                                mTvPickerYearMonthDAYHourMinute.setText(result);
                            }
                        })
                        .build();
                if (dialog != null) {
                    if (!dialog.isAdded()) {
                        dialog.show(getSupportFragmentManager(), "DatePickerDialog");
                    }
                }
                break;
            }
        }
    }
}

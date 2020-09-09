package com.potato.datepicker.datepicker.listener;


import com.potato.datepicker.datepicker.DateScrollerDialog;

/**
 * 日期设置的监听器
 *
 * @author C.L. Wang
 */
public interface OnDateSetListener {
    /**
     * 双选日期范围 筛选回调
     * @param timePickerView
     * @param milliseconds
     * @param milliFinishSeconds
     */
    void onDoubleDateSet(DateScrollerDialog timePickerView, long milliseconds, long milliFinishSeconds);


    /**
     * 单选日期 回调
     * @param timePickerView
     * @param selectMillSeconds
     */
    void onSingleDateSet(DateScrollerDialog timePickerView, long selectMillSeconds);

}

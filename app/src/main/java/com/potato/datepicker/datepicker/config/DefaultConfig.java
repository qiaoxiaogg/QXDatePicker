package com.potato.datepicker.datepicker.config;


import com.potato.datepicker.R;
import com.potato.datepicker.datepicker.data.Mode;
import com.potato.datepicker.datepicker.data.Type;

/**
 * 默认配置, 类型, 颜色, 文字
 *
 * @author C.L. Wang
 */
public class DefaultConfig {

    public static final Mode MODE = Mode.DOUBLE_MODE;//单选或双选模式

    public static final Type TYPE = Type.ALL; // 全部类型


    public static final int TOOLBAR_BKG_COLOR = R.color.toolbar_bg; // 背景颜色
    public static final int ITEM_SELECTOR_LINE = R.color.item_selector_line; // 选择线颜色
    public static final int ITEM_SELECTOR_RECT = R.color.item_selector_rect; // 选择框颜色


    public static final int TOOLBAR_TV_COLOR = 0xFFFFFFFF;
    public static final int TV_NORMAL_COLOR = R.color.timepicker_dialog_bg; // 默认文字颜色
    public static final int TV_SELECTOR_COLOR = R.color.toolbar_text_cancel; // 选中文字颜色

    public static final int TV_SIZE = 12; // 尺寸

    public static final boolean CYCLIC = true; // 循环

    public static final int MAX_LINE = 5; // 最大行数, 依据控件样式

    // 默认文案
    public static String CANCEL = "取消";
    public static String SURE = "确定";
    public static String TITLE = "请选择日期";
    public static String YEAR = "年";
    public static String MONTH = "月";
    public static String DAY = "日";
    public static String HOUR = "时";
    public static String MINUTE = "分";
}

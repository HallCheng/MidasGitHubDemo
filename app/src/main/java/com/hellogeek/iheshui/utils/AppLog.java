package com.hellogeek.iheshui.utils;

import android.util.Log;

/**
 * Desc:日志打印工具类
 * <p>
 * Author: AnYaBo
 * Date: 2020/1/8
 * Copyright: Copyright (c) 2016-2022
 * Company: @小牛科技
 * Email:anyabo@xiaoniu.com
 * Update Comments:
 *
 * @author anyabo
 */
public class AppLog {
    private static final String TAG = "midas_demo";
    /**
     * 日志开关
     */
    private static final boolean mLogClose = false;

    /**
     * 统一日志输出
     * @param message 日志消息
     */
    public static void e(String message){
        if (mLogClose) {
            return;
        }
        Log.e(TAG,message);
    }

}

package com.hellogeek.iheshui.utils;

import android.content.Context;

/**
 * Desc:设备工具类
 * <p>
 * Author: AnYaBo
 * Date: 2020/3/7
 * Copyright: Copyright (c) 2016-2022
 * Company: @小牛科技
 * Email:anyabo@xiaoniu.com
 * Update Comments:
 *
 * @author anyabo
 */
public class DeviceUtils {

    /**
     * 密度转像素
     * @param dipValue  密度值
     * @return 像素值
     */
    public static int dp2px(Context context, float dipValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        } else {
            return 0;
        }
    }


}

package com.hellogeek.iheshui;

import android.content.Context;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.xnad.sdk.MidasAdSdk;
import com.xnad.sdk.ad.entity.ScreenOrientation;
import com.xnad.sdk.config.AdConfig;

/**
 * Desc:应用入口
 * <p>
 * Author: AnYaBo
 * Date: 2020/3/26
 * Copyright: Copyright (c) 2016-2022
 * Company: @小牛科技
 * Email:anyabo@xiaoniu.com
 * Update Comments:
 *
 * @author anyabo
 */
public class YourApplication extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AdConfig configBuild = new AdConfig.Build()
                .setAppId("320001")//应用ID
                .setProductId("32")//大数据业务线ID
                .setChannel("yingyongbao")//渠道名
                .setServerUrl("http://testaidataprobe2.51huihuahua.com/apis/v1/dataprobe2/dhs")//大数据埋点地址
                .setIsFormal(false)//是否是正式环境 true 线上环境
                .setScreenOrientation(ScreenOrientation.VERTICAL)//设置屏幕方向
                .setCsjAppId("5037925")//穿山甲appId
                .setIsReportIMEI(true)//是否上报IMEI，外部对接需填写true
                .build();
        //初始化广告SDK
        MidasAdSdk.init(this, configBuild);
    }

}

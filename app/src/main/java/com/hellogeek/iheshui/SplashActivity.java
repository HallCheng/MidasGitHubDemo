package com.hellogeek.iheshui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xnad.sdk.MidasAdSdk;
import com.xnad.sdk.ad.entity.AdInfo;
import com.xnad.sdk.ad.listener.AbsAdCallBack;
import com.xnad.sdk.config.AdParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:开屏页
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
public class SplashActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 1024;
    private FrameLayout mAdContainerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAdContainerLayout = findViewById(R.id.splash_ad_container);
        //权限检查
        checkAndRequestPermission();
    }

    private void checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> lackedPermission = new ArrayList<>();

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                lackedPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                lackedPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            // 如果需要的权限都已经有了，那么直接调用SDK
            if (lackedPermission.size() == 0) {
                doSplashData();
            } else {
                // 否则，建议请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限
                String[] requestPermissions = new String[lackedPermission.size()];
                lackedPermission.toArray(requestPermissions);
                requestPermissions(requestPermissions, REQUEST_PERMISSION_CODE);
            }
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE && hasAllPermissionsGranted(grantResults)) {
            //设置imei 获取到权限后，必须设置一次
            MidasAdSdk.setImei();
            doSplashData();
        } else {
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    //加载开屏广告
    public void doSplashData(){
        AdParameter adParameter = new AdParameter.Builder(this, "adpos_8641978201")
                //设置填充父布局
                .setViewContainer(mAdContainerLayout)
                .build();
        MidasAdSdk.getAdsManger().loadAd(adParameter, new AbsAdCallBack() {
            @Override
            public void onAdClose(AdInfo adInfo) {
                goToMainActivity();
            }
            @Override
            public void onShowError(int errorCode, String errorMsg) {
                goToMainActivity();
            }
        });
    }


    private boolean mCanJump;

    @Override
    protected void onResume() {
        super.onResume();
        if (mCanJump){
            goToMainActivity();
        }
        mCanJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCanJump = false;
    }

    public void goToMainActivity(){
        if (mCanJump){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }else {
            mCanJump = true;
        }
    }

}

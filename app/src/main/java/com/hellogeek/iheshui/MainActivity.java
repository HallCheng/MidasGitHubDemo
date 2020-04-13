package com.hellogeek.iheshui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hellogeek.iheshui.utils.AppLog;
import com.xnad.sdk.MidasAdSdk;
import com.xnad.sdk.ad.entity.AdInfo;
import com.xnad.sdk.ad.entity.AdType;
import com.xnad.sdk.ad.listener.AbsAdCallBack;
import com.xnad.sdk.config.AdParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //广告容器
    private FrameLayout mViewContainer;
    //广告位ID
    private String mLoadAdPositionId = "adpos_8641978201";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioButton splashRadioButton = findViewById(R.id.splash_type);
        splashRadioButton.setChecked(true);
        FrameLayout splashAdViewContainer = findViewById(R.id.splash_ad_view_container);
        FrameLayout normalAdViewContainer = findViewById(R.id.normal_ad_view_container);
        FrameLayout buoyAdViewContainer = findViewById(R.id.buoy_ad_view_container);
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        mViewContainer = splashAdViewContainer;
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            switch (checkedId){
                case R.id.splash_type:
                    mViewContainer = splashAdViewContainer;
                    mLoadAdPositionId = "adpos_8641978201";
                    break;
                case R.id.banner_type:
                    mViewContainer = normalAdViewContainer;
                    mLoadAdPositionId = "adpos_2335893411";
                    break;
                case R.id.insert_type:
                    mLoadAdPositionId = "adpos_3712248521";
                    break;
                case R.id.full_screen_type:
                    mLoadAdPositionId = "adpos_2644140531";
                    break;
                case R.id.reward_type:
                    mLoadAdPositionId = "adpos_5090340441";
                    break;
                case R.id.native_template_type:
                    mViewContainer = normalAdViewContainer;
                    mLoadAdPositionId = "adpos_6165143951";
                    break;
                case R.id.buoy_type:
                    mViewContainer = buoyAdViewContainer;
                    mLoadAdPositionId = "adpos_4301618571";
                    break;
            }
        });
        //展示广告
        findViewById(R.id.load_ad_btn).setOnClickListener(view -> {
            //加载展示广告
            //上下文、广告位置ID
            AdParameter adParameter = new AdParameter.Builder(this,mLoadAdPositionId)
                    //设置填充父布局
                    .setViewContainer(mViewContainer)
                    .build();
            MidasAdSdk.getAdsManger().loadAd(adParameter,mAbsAdCallBack);
        });
        //预加载广告
        findViewById(R.id.prepare_load_btn).setOnClickListener(view -> {
            //预加载广告
            List<String> preLoadList = new ArrayList<>();
            preLoadList.add(mLoadAdPositionId);
            //...
            MidasAdSdk.getAdsManger().preLoad(MainActivity.this,preLoadList);
        });
        findViewById(R.id.locker_set_btn).setOnClickListener(view -> {
            MidasAdSdk.configLockAdParameter(mLoadAdPositionId,R.drawable.common_lock_bg);
            Toast.makeText(MainActivity.this,"锁屏广告参数设置成功",Toast.LENGTH_SHORT).show();
        });
        //ready询问接口
        findViewById(R.id.ask_is_ready_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.ask_ready_dialog);
                TextView loadTv = dialog.findViewById(R.id.load_ad_btn);
                ImageView loadingIV = dialog.findViewById(R.id.loading_iv);
                loadTv.setEnabled(false);
                loadingIV.setVisibility(View.VISIBLE);
                MidasAdSdk.getAdsManger().askIsReady(MainActivity.this, mLoadAdPositionId, isReadySuccess -> {
                    loadingIV.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, isReadySuccess ? "准备成功了" : "准备失败了", Toast.LENGTH_SHORT).show();
                    if (isReadySuccess) {
                        loadTv.setEnabled(true);
                    } else {
                        loadTv.setEnabled(false);
                    }
                });
                dialog.show();
            }
        });
        //自渲染列表
        findViewById(R.id.self_render_list_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),SelfRenderRecyclerActivity.class));
            }
        });
    }

    private AbsAdCallBack mAbsAdCallBack = new AbsAdCallBack() {
        /**
         * 广告显示前回调
         * @param adInfo    广告信息实体
         */
        @Override
        public void onReadyToShow(AdInfo adInfo) {
            super.onReadyToShow(adInfo);
        }

        /**
         * 广告显示错误
         * @param errorCode 错误码
         * @param errorMsg  错误信息
         */
        @Override
        public void onShowError(int errorCode, String errorMsg) {
            AppLog.e("错误" + errorMsg);
        }
        /**
         * 广告展示成功
         * @param adInfo    广告信息实体
         */
        @Override
        public void onAdShow(AdInfo adInfo) {
            AppLog.e("广告被展示");
        }
        /**
         * 广告被点击
         * @param adInfo    广告信息实体
         */
        @Override
        public void onAdClicked(AdInfo adInfo) {
            AppLog.e("广告被点击");
        }
        /**
         * 广告关闭
         * @param adInfo    广告信息实体
         */
        @Override
        public void onAdClose(AdInfo adInfo) {
            AppLog.e("广告关闭");
            //这里只是示例调试使用，外部可以看情况处理
            if (TextUtils.equals(adInfo.mAdType, AdType.SPLASH.adType)){
                mViewContainer.removeAllViews();
            }
        }

        @Override
        public void onAdVideoComplete(AdInfo adInfo) {
            super.onAdVideoComplete(adInfo);
        }

        /**
         * 广告激励事件
         * @param adInfo    广告信息实体
         */
        @Override
        public void onVideoRewardEvent(AdInfo adInfo) {
            AppLog.e("广告激励事件");
        }
    };

}

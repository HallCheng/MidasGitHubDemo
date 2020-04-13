package com.hellogeek.iheshui;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.hellogeek.iheshui.utils.DeviceUtils;
import com.hellogeek.iheshui.view.ILoadMoreListener;
import com.hellogeek.iheshui.view.LoadMoreRecyclerView;
import com.hellogeek.iheshui.view.LoadMoreView;
import com.xnad.sdk.MidasAdSdk;
import com.xnad.sdk.ad.entity.AdInfo;
import com.xnad.sdk.ad.entity.AdPatternType;
import com.xnad.sdk.ad.entity.SelfRenderBean;
import com.xnad.sdk.ad.listener.AbsAdCallBack;
import com.xnad.sdk.ad.widget.AdLogoImageView;
import com.xnad.sdk.config.AdParameter;
import com.xnad.sdk.config.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:原生信息流页面
 * <p>
 * Author: AnYaBo
 * Date: 2020/3/6
 * Copyright: Copyright (c) 2016-2022
 * Company: @小牛科技
 * Email:anyabo@xiaoniu.com
 * Update Comments:
 *
 * @author anyabo
 */
public class SelfRenderRecyclerActivity extends AppCompatActivity {
    private static final int LIST_ITEM_COUNT = 30;

    private List<SelfRenderBean> mDataList = new ArrayList<>();

    private LoadMoreRecyclerView mListView;

    private DataListAdapter mDataListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_render_list);
        mListView = findViewById(R.id.load_more_list_view);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });

        mDataListAdapter = new DataListAdapter(this, mDataList);
        mListView.setAdapter(mDataListAdapter);

        loadAdData();
        mListView.setLoadMoreListener(new ILoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadAdData();
            }
        });
    }

    private void loadAdData() {
        //上下文、广告位置ID
        AdParameter adParameter = new AdParameter.Builder(this, "adpos_4369719761")
                //请求列表条数 最大3条
                .setRequestListCount(3)
                //是否自渲染显示缓存
                .setNeedSelfRenderCache(false)
                .build();
        MidasAdSdk.getAdsManger().loadAd(adParameter, new AbsAdCallBack() {
            @Override
            public void callBackRenderList(AdInfo adInfo, List<SelfRenderBean> renderList) {
                for (int i = 0; i < LIST_ITEM_COUNT; i++) {
                    mDataList.add(null);
                }
                if (renderList != null && renderList.size() > 0){
                    for (SelfRenderBean renderBean: renderList) {
                        int random = (int) (Math.random() * LIST_ITEM_COUNT) + mDataList.size() - LIST_ITEM_COUNT;
                        mDataList.set(random, renderBean);
                    }
                }
                mDataListAdapter.notifyDataSetChanged();
                mListView.setLoadingFinish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //恢复资源
        for (SelfRenderBean selfRenderBean : mDataList) {
            if (selfRenderBean != null){
                selfRenderBean.onResume();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        for (SelfRenderBean selfRenderBean : mDataList) {
            if (selfRenderBean != null){
                selfRenderBean.onDestroy();
            }
        }
    }

    private static class  DataListAdapter extends RecyclerView.Adapter {
        private static final int FOOTER_VIEW_COUNT = 1;

        private static final int ITEM_VIEW_TYPE_LOAD_MORE = -1;
        //正常item
        private static final int ITEM_VIEW_TYPE_NORMAL = 0;
        //穿山甲
        //1.视频样式
        private static final int CSJ_ITEM_VIEW_TYPE_AD_VIDEO = 1;
        //2.大图样式
        private static final int CSJ_ITEM_VIEW_TYPE_AD_BIG_IMG = 2;
        //3.大图样式
        private static final int CSJ_ITEM_VIEW_TYPE_AD_SMALL_IMG = 3;
        //4.三图样式
        private static final int CSJ_ITEM_VIEW_TYPE_AD_THREE_IMG = 4;

        //优量汇
        //1.视频样式
        private static final int YLH_ITEM_VIEW_TYPE_AD_VIDEO = 5;
        //2.大图样式
        private static final int YLH_ITEM_VIEW_TYPE_AD_BIG_IMG = 6;
        //3.大图样式
        private static final int YLH_ITEM_VIEW_TYPE_AD_SMALL_IMG = 7;
        //4.三图样式
        private static final int YLH_ITEM_VIEW_TYPE_AD_THREE_IMG = 8;

        //MobVista
        //1.视频样式
        private static final int MGT_ITEM_VIEW_TYPE_AD_VIDEO = 9;

        //InMoBi
        //1.视频样式
        private static final int IMB_ITEM_VIEW_TYPE_AD_VIDEO = 10;

        private List<SelfRenderBean> mData;
        private Context mContext;
        private RequestManager mGlideRequestManager;

        public DataListAdapter(Context context, List<SelfRenderBean> data) {
            this.mContext = context;
            this.mData = data;
            mGlideRequestManager = Glide.with(mContext);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case ITEM_VIEW_TYPE_LOAD_MORE:
                    return new LoadMoreViewHolder(new LoadMoreView(mContext));
                case YLH_ITEM_VIEW_TYPE_AD_VIDEO:
                    return new VideoAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.ylh_item_ad_video, parent, false));
                case CSJ_ITEM_VIEW_TYPE_AD_VIDEO:
                    return new VideoAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.csj_item_ad_video, parent, false));
                case MGT_ITEM_VIEW_TYPE_AD_VIDEO:
                    return new VideoAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.mgt_item_ad_video, parent, false));
                case IMB_ITEM_VIEW_TYPE_AD_VIDEO:
                    return new VideoAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.imb_item_ad_video, parent, false));
                case YLH_ITEM_VIEW_TYPE_AD_BIG_IMG:
                    return new BigImgAdViewHolderHolder(LayoutInflater.from(mContext).inflate(R.layout.ylh_item_ad_big_img, parent, false));
                case CSJ_ITEM_VIEW_TYPE_AD_BIG_IMG:
                    return new BigImgAdViewHolderHolder(LayoutInflater.from(mContext).inflate(R.layout.csj_item_ad_big_img, parent, false));
                case YLH_ITEM_VIEW_TYPE_AD_SMALL_IMG:
                    return new SmallImgAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.ylh_item_small_img, parent, false));
                case CSJ_ITEM_VIEW_TYPE_AD_SMALL_IMG:
                    return new SmallImgAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.csj_item_ad_small_img, parent, false));
                case YLH_ITEM_VIEW_TYPE_AD_THREE_IMG:
                    return new ThreeImgAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.ylh_item_ad_three_img, parent, false));
                case CSJ_ITEM_VIEW_TYPE_AD_THREE_IMG:
                    return new ThreeImgAdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.csj_item_ad_three_img, parent, false));
                default:
                    return new NormalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_normal, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Context context = holder.itemView.getContext();
            SelfRenderBean selfRenderBean;
            if (holder instanceof LoadMoreViewHolder){
                LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;
            }else if(holder instanceof NormalViewHolder){
                NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
                normalViewHolder.bindView(position);
            }else if(holder instanceof VideoAdViewHolder){
                selfRenderBean = mData.get(position);
                VideoAdViewHolder adVideoHolder = (VideoAdViewHolder) holder;
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DeviceUtils.dp2px(context,48),
                        DeviceUtils.dp2px(context,20));
                params.gravity = Gravity.TOP | Gravity.RIGHT;
                params.topMargin = DeviceUtils.dp2px(context,256);
                params.rightMargin = DeviceUtils.dp2px(context,12);

                if (adVideoHolder.videoView != null) {
                    View video = selfRenderBean.getVideoView(adVideoHolder.videoView);
                    if (video != null) {
                        if (video.getParent() == null) {
                            adVideoHolder.videoView.removeAllViews();
                            adVideoHolder.videoView.addView(video);
                        }
                    }
                }
                bindData(adVideoHolder, selfRenderBean, params);
            }else if(holder instanceof BigImgAdViewHolderHolder){
                selfRenderBean = mData.get(position);
                BigImgAdViewHolderHolder bigImgAdViewHolderHolder = (BigImgAdViewHolderHolder) holder;

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DeviceUtils.dp2px(context, 48),
                        DeviceUtils.dp2px(context, 20));
                params.gravity = Gravity.TOP | Gravity.RIGHT;
                params.topMargin = DeviceUtils.dp2px(context,256);
                params.rightMargin = DeviceUtils.dp2px(context,12);
                mGlideRequestManager.load(selfRenderBean.getImgUrl()).centerCrop().into(bigImgAdViewHolderHolder.bigImgIv);
                bindData(bigImgAdViewHolderHolder, selfRenderBean, params);
            }else if(holder instanceof SmallImgAdViewHolder){
                selfRenderBean = mData.get(position);
                SmallImgAdViewHolder smallImgAdViewHolder = (SmallImgAdViewHolder) holder;
                mGlideRequestManager.load(selfRenderBean.getImgUrl()).into(smallImgAdViewHolder.smallImgIv);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DeviceUtils.dp2px(context, 48),
                        DeviceUtils.dp2px(context, 20));
                params.gravity = Gravity.TOP | Gravity.LEFT;
                params.leftMargin = DeviceUtils.dp2px(context,84);
                params.topMargin = DeviceUtils.dp2px(context,72);
                bindData(smallImgAdViewHolder, selfRenderBean, params);
            }else if(holder instanceof ThreeImgAdViewHolder){
                selfRenderBean = mData.get(position);
                ThreeImgAdViewHolder threeImgAdViewHolder = (ThreeImgAdViewHolder) holder;
                mGlideRequestManager.load(selfRenderBean.getImgList().get(0)).into(threeImgAdViewHolder.image1);
                mGlideRequestManager.load(selfRenderBean.getImgList().get(1)).into(threeImgAdViewHolder.image2);
                mGlideRequestManager.load(selfRenderBean.getImgList().get(2)).into(threeImgAdViewHolder.image3);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(DeviceUtils.dp2px(context, 48),
                        DeviceUtils.dp2px(context, 20));
                params.gravity = Gravity.TOP | Gravity.RIGHT;
                params.topMargin = DeviceUtils.dp2px(context,168);
                params.rightMargin = DeviceUtils.dp2px(context,12);
                bindData(threeImgAdViewHolder, selfRenderBean, params);
            }
        }

        @Override
        public int getItemCount() {
            int count = mData == null ? 0 : mData.size();
            return count + FOOTER_VIEW_COUNT;
        }

        private static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;
            ProgressBar mProgressBar;

            public LoadMoreViewHolder(View itemView) {
                super(itemView);
                itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
                mTextView = itemView.findViewById(R.id.tv_load_more_tip);
                mProgressBar = itemView.findViewById(R.id.pb_load_more_progress);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mData != null){
                int count = mData.size();
                if (position >= count) {
                    return ITEM_VIEW_TYPE_LOAD_MORE;
                } else {
                    SelfRenderBean selfRenderBean = mData.get(position);
                    if (selfRenderBean == null) {
                        return ITEM_VIEW_TYPE_NORMAL;
                    } else if (selfRenderBean.getAdPatternType() == AdPatternType.VIDEO_TYPE) {
                        switch (selfRenderBean.getAdFrom()){
                            case Constants.AD_SOURCE_FROM_YLH:
                                return YLH_ITEM_VIEW_TYPE_AD_VIDEO;
                            case Constants.AD_SOURCE_FROM_CSJ:
                                return CSJ_ITEM_VIEW_TYPE_AD_VIDEO;
                            case Constants.AD_SOURCE_FROM_MGT:
                                return MGT_ITEM_VIEW_TYPE_AD_VIDEO;
                            case Constants.AD_SOURCE_FROM_IMB:
                                return IMB_ITEM_VIEW_TYPE_AD_VIDEO;
                        }
                    } else if (selfRenderBean.getAdPatternType() == AdPatternType.BIG_IMG_TYPE) {
                        switch (selfRenderBean.getAdFrom()){
                            case Constants.AD_SOURCE_FROM_YLH:
                                return YLH_ITEM_VIEW_TYPE_AD_BIG_IMG;
                            case Constants.AD_SOURCE_FROM_CSJ:
                                return CSJ_ITEM_VIEW_TYPE_AD_BIG_IMG;
                            case Constants.AD_SOURCE_FROM_MGT:

                        }
                    } else if (selfRenderBean.getAdPatternType() == AdPatternType.SMALL_IMG_TYPE) {
                        switch (selfRenderBean.getAdFrom()){
                            case Constants.AD_SOURCE_FROM_YLH:
                                return YLH_ITEM_VIEW_TYPE_AD_SMALL_IMG;
                            case Constants.AD_SOURCE_FROM_CSJ:
                                return CSJ_ITEM_VIEW_TYPE_AD_SMALL_IMG;
                            case Constants.AD_SOURCE_FROM_MGT:

                        }
                    } else if (selfRenderBean.getAdPatternType() == AdPatternType.THREE_IMG_TYPE) {
                        switch (selfRenderBean.getAdFrom()){
                            case Constants.AD_SOURCE_FROM_YLH:
                                return YLH_ITEM_VIEW_TYPE_AD_THREE_IMG;
                            case Constants.AD_SOURCE_FROM_CSJ:
                                return CSJ_ITEM_VIEW_TYPE_AD_THREE_IMG;
                            case Constants.AD_SOURCE_FROM_MGT:

                        }
                    }else {
                        return ITEM_VIEW_TYPE_NORMAL;
                    }
                }
            }

            return super.getItemViewType(position);
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
            //noinspection unchecked
            super.onViewAttachedToWindow(holder);
        }

        private static class NormalViewHolder extends RecyclerView.ViewHolder{
            TextView idleTv;
            public NormalViewHolder(View convertView){
                super(convertView);
                idleTv = convertView.findViewById(R.id.text_idle);
            }
            public void bindView(int position){
                idleTv.setText("ListView item " + position);
            }
        }

        private static class ThreeImgAdViewHolder extends AdViewHolder{
            ImageView image1;
            ImageView image2;
            ImageView image3;

            public ThreeImgAdViewHolder(View convertView){
                super(convertView);
                image1 = convertView.findViewById(R.id.img_1);
                image2 = convertView.findViewById(R.id.img_2);
                image3 = convertView.findViewById(R.id.img_3);
            }
        }


        private static class SmallImgAdViewHolder extends AdViewHolder{
            ImageView smallImgIv;

            public SmallImgAdViewHolder(View convertView){
                super(convertView);
                smallImgIv = convertView.findViewById(R.id.small_img);
            }
        }

        private static class BigImgAdViewHolderHolder extends AdViewHolder{
            ImageView bigImgIv;

            public BigImgAdViewHolderHolder(View convertView){
                super(convertView);
                bigImgIv = convertView.findViewById(R.id.big_img_iv);
            }
        }

        private static class VideoAdViewHolder extends AdViewHolder{
            FrameLayout videoView;

            public VideoAdViewHolder(@NonNull View itemView) {
                super(itemView);
                videoView = itemView.findViewById(R.id.video_view);
            }

        }

        private static class AdViewHolder extends RecyclerView.ViewHolder {
            TextView adTitleTv;
            TextView adDescTv;
            TextView adSourceTv;
            ImageView adIconIv;
            AdLogoImageView adLogoIv;
            TextView creativeView;

            public AdViewHolder(@NonNull View itemView) {
                super(itemView);
                adTitleTv = itemView.findViewById(R.id.ad_title_tv);
                adDescTv = itemView.findViewById(R.id.ad_desc_tv);
                adSourceTv = itemView.findViewById(R.id.ad_source_tv);
                adIconIv = itemView.findViewById(R.id.ad_icon_iv);
                adLogoIv = itemView.findViewById(R.id.ad_logo_iv);
                creativeView = itemView.findViewById(R.id.creative_view);
            }
        }

        private void bindData(AdViewHolder adViewHolder, SelfRenderBean selfRenderBean, FrameLayout.LayoutParams layoutParams) {
            List<View> clickViewList = new ArrayList<>();
            ViewGroup itemViewGroup = (ViewGroup) adViewHolder.itemView;
            if (itemViewGroup != null && itemViewGroup.getChildCount() > 0){
                for (int i = 0; i < itemViewGroup.getChildCount(); i++) {
                    clickViewList.add(itemViewGroup.getChildAt(i));
                }
            }

            List<View> creativeViewList = new ArrayList<>();
            creativeViewList.add(adViewHolder.itemView);
            selfRenderBean.registerViewForInteraction((ViewGroup) adViewHolder.itemView, clickViewList,
                    creativeViewList, layoutParams, new AbsAdCallBack() {
                        //点击、显示、下载回调
                    });
            adViewHolder.adTitleTv.setText(selfRenderBean.getTitle());
            adViewHolder.adDescTv.setText(selfRenderBean.getDescription());
            adViewHolder.adSourceTv.setText(selfRenderBean.getSource());
            mGlideRequestManager.load(selfRenderBean.getIconUrl()).into(adViewHolder.adIconIv);
            if (selfRenderBean.getAdLogo() != null){
                adViewHolder.adLogoIv.setVisibility(View.VISIBLE);
                adViewHolder.adLogoIv.setImageBitmap(selfRenderBean.getAdLogo());
            }else {
                if (selfRenderBean.getCampaign() != null){
                    adViewHolder.adLogoIv.setVisibility(View.VISIBLE);
                    adViewHolder.adLogoIv.setCampaign(selfRenderBean.getCampaign());
                }else {
                    adViewHolder.adLogoIv.setVisibility(View.INVISIBLE);
                }
            }
            adViewHolder.creativeView.setText(selfRenderBean.getButtonText());
        }

    }


}

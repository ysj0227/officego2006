package com.officego.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.officego.R;
import com.officego.application.MyApplication;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.common.model.ShareBean;
import com.officego.utils.Util;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    /**
     * 关于微信分享成功的回调：在这里着重说明必须使用微信提供的 WXEntryActivity类
     * 否则分享回调不成功
     */
    private final String TAG = this.getClass().getSimpleName();
    private boolean isFirstOpen;
    private boolean isGoBaseResp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //分享设置透明布局
        setContentView(R.layout.activity_wx_transparent);
        try {
            Constants.WXapi.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        int shareType = intent.getIntExtra(Constants.WX_TYPE, 0);
        ShareBean bean = (ShareBean) intent.getSerializableExtra(Constants.WX_DATA);
        shareWX(shareType, bean);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstOpen) {
            finish();
        }
        isFirstOpen = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Constants.WXapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        isGoBaseResp = true;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
//                result = R.string.errcode_success;
//                Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
                this.finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                this.finish();
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.errcode_unsupported;
                Toast.makeText(WXEntryActivity.this, result, Toast.LENGTH_LONG).show();
                this.finish();
                break;
            default:
                this.finish();
                break;
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void shareWX(int mTargetScene, ShareBean bean) {
        if (bean == null) {
            this.finish();
            return;
        }
        String webpageUrl;
//        楼盘详情分享回调链接：https://m.officego.com/lessee/housesDetail.html?buildingId=7154
//        楼盘下的房源分享回调链接：https://m.officego.com/lessee/detail.html?buildingId=7154&houseId=9519
//        网点分享回调链接：https://m.officego.com/lessee/housesDetail2.html?buildingId=7154
//        网点下的房源分享回调链接：https://m.officego.com/lessee/detail2.html?buildingId=7154&houseId=9519
        if (bean.getbType() == Constants.TYPE_BUILDING) {
            if (!bean.isHouseChild()) {
                webpageUrl = AppConfig.APP_URL_MAIN + "lessee/housesDetail.html?" + bean.getId() + "&isShare=0";
            } else {
                webpageUrl = AppConfig.APP_URL_MAIN + "lessee/detail.html?" + bean.getId() + "&isShare=0";
            }
        } else {
            if (!bean.isHouseChild()) {
                webpageUrl = AppConfig.APP_URL_MAIN + "lessee/housesDetail2.html?" + bean.getId() + "&isShare=0";
            } else {
                webpageUrl = AppConfig.APP_URL_MAIN + "lessee/detail2.html?" + bean.getId() + "&isShare=0";
            }
        }
        int THUMB_SIZE = 150;
        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = bean.getDetailsUrl();
        webpage.webpageUrl = webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = bean.getTitle();
        msg.description = bean.getDes();
        //0 分享好友  1 分享朋友圈 webpage
        Glide.with(this).asBitmap().load(bean.getImgUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Bitmap thumbBmp = Bitmap.createScaledBitmap(resource, THUMB_SIZE, THUMB_SIZE, true);
                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = mTargetScene == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                Constants.WXapi.sendReq(req);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_def_logo_small);
                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = mTargetScene == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                Constants.WXapi.sendReq(req);
            }
        });
    }

}

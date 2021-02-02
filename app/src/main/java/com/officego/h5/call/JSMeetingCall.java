package com.officego.h5.call;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.officego.R;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.dialog.MapDialog;
import com.officego.commonlib.common.dialog.WeChatShareDialog;
import com.officego.commonlib.common.model.ShareBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.model.ChatsBean;
import com.officego.ui.login.CommonLoginTenant;
import com.officego.ui.login.LoginActivity_;
import com.officego.ui.message.ConversationActivity_;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shijie
 * Date 2021/1/6
 **/
public class JSMeetingCall {
    private final Activity context;

    public JSMeetingCall(Activity context) {
        this.context = context;
    }

    @JavascriptInterface
    public void closeView() {
        context.finish();
    }

    @JavascriptInterface
    public void shareClick(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        String officeTitle = object.getString("officeTitle");
        String url = object.getString("url");
        String mainPic = object.getString("mainPic");
        ShareBean bean = new ShareBean();
        bean.setbType(Constants.TYPE_MEETING_ROOM);
        bean.setTitle(officeTitle);
        bean.setDes("");
        bean.setImgUrl(mainPic);//图片url
        bean.setDetailsUrl(url);//分享url
        new WeChatShareDialog(context, bean);
    }

    @JavascriptInterface
    public void callPhoneClick(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        String phone = object.getString("phone");
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle(phone)
                .setCancelButton(R.string.sm_cancel)
                .setConfirmButton("拨打", (dialog12, which) -> {
                    dialog12.dismiss();
                    CommonHelper.callPhone(context, phone);
                }).create();
        dialog.showWithOutTouchable(true);
    }

    @JavascriptInterface
    public void chatClick(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        int buildingId = object.getInt("buildingId");
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            new CommonLoginTenant(context);
            return;
        }
        gotoChat(buildingId);
    }

    @JavascriptInterface
    public void mapClick(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        double latitude = TextUtils.isEmpty(object.getString("latitude")) ? 0 :
                Double.parseDouble(object.getString("latitude"));
        double longitude = TextUtils.isEmpty(object.getString("longitude")) ? 0 :
                Double.parseDouble(object.getString("longitude"));
        String address = object.getString("address");
        new MapDialog(context, latitude, longitude, address);
    }


    /**
     * 服务端创建聊天会话
     */
    public void gotoChat(int buildingId) {
        if (buildingId != 0) {
            OfficegoApi.getInstance().getTargetId3(true,buildingId, new RetrofitCallback<ChatsBean>() {
                @Override
                public void onSuccess(int code, String msg, ChatsBean data) {
                    ConversationActivity_.intent(context).buildingId(buildingId)
                            .targetId(data.getTargetId()).isMeetingEnter(true).start();
                }

                @Override
                public void onFail(int code, String msg, ChatsBean data) {
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        ToastUtils.toastForShort(context, msg);
                    }
                }
            });
        }
    }
}

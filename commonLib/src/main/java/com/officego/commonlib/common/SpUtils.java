package com.officego.commonlib.common;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.officego.commonlib.base.BaseApplication;
import com.officego.commonlib.utils.SharedManager;

public class SpUtils {

    private final static String PACKAGE_NAME = "OFFICEGO_";
    private static final String USER_ID = PACKAGE_NAME + "USER_ID";
    private static final String RONG_CLOUD_TOKEN = PACKAGE_NAME + "RONG_CLOUD_TOKEN";
    private static final String RONG_CLOUD_ID = PACKAGE_NAME + "RONG_CLOUD_ID";
    private static final String SIGN_TOKEN = PACKAGE_NAME + "SIGN_TOKEN";
    private static final String PHONE_NUM = PACKAGE_NAME + "PHONE_NUM";
    private static final String WE_CHAT = PACKAGE_NAME + "WE_CHAT";
    private static final String LOGIN_ROLE = PACKAGE_NAME + "LOGIN_ROLE";
    //头像图片
    private static final String HEADER_IMG = PACKAGE_NAME + "HEADER_IMG";
    private static final String NICK_NAME = PACKAGE_NAME + "NICK_NAME";
    //聊天插入楼盘消息
    private static final String CHAT_INSERT_BUILDING_INFO = PACKAGE_NAME + "CHAT_INSERT_BUILDING_INFO";
    //是否取消更新
    private static final String CANCEL_UPDATE = PACKAGE_NAME + "CANCEL_UPDATE";
    //保存引导页
    private static final String LEAD_PAGES = PACKAGE_NAME + "LEAD_PAGES";
    //协议
    private static final String AGREE_PROTOCOL = PACKAGE_NAME + "AGREE_PROTOCOL";

    private static SharedPreferences getSharedPreference() {
        return SharedManager.getSharedPreference(BaseApplication.getContext());
    }

    //save用户登录信息
    public static void saveLoginInfo(LoginBean data, String phone) {
        SpUtils.saveUserId(data.getUid());
        SpUtils.savePhoneNum(phone);
        SpUtils.saveSignToken(data.getToken());
        SpUtils.saveRongToken(data.getRongyuntoken());
        SpUtils.saveHeaderImg(data.getAvatar());
        SpUtils.saveNickName(data.getNickName());
    }

    //clear用户登录信息
    public static void clearLoginInfo() {
        SharedManager.clearValue(BaseApplication.getContext(), USER_ID);
        SharedManager.clearValue(BaseApplication.getContext(), PHONE_NUM);
        SharedManager.clearValue(BaseApplication.getContext(), SIGN_TOKEN);
        SharedManager.clearValue(BaseApplication.getContext(), RONG_CLOUD_TOKEN);
        SharedManager.clearValue(BaseApplication.getContext(), HEADER_IMG);
        SharedManager.clearValue(BaseApplication.getContext(), NICK_NAME);
        SharedManager.clearValue(BaseApplication.getContext(), CANCEL_UPDATE);
    }

    //uid
    public static void saveUserId(String userId) {
        SharedManager.putValue(BaseApplication.getContext(), USER_ID, userId);
    }

    public static String getUserId() {
        return SharedManager.getValue(BaseApplication.getContext(), USER_ID);
    }

    //用户签名token
    public static void saveSignToken(String token) {
        SharedManager.putValue(BaseApplication.getContext(), SIGN_TOKEN, token);
    }

    public static String getSignToken() {
        String signToken = SharedManager.getValue(BaseApplication.getContext(), SIGN_TOKEN);
        return TextUtils.isEmpty(signToken) ? "" : signToken;
    }

    //融云token
    public static void saveRongToken(String rongCloudtoken) {
        SharedManager.putValue(BaseApplication.getContext(), RONG_CLOUD_TOKEN, rongCloudtoken);
    }

    public static String getRongToken() {
        return SharedManager.getValue(BaseApplication.getContext(), RONG_CLOUD_TOKEN);
    }

    //融云 id 用于聊天使用
    public static void saveRongChatId(String rongCloudId) {
        SharedManager.putValue(BaseApplication.getContext(), RONG_CLOUD_ID, rongCloudId);
    }

    public static String getRongChatId() {
        return SharedManager.getValue(BaseApplication.getContext(), RONG_CLOUD_ID);
    }

    //mobile
    public static void savePhoneNum(String phone) {
        SharedManager.putValue(BaseApplication.getContext(), PHONE_NUM, phone);
    }

    public static String getPhoneNum() {
        return SharedManager.getValue(BaseApplication.getContext(), PHONE_NUM);
    }

    //头像图片
    public static void saveHeaderImg(String url) {
        SharedManager.putValue(BaseApplication.getContext(), HEADER_IMG, url);
    }

    public static String getHeaderImg() {
        return SharedManager.getValue(BaseApplication.getContext(), HEADER_IMG);
    }

    //昵称
    public static void saveNickName(String nickName) {
        SharedManager.putValue(BaseApplication.getContext(), NICK_NAME, nickName);
    }

    public static String getNickName() {
        return SharedManager.getValue(BaseApplication.getContext(), NICK_NAME);
    }

    //wx
    public static void saveWechat(String wx) {
        SharedManager.putValue(BaseApplication.getContext(), WE_CHAT, wx);
    }

    public static String getWechat() {
        return SharedManager.getValue(BaseApplication.getContext(), WE_CHAT);
    }

    //聊天插入楼盘消息 tooken+融云token+buildingId+houseId+targetId
    public static void saveChatBuildingInfo(String key, String value) {
        SharedManager.putValue(BaseApplication.getContext(), key, value);
    }

    //获取聊天插入楼盘消息 tooken+houseId+targetId
    public static String getChatBuildingInfo(String key) {
        return SharedManager.getValue(BaseApplication.getContext(), key);
    }

    //role  ：0租户，1户主
    public static void saveRole(String role) {
        SharedManager.putValue(BaseApplication.getContext(), LOGIN_ROLE, role);
    }

    public static String getRole() {
        return SharedManager.getValue(BaseApplication.getContext(), LOGIN_ROLE);
    }

    //取消版本更新
    public static void saveCancelUpdate() {
        SharedManager.putValue(BaseApplication.getContext(), CANCEL_UPDATE, "Y");
    }

    public static String getCancelUpdate() {
        return SharedManager.getValue(BaseApplication.getContext(), CANCEL_UPDATE);
    }

    //保存是否显示引导页
    public static void saveLead() {
        SharedManager.putValue(BaseApplication.getContext(), LEAD_PAGES, "TRUE");
    }

    //获取引导页值
    public static String getLead() {
        return SharedManager.getValue(BaseApplication.getContext(), LEAD_PAGES);
    }

    public static void saveProtocol() {
        SharedManager.putValue(BaseApplication.getContext(), AGREE_PROTOCOL, "Y");
    }

    public static String getProtocol() {
        return SharedManager.getValue(BaseApplication.getContext(), AGREE_PROTOCOL);
    }

}

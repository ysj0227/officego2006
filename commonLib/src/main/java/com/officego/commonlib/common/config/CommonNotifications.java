package com.officego.commonlib.common.config;

public class CommonNotifications {
    //common notice
    private static int totalEvents = 0x0001;

    public static final int independentAll = totalEvents++;
    public static final int conversationPhoneAgree = totalEvents++;
    public static final int conversationPhoneReject = totalEvents++;

    public static final int conversationWeChatAgree = totalEvents++;
    public static final int conversationWeChatReject = totalEvents++;

    public static final int conversationViewHouseAgree = totalEvents++;
    public static final int conversationViewHouseReject = totalEvents++;

    public static final int conversationIdApplyAgree = totalEvents++;
    public static final int conversationIdApplyReject = totalEvents++;
    //更新用户信息成功
    public static final int updateUserInfoSuccess = totalEvents++;
    public static final int updateUserOwnerInfoSuccess = totalEvents++;

    //绑定微信
    public static final int conversationBindWeChat = totalEvents++;
    public static final int conversationBindPhone = totalEvents++;
    //融云其他设备登录踢出
    public static final int rongCloudkickDialog = totalEvents++;
    //身份发送变化重新登录
    public static final int identityChangeToRelogin = totalEvents++;
    //登录登出
    public static final int loginIn = totalEvents++;
    public static final int loginOut = totalEvents++;
    //楼盘网点添加成功
    public static final int updateBuildingSuccess = totalEvents++;
    public static final int rejectBuildingSuccess = totalEvents++;
    public static final int updateHouseSuccess = totalEvents++;
    public static final int refreshHouseSuccess = totalEvents++;
    //认证或添加网点楼盘提交
    public static final int checkedIdentitySuccess = totalEvents++;
}

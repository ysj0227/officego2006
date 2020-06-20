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
    //更新用户信息成功
    public static final int updateUserInfoSuccess = totalEvents++;
    public static final int updateUserOwnerInfoSuccess = totalEvents++;
    public static final int updateAvatarSuccess = totalEvents++;

    //绑定微信
    public static final int conversationBindWeChat = totalEvents++;
    //广播登录成功
    public static final int broadcastLoginSuccess = totalEvents++;

}

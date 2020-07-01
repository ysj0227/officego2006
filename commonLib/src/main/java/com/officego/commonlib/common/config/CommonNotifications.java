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
    public static final int conversationBindPhone = totalEvents++;
    //业主认证成功
    public static final int ownerIdentityComplete = totalEvents++;

    //融云其他设备登录踢出
    public static final int rongCloudkickDialog = totalEvents++;

    //身份发送变化重新登录
    public static final int identityChangeToRelogin = totalEvents++;

}

package com.owner.identity;

import android.text.TextUtils;

import com.owner.identity.model.GetIdentityInfoBean;

/**
 * 认证驳回 判断是否关联还是创建
 */
public class IdentityRejectInfo {

    //auditStatus 为2 驳回  authority 如果是1(普通) 就是创建 ，如果是0(管理员)就是关联      4待完善
    public static boolean isCreateReject(GetIdentityInfoBean data) {
        return TextUtils.equals("2", data.getAuditStatus())
                && TextUtils.equals("1", data.getAuthority());
    }

    //信息尚未完成
    public static boolean isCompleteInfo(GetIdentityInfoBean data) {
        return TextUtils.equals("4", data.getAuditStatus());
    }

}

package com.owner.mine;

import android.text.TextUtils;

import com.owner.mine.model.UserOwnerBean;

public class StringIdentity {

    /**
     * identityType :  "identityType": 0,//身份类型0个人1企业2联合
     * auditStatus :  0待审核1审核通过2审核未通过 3过期(和2未通过一样处理)-1未认证
     */
    public static String identityInfo(UserOwnerBean data) {
        String id, status;
        if (data.getIdentityType() == 0) {
            id = "";
        } else if (data.getIdentityType() == 1) {
            id = "";
        } else if (data.getIdentityType() == 2) {
            id = "";
        } else {
            id = "";
        }
        if (data.getAuditStatus() == 0) {
            status = "待审核";
        } else if (data.getAuditStatus() == 1) {
            status = "已认证";
        } else if (data.getAuditStatus() == 2) {
            status = "审核未通过";
        } else {
            status = "未认证";
        }
        if (TextUtils.isEmpty(id)) {
            return status;
        }
        return id + status;
    }

}

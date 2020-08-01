package com.owner.identity;

import android.text.TextUtils;

import com.owner.identity.model.GetIdentityInfoBean;

/**
 * 认证驳回 判断是否关联还是创建
 */
public class IdentityInfo {

    //auditStatus 为2 驳回  authority 如果是1(普通) 就是创建 ，如果是0(管理员)就是关联      4待完善
    public static boolean isCreateReject(GetIdentityInfoBean data) {
        return TextUtils.equals("2", data.getAuditStatus())
                && TextUtils.equals("1", data.getAuthority());
    }

    //auditStatus 为2 驳回  authority 如果是1(普通) 就是创建 ，如果是0(管理员)就是关联      4待完善
    //*******************************************************
    //*******************************************************
    //是否创建公司
    public static boolean isCreateCompanyInfo(GetIdentityInfoBean data) {
        return TextUtils.equals("1", data.getAuthority());
    }

    //是否创建楼盘
    public static boolean isCreateBuildingInfo(GetIdentityInfoBean data) {
        //创建的
        return TextUtils.isEmpty(data.getBuildingId()) ||
                TextUtils.equals("0", data.getBuildingId());
    }

    //0 空  无定义     1创建  2关联
    //isCreateCompany  isCreateBuilding  isCreateBranch

    //是否创建公司
    public static String strCreateCompany(GetIdentityInfoBean data) {
        return "";
    }

    //是否创建楼盘
    public static String strCreateBuilding(GetIdentityInfoBean data) {
        return "";
    }

    //是否创建网点
    public static String strCreateBranch(GetIdentityInfoBean data) {
        return "";
    }

}

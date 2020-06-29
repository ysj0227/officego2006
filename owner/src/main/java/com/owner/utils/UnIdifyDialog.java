package com.owner.utils;

import android.content.Context;
import android.text.TextUtils;

import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.h5.WebViewIdifyActivity_;
import com.owner.mine.model.UserOwnerBean;

/**
 * Created by YangShiJie
 * Data 2020/6/28.
 * 业主未认证
 **/
public class UnIdifyDialog {
    private CommonDialog dialog;

    public UnIdifyDialog(Context context, UserOwnerBean data) {
        unIdifyDialog(context, data);
    }

    // 0待审核1审核通过2审核未通过
    private void unIdifyDialog(Context context, UserOwnerBean data) {
        if (dialog != null && !dialog.isShowing()) {
            dialog = null;
        }
        if (dialog == null) {
            dialog = new CommonDialog.Builder(context)
                    .setTitle(idify(data) + "\n请您先认证信息")
                    .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                        WebViewIdifyActivity_.intent(context).start();
                        dialog.dismiss();
                        dialog = null;
                    }).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
        }
    }

    /**
     * identityType :  "identityType": 0,//身份类型0个人1企业2联合
     * auditStatus :  0待审核1审核通过2审核未通过
     */
    private String idify(UserOwnerBean data) {
        String id, status;
        if (data.getIdentityType() == 0) {
            id = "个人";
        } else if (data.getIdentityType() == 1) {
            id = "企业";
        } else if (data.getIdentityType() == 2) {
            id = "联办";
        } else {
            id = "";
        }
        if (data.getAuditStatus() == 0) {
            status = "待审核";
        } else if (data.getAuditStatus() == 1) {
            status = "已认证";
        } else if (data.getAuditStatus() == 2) {
            status = "审核驳回";
        } else {
            status = "未认证";
        }
        if (TextUtils.isEmpty(id)) {
            return status;
        }
        return id + "-" + status;
    }

}

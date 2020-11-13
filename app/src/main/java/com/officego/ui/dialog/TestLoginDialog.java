package com.officego.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.officego.R;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.view.ClearableEditText;
import com.officego.ui.login.presenter.LoginPresenter;

/**
 * Created by shijie
 * Date 2020/11/13
 **/
public class TestLoginDialog {

    public TestLoginDialog(Context context, LoginPresenter mPresenter) {
        testDialog(context, mPresenter);
    }

    /**
     * 测试登录
     */
    public void testDialog(Context context, LoginPresenter mPresenter) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_test_login, null);
        dialog.setContentView(viewLayout);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = 900;
            dialogWindow.setAttributes(lp);
            ClearableEditText cetInfact = viewLayout.findViewById(R.id.cet_infact);
            ClearableEditText cetUrl = viewLayout.findViewById(R.id.cet_url);
            ClearableEditText cetTest = viewLayout.findViewById(R.id.cet_test);
            ClearableEditText cetCode = viewLayout.findViewById(R.id.cet_code);

            Button btnGo = viewLayout.findViewById(R.id.btn_go);
            btnGo.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(cetInfact.getText().toString().trim())) {
                    AppConfig.APP_URL = cetInfact.getText().toString().trim() + "/";
                }
                if (!TextUtils.isEmpty(cetUrl.getText().toString().trim())) {
                    AppConfig.APP_URL_MAIN = cetUrl.getText().toString().trim() + "/";
                }
                mPresenter.login(cetTest.getText().toString().trim(), cetCode.getText().toString().trim());
                dialog.dismiss();
            });
            dialog.setCancelable(true);
            dialog.show();
        }
    }

}

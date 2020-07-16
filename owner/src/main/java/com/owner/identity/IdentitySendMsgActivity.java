package com.owner.identity;

import com.officego.commonlib.base.BaseActivity;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(resName = "activity_go_send_message")
public class IdentitySendMsgActivity extends BaseActivity {

    @AfterExtras
    void init() {

    }

    @Click(resName = "iv_back")
    void backClick() {
        finish();
    }

}

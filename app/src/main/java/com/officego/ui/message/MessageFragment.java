package com.officego.ui.message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.officego.R;
import com.officego.commonlib.base.BaseFragment;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

import static android.app.Activity.RESULT_OK;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint("NewApi")
@EFragment(R.layout.conversationlist)
public class MessageFragment extends BaseFragment {
    private static final int REQUEST_CODE = 1001;
    @ViewById(R.id.ll_root_message)
    LinearLayout llRootMessage;
    @ViewById(R.id.rl_input_text)
    RelativeLayout rlInputText;
    @ViewById(R.id.rl_ibtn_search)
    RelativeLayout rlIbtnSearch;
    @ViewById(R.id.rl_title)
    RelativeLayout rlTitle;
    @ViewById(R.id.ctl_no_login)
    ConstraintLayout ctlNoLogin;
    @ViewById(R.id.conversationlist)
    View conversationList;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlTitle.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = CommonHelper.statusHeight(mActivity) + CommonHelper.dp2px(mActivity, 60);
        rlTitle.setLayoutParams(params);
        //当前未登录状态
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            loginOut();
        } else {
            loginIn();
        }
        initIm();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            loginOut();
        }
    }

    @Click(R.id.btn_login)
    void loginClick() {
        if (isFastClick(1500)) {
            return;
        }
        //神策
        SensorsTrack.login();
        LoginActivity_.intent(mActivity).isTenantGotoLogin(true).startForResult(REQUEST_CODE);
    }

    @OnActivityResult(REQUEST_CODE)
    void onAvatarResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            initIm();
        }
    }

    //初始化聊天列表
    private ConversationListFragment fragment;

    private void initIm() {
        if (fragment == null) {
            fragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + mActivity.getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置群组会话，该会话非聚合显示
                    .build();
            fragment.setUri(uri);

            FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.conversationlist, fragment);
            transaction.commit();
        }
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.loginIn,
                CommonNotifications.loginOut};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (id == CommonNotifications.loginIn) {
            loginIn();
        } else if (id == CommonNotifications.loginOut) {
            loginOut();
        }
    }

    @UiThread
    void loginIn() {
        ctlNoLogin.setVisibility(View.GONE);
        conversationList.setVisibility(View.VISIBLE);
    }

    @UiThread
    void loginOut() {
        ctlNoLogin.setVisibility(View.VISIBLE);
        conversationList.setVisibility(View.GONE);
    }
}
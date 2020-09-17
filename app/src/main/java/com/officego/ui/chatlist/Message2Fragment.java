package com.officego.ui.chatlist;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.common.adapter.ChatListAdapter;
import com.officego.commonlib.common.contract.ChatListContract;
import com.officego.commonlib.common.presenter.ChatListPresenter;
import com.officego.ui.login.LoginActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint("NewApi")
@EFragment(R.layout.conversationlist2)
public class Message2Fragment extends BaseMvpFragment<ChatListPresenter>
        implements ChatListContract.View {
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
    @ViewById(R.id.rv_message_list)
    RecyclerView rvMessageList;
    private ChatListAdapter adapter;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new ChatListPresenter();
        mPresenter.attachView(this);
        mPresenter.getChatList();
        initIm();
    }

    private void initIm() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlTitle.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = CommonHelper.statusHeight(mActivity) + CommonHelper.dp2px(mActivity, 60);
        rlTitle.setLayoutParams(params);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        rvMessageList.setLayoutManager(layoutManager);
        //当前未登录状态
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            loginOut();
        } else {
            loginIn();
        }
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
        //登录
        LoginActivity_.intent(mActivity).start();
    }

    @UiThread
    void loginIn() {
        ctlNoLogin.setVisibility(View.GONE);
        rvMessageList.setVisibility(View.VISIBLE);
    }

    @UiThread
    void loginOut() {
        ctlNoLogin.setVisibility(View.VISIBLE);
        rvMessageList.setVisibility(View.GONE);
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.loginIn,
                CommonNotifications.loginOut,
                CommonNotifications.refreshConversationList};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (id == CommonNotifications.loginIn) {
            loginIn();
        } else if (id == CommonNotifications.loginOut) {
            loginOut();
        } else if (id == CommonNotifications.refreshConversationList) {
            LogCat.e(TAG, "111111  refreshConversationList");
            mPresenter.getChatList();
        }
    }

    @Override
    public void chatListSuccess(List<ChatListBean.ListBean> data) {
        if (adapter == null) {
            adapter = new ChatListAdapter(mActivity, data);
            rvMessageList.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void unreadCountSuccess(Integer unReadCount) {
        LogCat.e(TAG, "111111 unReadCount=" + unReadCount);
    }
}
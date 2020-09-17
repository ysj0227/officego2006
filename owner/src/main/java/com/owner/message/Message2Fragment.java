package com.owner.message;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.base.BaseMvpFragment;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.adapter.ChatListAdapter;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.contract.ChatListContract;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.commonlib.common.presenter.ChatListPresenter;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.dialog.CommonDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/11.
 * Descriptions:
 **/
@SuppressLint("NewApi")
@EFragment(resName = "conversationlist2")
public class Message2Fragment extends BaseMvpFragment<ChatListPresenter> implements ChatListContract.View {
    @ViewById(resName = "ll_root_message")
    LinearLayout llRootMessage;
    @ViewById(resName = "rl_input_text")
    RelativeLayout rlInputText;
    @ViewById(resName = "rl_ibtn_search")
    RelativeLayout rlIbtnSearch;
    @ViewById(resName = "rl_title")
    RelativeLayout rlTitle;
    @ViewById(resName = "rv_message_list")
    RecyclerView rvMessageList;

    private ChatListAdapter adapter;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(mActivity);
        mPresenter = new ChatListPresenter();
        mPresenter.attachView(this);
        initIm();
        mPresenter.getChatList();
    }

    //初始化聊天列表
    private void initIm() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlTitle.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = CommonHelper.statusHeight(mActivity) + CommonHelper.dp2px(mActivity, 60);
        rlTitle.setLayoutParams(params);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        rvMessageList.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            CommonDialog dialog = new CommonDialog.Builder(getContext())
                    .setMessage("账号已退出，请重新登录")
                    .setConfirmButton(com.officego.commonlib.R.string.str_login, (dialog12, which) -> {
                        GotoActivityUtils.gotoLoginActivity(getActivity());
                        dialog12.dismiss();
                    }).create();
            dialog.showWithOutTouchable(false);
            dialog.setCancelable(false);
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
        LogCat.e(TAG, "111111 owner unReadCount=" + unReadCount);
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.refreshConversationList};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (id == CommonNotifications.refreshConversationList) {
            LogCat.e(TAG, "111111  owner refreshConversationList");
            mPresenter.getChatList();
        }
    }
}
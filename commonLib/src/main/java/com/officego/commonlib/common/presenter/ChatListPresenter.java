package com.officego.commonlib.common.presenter;


import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.common.contract.ChatListContract;

import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by shijie
 * Date 2020/9/17
 **/
public class ChatListPresenter extends BasePresenter<ChatListContract.View>
        implements ChatListContract.Presenter {

    @Override
    public void getChatList() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getChatList(new RetrofitCallback<ChatListBean>() {
            @Override
            public void onSuccess(int code, String msg, ChatListBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.chatListSuccess(data.getList());
                }
            }

            @Override
            public void onFail(int code, String msg, ChatListBean data) {
                if (isViewAttached()) {
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.hideLoadingDialog();
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    /**
     * 获取指定会话未读数
     */
    @Override
    public void getUnReadCount(String targetId) {
        Conversation.ConversationType conversationType;
        if (TextUtils.equals(Constants.TYPE_SYSTEM, targetId) ||
                (targetId.length() > 1 && TextUtils.equals(Constants.TYPE_SYSTEM, targetId.substring(targetId.length() - 1)))) {
            conversationType = Conversation.ConversationType.SYSTEM;
        } else {
            conversationType = Conversation.ConversationType.PRIVATE;
        }
        RongIMClient.getInstance().getUnreadCount(conversationType, targetId,
                new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer unReadCount) {
                        if (isViewAttached()) {
                            if (unReadCount > 0) {
                                mView.unreadCountSuccess(unReadCount);
                            }
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode ErrorCode) {

                    }
                });

    }
}

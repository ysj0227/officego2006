package com.officego.ui.chatlist.presenter;


import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.ui.chatlist.contract.ChatListContract;

import java.util.List;

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
}

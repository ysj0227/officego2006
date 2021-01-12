package com.officego.ui.home.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.officego.R;
import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.QueryHistoryKeywordsBean;
import com.officego.commonlib.common.model.SearchListBean;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.contract.SearchContract;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class SearchKeywordsPresenter extends BasePresenter<SearchContract.View>
        implements SearchContract.Presenter {
    private final Context context;

    public SearchKeywordsPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getHistory() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getSearchKeywords(new RetrofitCallback<List<QueryHistoryKeywordsBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<QueryHistoryKeywordsBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.historySuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<QueryHistoryKeywordsBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                }
            }
        });
    }

    @Override
    public void getHot() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getHotKeywords(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.hotSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                }
            }
        });
    }

    @Override
    public void searchList(String keywords) {
        OfficegoApi.getInstance().searchList(keywords, new RetrofitCallback<List<SearchListBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<SearchListBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.searchListSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<SearchListBean.DataBean> data) {
            }
        });
    }

    @Override
    public void addSearchKeywords(String keywords) {
        if(!TextUtils.isEmpty(SpUtils.getSignToken())){
            OfficegoApi.getInstance().addSearchKeywords(keywords, new RetrofitCallback<Object>() {
                @Override
                public void onSuccess(int code, String msg, Object data) {
                }

                @Override
                public void onFail(int code, String msg, Object data) {
                }
            });
        }
    }

    @Override
    public void clearSearchKeywords() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().clearSearchKeywords(new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.clearHistorySuccess();
                    mView.shortTip(R.string.tip_delete_success);
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.shortTip(R.string.tip_delete_fail);
                }
            }
        });
    }

}

package com.officego.ui.find;

import android.content.Context;

import com.officego.R;
import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.OfficegoApi;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.utils.GotoActivityUtils;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class FindhousePresenter extends BasePresenter<FindHouseContract.View>
        implements FindHouseContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;

    public FindhousePresenter(Context context) {
        this.context = context;
    }


    @Override
    public void getHouseUnique() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getHouseUnique(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.getHouseUniqueSuccess(data);
                    mView.hideLoadingDialog();
                }
            }

            @Override
            public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.getHouseUniqueFail(code, msg);
                    mView.hideLoadingDialog();
                }
            }
        });
    }

    @Override
    public void getDecoratedType() {
        OfficegoApi.getInstance().getDecoratedType(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.getDecoratedTypeSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.getDecoratedTypeFail(code, msg);
                }
            }
        });
    }

    @Override
    public void wantToFind(String btype, String constructionArea, String rentPrice,
                           String simple, String decoration, String tags) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().wantToFind(btype, constructionArea,
                rentPrice, simple, decoration, tags, new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.shortTip(R.string.str_login_success);
                            GotoActivityUtils.gotoMainActivity(context);
                            mView.hideLoadingDialog();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.shortTip(R.string.tip_save_fail);
                        }
                    }
                });
    }
}

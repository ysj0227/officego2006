package com.officego.ui.home.presenter;

import android.content.Context;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.retrofit.RpcErrorCode;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.contract.SearchListContract;
import com.officego.ui.home.model.BannerBean;
import com.officego.ui.home.model.BuildingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class SearchListPresenter extends BasePresenter<SearchListContract.View> implements SearchListContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private Context context;
    //banner list
    private List<String> bannerList = new ArrayList<>();
    //筛选条件列表
    private int index;
    private List<DirectoryBean.DataBean> decorationList = new ArrayList<>();
    private List<DirectoryBean.DataBean> buildingUniqueList = new ArrayList<>();
    private List<DirectoryBean.DataBean> jointWorkUniqueList = new ArrayList<>();
    private List<DirectoryBean.DataBean> brandList = new ArrayList<>();

    public SearchListPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getBannerList() {
        OfficegoApi.getInstance().getBannerList(new RetrofitCallback<List<BannerBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<BannerBean.DataBean> data) {
                if (isViewAttached()) {
                    bannerList.clear();
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            bannerList.add(data.get(i).getImg());
                        }
                        mView.bannerListSuccess(bannerList, data);
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, List<BannerBean.DataBean> data) {
                if (isViewAttached()) {
                }
            }
        });
    }

    @Override
    public void getBuildingList(int pageNo, String btype, String district, String business, String line,
                                String nearbySubway, String area, String dayPrice, String seats, String decoration,
                                String houseTags, String sort, String keyWord) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getBuildingList(pageNo, btype, district, business,
                line, nearbySubway, area, dayPrice, seats, decoration,
                houseTags, sort, keyWord, Constants.LONGITUDE, Constants.LATITUDE, new RetrofitCallback<BuildingBean>() {
                    @Override
                    public void onSuccess(int code, String msg, BuildingBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.BuildingListSuccess(data.getList(), data.getList().size() >= 10);
                            mView.endRefresh();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, BuildingBean data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                            mView.endRefresh();
                            if (code == Constants.DEFAULT_ERROR_CODE || code == Constants.ERROR_CODE_5002 ||
                                    code == RpcErrorCode.RPC_ERR_TIMEOUT) {
                                mView.shortTip(msg);
                            }
                        }
                    }
                });
    }


    //获取筛选-品牌，楼盘特色，网点特色，装修类型
    @Override
    public void getConditionList() {
        //楼盘特色
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getBuildingUnique(
                new RetrofitCallback<List<DirectoryBean.DataBean>>() {
                    @Override
                    public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                        if (isViewAttached()) {
                            buildingUniqueList = data;
                            conditionListSuccess();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                        }
                    }
                });
        //网点特色
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getBranchUnique(
                new RetrofitCallback<List<DirectoryBean.DataBean>>() {
                    @Override
                    public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                        if (isViewAttached()) {
                            jointWorkUniqueList = data;
                            conditionListSuccess();
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
                        if (isViewAttached()) {
                            mView.hideLoadingDialog();
                        }
                    }
                });
        //装修类型
        OfficegoApi.getInstance().getDecoratedType(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                decorationList = data;
                conditionListSuccess();
            }

            @Override
            public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                }
            }
        });
        //品牌
        OfficegoApi.getInstance().getDecoratedType(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                DirectoryBean.DataBean bean;
                for (int i = 0; i < 5; i++) {
                    bean=new DirectoryBean.DataBean();
                    bean.setDictCname("品牌"+i);
                    bean.setDictValue(i);
                    brandList.add(bean);
                }
                conditionListSuccess();
            }

            @Override
            public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                }
            }
        });
    }

    private void conditionListSuccess() {
        index++;
        if (index == 4 && isViewAttached()) {
            index = 0;
            mView.hideLoadingDialog();
            mView.conditionListSuccess(decorationList, buildingUniqueList, jointWorkUniqueList, brandList);
        }
    }
}

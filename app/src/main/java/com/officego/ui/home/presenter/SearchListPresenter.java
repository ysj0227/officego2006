package com.officego.ui.home.presenter;

import android.content.Context;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.retrofit.RpcErrorCode;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.home.contract.SearchListContract;
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

    /**
     * 列表
     * district 	否 	string 	大区
     * business 	否 	string 	商圈 不限：0 多个英文逗号分隔Id
     * nearbySubway 	否 	string 	地铁站名 ，不限：0 多个英文逗号分隔Id
     * line 	否 	string 	地铁线
     * area 	否 	string 	平方米区间英文逗号分隔
     * dayPrice 	否 	string 	楼盘的时候是 每平方米单价区间英文逗号分隔 网点的时候是 每工位每月单价区间英文逗号分隔
     * decoration 	否 	string 	装修类型id英文逗号分隔
     * btype 	否 	string 	类型1:楼盘,2:网点, 0全部
     * houseTags 	否 	string 	房源特色id英文逗号分隔
     * vrFlag 	否 	int 	是否只看VR房源 0:不限1:只看VR房源
     * sort 	否 	int 	排序0默认1价格从高到低2价格从低到高3面积从大到小4面积从小到大
     * seats 	否 	string 	联合工位区间英文逗号分隔
     * longitude 	否 	string 	经度
     * latitude 	否 	string 	纬度
     * keyWord 	否 	string 	关键字搜索
     * pageNo 	否 	int 	当前页
     * pageSize 	否 	int 	每页条数
     */
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
        OfficegoApi.getInstance().brandList(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                brandList = data;
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

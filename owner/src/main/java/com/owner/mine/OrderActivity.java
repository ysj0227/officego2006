package com.owner.mine;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.model.OrderBean;
import com.officego.commonlib.common.model.ServiceBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.CustomiseTabListener;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.owner.R;
import com.owner.adapter.OrderAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by shijie
 * Date 2021/3/24
 **/
@EActivity(resName = "mine_activity_pay_order")
public class OrderActivity extends BaseActivity implements
        OrderAdapter.OrderListener {

    @ViewById(resName = "rv_order")
    RecyclerView rvOrder;
    @ViewById(resName = "tv_no_data")
    TextView tvNoData;
    @ViewById(resName = "tab_layout")
    TabLayout tabLayout;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        rvOrder.setLayoutManager(new LinearLayoutManager(context));
        orderList(1);
        tabLayout.addOnTabSelectedListener(new CustomiseTabListener() {
            @Override
            protected void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        orderList(1);
                        break;
                    case 1:
                        orderList(2);
                        break;
                    case 2:
                        orderList(3);
                        break;
                }
            }
        });
    }

    private void orderList(int type) {
        OfficegoApi.getInstance().orderList(type, new RetrofitCallback<List<OrderBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<OrderBean.DataBean> data) {
                hideLoadingDialog();
                if (data == null || data.size() == 0) {
                    rvOrder.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                } else {
                    rvOrder.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                    OrderAdapter adapter = new OrderAdapter(context, data);
                    adapter.setListener(OrderActivity.this);
                    rvOrder.setAdapter(adapter);
                }
            }

            @Override
            public void onFail(int code, String msg, List<OrderBean.DataBean> data) {
                hideLoadingDialog();
                if (code == Constants.DEFAULT_ERROR_CODE) {
                    shortTip(msg);
                }
            }
        });
    }

    private void getMobile() {
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().
                serviceMobile(new RetrofitCallback<ServiceBean>() {
                    @Override
                    public void onSuccess(int code, String msg, ServiceBean bean) {
                        hideLoadingDialog();
                        String mobile = bean.getTechnicalSupport();
                        new CommonDialog.Builder(context)
                                .setTitle("客服电话")
                                .setMessage(mobile)
                                .setCancelButton(R.string.sm_cancel)
                                .setConfirmButton("拨打", (dialog12, which) -> {
                                    dialog12.dismiss();
                                    CommonHelper.callPhone(context, mobile);
                                }).create().show();
                    }

                    @Override
                    public void onFail(int code, String msg, ServiceBean data) {
                        hideLoadingDialog();
                    }
                });
    }

    @Override
    public void mobile() {
        getMobile();
    }
}

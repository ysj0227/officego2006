package com.owner.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.ToastUtils;
import com.owner.R;
import com.owner.identity.model.BusinessCircleBean;
import com.owner.rpc.OfficegoApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaDialog {
    private TextView tvCity, tvArea;
    private int district, business;

    public AreaSureListener getListener() {
        return listener;
    }

    public void setListener(AreaSureListener listener) {
        this.listener = listener;
    }

    public AreaSureListener listener;

    public interface AreaSureListener {
        void AreaSure(String area, int district, int business);
    }

    public AreaDialog(Context context, int district, int business) {
        this.district = district;
        this.business = business;
        areaDialog(context);
    }

    private void areaDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_select_area, null);
        dialog.setContentView(viewLayout);
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        lp.height = CommonHelper.getScreenHeight(context) - 500;
        dialogWindow.setAttributes(lp);

        RecyclerView recyclerViewCenter = viewLayout.findViewById(R.id.rv_center);
        recyclerViewCenter.setLayoutManager(new LinearLayoutManager(context));
        RecyclerView recyclerViewRight = viewLayout.findViewById(R.id.rv_right);
        recyclerViewRight.setLayoutManager(new LinearLayoutManager(context));
        tvCity = viewLayout.findViewById(R.id.tv_city);
        tvArea = viewLayout.findViewById(R.id.tv_area);
        getSearchDistrictList(context, recyclerViewCenter, recyclerViewRight);

        viewLayout.findViewById(R.id.tv_sure).setOnClickListener(view -> {
            String city = tvCity.getText().toString();
            String area = tvArea.getText().toString();
            if (TextUtils.isEmpty(city)) {
                ToastUtils.toastForShort(context, "请选择区域");
                return;
            }
            if (TextUtils.isEmpty(area)) {
                ToastUtils.toastForShort(context, "请选择商圈");
                return;
            }
            listener.AreaSure("上海市" + city + area, district, business);
            dialog.dismiss();
        });
        viewLayout.findViewById(R.id.tv_cancel).setOnClickListener(view -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }

    //商圈
    private BusinessCircleAdapter businessCircleAdapter;
    private BusinessCircleDetailsAdapter businessCircleDetailsAdapter;

    private void getSearchDistrictList(Context context, RecyclerView recyclerViewCenter,
                                       RecyclerView recyclerViewRight) {
        OfficegoApi.getInstance().getDistrictList(new RetrofitCallback<List<BusinessCircleBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<BusinessCircleBean.DataBean> data) {
                showCity(data);
                businessCircleAdapter = new BusinessCircleAdapter(context, data, recyclerViewRight);
                recyclerViewCenter.setAdapter(businessCircleAdapter);
            }

            @Override
            public void onFail(int code, String msg, List<BusinessCircleBean.DataBean> data) {
            }
        });
    }

    private class BusinessCircleAdapter extends CommonListAdapter<BusinessCircleBean.DataBean> {
        private Map<Integer, Boolean> mapBusiness = new HashMap<>();
        private RecyclerView recyclerViewRight;
        private boolean onBind;

        BusinessCircleAdapter(Context context, List<BusinessCircleBean.DataBean> data, RecyclerView recyclerViewRight) {
            super(context, R.layout.item_search_area, data);
            this.recyclerViewRight = recyclerViewRight;
            //如果此时有选择
            for (int i = 0; i < data.size(); i++) {
                if (district == data.get(i).getDistrictID()) {
                    businessCircleDetailsAdapter = new BusinessCircleDetailsAdapter(mContext, data.get(i).getList());
                    recyclerViewRight.setAdapter(businessCircleDetailsAdapter);
                }
            }
        }

        @Override
        public void convert(ViewHolder holder, BusinessCircleBean.DataBean bean) {
            TextView itemBusiness = holder.getView(R.id.tv_item_meter);
            itemBusiness.setText(bean.getDistrict());
            if (district == bean.getDistrictID()) {
                tvCity.setText(bean.getDistrict());
                mapBusiness.put(holder.getAdapterPosition(), true);
            }
            holder.itemView.setOnClickListener(v -> {
                mapBusiness.clear();
                mapBusiness.put(holder.getAdapterPosition(), true);
                tvCity.setText(bean.getDistrict());
                tvArea.setText("");
                district = bean.getDistrictID();
                if (!onBind) {
                    notifyDataSetChanged();
                }
                businessCircleDetailsAdapter = new BusinessCircleDetailsAdapter(mContext, bean.getList());
                recyclerViewRight.setAdapter(businessCircleDetailsAdapter);
            });
            //显示选中的文本
            onBind = true;
            if (mapBusiness != null && mapBusiness.containsKey(holder.getAdapterPosition())) {
                itemBusiness.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
            } else {
                itemBusiness.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
            }
            onBind = false;
        }
    }

    //商圈详情
    private class BusinessCircleDetailsAdapter extends CommonListAdapter<BusinessCircleBean.DataBean.ListBean> {
        private Map<Integer, Boolean> mapBusiness = new HashMap<>();
        private boolean onBind;

        BusinessCircleDetailsAdapter(Context context, List<BusinessCircleBean.DataBean.ListBean> list) {
            super(context, R.layout.item_search_area, list);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void convert(ViewHolder holder, BusinessCircleBean.DataBean.ListBean bean) {
            TextView itemBusiness = holder.getView(R.id.tv_item_meter);
            itemBusiness.setText(bean.getArea());
            if (business == bean.getId()) {
                tvArea.setText(bean.getArea());
                mapBusiness.put(holder.getAdapterPosition(), true);
            }
            holder.itemView.setOnClickListener(v -> {
                mapBusiness.clear();
                mapBusiness.put(holder.getAdapterPosition(), true);
                tvArea.setText(bean.getArea());
                business = bean.getId();
                if (!onBind) {
                    notifyDataSetChanged();
                }
            });
            //显示选中的文本
            onBind = true;
            if (mapBusiness != null && mapBusiness.containsKey(holder.getAdapterPosition())) {
                itemBusiness.setTextColor(ContextCompat.getColor(mContext, R.color.common_blue_main));
            } else {
                itemBusiness.setTextColor(ContextCompat.getColor(mContext, R.color.text_33));
            }
            onBind = false;
        }
    }

    private void showCity(List<BusinessCircleBean.DataBean> data) {
        //区域
        for (int i = 0; i < data.size(); i++) {
            if (district == data.get(i).getDistrictID()) {
                tvCity.setText(data.get(i).getDistrict());
                //商圈
                for (int j = 0; j < data.get(i).getList().size(); j++) {
                    if (business == data.get(i).getList().get(j).getId()) {
                        tvArea.setText(data.get(i).getList().get(j).getArea());
                    }
                }
            }
        }
    }

}

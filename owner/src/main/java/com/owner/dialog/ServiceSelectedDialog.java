package com.owner.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by YangShiJie
 * Data 2020/6/16.
 * Descriptions:
 **/
public class ServiceSelectedDialog {
    private Context context;

    public ServiceSelectedDialog(Context context) {
        this.context = context;
        shareWx(context);
    }

    private void shareWx(Context context) {
        shareDialog(context);
    }

    private void shareDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_service_select, null);
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
        lp.height = context.getResources().getDimensionPixelSize(R.dimen.dp_400);
        dialogWindow.setAttributes(lp);
        handleLayout(viewLayout);
        viewLayout.findViewById(R.id.iv_exit).setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }

    private void handleLayout(View viewLayout) {
        TextView title = viewLayout.findViewById(R.id.tv_title);
        RecyclerView rvService = viewLayout.findViewById(R.id.rv_service);
        rvService.setLayoutManager(new GridLayoutManager(context, 2));

        List<DirectoryBean.DataBean> serviceList = new ArrayList<>();
        DirectoryBean.DataBean bean;
        for (int i = 0; i < 6; i++) {
            bean = new DirectoryBean.DataBean();
            bean.setDictValue(i);
            bean.setDictImg("https://img.officego.com/dictionary/1591169908538.png");
            bean.setDictCname(i == 0 ? "办公家具" : "设备发电机");
            serviceList.add(bean);
        }
        rvService.setAdapter(new ServiceAdapter(context, serviceList));
        title.setText("基础服务");
    }

    class ServiceAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

        private Context context;
        private Map<Integer, String> mMapLogo;
        private String mStrLogo = "";

        @SuppressLint("UseSparseArrays")
        ServiceAdapter(Context context, List<DirectoryBean.DataBean> list) {
            super(context, R.layout.item_service, list);
            this.context = context;
            if (mMapLogo == null) {
                mMapLogo = new HashMap<>();
            }
        }

        @Override
        public void convert(ViewHolder holder, final DirectoryBean.DataBean bean) {
            CheckBox cbName = holder.getView(R.id.cb_name);
            TextView text = holder.getView(R.id.tv_service_logo_text);
            ImageView imageView = holder.getView(R.id.iv_service_logo);
            Glide.with(context).load(bean.getDictImg()).into(imageView);
            text.setText(bean.getDictCname());
            if (mMapLogo != null) {
                cbName.setChecked(mMapLogo.containsKey(bean.getDictValue()));
            }
            cbName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!mMapLogo.containsKey(bean.getDictValue())) {
                        mMapLogo.put(bean.getDictValue(), bean.getDictCname());
                    }
                } else {
                    mMapLogo.remove(bean.getDictValue());
                }
                mStrLogo = getKey(mMapLogo);
                LogCat.e("TAG", "service=" + mStrLogo);
            });
        }

        private String getKey(Map<Integer, String> map) {
            StringBuilder key = new StringBuilder();
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                if (map.size() == 1) {
                    key.append(entry.getKey());
                } else {
                    key.append(entry.getKey()).append(",");
                }
            }
            if (map.size() > 1) {
                key = key.replace(key.length() - 1, key.length(), "");
            }
            return key.toString();
        }
    }
}

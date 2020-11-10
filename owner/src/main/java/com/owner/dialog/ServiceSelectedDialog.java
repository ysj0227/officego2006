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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.DirectoryBean;
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
    private int mTitleFlay;
    private List<DirectoryBean.DataBean> list;
    private ServiceLogoListener logoListener;
    //列表
    private Map<Integer, String> mMapLogo;
    private List<DirectoryBean.DataBean> selectList = new ArrayList<>();

    public ServiceLogoListener getLogoListener() {
        return logoListener;
    }

    public void setLogoListener(ServiceLogoListener logoListener) {
        this.logoListener = logoListener;
    }

    public interface ServiceLogoListener {
        void serviceLogoResult(int flay, Map<Integer, String> mapLogo, List<DirectoryBean.DataBean> list);
    }

    public ServiceSelectedDialog(Context context, int title, Map<Integer, String> map, List<DirectoryBean.DataBean> list) {
        this.context = context;
        this.mTitleFlay = title;
        this.mMapLogo = map;
        this.list = list;
        serviceDialog(context);
    }

    private void serviceDialog(Context context) {
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
        handleLayout(viewLayout, dialog);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void handleLayout(View viewLayout, Dialog dialog) {
        viewLayout.findViewById(R.id.rl_exit).setOnClickListener(v -> dialog.dismiss());
        TextView title = viewLayout.findViewById(R.id.tv_title);
        Button sure = viewLayout.findViewById(R.id.btn_sure);
        if (mTitleFlay == 0) {
            title.setText("会议室配套");
        } else if (mTitleFlay == 1) {
            title.setText(context.getString(R.string.str_title_create_service));
        } else if (mTitleFlay == 2) {
            title.setText(context.getString(R.string.str_title_base_sservice));
        }
        RecyclerView rvService = viewLayout.findViewById(R.id.rv_service);
        rvService.setLayoutManager(new GridLayoutManager(context, 2));
        rvService.setAdapter(new ServiceAdapter(context, list));
        //确定
        sure.setOnClickListener(view -> {
            if (logoListener != null) {
                dialog.dismiss();
                logoListener.serviceLogoResult(mTitleFlay, mMapLogo, selectList);
            }
        });
    }

    class ServiceAdapter extends CommonListAdapter<DirectoryBean.DataBean> {

        private Context context;

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
            ImageView imageView = holder.getView(R.id.iv_service_logo);
            Glide.with(context).load(bean.getDictImgBlack()).into(imageView);
            cbName.setText(bean.getDictCname());
            if (mMapLogo != null) {
                cbName.setChecked(mMapLogo.containsKey(bean.getDictValue()));
                if (mMapLogo.containsKey(bean.getDictValue())) selectList.add(bean);
            }
            cbName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!mMapLogo.containsKey(bean.getDictValue())) {
                        mMapLogo.put(bean.getDictValue(), bean.getDictImgBlack());
                        selectList.add(bean);
                    }
                } else {
                    mMapLogo.remove(bean.getDictValue());
                    //移除列表项
                    for (int i = 0; i < selectList.size(); i++) {
                        if (bean.getDictValue() == selectList.get(i).getDictValue()) {
                            selectList.remove(i);
                            break;
                        }
                    }
                }
            });
        }
    }
}

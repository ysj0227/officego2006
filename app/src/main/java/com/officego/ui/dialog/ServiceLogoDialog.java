package com.officego.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.ui.adapter.HouseServiceAdapter;
import com.officego.ui.adapter.SpaceItemDecorationHouse;
import com.officego.commonlib.common.model.DirectoryBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/14.
 * Descriptions:
 **/
public class ServiceLogoDialog {

    public ServiceLogoDialog(Context context, String title, List<DirectoryBean.DataBean> list) {
        serviceDialog(context, title, list);
    }

    private void serviceDialog(Context context, String title, List<DirectoryBean.DataBean> list) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_house_service, null);
        //将布局设置给Dialog
        dialog.setContentView(viewLayout);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        // 屏幕宽度（像素）
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        lp.height = height;
        dialogWindow.setAttributes(lp);
        TextView tvTitle = viewLayout.findViewById(R.id.tv_service_title);
        RecyclerView rvService = viewLayout.findViewById(R.id.rv_dialog_service);
        ImageButton ibClose = viewLayout.findViewById(R.id.ib_close);
        rvService.setLayoutManager(new GridLayoutManager(context, 2));
        rvService.addItemDecoration(new SpaceItemDecorationHouse(context, 2));

        tvTitle.setText(title);
        ibClose.setOnClickListener(v -> dialog.dismiss());
        rvService.setAdapter(new HouseServiceAdapter(context, list));

        dialog.setCancelable(true);
        dialog.show();//显示对话框
    }

}

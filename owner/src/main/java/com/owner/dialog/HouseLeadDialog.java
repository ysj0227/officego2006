package com.owner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.officego.commonlib.common.SpUtils;
import com.owner.R;

/**
 * Created by YangShiJie
 * Data 2020/5/14.
 * Descriptions:
 **/
public class HouseLeadDialog {

    public HouseLeadDialog(Context context) {
        serviceDialog(context);
    }

    private void serviceDialog(Context context) {
        SpUtils.saveHouseLead();
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_house_lead, null);
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
        Button btnDismiss = viewLayout.findViewById(R.id.btn_dismiss);
        btnDismiss.setOnClickListener(view -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();//显示对话框
    }
}

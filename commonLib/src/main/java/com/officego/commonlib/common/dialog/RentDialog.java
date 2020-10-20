package com.officego.commonlib.common.dialog;

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

import com.officego.commonlib.R;
import com.officego.commonlib.utils.ListUtils;
import com.officego.commonlib.view.loopview.LoopView;

/**
 * Created by YangShiJie
 * Data 2020/6/11.
 * Descriptions:
 **/
public class RentDialog {
    private String text;
    private String title;
    private SureClickListener sureListener;

    public RentDialog(Context context,String title) {
        this.title=title;
        selectDateDialog(context);
    }

    public SureClickListener getSureListener() {
        return sureListener;
    }

    public void setSureListener(SureClickListener sureListener) {
        this.sureListener = sureListener;
    }

    public interface SureClickListener {
        void selectedRent(String str);
    }

    private void selectDateDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.item_year_wheelview, null);
        dialog.setContentView(viewLayout);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
//        int height = dm.heightPixels;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
//        lp.height = height;
        dialogWindow.setAttributes(lp);
        wheelView(dialog, viewLayout);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void wheelView(Dialog dialog, View viewLayout) {
        LoopView lvWheelYear = viewLayout.findViewById(R.id.lv_wheel_year);
        TextView cancel = viewLayout.findViewById(R.id.tv_cancel);
        TextView sure = viewLayout.findViewById(R.id.tv_sure);
        TextView tvTitle = viewLayout.findViewById(R.id.tv_dialog_title);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        //不循环
        lvWheelYear.setNotLoop();
        //滚动监听
        lvWheelYear.setListener(index -> {
            text = ListUtils.rentList().get(index);
        });
        //设置原始数据
        lvWheelYear.setItems(ListUtils.rentList());
        //设置初始位置 初始化默认值
        lvWheelYear.setInitPosition(0);
        text = ListUtils.rentList().get(0);
        //取消确认
        cancel.setOnClickListener(v -> dialog.dismiss());
        sure.setOnClickListener(v -> {
            dialog.dismiss();
            this.sureListener.selectedRent(text);
        });
    }
}

package com.officego.commonlib.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.nfc.Tag;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.officego.commonlib.R;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.ListUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.loopview.LoopView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by YangShiJie
 * Data 2020/6/11.
 * Descriptions:
 **/
public class YearDateDialog {
    private String year;
    private SureClickListener sureListener;

    public YearDateDialog(Context context) {
        selectDateDialog(context);
    }

    public SureClickListener getSureListener() {
        return sureListener;
    }

    public void setSureListener(SureClickListener sureListener) {
        this.sureListener = sureListener;
    }

    public interface SureClickListener {
        void selectedDate(String date);
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
        //不循环
        lvWheelYear.setNotLoop();
        //滚动监听
        lvWheelYear.setListener(index -> {
            year = ListUtils.yearSetList().get(index);
        });
        //设置原始数据
        lvWheelYear.setItems(ListUtils.yearSetList());
        //设置初始位置 初始化默认值
        lvWheelYear.setInitPosition(0);
        year = ListUtils.yearSetList().get(0);
        //取消确认
        cancel.setOnClickListener(v -> dialog.dismiss());
        sure.setOnClickListener(v -> {
            dialog.dismiss();
            this.sureListener.selectedDate(year+"年");
        });
    }
}

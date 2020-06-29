package com.officego.commonlib.common.dialog;

import android.app.Dialog;
import android.content.Context;
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
import com.officego.commonlib.view.loopview.LoopView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by YangShiJie
 * Data 2020/6/11.
 * Descriptions:
 **/
public class ViewingDateDialog {
    private String year, month, days, hour, minutes;
    private SureClickListener sureListener;

    public ViewingDateDialog(Context context) {
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
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.item_wheelview, null);
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
        wheelView(context, dialog, viewLayout);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void wheelView(Context context, Dialog dialog, View viewLayout) {
        Date mDate = Calendar.getInstance().getTime();
        LoopView lvWheelYear = viewLayout.findViewById(R.id.lv_wheel_year);
        LoopView lvWheelMonth = viewLayout.findViewById(R.id.lv_wheel_month);
        LoopView lvWheelDay = viewLayout.findViewById(R.id.lv_wheel_day);
        LoopView lvWheelHour = viewLayout.findViewById(R.id.lv_wheel_hour);
        LoopView lvWheelMinute = viewLayout.findViewById(R.id.lv_wheel_minute);
        TextView cancel = viewLayout.findViewById(R.id.tv_cancel);
        TextView sure = viewLayout.findViewById(R.id.tv_sure);
        //不循环
        lvWheelYear.setNotLoop();
        lvWheelMonth.setNotLoop();
        lvWheelDay.setNotLoop();
        lvWheelHour.setNotLoop();
        lvWheelMinute.setNotLoop();
        //滚动监听
        lvWheelYear.setListener(index -> {
            year = ListUtils.yearList().get(index);
        });
        lvWheelMonth.setListener(index -> {
            month = ListUtils.monthList().get(index);
            lvWheelDay.setItems(ListUtils.dayList(year, month));
        });
        lvWheelDay.setListener(index -> {
            days = ListUtils.dayList(year, month).get(index);
        });
        lvWheelHour.setListener(index -> {
            hour = ListUtils.hoursList().get(index);
        });
        lvWheelMinute.setListener(index -> {
            minutes = ListUtils.minuteList().get(index);
        });
        //设置原始数据
        lvWheelYear.setItems(ListUtils.yearList());
        lvWheelMonth.setItems(ListUtils.monthList());
        lvWheelDay.setItems(ListUtils.dayList(String.valueOf(DateTimeUtils.getYear(mDate)), String.valueOf(DateTimeUtils.getMonth(mDate))));
        lvWheelHour.setItems(ListUtils.hoursList());
        lvWheelMinute.setItems(ListUtils.minuteList());
        //设置初始位置 初始化默认值
        lvWheelYear.setInitPosition(0);
        year = ListUtils.yearList().get(0);
        lvWheelMonth.setInitPosition(DateTimeUtils.getMonth(mDate) - 1);
        month = ListUtils.monthList().get(DateTimeUtils.getMonth(mDate) - 1);
        lvWheelDay.setInitPosition(DateTimeUtils.getDay(mDate) - 1);
        days = ListUtils.dayList(year, month).get(DateTimeUtils.getDay(mDate) - 1);
        int currentHour = DateTimeUtils.getHour(mDate);
        if (currentHour > 0 && currentHour < 23) {
            lvWheelHour.setInitPosition(currentHour + 1);
            hour = ListUtils.hoursList().get(currentHour + 1);
        } else {
            lvWheelHour.setInitPosition(0);
            hour = ListUtils.hoursList().get(0);
        }
        lvWheelMinute.setInitPosition(0);
        minutes = ListUtils.minuteList().get(0);
        //取消确认
        cancel.setOnClickListener(v -> dialog.dismiss());
        sure.setOnClickListener(v -> {
            String strDate = new StringBuilder().append(year).append("-").append(month).append("-").append(days)
                    .append(" ").append(hour).append(":").append(minutes).toString();
            long currentStamp = DateTimeUtils.currentTimeSecond();
            long selectStamp = DateTimeUtils.dateToSecondStampLong(strDate);
            if (selectStamp <= currentStamp) {
                ToastUtils.toastForShort(context, "预约时间不能小于当前时间");
                return;
            }
            dialog.dismiss();
            this.sureListener.selectedDate(strDate);
        });
    }
}

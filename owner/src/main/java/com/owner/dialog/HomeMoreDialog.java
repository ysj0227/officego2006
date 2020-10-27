package com.owner.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.owner.R;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public class HomeMoreDialog {
    private Context context;
    private boolean isOpenSeats;//是否开放工位
    private boolean isPublish;//是否发布房源

    public HomeMoreDialog(Context context, boolean isOpenSeats, boolean isPublish) {
        this.context = context;
        this.isOpenSeats = isOpenSeats;
        this.isPublish = isPublish;
        moreDialog(context);
    }

    private void moreDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_more_operate, null);
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
        lp.height = context.getResources().getDimensionPixelSize(R.dimen.dp_260);
        dialogWindow.setAttributes(lp);
        handleLayout(viewLayout);
        viewLayout.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        viewLayout.findViewById(R.id.rl_exit).setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();
    }

    private void handleLayout(View viewLayout) {
        TextView tvOff = viewLayout.findViewById(R.id.tv_off);
        if (isOpenSeats) {
            tvOff.setVisibility(View.GONE);
        } else {
            tvOff.setVisibility(isPublish ? View.VISIBLE : View.GONE);
        }
    }
}

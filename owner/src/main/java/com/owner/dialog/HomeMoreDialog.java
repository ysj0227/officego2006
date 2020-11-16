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

import com.officego.commonlib.common.model.owner.HouseBean;
import com.officego.commonlib.utils.ToastUtils;
import com.owner.R;

/**
 * Created by shijie
 * Date 2020/10/15
 **/
public class HomeMoreDialog {
    private Context context;
    private HouseBean.ListBean bean;
    private int position;
    private boolean isOpenSeats;//是否开放工位
    private boolean isPublish;//是否上架发布

    public HouseMoreListener getListener() {
        return listener;
    }

    public void setListener(HouseMoreListener listener) {
        this.listener = listener;
    }

    private HouseMoreListener listener;

    public interface HouseMoreListener {
        void toMoreHouseManager(boolean isDeleteHouse, HouseBean.ListBean bean, int position);
    }

    public HomeMoreDialog(Context context, HouseBean.ListBean bean, int position) {
        this.context = context;
        this.bean = bean;
        this.position = position;
        isOpenSeats = bean.getBtype() == 2 && bean.getOfficeType() == 2; //0未发布，1发布，2下架,3:待完善
        isPublish = bean.getHouseStatus() == 1;
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
        if (isOpenSeats || !isPublish) {
            lp.height = context.getResources().getDimensionPixelSize(R.dimen.dp_200);
        } else {
            lp.height = context.getResources().getDimensionPixelSize(R.dimen.dp_260);
        }
        dialogWindow.setAttributes(lp);
        handleLayout(viewLayout, dialog);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void handleLayout(View viewLayout, Dialog dialog) {
        viewLayout.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        viewLayout.findViewById(R.id.rl_exit).setOnClickListener(v -> dialog.dismiss());
        TextView tvOff = viewLayout.findViewById(R.id.tv_off);
        TextView tvDelete = viewLayout.findViewById(R.id.tv_delete);
        if (isOpenSeats || !isPublish) {
            tvOff.setVisibility(View.GONE);
        } else {
            tvOff.setVisibility(View.VISIBLE);
        }
        tvOff.setOnClickListener(view -> {
            dialog.dismiss();
            listener.toMoreHouseManager(false, bean, position);
        });
        tvDelete.setOnClickListener(view -> {
            dialog.dismiss();
            if (isOpenSeats) {
                ToastUtils.toastForShort(context, "开放工位不支持删除");
                return;
            }
            listener.toMoreHouseManager(true, bean, position);
        });
    }
}

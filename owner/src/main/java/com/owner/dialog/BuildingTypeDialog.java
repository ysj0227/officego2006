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
 * 空调
 **/
public class BuildingTypeDialog {
    private Context context;

    public BuildingTypeListener getListener() {
        return listener;
    }

    public void setListener(BuildingTypeListener listener) {
        this.listener = listener;
    }

    private BuildingTypeListener listener;

    public BuildingTypeDialog(Context context) {
        this.context = context;
        moreDialog(context);
    }

    public interface BuildingTypeListener {
        void sureBuildingType(String type,int buildingType,boolean isOffice);
    }

    private void moreDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_building_type, null);
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
        lp.height = context.getResources().getDimensionPixelSize(R.dimen.dp_320);
        dialogWindow.setAttributes(lp);
        handleLayout(viewLayout, dialog);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void handleLayout(View viewLayout, Dialog dialog) {
        TextView tvType1 = viewLayout.findViewById(R.id.tv_type1);
        TextView tvType2 = viewLayout.findViewById(R.id.tv_type2);
        TextView tvType3 = viewLayout.findViewById(R.id.tv_type3);
        viewLayout.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        viewLayout.findViewById(R.id.rl_exit).setOnClickListener(v -> dialog.dismiss());
        tvType1.setOnClickListener(view -> {
            listener.sureBuildingType(tvType1.getText().toString(),1,true);
            dialog.dismiss();
        });
        tvType2.setOnClickListener(view -> {
            listener.sureBuildingType(tvType2.getText().toString(),3,false);
            dialog.dismiss();
        });
        tvType3.setOnClickListener(view -> {
            listener.sureBuildingType(tvType3.getText().toString(),6,false);
            dialog.dismiss();
        });
    }
}

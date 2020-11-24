package com.owner.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.view.CircleImage;
import com.officego.commonlib.view.ClearableEditText;
import com.owner.R;
import com.owner.mine.MineMessageActivity_;

/**
 * Created by shijie
 * Date 2020/10/15
 * 空调
 **/
public class CardDialog {
    private Context context;
    private UserMessageBean data;
    private CardDialogListener listener;

    public CardDialogListener getListener() {
        return listener;
    }

    public void setListener(CardDialogListener listener) {
        this.listener = listener;
    }

    public interface CardDialogListener {
        void requestPermission();
    }

    public CardDialog(Context context, UserMessageBean data) {
        this.context = context;
        this.data = data;
        moreDialog(context);
    }

    private void moreDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.dialog_mine_card, null);
        dialog.setContentView(viewLayout);
        handleLayout(viewLayout, dialog);
        dialog.setCancelable(true);
        dialog.show();
    }

    private void handleLayout(View viewLayout, Dialog dialog) {
        CircleImage civAvatar = viewLayout.findViewById(R.id.civ_avatar);
        ClearableEditText cetName = viewLayout.findViewById(R.id.cet_name);
        ClearableEditText cetJob = viewLayout.findViewById(R.id.cet_job);
        Button save = viewLayout.findViewById(R.id.btn_save);
        TextView tvMore = viewLayout.findViewById(R.id.tv_more);
        viewLayout.findViewById(R.id.rl_exit).setOnClickListener(v -> dialog.dismiss());
        cetName.setText(data.getNickname());
        cetJob.setText(data.getJob());
        Glide.with(context).load(data.getAvatar()).into(civAvatar);
        //头像
        civAvatar.setOnClickListener(view -> {
            BaseNotification.newInstance().postNotificationName(
                    CommonNotifications.identityModifyAvatar, "identityModifyAvatar");
        });
        //保存
        save.setOnClickListener(view -> {

        });
        //个人信息
        tvMore.setOnClickListener(view -> {
            MineMessageActivity_.intent(context).mUserInfo(data).start();
            dialog.dismiss();
        });
    }

    private void chooseImage() {
        final String[] items = {"拍照", "从相册选择"};
        new AlertDialog.Builder(context)
                .setItems(items, (dialogInterface, i) -> {
                    if (i == 0) {
//                        takePhoto();
                    } else if (i == 1) {
//                        openGallery();
                    }
                }).create().show();
    }

}

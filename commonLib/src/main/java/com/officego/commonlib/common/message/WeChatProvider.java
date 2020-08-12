package com.officego.commonlib.common.message;

/**
 * Created by YangShiJie
 * Data 2020/5/25.
 * Descriptions:
 **/

import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.officego.commonlib.R;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.view.ClearableEditText;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * PhoneInfo的布局
 */
@ProviderTag(messageContent = WeChatInfo.class, showPortrait = true, centerInHorizontal = true)
public class WeChatProvider extends IContainerItemProvider.MessageProvider<WeChatInfo> {
    private Context context;

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_message, viewGroup, false);
        WeChatHolder holder = new WeChatHolder();
        holder.rlContent = view.findViewById(R.id.rl_content);
        holder.rlBtn = view.findViewById(R.id.rl_btn);
        holder.ivIcon = view.findViewById(R.id.iv_icon);
        holder.tvContent = view.findViewById(R.id.tv_content);
        holder.vLine = view.findViewById(R.id.v_line);
        holder.btnAgree = view.findViewById(R.id.btn_agree);
        holder.btnReject = view.findViewById(R.id.btn_reject);
        holder.tvSend = view.findViewById(R.id.tv_send);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, WeChatInfo info, UIMessage uiMessage) {
        WeChatHolder holder = (WeChatHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {//接收显示同意拒绝
            String otherWx = info.getNumber();//请求方手机号
            String mineWx = SpUtils.getWechat();//接收方自己的微信
            holder.tvContent.setText(info.getContent());
            holder.ivIcon.setBackgroundResource(R.mipmap.ic_exchange_wechat);
            holder.rlContent.setVisibility(View.VISIBLE);
            holder.rlBtn.setVisibility(View.VISIBLE);
            holder.tvSend.setVisibility(View.GONE);
            //此时需要判断接收人微信是否已经绑定
            holder.btnAgree.setOnClickListener(v -> {
                if (TextUtils.isEmpty(mineWx)) {
                    weChatInputDialog(context, otherWx);
                } else {
                    BaseNotification.newInstance().postNotificationName(CommonNotifications.conversationWeChatAgree, otherWx, mineWx);
                }
            });
            holder.btnReject.setOnClickListener(v -> {
                BaseNotification.newInstance().postNotificationName(CommonNotifications.conversationWeChatReject, "conversation");
            });
        } else {//消息方向，自己发送的
            holder.tvSend.setText(info.getContent());
            holder.tvSend.setVisibility(View.VISIBLE);
            holder.rlBtn.setVisibility(View.GONE);
            holder.ivIcon.setVisibility(View.GONE);
            holder.vLine.setVisibility(View.GONE);
            holder.rlContent.setVisibility(View.GONE);
        }
    }

    private void setClickableFalse(WeChatHolder holder) {
        holder.btnReject.setEnabled(false);
        holder.btnReject.setClickable(false);
        holder.btnAgree.setEnabled(false);
        holder.btnAgree.setClickable(false);
    }

    public void weChatInputDialog(Context context, String otherWx) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        View viewLayout = LayoutInflater.from(context).inflate(R.layout.conversation_dialog_cantacts_input, null);
        //将布局设置给Dialog
        dialog.setContentView(viewLayout);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        if (dialogWindow == null) {
            return;
        }
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = CommonHelper.dp2px(context, 255);
        lp.height = CommonHelper.dp2px(context, 152);
        dialogWindow.setAttributes(lp);

        ClearableEditText etWx = viewLayout.findViewById(R.id.et_wechat);
        viewLayout.findViewById(R.id.btn_Confirm).setOnClickListener(v -> {
            String weChat = etWx.getText() == null ? "" : etWx.getText().toString().trim();
            if (TextUtils.isEmpty(weChat)) {
                ToastUtils.toastForShort(context, R.string.str_input_wechat);
            } else {
                dialog.dismiss();
                //发送微信信息
                BaseNotification.newInstance().postNotificationName(CommonNotifications.conversationWeChatAgree, otherWx, weChat);
            }
        });
        viewLayout.findViewById(R.id.btn_cancel).setOnClickListener(v ->
                dialog.dismiss());
        dialog.setCancelable(true);
        dialog.show();//显示对话框
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(WeChatInfo info) {
        return new SpannableString(info.getContent());
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, WeChatInfo info, UIMessage uiMessage) {

    }

    class WeChatHolder {
        RelativeLayout rlBtn;
        RelativeLayout rlContent;
        ImageView ivIcon;
        TextView tvContent;
        View vLine;
        Button btnAgree;
        Button btnReject;
        TextView tvSend;
    }

}
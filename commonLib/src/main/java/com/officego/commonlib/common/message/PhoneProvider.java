package com.officego.commonlib.common.message;

/**
 * Created by YangShiJie
 * Data 2020/5/25.
 * Descriptions:
 **/

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.officego.commonlib.R;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.notification.BaseNotification;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * PhoneInfo的布局
 */
@ProviderTag(messageContent = PhoneInfo.class, showPortrait = true, centerInHorizontal = true)
public class PhoneProvider extends IContainerItemProvider.MessageProvider<PhoneInfo> {
    private Context context;

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_message, viewGroup, false);
        PhoneHolder holder = new PhoneHolder();
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
    public void bindView(View view, int i, PhoneInfo info, UIMessage uiMessage) {
        PhoneHolder holder = (PhoneHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {//接收显示同意拒绝
            String oppositeSidePhone = info.getNumber();//请求方手机号
            String minePhone = SpUtils.getPhoneNum();//接收方自己手机
            holder.rlContent.setVisibility(View.VISIBLE);
            holder.rlBtn.setVisibility(View.VISIBLE);
            holder.tvSend.setVisibility(View.GONE);
            holder.tvContent.setText(info.getContent());
            holder.ivIcon.setBackgroundResource(R.mipmap.ic_exchange_phone);
            holder.btnAgree.setOnClickListener(v -> {
                BaseNotification.newInstance().postNotificationName(
                        CommonNotifications.conversationPhoneAgree, oppositeSidePhone, minePhone);
            });
            holder.btnReject.setOnClickListener(v -> {
                BaseNotification.newInstance().postNotificationName(
                        CommonNotifications.conversationPhoneReject, "conversation");
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
    private void setClickableFalse( PhoneHolder holder){
        holder.btnReject.setEnabled(false);
        holder.btnReject.setClickable(false);
        holder.btnAgree.setEnabled(false);
        holder.btnAgree.setClickable(false);
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(PhoneInfo phoneInfo) {
        return new SpannableString(phoneInfo.getContent());
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, PhoneInfo phoneInfo, UIMessage uiMessage) {

    }

    class PhoneHolder {
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
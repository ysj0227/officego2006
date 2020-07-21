package com.officego.commonlib.common.message;

/**
 * Created by YangShiJie
 * Data 2020/5/25.
 * Descriptions:
 **/

import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.officego.commonlib.R;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.notification.BaseNotification;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * PhoneInfo的布局
 */
@ProviderTag(messageContent = IdentityApplyInfo.class, showPortrait = true, centerInHorizontal = true)
public class IdentityApplyProvider extends IContainerItemProvider.MessageProvider<IdentityApplyInfo> {
    private Context context;

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_message_identity, viewGroup, false);
        IdentityHolder holder = new IdentityHolder();
        holder.rlBtn = view.findViewById(R.id.rl_btn);
        holder.tvContent = view.findViewById(R.id.tv_content);
        holder.vLine = view.findViewById(R.id.v_line);
        holder.btnAgree = view.findViewById(R.id.btn_agree);
        holder.btnReject = view.findViewById(R.id.btn_reject);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, IdentityApplyInfo info, UIMessage uiMessage) {
        IdentityHolder holder = (IdentityHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {//接收显示同意拒绝
            holder.rlBtn.setVisibility(View.VISIBLE);
            holder.tvContent.setText(info.getContent());
            holder.btnAgree.setOnClickListener(v -> {
                //TODO 接口
                BaseNotification.newInstance().postNotificationName(
                        CommonNotifications.conversationIdApplyAgree, "conversationIdApplyAgree");
            });
            holder.btnReject.setOnClickListener(v -> {
                //TODO 接口
                BaseNotification.newInstance().postNotificationName(
                        CommonNotifications.conversationIdApplyReject, "conversationIdApplyReject");
            });
        } else {//消息方向，自己发送的
            holder.tvContent.setText("你已申请加入TA的公司等待对方同意");
            holder.rlBtn.setVisibility(View.GONE);
            holder.vLine.setVisibility(View.GONE);
        }
    }
    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(IdentityApplyInfo phoneInfo) {
        return null;
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, IdentityApplyInfo phoneInfo, UIMessage uiMessage) {

    }

    class IdentityHolder {
        RelativeLayout rlBtn;
        TextView tvContent;
        View vLine;
        Button btnAgree;
        Button btnReject;
    }

}
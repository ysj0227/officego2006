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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.officego.commonlib.R;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * ViewingDateStatusInfo的布局
 */
@ProviderTag(messageContent = IdentityApplyStatusInfo.class, showPortrait = false, centerInHorizontal = true)
public class IdentityApplyStatusProvider extends IContainerItemProvider.MessageProvider<IdentityApplyStatusInfo> {
    private Context context;

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_viewing_date_message_status, viewGroup, false);
        IdStatusInfoHolder holder = new IdStatusInfoHolder();
        holder.rlLayout = view.findViewById(R.id.rl_layout);
        holder.tvStatus = view.findViewById(R.id.tv_status);
        ViewGroup.LayoutParams params = holder.rlLayout.getLayoutParams();
        params.width = 400;
        holder.rlLayout.setLayoutParams(params);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, IdentityApplyStatusInfo info, UIMessage uiMessage) {
        IdStatusInfoHolder holder = (IdStatusInfoHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {//接收显示同意拒绝
            if (info.isAgree()) {
                holder.tvStatus.setText("已同意");
            } else {
                holder.tvStatus.setText("已拒绝");
            }
        } else {//消息方向，自己发送的
            if (info.isAgree()) {
                holder.tvStatus.setText("已同意");
            } else {
                holder.tvStatus.setText("已拒绝");
            }
        }
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(IdentityApplyStatusInfo info) {
        return null;
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, IdentityApplyStatusInfo info, UIMessage uiMessage) {

    }

    class IdStatusInfoHolder {
        RelativeLayout rlLayout;
        TextView tvStatus;
    }
}
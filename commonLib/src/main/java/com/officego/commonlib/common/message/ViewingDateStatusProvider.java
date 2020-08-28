package com.officego.commonlib.common.message;

/**
 * Created by YangShiJie
 * Data 2020/5/25.
 * Descriptions:
 **/

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.officego.commonlib.R;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * ViewingDateStatusInfo的布局
 */
@ProviderTag(messageContent = ViewingDateStatusInfo.class, showPortrait = false, centerInHorizontal = true)
public class ViewingDateStatusProvider extends IContainerItemProvider.MessageProvider<ViewingDateStatusInfo> {
    private Context context;
    private String mMessage = "";

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_viewing_date_message_status, viewGroup, false);
        ViewingDateStatusInfoHolder holder = new ViewingDateStatusInfoHolder();
        holder.tvStatus = view.findViewById(R.id.tv_status);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, ViewingDateStatusInfo info, UIMessage uiMessage) {
        ViewingDateStatusInfoHolder holder = (ViewingDateStatusInfoHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {//接收显示同意拒绝
            if (info.isAgree()) {
                holder.tvStatus.setText("对方已同意看房邀约");
            } else {
                holder.tvStatus.setText("对方已拒绝看房邀约");
            }
        } else {//消息方向，自己发送的
            if (info.isAgree()) {
                holder.tvStatus.setText("已同意");
            } else {
                holder.tvStatus.setText("已拒绝");
            }
        }
        mMessage = holder.tvStatus.getText().toString();
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(ViewingDateStatusInfo info) {
        return new SpannableString(TextUtils.isEmpty(mMessage) ? "消息" : mMessage);
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, ViewingDateStatusInfo info, UIMessage uiMessage) {

    }

    class ViewingDateStatusInfoHolder {
        TextView tvStatus;
    }

}
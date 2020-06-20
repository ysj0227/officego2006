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

import androidx.core.content.ContextCompat;

import com.officego.commonlib.R;
import com.officego.commonlib.view.CopyTextUtils;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * info的布局
 */
@ProviderTag(messageContent = EcWeChatStatusInfo.class, showPortrait = false, centerInHorizontal = true)
public class EcWeChatStatusProvider extends IContainerItemProvider.MessageProvider<EcWeChatStatusInfo> {
    private Context context;

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_exchange_wx, viewGroup, false);
        EcWeChatStatusHolder holder = new EcWeChatStatusHolder();
        holder.rl_layout = view.findViewById(R.id.rl_layout);
        holder.rlBtn = view.findViewById(R.id.rl_btn);
        holder.tv_title = view.findViewById(R.id.tv_title);
        holder.tv_content = view.findViewById(R.id.tv_content);
        holder.v_line = view.findViewById(R.id.v_line);
        holder.btn_copy_contacts = view.findViewById(R.id.btn_copy_contacts);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, EcWeChatStatusInfo info, UIMessage uiMessage) {
        EcWeChatStatusHolder holder = (EcWeChatStatusHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {//receive
            if (info.isAgree()) {
                //我同意了对方的请求 我同意和您交换微信号
                String wxNum = info.getReceiveNumber();
                holder.tv_title.setText(info.getContent());
                holder.tv_content.setText("对方微信：" + wxNum);
                holder.rlBtn.setVisibility(View.VISIBLE);
                holder.v_line.setVisibility(View.VISIBLE);
                holder.tv_content.setVisibility(View.VISIBLE);
                //复制文本
                holder.btn_copy_contacts.setOnClickListener(v ->
                        new CopyTextUtils(context, wxNum));
            } else {
                //对方拒绝了我的请求
                holder.tv_title.setText(info.getContent());
                holder.rlBtn.setVisibility(View.GONE);
                holder.v_line.setVisibility(View.GONE);
                holder.tv_content.setVisibility(View.GONE);
                holder.rl_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            }
        } else {//send
            if (info.isAgree()) {
                //我同意了对方的请求 我同意和您交换微信号
                String wxNum = info.getSendNumber();
                holder.tv_title.setText(info.getContent());
                holder.tv_content.setText("对方微信：" + wxNum);
                holder.rlBtn.setVisibility(View.VISIBLE);
                holder.v_line.setVisibility(View.VISIBLE);
                holder.tv_content.setVisibility(View.VISIBLE);
                //复制文本
                holder.btn_copy_contacts.setOnClickListener(v ->
                        new CopyTextUtils(context, wxNum));
            } else {
                //我拒绝了对方要手机的请求
                holder.tv_title.setText(info.getContent());
                holder.rlBtn.setVisibility(View.GONE);
                holder.v_line.setVisibility(View.GONE);
                holder.tv_content.setVisibility(View.GONE);
                holder.rl_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            }
        }
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(EcWeChatStatusInfo info) {
        return null;
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, EcWeChatStatusInfo info, UIMessage uiMessage) {

    }

    class EcWeChatStatusHolder {
        RelativeLayout rl_layout;
        RelativeLayout rlBtn;
        TextView tv_title;
        TextView tv_content;
        View v_line;
        Button btn_copy_contacts;
    }

}
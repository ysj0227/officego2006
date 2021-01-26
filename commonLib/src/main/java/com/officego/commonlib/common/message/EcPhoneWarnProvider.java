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
import android.widget.TextView;

import com.officego.commonlib.R;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * TimeTipInfo的布局
 */
@ProviderTag(messageContent = EcPhoneWarnInfo.class, showPortrait = false, centerInHorizontal = true)
public class EcPhoneWarnProvider extends IContainerItemProvider.MessageProvider<EcPhoneWarnInfo> {
    private final String con="为避免电话被频繁骚扰，请谨慎交换电话";

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_phone_warn_message, viewGroup, false);
        EcPhoneWarnHolder holder = new EcPhoneWarnHolder();
        holder.tvContent = view.findViewById(R.id.tv_content);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, EcPhoneWarnInfo info, UIMessage uiMessage) {
        EcPhoneWarnHolder holder = (EcPhoneWarnHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(con);
        } else {
            holder.tvContent.setVisibility(View.GONE);
        }
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(EcPhoneWarnInfo info) {
        return new SpannableString(con);
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, EcPhoneWarnInfo info, UIMessage uiMessage) {
    }

    static class EcPhoneWarnHolder {
        TextView tvContent;
    }
}
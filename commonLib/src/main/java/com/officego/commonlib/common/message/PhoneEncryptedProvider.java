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
 * PhoneEncryptedInfo的布局
 */
@ProviderTag(messageContent = PhoneEncryptedInfo.class, showPortrait = false, centerInHorizontal = true)
public class PhoneEncryptedProvider extends IContainerItemProvider.MessageProvider<PhoneEncryptedInfo> {
    private final String str = "平台暂不支持直接发送手机号、微信等联系方式，请使用页面上交换功能";

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_phone_enterypted_message, viewGroup, false);
        PhoneEncryptedHolder holder = new PhoneEncryptedHolder();
        holder.tvContent = view.findViewById(R.id.tv_content);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, PhoneEncryptedInfo info, UIMessage uiMessage) {
        PhoneEncryptedHolder holder = (PhoneEncryptedHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {
            holder.tvContent.setText(str);
        }
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(PhoneEncryptedInfo PhoneEncryptedInfo) {
        return new SpannableString(str);
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, PhoneEncryptedInfo PhoneEncryptedInfo, UIMessage uiMessage) {
    }

    class PhoneEncryptedHolder {
        TextView tvContent;
    }
}
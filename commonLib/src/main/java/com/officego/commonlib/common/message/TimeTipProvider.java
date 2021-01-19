package com.officego.commonlib.common.message;

/**
 * Created by YangShiJie
 * Data 2020/5/25.
 * Descriptions:
 **/

import android.content.Context;
import android.text.Html;
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
@ProviderTag(messageContent = TimeTipInfo.class, showPortrait = false, centerInHorizontal = true)
public class TimeTipProvider extends IContainerItemProvider.MessageProvider<TimeTipInfo> {
    private final CharSequence source = Html.fromHtml(
            "当前是非工作时段，房东可能不在线或回复较慢哦，您可以在" + "<font color='#FDA82A'>9:00-18:00</font>再来试试");

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_time_tip_message, viewGroup, false);
        TimeTipHolder holder = new TimeTipHolder();
        holder.tvContent = view.findViewById(R.id.tv_content);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, TimeTipInfo info, UIMessage uiMessage) {
        TimeTipHolder holder = (TimeTipHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {
            holder.tvContent.setText(source);
        }
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(TimeTipInfo PhoneEncryptedInfo) {
        return new SpannableString(source);
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, TimeTipInfo info, UIMessage uiMessage) {
    }

    static class TimeTipHolder {
        TextView tvContent;
    }
}
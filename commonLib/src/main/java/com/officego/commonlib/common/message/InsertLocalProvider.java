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
import android.widget.TextView;

import com.officego.commonlib.R;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;

/**
 * PhoneEncryptedInfo的布局
 */
@ProviderTag(messageContent = InsertLocalInfo.class, showPortrait = false, centerInHorizontal = true)
public class InsertLocalProvider extends IContainerItemProvider.MessageProvider<InsertLocalInfo> {
    private final String str = "";

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_local_message, viewGroup, false);
        LocalHolder holder = new LocalHolder();
        holder.tvContent = view.findViewById(R.id.tv_content);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, InsertLocalInfo info, UIMessage uiMessage) {
        LocalHolder holder = (LocalHolder) view.getTag();
        holder.tvContent.setVisibility(View.GONE);
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(InsertLocalInfo info) {
        return null;
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, InsertLocalInfo info, UIMessage uiMessage) {
    }

    class LocalHolder {
        TextView tvContent;
    }
}
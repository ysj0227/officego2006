package com.officego.commonlib.common.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.officego.commonlib.CommonListAdapter;
import com.officego.commonlib.R;
import com.officego.commonlib.ViewHolder;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.GlideUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.RoundImageView;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by YangShiJie
 * Data 2020/5/9.
 * Descriptions:
 **/
public class ChatListAdapter extends CommonListAdapter<ChatListBean.ListBean> {

    private Context context;
    private Conversation.ConversationType conversationType;
    private String targetId;


    public ChatListAdapter(Context context, List<ChatListBean.ListBean> list) {
        super(context, R.layout.item_chat_list, list);
        this.context = context;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void convert(ViewHolder holder, final ChatListBean.ListBean bean) {
        RoundImageView rivAvatar = holder.getView(R.id.riv_avatar);
        Glide.with(context).applyDefaultRequestOptions(GlideUtils.options()).load(bean.getAvatar()).into(rivAvatar);
        holder.setText(R.id.tv_name, bean.getNickname());
        holder.setText(R.id.tv_message, "消息");
        holder.setText(R.id.tv_date, "2020-11-12");
        TextView tvUnread = holder.getView(R.id.tv_unread);

        holder.itemView.setOnClickListener(view -> {
            String targetId = bean.getChattedId();
            Conversation.ConversationType conversationType;
            if (!TextUtils.isEmpty(targetId)) {
                if (TextUtils.equals(Constants.TYPE_SYSTEM, targetId) ||
                        (targetId.length() > 1 && TextUtils.equals(Constants.TYPE_SYSTEM, targetId.substring(targetId.length() - 1)))) {
                    conversationType = Conversation.ConversationType.SYSTEM;
                } else {
                    conversationType = Conversation.ConversationType.PRIVATE;
                }
                RongIM.getInstance().startConversation(context, conversationType, targetId, bean.getNickname());
            }
        });

        targetId = bean.getChattedId();
        if (!TextUtils.isEmpty(targetId)) {
            if (TextUtils.equals(Constants.TYPE_SYSTEM, targetId) ||
                    (targetId.length() > 1 && TextUtils.equals(Constants.TYPE_SYSTEM, targetId.substring(targetId.length() - 1)))) {
                conversationType = Conversation.ConversationType.SYSTEM;
            } else {
                conversationType = Conversation.ConversationType.PRIVATE;
            }
        }
        RongIMClient.getInstance().getUnreadCount(conversationType, targetId,
                new RongIMClient.ResultCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer unReadCount) {
                        LogCat.e("TAG","11111 unReadCount="+unReadCount);
                        if (unReadCount > 0) {
                            tvUnread.setVisibility(View.VISIBLE);
                            tvUnread.setText(unReadCount + "");
                        } else {
                            tvUnread.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode ErrorCode) {

                    }
                });
    }

}

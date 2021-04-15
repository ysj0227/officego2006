package com.officego.ui.message;

import android.content.Context;

import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.model.Message;

public class MyMessageListAdapter extends MessageListAdapter {

    public MyMessageListAdapter(Context context) {
        super(context);
    }

    /**
     * 多选状态时是否显示checkBox，开发者需要重写此方法，根据消息类型判断是否显示。
     *
     * @param message 消息类型
     * @return true 显示，false 不显示
     */
    @Override
    protected boolean allowShowCheckButton(Message message){
        return true;
    }
}

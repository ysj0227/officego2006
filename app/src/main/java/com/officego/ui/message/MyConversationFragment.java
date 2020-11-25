package com.officego.ui.message;

import android.view.View;

import io.rong.imkit.fragment.ConversationFragment;

/**
 * Created by shijie
 * Date 2020/11/20
 **/
public class MyConversationFragment extends ConversationFragment {

    /**
     * 发送按钮监听
     *
     * @param view 发送控件
     * @param text 发送文本
     */
    @Override
    public void onSendToggleClick(View view, String text) {
//        if (TextUtils.equals("159",text)){
//            //todo 发送自定义消息
//            text="****";
//        }
        super.onSendToggleClick(view, text);
    }
}
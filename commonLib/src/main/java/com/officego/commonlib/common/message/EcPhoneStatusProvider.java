package com.officego.commonlib.common.message;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
 * Created by YangShiJie
 * Data 2020/5/25.
 * Descriptions:
 * ExPhoneStatusInfo的布局 交换手机是否接受的状态
 */
@ProviderTag(messageContent = EcPhoneStatusInfo.class, showPortrait = false, centerInHorizontal = true)
public class EcPhoneStatusProvider extends IContainerItemProvider.MessageProvider<EcPhoneStatusInfo> {
    private Context context;

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_exchange_phone, viewGroup, false);
        EcPhoneStatusHolder holder = new EcPhoneStatusHolder();
        holder.rl_layout = view.findViewById(R.id.rl_layout);
        holder.rlBtn = view.findViewById(R.id.rl_btn);
        holder.tv_title = view.findViewById(R.id.tv_title);
        holder.tv_content = view.findViewById(R.id.tv_content);
        holder.v_line = view.findViewById(R.id.v_line);
        holder.v_vertical_line = view.findViewById(R.id.v_vertical_line);
        holder.btn_copy_contacts = view.findViewById(R.id.btn_copy_contacts);
        holder.btn_call = view.findViewById(R.id.btn_call);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, EcPhoneStatusInfo info, UIMessage uiMessage) {
        EcPhoneStatusHolder holder = (EcPhoneStatusHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {//receive
            if (info.isAgree()) {
                //我同意了对方的请求
                String phone = info.getReceiveNumber();
                holder.tv_title.setText(info.getContent());
                holder.tv_content.setText("对方电话：" + phone);
                holder.rlBtn.setVisibility(View.VISIBLE);
                holder.v_line.setVisibility(View.VISIBLE);
                holder.tv_content.setVisibility(View.VISIBLE);
                buttonOperate(holder, phone);
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
                //我同意了对方的请求
                String phone = info.getSendNumber();
                holder.tv_title.setText(info.getContent());
                holder.tv_content.setText("对方电话：" + phone);
                holder.rlBtn.setVisibility(View.VISIBLE);
                holder.v_line.setVisibility(View.VISIBLE);
                holder.tv_content.setVisibility(View.VISIBLE);
                buttonOperate(holder, phone);
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

    private void buttonOperate(EcPhoneStatusHolder holder, String phone) {
        //复制文本
        holder.btn_copy_contacts.setOnClickListener(v ->
                new CopyTextUtils(context, phone));
        //拨打电话
        holder.btn_call.setOnClickListener(v -> callPhone(phone));
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(EcPhoneStatusInfo info) {
        return null;
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, EcPhoneStatusInfo info, UIMessage uiMessage) {

    }

    class EcPhoneStatusHolder {
        RelativeLayout rl_layout;
        RelativeLayout rlBtn;
        TextView tv_title;
        TextView tv_content;
        View v_line, v_vertical_line;
        Button btn_copy_contacts;
        Button btn_call;
    }
}
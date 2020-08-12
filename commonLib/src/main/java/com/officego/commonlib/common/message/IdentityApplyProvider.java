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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.officego.commonlib.R;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * PhoneInfo的布局
 */
@ProviderTag(messageContent = IdentityApplyInfo.class, showPortrait = true, centerInHorizontal = true)
public class IdentityApplyProvider extends IContainerItemProvider.MessageProvider<IdentityApplyInfo> {
    private Context context;
    private String mMessage = "";

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_message_identity, viewGroup, false);
        IdentityHolder holder = new IdentityHolder();
        holder.rlBtn = view.findViewById(R.id.rl_btn);
        holder.tvContent = view.findViewById(R.id.tv_content);
        holder.vLine = view.findViewById(R.id.v_line);
        holder.btnAgree = view.findViewById(R.id.btn_agree);
        holder.btnReject = view.findViewById(R.id.btn_reject);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, IdentityApplyInfo info, UIMessage uiMessage) {
        IdentityHolder holder = (IdentityHolder) view.getTag();
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {//接收显示同意拒绝
            holder.rlBtn.setVisibility(View.VISIBLE);
            holder.tvContent.setText(info.getContent());
            //1通过2取消
            holder.btnAgree.setOnClickListener(v ->
                    updateAuditStatus(true, info.getExtraMessage(), info.getId(), info.getLicenceId(), 1));
            holder.btnReject.setOnClickListener(v ->
                    updateAuditStatus(false, info.getExtraMessage(), info.getId(), info.getLicenceId(), 2));
        } else {//消息方向，自己发送的
            holder.tvContent.setText("你已申请加入TA的公司等待对方同意");
            holder.rlBtn.setVisibility(View.GONE);
            holder.vLine.setVisibility(View.GONE);
        }
        mMessage = holder.tvContent.getText().toString();
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(IdentityApplyInfo phoneInfo) {
        return new SpannableString(mMessage);
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, IdentityApplyInfo phoneInfo, UIMessage uiMessage) {

    }

    class IdentityHolder {
        RelativeLayout rlBtn;
        TextView tvContent;
        View vLine;
        Button btnAgree;
        Button btnReject;
    }

    /**
     * 同意拒绝申请加入 认证
     */
    private void updateAuditStatus(boolean isAgree, String identityType, int id, int licenceId, int auditStatus) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            ToastUtils.toastForShort(context, R.string.str_check_net);
            return;
        }
        if (TextUtils.isEmpty(identityType)) {
            return;
        }
        OfficegoApi.getInstance().updateAuditStatusIdentity(Integer.valueOf(identityType), id, licenceId, auditStatus,
                new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        if (isAgree) {
                            BaseNotification.newInstance().postNotificationName(
                                    CommonNotifications.conversationIdApplyAgree, "conversationIdApplyAgree");
                        } else {
                            BaseNotification.newInstance().postNotificationName(
                                    CommonNotifications.conversationIdApplyReject, "conversationIdApplyReject");
                        }
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        LogCat.e("TAG", "11111 updateAuditStatusIdentity code code=" + code);
                        if (code == Constants.DEFAULT_ERROR_CODE ||
                                code == Constants.ERROR_CODE_5002 ||
                                code == Constants.ERROR_CODE_5007) {
                            ToastUtils.toastForShort(context, msg);
                        }
                    }
                });
    }
}
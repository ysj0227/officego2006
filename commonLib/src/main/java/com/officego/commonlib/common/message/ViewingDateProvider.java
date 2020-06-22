package com.officego.commonlib.common.message;

/**
 * Created by YangShiJie
 * Data 2020/5/25.
 * Descriptions:
 **/

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.officego.commonlib.R;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * ViewingDateInfo的布局
 */
@ProviderTag(messageContent = ViewingDateInfo.class, showPortrait = true, centerInHorizontal = false)
public class ViewingDateProvider extends IContainerItemProvider.MessageProvider<ViewingDateInfo> {
    private Context context;

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_item_viewing_date_message, viewGroup, false);
        ViewingDateInfoHolder holder = new ViewingDateInfoHolder();
        holder.rlContent = view.findViewById(R.id.rl_content);
        holder.rlBtn = view.findViewById(R.id.rl_btn);
        holder.ivIcon = view.findViewById(R.id.iv_icon);
        holder.tvContent = view.findViewById(R.id.tv_content);
        holder.tvBuildingName = view.findViewById(R.id.tv_building_name);
        holder.tvAddress = view.findViewById(R.id.tv_address);
        holder.vLine = view.findViewById(R.id.v_line);
        holder.btnAgree = view.findViewById(R.id.btn_agree);
        holder.btnReject = view.findViewById(R.id.btn_reject);
        holder.tvTime = view.findViewById(R.id.tv_time);
        view.setTag(holder);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(View view, int i, ViewingDateInfo info, UIMessage uiMessage) {
        ViewingDateInfoHolder holder = (ViewingDateInfoHolder) view.getTag();
        LogCat.d("TAG", "1111 msg=" + info.getContent() + " exc= " + info.getExtraMessage() + " id=" + info.getId());
        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {//接收显示同意拒绝
            holder.tvContent.setText("你收到一个预约看房消息");
            holder.rlBtn.setVisibility(View.VISIBLE);
            holder.ivIcon.setVisibility(View.VISIBLE);
            holder.vLine.setVisibility(View.VISIBLE);
            //预约看房同意  行程id  1预约成功(同意)2预约失败(拒绝)
            holder.btnAgree.setOnClickListener(v -> updateAuditStatus(holder, info.getId(), 1));
            //预约看房拒绝  行程id  1预约成功(同意)2预约失败(拒绝)
            holder.btnReject.setOnClickListener(v -> updateAuditStatus(holder, info.getId(), 2));
        } else {//消息方向自己发送
            holder.tvContent.setText("你发起了一个看房邀约，等待对方接受");
            holder.rlBtn.setVisibility(View.GONE);
            holder.ivIcon.setVisibility(View.GONE);
            holder.vLine.setVisibility(View.GONE);
        }
        holder.tvBuildingName.setText("名称：" + info.getBuildingName());
        holder.tvAddress.setText("地址：" + info.getBuildingAddress());
        holder.tvTime.setText("约看时间：" +
                DateTimeUtils.StampToDate(info.getTime() + "000", "yyyy-MM-dd HH:mm"));
    }

    @Override //这里意思是你的这个自定义消息显示的内容
    public Spannable getContentSummary(ViewingDateInfo info) {
        return null;
    }

    @Override  //点击你的自定义消息执行的操作
    public void onItemClick(View view, int i, ViewingDateInfo info, UIMessage uiMessage) {

    }

    class ViewingDateInfoHolder {
        RelativeLayout rlBtn;
        RelativeLayout rlContent;
        ImageView ivIcon;
        TextView tvContent;
        TextView tvBuildingName;
        TextView tvAddress;
        TextView tvTime;
        View vLine;
        Button btnAgree;
        Button btnReject;
    }

    /**
     * 同意拒绝预约看房
     */
    private void updateAuditStatus(ViewingDateInfoHolder holder, int id, int auditStatus) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            ToastUtils.toastForShort(context, R.string.str_check_net);
            return;
        }
        OfficegoApi.getInstance().updateAuditStatus(id, auditStatus,
                new RetrofitCallback<Object>() {
                    @Override
                    public void onSuccess(int code, String msg, Object data) {
                        LogCat.e("TAG", "1111 updateAuditStatus onSuccess");
                        //1预约成功(同意)2预约失败(拒绝)
                        if (auditStatus == 1) {
                            holder.btnAgree.setText("已同意");
                            BaseNotification.newInstance().postNotificationName(
                                    CommonNotifications.conversationViewHouseAgree, "conversation");
                        } else {
                            holder.btnReject.setText("已拒绝");
                            BaseNotification.newInstance().postNotificationName(
                                    CommonNotifications.conversationViewHouseReject, "conversation");
                        }
                        //此时按钮不可操作
                        holder.btnAgree.setEnabled(false);
                        holder.btnAgree.setClickable(false);
                        holder.btnReject.setEnabled(false);
                        holder.btnReject.setClickable(false);
                        holder.btnAgree.setTextColor(ContextCompat.getColor(context, R.color.text_disable));
                        holder.btnReject.setTextColor(ContextCompat.getColor(context, R.color.text_disable));
                    }

                    @Override
                    public void onFail(int code, String msg, Object data) {
                        LogCat.e("TAG", "1111 updateAuditStatus onFail code=" + code + "  msg=" + msg);
                        if (code == 5000) {
                            ToastUtils.toastForShort(context, msg);
                        }
                    }
                });
    }

}
package com.officego.ui.message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.contract.ConversationContract;
import com.officego.commonlib.common.dialog.ConfirmDialog;
import com.officego.commonlib.common.dialog.InputContactsDialog;
import com.officego.commonlib.common.message.BuildingInfo;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.IdentitychattedMsgBean;
import com.officego.commonlib.common.model.RongUserInfoBean;
import com.officego.commonlib.common.presenter.ConversationPresenter;
import com.officego.commonlib.common.rongcloud.RongCloudSetUserInfoUtils;
import com.officego.commonlib.common.rongcloud.SendMessageManager;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.db.LitepalUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import java.util.Objects;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by YangShiJie
 * Data 2020/5/22.
 * Descriptions:
 **/
@SuppressLint("Registered")
@EActivity(R.layout.conversation)
public class ConversationActivity extends BaseMvpActivity<ConversationPresenter>
        implements ConversationContract.View, RongIM.OnSendMessageListener {
    private LinearLayout llRoot;
    private ConstraintLayout ctlChat;
    private TextView tvTitleName, tvJob;
    private String getHouseChatId; //去除 targetId  的最后一位 ,产品定义
    @Extra
    String targetId;//融云的id
    @Extra
    int buildingId;//楼盘详情传入
    @Extra
    int houseId;//房源详情传入

    private ChatHouseBean mData;
    // "isChat": 0 :点击发送按钮的时候需要调用 addChat接口，1:不需要
    private boolean isFirstChat = true;//是否第一次聊天

    private boolean isSendApply;//租户认证发送的申请

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new ConversationPresenter();
        mPresenter.attachView(this);
        llRoot = findViewById(R.id.ll_root);
        ctlChat = findViewById(R.id.ctl_chat);
        tvTitleName = findViewById(R.id.tv_title_name);
        tvJob = findViewById(R.id.tv_job);
        llRoot.setPadding(0, CommonHelper.statusHeight(this), 0, 0);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("chatTargetId")) {//聊天认证申请进入
            //认证申请页面进入
            isSendApply = intent.hasExtra("isSendApply");
            targetId = intent.getStringExtra("chatTargetId");
            ctlChat.setVisibility(View.GONE);
            mPresenter.identityChattedMsg(targetId);
            initIM();
        } else if (intent != null && intent.hasExtra("systemPushTargetId")) {
            //系统消息
            targetId = intent.getStringExtra("systemPushTargetId");
            ctlChat.setVisibility(View.GONE);
            tvTitleName.setText("系统消息");
            tvTitleName.setPadding(0, 28, 0, 0);
            initIM();
        } else {
            initIMInfo();
            //认证申请聊天列表进入
            if (TextUtils.equals(Constants.TYPE_OWNER, targetId.substring(targetId.length() - 1)) &&
                    TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRongChatId().substring(SpUtils.getRongChatId().length() - 1))) {
                //认证申请聊天列表进入,融云id最后一位是“1”
                isSendApply = false;
                ctlChat.setVisibility(View.GONE);
                mPresenter.identityChattedMsg(targetId);
                initIM();
            } else if (TextUtils.equals(Constants.TYPE_SYSTEM, targetId.substring(targetId.length() - 1))) {
                //系统消息聊天列表进入
                ctlChat.setVisibility(View.GONE);
                tvTitleName.setText("系统消息");
                tvTitleName.setPadding(0, 28, 0, 0);
                initIM();
            } else {
                //租户-业主聊天
                isSendApply = false;
                ctlChat.setVisibility(View.VISIBLE);
                initIM();
                RongIM.getInstance().setSendMessageListener(this);
                mPresenter.getHouseDetails(buildingId, houseId, getHouseChatId);
            }
        }
    }

    @Click(R.id.rl_back)
    void backClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (isSendApply) {
            //发送认证 ，返回业主个人中心
            GotoActivityUtils.mainOwnerDefMainActivity(context);
        } else {
            super.onBackPressed();
        }
    }

    private void initIMInfo() {
        if (TextUtils.isEmpty(targetId)) {
            targetId = Objects.requireNonNull(getIntent().getData()).getQueryParameter("targetId");
        }
        assert targetId != null;
        getHouseChatId = targetId.substring(0, targetId.length() - 1);
    }

    private void initIM() {
        FragmentManager fragmentManage = getSupportFragmentManager();
        ConversationFragment fragment = (ConversationFragment) fragmentManage.findFragmentById(R.id.conversation);

        Uri uri;
        if (!TextUtils.isEmpty(targetId) &&
                TextUtils.equals(Constants.TYPE_SYSTEM, targetId.substring(targetId.length() - 1))) {
            //系统消息
            findViewById(R.id.rc_extension).setVisibility(View.INVISIBLE);
            uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversation")
                    .appendPath(Conversation.ConversationType.SYSTEM.getName().toLowerCase())
                    .appendQueryParameter("targetId", targetId).build();
        } else {
            //聊天消息
            findViewById(R.id.rc_extension).setVisibility(View.VISIBLE);
            uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversation")
                    .appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                    .appendQueryParameter("targetId", targetId).build();
        }
        if (fragment != null) {
            fragment.setUri(uri);
        }
    }

    /**
     * 插入聊天大楼信息
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void houseSuccess(ChatHouseBean data) {
        if (data == null) {
            getRongTargetInfo(targetId); //推送点击获取Target用户信息
            return;
        }
        mData = data;
        isFirstChat = data.getIsChat() == 0;//是否第一次聊天
        if (data.getBuilding() == null) {
            return;
        }
        //刷新用户信息
        refreshChatUserInfo(data);
        //是否插入消息
        String mValue = SpUtils.getUserId() + data.getBuilding().getBuildingId() +
                (data.getBuilding().getHouseId() == null ? "" : data.getBuilding().getHouseId()) + targetId;
        if (LitepalUtils.getBuildingInfo(mValue) != null &&
                TextUtils.equals(mValue, LitepalUtils.getBuildingInfo(mValue).getBuildingValue())) {
            return;
        }
        //租户第一次聊天,发送默认消息
        if (isFirstChat && TextUtils.equals(Constants.TYPE_TENANT, SpUtils.getRole())) {
            SendMessageManager.getInstance().sendTextMessage(targetId, "我对你发布的房源有兴趣，能聊聊吗？");
        }
        //插入消息
        SendMessageManager.getInstance().insertIncomingMessage(info(data), targetId, SpUtils.getRongChatId());
        //保存第一次插入状态
        String value = SpUtils.getUserId() + data.getBuilding().getBuildingId() +
                (data.getBuilding().getHouseId() == null ? "" : String.valueOf(data.getBuilding().getHouseId())) + targetId;
        LitepalUtils.saveBuilding(value);
    }

    //刷新用户信息
    private void refreshChatUserInfo(ChatHouseBean data) {
        RongCloudSetUserInfoUtils.refreshUserInfoCache(targetId, data.getChatted().getNickname(), data.getChatted().getAvatar());
        RongCloudSetUserInfoUtils.refreshUserInfoCache(SpUtils.getRongChatId(), SpUtils.getNickName(), SpUtils.getHeaderImg());
        tvTitleName.setText(data.getChatted().getNickname());
        tvJob.setText(data.getChatted().getJob());
    }

    //插入楼盘信息
    private BuildingInfo info(ChatHouseBean data) {
        BuildingInfo info = new BuildingInfo();
        info.setbuildingName(data.getBuilding().getBuildingName());
        info.setImgUrl(data.getBuilding().getMainPic());
        String showUser = TextUtils.equals(SpUtils.getUserId(), data.getCreateUser()) ? "你" : "对方";
        String mDate;
        if (data.getCreateTime() == 0) {
            mDate = DateTimeUtils.getStringDateTimeByStringPattern(DateTimeUtils.DateTimePattern.LONG_DATETIME_2);
        } else {
            mDate = DateTimeUtils.secondToDate(data.getCreateTime(), "yyyy-MM-dd HH:mm");
        }
        info.setCreateTime(mDate + " 由" + showUser + "发起沟通");
        info.setDistrict(data.getBuilding().getAddress());
        if (data.getBuilding().getStationline().size() > 0) {
            String workTime = data.getBuilding().getNearbySubwayTime().get(0);
            String stationLine = data.getBuilding().getStationline().get(0);
            String stationName = data.getBuilding().getStationNames().get(0);
            info.setRouteMap("步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName);
        }
        if (data.getBuilding().getMinSinglePrice() != null) {
            if (data.getBuilding().getBtype() == Constants.TYPE_BUILDING) {
                info.setMinSinglePrice("¥" + data.getBuilding().getMinSinglePrice() + "/㎡/天");
            } else {
                info.setMinSinglePrice("¥" + data.getBuilding().getMinSinglePrice() + "/位/月");
            }
        }
        info.setFavorite(data.isIsFavorite());
        if (data.getBuilding().getTags() != null && data.getBuilding().getTags().size() > 0) {
            info.setTags(HouseTags.getTags(data));
        }
        return info;
    }

    @Override
    public void firstChatSuccess(FirstChatBean data) {
        //是否第一次聊天
        isFirstChat = data.getIsChat() == 0;
    }

    //认证申请显示个人信息
    @Override
    public void identityChattedMsgSuccess(IdentitychattedMsgBean data) {
        //刷新用户信息
        RongCloudSetUserInfoUtils.refreshUserInfoCache(targetId, data.getNickname(), data.getAvatar());
        RongCloudSetUserInfoUtils.refreshUserInfoCache(SpUtils.getRongChatId(), SpUtils.getNickName(), SpUtils.getHeaderImg());
        tvTitleName.setText(data.getNickname());
        tvJob.setText(data.getJob());
    }

    /**
     * 交换手机
     */
    @Click(R.id.rl_mobile)
    void mobileClick() {
        if (isFastClick(3000)) {
            return;
        }
        if (!TextUtils.isEmpty(SpUtils.getPhoneNum())) {
            new ConfirmDialog(context, true, getString(
                    R.string.dialog_title_exchange_phone_contacts), "");
        }
    }

    /**
     * 交换微信
     */
    @Click(R.id.rl_wx)
    void wxClick() {
        if (isFastClick(3000)) {
            return;
        }
        if (TextUtils.isEmpty(SpUtils.getWechat())) {
            new InputContactsDialog(context);
        } else {
            new ConfirmDialog(context, false, getString(
                    R.string.dialog_title_exchange_wechat_contacts), SpUtils.getWechat());
        }
    }

    /**
     * 预约看房
     */
    @Click(R.id.rl_viewing_date)
    void viewingDateClick() {
        if (isFastClick(1500)) {
            return;
        }
        if (mData == null) {
            shortTip("暂无楼盘信息，无法预约");
            return;
        }
        ConversationViewingDateActivity_.intent(this).buildingId(buildingId).houseId(houseId).targetId(targetId).start();
    }

    /**
     * 举报
     */
    @Click(R.id.rl_report)
    void reportClick() {
        ToastUtils.toastForShort(context, "暂未开放");
    }


    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.conversationPhoneAgree,
                CommonNotifications.conversationPhoneReject,
                CommonNotifications.conversationWeChatAgree,
                CommonNotifications.conversationWeChatReject,
                CommonNotifications.conversationViewHouseAgree,
                CommonNotifications.conversationViewHouseReject,
                CommonNotifications.conversationBindWeChat,
                CommonNotifications.conversationBindPhone,
                CommonNotifications.conversationIdApplyAgree,
                CommonNotifications.conversationIdApplyReject
        };
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (args == null) {
            return;
        }
        String dataMes = (String) args[0];//请求方
        if (id == CommonNotifications.conversationPhoneAgree) {
            //同意交换手机
            String mineMes = (String) args[1];//自己手机
            SendMessageManager.getInstance().sendEcPhoneStatusMessage(true, targetId, "我同意和您交换手机号", dataMes, mineMes, "");
        } else if (id == CommonNotifications.conversationPhoneReject) {
            SendMessageManager.getInstance().sendEcPhoneStatusMessage(false, targetId, "已拒绝和您交换手机号", "", "", "");
        } else if (id == CommonNotifications.conversationWeChatAgree) {
            //同意交换微信
            String mineMes = (String) args[1];//自己微信
            SendMessageManager.getInstance().sendEcWeChatStatusMessage(true, targetId, "我同意和您交换微信号", dataMes, mineMes, "");
        } else if (id == CommonNotifications.conversationWeChatReject) {
            SendMessageManager.getInstance().sendEcWeChatStatusMessage(false, targetId, "已拒绝和您交换微信号", "", "", "");
        } else if (id == CommonNotifications.conversationViewHouseAgree) {
            //同意预约看房
            SendMessageManager.getInstance().sendViewingDateStatusMessage(true, targetId, "同意预约看房", "");
        } else if (id == CommonNotifications.conversationViewHouseReject) {
            SendMessageManager.getInstance().sendViewingDateStatusMessage(false, targetId, "拒绝预约看房", "");
        } else if (id == CommonNotifications.conversationBindWeChat) {
            //开始发送交换微信信息 dataMes微信号  本人发出要约， 填写自己微信
            SendMessageManager.getInstance().sendWeChatMessage(targetId, "我想和您交换微信号", dataMes, "");
        } else if (id == CommonNotifications.conversationBindPhone) {
            //开始发送交换手机信息   本人发出要约，发出自己手机号
            SendMessageManager.getInstance().sendPhoneMessage(targetId, "我想和您交换手机号", SpUtils.getPhoneNum(), "");
        } else if (id == CommonNotifications.conversationIdApplyAgree) {
            //同意认证申请
            SendMessageManager.getInstance().sendIdApplyStatusMessage(true, targetId, "", "");
            SendMessageManager.getInstance().sendTextMessage(targetId, "我已同意你加入公司，欢迎");
        } else if (id == CommonNotifications.conversationIdApplyReject) {
            //拒绝认证申请
            SendMessageManager.getInstance().sendIdApplyStatusMessage(false, targetId, "", "");
            SendMessageManager.getInstance().sendTextMessage(targetId, "我已拒绝你加入公司");
        }
    }

    @Override
    public Message onSend(Message message) {
        return message;
    }

    /**
     * 消息发送监听
     * 第一次发送监听
     */
    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        //消息发送成功的回调
        if (isFirstChat) {
            mPresenter.isFirstChat(buildingId, houseId, getHouseChatId);
        }
        return false;
    }

    private void getRongTargetInfo(String targetId) {
        if (!TextUtils.isEmpty(targetId)) {
            OfficegoApi.getInstance().getRongUserInfo(targetId,
                    new RetrofitCallback<RongUserInfoBean>() {
                        @Override
                        public void onSuccess(int code, String msg, RongUserInfoBean data) {
                            tvTitleName.setText(data.getName());
                            tvTitleName.setPadding(0, 28, 0, 0);
                        }

                        @Override
                        public void onFail(int code, String msg, RongUserInfoBean data) {
                        }
                    });
        }
    }

}
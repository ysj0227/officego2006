package com.officego.ui.message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.dialog.ConfirmDialog;
import com.officego.commonlib.common.dialog.InputContactsDialog;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.IdentitychattedMsgBean;
import com.officego.commonlib.common.model.RongUserInfoBean;
import com.officego.commonlib.common.rongcloud.RongCloudSetUserInfoUtils;
import com.officego.commonlib.common.rongcloud.SendMessageManager;
import com.officego.commonlib.common.sensors.SensorsTrack;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.ui.message.contract.ConversationContract;
import com.officego.ui.message.presenter.ConversationPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

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
    private ConstraintLayout ctlChat;
    private TextView tvTitleName, tvJob;
    private TextView tvTip;
    private RelativeLayout rlTip;
    private ImageView ivMobile, ivWx;

    @Extra
    String targetId;//融云的id
    @Extra
    int buildingId;//楼盘详情传入
    @Extra
    int houseId;//房源详情传入

    private String getHouseChatId;//去除 targetId  的最后一位 ,产品定义
    private ChatHouseBean mData;
    private boolean isFirstChat = true;//是否第一次聊天  isChat": 0 :点击发送按钮的时候需要调用 addChat接口，1:不需要
    private boolean isSendApply;//租户认证发送的申请
    private String mNikeName;
    private boolean isCanExchange;//是否可以交换手机微信

    private void initViewById() {
        LinearLayout llRoot = findViewById(R.id.ll_root);
        llRoot.setPadding(0, CommonHelper.statusHeight(this), 0, 0);
        ctlChat = findViewById(R.id.ctl_chat);
        tvTitleName = findViewById(R.id.tv_title_name);
        tvJob = findViewById(R.id.tv_job);
        rlTip = findViewById(R.id.rl_tip);
        tvTip = findViewById(R.id.tv_tip);
        ivMobile = findViewById(R.id.iv_mobile);
        ivWx = findViewById(R.id.iv_wx);
        ivMobile.setImageResource(R.mipmap.ic_mobile_gray_big);
        ivWx.setImageResource(R.mipmap.ic_wx_exchange_gray);
    }

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new ConversationPresenter();
        mPresenter.attachView(this);
        initViewById();
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
            String mineId = SpUtils.getRongChatId();
            if (!TextUtils.isEmpty(targetId) && targetId.length() > 1 &&
                    TextUtils.equals(Constants.TYPE_OWNER, targetId.substring(targetId.length() - 1)) &&
                    (!TextUtils.isEmpty(mineId) && mineId.length() > 1 &&
                            TextUtils.equals(Constants.TYPE_OWNER, mineId.substring(mineId.length() - 1)))) {
                //认证申请聊天列表进入,融云id最后一位是“1”
                isSendApply = false;
                ctlChat.setVisibility(View.GONE);
                mPresenter.identityChattedMsg(targetId);
                initIM();
            } else if (!TextUtils.isEmpty(targetId) && (TextUtils.equals(Constants.TYPE_SYSTEM, targetId) ||
                    (targetId.length() > 1 && TextUtils.equals(Constants.TYPE_SYSTEM, targetId.substring(targetId.length() - 1))))) {
                //系统消息聊天列表进入
                ctlChat.setVisibility(View.GONE);
                tvTitleName.setText("系统消息");
                tvTitleName.setPadding(0, 28, 0, 0);
                initIM();
            } else {
                //是否可以交换微信电话
                mPresenter.exchangeContactsVerification(targetId);
                //租户-房东聊天
                isSendApply = false;
                ctlChat.setVisibility(View.VISIBLE);
                initIM();
                RongIM.getInstance().setSendMessageListener(this);
                mPresenter.firstChatApp(targetId, buildingId, houseId, getHouseChatId);
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
            //发送认证 ，返回房东个人中心
            GotoActivityUtils.mainOwnerDefMainActivity(context);
        } else {
            super.onBackPressed();
        }
    }

    private void initIMInfo() {
        if (TextUtils.isEmpty(targetId)) {
            if (getIntent().getData() != null) {
                targetId = getIntent().getData().getQueryParameter("targetId");
            }
        }
        if (TextUtils.isEmpty(targetId)) {
            shortTip("对方获取信息异常，请稍后再聊");
        }
        if (!TextUtils.isEmpty(targetId) && targetId.length() > 1) {
            getHouseChatId = targetId.substring(0, targetId.length() - 1);
        }
    }

    private void initIM() {
        try {
            FragmentManager fragmentManage = getSupportFragmentManager();
            ConversationFragment fragment = (ConversationFragment) fragmentManage.findFragmentById(R.id.conversation);
            Uri uri;
            if (!TextUtils.isEmpty(targetId) && (targetId.length() > 1 &&
                    !TextUtils.equals(Constants.TYPE_TENANT, targetId.substring(targetId.length() - 1)) &&
                    !TextUtils.equals(Constants.TYPE_OWNER, targetId.substring(targetId.length() - 1)))
                    || TextUtils.equals(Constants.TYPE_SYSTEM, targetId)) {
                //系统消息
                findViewById(R.id.rc_extension).setVisibility(View.INVISIBLE);
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversation")
                        .appendPath(Conversation.ConversationType.SYSTEM.getName().toLowerCase())
                        .appendQueryParameter("targetId", targetId).build();
            } else {
                //聊天消息
                findViewById(R.id.rc_extension).setVisibility(TextUtils.isEmpty(targetId) ? View.INVISIBLE : View.VISIBLE);
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversation")
                        .appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                        .appendQueryParameter("targetId", targetId).build();
            }
            if (fragment != null) {
                fragment.setUri(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //插入聊天大楼信息
    @SuppressLint("SetTextI18n")
    @Override
    public void houseSuccess(ChatHouseBean data) {
        if (data == null) {
            mPresenter.getRongTargetInfo(targetId); //推送点击获取Target用户信息
            return;
        }
        mData = data;
        isFirstChat = data.getIsChat() == 0; //是否第一次聊天
        //租户第一次聊天,发送默认消息
        if (isFirstChat && TextUtils.equals(Constants.TYPE_TENANT, SpUtils.getRole())) {
            SendMessageManager.getInstance().sendTextMessage(targetId, "我对你发布的房源有兴趣，能聊聊吗？");
        }
        //刷新用户信息
        refreshChatUserInfo(data);
    }

    //刷新用户信息
    private void refreshChatUserInfo(ChatHouseBean data) {
        if (data.getChatted() != null) {
            mNikeName = data.getChatted().getNickname();
            tvTitleName.setText(data.getChatted().getNickname());
            tvJob.setText(data.getChatted().getJob());
            if (!TextUtils.isEmpty(targetId)) {
                RongCloudSetUserInfoUtils.refreshUserInfoCache(targetId,
                        TextUtils.isEmpty(data.getChatted().getNickname()) ? "" : data.getChatted().getNickname(),
                        TextUtils.isEmpty(data.getChatted().getAvatar()) ? "" : data.getChatted().getAvatar());
            }
            if (!TextUtils.isEmpty(SpUtils.getRongChatId())) {
                RongCloudSetUserInfoUtils.refreshUserInfoCache(SpUtils.getRongChatId(),
                        TextUtils.isEmpty(SpUtils.getNickName()) ? "" : SpUtils.getNickName(),
                        TextUtils.isEmpty(SpUtils.getHeaderImg()) ? "" : SpUtils.getHeaderImg());
            }
        }
    }

    //是否可以交换手机和微信
    @Override
    public void exchangeContactsSuccess(boolean isCanExchange) {
        this.isCanExchange = isCanExchange;
        if (isCanExchange) {
            rlTip.setVisibility(View.GONE);
            ivMobile.setImageResource(R.mipmap.ic_mobile_blue_big);
            ivWx.setImageResource(R.mipmap.ic_wx_exchange);
        } else {
            rlTip.setVisibility(View.VISIBLE);
            ivMobile.setImageResource(R.mipmap.ic_mobile_gray_big);
            ivWx.setImageResource(R.mipmap.ic_wx_exchange_gray);
            if (!TextUtils.isEmpty(SpUtils.getSignToken())) {
                tvTip.setText(TextUtils.equals(Constants.TYPE_TENANT, SpUtils.getRole()) ?
                        "和房东建立聊天后即可发起交换微信和电话" :
                        "双方建立看房日程后才能发起交换微信和电话");
            }
        }
    }

    @Override
    public void firstChatSuccess(FirstChatBean data) {
        //是否第一次聊天
        isFirstChat = data.getIsChat() == 0;
    }

    //认证申请显示个人信息
    @Override
    public void identityChattedMsgSuccess(IdentitychattedMsgBean data) {
        mNikeName = data.getNickname();
        RongCloudSetUserInfoUtils.refreshUserInfoCache(targetId, data.getNickname(), data.getAvatar());
        RongCloudSetUserInfoUtils.refreshUserInfoCache(SpUtils.getRongChatId(), SpUtils.getNickName(), SpUtils.getHeaderImg());
        tvTitleName.setText(data.getNickname());
        tvJob.setText(data.getJob());
    }

    //推送点击获取Target用户信息
    @Override
    public void rongTargetInfoSuccess(RongUserInfoBean data) {
        tvTitleName.setText(data.getName());
        tvTitleName.setPadding(0, 28, 0, 0);
    }

    /**
     * 交换手机
     */
    @Click(R.id.rl_mobile)
    void mobileClick() {
        if (isFastClick(3000)) {
            return;
        }
        //聊天次数是否可以交换手机微信
        if (!isCanExchange) {
            mPresenter.exchangeContactsVerification(targetId);
            return;
        }
        //交换
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
        //聊天次数是否可以交换手机微信
        if (!isCanExchange) {
            mPresenter.exchangeContactsVerification(targetId);
            return;
        }
        //交换
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
        //神策
        String sensorEventDate = String.valueOf(DateTimeUtils.currentTimeSecond());
        SensorsTrack.clickImOrderSeeHouseButton(String.valueOf(buildingId), String.valueOf(houseId),
                targetId, mNikeName, sensorEventDate);
        if (mData == null) {
            shortTip("暂无楼盘信息，无法预约");
            return;
        }
        ConversationViewingDateActivity_.intent(this)
                .buildingId(buildingId)
                .houseId(houseId)
                .targetId(targetId)
                .sensorEventDate(sensorEventDate)
                .start();
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
            String messageUid = (String) args[2];//messageUid
            SendMessageManager.getInstance().sendEcPhoneStatusMessage(true, targetId, "我同意和您交换手机号", dataMes, mineMes, "");
            //神策
            sensorsPhoneExStatus(true, messageUid);
        } else if (id == CommonNotifications.conversationPhoneReject) {
            SendMessageManager.getInstance().sendEcPhoneStatusMessage(false, targetId, "已拒绝和您交换手机号", "", "", "");
            //神策
            sensorsPhoneExStatus(false, dataMes);
        } else if (id == CommonNotifications.conversationWeChatAgree) {
            //同意交换微信
            String mineMes = (String) args[1];//自己微信
            String messageUid = (String) args[2];//messageUid
            SendMessageManager.getInstance().sendEcWeChatStatusMessage(true, targetId, "我同意和您交换微信号", dataMes, mineMes, "");
            //神策
            sensorsWxExStatus(true, messageUid);
        } else if (id == CommonNotifications.conversationWeChatReject) {
            SendMessageManager.getInstance().sendEcWeChatStatusMessage(false, targetId, "已拒绝和您交换微信号", "", "", "");
            //神策
            sensorsWxExStatus(false, dataMes);
        } else if (id == CommonNotifications.conversationViewHouseAgree) {
            //同意预约看房
            SendMessageManager.getInstance().sendViewingDateStatusMessage(true, targetId, "同意预约看房", "");
        } else if (id == CommonNotifications.conversationViewHouseReject) {
            SendMessageManager.getInstance().sendViewingDateStatusMessage(false, targetId, "拒绝预约看房", "");
        } else if (id == CommonNotifications.conversationBindWeChat) {
            //开始发送交换微信信息 dataMes微信号  本人发出要约， 填写自己微信   mData加入神策数据使用
            SendMessageManager.getInstance().sendWeChatMessage(mData, targetId, "我想和您交换微信号", dataMes, "");
        } else if (id == CommonNotifications.conversationBindPhone) {
            //开始发送交换手机信息   本人发出要约，发出自己手机号    mData加入神策数据使用
            SendMessageManager.getInstance().sendPhoneMessage(mData, targetId, "我想和您交换手机号", SpUtils.getPhoneNum(), "");
        } else if (id == CommonNotifications.conversationIdApplyAgree) {
            //同意认证申请
            SendMessageManager.getInstance().sendIdApplyStatusMessage(true, targetId, "我已同意你加入公司，欢迎", "");
            SendMessageManager.getInstance().sendTextMessage(targetId, "我已同意你加入公司，欢迎");
        } else if (id == CommonNotifications.conversationIdApplyReject) {
            //拒绝认证申请
            SendMessageManager.getInstance().sendIdApplyStatusMessage(false, targetId, "我已拒绝你加入公司", "");
            SendMessageManager.getInstance().sendTextMessage(targetId, "我已拒绝你加入公司");
        }
    }

    private int getHouseId() {
        return ((mData == null || mData.getBuilding().getHouseId() == null) ? 0 :
                Integer.parseInt(CommonHelper.bigDecimal(mData.getBuilding().getHouseId(), true)));
    }

    //神策手机交换状态
    private void sensorsPhoneExStatus(boolean isAgree, String messageUid) {
        SensorsTrack.confirmPhoneExchangeState(mData == null ? 0 : mData.getIsBuildOrHouse(),
                mData == null ? 0 : mData.getBuilding().getBtype(),
                mData == null ? 0 : mData.getBuilding().getBuildingId(),
                getHouseId(), messageUid, isAgree);
    }

    //神策微信交换状态
    private void sensorsWxExStatus(boolean isAgree, String messageUid) {
        SensorsTrack.confirmWechatExchangeState(mData == null ? 0 : mData.getIsBuildOrHouse(),
                mData == null ? 0 : mData.getBuilding().getBtype(),
                mData == null ? 0 : mData.getBuilding().getBuildingId(),
                getHouseId(), messageUid, isAgree);
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
}
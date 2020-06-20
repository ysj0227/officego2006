package com.officego.ui.message;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.ToastUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.common.contract.ConversationContract;
import com.officego.commonlib.common.dialog.ConfirmDialog;
import com.officego.commonlib.common.dialog.InputContactsDialog;
import com.officego.commonlib.common.message.BuildingInfo;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.presenter.ConversationPresenter;
import com.officego.commonlib.common.rongcloud.SendMessageManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import java.util.Objects;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by YangShiJie
 * Data 2020/5/22.
 * Descriptions:
 **/
@SuppressLint("Registered")
@EActivity(R.layout.conversation)
public class ConversationActivity extends BaseMvpActivity<ConversationPresenter>
        implements ConversationContract.View {
    private LinearLayout llRoot;
    private String targetTitle;
    private TextView tvTitleName, tvJob;
    private String getHouseChatId; //去除 targetId  的最后一位 ,产品定义
    @Extra
    String targetId;//融云的id
    private ChatHouseBean mData;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new ConversationPresenter();
        mPresenter.attachView(this);
        llRoot = findViewById(R.id.ll_root);
        tvTitleName = findViewById(R.id.tv_title_name);
        tvJob = findViewById(R.id.tv_job);
        llRoot.setPadding(0, CommonHelper.statusHeight(this), 0, 0);
        initRongCloudIM();
        //插入一次
        mPresenter.getHouseDetails(getHouseChatId);
    }

    private void initRongCloudIM() {
        //会话界面 对方id
        if (TextUtils.isEmpty(targetId)) {
            targetId = Objects.requireNonNull(getIntent().getData()).getQueryParameter("targetId");
            targetTitle = Objects.requireNonNull(getIntent().getData().getQueryParameter("title")); //对方 昵称
        }
        getHouseChatId = targetId.substring(0, targetId.length() - 1);
        LogCat.e("TAG", "11111 targetId: " + targetId + "getHouseChatId: " + getHouseChatId + "  title: " + targetTitle);

        FragmentManager fragmentManage = getSupportFragmentManager();
        ConversationFragment fragement = (ConversationFragment) fragmentManage.findFragmentById(R.id.conversation);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation")
                .appendPath(Conversation.ConversationType.PRIVATE.getName().toLowerCase())
                .appendQueryParameter("targetId", targetId).build();

        fragement.setUri(uri);
    }

    /**
     * 刷新融云用户信息
     *
     * @param id
     * @param name
     * @param imgUrl
     */
    private void refreshUserInfoCache(String id, String name, String imgUrl) {
        UserInfo userInfo = new UserInfo(id, name, Uri.parse(imgUrl));
        RongIM.getInstance().refreshUserInfoCache(userInfo);
    }

    /**
     * 插入聊天大楼信息
     *
     * @param data data
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void houseSuccess(ChatHouseBean data) {
        mData = data;
        if (data == null) {
            return;
        }
        //刷新用户信息
        if (data.getHouse() != null) {
            refreshUserInfoCache(targetId, data.getChatted().getNickname(), data.getChatted().getAvatar());
            tvTitleName.setText(data.getChatted().getNickname());
            tvJob.setText(data.getChatted().getCompany() + "-" + data.getChatted().getJob());
        }
        //是否插入消息
        if (TextUtils.equals(SpUtils.getSignToken() + data.getHouse().getHouseId() + targetId, SpUtils.getChatBuildingInfo())) {
            return;
        }
        if (data.getHouse() != null) {
            BuildingInfo info = new BuildingInfo();
            info.setbuildingName(data.getHouse().getHouseName());
            info.setImgUrl(data.getHouse().getMainPic());
            info.setCreateTime(DateTimeUtils.secondToDate(data.getCreateTime(), "yyyy-MM-dd HH:mm") +
                    " 由" + data.getCreateUser() + "发起沟通");
            if (TextUtils.isEmpty(data.getHouse().getDistance())) {
                info.setDistrict(data.getHouse().getDistrict());
            } else {
                info.setDistrict(data.getHouse().getDistance() + "km | " + data.getHouse().getDistrict());
            }
            if (data.getHouse() != null && data.getHouse().getStationline().size() > 0) {
                String workTime = data.getHouse().getNearbySubwayTime().get(0);
                String stationLine = data.getHouse().getStationline().get(0);
                String stationName = data.getHouse().getStationNames().get(0);
                info.setRouteMap("步行" + workTime + "分钟到 | " + stationLine + "号线 ·" + stationName);
            }
            assert data.getHouse() != null;
            info.setMinSinglePrice("￥" + data.getHouse().getMinSinglePrice());
            info.setFavorite(data.isIsFavorite());
            if (data.getHouse() != null && data.getHouse().getTags() != null && data.getHouse().getTags().size() > 0) {
                info.setTags(getTags(data));
            }
            SendMessageManager.getInstance().insertIncomingMessage(info, targetId, SpUtils.getRongChatId());
            //保存第一次插入状态
            if (data.getHouse() != null) {
                SpUtils.saveChatBuildingInfo(data.getHouse().getHouseId() + "" + targetId);
            }
        }
    }

    private String getTags(ChatHouseBean data) {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < data.getHouse().getTags().size(); i++) {
            if (data.getHouse().getTags().size() == 1) {
                key.append(data.getHouse().getTags().get(i).getDictCname());
            } else {
                key.append(data.getHouse().getTags().get(i).getDictCname()).append(",");
            }
        }
        if (data.getHouse().getTags().size() > 1) {
            key = key.replace(key.length() - 1, key.length(), "");
        }
        return key.toString();
    }

    @Click(R.id.rl_back)
    void backClick() {
        finish();
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
            setPhoneMessage();
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
            new ConfirmDialog(context, getString(
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
            shortTip("暂无楼盘信息无法预约");
            return;
        }
        ConversationViewingDateActivity_.intent(this).targetId(targetId).start();
    }

    /**
     * 举报
     */
    @Click(R.id.rl_report)
    void reportClick() {
        ToastUtils.toastForShort(context, "暂未开放");
    }

    //本人发出要约，发出自己手机号
    private void setPhoneMessage() {
        SendMessageManager.getInstance().sendPhoneMessage(targetId, "我想和您交换手机号", SpUtils.getPhoneNum(), "");
    }

    //本人发出要约， 填写自己微信
    private void setWechatMessage(String wechat) {
        SendMessageManager.getInstance().sendWeChatMessage(targetId, "我想和您交换微信号", wechat, "");
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.conversationPhoneAgree,
                CommonNotifications.conversationPhoneReject,
                CommonNotifications.conversationWeChatAgree,
                CommonNotifications.conversationWeChatReject,
                CommonNotifications.conversationViewHouseAgree,
                CommonNotifications.conversationViewHouseReject,
                CommonNotifications.conversationBindWeChat};
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
            //开始发送交换微信信息
            setWechatMessage(dataMes);
        }
    }
}
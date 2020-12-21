package com.officego.commonlib.common.rongcloud;

import android.content.Context;
import android.text.TextUtils;

import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.message.BuildingInfo;
import com.officego.commonlib.common.message.BuildingProvider;
import com.officego.commonlib.common.message.EcPhoneStatusInfo;
import com.officego.commonlib.common.message.EcPhoneStatusProvider;
import com.officego.commonlib.common.message.EcWeChatStatusInfo;
import com.officego.commonlib.common.message.EcWeChatStatusProvider;
import com.officego.commonlib.common.message.IdentityApplyInfo;
import com.officego.commonlib.common.message.IdentityApplyProvider;
import com.officego.commonlib.common.message.IdentityApplyStatusInfo;
import com.officego.commonlib.common.message.IdentityApplyStatusProvider;
import com.officego.commonlib.common.message.PhoneEncryptedInfo;
import com.officego.commonlib.common.message.PhoneEncryptedProvider;
import com.officego.commonlib.common.message.PhoneInfo;
import com.officego.commonlib.common.message.PhoneProvider;
import com.officego.commonlib.common.message.ViewingDateInfo;
import com.officego.commonlib.common.message.ViewingDateProvider;
import com.officego.commonlib.common.message.ViewingDateStatusInfo;
import com.officego.commonlib.common.message.ViewingDateStatusProvider;
import com.officego.commonlib.common.message.WeChatInfo;
import com.officego.commonlib.common.message.WeChatProvider;
import com.officego.commonlib.common.model.RongUserInfoBean;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.push.RongPushClient;
import io.rong.push.common.PushCacheHelper;
import io.rong.push.pushconfig.PushConfig;

/**
 * Created by YangShiJie
 * Data 2020/5/22.
 * Descriptions:
 **/
public class IMManager {
    protected final String TAG = this.getClass().getSimpleName();
    private static volatile IMManager instance;
    /**
     * 接收戳一下消息
     */
    private volatile Boolean isReceivePokeMessage = null;

//    private IMInfoProvider imInfoProvider;
//    private ConversationRecord lastConversationRecord;


    private IMManager() {
    }

    public static IMManager getInstance() {
        if (instance == null) {
            synchronized (IMManager.class) {
                if (instance == null) {
                    instance = new IMManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        //push
        initRongPush();
        // 调用 RongIM 初始化
        initRongIM(context);
        //接收新用户信息，并设置用户信息提供者
        receiveUserInfoProvider();
        // 初始化已读回执类型
        initReadReceiptConversation();
        //初始化自定义消息
        initMessageType();
        // 初始化连接状态变化监听
        initConnectStateChangeListener(context);
        // 初始化消息监听
        initSendReceiveMessageListener();
        //初始化接收消息监听
        initReceiveMessageWrapperListener();

    }

    //融云推送
    private void initRongPush() {
        PushConfig config = new PushConfig.Builder()
                .enableMiPush(AppConfig.MI_APP_ID, AppConfig.MI_APP_KEY)
                .enableOppoPush(AppConfig.OPPO_APP_KEY,AppConfig.OPPO_APP_SECRET)
                .enableHWPush(true)
                .enableVivoPush(true)
                .build();
        RongPushClient.setPushConfig(config);
    }

    //融云初始化
    private void initRongIM(Context context) {
        //初始化
        RongIM.init(context, AppConfig.RC_APPKEY);
        //系统消息是否显示详情
        PushCacheHelper.getInstance().setPushContentShowStatus(context, true);
    }

    private void initMessageType() {
        //交换手机
        RongIM.registerMessageType(PhoneInfo.class);
        RongIM.registerMessageTemplate(new PhoneProvider());
        //交换微信
        RongIM.registerMessageType(WeChatInfo.class);
        RongIM.registerMessageTemplate(new WeChatProvider());
        //是否同意交换手机
        RongIM.registerMessageType(EcPhoneStatusInfo.class);
        RongIM.registerMessageTemplate(new EcPhoneStatusProvider());
        //是否同意交换微信
        RongIM.registerMessageType(EcWeChatStatusInfo.class);
        RongIM.registerMessageTemplate(new EcWeChatStatusProvider());
        //预约看房
        RongIM.registerMessageType(ViewingDateInfo.class);
        RongIM.registerMessageTemplate(new ViewingDateProvider());
        //预约看房同意拒绝
        RongIM.registerMessageType(ViewingDateStatusInfo.class);
        RongIM.registerMessageTemplate(new ViewingDateStatusProvider());
        //插入自定义消息房子的item
        RongIM.registerMessageType(BuildingInfo.class);
        RongIM.registerMessageTemplate(new BuildingProvider());
        //认证发送申请
        RongIM.registerMessageType(IdentityApplyInfo.class);
        RongIM.registerMessageTemplate(new IdentityApplyProvider());
        //认证发送申请  同意拒绝
        RongIM.registerMessageType(IdentityApplyStatusInfo.class);
        RongIM.registerMessageTemplate(new IdentityApplyStatusProvider());
        //聊天手机发送-加密提示
        RongIM.registerMessageType(PhoneEncryptedInfo.class);
        RongIM.registerMessageTemplate(new PhoneEncryptedProvider());
    }

    /**
     * 连接 IM 服务
     *
     * @param token    token
     * @param callback callback
     */
    public void connectIM(String token, ResultCallback<String> callback) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                //融云token错误,从服务端重新获取
                rongCloudTokenError();
            }

            @Override
            public void onSuccess(String s) {
                Constants.isRCIMConnectSuccess = true;
                callback.onSuccess(s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Constants.isRCIMConnectSuccess = false;
                if (errorCode == RongIMClient.ErrorCode.RC_MSG_RESP_TIMEOUT ||
                        errorCode == RongIMClient.ErrorCode.RC_SOCKET_NOT_CREATED ||
                        errorCode == RongIMClient.ErrorCode.RC_SOCKET_DISCONNECTED ||
                        errorCode == RongIMClient.ErrorCode.RC_CONN_REDIRECTED) {
                    connectIM(token, callback);
                } else {
                    if (callback != null) {
                        callback.onFail(errorCode.getValue());
                    } else {
                        //do something
                        //connectIM(token, callback);
                    }
                }
            }
        });
    }

    /**
     * 初始化已读回执类型
     */
    private void initReadReceiptConversation() {
        Conversation.ConversationType[] types = new Conversation.ConversationType[] {
                Conversation.ConversationType.PRIVATE
        };
        RongIM.getInstance().setReadReceiptConversationTypeList(types);
    }

    /**
     * 初始化连接状态监听
     */
    private void initConnectStateChangeListener(Context context) {
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                if (connectionStatus.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                    //被其他提出时，需要返回登录界面 剔除其他登录
                    BaseNotification.newInstance().postNotificationName(CommonNotifications.rongCloudkickDialog, "rongCloudkickDialog");
                } else if (connectionStatus == ConnectionStatus.TOKEN_INCORRECT) {
                    //token错误退出登录
                    SpUtils.clearLoginInfo();
                    GotoActivityUtils.loginClearActivity(context);
                }
            }
        });
    }

    /**
     * token 错误时，重新登录
     */
    private void rongCloudTokenError() {
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getRongCloudToken(new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (data != null) {
                    SpUtils.saveRongToken(data.toString());
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
            }
        });
    }

    private void initSendReceiveMessageListener() {
        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {

            /**
             * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
             *
             * @param message 发送的消息实例。
             * @return 处理后的消息实例。
             */
            @Override
            public Message onSend(Message message) {
                return message;
            }

            /**
             * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
             *
             * @param message              消息实例。
             * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
             * @return true 表示走自己的处理方式，false 走融云默认处理方式。
             */
            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                return false;
            }
        });
    }

    /**
     * 接收实时或者离线消息
     */
    private void initReceiveMessageWrapperListener() {
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageWrapperListener() {
            /**
             * 接收实时或者离线消息。
             * 注意:
             * 1. 针对接收离线消息时，服务端会将 200 条消息打成一个包发到客户端，客户端对这包数据进行解析。
             * 2. hasPackage 标识是否还有剩余的消息包，left 标识这包消息解析完逐条抛送给 App 层后，剩余多少条。
             * 如何判断离线消息收完：
             * 1. hasPackage 和 left 都为 0；
             * 2. hasPackage 为 0 标识当前正在接收最后一包（200条）消息，left 为 0 标识最后一包的最后一条消息也已接收完毕。
             *
             * @param message    接收到的消息对象
             * @param left       每个数据包数据逐条上抛后，还剩余的条数
             * @param hasPackage 是否在服务端还存在未下发的消息包
             * @param offline    消息是否离线消息
             * @return 是否处理消息。 如果 App 处理了此消息，返回 true; 否则返回 false 由 SDK 处理。
             */
            @Override
            public boolean onReceived(final Message message, final int left, boolean hasPackage, boolean offline) {
                return false;
            }
        });
    }

    //接收新用户信息，并设置用户信息提供者
    private void receiveUserInfoProvider() {
        RongIM.setUserInfoProvider(userId -> {
            getRongUserInfo(userId);
            return null;
        }, true);
    }

    private void getRongUserInfo(String targetId) {
        if (!TextUtils.isEmpty(targetId)) {
            OfficegoApi.getInstance().getRongUserInfo(targetId,
                    new RetrofitCallback<RongUserInfoBean>() {
                        @Override
                        public void onSuccess(int code, String msg, RongUserInfoBean data) {
                            RongCloudSetUserInfoUtils.refreshUserInfoCache(targetId,
                                    data.getName(), data.getAvatar());
                        }

                        @Override
                        public void onFail(int code, String msg, RongUserInfoBean data) {
                        }
                    });
        }
    }
}

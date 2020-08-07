package com.officego.commonlib.common.rongcloud;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

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
import com.officego.commonlib.common.message.PhoneInfo;
import com.officego.commonlib.common.message.PhoneProvider;
import com.officego.commonlib.common.message.ViewingDateInfo;
import com.officego.commonlib.common.message.ViewingDateProvider;
import com.officego.commonlib.common.message.ViewingDateStatusInfo;
import com.officego.commonlib.common.message.ViewingDateStatusProvider;
import com.officego.commonlib.common.message.WeChatInfo;
import com.officego.commonlib.common.message.WeChatProvider;
import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.notification.BaseNotification;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.push.RongPushClient;
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
        // 调用 RongIM 初始化
        initRongIM(context);
        //push
        initRongPush();
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

//        // 初始化会话界面相关内容
//        initConversation();
//        // 初始化会话列表界面相关内容
//        initConversationList();
//        // 初始化消息监听
//        initOnReceiveMessage(context);
////        // 缓存连接
////        cacheConnectIM();
    }

    //融云初始化
    private void initRongIM(Context context) {
        RongIM.init(context, AppConfig.RC_APPKEY, true);
    }

    //融云推送
    private void initRongPush() {
        PushConfig config = new PushConfig.Builder()
                .enableMiPush("2882303761518466472", "5901846688472")
                .build();
        RongPushClient.setPushConfig(config);
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
    }

    /**
     * 连接 IM 服务
     *
     * @param token
     * @param getTokenOnIncorrect
     * @param callback
     */
    public void connectIM(String token, boolean getTokenOnIncorrect, ResultCallback<String> callback) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                LogCat.e(TAG, "connect error - onTokenIncorrect");
            }

            @Override
            public void onSuccess(String s) {
                callback.onSuccess(s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogCat.e(TAG, "connect error - code:" + errorCode.getValue() + ", msg:" + errorCode.getMessage());
                if (errorCode == RongIMClient.ErrorCode.RC_CONN_REDIRECTED) {
                    // 重定向错误，直接调用重新连接
                    connectIM(token, getTokenOnIncorrect, callback);
                } else {
                    if (callback != null) {
                        callback.onFail(errorCode.getValue());
                    } else {
                        // do nothing
                        connectIM(token, getTokenOnIncorrect, callback);
                    }
                }

            }
        });
    }

//    /**
//     * 初始化会话相关
//     */
//    private void initConversation() {
//        // 启用会话界面新消息提示
//        RongIM.getInstance().enableNewComingMessageIcon(true);
//        // 启用会话界面未读信息提示
//        RongIM.getInstance().enableUnreadMessageIcon(true);
//        // 添加会话界面点击事件
//
//        RongIM.setConversationClickListener(new RongIM.ConversationClickListener() {
//            @Override
//            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
//                if (conversationType != Conversation.ConversationType.CUSTOMER_SERVICE) {
//                    Intent intent = new Intent(context, UserDetailActivity.class);
//                    intent.putExtra(IntentExtra.STR_TARGET_ID, userInfo.getUserId());
//                    if (conversationType == Conversation.ConversationType.GROUP) {
//                        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(s);
//                        if (groupInfo != null) {
//                            intent.putExtra(IntentExtra.GROUP_ID, groupInfo.getId());
//                            intent.putExtra(IntentExtra.STR_GROUP_NAME, groupInfo.getName());
//                        }
//                    }
//                    context.startActivity(intent);
//                }
//                return true;
//            }
//
//            @Override
//            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo, String s) {
//                if (conversationType == Conversation.ConversationType.GROUP) {
//                    // 当在群组时长按用户在输入框中加入 @ 信息
//                    ThreadManager.getInstance().runOnWorkThread(() -> {
//                        // 获取该群成员的用户名并显示在 @ 中的信息
//                        UserInfo groupMemberInfo = imInfoProvider.getGroupMemberUserInfo(s, userInfo.getUserId());
//                        if (groupMemberInfo != null) {
//                            groupMemberInfo.setName("@" + groupMemberInfo.getName());
//                            ThreadManager.getInstance().runOnUIThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    RongMentionManager.getInstance().mentionMember(groupMemberInfo);
//                                    // 填充完用户@信息后弹出软键盘
//                                    if (context instanceof ConversationActivity) {
//                                        ((ConversationActivity) context).showSoftInput();
//                                    }
//                                }
//                            });
//                        }
//                    });
//                    return true;
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onMessageClick(Context context, View view, Message message) {
//                if (message.getContent() instanceof ImageMessage) {
//                    Intent intent = new Intent(view.getContext(), SealPicturePagerActivity.class);
//                    intent.setPackage(view.getContext().getPackageName());
//                    intent.putExtra("message", message);
//                    view.getContext().startActivity(intent);
//                    return true;
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onMessageLinkClick(Context context, String s, Message message) {
//                return false;
//            }
//
//            @Override
//            public boolean onMessageLongClick(Context context, View view, Message message) {
//                return false;
//            }
//        });
//    }
//
//

    /**
     * 初始化已读回执类型
     */
    private void initReadReceiptConversation() {
        // 将私聊，群组加入消息已读回执
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.ENCRYPTED
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
                //LogCat.d(TAG, "ConnectionStatus onChanged = " + connectionStatus.getMessage() + " rcToken=" + SpUtils.getRongToken());
                if (connectionStatus.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                    //被其他提出时，需要返回登录界面 剔除其他登录
                    BaseNotification.newInstance().postNotificationName(CommonNotifications.rongCloudkickDialog, "rongCloudkickDialog");
                } else if (connectionStatus == ConnectionStatus.TOKEN_INCORRECT) {
                    //融云token错误
                    rongCloudTokenError();
                    //Toast.makeText(context, "融云token错误", Toast.LENGTH_SHORT).show();
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
                if (data != null)
                    SpUtils.saveRongToken(data.toString());
                new ConnectRongCloudUtils();
            }

            @Override
            public void onFail(int code, String msg, Object data) {
            }
        });
    }

    private void toast(Context context) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> Toast.makeText(context, "账号已在其他设备登录", Toast.LENGTH_LONG).show());

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

                if (message.getSentStatus() == Message.SentStatus.FAILED) {
                    if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
                        //不在聊天室
                    } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
                        //不在讨论组
                    } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
                        //不在群组
                    } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
                        //你在他的黑名单中
                    }
                }

                MessageContent messageContent = message.getContent();

                if (messageContent instanceof TextMessage) {//文本消息
                    TextMessage textMessage = (TextMessage) messageContent;
                    Log.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
                } else if (messageContent instanceof ImageMessage) {//图片消息
                    ImageMessage imageMessage = (ImageMessage) messageContent;
                    Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
                } else if (messageContent instanceof VoiceMessage) {//语音消息
                    VoiceMessage voiceMessage = (VoiceMessage) messageContent;
                    Log.d(TAG, "onSent-voiceMessage:" + voiceMessage.getUri().toString());
                } else if (messageContent instanceof RichContentMessage) {//图文消息
                    RichContentMessage richContentMessage = (RichContentMessage) messageContent;
                    Log.d(TAG, "onSent-RichContentMessage:" + richContentMessage.getContent());
                } else {
                    Log.d(TAG, "onSent-其他消息，自己来判断处理");
                }

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

}

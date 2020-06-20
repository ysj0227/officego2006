package com.owner.message.rongcloud;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.officego.commonlib.common.message.BuildingInfo;
import com.officego.commonlib.common.message.BuildingProvider;
import com.officego.commonlib.common.message.EcPhoneStatusInfo;
import com.officego.commonlib.common.message.EcPhoneStatusProvider;
import com.officego.commonlib.common.message.EcWeChatStatusInfo;
import com.officego.commonlib.common.message.EcWeChatStatusProvider;
import com.officego.commonlib.common.message.PhoneInfo;
import com.officego.commonlib.common.message.PhoneProvider;
import com.officego.commonlib.common.message.ViewingDateInfo;
import com.officego.commonlib.common.message.ViewingDateProvider;
import com.officego.commonlib.common.message.ViewingDateStatusInfo;
import com.officego.commonlib.common.message.ViewingDateStatusProvider;
import com.officego.commonlib.common.message.WeChatInfo;
import com.officego.commonlib.common.message.WeChatProvider;
import com.officego.commonlib.constant.Constants;
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
        // 初始化已读回执类型
        initReadReceiptConversation();
        //初始化自定义消息
        initMessageType();
        // 初始化连接状态变化监听
        initConnectStateChangeListener(context);
        // 初始化消息监听
        initSendReceiveMessageListener();
        //初始化接收监听
        initReceiveListener();

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
        RongIM.init(context, Constants.RC_APPKEY, true);
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
                LogCat.e(TAG, "ConnectionStatus onChanged = " + connectionStatus.getMessage());
                if (connectionStatus.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                    //被其他提出时，需要返回登录界面 剔除其他登录
                    toast(context);
                   // LoginActivity_.intent(context).start();
                } else if (connectionStatus == ConnectionStatus.TOKEN_INCORRECT) {
                    //token 错误时，重新登录
                    Toast.makeText(context, "token 错误", Toast.LENGTH_SHORT).show();
                    //LoginActivity_.intent(context).start();
                }
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
     * 监听消息接收事件
     */
    private void initReceiveListener() {
//        RongIM.setOnReceiveMessageListener((message, i) -> {
//            UserInfo userInfo = message.getContent().getUserInfo();
//            if (userInfo != null) {
//                RongIM.getInstance().refreshUserInfoCache(userInfo);
//                return false;
//            }
//            //返回false则依旧走融云默认的通知和铃声
//            return false;
//        });
    }

}

package com.owner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.GotoActivityUtils;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rongcloud.ConnectRongCloudUtils;
import com.officego.commonlib.common.rongcloud.kickDialog;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.view.MyFragmentTabHost;
import com.owner.message.MessageFragment_;
import com.owner.utils.MainTab;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeTextView;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;


@SuppressLint("Registered")
@EActivity(resName = "activity_main")
public class MainOwnerActivity extends BaseActivity implements TabHost.OnTabChangeListener {
    @ViewById(android.R.id.tabhost)
    MyFragmentTabHost mTabHost;

    private long mExitTime;
    private BGABadgeTextView mMessageTitle;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        initTabs();
        if (!TextUtils.isEmpty(SpUtils.getSignToken())) {
            new ConnectRongCloudUtils();
        }
        addUnReadMessageCountChangedObserver();
    }

    /**
     * 设置未读消息数变化监听器。
     * 注意:如果是在 activity 中设置,那么要在 activity 销毁时,调用 @link removeUnReadMessageCountChangedObserver(IUnReadMessageObserver)}
     * 否则会造成内存泄漏。
     */
    IUnReadMessageObserver observer = this::showMessageCount;

    private void showMessageCount(int i) {
        mMessageTitle.showCirclePointBadge();
        mMessageTitle.getBadgeViewHelper().setBadgeTextSizeSp(10);
        mMessageTitle.getBadgeViewHelper().setBadgeTextColorInt(Color.WHITE);
        mMessageTitle.getBadgeViewHelper().setBadgeBgColorInt(Color.RED);
        mMessageTitle.getBadgeViewHelper().setDraggable(true);
        mMessageTitle.getBadgeViewHelper().setBadgePaddingDp(5);
        mMessageTitle.getBadgeViewHelper().setBadgeBorderWidthDp(1);
        mMessageTitle.getBadgeViewHelper().setBadgeBorderColorInt(Color.WHITE);
        if (i < 1) {
            mMessageTitle.hiddenBadge();
        } else if (i < 100) {
            mMessageTitle.showTextBadge(String.valueOf(i));
        } else {
            mMessageTitle.showTextBadge("99+");
        }
    }

    /**
     * 未读消息监听
     */
    public void addUnReadMessageCountChangedObserver() {
        RongIM.getInstance().addUnReadMessageCountChangedObserver(observer, Conversation.ConversationType.PRIVATE);
    }

    /**
     * 移除未读消息监听
     */
    public void removeUnReadMessageCountChangedObserver() {
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(observer);
    }

    @Override
    public void onTabChanged(String tabId) {
        initStatusBar(tabId);
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
            upDateTab(i);
        }
    }


    /**
     * 更新文字颜色。
     */
    private void upDateTab(int currentTab) {
        if (mTabHost != null) {
            BGABadgeTextView title = mTabHost.getTabWidget().getChildAt(currentTab).findViewById(R.id.tab_title);
            if (mTabHost.getCurrentTab() == currentTab) {//选中
                title.setTextColor(this.getResources().getColor(R.color.common_blue_main));
            } else {//不选中
                title.setTextColor(this.getResources().getColor(R.color.text_caption));
            }
        }
    }

    //状态栏背景
    private void initStatusBar(String tabId) {
        if (TextUtils.equals(getStringById(R.string.str_tab_home), tabId)) {
            StatusBarUtils.setStatusBarFullTransparent(this);
        } else {
            StatusBarUtils.setStatusBarColor(this, StatusBarUtils.TYPE_DARK);
        }
    }

    void initTabs() {
        mTabHost.setup(context, getSupportFragmentManager(), R.id.fl_content);
        mTabHost.getTabWidget().setShowDividers(0);
        if (mTabHost.getChildCount() > 0) {
            mTabHost.clearAllTabs();
        }
        MainTab[] mainTabs = MainTab.values();
        for (MainTab mainTab : mainTabs) {
            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            BGABadgeTextView title = indicator.findViewById(R.id.tab_title);
            if (mainTab.getResIcon() != -1) {
                Drawable drawable = this.getResources().getDrawable(mainTab.getResIcon());
                title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            }
            title.setText(getString(mainTab.getResName()));
            if (mainTab.getClz() == MessageFragment_.class) {
                mMessageTitle = title;
            }
            tab.setIndicator(indicator);
            tab.setContent(tag -> new View(context));
            mTabHost.addTab(tab, mainTab.getClz(), null);
        }
        mTabHost.setCurrentTab(0);
        upDateTab(0);
        mTabHost.setOnTabChangedListener(this);
    }

    private Fragment getFragment(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeUnReadMessageCountChangedObserver();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                shortTip(R.string.toast_press_again_quit);
                mExitTime = System.currentTimeMillis();
            } else {
                gotoHome(context);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //fragment startActivityForResult无法接收返回值的解决方案
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().getFragments();
        if (getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /**
     * 回到桌面
     */
    private void gotoHome(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }

    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.rongCloudkickDialog,
                CommonNotifications.identityChangeToRelogin};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (args == null) {
            return;
        }
        //踢出登录
        if (id == CommonNotifications.rongCloudkickDialog) {
            new kickDialog(context);
        } else if (id == CommonNotifications.identityChangeToRelogin) {
            //身份发生变化
            shortTip((String) args[0]);
            SpUtils.clearLoginInfo();
            GotoActivityUtils.loginClearActivity(context, TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRole()));
        }
    }
}
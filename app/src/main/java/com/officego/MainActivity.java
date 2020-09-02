package com.officego;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.officego.commonlib.base.BaseActivity;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.commonlib.common.rongcloud.ConnectRongCloudUtils;
import com.officego.commonlib.common.rongcloud.kickDialog;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.DesktopCornerUtil;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.ui.collect.CollectFragment_;
import com.officego.ui.home.BuildingDetailsActivity_;
import com.officego.ui.home.BuildingDetailsChildActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkChildActivity_;
import com.officego.ui.home.HomeFragment_;
import com.officego.ui.home.utils.BundleUtils;
import com.officego.ui.message.MessageFragment_;
import com.officego.ui.mine.MineFragment_;
import com.officego.utils.GotoActivityUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeTextView;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

import static com.officego.commonlib.constant.Constants.TABLE_BAR_POSITION;

/**
 * Created by YangShiJie
 * Data 2020/7/3.
 * Descriptions:
 **/
@SuppressLint("Registered")
@EActivity(R.layout.activity_main_new)
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @ViewById(R.id.rg_tab_bar)
    RadioGroup rg_tab_bar;
    @ViewById(R.id.tab_home)
    RadioButton rb_1;
    @ViewById(R.id.tab_message)
    RadioButton rb_2;
    @ViewById(R.id.tab_collect)
    RadioButton rb_3;
    @ViewById(R.id.tab_mine)
    RadioButton rb_4;
    @ViewById(R.id.tab_unread_message)
    BGABadgeTextView unreadMessage;

    private HomeFragment_ fg1;
    private MessageFragment_ fg2;
    private CollectFragment_ fg3;
    private MineFragment_ fg4;
    private FragmentManager fManager;
    private long mExitTime;

    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarColor(this);
        fManager = getSupportFragmentManager();
        rg_tab_bar.setOnCheckedChangeListener(this);
        if (TABLE_BAR_POSITION == 1) {
            rb_2.setChecked(true);
        } else if (TABLE_BAR_POSITION == 2) {
            rb_3.setChecked(true);
        } else if (TABLE_BAR_POSITION == 3) {
            rb_4.setChecked(true);
        } else {
            rb_1.setChecked(true);
        }
        initBottomImage();
        if (!TextUtils.isEmpty(SpUtils.getSignToken())) {
            new ConnectRongCloudUtils();
        }
        addUnReadMessageCountChangedObserver();
        //设置未读消息位置
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) unreadMessage.getLayoutParams();
        params.width = CommonHelper.getScreenWidth(context) / 4;
        params.leftMargin = CommonHelper.getScreenWidth(context) / 4;
        unreadMessage.setLayoutParams(params);
        //外部链接唤起app
        appScheme();
    }

    //外部链接唤起app
    private void appScheme() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            //获取参数值
            String btyte = uri.getQueryParameter("btyte");
            String buildingId = uri.getQueryParameter("buildingId");
            String houseId = uri.getQueryParameter("houseId");
            if (!TextUtils.isEmpty(buildingId) && !TextUtils.isEmpty(btyte)) {
                //楼盘，网点
                if (TextUtils.equals(String.valueOf(Constants.TYPE_BUILDING), btyte)) {
                    BuildingDetailsActivity_.intent(context)
                            .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_BUILDING, Integer.valueOf(buildingId))).start();
                } else {
                    BuildingDetailsJointWorkActivity_.intent(context)
                            .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_JOINTWORK, Integer.valueOf(buildingId))).start();
                }
            } else if (!TextUtils.isEmpty(houseId) && !TextUtils.isEmpty(btyte)) {
                //房源
                if (TextUtils.equals(String.valueOf(Constants.TYPE_BUILDING), btyte)) {
                    BuildingDetailsChildActivity_.intent(context)
                            .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_BUILDING, Integer.valueOf(houseId))).start();
                } else {
                    BuildingDetailsJointWorkChildActivity_.intent(context)
                            .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_JOINTWORK, Integer.valueOf(houseId))).start();
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (checkedId) {
            case R.id.tab_home:
                TABLE_BAR_POSITION = 0;
                if (fg1 == null) {
                    fg1 = new HomeFragment_();
                    fTransaction.add(R.id.ly_content, fg1, "Fragment1");
                } else {
                    fTransaction.show(fg1);
                }
                break;
            case R.id.tab_message:
                TABLE_BAR_POSITION = 1;
                if (fg2 == null) {
                    fg2 = new MessageFragment_();
                    fTransaction.add(R.id.ly_content, fg2, "Fragment2");
                } else {
                    fTransaction.show(fg2);
                }
                break;
            case R.id.tab_collect:
                TABLE_BAR_POSITION = 2;
                fg3 = new CollectFragment_();
                fTransaction.add(R.id.ly_content, fg3, "Fragment3");
                break;
            case R.id.tab_mine:
                TABLE_BAR_POSITION = 3;
                fg4 = new MineFragment_();
                fTransaction.add(R.id.ly_content, fg4, "Fragment4");
                break;
        }
        fTransaction.commit();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (fg1 != null) fragmentTransaction.hide(fg1);
        if (fg2 != null) fragmentTransaction.hide(fg2);
        if (fg3 != null) fragmentTransaction.hide(fg3);
        if (fg4 != null) fragmentTransaction.hide(fg4);
    }

    private void initBottomImage() {
        //定义底部标签图片大小和位置
        Drawable drawable_news = getResources().getDrawable(R.drawable.ic_tab_home);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_news.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪个方向
        rb_1.setPadding(0, 18, 0, 5);
        rb_1.setCompoundDrawables(null, drawable_news, null, null);

        //定义底部标签图片大小和位置
        Drawable drawable_live = getResources().getDrawable(R.drawable.ic_tab_message);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_live.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪个方向
        rb_2.setPadding(0, 18, 0, 5);
        rb_2.setCompoundDrawables(null, drawable_live, null, null);

        //定义底部标签图片大小和位置
        Drawable drawable_tuijian = getResources().getDrawable(R.drawable.ic_tab_collect);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_tuijian.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪个方向
        rb_3.setPadding(0, 18, 0, 5);
        rb_3.setCompoundDrawables(null, drawable_tuijian, null, null);
        //定义底部标签图片大小和位置
        Drawable drawable_mine = getResources().getDrawable(R.drawable.ic_tab_mine);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        drawable_mine.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪个方向
        rb_4.setPadding(0, 18, 0, 5);
        rb_4.setCompoundDrawables(null, drawable_mine, null, null);
    }

    /**
     * 设置未读消息数变化监听器。
     * 注意:如果是在 activity 中设置,那么要在 activity 销毁时,调用 @link removeUnReadMessageCountChangedObserver(IUnReadMessageObserver)}
     * 否则会造成内存泄漏。
     */
    IUnReadMessageObserver observer = this::showMessageCount;
    private int unreadNum = 0;

    private void showMessageCount(int i) {
        unreadMessage.showCirclePointBadge();
        unreadMessage.getBadgeViewHelper().setBadgeTextSizeSp(10);
        unreadMessage.getBadgeViewHelper().setBadgeTextColorInt(Color.WHITE);
        unreadMessage.getBadgeViewHelper().setBadgeBgColorInt(Color.RED);
        unreadMessage.getBadgeViewHelper().setDraggable(true);
        unreadMessage.getBadgeViewHelper().setBadgePaddingDp(5);
        unreadMessage.getBadgeViewHelper().setBadgeBorderWidthDp(1);
        unreadMessage.getBadgeViewHelper().setBadgeBorderColorInt(Color.WHITE);
        if (i < 1) {
            unreadMessage.hiddenBadge();
            unreadNum = 0;
        } else if (i < 100) {
            unreadMessage.showTextBadge(String.valueOf(i));
            unreadNum = i;
        } else {
            unreadMessage.showTextBadge("99+");
            unreadNum = 99;
        }
        DesktopCornerUtil.setBadgeNumber(unreadNum);
    }

    /**
     * 未读消息监听
     */
    public void addUnReadMessageCountChangedObserver() {
        RongIM.getInstance().addUnReadMessageCountChangedObserver(observer,
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.SYSTEM);
    }

    /**
     * 移除未读消息监听
     */
    public void removeUnReadMessageCountChangedObserver() {
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(observer);
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
                GotoActivityUtils.gotoHome(context);
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
            com.officego.commonlib.common.GotoActivityUtils.loginClearActivity(context, TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRole()));
        }
    }
}

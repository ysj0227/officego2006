package com.officego.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.officego.MainActivity;
import com.officego.commonlib.base.BaseApplication;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.DesktopCornerUtil;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.officego.commonlib.constant.Constants.WXapi;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 **/
public class MyApplication extends BaseApplication {
    /**
     * 应用是否在后台
     */
    private boolean isAppInForeground;
    private String lastVisibleActivityName;
    private Intent nextOnForegroundIntent;
    private boolean isMainActivityIsCreated;
    private final String mPackageName = "com.officego";

    @Override
    public void onCreate() {
        super.onCreate();
        RootLoader bootLoader = new RootLoader(this);
        bootLoader.init();
        createWXAPI();
        observeAppInBackground();
        //App图标未读数量
        DesktopCornerUtil.init(this.getPackageName(), mPackageName + ".LaunchActivity", this);
    }

    //初始化微信分享
    private void createWXAPI() {
        String packageName = getApplicationInfo().packageName;
        String wxAppId = TextUtils.equals(mPackageName, packageName) ? Constants.APP_ID : Constants.APP_ID_TEST;
        WXapi = WXAPIFactory.createWXAPI(this, wxAppId, false);
        WXapi.registerApp(wxAppId);
    }

    /**
     * 监听应用是否转为后台
     */
    private void observeAppInBackground() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity instanceof MainActivity) {
                    isMainActivityIsCreated = true;
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                // 当切换为前台时启动预设的优先显示界面
                if (isMainActivityIsCreated && !isAppInForeground && nextOnForegroundIntent != null) {
                    activity.startActivity(nextOnForegroundIntent);
                    nextOnForegroundIntent = null;
                }

                lastVisibleActivityName = activity.getClass().getSimpleName();
                isAppInForeground = true;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                String pauseActivityName = activity.getClass().getSimpleName();
                /*
                 * 介于 Activity 生命周期在切换画面时现进行要跳转画面的 onResume，
                 * 再进行当前画面 onPause，所以当用户且到后台时肯定会为当前画面直接进行 onPause，
                 * 同过此来判断是否应用在前台
                 */
                if (pauseActivityName.equals(lastVisibleActivityName)) {
                    isAppInForeground = false;
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity instanceof MainActivity) {
                    isMainActivityIsCreated = false;
                }
            }
        });
    }

    /**
     * 当前 App 是否在前台
     *
     * @return
     */
    public boolean isAppInForeground() {
        return isAppInForeground;
    }

    /**
     * 获取最后在前台的 Activity 名称
     *
     * @return
     */
    public String getLastVisibleActivityName() {
        return lastVisibleActivityName;
    }

    /**
     * 设置当 App 切换为前台时启动的 intent，该 intent 在启动后情况
     *
     * @param intent
     */
    public void setOnAppForegroundStartIntent(Intent intent) {
        nextOnForegroundIntent = intent;
    }

    /**
     * 获取最近设置的未触发的启动 intent
     *
     * @return
     */
    public Intent getLastOnAppForegroundStartIntent() {
        return nextOnForegroundIntent;
    }

    /**
     * 判断是否进入到了主界面
     *
     * @return
     */
    public boolean isMainActivityCreated() {
        return isMainActivityIsCreated;
    }

}

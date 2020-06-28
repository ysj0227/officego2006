package com.officego.commonlib.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.officego.commonlib.utils.systembar.StatusBarCompat;
import com.officego.commonlib.utils.systembar.StatusBarUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class StatusBarUtils {

    public static final int TYPE_TRANSPARENT = 0;//透明
    public static final int TYPE_DARK = 1;//字体dark


    /**
     * 状态栏显示
     *
     * @param type
     */
    public static void setStatusBarColor(Activity activity, int type) {
//        if (type == TYPE_TRANSPARENT) {
//            setStatusBarFullTransparent(activity);
//        } else {
//            StatusBarLightMode(activity);
//        }

        setStatusBarColor(activity);
    }

    /**
     * 白底灰字
     */
    public static void setStatusBarColor(Activity activity) {
        //状态栏颜色是否隐藏
        StatusBarCompat.translucentStatusBar(activity, true);
        //修改状态栏字体颜色
        StatusBarUtil.setImmersiveStatusBar(activity, true);
    }

    /**
     * 全透状态栏
     */
    public static void setStatusBarFullTransparent(Activity activity) {
        // 21->5.0  22->5.1  23->6.0  24->7.0

        if (Build.VERSION.SDK_INT > 22) {//21表示5.0  Build.VERSION_CODES.LOLLIPOP
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 全透状态栏
     */
    public static void setStatusBarMainColor(Activity activity,int color) {
        // 21->5.0  22->5.1  23->6.0  24->7.0

        if (Build.VERSION.SDK_INT > 22) {//21表示5.0  Build.VERSION_CODES.LOLLIPOP
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标， * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android * @param activity * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity, true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * * 可以用来判断是否为Flyme用户 *
     *
     * @param window 需要设置的窗口
     *               * @param dark 是否把状态栏文字及图标颜色设置为深色 *
     * @return boolean 成功执行返回true *
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上 * @param activity * @param dark 是否把状态栏文字及图标颜色设置为深色 * @return boolean 成功执行返回true *
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

}

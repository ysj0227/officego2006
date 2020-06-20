package com.officego.commonlib.utils;

import android.content.Context;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class DBUtils {
    public static void init(Context context) {
        LitePal.initialize(context);
    }

    /**
     * If you just want to create a new database but with same configuration as litepal.xml, you can do it with:
     */
    public static void createNewDb(String newdb) {
        LitePalDB litePalDB = LitePalDB.fromDefault(newdb);
        LitePal.use(litePalDB);
    }

    /**
     * You can always switch back to default database with:
     */
    public static void useDefault() {
        LitePal.useDefault();
    }
}

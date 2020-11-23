package com.officego.commonlib.common.rpc;

import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.retrofit.BaseRetrofitClient1;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/5/8.
 * Descriptions:
 **/
public class OfficegoRetrofitClient1 extends BaseRetrofitClient1 {
    private static volatile OfficegoRetrofitClient1 instance;

    private OfficegoRetrofitClient1() {
        init(AppConfig.APP_URL, getHeaders());
    }

    public static OfficegoRetrofitClient1 getInstance() {
        if (instance == null) {
            synchronized (OfficegoRetrofitClient1.class) {
                if (instance == null)
                    instance = new OfficegoRetrofitClient1();
            }
        }
        return instance;
    }

    public static void createInstance() {
        if (instance != null) {
            instance = null;
        }
        instance = new OfficegoRetrofitClient1();
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
//        if (!TextUtils.isEmpty(SpUtils.getStoreToken())) {
//            headers.put("Authorization", "Bearer " + SpUtils.getStoreToken());
//        }
        return headers;
    }
}

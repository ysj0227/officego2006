package com.officego.commonlib.common.rpc;

import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.retrofit.BaseRetrofitClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/5/8.
 * Descriptions:
 **/
public class OfficegoRetrofitClient extends BaseRetrofitClient {
    private static volatile OfficegoRetrofitClient instance;

    private OfficegoRetrofitClient() {
        init(AppConfig.APP_URL, getHeaders());
    }

    public static OfficegoRetrofitClient getInstance() {
        if (instance == null) {
            synchronized (OfficegoRetrofitClient.class) {
                if (instance == null)
                    instance = new OfficegoRetrofitClient();
            }
        }
        return instance;
    }

    public static void createInstance() {
        if (instance != null) {
            instance = null;
        }
        instance = new OfficegoRetrofitClient();
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
//        if (!TextUtils.isEmpty(SpUtils.getStoreToken())) {
//            headers.put("Authorization", "Bearer " + SpUtils.getStoreToken());
//        }
        return headers;
    }
}

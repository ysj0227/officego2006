package com.owner.rpc;

import com.officego.commonlib.constant.AppConfig;
import com.officego.commonlib.retrofit.BaseRetrofitClient;
import com.officego.commonlib.retrofit.BaseRetrofitClientWeb;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YangShiJie
 * Data 2020/5/8.
 * Descriptions:
 **/
public class OfficegoRetrofitClientWeb extends BaseRetrofitClientWeb {
    private static volatile OfficegoRetrofitClientWeb instance;

    private OfficegoRetrofitClientWeb() {
        init(AppConfig.WEB_URL_SCAN_LOGIN, getHeaders());
    }

    public static OfficegoRetrofitClientWeb getInstance() {
        if (instance == null) {
            synchronized (OfficegoRetrofitClientWeb.class) {
                if (instance == null)
                    instance = new OfficegoRetrofitClientWeb();
            }
        }
        return instance;
    }

    public static void createInstance() {
        if (instance != null) {
            instance = null;
        }
        instance = new OfficegoRetrofitClientWeb();
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
//        if (!TextUtils.isEmpty(SpUtils.getStoreToken())) {
//            headers.put("Authorization", "Bearer " + SpUtils.getStoreToken());
//        }
        return headers;
    }
}

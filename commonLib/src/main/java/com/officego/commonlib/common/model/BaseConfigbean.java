package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shijie
 * Date 2021/3/30
 **/
public class BaseConfigbean {


    /**
     * jumpUrl :
     */

    @SerializedName("enterprise_service")
    private EnterpriseServiceBean enterpriseService;

    public EnterpriseServiceBean getEnterpriseService() {
        return enterpriseService;
    }

    public void setEnterpriseService(EnterpriseServiceBean enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    public static class EnterpriseServiceBean {
        @SerializedName("jumpUrl")
        private String jumpUrl;

        public String getJumpUrl() {
            return jumpUrl;
        }

        public void setJumpUrl(String jumpUrl) {
            this.jumpUrl = jumpUrl;
        }
    }
}

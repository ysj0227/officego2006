package com.officego.commonlib.common.model.owner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/2
 **/
public class UploadImageBean {


    @SerializedName("urls")
    private List<UrlsBean> urls;

    public List<UrlsBean> getUrls() {
        return urls;
    }

    public void setUrls(List<UrlsBean> urls) {
        this.urls = urls;
    }

    public static class UrlsBean {
        /**
         * url : https://img.officego.com/test/1596348101924.png?x-oss-process=style/large
         */

        @SerializedName("url")
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

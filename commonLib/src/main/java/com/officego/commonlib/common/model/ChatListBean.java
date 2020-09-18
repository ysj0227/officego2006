package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/9/16
 **/
public class ChatListBean {

    /**
     * list : [{"buildingName":"APP金星大厦物业管理公司","chattedId":"2501","Isblock":false,"nickname":"李芳","IsDepartureStatus":false,"typeId":1,"sort":0,"avatar":"https://img.officego.com/user/1592975329881.png?x-oss-process=style/small"},{"buildingName":"officego测试网点","chattedId":"4761","Isblock":false,"nickname":"1","IsDepartureStatus":false,"typeId":0,"sort":0,"avatar":"https://img.officego.com/head.png"}]
     * states :
     */

    @SerializedName("states")
    private String states;
    @SerializedName("list")
    private List<ListBean> list;

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * buildingName : APP金星大厦物业管理公司
         * chattedId : 2501
         * Isblock : false
         * nickname : 李芳
         * IsDepartureStatus : false
         * typeId : 1
         * sort : 0
         * avatar : https://img.officego.com/user/1592975329881.png?x-oss-process=style/small
         */

        @SerializedName("chattedId")
        private String chattedId;
        @SerializedName("Isblock")
        private boolean Isblock;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("IsDepartureStatus")
        private boolean IsDepartureStatus;
        @SerializedName("typeId")
        private int typeId;
        @SerializedName("sort")
        private int sort;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("buildingName")
        private String buildingName;

        public String getChattedId() {
            return chattedId;
        }

        public void setChattedId(String chattedId) {
            this.chattedId = chattedId;
        }

        public boolean isIsblock() {
            return Isblock;
        }

        public void setIsblock(boolean Isblock) {
            this.Isblock = Isblock;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public boolean isIsDepartureStatus() {
            return IsDepartureStatus;
        }

        public void setIsDepartureStatus(boolean IsDepartureStatus) {
            this.IsDepartureStatus = IsDepartureStatus;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }
    }
}

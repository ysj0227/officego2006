package com.officego.ui.message;

import com.officego.commonlib.common.model.ChatHouseBean;

public class HouseTags {

    /**
     * 聊天楼盘tags
     *
     * @param data data
     * @return String
     */
    public static String getTags(ChatHouseBean data) {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < data.getBuilding().getTags().size(); i++) {
            if (data.getBuilding().getTags().size() == 1) {
                key.append(data.getBuilding().getTags().get(i).getDictCname());
            } else {
                key.append(data.getBuilding().getTags().get(i).getDictCname()).append(",");
            }
        }
        if (data.getBuilding().getTags().size() > 1) {
            key = key.replace(key.length() - 1, key.length(), "");
        }
        return key.toString();
    }

    /**
     * 聊天楼盘tags
     *
     * @param data data
     * @return String
     */
    public static String getHouseTags(ChatHouseBean data) {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < data.getHouse().getTags().size(); i++) {
            if (data.getHouse().getTags().size() == 1) {
                key.append(data.getHouse().getTags().get(i).getDictCname());
            } else {
                key.append(data.getHouse().getTags().get(i).getDictCname()).append(",");
            }
        }
        if (data.getHouse().getTags().size() > 1) {
            key = key.replace(key.length() - 1, key.length(), "");
        }
        return key.toString();
    }
}

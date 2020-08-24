package com.officego.commonlib.common.sensors;

import android.content.Context;
import android.text.TextUtils;

import com.meituan.android.walle.WalleChannelReader;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 神策数据分析
 */
public class SensorsTrack {

    public static void superProperties() {
        // 将应用名称作为事件公共属性，后续所有 track() 追踪的事件都会自动带上 "AppName" 属性
        try {
            JSONObject properties = new JSONObject();
            properties.put("platform_type", "Android");
            properties.put("app_name", "OfficeGo");
            SensorsDataAPI.sharedInstance().registerSuperProperties(properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 初始化 SDK 后，设置动态公共属性
    public static void dynamicSuperProperties() {
        SensorsDataAPI.sharedInstance().registerDynamicSuperProperties(() -> {
            try {
                // 比如 isLogin() 是用于获取用户当前的登录状态，SDK 会自动获取 getDynamicSuperProperties 中的属性添加到触发的事件中。
                boolean bool = !TextUtils.isEmpty(SpUtils.getSignToken());
                return new JSONObject().put("is_login", bool);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * 用户在注册成功时
     * 用户登录成功时
     * 已登录用户每次启动 App
     */
    public static void sensorsLogin(String uid) {
        if (!TextUtils.isEmpty(SpUtils.getUserId())) {
            SensorsDataAPI.sharedInstance().login(uid);
        }
    }

    /**
     * 记录激活事件
     */
    public static void trackInstallation(Context context) {
        try {
            String channel = WalleChannelReader.getChannel(context);
            JSONObject properties = new JSONObject();
            //这里的 DownloadChannel 负责记录下载商店的渠道，值应传入具体应用商店包的标记。如果没有为不同商店打多渠道包，则可以忽略该属性的代码示例。
            if (!TextUtils.isEmpty(channel)) {
                properties.put("register_channel", channel);
            }
            // 触发激活事件
            SensorsDataAPI.sharedInstance().trackInstallation("AppInstall", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     */
    public static void login() {
        SensorsDataAPI.sharedInstance().track("visit_reg_login", new JSONObject());
    }

    /**
     * 点击获取验证码
     */
    public static void smsCode() {
        try {
            JSONObject properties = new JSONObject();
            if (!TextUtils.isEmpty(SpUtils.getUserId())) {
                properties.put("uid", SpUtils.getUserId());
            }
            SensorsDataAPI.sharedInstance().track("click_code", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证码输入框
     */
    public static void codeInput() {
        try {
            JSONObject properties = new JSONObject();
            if (!TextUtils.isEmpty(SpUtils.getUserId())) {
                properties.put("uid", SpUtils.getUserId());
            }
            SensorsDataAPI.sharedInstance().track("click_code_input", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索-访问搜索页面
     */
    public static void visitSearchPage() {
        try {
            JSONObject properties = new JSONObject();
            if (!TextUtils.isEmpty(SpUtils.getUserId())) {
                properties.put("uid", SpUtils.getUserId());
            }
            SensorsDataAPI.sharedInstance().track("visit_search_page", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索-楼盘推荐页面点击搜索按钮
     */
    public static void searchButtonIndex() {
        try {
            JSONObject properties = new JSONObject();
            if (!TextUtils.isEmpty(SpUtils.getUserId())) {
                properties.put("uid", SpUtils.getUserId());
            }
            SensorsDataAPI.sharedInstance().track("click_search_button_index", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索-访问搜索结果页
     * searchType 推荐词/历史词/搜索按钮
     */
    public static void visitSearchResultsPage(int type, String keyWords) {
        String searchType;
        if (type == 0) {
            searchType = "推荐词";
        } else if (type == 1) {
            searchType = "历史词";
        } else {
            searchType = "搜索按钮";
        }
        try {
            JSONObject properties = new JSONObject();
            properties.put("searchType", searchType);
            properties.put("userSearchContent", keyWords);
            SensorsDataAPI.sharedInstance().track("visit_search_results_page", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索-点击搜索结果页某一条数据内容
     * 网点、楼盘
     */
    public static void clickSearchResultsPage(String keyWords, int type,
                                              int clickLocal, int buildingId) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("userSearchContent", keyWords);
            properties.put("clickLocal", clickLocal + "");
            properties.put("buildingId", buildingId + "");
            properties.put("buildOrHouse", type == Constants.TYPE_BUILDING ? "楼盘" : "网点");
            SensorsDataAPI.sharedInstance().track("click_search_results_page", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 访问楼盘网点列表
     * uCity	地区	STRING
     * buildCnt	列表展示数	NUMBER
     * areaType	区域类型	STRING
     * areaContent	区域筛选内容	"STRING
     * 可能为一级区域或者地铁几号线
     * 也可能为二级拼接字符串"
     * officeType	办公场地选择类型	"STRING
     * 办公楼和共享办公、全部"
     * oderType	排序选择类型	STRING
     * allOfficeType	综合筛选办公场地选择类型	STRING
     * area	面积	逗号拼接字符串
     * dayPrice	租金	逗号拼接字符串
     * simple	工位	逗号拼接字符串
     * decoration	装修类型	STRING
     * tags	房源特色	STRING
     * isVr	是否只看VR	BOOL
     * isSelect	是否有筛选	BOOL
     */
    public static void visitBuildingNetworkList(String uCity, String areaType, String areaContent, int officeType, int oderType,
                                                String area, String dayPrice, String simple, String decorationName, boolean isVr, boolean isSelect) {
        String strOfficeType, strOrder;
        if (officeType == 1) {
            strOfficeType = "写字楼";
        } else if (officeType == 2) {
            strOfficeType = "共享办公";
        } else {
            strOfficeType = "全部";
        }
        if (oderType == 0) {
            strOrder = "默认排序";
        } else if (oderType == 1) {
            strOrder = "价格从高到低";
        } else if (oderType == 2) {
            strOrder = "价格从低到高";
        } else if (oderType == 3) {
            strOrder = "面积从大到小";
        } else {
            strOrder = "面积从小到大";
        }
        try {
            JSONObject properties = new JSONObject();
            properties.put("uCity", uCity);
            if (!TextUtils.equals("区域", areaType)) {
                properties.put("areaType", areaType);
            }
            if (!TextUtils.isEmpty(areaContent)) {
                properties.put("areaContent", areaContent);
            }
            if (!TextUtils.isEmpty(area)) {
                properties.put("area", area);
            }
            if (!TextUtils.isEmpty(dayPrice)) {
                properties.put("dayPrice", dayPrice);
            }
            if (!TextUtils.isEmpty(simple)) {
                properties.put("simple", simple);
            }
            if (!TextUtils.isEmpty(decorationName)) {
                properties.put("decoration", decorationName);
            }
            properties.put("officeType", strOfficeType);
            properties.put("oderType", strOrder);
            properties.put("isVr", isVr);
            properties.put("isSelect", isSelect);
            SensorsDataAPI.sharedInstance().track("visit_building_network_list", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 访问楼盘详情页
     * 楼盘在楼盘列表显示位置，点击在列表中第几条
     *
     * @param buildLocation 楼盘列表位置
     * @param buildingId    id
     */
    public static void visitBuildingDataPage(int buildLocation, int buildingId) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("buildLocation", buildLocation + "");
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            SensorsDataAPI.sharedInstance().track("visit_building_data_page", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 楼盘详情页阅读完成
     * 滑动到页面底部
     *
     * @param buildingId
     * @param isRead     是否阅读完成
     */
    public static void visitBuildingDataPageComplete(int buildingId, boolean isRead) {
        try {
            JSONObject properties = new JSONObject();
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            properties.put("isRead", isRead);
            SensorsDataAPI.sharedInstance().track("visit_building_data_page_complete", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击收藏按钮 楼盘网点详情
     *
     * @param buildingId buildingId
     * @param isCollect  是否收藏成功
     */
    public static void clickFavoritesButton(int buildingId, boolean isCollect) {
        try {
            JSONObject properties = new JSONObject();
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            properties.put("isCollect", isCollect);
            SensorsDataAPI.sharedInstance().track("click_favorites_button", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击收藏按钮 楼盘网点下的房源
     *
     * @param houseId   buildingId
     * @param isCollect 是否收藏成功
     */
    public static void clickFavoritesButtonChild(String houseId, boolean isCollect) {
        try {
            JSONObject properties = new JSONObject();
            if (!TextUtils.isEmpty(houseId) && !TextUtils.equals("0", houseId)) {
                properties.put("houseId", houseId);
            }
            properties.put("isCollect", isCollect);
            SensorsDataAPI.sharedInstance().track("click_favorites_button", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 楼盘详情页筛选房源
     *
     * @param buildingId buildingId
     * @param houseCnt   房源套数
     */
    public static void buildingDataPageScreen(int buildingId, String houseCnt) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("buildingId", buildingId + "");
            properties.put("houseCnt", houseCnt);
            SensorsDataAPI.sharedInstance().track("building_data_page_screen", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击楼盘详情页房源筛选按钮
     *
     * @param buildingId buildingId
     * @param area       面积
     * @param simple     工位
     */
    public static void clickBuildingDataPageScreenButton(int buildingId, String area, String simple) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("buildingId", buildingId + "");
            if (!TextUtils.isEmpty(area)) {
                properties.put("area", area);
            }
            if (!TextUtils.isEmpty(simple)) {
                properties.put("simple", simple);
            }
            SensorsDataAPI.sharedInstance().track("click_building_data_page_screen_button", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 访问房源详情页
     *
     * @param houseId houseId
     */
    public static void visitHouseDataPage(String houseId) {
        try {
            JSONObject properties = new JSONObject();
            if (!TextUtils.isEmpty(houseId) && !TextUtils.equals("0", houseId)) {
                properties.put("houseId", houseId);
            }
            SensorsDataAPI.sharedInstance().track("visit_house_data_page", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * IM中点击预约看房
     */
    public static void clickImOrderSeeHouseButton(String buildingId, String houseId,
                                                  String chatedId, String chatedName, String timestamp) {
        try {
            JSONObject properties = new JSONObject();
            if (!TextUtils.isEmpty(buildingId) && !TextUtils.equals("0", buildingId)) {
                properties.put("buildingId", buildingId);
            }
            if (!TextUtils.isEmpty(houseId) && !TextUtils.equals("0", houseId)) {
                properties.put("houseId", houseId);
            }
            if (!TextUtils.isEmpty(chatedId) && !TextUtils.equals("0", chatedId)) {
                properties.put("chatedId", chatedId);
            }
            if (!TextUtils.isEmpty(chatedName)) {
                properties.put("chatedName", chatedName);
            }
            properties.put("timestamp", timestamp);
            SensorsDataAPI.sharedInstance().track("click_im_order_see_house_button", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击看房时间选择按钮
     *
     * @param buildOrHouse 1:从楼盘进入返回building对象,2:从房源进入返回house对象
     * @param bType        "楼盘" : "网点"
     * @param buildingId   buildingId
     * @param timestamp    日期
     */
    public static void orderSeeHouseTime(int buildOrHouse, int bType, int buildingId, String timestamp) {
        String mText;
        if (buildOrHouse == 2) {
            mText = "房源";
        } else {
            mText = (bType == Constants.TYPE_BUILDING ? "楼盘" : "网点");
        }
        try {
            JSONObject properties = new JSONObject();
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            properties.put("timestamp", timestamp);
            properties.put("buildOrHouse", mText);
            SensorsDataAPI.sharedInstance().track("order_see_house_time", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预约看房时间确定
     *
     * @param buildOrHouse 1:从楼盘进入返回building对象,2:从房源进入返回house对象
     * @param bType        "楼盘" : "网点"
     * @param buildingId   buildingId
     * @param timestamp    点击时间
     * @param seeTime      预约时间
     */
    public static void confirmSeeHouseTime(int buildOrHouse, int bType, int buildingId, String timestamp, String seeTime) {
        String mText;
        if (buildOrHouse == 2) {
            mText = "房源";
        } else {
            mText = (bType == Constants.TYPE_BUILDING ? "楼盘" : "网点");
        }
        try {
            JSONObject properties = new JSONObject();
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            properties.put("buildOrHouse", mText);
            properties.put("timestamp", timestamp);
            properties.put("seeTime", seeTime);
            SensorsDataAPI.sharedInstance().track("confirm_see_house_time", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 预约看房申请提交
     * buildOrHouse	页面类型	STRING
     * seeTime	预约时间	DATETIME
     * buildingId	楼盘ID	STRING
     * timestamp	行程预约ID	DATETIME
     * status	行程状态	STRING
     * chatedId	房东ID	STRING
     * chatedName	房东名称	STRING
     * createTime	时间	"DATETIME   按钮提交时间
     * 2020-09-03"
     */
    public static void submitBookingSeeHouse(int buildOrHouse, int bType, int buildingId, String timestamp, String seeTime,
                                             String chatedId, String chatedName, String createTime) {
        String mText;
        if (buildOrHouse == 2) {
            mText = "房源";
        } else {
            mText = (bType == Constants.TYPE_BUILDING ? "楼盘" : "网点");
        }
        try {
            JSONObject properties = new JSONObject();
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            properties.put("buildOrHouse", mText);
            properties.put("timestamp", timestamp);
            properties.put("seeTime", seeTime);
            properties.put("status", "预约等待房东审核");
            if (!TextUtils.isEmpty(chatedId) && !TextUtils.equals("0", chatedId)) {
                properties.put("chatedId", chatedId);
            }
            if (!TextUtils.isEmpty(chatedName)) {
                properties.put("chatedName", chatedName);
            }
            properties.put("createTime", createTime);
            SensorsDataAPI.sharedInstance().track("submit_booking_see_house", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发起电话交换
     * buildingId	楼盘ID	STRING
     * houseId	房源ID	STRING
     * rid	发起方身份	STRING
     * timestamp	行程预约ID	STRING
     * statusPhone	电话交换状态	STRING
     * createTime	时间	DATETIME
     * 发送交换此时 createTime 和timestamp相同
     */
    public static void clickPhoneExchangeButton(int buildingId, int houseId, String timestamp, String createTime) {
        try {
            JSONObject properties = new JSONObject();
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            if (houseId != 0) {
                properties.put("houseId", houseId + "");
            }
            properties.put("rid", TextUtils.equals(Constants.TYPE_TENANT, SpUtils.getRole()) ? "租户" : "房东");
            properties.put("statusPhone", "申请中");
            properties.put("timestamp", timestamp);
            properties.put("createTime", createTime);
            SensorsDataAPI.sharedInstance().track("click_phone_exchange_button", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 电话交换状态确认
     * buildingId	楼盘ID	STRING
     * houseId	房源ID	STRING
     * buildOrHouse	页面类型	STRING
     * rid	发起方身份	STRING
     * timestamp	行程预约ID	STRING
     * statusPhone	电话交换状态	STRING
     * isSuccess	是否成功	BOOL
     */
    public static void confirmPhoneExchangeState(int buildOrHouse, int bType, int buildingId, int houseId, String timestamp, boolean isAgree) {
        try {
            JSONObject properties = new JSONObject();
            String mText;
            if (buildOrHouse == 2) {
                mText = "房源";
            } else if (buildOrHouse == 1) {
                mText = (bType == Constants.TYPE_BUILDING ? "楼盘" : "网点");
            } else {
                mText = "";
            }
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            if (houseId != 0) {
                properties.put("houseId", houseId + "");
            }
            if (!TextUtils.isEmpty(mText)) {
                properties.put("buildOrHouse", mText);
            }
            properties.put("timestamp", timestamp);
            properties.put("rid", TextUtils.equals(Constants.TYPE_TENANT, SpUtils.getRole()) ? "租户" : "房东");
            properties.put("statusPhone", isAgree ? "通过" : "拒绝");
            properties.put("isSuccess", isAgree);
            SensorsDataAPI.sharedInstance().track("confirm_phone_exchange_state", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发起微信交换
     * buildingId	楼盘ID	STRING
     * houseId	房源ID	STRING
     * rid	发起方身份	STRING
     * timestamp	行程预约ID	STRING
     * statusWechat	微信交换状态	STRING
     * createTime	时间	DATETIME
     */
    public static void clickWechatExchangeButton(int buildingId, int houseId, String timestamp, String createTime) {
        try {
            JSONObject properties = new JSONObject();
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            if (houseId != 0) {
                properties.put("houseId", houseId + "");
            }
            properties.put("rid", TextUtils.equals(Constants.TYPE_TENANT, SpUtils.getRole()) ? "租户" : "房东");
            properties.put("statusWechat", "申请中");
            properties.put("timestamp", timestamp);
            properties.put("createTime", createTime);
            SensorsDataAPI.sharedInstance().track("click_wechat_exchange_button", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信交换状态确认
     * buildingId	楼盘ID	STRING
     * houseId	房源ID	STRING
     * buildOrHouse	页面类型	STRING
     * rid	发起方身份	STRING
     * timestamp	行程预约ID	STRING
     * statusPhone	电话交换状态	STRING
     * isSuccess	是否成功	BOOL
     */
    public static void confirmWechatExchangeState(int buildOrHouse, int bType, int buildingId, int houseId, String timestamp, boolean isAgree) {
        try {
            JSONObject properties = new JSONObject();
            String mText;
            if (buildOrHouse == 2) {
                mText = "房源";
            } else if (buildOrHouse == 1) {
                mText = (bType == Constants.TYPE_BUILDING ? "楼盘" : "网点");
            } else {
                mText = "";
            }
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            if (houseId != 0) {
                properties.put("houseId", houseId + "");
            }
            if (!TextUtils.isEmpty(mText)) {
                properties.put("buildOrHouse", mText);
            }
            properties.put("timestamp", timestamp);
            properties.put("rid", TextUtils.equals(Constants.TYPE_TENANT, SpUtils.getRole()) ? "租户" : "房东");
            properties.put("statusWechat", isAgree ? "通过" : "拒绝");
            properties.put("isSuccess", isAgree);
            SensorsDataAPI.sharedInstance().track("confirm_wechat_exchange_state", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 租户切换成房东
     */
    public static void tenantToOwner() {
        SensorsDataAPI.sharedInstance().track("tenant_to_owner", new JSONObject());
    }

    /**
     * 房东切换成租户
     */
    public static void ownerToTenant() {
        SensorsDataAPI.sharedInstance().track("owne_to_tenant", new JSONObject());
    }

    /**
     * 点击楼盘卡片
     * buildingId	楼盘ID	STRING
     * buildLocation	楼盘列表位置	NUMBER
     * isVr	是否VR	BOOL
     */
    public static void clickCardShow(int buildingId, String buildLocation, boolean isVr) {
        try {
            JSONObject properties = new JSONObject();
            if (buildingId != 0) {
                properties.put("buildingId", buildingId + "");
            }
            properties.put("buildLocation", buildLocation);
            properties.put("isVr", isVr);
            SensorsDataAPI.sharedInstance().track("clickShow", properties);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.officego.commonlib.common.rpc;

import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.VersionBean;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.ExchangeContactsBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.IdentitychattedMsgBean;
import com.officego.commonlib.common.model.RongUserInfoBean;
import com.officego.commonlib.common.model.owner.BuildingEditBean;
import com.officego.commonlib.common.model.owner.BuildingJointWorkBean;
import com.officego.commonlib.common.model.owner.HouseBean;
import com.officego.commonlib.common.rpc.request.BuildingJointWorkInterface;
import com.officego.commonlib.common.rpc.request.ChatInterface;
import com.officego.commonlib.common.rpc.request.DirectoryInterface;
import com.officego.commonlib.common.rpc.request.LicenceInterface;
import com.officego.commonlib.common.rpc.request.MineMsgInterface;
import com.officego.commonlib.common.rpc.request.ScheduleInterface;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by YangShiJie
 * Data 2020/5/8.
 * Descriptions:
 **/
public class OfficegoApi {
    public static final String TAG = "OfficegoApi";

    public static OfficegoApi getInstance() {
        return Singleton.INSTANCE;
    }

    private static final class Singleton {
        private static final OfficegoApi INSTANCE = new OfficegoApi();
    }

    private RequestBody requestBody(String content) {
        return RequestBody.create(MediaType.parse("text/plain"), content);
    }

    /**
     * 版本更新
     *
     * @param versioncode
     * @param callback
     */
    public void updateVersion(String versioncode, RetrofitCallback<VersionBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("versioncode", requestBody(versioncode));
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .updateVersion(map)
                .enqueue(callback);
    }

    /**
     * id 	是 	int 	行程id
     * auditStatus 	是 	int 	审核状态0预约1预约成功(同意)2预约失败(拒绝)3已看房4未看房
     */
    public void updateAuditStatus(String id, int auditStatus,
                                  RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("id", requestBody(id + ""));
        map.put("auditStatus", requestBody(auditStatus + ""));
        OfficegoRetrofitClient.getInstance().create(ScheduleInterface.class)
                .updateAuditStatus(map)
                .enqueue(callback);
    }

    /**
     * 添加微信
     * wxId 	是 	String 	微信号
     * channel 	是 	int 	终端渠道,1:IOS,2:安卓,3:H5
     * token 	是 	String 	token (登录接口返回)
     */
    public void addWechat(String wxId, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("wxId", requestBody(wxId));
        map.put("channel", requestBody("2"));
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .bindWechat(map)
                .enqueue(callback);
    }

    /**
     * uid 	是 	int 	targetId 聊天对方的id
     * token 	是 	是 	token
     * houseId 	是 	int 	从房源进入聊天页面需要传递
     * buildingId 	是 	int 	从楼盘进入聊天页面需要传递
     * 获取大楼详情
     */
    public void getChatHouseDetails(int buildingId, int houseId, String targetId, RetrofitCallback<ChatHouseBean> callback) {
        LogCat.e(TAG, "token=" + SpUtils.getSignToken() +
                " uid=" + targetId +
                " buildingId=" + (buildingId == 0 ? "" : buildingId) +
                " houseId=" + (houseId == 0 ? "" : String.valueOf(houseId)));
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("uid", requestBody(targetId));
        map.put("buildingId", requestBody(buildingId == 0 ? "" : String.valueOf(buildingId)));
        map.put("houseId", requestBody(houseId == 0 ? "" : String.valueOf(houseId)));
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .getChatHouseDetails(map)
                .enqueue(callback);
    }

    /**
     * 重新获取融云token
     */
    public void getRongCloudToken(RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .getRongCloudToken(map)
                .enqueue(callback);
    }

    /**
     * 聊天
     *
     * @param callback
     */
    public void isChat(int buildingId, int houseId, String targetId, RetrofitCallback<FirstChatBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("uid", requestBody(targetId));
        map.put("buildingId", requestBody(buildingId == 0 ? "" : String.valueOf(buildingId)));
        map.put("houseId", requestBody(houseId == 0 ? "" : String.valueOf(houseId)));
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .isChat(map)
                .enqueue(callback);
    }

    /**
     * 判断是否可以交换手机和微信
     */
    public void exchangeContactsVerification(String targetId, RetrofitCallback<ExchangeContactsBean> callback) {
        LogCat.e(TAG, "111  token=" + SpUtils.getSignToken() + "  targetId=" + targetId);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("targetId", requestBody(targetId));
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .exchangeContactsVerification(map)
                .enqueue(callback);
    }

    /**
     * 管理员审核申请加入员工接口
     * auditStatus 1通过2取消
     */
    public void updateAuditStatusIdentity(int id, int licenceId, int auditStatus,
                                          RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("identityType", requestBody(""));
        map.put("id", requestBody(id + ""));
        map.put("licenceId", requestBody(licenceId + ""));
        map.put("auditStatus", requestBody(auditStatus + ""));
        OfficegoRetrofitClient.getInstance().create(LicenceInterface.class)
                .updateAuditStatusApp(map)
                .enqueue(callback);
    }

    /**
     * 认证聊天获取信息
     */
    public void identityChattedMsg(String targetId,
                                   RetrofitCallback<IdentitychattedMsgBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("targetId", requestBody(targetId));
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .identityChattedMsg(map)
                .enqueue(callback);
    }

    /**
     * 获取消息信息
     */
    public void getRongUserInfo(String targetId, RetrofitCallback<RongUserInfoBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("targetId", requestBody(targetId));
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .getRongUserInfo(map)
                .enqueue(callback);
    }

    /**
     * 获取会话列表
     */
    public void getChatList(RetrofitCallback<ChatListBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .getChatList(map)
                .enqueue(callback);
    }

    /**
     * 获取楼盘特色
     *
     * @param callback
     */
    public void getBuildingUnique(RetrofitCallback<List<DirectoryBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("code", requestBody("buildingUnique"));
        OfficegoRetrofitClient.getInstance().create(DirectoryInterface.class)
                .getDictionary(map)
                .enqueue(callback);
    }

    /**
     * 获取网点特色
     *
     * @param callback
     */
    public void getBranchUnique(RetrofitCallback<List<DirectoryBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("code", requestBody("branchUnique"));
        OfficegoRetrofitClient.getInstance().create(DirectoryInterface.class)
                .getDictionary(map)
                .enqueue(callback);
    }

    /**
     * 获取房源特色
     *
     * @param callback
     */
    public void getHouseUnique(RetrofitCallback<List<DirectoryBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("code", requestBody("houseUnique"));
        OfficegoRetrofitClient.getInstance().create(DirectoryInterface.class)
                .getDictionary(map)
                .enqueue(callback);
    }

    /**
     * 装修类型
     *
     * @param callback
     */
    public void getDecoratedType(RetrofitCallback<List<DirectoryBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("code", requestBody("decoratedType"));
        OfficegoRetrofitClient.getInstance().create(DirectoryInterface.class)
                .getDictionary(map)
                .enqueue(callback);
    }

    /**
     * 基础服务
     *
     * @param callback
     */
    public void getBasicServices(RetrofitCallback<List<DirectoryBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("code", requestBody("basicServices"));
        OfficegoRetrofitClient.getInstance().create(DirectoryInterface.class)
                .getDictionary(map)
                .enqueue(callback);
    }

    /**
     * 企业服务
     *
     * @param callback
     */
    public void getCompanyService(RetrofitCallback<List<DirectoryBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("code", requestBody("companyService"));
        OfficegoRetrofitClient.getInstance().create(DirectoryInterface.class)
                .getDictionary(map)
                .enqueue(callback);
    }

    /**
     * 会议室配套
     *
     * @param callback
     */
    public void roomMatchingService(RetrofitCallback<List<DirectoryBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("code", requestBody("roomMatchingUnique"));
        OfficegoRetrofitClient.getInstance().create(DirectoryInterface.class)
                .getDictionary(map)
                .enqueue(callback);
    }

    //业主首页楼盘网点添加或编辑

    /**
     * 楼盘或网点列表
     */
    public void getBuildingJointWorkList(RetrofitCallback<BuildingJointWorkBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .getBuildingJointWorkList(map)
                .enqueue(callback);
    }

    /**
     * 房源列表
     * buildingId 	是 	int 	楼盘id
     * <p>
     * isTemp 	是 	int 	是不是临时的楼盘；0不是，1是
     * pageNo 	是 	int 	当前页
     * pageSize 	是 	int 	每页条数
     * isStatus 	否 	int 	0全部1发布2下架
     * keyWord 	否 	String 	关键字
     */
    public void getHouseList(int buildingId, int isTemp, int pageNo, int isStatus,
                             RetrofitCallback<HouseBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.put("pageNo", requestBody(pageNo + ""));
        map.put("pageSize", requestBody("10"));
//        map.put("isStatus", requestBody(isStatus + ""));
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .getHouseList(map)
                .enqueue(callback);
    }

    /**
     * 上下架
     * houseId 	是 	Integer 	房源id
     * isRelease 	是 	Integer 	1发布2下架
     */
    public void isPublishHouse(int houseId, int isRelease, int isTemp, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("houseId", requestBody(houseId + ""));
        map.put("isRelease", requestBody(isRelease + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .getHouseRelease(map)
                .enqueue(callback);
    }

    /**
     * 房源删除
     * houseId 	    是 	Integer 	房源id
     * isTemp 	是否为临时房源 0:不是 1:是
     */
    public void getHouseDelete(int houseId, int isTemp, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("houseId", requestBody(houseId + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .getHouseDelete(map)
                .enqueue(callback);
    }

    /**
     * 楼盘网点编辑
     */
    public void getBuildingEdit(int buildingId, int isTemp, RetrofitCallback<BuildingEditBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .getBuildingEdit(map)
                .enqueue(callback);
    }
}

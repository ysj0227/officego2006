package com.officego.commonlib.common.rpc;

import android.text.TextUtils;

import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.VersionBean;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.ChatListBean;
import com.officego.commonlib.common.model.CouponDetailsBean;
import com.officego.commonlib.common.model.CouponListBean;
import com.officego.commonlib.common.model.CouponWriteOffBean;
import com.officego.commonlib.common.model.CouponWriteOffListBean;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.common.model.ExchangeContactsBean;
import com.officego.commonlib.common.model.FirstChatBean;
import com.officego.commonlib.common.model.IdentitychattedMsgBean;
import com.officego.commonlib.common.model.JPushLoginBean;
import com.officego.commonlib.common.model.RongUserInfoBean;
import com.officego.commonlib.common.model.SearchListBean;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.common.model.owner.AddHouseSuccessBean;
import com.officego.commonlib.common.model.owner.BuildingEditBean;
import com.officego.commonlib.common.model.owner.BuildingJointWorkBean;
import com.officego.commonlib.common.model.owner.HouseBean;
import com.officego.commonlib.common.model.owner.HouseEditBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.common.rpc.request.BuildingJointWorkInterface;
import com.officego.commonlib.common.rpc.request.ChatInterface;
import com.officego.commonlib.common.rpc.request.CouponInterface;
import com.officego.commonlib.common.rpc.request.DirectoryInterface;
import com.officego.commonlib.common.rpc.request.JPushOneKeyLoginInterface;
import com.officego.commonlib.common.rpc.request.LicenceInterface;
import com.officego.commonlib.common.rpc.request.MineMsgInterface;
import com.officego.commonlib.common.rpc.request.ScheduleInterface;
import com.officego.commonlib.common.rpc.request.SearchInterface;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
     * 接口公共参数
     */
    private Map<String, RequestBody> map() {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("imei", requestBody(TextUtils.isEmpty(SpUtils.getImei()) ? "" : SpUtils.getImei()));
        map.put("channel", requestBody("2"));
        return map;
    }
    /**
     * 版本更新
     */
    public void updateVersion(String versioncode, RetrofitCallback<VersionBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("versioncode", requestBody(versioncode));
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("uid", requestBody(targetId));
        map.put("buildingId", requestBody(buildingId == 0 ? "" : String.valueOf(buildingId)));
        map.put("houseId", requestBody(houseId == 0 ? "" : String.valueOf(houseId)));
        map.putAll(map());
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
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .getRongCloudToken(map)
                .enqueue(callback);
    }

    /**
     * 聊天
     */
    public void isChat(int buildingId, int houseId, String targetId, RetrofitCallback<FirstChatBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("uid", requestBody(targetId));
        map.put("buildingId", requestBody(buildingId == 0 ? "" : String.valueOf(buildingId)));
        map.put("houseId", requestBody(houseId == 0 ? "" : String.valueOf(houseId)));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .isChat(map)
                .enqueue(callback);
    }

    /**
     * 判断是否可以交换手机和微信
     */
    public void exchangeContactsVerification(String targetId, RetrofitCallback<ExchangeContactsBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("targetId", requestBody(targetId));
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.put("pageNo", requestBody("1"));
        map.put("pageSize", requestBody("99999"));
        map.putAll(map());
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
    public void getHouseList(int buildingId, int isTemp, int pageNo,
                             RetrofitCallback<HouseBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.put("pageNo", requestBody(pageNo + ""));
        map.put("pageSize", requestBody("10"));
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
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
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .getBuildingEdit(map)
                .enqueue(callback);
    }

    /**
     * 房源编辑
     */
    public void getHouseEdit(int houseId, int isTemp, RetrofitCallback<HouseEditBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("houseId", requestBody(houseId + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .getHouseEdit(map)
                .enqueue(callback);
    }

    /**
     * 楼盘编辑保存上传
     * <p>
     * buildingType 1写字楼 2商务园 3创意园 4共享空间 5公寓  6产业园
     * buildingNum 	是 	string 	楼号
     * districtId 	是 	number 	区域
     * area 	是 	number 	商圈
     * address 	是 	string 	地址
     * totalFloor 	是 	string 	总楼层
     * clearHeight 	是 	String 	净高
     * storeyHeight 	否 	string 	层高
     * ParkingSpaceRent 	否 	string 	车位租金
     * propertyCosts 	是 	string 	物业费用
     * parkingSpace 	是 	string 	车位数量
     * tags 	否 	string 	标签,网点特色,多个用英文逗号隔开
     * airConditioningFee 	是 	string 	空调费用
     * passengerLift 	否 	number 	客梯
     * cargoLift 	否 	number 	货梯
     * |delImgUrl|否|String|英文逗号拼接的需要删除的url|
     * |addImgUrl|是|String|英文逗号拼接的需要添加的url|
     */
    public void buildingEditSave(int buildingId, int isTemp, int buildingType, String buildingNum,
                                 int districtId, int area, String address, String totalFloor,
                                 String completionTime, String refurbishedTime, String constructionArea,
                                 String clearHeight, String storeyHeight, String property, String propertyCosts,
                                 String parkingSpace, String ParkingSpaceRent, String airConditioning, String airConditioningFee,
                                 String passengerLift, String cargoLift, String buildingIntroduction,
                                 String internet, String settlementLicence, String tags, String mainPic, String addImgUrl, String delImgUrl,
                                 RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.put("buildingType", requestBody(buildingType + ""));
        if (buildingType != 1) {
            map.put("buildingNum", requestBody(buildingNum + ""));
        }
        map.put("districtId", requestBody(districtId + ""));
        map.put("businessDistrict", requestBody(area + ""));
        map.put("address", requestBody(address + ""));
        map.put("totalFloor", requestBody(totalFloor + ""));
        map.put("completionTime", requestBody(completionTime + ""));
        map.put("refurbishedTime", requestBody(refurbishedTime + ""));
        map.put("constructionArea", requestBody(constructionArea + ""));
        map.put("clearHeight", requestBody(clearHeight + ""));
        map.put("storeyHeight", requestBody(storeyHeight + ""));
        map.put("property", requestBody(property + ""));
        map.put("propertyCosts", requestBody(propertyCosts + ""));
        map.put("parkingSpace", requestBody(parkingSpace + ""));
        map.put("ParkingSpaceRent", requestBody(ParkingSpaceRent + ""));
        map.put("airConditioning", requestBody(airConditioning + ""));
        map.put("airConditioningFee", requestBody(airConditioningFee + ""));
        map.put("passengerLift", requestBody(passengerLift + ""));
        map.put("cargoLift", requestBody(cargoLift + ""));
        map.put("buildingIntroduction", requestBody(buildingIntroduction + ""));
        //网络
        map.put("internet", requestBody(internet + ""));
        //入住企业
        map.put("settlementLicence", requestBody(settlementLicence + ""));
        //特色
        map.put("tags", requestBody(tags + ""));
        //图片
        map.put("mainPic", requestBody(mainPic + ""));
        map.put("addImgUrl", requestBody(addImgUrl + ""));
        map.put("delImgUrl", requestBody(delImgUrl + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .buildingEditSave(map)
                .enqueue(callback);
    }

    /**
     * 网点编辑保存
     * floorType 网点的单层或多层（网点专属）1单层2多层
     */
    public void jointWorkEditSave(int buildingId, int isTemp, int districtId, int area, String address,
                                  String floorType, String totalFloor, String branchesTotalFloor,
                                  String clearHeight, String airConditioning, String airConditioningFee,
                                  String conferenceNumber, String conferencePeopleNumber, String roomMatching,
                                  String parkingSpace, String ParkingSpaceRent,
                                  String passengerLift, String cargoLift, String buildingIntroduction,
                                  String internet, String settlementLicence, String tags,
                                  String corporateServices, String basicServices,
                                  String mainPic, String addImgUrl, String delImgUrl,
                                  RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.put("districtId", requestBody(districtId + ""));
        map.put("businessDistrict", requestBody(area + ""));
        map.put("address", requestBody(address + ""));
        map.put("floorType", requestBody(floorType + ""));
        map.put("totalFloor", requestBody(totalFloor + ""));
        map.put("branchesTotalFloor", requestBody(branchesTotalFloor + ""));
        map.put("clearHeight", requestBody(clearHeight + ""));
        map.put("airConditioning", requestBody(airConditioning + ""));
        map.put("airConditioningFee", requestBody(airConditioningFee + ""));
        map.put("conferenceNumber", requestBody(conferenceNumber + ""));
        map.put("conferencePeopleNumber", requestBody(conferencePeopleNumber + ""));
        map.put("roomMatching", requestBody(roomMatching + ""));
        map.put("parkingSpace", requestBody(parkingSpace + ""));
        map.put("ParkingSpaceRent", requestBody(ParkingSpaceRent + ""));
        map.put("passengerLift", requestBody(passengerLift + ""));
        map.put("cargoLift", requestBody(cargoLift + ""));
        //网络
        map.put("internet", requestBody(internet + ""));
        //入住企业
        map.put("settlementLicence", requestBody(settlementLicence + ""));
        //介绍
        map.put("buildingIntroduction", requestBody(buildingIntroduction + ""));
        //特色
        map.put("tags", requestBody(tags + ""));
        //服务：公司，基础
        map.put("corporateServices", requestBody(corporateServices + ""));
        map.put("basicServices", requestBody(basicServices + ""));
        //图片
        map.put("mainPic", requestBody(mainPic + ""));
        map.put("addImgUrl", requestBody(addImgUrl + ""));
        map.put("delImgUrl", requestBody(delImgUrl + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .buildingEditSave(map)
                .enqueue(callback);
    }

    /**
     * 楼盘下房源编辑保存
     * dayPrice 	否 	Double 	单价
     * monthPrice 	否 	Double 	总价
     * minimumLease 	否 	String 	最短租期
     * rentFreePeriod 	否 	String 	免租期
     */
    public void houseEditSave(int id, int isTemp, String title, String area,
                              String simple, String dayPrice, String monthPrice,
                              String floor, String clearHeight, String storeyHeight,
                              String minimumLease, String rentFreePeriod,
                              String propertyHouseCosts, String decoration, String unitPatternRemark,
                              String tags, String unitPatternImg,
                              String mainPic, String addImgUrl, String delImgUrl,
                              RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("id", requestBody(id + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.put("title", requestBody(title + ""));
        map.put("area", requestBody(area + ""));
        map.put("simple", requestBody(simple + ""));
        map.put("dayPrice", requestBody(dayPrice + ""));
        map.put("monthPrice", requestBody(monthPrice + ""));
        map.put("floor", requestBody(floor + ""));//第几层
        map.put("clearHeight", requestBody(clearHeight + ""));
        map.put("storeyHeight", requestBody(storeyHeight + ""));
        map.put("minimumLease", requestBody(minimumLease + ""));
        map.put("rentFreePeriod", requestBody(rentFreePeriod + ""));
        map.put("propertyHouseCosts", requestBody(propertyHouseCosts + ""));
        map.put("decoration", requestBody(decoration + ""));
        map.put("unitPatternRemark", requestBody(unitPatternRemark + ""));
        map.put("tags", requestBody(tags + ""));
        //图片
        map.put("unitPatternImg", requestBody(unitPatternImg));
        map.put("mainPic", requestBody(mainPic + ""));
        map.put("addImgUrl", requestBody(addImgUrl + ""));
        map.put("delImgUrl", requestBody(delImgUrl + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .houseEditSave(map)
                .enqueue(callback);
    }


    /**
     * 楼盘下房源添加
     * dayPrice 	否 	Double 	单价
     * monthPrice 	否 	Double 	总价
     * minimumLease 	否 	String 	最短租期
     * rentFreePeriod 	否 	String 	免租期
     */
    public void addBuildingHouse(int buildingId, int isTemp, String title, String area,
                                 String simple, String dayPrice, String monthPrice,
                                 String floor, String clearHeight, String storeyHeight,
                                 String minimumLease, String rentFreePeriod,
                                 String propertyHouseCosts, String decoration, String unitPatternRemark,
                                 String tags, String unitPatternImg,
                                 String mainPic, String addImgUrl, String delImgUrl,
                                 RetrofitCallback<AddHouseSuccessBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.put("title", requestBody(title + ""));
        map.put("area", requestBody(area + ""));
        map.put("simple", requestBody(simple + ""));
        map.put("dayPrice", requestBody(dayPrice + ""));
        map.put("monthPrice", requestBody(monthPrice + ""));
        map.put("floor", requestBody(floor + ""));//第几层
        map.put("clearHeight", requestBody(clearHeight + ""));
        map.put("storeyHeight", requestBody(storeyHeight + ""));
        map.put("minimumLease", requestBody(minimumLease + ""));
        map.put("rentFreePeriod", requestBody(rentFreePeriod + ""));
        map.put("propertyHouseCosts", requestBody(propertyHouseCosts + ""));
        map.put("decoration", requestBody(decoration + ""));
        map.put("unitPatternRemark", requestBody(unitPatternRemark + ""));
        map.put("tags", requestBody(tags + ""));
        //图片
        map.put("unitPatternImg", requestBody(unitPatternImg + ""));
        map.put("mainPic", requestBody(mainPic + ""));
        map.put("addImgUrl", requestBody(addImgUrl + ""));
        map.put("delImgUrl", requestBody(delImgUrl + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .houseAdd(map)
                .enqueue(callback);
    }

    /**
     * 独立办公室编辑保存
     * dayPrice 	否 	Double 	单价
     * monthPrice 	否 	Double 	总价
     * minimumLease 	否 	String 	最短租期
     * rentFreePeriod 	否 	String 	免租期
     */
    public void independentEditSave(int id, int isTemp, String title,
                                    String seats, String area, String monthPrice,
                                    String floor, String minimumLease, String rentFreePeriod,
                                    String conditioningType, String conditioningTypeCost,
                                    String clearHeight, String unitPatternRemark,
                                    String unitPatternImg, String mainPic, String addImgUrl, String delImgUrl,
                                    RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("id", requestBody(id + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.put("title", requestBody(title + ""));
        map.put("seats", requestBody(seats + ""));
        map.put("area", requestBody(area + ""));
        map.put("monthPrice", requestBody(monthPrice + ""));
        map.put("floor", requestBody(floor + ""));//第几层
        map.put("minimumLease", requestBody(minimumLease + ""));
        map.put("rentFreePeriod", requestBody(rentFreePeriod + ""));
        map.put("conditioningType", requestBody(conditioningType + ""));
        map.put("conditioningTypeCost", requestBody(conditioningTypeCost + ""));
        map.put("clearHeight", requestBody(clearHeight + ""));
        //图片
        map.put("unitPatternRemark", requestBody(unitPatternRemark + ""));
        map.put("unitPatternImg", requestBody(unitPatternImg + ""));
        map.put("mainPic", requestBody(mainPic + ""));
        map.put("addImgUrl", requestBody(addImgUrl + ""));
        map.put("delImgUrl", requestBody(delImgUrl + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .houseEditSave(map)
                .enqueue(callback);
    }

    /**
     * 添加独立办公室
     * officeType 办公类型为网点下房源时需要传递的必选参数1是独立办公室，2是开放工位
     */
    public void addIndependentHouse(int buildingId, int isTemp, String title, String seats, String area, String monthPrice,
                                    String floor, String minimumLease, String rentFreePeriod,
                                    String conditioningType, String conditioningTypeCost,
                                    String clearHeight, String unitPatternRemark, String unitPatternImg,
                                    String mainPic, String addImgUrl, String delImgUrl,
                                    RetrofitCallback<AddHouseSuccessBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.put("officeType", requestBody("1"));
        map.put("title", requestBody(title + ""));
        map.put("seats", requestBody(seats + ""));
        map.put("area", requestBody(area + ""));
        map.put("monthPrice", requestBody(monthPrice + ""));
        map.put("floor", requestBody(floor + ""));//第几层
        map.put("minimumLease", requestBody(minimumLease + ""));
        map.put("rentFreePeriod", requestBody(rentFreePeriod + ""));
        map.put("conditioningType", requestBody(conditioningType + ""));
        map.put("conditioningTypeCost", requestBody(conditioningTypeCost + ""));
        map.put("clearHeight", requestBody(clearHeight + ""));
        //图片
        map.put("unitPatternRemark", requestBody(unitPatternRemark + ""));
        map.put("unitPatternImg", requestBody(unitPatternImg + ""));
        map.put("mainPic", requestBody(mainPic + ""));
        map.put("addImgUrl", requestBody(addImgUrl + ""));
        map.put("delImgUrl", requestBody(delImgUrl + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .houseAdd(map)
                .enqueue(callback);
    }


    /**
     * 开放工位编辑保存
     * dayPrice 	否 	Double 	单价
     * monthPrice 	否 	Double 	总价
     * minimumLease 	否 	String 	最短租期
     * rentFreePeriod 	否 	String 	免租期
     */
    public void openSeatsEditSave(int id, int isTemp, String seats, String dayPrice,
                                  String floor, String minimumLease, String rentFreePeriod,
                                  String clearHeight, String mainPic, String addImgUrl, String delImgUrl,
                                  RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("id", requestBody(id + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.put("seats", requestBody(seats + ""));
        map.put("dayPrice", requestBody(dayPrice + ""));
        map.put("floor", requestBody(floor + ""));//第几层
        map.put("minimumLease", requestBody(minimumLease + ""));
        map.put("rentFreePeriod", requestBody(rentFreePeriod + ""));
        map.put("clearHeight", requestBody(clearHeight + ""));
        //图片
        map.put("mainPic", requestBody(mainPic + ""));
        map.put("addImgUrl", requestBody(addImgUrl + ""));
        map.put("delImgUrl", requestBody(delImgUrl + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .houseEditSave(map)
                .enqueue(callback);
    }

    //楼盘保存发布带VR
    public void buildingPublishVr(int buildingId, int isTemp, String vr,
                                  RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId + ""));
        map.put("isTemp", requestBody(isTemp + ""));
        map.put("vr", requestBody(vr + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .buildingPublishVr(map)
                .enqueue(callback);
    }

    //房源保存发布带VR
    public void housePublishVr(int houseId, int isTemp, String vr,
                               RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("houseId", requestBody(houseId + ""));
        //map.put("isTemp", requestBody(isTemp + ""));
        map.put("vr", requestBody(vr + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(BuildingJointWorkInterface.class)
                .housePublishVr(map)
                .enqueue(callback);
    }

    /**
     * 上传多张图片  新认证
     */
    public void uploadImage(int type, List<String> mFilePath, RetrofitCallback<UploadImageBean> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("filedirType", type + "");
        if (mFilePath != null && mFilePath.size() > 0) {
            RequestBody file;
            for (int i = 0; i < mFilePath.size(); i++) {
                file = RequestBody.create(MediaType.parse("image/*"), new File(mFilePath.get(i)));
                builder.addFormDataPart("files", ("files" + type) + i + ".png", file);
            }
        }
        OfficegoRetrofitClient1.getInstance().create(BuildingJointWorkInterface.class)
                .uploadResourcesUrl(builder.build())
                .enqueue(callback);
    }

    /**
     * 上传图片
     */
    public void uploadSingleImageUrl(int type, String mFilePath, RetrofitCallback<UploadImageBean> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("filedirType", type + "");
        if (!TextUtils.isEmpty(mFilePath)) {
            RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(mFilePath));
            builder.addFormDataPart("files", "owner_files.png", file);
            OfficegoRetrofitClient1.getInstance().create(BuildingJointWorkInterface.class)
                    .uploadResourcesUrl(builder.build())
                    .enqueue(callback);
        }
    }

    /**
     * 获取个人信息
     */
    public void getUserMsg(RetrofitCallback<UserMessageBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .getUserMsg(map)
                .enqueue(callback);
    }

    /**
     * 业主更新个人信息
     */
    public void updateUserInfo(String avatar, String nickname, String sex,
                               String job, String wx, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("avatar", requestBody(avatar + ""));
        map.put("nickname", requestBody(nickname));
        map.put("sex", requestBody(sex));
        map.put("job", requestBody(job));
        map.put("wxId", requestBody(wx));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .updateUserInfo(map)
                .enqueue(callback);
    }

    /**
     * 业主更新个人信息卡片
     */
    public void updateUserInfoCard(String avatar, String nickname, String sex,
                                   String job, String wx, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("avatar", requestBody(avatar + ""));
        map.put("nickname", requestBody(nickname));
        map.put("sex", requestBody(sex));
        map.put("job", requestBody(job));
        map.put("wxId", requestBody(wx));
        map.put("isCard", requestBody("1"));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .updateUserInfo(map)
                .enqueue(callback);
    }

    /**
     * 租户更新个人信息
     */
    public void updateUserInfo(String avatar, String nickname, String sex, String wx, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("avatar", requestBody(avatar + ""));
        map.put("nickname", requestBody(nickname));
        map.put("sex", requestBody(sex));
        map.put("wxId", requestBody(wx));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .updateUserInfo(map)
                .enqueue(callback);
    }

    /**
     * 全局搜索
     */
    public void searchList(String keywords, RetrofitCallback<List<SearchListBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("keywords", requestBody(keywords));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(SearchInterface.class)
                .searchList(map)
                .enqueue(callback);
    }

    /**
     * 全局搜索-认证
     */
    public void searchList2(String keywords, RetrofitCallback<List<SearchListBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("keywords", requestBody(keywords));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(SearchInterface.class)
                .searchList2(map)
                .enqueue(callback);
    }

    /**
     * 卡券列表
     * status 	是 	string 	1可使用2已过期
     * pageSize 	否 	int 	条数
     * pageNo 	否 	int 	页数
     */
    public void getCouponList(int status, RetrofitCallback<CouponListBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("status", requestBody(status + ""));
        map.put("pageSize", requestBody("99999"));
        map.put("pageNo", requestBody("1"));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(CouponInterface.class)
                .getCouponList(map)
                .enqueue(callback);
    }

    /**
     * 卡券详情
     */
    public void getCouponDetails(String batchCode, RetrofitCallback<CouponDetailsBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("batchCode", requestBody(batchCode));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(CouponInterface.class)
                .getCouponCodeDetails(map)
                .enqueue(callback);
    }

    /**
     * 卡券核销
     * id 	是 	int 	劵id
     * roomId 	是 	int 	会议室id
     */
    public void couponSureWriteOff(int couponId, int roomId, RetrofitCallback<CouponWriteOffBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("id", requestBody(couponId + ""));
        map.put("roomId", requestBody(roomId + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(CouponInterface.class)
                .sureWriteOffCoupon(map)
                .enqueue(callback);
    }

    /**
     * 卡券核销列表
     */
    public void couponSureWriteOffList(int pageNo, RetrofitCallback<CouponWriteOffListBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("pageSize", requestBody("10"));
        map.put("pageNo", requestBody(pageNo + ""));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(CouponInterface.class)
                .getCouponUserList(map)
                .enqueue(callback);
    }

    /**
     * 极光一键登录
     */
    public void jPushLogin(String loginToken, RetrofitCallback<JPushLoginBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("loginToken", requestBody(loginToken));
        map.putAll(map());
        OfficegoRetrofitClient.getInstance().create(JPushOneKeyLoginInterface.class)
                .getJPushPhone(map)
                .enqueue(callback);
    }
}

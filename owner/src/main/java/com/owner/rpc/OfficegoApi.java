package com.owner.rpc;

import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.QueryApplyLicenceBean;
import com.officego.commonlib.common.rpc.request.LicenceInterface;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.identity.model.ApplyJoinBean;
import com.owner.identity.model.ApplyLicenceBean;
import com.owner.identity.model.BusinessCircleBean;
import com.owner.identity.model.CheckIdentityBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.identity.model.IdentityJointWorkBean;
import com.owner.mine.model.AvatarBean;
import com.owner.mine.model.UserOwnerBean;
import com.owner.rpc.request.IdentitySearchInterface;
import com.owner.rpc.request.LoginInterface;
import com.owner.rpc.request.MineMsgInterface;
import com.owner.rpc.request.ScheduleInterface;
import com.owner.schedule.model.ViewingDateBean;

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
     * 登录
     *
     * @param phone 是	string	手机号
     */
    public void getSmsCode(String phone, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("phone", requestBody(phone));
        OfficegoRetrofitClient.getInstance().create(LoginInterface.class)
                .getSmsCode(map)
                .enqueue(callback);
    }

    //********************************************************************************
    //个人信息***************************************************************************
    //********************************************************************************
    public void getUserMsg(RetrofitCallback<UserOwnerBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .getUserMsg(map)
                .enqueue(callback);
    }

    /**
     * realname 	否 	string 	真实姓名
     * file 	否 	file 	头像
     * sex 	否 	string 	性别
     * company 	否 	string 	公司名称
     * job 	否 	string 	职位
     * token
     */
    public void updateUserData(String realName, String sex, String company, String job, String wx, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("realname", requestBody(realName));
        map.put("sex", requestBody(sex));
        map.put("company", requestBody(company));
        map.put("job", requestBody(job));
        map.put("WX", requestBody(wx));
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .updateUserData(map)
                .enqueue(callback);
    }


    /**
     * 更新头像
     */
    public void updateAvatar(File avatar, RetrofitCallback<AvatarBean> callback) {
        RequestBody file = RequestBody.create(MediaType.parse("image/*"), avatar);

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", "avatar_owner_officego.png", file)
                .addFormDataPart("token", SpUtils.getSignToken());
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .updateUserAvatar(builder.build())
                .enqueue(callback);
    }

//    /**
//     * 添加微信
//     * wxId 	是 	String 	微信号
//     * channel 	是 	int 	终端渠道,1:IOS,2:安卓,3:H5
//     * token 	是 	String 	token (登录接口返回)
//     */
//    public void bindWechat(String wxId, RetrofitCallback<Object> callback) {
//        Map<String, RequestBody> map = new HashMap<>();
//        map.put("token", requestBody(SpUtils.getSignToken()));
//        map.put("wxId", requestBody(wxId));
//        map.put("channel", requestBody("2"));
//        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
//                .bindWechat(map)
//                .enqueue(callback);
//    }

    /**
     * 修改手机号
     */
    public void modifyMobile(String mobile, String code, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("newPhone", requestBody(mobile));
        map.put("code", requestBody(code));
        map.put("channel", requestBody("2"));
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .modifyMobile(map)
                .enqueue(callback);
    }

//    /**
//     * 版本更新
//     *
//     * @param versioncode
//     * @param callback
//     */
//    public void updateVersion(String versioncode, RetrofitCallback<VersionBean> callback) {
//        Map<String, RequestBody> map = new HashMap<>();
//        map.put("token", requestBody(SpUtils.getSignToken()));
//        map.put("versioncode", requestBody(versioncode));
//        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
//                .updateVersion(map)
//                .enqueue(callback);
//    }

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
     * 预约看房行程
     * startTime 	是 	int 	开始时间
     * endTime 	是 	int
     */
    public void getScheduleList(long startTime, long endTime, RetrofitCallback<List<ViewingDateBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("startTime", requestBody(startTime + ""));
        map.put("endTime", requestBody(endTime + ""));
        OfficegoRetrofitClient.getInstance().create(ScheduleInterface.class)
                .getScheduleList(map)
                .enqueue(callback);
    }

    /**
     * 看房记录
     * startTime 	是 	int 	开始时间
     * endTime 	是 	int
     */
    public void getOldScheduleList(long startTime, long endTime, RetrofitCallback<List<ViewingDateBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("startTime", requestBody(startTime + ""));
        map.put("endTime", requestBody(endTime + ""));
        OfficegoRetrofitClient.getInstance().create(ScheduleInterface.class)
                .getOldScheduleList(map)
                .enqueue(callback);
    }


    /**
     * 切换身份
     * 用户身份标：0租户，1户主
     */
    public void switchId(String roleType,
                         RetrofitCallback<LoginBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("roleType", requestBody(roleType));
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .switchId(map)
                .enqueue(callback);
    }


    /**
     * ***********************************************
     * 认证搜索
     */
    public void searchCompany(String keywords,
                              RetrofitCallback<List<IdentityCompanyBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("keywords", requestBody(keywords));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .searchCompany(map)
                .enqueue(callback);
    }

    /**
     * 搜索楼盘
     */
    public void searchListBuild(String keywords,
                                RetrofitCallback<List<IdentityBuildingBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("keywords", requestBody(keywords));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .searchListBuild(map)
                .enqueue(callback);
    }

    /**
     * 搜索网点
     */
    public void searchListBranch(String keywords,
                                 RetrofitCallback<List<IdentityJointWorkBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("keywords", requestBody(keywords));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .searchListBranch(map)
                .enqueue(callback);
    }

    /**
     * 认证公司，网点id信息
     */
    public void selectApplyLicence(int identityType, int id,
                                   RetrofitCallback<ApplyLicenceBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("identityType", requestBody(identityType + ""));
        map.put("id", requestBody(id + ""));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .selectApplyLicence(map)
                .enqueue(callback);
    }

    /**
     * 认证公司，网点申请加入
     */
    public void applyLicenceProprietor(int identityType, int id, int chattedId,
                                       RetrofitCallback<ApplyJoinBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("identityType", requestBody(identityType + ""));
        map.put("id", requestBody(id + ""));
        map.put("chattedId", requestBody(chattedId + ""));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .applyLicenceProprietorApp(map)
                .enqueue(callback);
    }

    /**
     * 商圈
     *
     * @param callback
     */
    public void getDistrictList(RetrofitCallback<List<BusinessCircleBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("city", requestBody("上海市"));
        map.put("type", requestBody("1")); //1：全部，0：系统已有楼盘的地铁
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .getDistrictList(map)
                .enqueue(callback);
    }

    /**
     * 认证公司，网点撤销
     */
    public void deleteUserLicence(int id, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("id", requestBody(id + ""));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .deleteUserLicenceApp(map)
                .enqueue(callback);
    }

    /**
     * 认证查询申请信息接口
     */
    public void queryApplyLicenceProprietor(RetrofitCallback<QueryApplyLicenceBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        OfficegoRetrofitClient.getInstance().create(LicenceInterface.class)
                .queryApplyLicenceProprietor(map)
                .enqueue(callback);
    }

    /**
     * 企业名称创建校验接口
     */
    public void checkLicenceByCompany(int identityType, String company, RetrofitCallback<CheckIdentityBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("identityType", requestBody(identityType + ""));
        map.put("company", requestBody(company));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .checkLicenceByCompany(map)
                .enqueue(callback);
    }

    /**
     * 楼盘名称创建校验接口
     */
    public void checkBuildingByName(int identityType, String name, RetrofitCallback<CheckIdentityBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("identityType", requestBody(identityType + ""));
        map.put("name", requestBody(name));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .checkBuildingByName(map)
                .enqueue(callback);
    }

    /**
     * 获取认证信息
     */
    public void getIdentityInfo(int identityType, RetrofitCallback<CheckIdentityBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("identityType", requestBody(identityType + ""));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .getLicenceProprietorInfo(map)
                .enqueue(callback);
    }


    /**
     * @param createCompany 1提交认证2企业确认3楼盘、网点确认
     * @param identityType  身份类型0个人1企业2联合
     * @param leaseType     租赁类型0直租1转租
     * @param callback
     */
    public void submitIdentityInfo(int createCompany, int identityType, int leaseType,
                                   List<String> mStrPath, RetrofitCallback<Object> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("createCompany", createCompany + "");
        builder.addFormDataPart("identityType", identityType + "");
        builder.addFormDataPart("leaseType", leaseType + "");

//        builder.addFormDataPart("licenceId", "21");
//        builder.addFormDataPart("userLicenceId", "68");
//        builder.addFormDataPart("buildingName", "未来大楼");
//        builder.addFormDataPart("buildingId", "4626");
        RequestBody file;
        for (int i = 0; i < mStrPath.size(); i++) {
            file = RequestBody.create(MediaType.parse("image/*"), new File(mStrPath.get(i)));
            builder.addFormDataPart("filePremisesPermit", "filePremisesPermit" + i + ".png", file);
        }
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .submitIdentityInfo(builder.build())
                .enqueue(callback);
    }


    /**
     * createCompany 1提交认证2企业确认3楼盘、网点确认
     *
     * @param identityType 身份类型0个人1企业2联合
     * @param callback
     */
    public void submitIdentityCreateCompany(int createCompany, int identityType, String company, String address,
                                            String creditNo,
                                            String mStrPath, RetrofitCallback<Object> callback) {
        RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(mStrPath));
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("createCompany", createCompany + "");
        builder.addFormDataPart("identityType", identityType + "");
        builder.addFormDataPart("company", company);
        builder.addFormDataPart("address", address);
        builder.addFormDataPart("creditNo", creditNo);
        builder.addFormDataPart("fileBusinessLicense", "fileBusinessLicense.png", file);
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .submitIdentityInfo(builder.build())
                .enqueue(callback);
    }

    /**
     * 创建楼盘
     * createCompany 1提交认证2企业确认3楼盘、网点确认
     *
     * @param identityType 身份类型0个人1企业2联合
     * @param callback
     */
    public void submitIdentityCreateBuilding(int createCompany, int identityType, String buildingName, String address,
                                             int district, int business, String mPath, RetrofitCallback<Object> callback) {
        RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(mPath));
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("createCompany", createCompany + "");
        builder.addFormDataPart("identityType", identityType + "");
        builder.addFormDataPart("buildingName", buildingName);
        builder.addFormDataPart("buildingAddress", address);
        builder.addFormDataPart("district", district + "");
        builder.addFormDataPart("business", business + "");
        builder.addFormDataPart("fileMainPic", "fileMainPic.png", file);
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .submitIdentityInfo(builder.build())
                .enqueue(callback);
    }

    /**
     * 创建网点
     * createCompany 1提交认证2企业确认3楼盘、网点确认
     *
     * @param identityType 身份类型0个人1企业2联合
     * @param callback
     */
    public void submitIdentityCreateJointWork(int createCompany, int identityType, String branchesName, String address,
                                             int district, int business, String mPath, RetrofitCallback<Object> callback) {
        RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(mPath));
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("createCompany", createCompany + "");
        builder.addFormDataPart("identityType", identityType + "");
        builder.addFormDataPart("branchesName", branchesName);
//        builder.addFormDataPart("buildingAddress", address);
        builder.addFormDataPart("district", district + "");
        builder.addFormDataPart("business", business + "");
        builder.addFormDataPart("fileMainPic", "fileMainPic.png", file);
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .submitIdentityInfo(builder.build())
                .enqueue(callback);
    }
}

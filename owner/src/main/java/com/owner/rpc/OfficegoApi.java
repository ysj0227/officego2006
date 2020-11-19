package com.owner.rpc;

import android.text.TextUtils;

import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.QueryApplyLicenceBean;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.common.rpc.request.BuildingJointWorkInterface;
import com.officego.commonlib.common.rpc.request.LicenceInterface;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.identity.model.ApplyJoinBean;
import com.owner.identity.model.ApplyLicenceBean;
import com.owner.identity.model.BusinessCircleBean;
import com.owner.identity.model.CheckIdentityBean;
import com.owner.identity.model.GetIdentityInfoBean;
import com.owner.identity.model.IdentityBuildingBean;
import com.owner.identity.model.IdentityCompanyBean;
import com.owner.identity.model.IdentityJointWorkBean;
import com.owner.identity.model.ImageBean;
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

import static com.officego.commonlib.utils.CommonHelper.checkObjAllFieldsIsNull;

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
        map.put("channel", requestBody("2"));
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
     * 搜索网点
     */
    public void searchListBranch2(String keywords,
                                  RetrofitCallback<List<IdentityBuildingBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("keywords", requestBody(keywords));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .searchListBranch2(map)
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
    public void applyLicenceProprietor(int identityType, int id, String chattedId,
                                       RetrofitCallback<ApplyJoinBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("identityType", requestBody(identityType + ""));
        map.put("id", requestBody(id + ""));
        map.put("chattedId", requestBody(chattedId));
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
//        map.put("city", requestBody("上海市"));
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
    public void getIdentityInfo(int identityType, RetrofitCallback<GetIdentityInfoBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("identityType", requestBody(identityType + ""));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .getLicenceProprietorInfo(map)
                .enqueue(callback);
    }


    /**
     * 公司提交
     *
     * @param createCompany 1提交认证2企业确认3楼盘、网点确认
     * @param identityType  身份类型0个人1企业2联合
     * @param leaseType     租赁类型0直租1转租
     * @param callback      String licenceId, String userLicenceId, String buildingId, String buildingTempId,
     */
    public void submitCompanyIdentityInfo(GetIdentityInfoBean data, int createCompany, int identityType, int leaseType,
                                          boolean isSelectedBuilding, String buildingId, String buildingName, String buildingAddress,
                                          List<ImageBean> mFilePremisesPath, List<ImageBean> mFileContractPath, RetrofitCallback<Object> callback) {
        if (data == null) {
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("createCompany", createCompany + "");
        builder.addFormDataPart("identityType", identityType + "");
        builder.addFormDataPart("leaseType", leaseType + "");
        builder.addFormDataPart("licenceId", data.getLicenceId());//企业id
        builder.addFormDataPart("userLicenceId", data.getUserLicenceId());//企业关系id
        if (isSelectedBuilding) {//关联的
            builder.addFormDataPart("buildingId", buildingId);//关联楼盘的id。- 覆盖
        } else {
            builder.addFormDataPart("buildingId", data.getBuildingId());//创建返回的楼盘id
        }
        builder.addFormDataPart("buildingName", buildingName);//关联楼盘名称
        builder.addFormDataPart("buildingAddress", buildingAddress);//关联楼盘地址
        builder.addFormDataPart("buildingTempId", data.getBuildingTempId());//关联楼id  接口给
        //房产证
        if (mFilePremisesPath != null && mFilePremisesPath.size() > 0) {
            RequestBody file;
            boolean isNetImg;
            for (int i = 0; i < mFilePremisesPath.size(); i++) {
                isNetImg = mFilePremisesPath.get(i).isNetImage();
                if (!isNetImg && i < mFilePremisesPath.size() - 1) {
                    file = RequestBody.create(MediaType.parse("image/*"), new File(mFilePremisesPath.get(i).getPath()));
                    builder.addFormDataPart("filePremisesPermit", "filePremisesPermit" + i + ".png", file);
                }
            }
        }
        //租赁合同
        if (leaseType == 1 && mFileContractPath != null && mFileContractPath.size() > 0) {
            RequestBody file1;
            boolean isNetImg;
            for (int i = 0; i < mFileContractPath.size(); i++) {
                isNetImg = mFileContractPath.get(i).isNetImage();
                if (!isNetImg && i < mFileContractPath.size() - 1) {
                    file1 = RequestBody.create(MediaType.parse("image/*"), new File(mFileContractPath.get(i).getPath()));
                    builder.addFormDataPart("fileContract", "fileContract" + i + ".png", file1);
                }
            }
        }
        OfficegoRetrofitClient1.getInstance().create(IdentitySearchInterface.class)
                .submitIdentityInfo(builder.build())
                .enqueue(callback);
    }

    /**
     * 共享办公提交
     *
     * @param createCompany 1提交认证2企业确认3楼盘、网点确认
     * @param identityType  身份类型0个人1企业2联合
     * @param leaseType     租赁类型0直租1转租
     * @param callback
     */
    public void submitJointWorkIdentityInfo(GetIdentityInfoBean data, int createCompany, int identityType, int leaseType, String buildingName,
                                            List<ImageBean> mFilePremisesPath, List<ImageBean> mFileContractPath, RetrofitCallback<Object> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("createCompany", createCompany + "");
        builder.addFormDataPart("identityType", identityType + "");
        builder.addFormDataPart("leaseType", leaseType + "");
        if (data != null && !checkObjAllFieldsIsNull(data)) {
            builder.addFormDataPart("licenceId", data.getLicenceId());//企业id(如果是创建)//联办公司必须创建，目前无关联
            builder.addFormDataPart("userLicenceId", data.getUserLicenceId());//企业关系id
            builder.addFormDataPart("buildingId", data.getBuildingId());//创建返回的网点id
            builder.addFormDataPart("buildingTempId", data.getBuildingTempId());//关联网点id  接口给
        }
        builder.addFormDataPart("buildingName", buildingName);  //底部楼盘名字
        //房产证
        if (mFilePremisesPath != null && mFilePremisesPath.size() > 0) {
            RequestBody file;
            boolean isNetImg;
            for (int i = 0; i < mFilePremisesPath.size(); i++) {
                isNetImg = mFilePremisesPath.get(i).isNetImage();
                if (!isNetImg && i < mFilePremisesPath.size() - 1) {
                    file = RequestBody.create(MediaType.parse("image/*"), new File(mFilePremisesPath.get(i).getPath()));
                    builder.addFormDataPart("filePremisesPermit", "filePremisesPermit" + i + ".png", file);
                }
            }
        }
        //租赁合同
        if (mFileContractPath != null && mFileContractPath.size() > 0) {
            RequestBody file1;
            boolean isNetImg;//是否网络图片
            for (int i = 0; i < mFileContractPath.size(); i++) {
                isNetImg = mFileContractPath.get(i).isNetImage();
                if (!isNetImg && i < mFileContractPath.size() - 1) {
                    file1 = RequestBody.create(MediaType.parse("image/*"), new File(mFileContractPath.get(i).getPath()));
                    builder.addFormDataPart("fileContract", "fileContract" + i + ".png", file1);
                }
            }
        }
        OfficegoRetrofitClient1.getInstance().create(IdentitySearchInterface.class)
                .submitIdentityInfo(builder.build())
                .enqueue(callback);
    }

    /**
     * 个人提交
     *
     * @param createCompany 1提交认证2企业确认3楼盘、网点确认
     * @param identityType  身份类型0个人1企业2联合
     * @param leaseType     租赁类型0直租1转租
     * @param callback
     */
    public void submitPersonalIdentityInfo(GetIdentityInfoBean data, int createCompany, int identityType, int leaseType,
                                           boolean isSelectedBuilding, String buildingId, String buildingName, String buildingAddress,
                                           String userName, String idCard, String isCardFrontPath, String isCardBackPath,
                                           List<ImageBean> mFilePremisesPath, List<ImageBean> mFileContractPath,
                                           RetrofitCallback<Object> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("createCompany", createCompany + "");
        builder.addFormDataPart("identityType", identityType + "");
        builder.addFormDataPart("leaseType", leaseType + "");
        if (data != null && !checkObjAllFieldsIsNull(data)) {
            builder.addFormDataPart("licenceId", data.getLicenceId());//企业id
            builder.addFormDataPart("userLicenceId", data.getUserLicenceId());//企业关系id
            builder.addFormDataPart("buildingTempId", data.getBuildingTempId());//关联楼id  接口给
            if (!isSelectedBuilding) {
                builder.addFormDataPart("buildingId", data.getBuildingId());//创建返回的楼盘id
            }
        }
        if (isSelectedBuilding) {//关联的
            builder.addFormDataPart("buildingId", buildingId);//关联楼盘的id。- 覆盖
        }
        builder.addFormDataPart("buildingName", buildingName);//关联楼盘名称
        builder.addFormDataPart("buildingAddress", buildingAddress);//关联楼盘地址
        builder.addFormDataPart("userName", userName); //姓名
        builder.addFormDataPart("idCard", idCard);//身份证号
        //身份证正面
        if (!TextUtils.isEmpty(isCardFrontPath)) {
            RequestBody fileIdFront = RequestBody.create(MediaType.parse("image/*"), new File(isCardFrontPath));
            builder.addFormDataPart("fileIdFront", "fileIdFront.png", fileIdFront);
        }
        //身份证背面
        if (!TextUtils.isEmpty(isCardBackPath)) {
            RequestBody fileIdBack = RequestBody.create(MediaType.parse("image/*"), new File(isCardBackPath));
            builder.addFormDataPart("fileIdBack", "fileIdBack.png", fileIdBack);
        }
        //房产证
        if (mFilePremisesPath != null && mFilePremisesPath.size() > 0) {
            RequestBody file;
            boolean isNetImg;
            for (int i = 0; i < mFilePremisesPath.size(); i++) {
                isNetImg = mFilePremisesPath.get(i).isNetImage();
                if (!isNetImg && i < mFilePremisesPath.size() - 1) {
                    file = RequestBody.create(MediaType.parse("image/*"), new File(mFilePremisesPath.get(i).getPath()));
                    builder.addFormDataPart("filePremisesPermit", "filePremisesPermit" + i + ".png", file);
                }
            }
        }
        //租赁合同
        if (leaseType == 1 && mFileContractPath != null && mFileContractPath.size() > 0) {
            RequestBody file1;
            boolean isNetImg;
            for (int i = 0; i < mFileContractPath.size(); i++) {
                isNetImg = mFileContractPath.get(i).isNetImage();
                if (!isNetImg && i < mFileContractPath.size() - 1) {
                    file1 = RequestBody.create(MediaType.parse("image/*"), new File(mFileContractPath.get(i).getPath()));
                    builder.addFormDataPart("fileContract", "fileContract" + i + ".png", file1);
                }
            }
        }
        OfficegoRetrofitClient1.getInstance().create(IdentitySearchInterface.class)
                .submitIdentityInfo(builder.build())
                .enqueue(callback);
    }

    /**
     * **********************************************
     * createCompany 1提交认证2企业确认3楼盘、网点确认
     *
     * @param identityType 身份类型0个人1企业2联合
     */
    public void submitIdentityCreateCompany(GetIdentityInfoBean data, int createCompany, int identityType,
                                            String company, String address, String creditNo,
                                            String mStrPath, RetrofitCallback<Object> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("createCompany", createCompany + "");
        builder.addFormDataPart("identityType", identityType + "");
        builder.addFormDataPart("company", company);
        builder.addFormDataPart("address", address);
        builder.addFormDataPart("creditNo", creditNo);
        if (data != null && !checkObjAllFieldsIsNull(data)) {
            builder.addFormDataPart("licenceId", data.getLicenceId());//企业id
            builder.addFormDataPart("userLicenceId", data.getUserLicenceId());//企业关系id
            builder.addFormDataPart("buildingId", data.getBuildingId());//创建返回的楼盘id
            builder.addFormDataPart("buildingTempId", data.getBuildingTempId());//关联楼id  接口给
        }
        if (!TextUtils.isEmpty(mStrPath)) {
            RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(mStrPath));
            builder.addFormDataPart("fileBusinessLicense", "fileBusinessLicense.png", file);
        }
        OfficegoRetrofitClient1.getInstance().create(IdentitySearchInterface.class)
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
    public void submitIdentityCreateBuilding(GetIdentityInfoBean data, int createCompany, int identityType, String buildingName, String address,
                                             int district, int business, String mPath, RetrofitCallback<Object> callback) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("createCompany", createCompany + "");
        builder.addFormDataPart("identityType", identityType + "");
        builder.addFormDataPart("buildingName", buildingName);
        builder.addFormDataPart("buildingAddress", address);
        builder.addFormDataPart("district", district + "");
        builder.addFormDataPart("business", business + "");
        if (data != null && !checkObjAllFieldsIsNull(data)) {
            builder.addFormDataPart("licenceId", data.getLicenceId());//企业id
            builder.addFormDataPart("userLicenceId", data.getUserLicenceId());//企业关系id
            builder.addFormDataPart("buildingId", data.getBuildingId());//创建返回的楼盘id
            builder.addFormDataPart("buildingTempId", data.getBuildingTempId());//关联楼id  接口给
        }
        if (!TextUtils.isEmpty(mPath)) {
            RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(mPath));
            builder.addFormDataPart("fileMainPic", "fileMainPic.png", file);
        }
        OfficegoRetrofitClient1.getInstance().create(IdentitySearchInterface.class)
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
    public void submitIdentityCreateJointWork(GetIdentityInfoBean data, int createCompany, int identityType, String branchesName, String address,
                                              int district, int business, String mPath, RetrofitCallback<Object> callback) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("createCompany", createCompany + "");
        builder.addFormDataPart("identityType", identityType + "");
        builder.addFormDataPart("branchesName", branchesName);
        builder.addFormDataPart("buildingAddress", address);
        builder.addFormDataPart("district", district + "");
        builder.addFormDataPart("business", business + "");
        if (data != null && !checkObjAllFieldsIsNull(data)) {
            builder.addFormDataPart("licenceId", data.getLicenceId());//企业id
            builder.addFormDataPart("userLicenceId", data.getUserLicenceId());//企业关系id
            builder.addFormDataPart("buildingId", data.getBuildingId());//创建返回的楼盘id
            builder.addFormDataPart("buildingTempId", data.getBuildingTempId());//关联楼id  接口给
        }
        if (!TextUtils.isEmpty(mPath)) {
            RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(mPath));
            builder.addFormDataPart("fileMainPic", "fileMainPic.png", file);
        }
        OfficegoRetrofitClient1.getInstance().create(IdentitySearchInterface.class)
                .submitIdentityInfo(builder.build())
                .enqueue(callback);
    }

    /**
     * 删除网络图片
     */
    public void deleteImage(int id, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("id", requestBody(id + ""));
        OfficegoRetrofitClient.getInstance().create(IdentitySearchInterface.class)
                .deleteImage(map)
                .enqueue(callback);
    }

    /**
     * app扫码web登录
     */
    public void scanWebLogin(String content, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("uid", requestBody(SpUtils.getUserId()));
        map.put("token", requestBody(content));
        OfficegoRetrofitClientWeb.getInstance().create(LoginInterface.class)
                .scanLogin(map)
                .enqueue(callback);
    }

    /**
     * 上传多张图片
     */
    public void uploadImageUrl(int type, List<ImageBean> mFilePath, RetrofitCallback<UploadImageBean> callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", SpUtils.getSignToken());
        builder.addFormDataPart("filedirType", type + "");
        if (mFilePath != null && mFilePath.size() > 0) {
            RequestBody file;
            boolean isNetImg;
            for (int i = 0; i < mFilePath.size(); i++) {
                isNetImg = mFilePath.get(i).isNetImage();
                if (!isNetImg && i < mFilePath.size() - 1) {
                    file = RequestBody.create(MediaType.parse("image/*"), new File(mFilePath.get(i).getPath()));
                    builder.addFormDataPart("files", "owner_files" + i + ".png", file);
                }
            }
        }
        OfficegoRetrofitClient1.getInstance().create(BuildingJointWorkInterface.class)
                .uploadResourcesUrl(builder.build())
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
                builder.addFormDataPart("files", ("files"+type) + i + ".png", file);
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

}

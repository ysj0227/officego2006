package com.officego.rpc;

import android.content.Context;
import android.text.TextUtils;

import com.officego.commonlib.common.LoginBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.ChatHouseBean;
import com.officego.commonlib.common.model.RenterBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.DateTimeUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.request.BannerInterface;
import com.officego.rpc.request.ChatInterface;
import com.officego.rpc.request.FavoriteInterface;
import com.officego.rpc.request.FindInterface;
import com.officego.rpc.request.HomeInterface;
import com.officego.rpc.request.LoginInterface;
import com.officego.rpc.request.MineMsgInterface;
import com.officego.rpc.request.ScheduleInterface;
import com.officego.rpc.request.SearchAreaInterface;
import com.officego.rpc.request.SearchInterface;
import com.officego.ui.collect.model.CollectBuildingBean;
import com.officego.ui.collect.model.CollectHouseBean;
import com.officego.ui.find.model.DirectoryBean;
import com.officego.ui.home.model.BannerBean;
import com.officego.ui.home.model.BuildingBean;
import com.officego.ui.home.model.BuildingDetailsBean;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.ui.home.model.BuildingJointWorkBean;
import com.officego.ui.home.model.BusinessCircleBean;
import com.officego.ui.home.model.ChatsBean;
import com.officego.ui.home.model.HouseOfficeDetailsBean;
import com.officego.ui.home.model.HouseOfficeDetailsJointWorkBean;
import com.officego.ui.home.model.MeterBean;
import com.officego.ui.home.model.QueryHistoryKeywordsBean;
import com.officego.ui.home.model.SearchListBean;
import com.officego.ui.mine.model.AvatarBean;
import com.officego.ui.mine.model.UserBean;
import com.officego.ui.mine.model.ViewingDateBean;
import com.officego.ui.mine.model.ViewingDateDetailsBean;

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

    /**
     * 登录
     *
     * @param mobile 是	string	手机号
     * @param code   是	string	code
     */
    public void login(Context context, String mobile, String code, RetrofitCallback<LoginBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("phone", requestBody(mobile));
        map.put("code", requestBody(code));
        map.put("imei", requestBody(CommonHelper.getIMEI(context)));
        map.put("channel", requestBody("2"));
        map.put("idType", requestBody(SpUtils.getRole()));
        OfficegoRetrofitClient.getInstance().create(LoginInterface.class)
                .login(map)
                .enqueue(callback);
    }

    /**
     * 手机免密登录
     *
     * @param mobile
     * @param callback
     */
    public void loginOnlyPhone(Context context, String mobile, RetrofitCallback<LoginBean> callback) {
        LogCat.e(TAG, "111111111111 role=" + SpUtils.getRole());
        Map<String, RequestBody> map = new HashMap<>();
        map.put("phone", requestBody(mobile));
        map.put("imei", requestBody(CommonHelper.getIMEI(context)));
        map.put("channel", requestBody("2"));
        map.put("idType", requestBody(SpUtils.getRole()));
        OfficegoRetrofitClient.getInstance().create(LoginInterface.class)
                .loginOnlyPhone(map)
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
        OfficegoRetrofitClient.getInstance().create(FindInterface.class)
                .getHouseUnique(map)
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
        OfficegoRetrofitClient.getInstance().create(FindInterface.class)
                .getDecoratedType(map)
                .enqueue(callback);
    }

    /**
     * 热门
     *
     * @param callback
     */
    public void getHotKeywords(RetrofitCallback<List<DirectoryBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("code", requestBody("hotKeywords"));
        OfficegoRetrofitClient.getInstance().create(FindInterface.class)
                .getDecoratedType(map)
                .enqueue(callback);
    }

    /**
     * 我想找
     * token 	是 	string 	根据token解析用户id
     * btype 	是 	int 	类型,1:楼盘 写字楼,2:网点 共享办公
     * area 	是 	String 	建筑面积 范围值,逗号分隔
     * dayPrice 	是 	String 	租金 范围值,逗号分隔
     * seats 	是 	String 	工位数范围,逗号分隔
     * decoration 	是 	String 	装修类型 字典数据
     * houseTags 	是 	String 	房源特色,字典数据
     */


    public void wantToFind(String btype, String constructionArea, String rentPrice,
                           String seats, String decoration, String houseTags,
                           RetrofitCallback<Object> callback) {
        LogCat.e(TAG, "Token=" + SpUtils.getSignToken());
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("btype", requestBody(btype)); //1:楼盘 写字楼,2:网点 共享办公
        map.put("area", requestBody(constructionArea));
        map.put("dayPrice", requestBody(rentPrice));
        map.put("seats", requestBody(seats));
        map.put("decoration", requestBody(decoration));
        map.put("houseTags", requestBody(houseTags));
        OfficegoRetrofitClient.getInstance().create(FindInterface.class)
                .wantToFind(map)
                .enqueue(callback);
    }

    /**
     * 轮播图
     *
     * @param callback
     */
    public void getBannerList(RetrofitCallback<List<BannerBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("type", requestBody("3"));
        OfficegoRetrofitClient.getInstance().create(BannerInterface.class)
                .getBannerList(map)
                .enqueue(callback);
    }

    /**
     * 地铁
     *
     * @param callback
     */
    public void getSubwayList(RetrofitCallback<List<MeterBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
//        map.put("city", requestBody("上海市"));
        map.put("type", requestBody("1")); //1：全部，0：系统已有楼盘的地铁
        OfficegoRetrofitClient.getInstance().create(SearchAreaInterface.class)
                .getSubwayList(map)
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
        OfficegoRetrofitClient.getInstance().create(SearchAreaInterface.class)
                .getDistrictList(map)
                .enqueue(callback);
    }

    /**
     * 首页列表
     * btype 	否 	string 	类型1:楼盘,2:网点, 0全部
     * district 	否 	string 	大区
     * business 	否 	string 	商圈 不限：0 多个英文逗号分隔Id
     * nearbySubway 	否 	string 	地铁站名 ，不限：0 多个英文逗号分隔Id
     * line 	否 	string 	地铁线
     * area 	否 	string 	平方米区间英文逗号分隔
     * dayPrice 	否 	string 	楼盘的时候是 每平方米单价区间英文逗号分隔 网点的时候是 每工位每月单价区间英文逗号分隔
     * decoration 	否 	string 	装修类型id英文逗号分隔
     * houseTags 	否 	string 	房源特色id英文逗号分隔
     * vrFlag 	否 	int 	是否只看VR房源 0:不限1:只看VR房源
     * sort 	否 	int 	排序0默认1价格从高到低2价格从低到高3面积从大到小4面积从小到大
     * seats 	否 	string 	联合工位区间英文逗号分隔
     * longitude 	否 	string 	经度
     * latitude 	否 	string 	纬度
     * keyWord 	否
     */
    public void getBuildingList(int pageNo, String btype, String district, String business, String line,
                                String nearbySubway, String area, String dayPrice, String seats, String decoration,
                                String houseTags, String sort, String keyWord,
                                RetrofitCallback<BuildingBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
//        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("token", requestBody(""));
        map.put("btype", requestBody(btype));
        map.put("district", requestBody(district));
        map.put("business", requestBody(business));
        map.put("line", requestBody(line));
        map.put("nearbySubway", requestBody(nearbySubway));
        map.put("area", requestBody(area));
        map.put("dayPrice", requestBody(dayPrice));
        map.put("decoration", requestBody(decoration));
        map.put("houseTags", requestBody(houseTags));
        map.put("sort", requestBody(sort));
        map.put("seats", requestBody(seats));
        if (!TextUtils.isEmpty(keyWord)) {
            map.put("keyWord", requestBody(keyWord));
        }
        map.put("pageNo", requestBody(pageNo + ""));
        map.put("pageSize", requestBody("10"));
        OfficegoRetrofitClient.getInstance().create(HomeInterface.class)
                .getBuildingList(map)
                .enqueue(callback);
    }

    /**
     * 楼盘网点详情
     * token 	是 	string 	登录凭证
     * btype 	是 	string 	类型1:楼盘,2:网点
     * buildingId 	是 	string 	btype为1时-为楼盘id btype为2时-为网点id
     * area 	否 	string 	平方米区间英文逗号分隔
     * dayPrice 	否 	string 	楼盘的时候是 每平方米单价区间英文逗号分隔 网点的时候是 每工位每月单价区间英文逗号分隔
     * decoration 	否 	string 	装修类型id英文逗号分隔
     * houseTags 	否 	string 	房源特色id英文逗号分隔
     * vrFlag 	否 	int 	是否只看VR房源 0:不限1:只看VR房源
     * seats 	否 	string 	联合工位区间英文逗号分隔
     */
    public void getBuildingDetails(String btype, String buildingId, String area, String dayPrice,
                                   String decoration, String houseTags, String seats,
                                   RetrofitCallback<BuildingDetailsBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
//        map.put("token", requestBody(""));
        map.put("btype", requestBody(btype));
        map.put("buildingId", requestBody(buildingId));
        map.put("area", requestBody(area));
        map.put("dayPrice", requestBody(dayPrice));
        map.put("decoration", requestBody(decoration));
        map.put("houseTags", requestBody(houseTags));
        map.put("seats", requestBody(seats));
        map.put("vrFlag", requestBody("0"));
        OfficegoRetrofitClient.getInstance().create(HomeInterface.class)
                .getBuildingDetails(map)
                .enqueue(callback);
    }

    /**
     * 网点详情
     * token 	是 	string 	登录凭证
     * btype 	是 	string 	类型1:楼盘,2:网点
     * buildingId 	是 	string 	btype为1时-为楼盘id btype为2时-为网点id
     * area 	否 	string 	平方米区间英文逗号分隔
     * dayPrice 	否 	string 	楼盘的时候是 每平方米单价区间英文逗号分隔 网点的时候是 每工位每月单价区间英文逗号分隔
     * decoration 	否 	string 	装修类型id英文逗号分隔
     * houseTags 	否 	string 	房源特色id英文逗号分隔
     * vrFlag 	否 	int 	是否只看VR房源 0:不限1:只看VR房源
     * seats 	否 	string 	联合工位区间英文逗号分隔
     */
    public void getBuildingJointWorkDetails(String btype, String buildingId, String area, String dayPrice,
                                            String decoration, String houseTags, String seats,
                                            RetrofitCallback<BuildingJointWorkBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
//        map.put("token", requestBody(""));
        map.put("btype", requestBody(btype));
        map.put("buildingId", requestBody(buildingId));
        map.put("area", requestBody(area));
        map.put("dayPrice", requestBody(dayPrice));
        map.put("decoration", requestBody(decoration));
        map.put("houseTags", requestBody(houseTags));
        map.put("seats", requestBody(seats));
        map.put("vrFlag", requestBody("0"));
        OfficegoRetrofitClient.getInstance().create(HomeInterface.class)
                .getBuildingJointWorkDetails(map)
                .enqueue(callback);
    }


    /**
     * 收藏或取消
     *
     * @param buildingId buildingId
     * @param flag       flag
     * @param callback   callback
     */
    public void favorite(String buildingId, int flag, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId));
        map.put("flag", requestBody(flag + ""));
        OfficegoRetrofitClient.getInstance().create(FavoriteInterface.class)
                .favorite(map)
                .enqueue(callback);
    }

    /**
     * 收藏或取消
     *
     * @param houseId  houseId
     * @param flag     flag
     * @param callback callback
     */
    public void favoriteChild(String houseId, int flag, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("houseId", requestBody(houseId));
        map.put("flag", requestBody(flag + ""));
        OfficegoRetrofitClient.getInstance().create(FavoriteInterface.class)
                .favorite(map)
                .enqueue(callback);
    }

    /**
     * 收藏楼盘列表
     * type 1为楼盘网点收藏，2为房源收藏
     * token 	是 	string 	用户id
     * longitude 	是 	String 	当前人的经度
     * latitude 	是 	String 	当前人的纬度
     * pageNo 	否 	int 	当前页
     * pageSize
     */
    public void favoriteBuildingList(int pageNo, String longitude, String
            latitude, RetrofitCallback<CollectBuildingBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("type", requestBody("1"));
        map.put("longitude", requestBody(longitude));
        map.put("latitude", requestBody(latitude));
        map.put("pageNo", requestBody(pageNo + ""));
        map.put("pageSize", requestBody("10"));
        OfficegoRetrofitClient.getInstance().create(FavoriteInterface.class)
                .favoriteBuildingList(map)
                .enqueue(callback);
    }

    /**
     * 收藏房源列表
     * type 1为楼盘网点收藏，2为房源收藏
     * token 	是 	string 	用户id
     * longitude 	是 	String 	当前人的经度
     * latitude 	是 	String 	当前人的纬度
     * pageNo 	否 	int 	当前页
     * pageSize
     */
    public void favoriteHouseList(int pageNo, String longitude, String
            latitude, RetrofitCallback<CollectHouseBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("type", requestBody("2"));
        map.put("longitude", requestBody(longitude));
        map.put("latitude", requestBody(latitude));
        map.put("pageNo", requestBody(pageNo + ""));
        map.put("pageSize", requestBody("10"));
        OfficegoRetrofitClient.getInstance().create(FavoriteInterface.class)
                .favoriteHouseList(map)
                .enqueue(callback);
    }

    /**
     * 楼盘网点下房源列表
     * token 	是 	string 	登录凭证
     * buildingId 	是 	int 	大楼ID
     * btype 	是 	int 	类型1:楼盘,2:网点, 0全部
     * area 	否 	string 	平方米区间英文逗号分隔
     * dayPrice 	否 	string 	楼盘的时候是 每平方米单价区间英文逗号分隔 网点的时候是 每工位每月单价区间英文逗号分隔
     * decoration 	否 	string 	装修类型id英文逗号分隔
     * houseTags 	否 	string 	房源特色id英文逗号分隔
     * vrFlag 	否 	int 	是否只看VR房源 0:不限1:只看VR房源
     * seats 	否 	string 	联合工位区间英文逗号分隔
     * pageNo 	否 	int 	当前页
     * pageSize 	否 	int 	每页条数
     */
    public void getBuildingSelectList(int pageNo, String btype, String buildingId, String area, String dayPrice,
                                      String decoration, String houseTags, String seats, RetrofitCallback<BuildingDetailsChildBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
//        map.put("token", requestBody(""));
        map.put("buildingId", requestBody(buildingId));
        map.put("btype", requestBody(btype));
        map.put("area", requestBody(area));
        map.put("dayPrice", requestBody(dayPrice));
        map.put("decoration", requestBody(decoration));
        map.put("houseTags", requestBody(houseTags));
        map.put("vrFlag", requestBody("0"));
        map.put("seats", requestBody(seats));
        map.put("pageNo", requestBody(pageNo + ""));
        map.put("pageSize", requestBody("10"));
        OfficegoRetrofitClient.getInstance().create(HomeInterface.class)
                .getBuildingSelectList(map)
                .enqueue(callback);
    }

    /**
     * 楼盘房源详情
     * houseId 	是 	int 	房源id
     * btype 	是 	int 	类型1标准楼盘下的房源2联合网点下的房源
     * token 	是 	string 	登录凭证
     */
    public void selectHousebyHouseId(String btype, String houseId,
                                     RetrofitCallback<HouseOfficeDetailsBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
//        map.put("token", requestBody(""));
        map.put("houseId", requestBody(houseId));
        map.put("btype", requestBody(btype));
        OfficegoRetrofitClient.getInstance().create(HomeInterface.class)
                .selectHousebyHouseId(map)
                .enqueue(callback);
    }

    /**
     * 共享办公|网点房源详情
     * houseId 	是 	int 	房源id
     * btype 	是 	int 	类型1标准楼盘下的房源2联合网点下的房源
     * token 	是 	string 	登录凭证
     */
    public void selectHousebyJointWorkHouseId(String btype, String houseId,
                                              RetrofitCallback<HouseOfficeDetailsJointWorkBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
//        map.put("token", requestBody(""));
        map.put("houseId", requestBody(houseId));
        map.put("btype", requestBody(btype));
        OfficegoRetrofitClient.getInstance().create(HomeInterface.class)
                .selectHousebyJointWorkHouseId(map)
                .enqueue(callback);
    }

    /**
     * 全局搜索
     */
    public void searchList(String keywords, RetrofitCallback<List<SearchListBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("keywords", requestBody(keywords));
        OfficegoRetrofitClient.getInstance().create(SearchInterface.class)
                .searchList(map)
                .enqueue(callback);
    }

    /**
     * 添加搜索历史
     * token 	是 	string 	根据token解析用户id
     * keywords 	是 	String 	搜索词
     */
    public void addSearchKeywords(String keywords, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("keywords", requestBody(keywords));
        OfficegoRetrofitClient.getInstance().create(SearchInterface.class)
                .addSearchKeywords(map)
                .enqueue(callback);
    }

    /**
     * 清除搜索历史
     * token 	是 	string 	根据token解析用户id
     * id 	是 	String 	搜索词 默认传递0清除全部
     */
    public void clearSearchKeywords(RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("id", requestBody("0"));
        OfficegoRetrofitClient.getInstance().create(SearchInterface.class)
                .clearSearchKeywords(map)
                .enqueue(callback);
    }


    /**
     * 查询搜索历史
     * token 	是 	string 	根据token解析用户id
     * keywords 	是 	String 	搜索词
     */
    public void getSearchKeywords(RetrofitCallback<List<QueryHistoryKeywordsBean.DataBean>> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        OfficegoRetrofitClient.getInstance().create(SearchInterface.class)
                .getSearchKeywords(map)
                .enqueue(callback);
    }

    //********************************************************************************
    //个人信息***************************************************************************
    //********************************************************************************
    public void getUserMsg(RetrofitCallback<UserBean> callback) {
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
    public void updateUserData(String realName, String sex, String wx, RetrofitCallback<Object> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        //map.put("avatar", requestBody());
        map.put("realname", requestBody(realName));
        map.put("sex", requestBody(sex));
        map.put("company", requestBody(""));
        map.put("job", requestBody(""));
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
                .addFormDataPart("file", "avatar_officego.png", file)
                .addFormDataPart("token", SpUtils.getSignToken());
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .updateUserAvatar(builder.build())
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
                .changeWechat(map)
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
     * 看房详情
     * startTime 	是 	int 	开始时间
     * endTime 	是 	int
     */
    public void getScheduleDetails(int scheduleId, RetrofitCallback<ViewingDateDetailsBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("scheduleId", requestBody(scheduleId + ""));
        OfficegoRetrofitClient.getInstance().create(ScheduleInterface.class)
                .getScheduleDetails(map)
                .enqueue(callback);
    }
    /**
     * 聊天**********************************************************
     * 聊天**********************************************************
     */
    /**
     * houseId 	是 	int 	从楼盘进入聊天页面需要传递
     * token 	是 	是 	token
     */
    public void getTargetId(String buildingId, RetrofitCallback<ChatsBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId));
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .getTargetId(map)
                .enqueue(callback);
    }


    /**
     * houseId 	是 	int 	从房源进入聊天页面需要传递
     * token 	是 	是 	token
     */
    public void getTargetId2(String houseId, RetrofitCallback<ChatsBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("houseId", requestBody(houseId));
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .getTargetId(map)
                .enqueue(callback);
    }

    /**
     * uid 	是 	int 	targetId 聊天对方的id
     * token 	是 	是 	token
     * 获取大楼详情
     */
    public void getChatHouseDetails(String targetId, RetrofitCallback<ChatHouseBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("uid", requestBody(targetId));
        OfficegoRetrofitClient.getInstance().create(ChatInterface.class)
                .getChatHouseDetails(map)
                .enqueue(callback);
    }

    /**
     * 添加预约看房
     * buildingId 	是 	int 	楼盘id
     * houseIds 	是 	String 	房源id （英文逗号分隔）
     * time 	是 	string 	预约时间
     * remark 	否 	string 	备注
     * chatUserId 	否 	int 	聊天界面对方用户id
     * token 	是 	string 	登录凭证
     */
    public void addRenter(int buildingId, String time, String targetId,
                          RetrofitCallback<RenterBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("buildingId", requestBody(buildingId + ""));
//        map.put("houseIds", requestBody(houseIds + ""));
        map.put("time", requestBody(time));
        map.put("times", requestBody(String.valueOf(DateTimeUtils.currentTimeSecond())));//时间戳
        map.put("chatUserId", requestBody(targetId));//聊天界面对方用户id
        if (TextUtils.equals(Constants.TYPE_OWNER, SpUtils.getRole())) {
            // LogCat.e(TAG, "11111111111 Owner");
            OfficegoRetrofitClient.getInstance().create(ScheduleInterface.class)
                    .addProprietorApp(map)
                    .enqueue(callback);
        } else {
            // LogCat.e(TAG, "11111111111 tentet");
            OfficegoRetrofitClient.getInstance().create(ScheduleInterface.class)
                    .addRenter(map)
                    .enqueue(callback);
        }
    }

    /**
     * id 	是 	int 	行程id
     * auditStatus 	是 	int 	审核状态0预约1预约成功(同意)2预约失败(拒绝)3已看房4未看房
     */
    public void updateAuditStatus(int id, int auditStatus,
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
     * 切换身份
     * 用户身份标：0租户，1户主
     */
    public void switchId(String roleType,
                         RetrofitCallback<LoginBean> callback) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("token", requestBody(SpUtils.getSignToken()));
        map.put("roleType", requestBody(roleType));
        LogCat.e(TAG, "1111  chat/regTokenApp  token=" + SpUtils.getSignToken() + " roleType=" + roleType);
        OfficegoRetrofitClient.getInstance().create(MineMsgInterface.class)
                .switchId(map)
                .enqueue(callback);
    }
}

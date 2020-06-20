package com.officego.ui.home;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.officego.R;
import com.officego.commonlib.base.BaseMvpActivity;
import com.officego.commonlib.utils.CommonHelper;
import com.officego.commonlib.utils.NetworkUtils;
import com.officego.commonlib.utils.StatusBarUtils;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.commonlib.view.IVideoPlayer;
import com.officego.commonlib.view.LabelsView;
import com.officego.commonlib.view.dialog.CommonDialog;
import com.officego.commonlib.common.config.CommonNotifications;
import com.officego.model.ShareBean;
import com.officego.ui.adapter.HouseItemAllAdapter;
import com.officego.ui.adapter.IndependentAllChildAdapter;
import com.officego.ui.home.contract.BuildingDetailsContract;
import com.officego.ui.home.model.BuildingConditionItem;
import com.officego.ui.home.model.BuildingDetailsBean;
import com.officego.ui.home.model.BuildingDetailsChildBean;
import com.officego.ui.home.model.BuildingIdBundleBean;
import com.officego.ui.home.model.BuildingJointWorkBean;
import com.officego.ui.home.model.ConditionBean;
import com.officego.ui.home.presenter.BuildingDetailsPresenter;
import com.officego.utils.ImageLoaderUtils;
import com.officego.utils.WeChatUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by YangShiJie
 * Data 2020/5/14.
 * Descriptions: 楼盘，办公室详情
 **/
@SuppressLint("Registered")
@EActivity(R.layout.home_activity_house_details)
public class BuildingDetailsActivity extends BaseMvpActivity<BuildingDetailsPresenter> implements
        BuildingDetailsContract.View, NestedScrollView.OnScrollChangeListener,
        SeekBar.OnSeekBarChangeListener,
        IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnSeekCompleteListener {
    //title
    @ViewById(R.id.nsv_view)
    NestedScrollView nsvView;
    @ViewById(R.id.rl_root_house_title)
    RelativeLayout rlRootHouseTitle;
    @ViewById(R.id.tv_title)
    TextView tvTitle;
    @ViewById(R.id.ll_title)
    LinearLayout llTitle;
    //banner
    @ViewById(R.id.banner_image)
    Banner bannerImage;
    //视频图片切换
    @ViewById(R.id.rb_video)
    RadioButton rbVideo;
    @ViewById(R.id.rb_picture)
    RadioButton rbPicture;
    @ViewById(R.id.rg_video_picture)
    RadioGroup rgVideoPicture;
    //视频
    @ViewById(R.id.ctl_video_play)
    ConstraintLayout ctlVideoPlay;
    @ViewById(R.id.rl_default_house_picture)
    RelativeLayout rlDefaultHousePic;
    @ViewById(R.id.ib_init_start)
    ImageButton ibInitStart;
    @ViewById(R.id.ivp_player)
    IVideoPlayer iVideoPlayer;
    @ViewById(R.id.ib_play)
    ImageButton ibPlay;
    @ViewById(R.id.tv_current_play_time)
    TextView tvCurrentPlayTime;
    @ViewById(R.id.tv_count_play_time)
    TextView tvCountPlayTime;
    @ViewById(R.id.sb_bar)
    SeekBar sbBar;
    @ViewById(R.id.ll_play_fail)
    LinearLayout llPlayFail;
    @ViewById(R.id.rl_bottom_panel)
    RelativeLayout rlBottomPanel;
    @ViewById(R.id.ll_play_loading)
    LinearLayout llPlayLoading;
    //   名称
    @ViewById(R.id.tv_building_name)
    TextView tvBuildingName;
    //  独立办公室
    @ViewById(R.id.ctl_independent)
    ConstraintLayout ctlIndependent;
    @ViewById(R.id.tv_independent_office)
    TextView tvIndependentOffice;
    @ViewById(R.id.tv_independent_office_area)
    TextView tvIndependentOfficeArea;
    @ViewById(R.id.tv_independent_office_area_text)
    TextView tvIndependentOfficeAreaText;
    @ViewById(R.id.tv_independent_office_price)
    TextView tvIndependentOfficePrice;
    @ViewById(R.id.tv_independent_office_price_text)
    TextView tvIndependentOfficePriceText;
    @ViewById(R.id.tv_independent_office_num)
    TextView tvIndependentOfficeNum;
    @ViewById(R.id.tv_independent_office_num_text)
    TextView tvIndependentOfficeNumText;
    //开放工位
    @ViewById(R.id.ctl_open_work)
    ConstraintLayout ctlOpenWork;
    @ViewById(R.id.tv_open_office_num)
    TextView tvOpenOfficeNum;
    @ViewById(R.id.tv_open_office_price)
    TextView tvOpenOfficePrice;
    @ViewById(R.id.tv_open_office_price_text)
    TextView tvOpenOfficePriceText;
    //线路，特色
    @ViewById(R.id.label_house_characteristic)
    LabelsView labelHouseTags;
    @ViewById(R.id.tv_location)
    TextView tvLocation;
    @ViewById(R.id.tv_bus_line)
    TextView tvBusLine;
    @ViewById(R.id.rl_characteristic)
    RelativeLayout rlCharacteristic;
    //  开放工位 item
    @ViewById(R.id.rl_open_work_model)
    RelativeLayout rlOpenWorkModel;
    @ViewById(R.id.tv_open_work_model_num)
    TextView tvOpenWorkModelNum;
    @ViewById(R.id.tv_open_work_model_text)
    TextView tvOpenWorkModelText;
    @ViewById(R.id.tv_open_work_model_price)
    TextView tvOpenWorkModelPrice;
    @ViewById(R.id.tv_min_month)
    TextView tvMinMonth;
    //独立办公室，写字楼 list 查看更多
    @ViewById(R.id.rl_independent_office_model)
    RelativeLayout rlIndependentOfficeModel;
    @ViewById(R.id.rl_independent_office_set_area)
    RelativeLayout rlIndependentOfficeSetArea;
    @ViewById(R.id.tv_item_list_top)
    TextView tvItemListTop;
    @ViewById(R.id.tv_item_list_bottom)
    TextView tvItemListBottom;
    @ViewById(R.id.iv_clear_set_area)
    ImageView ivClearSetArea;
    @ViewById(R.id.tv_independent_office_text)
    TextView tvIndependentOfficeText;
    @ViewById(R.id.rv_independent_office_all)
    RecyclerView rvHorizontalAll;
    @ViewById(R.id.rv_independent_office_child)
    RecyclerView rvIndependentOfficeChild;
    @ViewById(R.id.btn_query_more)
    Button btnQueryMore;
    //共享服务 图片
    @ViewById(R.id.rv_create_service)
    RecyclerView rvCreateService;
    @ViewById(R.id.rv_base_service)
    RecyclerView rvBaseService;
    //楼盘信息
    @ViewById(R.id.tv_air_conditioning)
    TextView tvAirConditioning;
    @ViewById(R.id.tv_air_conditioning_costs)
    TextView tvAirConditioningCosts;
    @ViewById(R.id.tv_completion_time)
    TextView tvCompletionTime;
    @ViewById(R.id.tv_total_floor)
    TextView tvTotalFloor;
    @ViewById(R.id.tv_storey_height)
    TextView tvStoreyHeight;
    @ViewById(R.id.tv_lift)
    TextView tvLift;
    @ViewById(R.id.tv_parking_space)
    TextView tvParkingSpace;
    @ViewById(R.id.tv_parking_space_rent)
    TextView tvParkingSpaceRent;
    @ViewById(R.id.tv_property)
    TextView tvProperty;
    @ViewById(R.id.tv_property_costs)
    TextView tvPropertyCosts;
    @ViewById(R.id.tv_net)
    TextView tvNet;
    @ViewById(R.id.tv_promote_slogan)
    TextView tvPromoteSlogan;
    //收藏取消
    @ViewById(R.id.tv_favorite)
    TextView tvFavorite;
    /**
     * 同步进度
     */
    private static final int MESSAGE_SHOW_PROGRESS = 1;
    /**
     * 缓冲进度界限值
     */
    private static final int BUFFERING_PROGRESS = 95;
    /**
     * 延迟毫秒数
     */
    private static final int DELAY_MILLIS = 10;
    /**
     * 是否在拖动进度条中，默认为停止拖动，true为在拖动中，false为停止拖动
     */
    private boolean isDragging;
    /**
     * 是否暂停，是否静音，是否初始化了截屏
     */
    private boolean isPaused;
    /**
     * 音量
     */
    private int bufferingUpdate;
    private String videoUrl;
//    String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    /**
     * 消息处理
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //滑动中，同步播放进度
            if (msg.what == MESSAGE_SHOW_PROGRESS) {
                if (!isDragging) {
                    msg = obtainMessage(MESSAGE_SHOW_PROGRESS, iVideoPlayer.getCurrentPosition());
                    sendMessageDelayed(msg, DELAY_MILLIS);
                    syncProgress(msg.obj);
                }
            }
        }
    };
    //******************************
    @Extra
    BuildingIdBundleBean mBuildingBean;
    @Extra
    ConditionBean mConditionBean;
    //是否已经收藏
    private boolean isFavorite;
    //显示child子项列表
    private List<BuildingDetailsChildBean.ListBean> childList = new ArrayList<>();
    private IndependentAllChildAdapter childAdapter;
    //当前页码
    private String currentAreaValue = "";
    private int pageNum = 1;
    private boolean hasMore;
    private BuildingDetailsBean mData;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @AfterViews
    void init() {
        StatusBarUtils.setStatusBarFullTransparent(this);
        mPresenter = new BuildingDetailsPresenter(context);
        mPresenter.attachView(this);
        rlRootHouseTitle.setPadding(0, CommonHelper.statusHeight(this), 0, 0);
        nsvView.setOnScrollChangeListener(this);
        initIndependentBuildingRecView();
        centerPlayIsShow(true);
        initVideo();
        //详情
        mPresenter.getBuildingDetails(String.valueOf(mBuildingBean.getBtype()), String.valueOf(mBuildingBean.getBuildingId()),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getAreaValue()) ? "" : mConditionBean.getAreaValue(),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDayPrice()) ? "" : mConditionBean.getDayPrice(),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDecoration()) ? "" : mConditionBean.getDecoration(),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getHouseTags()) ? "" : mConditionBean.getHouseTags(),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getSeatsValue()) ? "" : mConditionBean.getSeatsValue());
    }

    private void initIndependentBuildingRecView() {
        tvIndependentOfficeText.setText("在租写字楼");
        //parents
        LinearLayoutManager lmHorizontal = new LinearLayoutManager(this);
        lmHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHorizontalAll.setLayoutManager(lmHorizontal);
        //child
        rvIndependentOfficeChild.setLayoutManager(new LinearLayoutManager(this));
    }

    @Click(R.id.btn_back)
    void backClick() {
        finish();
    }

    //初始化中间播放按钮显示
    private void centerPlayIsShow(boolean isShow) {
        rlDefaultHousePic.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    //是否显示视频，图片按钮
    private void radioGroupIsShow(boolean isShow) {
        rgVideoPicture.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    //视频按钮
    @Click(R.id.rb_video)
    void videoClick() {
        ctlVideoPlay.setVisibility(View.VISIBLE);
        bannerImage.setVisibility(View.GONE);
    }

    //开始播放中间按钮
    @Click(R.id.ib_init_start)
    void ibStartClick() {
        centerPlayIsShow(false);
        radioGroupIsShow(false);
        ctlVideoPlay.setVisibility(View.VISIBLE);
        bannerImage.setVisibility(View.GONE);
        //loading
        loadingView();
        //初始化播放
        initVideoPlay();
    }

    //图片按钮
    @Click(R.id.rb_picture)
    void pictureClick() {
        ctlVideoPlay.setVisibility(View.GONE);
        bannerImage.setVisibility(View.VISIBLE);
        pauseVideo();
    }

    //onPause
    @Override
    protected void onPause() {
        super.onPause();
        pauseVideo();
    }

    //视频暂停
    private void pauseVideo() {
        if (iVideoPlayer != null && iVideoPlayer.isPlaying()) {
            iVideoPlayer.pauseVideo();
            isPaused = true;
            ibPlay.setBackgroundResource(R.mipmap.play_normal);
            radioGroupIsShow(true);
        }
    }

    //微信分享
    @Click(R.id.btn_share)
    void shareClick() {
        if (isFastClick(1200)) {
            return;
        }
        if (mData != null) {
            String dec = mData.getBuilding().getMinArea() + "~" + mData.getBuilding().getMaxArea() + "㎡\n" +
                    "¥" + mData.getBuilding().getMinDayPrice() + "/㎡/天起";
            ShareBean bean = new ShareBean();
            bean.setbType(mBuildingBean.getBtype());
            bean.setId("buildingId=" + mData.getBuilding().getBuildingId());
            bean.setHouseChild(false);
            bean.setTitle(mData.getBuilding().getName());
            bean.setDes(dec);
            bean.setImgUrl(mData.getBuilding().getMainPic());
            bean.setDetailsUrl(mData.getBuilding().getMainPic());
            new WeChatUtils(context, bean);
        }
    }

    //是否收藏 0收藏 1取消
    @Click(R.id.tv_favorite)
    void favoriteClick() {
        if (isFastClick(1200)) {
            return;
        }
        mPresenter.favorite(mBuildingBean.getBuildingId() + "", isFavorite ? 1 : 0);
    }

    //聊天
    @Click(R.id.btn_chat)
    void chatClick() {
        if (isFastClick(1200)) {
            return;
        }
        CommonDialog dialog = new CommonDialog.Builder(context)
                .setTitle("请从房源列表进入找业主聊天")
                .setConfirmButton(R.string.str_confirm, (dialog12, which) -> {
                    dialog12.dismiss();
                    scrollViewY();
                }).create();
        dialog.showWithOutTouchable(false);
    }

    /**
     * 滚动指定view
     */
    private void scrollViewY() {
        int[] position = new int[2];
        rlIndependentOfficeModel.getLocationOnScreen(position);
        nsvView.fling(position[1]);
        nsvView.smoothScrollTo(0, position[1]);
    }

    private void isFavoriteView(boolean isFavorite) {
        if (isFavorite) {
            tvFavorite.setText(R.string.str_collected);
            tvFavorite.setTextColor(ContextCompat.getColor(context, R.color.common_blue_main));
            tvFavorite.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context,
                    R.mipmap.ic_collect_selected), null, null);
        } else {
            tvFavorite.setText(R.string.str_collect);
            tvFavorite.setTextColor(ContextCompat.getColor(context, R.color.text_66));
            tvFavorite.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(context,
                    R.mipmap.ic_collect_unselected), null, null);
        }
    }

    //*******************************************

    /**
     * 初始视频设置
     */
    private void initVideo() {
        initScreenWidthHeight();
        if (!NetworkUtils.isNetworkAvailable(context)) {
            errorView();
        }
    }

    /**
     * 初始视频宽高
     */
    private void initScreenWidthHeight() {
        int screenWidth = CommonHelper.getScreenWidth(context);
        ViewGroup.LayoutParams params = iVideoPlayer.getLayoutParams();
        params.width = screenWidth;
        params.height = screenWidth;
        iVideoPlayer.setLayoutParams(params);
    }

    /**
     * 初始化播放
     */
    private void initVideoPlay() {
        if (TextUtils.isEmpty(videoUrl)) {
            return;
        }
        new Handler().postDelayed(() -> {
            iVideoPlayer.load(videoUrl);
            setVideoListener();
            isPaused = false;
            ibPlay.setBackgroundResource(R.mipmap.pause_normal);
            sbBar.setProgress(0);
            tvCurrentPlayTime.setText(iVideoPlayer.generateTime(0));
        }, 200);
    }

    /**
     * 初始化video listener
     */
    private void setVideoListener() {
        sbBar.setOnSeekBarChangeListener(this);
        iVideoPlayer.setOnPreparedListener(this);
        iVideoPlayer.setOnBufferingUpdateListener(this);
        iVideoPlayer.setOnCompletionListener(this);
        iVideoPlayer.setOnErrorListener(this);
        iVideoPlayer.setOnSeekCompleteListener(this);
    }

    @Click(R.id.tv_retry)
    void retryClick() {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            shortTip(R.string.toast_network_error);
            return;
        }
        if (isFastClick(1500)) {
            return;
        }
        loadingView();
    }

    @Click(R.id.ib_play)
    void onPlayClick() {
        if (iVideoPlayer == null) {
            return;
        }
        ibPlay.setBackgroundResource(isPaused ? R.mipmap.pause_normal : R.mipmap.play_normal);
        isPaused = !isPaused;
        if (isPaused) {
            iVideoPlayer.pauseVideo();
            radioGroupIsShow(true);
        } else {
            if (sbBar.getProgress() == sbBar.getMax()) {
                //循环播放
                iVideoPlayer.seekTo(0);
                if (!iVideoPlayer.isPlaying()) {
                    iVideoPlayer.startVideo();
                    radioGroupIsShow(false);
                }
                isDragging = false;
                mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, DELAY_MILLIS);
            } else {
                iVideoPlayer.startVideo();
                radioGroupIsShow(false);
            }
        }
    }

    /**
     * 更新进度
     */
    private void syncProgress(Object obj) {
        if (obj != null) {
            String strProgress = String.valueOf(obj);
            int progress = Integer.valueOf(strProgress);
            if ((progress == 0)) {
                return;
            }
            long generateTime;
            if (progress + DELAY_MILLIS >= iVideoPlayer.getDuration()) {
                progress = sbBar.getMax();
                generateTime = sbBar.getMax();//毫秒
            } else {
                generateTime = (Long) obj;
            }
            sbBar.setProgress(progress);
            tvCurrentPlayTime.setText(iVideoPlayer.generateTime(generateTime));
        }
    }

    @UiThread
    void errorView() {
        rlBottomPanel.setVisibility(View.GONE);
        iVideoPlayer.setVisibility(View.GONE);
        llPlayFail.setVisibility(View.VISIBLE);
        llPlayLoading.setVisibility(View.GONE);
    }

    private void isShowBottomView() {
        iVideoPlayer.setVisibility(View.VISIBLE);
        llPlayFail.setVisibility(View.GONE);
        rlBottomPanel.setVisibility(View.VISIBLE);
        llPlayLoading.setVisibility(View.GONE);
    }

    private void loadingView() {
        rlBottomPanel.setVisibility(View.GONE);
        iVideoPlayer.setVisibility(View.GONE);
        llPlayFail.setVisibility(View.GONE);
        llPlayLoading.setVisibility(View.VISIBLE);
        rlDefaultHousePic.setVisibility(View.GONE);
    }

    /**
     * 缓存状态
     **/
    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
        if (iVideoPlayer != null) {
            bufferingUpdate = i;
            int onBufferingProgress;
            if (i >= BUFFERING_PROGRESS) {
                onBufferingProgress = (int) iVideoPlayer.getDuration();
            } else {
                onBufferingProgress = (int) (iVideoPlayer.getDuration() / 100 * i);
            }
            sbBar.setSecondaryProgress(onBufferingProgress);
        }
    }

    /**
     * 播放完毕
     **/
    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (iVideoPlayer != null) {
            isPaused = true;
            ibPlay.setBackgroundResource(R.mipmap.play_normal);
        }
        if (mHandler != null) {
            mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
        }
        if (bufferingUpdate == 0) {
            errorView();
        } else {
            sbBar.setProgress(sbBar.getMax());
            tvCurrentPlayTime.setText(iVideoPlayer.generateTime(sbBar.getMax()));
        }
    }

    /**
     * 播放异常
     **/
    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        shortTip(R.string.str_server_exception);
        errorView();
        return false;
    }


    /**
     * 开始播放
     **/
    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (iVideoPlayer != null) {
            isShowBottomView();
            iVideoPlayer.startVideo();
            //设置seekBar的最大限度值，当前视频的总时长（毫秒）
            long duration = iVideoPlayer.getDuration();
            //不足一秒补一秒
            if (duration % 1000 > 0) {
                duration = duration + (1000 - duration % 1000);
            }
            sbBar.setMax((int) duration);
            //视频总时长
            tvCountPlayTime.setText(Objects.requireNonNull(iVideoPlayer).generateTime(duration));
            //发送当前播放时间点通知
            mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, DELAY_MILLIS);
        }
    }

    /**
     * Seek拖动完毕
     **/
    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
    }

    /**
     * 进度条滑动监听
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            String time = iVideoPlayer.generateTime(progress);
            tvCurrentPlayTime.setText(time);
        }
    }

    /**
     * 开始拖动
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isDragging = true;
        mHandler.removeMessages(MESSAGE_SHOW_PROGRESS);
    }

    /**
     * 停止拖动
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (iVideoPlayer != null) {
            iVideoPlayer.seekTo(seekBar.getProgress());
            if (!iVideoPlayer.isPlaying()) {
                iVideoPlayer.startVideo();
                isPaused = false;
                ibPlay.setBackgroundResource(R.mipmap.pause_normal);
            }
            isDragging = false;
            mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_PROGRESS, DELAY_MILLIS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (iVideoPlayer != null) {
            iVideoPlayer.release();
        }
    }

    //**************************************
    @Override
    public int[] getStickNotificationId() {
        return new int[]{CommonNotifications.independentAll};
    }

    @Override
    public void didReceivedNotification(int id, Object... args) {
        super.didReceivedNotification(id, args);
        if (args == null) {
            return;
        }
        if (id == CommonNotifications.independentAll) {
            currentAreaValue = (String) args[0]; //传递的面积区间值
            LogCat.e(TAG, "1111: area=" + currentAreaValue);
            //请求当前楼盘下的列表 初始化list 和pageNum
            childList.clear();
            pageNum = 1;
            getChildBuildingList();
        }
    }

    //获取详情下的独立办公室列表
    private void getChildBuildingList() {
        if (mConditionBean == null || TextUtils.isEmpty(mConditionBean.getArea())) {
            showAllAreaView();//显示全部的面积view
        } else {
            showSetAreaView();//显示筛选面积的view
            currentAreaValue = mConditionBean.getArea();
            ivClearSetArea.setOnClickListener(v -> {
                showAllAreaView();//显示全部的面积view
                mConditionBean.setArea("");
                currentAreaValue = "";
                childList.clear();
                mPresenter.getBuildingSelectList(pageNum, String.valueOf(mBuildingBean.getBtype()),
                        String.valueOf(mBuildingBean.getBuildingId()), currentAreaValue,
                        mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDayPrice()) ? "" : mConditionBean.getDayPrice(),
                        mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDecoration()) ? "" : mConditionBean.getDecoration(),
                        mConditionBean == null || TextUtils.isEmpty(mConditionBean.getHouseTags()) ? "" : mConditionBean.getHouseTags(),
                        mConditionBean == null || TextUtils.isEmpty(mConditionBean.getSeats()) ? "" : mConditionBean.getSeats());
            });
        }
        mPresenter.getBuildingSelectList(pageNum, String.valueOf(mBuildingBean.getBtype()),
                String.valueOf(mBuildingBean.getBuildingId()), currentAreaValue,
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDayPrice()) ? "" : mConditionBean.getDayPrice(),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getDecoration()) ? "" : mConditionBean.getDecoration(),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getHouseTags()) ? "" : mConditionBean.getHouseTags(),
                mConditionBean == null || TextUtils.isEmpty(mConditionBean.getSeats()) ? "" : mConditionBean.getSeats());
    }

    private void showSetAreaView() {
        rvHorizontalAll.setVisibility(View.GONE);
        rlIndependentOfficeSetArea.setVisibility(View.VISIBLE);
        tvItemListTop.setText(TextUtils.isEmpty(mConditionBean.getAreaValue()) ? "" : mConditionBean.getAreaValue());
        tvItemListBottom.setText("套");
    }

    @UiThread
    void showAllAreaView() {
        rvHorizontalAll.setVisibility(View.VISIBLE);
        rlIndependentOfficeSetArea.setVisibility(View.GONE);
    }

    /**
     * "airConditioning": "无空调",//空调
     * "propertyCosts": "8.8",//物业费(元/平米/月)
     * "ParkingSpaceRent": "800",//车位租金(元/月)
     * "passengerLift": 5,//客梯
     * "parkingSpace": "400",//车位数量
     * "completionTime": 694224000,//竣工时间
     * "promoteSlogan": "市中心，交通便利",//宣传口号
     * "cargoLift": 8,//货梯
     * "totalFloor": "20",//总楼层
     * "property": "上海要你美物业管理有限公司",//物业
     * "storeyHeight": "标准3.7米，净高2.5米",//层高
     * "constructionArea": "586321",//建筑面积
     * "internet": "电信,联通,"//网络
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void BuildingDetailsSuccess(BuildingDetailsBean data) {
        //video url
        if (data == null) {
            return;
        }
        mData = data;
        if (data.getVideoUrl() != null && data.getVideoUrl().size() > 0) {
            videoUrl = data.getVideoUrl().get(0).getImgUrl();
        }
        //是否收藏
        isFavorite = data.isIsFavorite();
        isFavoriteView(data.isIsFavorite());
        //轮播图
        playBanner(data.getImgUrl());
        if (data.getBuilding() != null) {
            tvTitle.setText(data.getBuilding().getName());
            tvBuildingName.setText(data.getBuilding().getName());
            tvIndependentOfficeAreaText.setText("面积");
            tvIndependentOfficePriceText.setText("租金");
            tvIndependentOfficeNumText.setText("在租房源");
            if (TextUtils.equals(data.getBuilding().getMinArea().toString(), data.getBuilding().getMaxArea().toString())) {
                tvIndependentOfficeArea.setText(Html.fromHtml("<font color='#46C3C2'>" + data.getBuilding().getMaxArea() + "</font>㎡"));
            } else {
                tvIndependentOfficeArea.setText(Html.fromHtml("<font color='#46C3C2'>" + data.getBuilding().getMinArea() + "~" + data.getBuilding().getMaxArea() + "</font>㎡"));
            }
            tvIndependentOfficePrice.setText(Html.fromHtml("<font color='#46C3C2'>¥" + data.getBuilding().getMinDayPrice() + "</font>/㎡/天起"));
            tvIndependentOfficeNum.setText(Html.fromHtml("<font color='#46C3C2'>" + data.getBuilding().getHouseCount() + "套" + "</font>"));
            //线路
            tvLocation.setText(data.getBuilding().getAddress());
            List<String> stationLine = data.getBuilding().getStationline();
            List<String> stationName = data.getBuilding().getStationNames();
            List<String> workTime = data.getBuilding().getNearbySubwayTime();
            StringBuffer linePlan = new StringBuffer();
            for (int i = 0; i < stationLine.size(); i++) {
                if (stationLine.size() == 1 || i == stationLine.size() - 1) {
                    linePlan.append("步行").append(workTime.get(i)).append("分钟到 | ").append(stationLine.get(i)).append("号线 ·").append(stationName.get(i));
                } else {
                    linePlan.append("步行").append(workTime.get(i)).append("分钟到 | ").append(stationLine.get(i)).append("号线 ·").append(stationName.get(i)).append("\n");
                }
            }
            tvBusLine.setText(linePlan);
        }
        //楼盘信息
        if (data.getIntroduction() != null) {
            tvAirConditioning.setText("常规：" + data.getIntroduction().getAirConditioning());
            tvCompletionTime.setText(data.getIntroduction().getCompletionTime() + "年");
            tvTotalFloor.setText(data.getIntroduction().getTotalFloor() + "层");
            tvStoreyHeight.setText(data.getIntroduction().getStoreyHeight() + "米");
            tvLift.setText(data.getIntroduction().getPassengerLift() + "客梯" + data.getIntroduction().getCargoLift() + "货梯");
            tvParkingSpace.setText(data.getIntroduction().getParkingSpace() + "个");
            tvParkingSpaceRent.setText(data.getIntroduction().getParkingSpaceRent() + "元/月/位");
            tvProperty.setText(data.getIntroduction().getProperty());
            tvPropertyCosts.setText(data.getIntroduction().getPropertyCosts() + "元/㎡/月");
            tvNet.setText(data.getIntroduction().getInternet());
            tvPromoteSlogan.setText((String) data.getIntroduction().getPromoteSlogan());
        }
        //特色
        if (data.getTags() != null && data.getTags().size() > 0) {
            rlCharacteristic.setVisibility(View.VISIBLE);
            labelHouseTags.setLabels(data.getTags(), (label, position, data1) -> data1.getDictCname());
        } else {
            rlCharacteristic.setVisibility(View.GONE);
        }
        //独立办公室
        independentBuildingList(data);
        //独立办公室子列表
        getChildBuildingList();
    }

    @Override
    public void BuildingJointWorkDetailsSuccess(BuildingJointWorkBean data) {

    }

    @Override
    public void favoriteSuccess() {
        isFavorite = !isFavorite;
        isFavoriteView(isFavorite);
    }

    @Override
    public void favoriteFail() {
        shortTip(R.string.network_error);
    }

    @Click(R.id.btn_query_more)
    void queryMoreClick() {
        if (hasMore) {
            pageNum++;
            getChildBuildingList();
        } else {
            shortTip(R.string.tip_no_more_data);

        }
    }

    @Override
    public void buildingSelectListSuccess(int totals, List<BuildingDetailsChildBean.ListBean> list) {
        tvItemListBottom.setText(totals + "套");//自选面积多少套
        hasMore = list == null || list.size() >= 9;
        btnQueryMore.setText(hasMore ? getString(R.string.str_query_more_data) : getResources().getString(R.string.tip_no_more_data));
        assert list != null;
        childList.addAll(list);
        if (childAdapter == null) {
            childAdapter = new IndependentAllChildAdapter(context, childList);
            rvIndependentOfficeChild.setAdapter(childAdapter);
        }
        childAdapter.notifyDataSetChanged();
    }

    private List<String> mBannerList = new ArrayList<>();

    private void playBanner(List<BuildingDetailsBean.ImgUrlBean> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!TextUtils.isEmpty(list.get(i).getImgUrl())) {
                mBannerList.add(list.get(i).getImgUrl());
            }
        }
        bannerImage.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器，图片加载器在下方
        bannerImage.setImageLoader(new ImageLoaderUtils(context));
        //设置图片网址或地址的集合
        bannerImage.setImages(mBannerList);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        bannerImage.setBannerAnimation(Transformer.Default);
        //设置是否为自动轮播，默认是“是”。
        bannerImage.isAutoPlay(false);
        bannerImage.start();
    }

    /**
     * 独立办公室，写字楼
     * all list横屏滑动
     * child list
     */
    private void independentBuildingList(BuildingDetailsBean data) {
        List<BuildingConditionItem> conditionList = new ArrayList<>();
        conditionList.add(new BuildingConditionItem("全部", "", data.getFactorMap().getBuildingItem0() + "套"));
        conditionList.add(new BuildingConditionItem("0-100㎡", "0,100", data.getFactorMap().getBuildingItem1() + "套"));
        conditionList.add(new BuildingConditionItem("100-200㎡", "100,200", data.getFactorMap().getBuildingItem2() + "套"));
        conditionList.add(new BuildingConditionItem("200-300㎡", "200,300", data.getFactorMap().getBuildingItem3() + "套"));
        conditionList.add(new BuildingConditionItem("300-400㎡", "300,400", data.getFactorMap().getBuildingItem4() + "套"));
        conditionList.add(new BuildingConditionItem("400-500㎡", "400,500", data.getFactorMap().getBuildingItem5() + "套"));
        conditionList.add(new BuildingConditionItem("500-1000㎡", "500,1000", data.getFactorMap().getBuildingItem6() + "套"));
        conditionList.add(new BuildingConditionItem("1000㎡以上", "1000,9999", data.getFactorMap().getBuildingItem7() + "套"));
        rvHorizontalAll.setAdapter(new HouseItemAllAdapter(context, conditionList));
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > oldScrollY) {//向下滚动
            if (scrollY > getResources().getDimensionPixelSize(R.dimen.dp_230)) {
                llTitle.setVisibility(View.VISIBLE);
                rlRootHouseTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.common_blue_main));
            }
        } else if (scrollY < oldScrollY || scrollY == 0) {//向上滚动 scrollY == 0 滚动到顶
            if (scrollY < getResources().getDimensionPixelSize(R.dimen.dp_230)) {
                llTitle.setVisibility(View.GONE);
                rlRootHouseTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
            }
        }
    }
}

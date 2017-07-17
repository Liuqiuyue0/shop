package demo2.com.example.liuqiuyue.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.Contants;
import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.WareListActivity;
import demo2.com.example.liuqiuyue.shop.adapter.HomeCategoryAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.decoration.DividerItemDecoration;
import demo2.com.example.liuqiuyue.shop.bean.Banner;
import demo2.com.example.liuqiuyue.shop.bean.Campaign;
import demo2.com.example.liuqiuyue.shop.bean.HomeCampaign;
import demo2.com.example.liuqiuyue.shop.http.OkHttpHelper;
import demo2.com.example.liuqiuyue.shop.http.SpotsCallback;
import demo2.com.example.liuqiuyue.shop.utils.ToastUtils;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Ivan on 15/9/25.
 */
public class HomeFragment extends Fragment {
    //轮播广告
    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;
    //指示器
    @ViewInject(R.id.custom_indicator)
    private PagerIndicator indicator;
    //首页分类
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    private HomeCategoryAdapter mAdatper;
    private static final String TAG = "HomeFragment";

    private List<Banner> mBanners;
    private List<HomeCampaign> mCampaigns;

    //获取封装
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        x.view().inject(this, view);
        //初始化RecyclerView
        initRecyclerView(view);
        //OkHttp方法
        requestImage();
        return view;

    }

    /**
     * OkHttp方法,加载网络图片 用来进行轮播
     */
    public void requestImage() {
        String url = Contants.API.BANNER_HOME;
        httpHelper.get(url, new SpotsCallback<List<Banner>>(getContext()) {
            @Override
            public void onSuccess(Call call, Response response, List<Banner> banners) {

                mBanners = banners;
                initSlider();
            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);
            }
        });
    }


    /**
     * 初始化RecyclerView
     * 加载网络商品分类信息
     */

    private void initRecyclerView(View view) {

        String url = Contants.API.CAMPAINGN_Home;
        httpHelper.get(url, new SpotsCallback<List<HomeCampaign>>(getContext()) {

            @Override
            public void onSuccess(Call call, Response response, List<HomeCampaign> homeCampaign) {
                initData(homeCampaign);

            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {
                ToastUtils.show(getContext(), "网络不给力");
            }

        });
    }

    /**
     * RecyclerView的数据
     *
     * @param homeCampaign
     */
    public void initData(List<HomeCampaign> homeCampaign) {
        //添加适配器
        mAdatper = new HomeCategoryAdapter(homeCampaign, getActivity());
        mRecyclerView.setAdapter(mAdatper);
        //点击跳转
        mAdatper.setOnClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                //点击动画效果
                animator(view);
                Intent intent = new Intent(getActivity(), WareListActivity.class);
                //将ID添加到intent
                intent.putExtra(Contants.CAMPAIGN_ID, campaign.getId());

                startActivity(intent);

            }
        });
        //装饰Item
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        //布局样式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    /**
     * Gson解析好的数据用Slider显示
     * 初始化Slider 轮播广告
     */
    private void initSlider() {

        if (mBanners != null) {
            for (final Banner banner : mBanners) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                //图片
                textSliderView.image(banner.getImgUrl());
                //描述
                textSliderView.description(banner.getName());

                mSliderLayout.addSlider(textSliderView);
                //点击事件
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        Intent intent = new Intent(getActivity(), WareListActivity.class);
                        //将ID添加到intent
                        intent.putExtra(Contants.CAMPAIGN_ID, banner.getId());

                        startActivity(intent);
                    }
                });
            }
        }


        //自定义构造器
        mSliderLayout.setCustomIndicator(indicator);
        //动画
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        //选择提供的视图过渡效果
//        mSliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        //时间
        mSliderLayout.setDuration(4000);

        /**
         * 监听
         */

        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override  // 在页面滚动
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override  //在选择页面
            public void onPageSelected(int position) {


            }

            @Override //页滚动状态更改
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 自定义动画
     *
     * @param view
     */
    public void animator(final View view) {

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.5f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.5f, 1f)
        );
        animatorSet.setDuration(2000);
        animatorSet.start();
    }

    @Override
    public void onStop() {
        mSliderLayout.stopAutoCycle();
        super.onStop();
    }
}

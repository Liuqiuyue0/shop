package demo2.com.example.liuqiuyue.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.Contants;
import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.WareListActivity;
import demo2.com.example.liuqiuyue.shop.adapter.BaseAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.CategoryAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.CategoryWaresAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.decoration.DividerGridItemDecoration;
import demo2.com.example.liuqiuyue.shop.adapter.decoration.DividerItemDecoration;
import demo2.com.example.liuqiuyue.shop.bean.Banner;
import demo2.com.example.liuqiuyue.shop.bean.Category;
import demo2.com.example.liuqiuyue.shop.bean.Page;
import demo2.com.example.liuqiuyue.shop.bean.Wares;
import demo2.com.example.liuqiuyue.shop.http.OkHttpHelper;
import demo2.com.example.liuqiuyue.shop.http.SpotsCallback;
import demo2.com.example.liuqiuyue.shop.utils.PagerUtils;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Ivan on 15/9/22.
 */
public class CategoryFragment extends BaseFragment implements PagerUtils.OnPageListener<Wares> {
    @ViewInject(R.id.cat_recycleLef)
    private RecyclerView mRecyclerViewLef;
    @ViewInject(R.id.cat_recyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.cat_slider)
    private SliderLayout mSliderLayout;
    @ViewInject(R.id.cat_refresh)
    private MaterialRefreshLayout materialRefreshLayout;

    private OkHttpHelper helper = OkHttpHelper.getInstance();
    private CategoryAdapter mCategoryAdapter;
    private CategoryWaresAdapter mWaresAdapter;

    private int curPage = 1;
    private long Category_id = 1;

    private PagerUtils pager;
    //正常状态
    private static final int STATE_NORMAL = 0;
    //当前状态
    private int state = STATE_NORMAL;
    private PagerUtils.Builder builder;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        x.view().inject(this, view);

        requestCategoryData();
        requestImage();
        return view;
    }

    public void init() {
        builder = PagerUtils.newBuilder()
                .setUrl(Contants.API.WARES_LIST)
                .setLoadMore(true)
                .setRefreshLayout(materialRefreshLayout)
                .setOnPageListener(this);
        pager = builder.build(getContext(), new TypeToken<Page<Wares>>() {
        }.getType());
        pager.requestWare(Category_id);


    }


    /**
     * 一级列表数据获取
     */
    private void requestCategoryData() {

        String url = Contants.API.CATEGORY_LIST;
        helper.get(url, new SpotsCallback<List<Category>>(getContext()) {
            @Override
            public void onSuccess(Call call, Response response, List<Category> categories) {
                showCategoryData(categories);
                //将categories的第一行的第一个给二级列表
                if (categories != null && categories.size() > 0) {
                    Category_id = categories.get(0).getId();
                    pager.requestWare(Category_id);
                }
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
     * 一级列表显示
     *
     * @param data
     */
    private void showCategoryData(List<Category> data) {
        mCategoryAdapter = new CategoryAdapter(getContext(), data);
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Category category = mCategoryAdapter.getItem(position);
                //点击列表时，恢复最初状态,每个一级列表的第一页
                builder.setCurPage(1);
                pager.putParam("state", STATE_NORMAL);

                //将每一个分类的id传给二级商品列表
                Category_id = category.getId();
                pager = builder.build(getContext(), new TypeToken<Page<Wares>>() {
                }.getType());
                pager.requestWare(Category_id);
            }
        });
        mRecyclerViewLef.setAdapter(mCategoryAdapter);
        mRecyclerViewLef.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewLef.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewLef.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * 得到轮播数据
     */
    private void requestImage() {
        String url = Contants.API.BANNER + "?type=1";
        helper.get(url, new SpotsCallback<List<Banner>>(getContext()) {
            @Override
            public void onSuccess(Call call, Response response, List<Banner> banners) {

                showSliderView(banners);
            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {

            }
        });
    }

    /**
     * 显示轮播照片
     *
     * @param banners
     */
    private void showSliderView(List<Banner> banners) {

        if (banners != null) {
            for (final Banner banner : banners) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                //图片
                textSliderView.image(banner.getImgUrl());
                //描述
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
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
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        //动画
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        //选择提供的视图过渡效果
        mSliderLayout.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        //时间
        mSliderLayout.setDuration(3000);
    }


    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter = new CategoryWaresAdapter(getContext(), datas);
        mRecyclerView.setAdapter(mWaresAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getContext()));
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.clearData();
        //添加数据
        mWaresAdapter.refreshData(datas);
        //选择位置
        mRecyclerView.scrollToPosition(0);

    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        //添加数据
        mWaresAdapter.loadMoreData(datas);
        //选中滚动位置
        mRecyclerView.scrollToPosition(mWaresAdapter.getDatas().size());
    }
}




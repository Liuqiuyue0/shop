package demo2.com.example.liuqiuyue.shop;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.adapter.BaseAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.HotAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.decoration.DividerItemDecoration;
import demo2.com.example.liuqiuyue.shop.bean.Page;
import demo2.com.example.liuqiuyue.shop.bean.Wares;
import demo2.com.example.liuqiuyue.shop.utils.PagerUtils;
import demo2.com.example.liuqiuyue.shop.widget.CtToolbar;


/**
 * Created by liuqiuyue on 2017/4/25.
 * 首页分类商品子列表
 */
@ContentView(R.layout.activity_ware_list)
public class WareListActivity extends BaseActivity implements PagerUtils.OnPageListener<Wares>, TabLayout.OnTabSelectedListener
        , View.OnClickListener {
    //tab的tag
    public static final int TAG_DEFAULT = 0;
    public static final int TAG_SALE = 1;
    public static final int TAG_PRICE = 2;
    private static final String TAG = "WareListActivity";

    public static final int ACTION_LIST = 1;
    public static final int ACTION_GIRD = 2;

    @ViewInject(R.id.WareList_text)
    private TextView mText;
    @ViewInject(R.id.WareList_tabLayout)
    private TabLayout mTableLayout;
    @ViewInject(R.id.WareList_recycle)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.WareList_refresh)
    private MaterialRefreshLayout mRefreshLayout;
    @ViewInject(R.id.WareList_toolbar)
    private CtToolbar mToolbar;

    private int orderBy = 0;
    private long campaignId = 0;
    private HotAdapter mAdapter;
    private PagerUtils pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        //获取campaignId
        campaignId = getIntent().getLongExtra(Contants.CAMPAIGN_ID, 0);
        initToolbar();
        getData();
        initTab();
    }

    public void initToolbar() {
        //给返回按钮添加点击事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });

        mToolbar.setTitle(R.string.ware_list);
        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolbar.getRightButton().setTag(ACTION_LIST);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonOnClickListener(this);
    }

    public void getData() {
        pager = PagerUtils.newBuilder()
                .setUrl(Contants.API.WARES_CAMPAIGN_LIST)
                .setLoadMore(true)
                .putParam("campaignId", campaignId)
                .putParam("orderBy", orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setOnPageListener(this)
                .build(this, new TypeToken<Page<Wares>>() {
                }.getType());
        pager.request();

    }

    private void initTab() {

        TabLayout.Tab tab = mTableLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTableLayout.addTab(tab);


        tab = mTableLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        mTableLayout.addTab(tab);

        tab = mTableLayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        mTableLayout.addTab(tab);
        mTableLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mText.setText("共有商品数量:" + totalCount);
        mAdapter = new HotAdapter(this, datas);
        //监听
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //布局样式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //装饰
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter.clearData();
        mAdapter.refreshData(datas);
        //选择位置
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        //添加数据mAdapter.getDatas().size()当前最后一个数据的位置
//        mAdapter.addData(mAdapter.getDatas().size(), datas);
        mAdapter.loadMoreData(datas);
        //选中滚动位置
        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
    }

    /**
     * Tab监听
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //更改orderBy，达到不同的排序方式的切换
        orderBy = (int) tab.getTag();   //获取orderBy
        pager.putParam("orderBy", orderBy);
        pager.request();

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if (ACTION_LIST == action) {
            mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
            mToolbar.getRightButton().setTag(ACTION_GIRD);
            //更改布局
            mAdapter.resetLayout(R.layout.template_grid_wares);

            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mRecyclerView.setAdapter(mAdapter);
        } else if (ACTION_GIRD == action) {
            mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
            mToolbar.getRightButton().setTag(ACTION_LIST);
            //更改布局
            mAdapter.resetLayout(R.layout.template_hot_wares);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(mAdapter);



        }
    }


}

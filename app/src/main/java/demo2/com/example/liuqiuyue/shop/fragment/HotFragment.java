package demo2.com.example.liuqiuyue.shop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.Contants;
import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.WareDetailActivity;
import demo2.com.example.liuqiuyue.shop.adapter.BaseAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.HotAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.decoration.DividerItemDecoration;
import demo2.com.example.liuqiuyue.shop.bean.Page;
import demo2.com.example.liuqiuyue.shop.bean.Wares;
import demo2.com.example.liuqiuyue.shop.utils.PagerUtils;

/**
 * Created by liuqiuyue on 2017/4/13.
 */

public class HotFragment extends BaseFragment implements PagerUtils.OnPageListener<Wares> {

    private HotAdapter mAdapter;
    @ViewInject(R.id.hot_recycler)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.hot_refresh)
    private MaterialRefreshLayout mRefreshLayout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        x.view().inject(this, view);
        return view;
    }

    public void init() {
        PagerUtils.Builder builder = PagerUtils.newBuilder()
                .setUrl(Contants.API.WARES_HOT)
                .setLoadMore(true)
                .setPageSize(20)
                .setRefreshLayout(mRefreshLayout)
                .setOnPageListener(this);

        PagerUtils pager = builder.build(getContext(), new TypeToken<Page<Wares>>() {
        }.getType());
        pager.request();  //请求数据
    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter = new HotAdapter(getContext(), datas);
        //监听
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //
                Wares mWares = mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                intent.putExtra(Contants.WARE, mWares);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //布局样式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //装饰
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter.clearData();
        //添加数据
        mAdapter.refreshData(datas);
        //选择位置
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        //添加数据mAdapter.getDatas().size()当前最后一个数据的位置
        mAdapter.loadMoreData(datas);
        //选中滚动位置
        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
    }

    //更改某个数据的值
//    public void test() {
//        PagerUtils pagerUtils=。。。;
//        pagerUtils.putParams(key, Value);
//        pagerUtils.request();
//    }
}




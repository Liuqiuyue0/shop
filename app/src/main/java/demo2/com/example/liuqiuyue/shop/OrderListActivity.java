package demo2.com.example.liuqiuyue.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo2.com.example.liuqiuyue.shop.adapter.BaseAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.OrderAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.decoration.DividerGridItemDecoration;
import demo2.com.example.liuqiuyue.shop.bean.Order;
import demo2.com.example.liuqiuyue.shop.http.OkHttpHelper;
import demo2.com.example.liuqiuyue.shop.http.SpotsCallback;
import demo2.com.example.liuqiuyue.shop.widget.CtToolbar;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liuqiuyue on 2017/5/13.
 * 我的订单列表
 */
@ContentView(R.layout.activity_order_list)
public class OrderListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{
    //tab的tag
    public static final int STATUS_ALL = 1000;  //全部订单
    public static final int STATUS_PAY_WAIT = 0;   //待支付
    public static final int STATUS_PAY_FALL = 2;  //支付失败
    public static final int STATUS_SUCCESS = 1;  //支付成功
    private int status = STATUS_ALL;

    @ViewInject(R.id.OrderList_tabLayout)
    private TabLayout mTableLayout;
    @ViewInject(R.id.OrderList_toolbar)
    private CtToolbar mToolbar;
    @ViewInject(R.id.Order_recycle)
    private RecyclerView mRecyclerView;
    private OkHttpHelper helper = OkHttpHelper.getInstance();
    private OrderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initToolbar();
        initTab();

    }


    /**
     * 获取订单的方法
     */
    private void getOrder() {
        Long userId = ContantsApplication.getInstance().getUser().getId();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("status", status);

        helper.get(Contants.API.ORDER_LIST, params, new SpotsCallback<List<Order>>(this) {

            @Override
            public void onSuccess(Call call, Response response, List<Order> orders) {
                showOrder(orders);
            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {

            }
        });

    }


    /**
     * 显示订单列表的方法
     *
     * @param orders
     */
    private void showOrder(List<Order> orders) {
        if (adapter != null) {
            adapter = new OrderAdapter(this, orders);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));

            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onClick(View view, int position) {

                  toOrderDetailActivity(position);

                }
            });
        }

    }

    /***
     *
     * @param position
     */
    private void toOrderDetailActivity(int position) {
        Intent intent = new Intent(this,OrderDetailActivity.class);
        Order order = adapter.getItem(position);
        intent.putExtra("order",order);
        StartActivity(intent,true);
    }

    private void initToolbar() {
        //给返回按钮添加点击事件
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle(getString(R.string.myOrder));

    }

    private void initTab() {

        TabLayout.Tab tab = mTableLayout.newTab();
        tab.setText("全部订单");
        tab.setTag(STATUS_ALL);
        mTableLayout.addTab(tab);


        tab = mTableLayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        mTableLayout.addTab(tab);

        tab = mTableLayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FALL);
        mTableLayout.addTab(tab);

        tab = mTableLayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        mTableLayout.addTab(tab);
        mTableLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        status = (int) tab.getTag();  //得到状态值
        getOrder();
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

















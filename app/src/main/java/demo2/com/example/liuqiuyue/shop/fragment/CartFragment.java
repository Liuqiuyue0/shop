package demo2.com.example.liuqiuyue.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.TestActvity;
import demo2.com.example.liuqiuyue.shop.adapter.CartAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.decoration.DividerItemDecoration;
import demo2.com.example.liuqiuyue.shop.bean.CartBean;
import demo2.com.example.liuqiuyue.shop.utils.CartProvider;
import demo2.com.example.liuqiuyue.shop.widget.CtToolbar;


/**
 * Created by Ivan on 15/9/22.
 * 购物车
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {
    @ViewInject(R.id.cart_checkboxAll)
    private CheckBox checkBox;
    @ViewInject(R.id.cart_recycle)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.cart_count)
    private TextView cart_price;
    @ViewInject(R.id.cart_set)
    private Button cart_order;
    @ViewInject(R.id.cart_delete)
    private Button cart_delete;
    @ViewInject(R.id.con_toolbar)
    private CtToolbar mToolbar;

    private CartProvider provider;
    private CartAdapter mAdapter;

    private boolean ShowOrder = true;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        x.view().inject(this, view);
        provider = new CartProvider(getContext());
        cart_order.setOnClickListener(this);
        return view;
    }

    @Override
    public void init() {
        List<CartBean> data = provider.getAll();
        mAdapter = new CartAdapter(getContext(), data, checkBox, cart_price);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

    }


    /**
     * 刷新的方法
     */
    public void refresh() {
        //请数据
        mAdapter.clearData();
        //得到数据 重新添加
        List<CartBean> cart = provider.getAll();
        mAdapter.refreshData(cart);
        mAdapter.showCount();
    }

    /**
     * 显示付款按钮
     */
    public void isShowOrder() {
        cart_order.setVisibility(View.VISIBLE);
        cart_delete.setVisibility(View.GONE);
        //全选
        checkBox.setChecked(true);
        mAdapter.checkAllListener(true);
        cart_price.setVisibility(View.VISIBLE);
    }

    /**
     * 显示删除按钮
     */
    public void isShowDelete() {
        cart_delete.setVisibility(View.VISIBLE);
        cart_order.setVisibility(View.GONE);
        //全不选
        checkBox.setChecked(false);
        mAdapter.checkAllListener(false);
        cart_price.setVisibility(View.GONE);
        cart_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.deleteCart();

            }
        });

    }


    /**
     * 改toolBar样式
     */

    public void initToolBar() {

        //隐藏SearchView
        mToolbar.hideSearchView();
        //设置标题
        mToolbar.setTitle(R.string.cart);
        //设置按钮
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setText(R.string.cart_set);
        //设置点击事件
        mToolbar.getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowOrder = !ShowOrder;
                if (ShowOrder) {
                    isShowOrder();
                    mToolbar.getRightButton().setText(R.string.cart_set);
                } else {
                    isShowDelete();
                    mToolbar.getRightButton().setText(R.string.bt_ov);
                }
            }
        });
    }

    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), TestActvity.class);
        StartActivity(intent, true);
    }
}


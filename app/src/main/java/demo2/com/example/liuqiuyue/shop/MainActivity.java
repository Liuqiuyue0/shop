package demo2.com.example.liuqiuyue.shop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import demo2.com.example.liuqiuyue.shop.bean.Tab;
import demo2.com.example.liuqiuyue.shop.fragment.CartFragment;
import demo2.com.example.liuqiuyue.shop.fragment.CategoryFragment;
import demo2.com.example.liuqiuyue.shop.fragment.HomeFragment;
import demo2.com.example.liuqiuyue.shop.fragment.HotFragment;
import demo2.com.example.liuqiuyue.shop.fragment.MineFragment;
import demo2.com.example.liuqiuyue.shop.widget.FragmentTabHost;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {


    private LayoutInflater mInflater;
    //导航栏
    private FragmentTabHost mTabhost;
    //用于装载每一个分页的Tab
    private List<Tab> mTabs = new ArrayList<>(5);
    private CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        //初始化底部导航栏
        initTab();

    }

    /**
     * 初始化底部导航栏
     */
    private void initTab() {
        mInflater = LayoutInflater.from(this);
        mTabhost = (FragmentTabHost) this.findViewById(android.R.id.tabhost);
        //调用setup()方法（必须调用这个方法），设置FragmentManager，以及指定用于装载Fragment的布局容器
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //新建5个分页，并且添加到List当中，便于管理，图标使用selector进行状态选择，即选中的时候会变色。
        Tab tab_home = new Tab(HomeFragment.class, R.string.home, R.drawable.selector_icon_home);
        Tab tab_hot = new Tab(HotFragment.class, R.string.hot, R.drawable.selector_icon_hot);
        Tab tab_category = new Tab(CategoryFragment.class, R.string.catagory, R.drawable.selector_icon_category);
        final Tab tab_cart = new Tab(CartFragment.class, R.string.cart, R.drawable.selector_icon_cart);
        Tab tab_mine = new Tab(MineFragment.class, R.string.mine, R.drawable.selector_icon_mine);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        for (Tab tab : mTabs) {
            //新建5个TabSpec，并且设置好它的Indicator。一个按钮就是一个TabSpec就是一个分页
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabhost.addTab(tabSpec, tab.getFragment(), null);

        }
        //完成 购物车点击一次tab就刷新一次的要求
        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId == getString(R.string.cart)) {
                    //刷新fragment
                    refData();
                } else {

                }
            }
        });

        //去掉分隔线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        //设置当前默认的分页为第一页
        mTabhost.setCurrentTab(0);
    }

    /**
     * 刷新fragment
     */
    public void refData() {
        if (cartFragment == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            //如果fragment不为空时获取fragment 程序刚运行时fragment为空
            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                cartFragment.refresh();
            }
        } else {
            cartFragment.refresh();
        }
    }

    /**
     * 自定义指示器
     *
     * @param tab
     * @return
     */
    private View buildIndicator(Tab tab) {

        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;
    }


}

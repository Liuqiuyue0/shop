package demo2.com.example.liuqiuyue.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo2.com.example.liuqiuyue.shop.adapter.AddressAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.decoration.DividerItemDecoration;
import demo2.com.example.liuqiuyue.shop.bean.Address;
import demo2.com.example.liuqiuyue.shop.http.OkHttpHelper;
import demo2.com.example.liuqiuyue.shop.http.SpotsCallback;
import demo2.com.example.liuqiuyue.shop.message.BaseRespMsg;
import demo2.com.example.liuqiuyue.shop.widget.CtToolbar;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liuqiuyue on 2017/5/3.
 */
@ContentView(R.layout.activity_address_list)
public class AddressListActivity extends BaseActivity {
    @ViewInject(R.id.addressList_recycle)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.addressList_toolbar)
    private CtToolbar toolbar;

    private OkHttpHelper helper = OkHttpHelper.getInstance();
    private AddressAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initToolbar();

    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressListActivity.this.finish();
            }
        });
        toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity();   //跳转到添加地址的页面
            }
        });
    }

    /**
     * 跳转到添加地址页面
     */
    private void toActivity() {
        Intent intent = new Intent(this, AddressAddActivity.class);
        startActivityForResult(intent,Contants.REQUEST_CODE);
    }

    /**
     * 接收回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initAddress();   //初始化地址
    }

    /**
     * 初始胡地址的方法
     */
    private void initAddress() {
        Map<String, Object> parsms = new HashMap<>(1);
        parsms.put("user_id", String.valueOf(ContantsApplication.getInstance().getUser().getId()));
        helper.get(Contants.API.ADDRESS_LIST, parsms, new SpotsCallback<List<Address>>(this) {
            @Override
            public void onSuccess(Call call, Response response, List<Address> addresses) {
                showAddress(addresses);
            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {

            }
        });

    }

    /**
     * 显示地址的方法
     * @param addresses
     */
    private void showAddress(List<Address> addresses) {
        Collections.sort(addresses);  //默认排序
        if (adapter == null) {
            adapter=new AddressAdapter(this, addresses, new AddressAdapter.AddressListener() {
                @Override
                public void setDefault(Address address) {
                    updataAddress(address);
                }
            });
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        }

        else{
            adapter.refreshData(addresses);   //刷新数据
            mRecyclerView.setAdapter(adapter);   //添加adapter
        }
    }

    /**
     *
     * 刷新地址的方法
     * @param address
     */
    private void updataAddress(Address address) {
        Map<String,Object> params = new HashMap<>(1);
        params.put("id", address.getId());
        params.put("consignee",address.getConsignee());
        params.put("phone",address.getPhone());
        params.put("addr",address.getAddr());
        params.put("zip_code",address.getZipCode());
        params.put("is_default", address.getIsDefault());

        helper.post(Contants.API.ADDRESS_UPDATE, params, new SpotsCallback<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Call call, Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus()==baseRespMsg.STATUS_SUCCESS){
                    initAddress();
                }
            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {

            }
        });


    }

}


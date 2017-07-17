package demo2.com.example.liuqiuyue.shop.utils;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import demo2.com.example.liuqiuyue.shop.bean.CartBean;
import demo2.com.example.liuqiuyue.shop.bean.Wares;

/**
 * Created by liuqiuyue on 2017/4/20.
 * 购物车的数据提供器
 */

public class CartProvider {
    //数据用key value方式储存
    private SparseArray<CartBean> data;

    private static final String CART_JSON = "cart_json";

    private Context context;


    //构造方法，用于初始化数据
    public CartProvider(Context context) {
        this.context = context;
        data = new SparseArray<>(10);
        listToData();   //将本地的list格式的数据转换成SparseArray形式
    }

    /**
     * 向购物车内添加数据
     * 以存在的商品只改变数量
     *
     * @param cart
     */
    public void put(CartBean cart) {
        //cart.getId得到的是long类型，要转换成int
        CartBean id = data.get(cart.getId().intValue());
        if (id != null) {
            id.setCount(id.getCount() + 1);
        } else {
            id = cart;
            id.setCount(1);
        }
        data.put(cart.getId().intValue(), id);
        //同步更新本地数据
        commit();
    }
    public void put(Wares wares) {
        //转换类型的方法
       CartBean cart=convertData(wares);
        put(cart);
        //同步更新本地数据
        commit();
    }

    /**
     * 更新购物车数据
     *
     * @param cart
     */
    public void upData(CartBean cart) {
        data.put(cart.getId().intValue(), cart);
        //同步更新本地数据
        commit();
    }

    /**
     * 删除购物车数据
     *
     * @param cart
     */
    public void delete(CartBean cart) {
        data.delete(cart.getId().intValue());
        //同步更新本地数据
        commit();
    }

    /**
     * 得到所有数据的方法
     *
     * @return
     */
    public List<CartBean> getAll() {


        return getDtaFromLocal();
    }

    /**
     * 将数据保存到本地的方法，同步更新
     */
    public void commit() {
        //将data转换成list格式
        List<CartBean> cart = dataToLIst();
        //将数据到本地
        PreferencesUtils.putString(context, CART_JSON, JSONUtil.toJSON(cart));
    }

    /**
     * 将数据传换成List的方法。因为取数据时取出的数list格式，所以存的时候也是list格式
     * 而当前data是SparseArray格式，需要转换
     *
     * @return
     */
    private List<CartBean> dataToLIst() {
        int size = data.size();
        List<CartBean> cart_list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            cart_list.add(data.valueAt(i));

        }
        return cart_list;
    }

    /**
     * 将取出的list数据准换成sparse的形式，进行put,delete,upData操作
     *
     * @return
     */
    private void listToData() {
        //获取数据
        List<CartBean> cart_list = getDtaFromLocal();
        if (cart_list != null && cart_list.size() > 0) {
            for (CartBean cart : cart_list) {
                data.put(cart.getId().intValue(), cart);

            }
        }
    }

    /**
     * 从本地读取数据的方法
     *
     * @return
     */
    public List<CartBean> getDtaFromLocal() {
        //获取数据
        String json = PreferencesUtils.getString(context, CART_JSON);
        List<CartBean> cart=null;
        if (json != null) {
            cart = JSONUtil.fromJson(json, new TypeToken<List<CartBean>>() {
            }.getType());
        }
        return cart;
    }
    /**
     * 子类强制转换成父类会出错 所以
     * 将子类的数据内容转换成父类
     *
     * @return
     */
    public CartBean convertData(Wares item) {
        CartBean cart = new CartBean();
        cart.setId(item.getId());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }

}

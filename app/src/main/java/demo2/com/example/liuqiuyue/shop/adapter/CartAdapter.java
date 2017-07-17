package demo2.com.example.liuqiuyue.shop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.bean.CartBean;
import demo2.com.example.liuqiuyue.shop.utils.CartProvider;
import demo2.com.example.liuqiuyue.shop.widget.AddSubView;

/**
 * Created by liuqiuyue on 2017/4/20.
 * 购物车 Adapter
 */

public class CartAdapter extends SimpleAdapter<CartBean> implements BaseAdapter.OnItemClickListener {
    private CheckBox mCheckboxAll;
    private TextView price;

    private CartProvider provider;


    public CartAdapter(Context context, List<CartBean> data, CheckBox box, TextView tv) {
        super(context, data, R.layout.template_cart_list);

        provider = new CartProvider(context);
        setOnItemClickListener(this);//从各选到全选

        mCheckboxAll = box;
        checkAll(); //全选到各选

        price = tv;
        showCount();
    }

    @Override
    public void bindData(BaseViewHolder holder, final CartBean cartBean) {

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.cart_drawee_view);
        draweeView.setImageURI(Uri.parse(cartBean.getImgUrl()));
        holder.getTextView(R.id.car_text_title).setText(cartBean.getName());
        holder.getTextView(R.id.cart_text_price).setText(cartBean.getPrice() + "");

        CheckBox mCheckbox = (CheckBox) holder.getView(R.id.cartlist_checkbox);
        mCheckbox.setChecked(cartBean.isChecked());

        AddSubView num = (AddSubView) holder.getView(R.id.num_control);
        num.setValue(cartBean.getCount());
        //为加减按钮添加点击事件
        num.setOnButtonClickListener(new AddSubView.onButtonClickListener() {
            @Override
            //重新计数，刷新，计算总价
            public void onAddClick(View view, int value) {
                cartBean.setCount(value);
                provider.upData(cartBean);
                showCount();
            }

            @Override
            public void onSubClick(View view, int value) {
                cartBean.setCount(value);
                provider.upData(cartBean);
                showCount();
            }
        });

    }

    /**
     * 显示总价
     */
    public void showCount() {
        float cart_price = getCount();
        price.setText("合计:" + cart_price);

    }

    /**
     * 总计价格的方法
     */
    public float getCount() {
        float sum = 0;
        if (isNull())
            return sum;
        //否则如果商品被选中，则计算总价
        for (CartBean cart : mData) {
            if (cart.isChecked())
                sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }

    public boolean isNull() {

        return (mData == null || mData.size() < 0);
    }

    @Override
    public void onClick(View view, int position) {
        CartBean cart = getItem(position);
        cart.setChecked(!cart.isChecked());   //被点击则checkBox状态取反
        notifyItemChanged(position);

        checkListener();
        provider.upData(cart);
        showCount();//刷新价格
    }

    /**
     * 遍历购物车内的商品，如果有商品的checkBox没有被选中，则设选择全部为false
     * 如果数目形同则设为true
     */
    public void checkListener() {
        //购物车商品总数
        int count = 0;
        //以勾选的商品数目
        int checkNum = 0;
        if (mData != null) {
            count = mData.size();
            for (CartBean cart : mData) {
                if (!cart.isChecked()) {
                    mCheckboxAll.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }
            if (count == checkNum) {
                mCheckboxAll.setChecked(true);
            }
        }
    }

    /**
     * 全选到各选
     */
    public void checkAll() {

        mCheckboxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllListener(mCheckboxAll.isChecked());

            }
        });
    }

    public void checkAllListener(boolean isChecked) {
        if (isNull()) return;

        int i = 0;
        for (CartBean cart : mData) {
            cart.setChecked(isChecked);
            notifyItemChanged(i);
            i++;
//            provider.upData(cart);
            showCount();
        }

    }

//        if (mCheckboxAll.isChecked()) {
//            for (CartBean cart : mData) {
//                cart.setChecked(true);
//                provider.upData(cart);
//                notifyItemChanged(i);
//                i++;
//            }
//        } else {
//            for (CartBean cart : mData) {
//                cart.setChecked(false);
//                provider.upData(cart);
//                notifyItemChanged(i);
//                i++;
//
//}
//        }
//        showCount();


        /**
         * 删除购物车商品
         */

    public void deleteCart() {
        if (isNull())
            return;
        for (Iterator iterator = mData.iterator(); iterator.hasNext(); ) {
            CartBean cart = (CartBean) iterator.next();
            if (cart.isChecked()) {
                //获取位置
                int position = mData.indexOf(cart);
                provider.delete(cart);
                iterator.remove();

                notifyItemRemoved(position);
            }
        }
    }

}
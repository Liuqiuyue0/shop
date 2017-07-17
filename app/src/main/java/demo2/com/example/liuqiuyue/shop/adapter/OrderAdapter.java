package demo2.com.example.liuqiuyue.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.w4lle.library.NineGridAdapter;
import com.w4lle.library.NineGridlayout;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.bean.Order;
import demo2.com.example.liuqiuyue.shop.bean.OrderItem;

/**
 * Created by liuqiuyue on 2017/5/13.
 */

public class OrderAdapter extends SimpleAdapter<Order> {


    public OrderAdapter(Context context, List data) {
        super(context, data, R.layout.template_my_orders);


    }

    @Override
    public void bindData(BaseViewHolder holder, Order order) {
        holder.getTextView(R.id.txt_order_num).setText("订单号：" + order.getOrderNum());
        holder.getTextView(R.id.txt_order_money).setText("实付金额：" + order.getAmount());

        TextView txtStatus = holder.getTextView(R.id.txt_status);
        switch (order.getStatus()) {
            case Order.STATUS_SUCCESS:
                txtStatus.setText("成功");
                txtStatus.setTextColor(Color.parseColor("#ff4CAF50"));
                break;

            case Order.STATUS_PAY_FALL:
                txtStatus.setText("支付失败");
                txtStatus.setTextColor(Color.parseColor("#ffF44336"));
                break;

            case Order.STATUS_PAY_WAIT:
                txtStatus.setText("待付款");
                txtStatus.setTextColor(Color.parseColor("#ffFFEB3B"));
                break;
        }
        //NineGridlayout
        NineGridlayout nineGridlayout= (NineGridlayout) holder.getView(R.id.iv_ngrid_layout);
        nineGridlayout.setGap(5);  //图片间隔
        nineGridlayout.setDefaultWidth(50);  //设置单张图片时的宽度，默认 140 * density
        nineGridlayout.setDefaultHeight(50); //设置单张图片时的高度,默认 140 * density



    }


    /**
     * NineGridAdapter
     */
    public class OrderItemAdapter extends NineGridAdapter {

        private List<OrderItem> items;

        public OrderItemAdapter(Context context, List<OrderItem> items) {
            super(context, items);
            this.items=items;
        }

        @Override
        public int getCount() {
            return (items == null) ? 0 : items.size();
        }

        @Override
        public String getUrl(int position) {

            return getItem(position) == null ? null : ( getItem(position)).getWares().getImgUrl();
        }

        @Override
        public OrderItem getItem(int position) {
            return (items == null) ? null : items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return(items == null) ? 0 : items.get(position).getId();
        }

        @Override
        public View getView(int i, View view) {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
            Picasso.with(context).load(getUrl(i)).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(iv);
            return iv;
        }

    }
}


package demo2.com.example.liuqiuyue.shop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.DraweeView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by liuqiuyue on 2017/5/14.
 * 订单详情
 */

@ContentView(R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {
    @ViewInject(R.id.txt_address)
    private TextView mAddress;
    @ViewInject(R.id.drawee_view)
    private DraweeView image;
    @ViewInject(R.id.text_title)
    private TextView title;
    @ViewInject(R.id.text_price)
    private TextView price;
    @ViewInject(R.id.bt_order)
    private Button order;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);


    }


}

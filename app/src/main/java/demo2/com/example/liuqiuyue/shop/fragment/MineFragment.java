package demo2.com.example.liuqiuyue.shop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo2.com.example.liuqiuyue.shop.AddressListActivity;
import demo2.com.example.liuqiuyue.shop.Contants;
import demo2.com.example.liuqiuyue.shop.ContantsApplication;
import demo2.com.example.liuqiuyue.shop.FavoriteActivity;
import demo2.com.example.liuqiuyue.shop.LoginActivity;
import demo2.com.example.liuqiuyue.shop.OrderListActivity;
import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.bean.User;

import static android.content.ContentValues.TAG;


/**
 * Created by Ivan on 15/9/22.
 */
public class MineFragment extends BaseFragment {
    @ViewInject(R.id.mine_img)
    private ImageView mImg;
    @ViewInject(R.id.mine_username)
    private TextView mUserName;
    @ViewInject(R.id.mine_logout)
    private Button out;
    @ViewInject(R.id.mine_my_orders)
    private TextView mine_my_orders;
    @ViewInject(R.id.mine_address)
    private TextView mine_address;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        x.view().inject(this, view);

        return view;
    }

    public void init() {
        User user = ContantsApplication.getInstance().getUser();
        showUser(user);

    }

    /**
     * 点击我的地址，进入地址编辑页面
     *
     * @param view
     */
    @Event(value = R.id.mine_address, type = View.OnClickListener.class)
    private void toAddress(View view) {

        Intent intent = new Intent(getActivity(), AddressListActivity.class);

        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

    /**
     * 我的订单
     * @param view
     */
    @Event(value = R.id.mine_my_orders, type = View.OnClickListener.class)
    private void toOrder(View view) {
        Intent intent = new Intent(getActivity(), OrderListActivity.class);
        StartActivity(intent,true);
    }
    /**
     * 我的收藏
     * @param view
     */
    @Event(value = R.id.favorite, type = View.OnClickListener.class)
    private void toFavorite(View view) {
        Intent intent = new Intent(getActivity(), FavoriteActivity.class);
        StartActivity(intent,true);
    }


    /**
     * 点击“登录 ” 跳转到登录页面
     *
     * @param view
     */
    @Event(value = R.id.mine_username, type = View.OnClickListener.class)
    private void toLogin(View view) {

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        //改写的StartActivityForResult方法，登录时和没登录不同
        StartActivityForResult(intent, Contants.REQUEST_CODE);
    }

    /**
     * 接受LoginActivity的code
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        User user = ContantsApplication.getInstance().getUser();

        showUser(user);
    }

    @Event(value = R.id.mine_logout, type = View.OnClickListener.class)
    private void outLogin(View view) {
        ContantsApplication.getInstance().clearUser();
        showUser(null);
    }

    public void showUser(User user) {
        if (user != null) {
            mUserName.setText(user.getUsername());
//            mImg.setImageURI(Uri.parse(user.getLogo_url()));
            //图片
            Log.d(TAG, "showUser: " + user.getLogo_url().toString());
            Picasso.with(getContext()).load(user.getLogo_url()).into(mImg);
            out.setVisibility(View.VISIBLE);  //显示退出按钮

        } else {
            mUserName.setText(R.string.login);
            out.setVisibility(View.GONE);  //隐藏显示按钮
        }
    }
}

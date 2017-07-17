package demo2.com.example.liuqiuyue.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import demo2.com.example.liuqiuyue.shop.bean.Favorites;
import demo2.com.example.liuqiuyue.shop.bean.User;
import demo2.com.example.liuqiuyue.shop.bean.Wares;
import demo2.com.example.liuqiuyue.shop.http.OkHttpHelper;
import demo2.com.example.liuqiuyue.shop.http.SpotsCallback;
import demo2.com.example.liuqiuyue.shop.utils.CartProvider;
import demo2.com.example.liuqiuyue.shop.utils.ToastUtils;
import demo2.com.example.liuqiuyue.shop.widget.CtToolbar;
import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by liuqiuyue on 2017/4/27.
 * 商品详情：与web交互。完成商品分享，商品加入购物车，商品加入收藏
 */
@ContentView(R.layout.activity_ware_data)
public class WareDetailActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "WareDetailActivity";
    @ViewInject(R.id.wareData_web)
    private WebView mWebView;
    @ViewInject(R.id.wareData_toolbar)
    private CtToolbar toolbar;

    private Wares mWares;
    private WebAppInterface appInterface;
    private CartProvider cartProvider;
    //加载进度效果
    private SpotsDialog dialog;
    private OkHttpHelper helper = OkHttpHelper.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        //得到intent,接受
        Serializable serializable = getIntent().getSerializableExtra(Contants.WARE);
        if (serializable == null)
            this.finish();

        mWares = (Wares) serializable; //name一定要和HTML上的相同
        cartProvider = new CartProvider(this);
        dialog = new SpotsDialog(this, "正在加载");
        dialog.show();


        initToolbar();
        initWebView();
    }

    private void initWebView() {
        Log.d("web", "initWebView: ");
        WebSettings settings = mWebView.getSettings();
        //允许执行 JS脚本
        settings.setJavaScriptEnabled(true);
        //false才能加载页面上的图片，默认为true
        settings.setBlockNetworkImage(false);
        //允许有缓存
        settings.setAppCacheEnabled(true);
        //数据加载
        mWebView.loadUrl(Contants.API.WARES_DETAIL);
        appInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(appInterface, "appInterface");
        //加载方法：页面结束之后的操作方法
        mWebView.setWebViewClient(new WC());

    }

    private void initToolbar() {
        toolbar.setNavigationOnClickListener(this);
        Log.d("", "initToolbar: ");
        toolbar.getRightButton().setText("分享");
        toolbar.getRightButton().setVisibility(View.VISIBLE);
        toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

    }

    //分享
    private void showShare() {
        //初始化
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://shop.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWares.getName());
        // imagePath是图片的网络图片
        oks.setImageUrl(mWares.getImgUrl());


        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://shop.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(mWares.getName());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);   //销毁分享
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }

    class WC extends WebViewClient {
        //页面加载玩之后加载的方法
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

            appInterface.showDetail();
        }

    }

    //交互的接口
    class WebAppInterface {
        private Context mContext;

        public WebAppInterface(Context context) {
            this.mContext = context;
        }

        //android调用JS
        @JavascriptInterface
        public void showDetail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail(" + mWares.getId() + ")");
                }
            });
        }

        //JS调用Android
        @JavascriptInterface
        public void buy(long id) {
            addToFavorite();
        }

        //JS调用Android
        @JavascriptInterface
        public void addToCart(long id) {
            cartProvider.put(mWares);
            ToastUtils.show(mContext, "加入购物车成功");

        }

    }

    /**
     * 加入收藏的方法
     * 由于权限问题，网络将诶口不可用
     */
    private void addToFavorite() {

        User user = ContantsApplication.getInstance().getUser();

        if (user == null) {
            StartActivity(new Intent(this, LoginActivity.class), true);
        }


        Long userId = ContantsApplication.getInstance().getUser().getId();

        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("ware_id", mWares.getId());

        helper.post(Contants.API.FAVORITE_CREATE, params, new SpotsCallback<List<Favorites>>(this) {
            @Override
            public void onSuccess(Call call, Response response, List<Favorites> favorites) {
                ToastUtils.show(WareDetailActivity.this, "添加收藏成功");
            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {

                Log.d(TAG, "code:" + code);
            }
        });
    }
}

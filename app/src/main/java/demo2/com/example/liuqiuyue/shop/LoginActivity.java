package demo2.com.example.liuqiuyue.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo2.com.example.liuqiuyue.shop.bean.User;
import demo2.com.example.liuqiuyue.shop.http.OkHttpHelper;
import demo2.com.example.liuqiuyue.shop.http.SpotsCallback;
import demo2.com.example.liuqiuyue.shop.message.LoginRespMsg;
import demo2.com.example.liuqiuyue.shop.utils.DESUtil;
import demo2.com.example.liuqiuyue.shop.utils.ToastUtils;
import demo2.com.example.liuqiuyue.shop.widget.ClearEditText;
import demo2.com.example.liuqiuyue.shop.widget.CtToolbar;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liuqiuyue on 2017/4/30.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.login_toolbar)
    private CtToolbar toolbar;
    @ViewInject(R.id.login_user)
    private ClearEditText mUser;
    @ViewInject(R.id.login_pass)
    private ClearEditText mPassWord;
    @ViewInject(R.id.login_bt)
    private Button login_bt;
    @ViewInject(R.id.login_toReg)
    private TextView reg;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        login_bt.setOnClickListener(this);
        initToolBar();
        initRes();

    }

    private void initRes() {
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegActivity.class);
                StartActivity(intent,false);
            }
        });
    }


    private void initToolBar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    /**
     * 登录：判断输入的账户和密码，登录按钮的点击事件
     *
     * @param view
     */
    public void onClick(View view) {

        String user = mUser.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            ToastUtils.show(this, "请输入账号或手机号");
            return;
        }
        boolean result = isPhone(user);
        if (result == false) {
            Toast.makeText(LoginActivity.this, "请输入正确的账号或手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwd = mPassWord.getText().toString().trim();

        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, "请输入密码");
            return;
        }
        if (pwd.toString().length() <= 6) {
            ToastUtils.show(this, "请输入六位以上密码");
            return;
        }


        Map<String, Object> params = new HashMap<>(2);
        params.put("phone", user);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));  //密码要进行加密Key要与服务器上的Key一样
        okHttpHelper.post(Contants.API.LOGIN, params, new SpotsCallback<LoginRespMsg<User>>(this) {
            @Override
            public void onSuccess(Call call, Response response, LoginRespMsg<User> userLoginRespMsg) {
                //获取application对象
                ContantsApplication application = ContantsApplication.getInstance();
                //数据保存到本地
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                if (application.getIntent() == null) {
                    //成功后将requestCode设置为RESULT_OK
                    setResult(RESULT_OK);
                    finish();
                } else {
                    application.jumToTargetActivity(LoginActivity.this);  //执行intent后进行清空
                    finish();
                }

            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {
//                ToastUtils.show(LoginActivity.class,"账号和密码不符请重新输入");
            }
        });


    }

    /**
     * 判断电话号码是否符合格式.
     *
     * @param inputText the input text
     * @return true, if is phone
     */
    public static boolean isPhone(String inputText) {
        Pattern p = Pattern.compile("^((14[0-9])|(13[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

}

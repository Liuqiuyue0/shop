package demo2.com.example.liuqiuyue.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import demo2.com.example.liuqiuyue.shop.bean.User;
import demo2.com.example.liuqiuyue.shop.fragment.MineFragment;
import demo2.com.example.liuqiuyue.shop.http.OkHttpHelper;
import demo2.com.example.liuqiuyue.shop.http.SpotsCallback;
import demo2.com.example.liuqiuyue.shop.message.LoginRespMsg;
import demo2.com.example.liuqiuyue.shop.utils.CountTimerView;
import demo2.com.example.liuqiuyue.shop.utils.DESUtil;
import demo2.com.example.liuqiuyue.shop.utils.ToastUtils;
import demo2.com.example.liuqiuyue.shop.widget.ClearEditText;
import demo2.com.example.liuqiuyue.shop.widget.CtToolbar;
import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Response;

public class RegSecondActivity extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.toolbar)
    private CtToolbar mToolBar;

    @ViewInject(R.id.txtTip)
    private TextView mTxtTip;

    @ViewInject(R.id.btn_reSend)
    private Button mBtnResend;

    @ViewInject(R.id.edittxt_code)
    private ClearEditText mEtCode;

    private String phone;
    private String pwd;
    private String countryCode;


    private CountTimerView countTimerView;


    private SpotsDialog dialog;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private SMSEvenHanlder evenHanlder;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_second);
        x.view().inject(this);

        initToolBar();
        mBtnResend.setOnClickListener(this);

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);


        String text = getString(R.string.smssdk_send_mobile_detail) + formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));


        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();


//        SMSSDK.initSDK(this, ManifestUtil.getMetaDataValue(this, "1d80069f3c837"),
//                ManifestUtil.getMetaDataValue(this, "068c2cb3ed60504882fcb7212e412717"));
        SMSSDK.initSDK(this, "1d80069f3c837", "068c2cb3ed60504882fcb7212e412717");

        evenHanlder = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);

        dialog = new SpotsDialog(this);
        dialog = new SpotsDialog(this, "正在校验验证码");


    }


    private void initToolBar() {


        mToolBar.getRightButton().setVisibility(View.VISIBLE);
        mToolBar.getRightButton().setText("完成");
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitCode();

            }
        });


    }


    public void onClick(View v) {
        SMSSDK.getVerificationCode("+" + countryCode, phone);
        countTimerView = new CountTimerView(mBtnResend, R.string.smssdk_resend_identify_code);
        countTimerView.start();

        dialog.setMessage("正在重新获取验证码");
        dialog.show();
    }


    /**
     * 分割电话号码
     */

    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }


    private void submitCode() {

        String vCode = mEtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countryCode, phone, vCode);
        dialog.show();
    }


    private void doReg() {
        Log.d("", "doReg: ");
        Map<String, Object> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));

        okHttpHelper.post(Contants.API.REG, params, new SpotsCallback<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Call call, Response response, LoginRespMsg<User> userLoginRespMsg) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                Log.d("", "onSuccess: ");
                if (userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR) {
                    Log.d("", "shibai: ");
                    ToastUtils.show(RegSecondActivity.this, "注册失败:" + userLoginRespMsg.getMessage());
                    Intent intent = new Intent(RegSecondActivity.this, LoginActivity.class);
                    StartActivity(intent, false);
//                    startActivity(new Intent(RegSecondActivity.this, LoginActivity.class));
                    return;
                } else if (userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_SUCCESS) {
                    ContantsApplication application = ContantsApplication.getInstance();
                    application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                    StartActivity(new Intent(RegSecondActivity.this, MineFragment.class), true);
//                startActivity(new Intent(RegSecondActivity.this, MineFragment.class));
                    Log.d("", "chenggong: ");
                }
                finish();

            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {
                Log.d("", "token: ");
                super.onTokenError(response, code);
                ToastUtils.show(RegSecondActivity.this, "注册失败:" + response.code());
                StartActivity(new Intent(RegSecondActivity.this, LoginActivity.class), false);

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }

    class SMSEvenHanlder extends EventHandler {


        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {


            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");

                            ToastUtils.show(RegSecondActivity.this, "验证成功：" + phone + ",country:" + country);


                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();

                        }
                    } else {

                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }


                }
            });
        }
    }


}

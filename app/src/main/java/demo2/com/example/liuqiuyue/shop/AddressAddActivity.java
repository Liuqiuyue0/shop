package demo2.com.example.liuqiuyue.shop;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import demo2.com.example.liuqiuyue.shop.city.XmlParserHandler;
import demo2.com.example.liuqiuyue.shop.city.modle.CityModel;
import demo2.com.example.liuqiuyue.shop.city.modle.DistrictModel;
import demo2.com.example.liuqiuyue.shop.city.modle.ProvinceModel;
import demo2.com.example.liuqiuyue.shop.http.OkHttpHelper;
import demo2.com.example.liuqiuyue.shop.http.SpotsCallback;
import demo2.com.example.liuqiuyue.shop.message.BaseRespMsg;
import demo2.com.example.liuqiuyue.shop.widget.ClearEditText;
import demo2.com.example.liuqiuyue.shop.widget.CtToolbar;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liuqiuyue on 2017/5/3.
 * 添加地址页面，接口权限问题，无法实现
 */
@ContentView(R.layout.activity_address_add)
public class AddressAddActivity extends BaseActivity  {

    @ViewInject(R.id.address_toolbar)
    private CtToolbar toolbar;
    @ViewInject(R.id.addressList_recycle)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.address_txt)
    private TextView mAddressText;
    @ViewInject(R.id.address_phone)
    private ClearEditText mPhone;
    @ViewInject(R.id.address_consignee)
    private ClearEditText mConsignee;
    @ViewInject(R.id.address_add)
    private ClearEditText more_address;

    private List<ProvinceModel> mProvince = new ArrayList<>();
    private ArrayList<ArrayList<String>> mCities = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<>();

    private OptionsPickerView mProvincePikerView, mCitiesPikerView, mDistrictsPikerView;

    private OkHttpHelper helper=OkHttpHelper.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initToolbar();
        init();
    }

    private void init() {
        show();
        initProvinceData();
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        mCitiesPikerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String address = mProvince.get(options1).getName()
                        + mCities.get(options1).get(options2)
                        + mDistricts.get(options1).get(options2).get(options3);
                mAddressText.setText(address);   //显示在地址控件的位置
            }
        })
                .setTitleText("城市选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.GREEN)//设置分割线的颜色
                .setCyclic(false, false, false)  //
                .setSelectOptions(0, 0, 0)//默认选中项
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setBgColor(Color.BLACK)
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.YELLOW)
                .setSubmitColor(Color.YELLOW)
                .setTextColorCenter(Color.LTGRAY).setLabels("省", "市", "区")
                .build();

        //pvOptions.setSelectOptions(1,1);

//        mCitiesPikerView.setPicker(mProvince);//一级选择器
//        mCitiesPikerView.setPicker(mProvince, mCities);//二级选择器
        mCitiesPikerView.setPicker(mProvince, mCities, mDistricts);//三级选择器

    }

    private void initProvinceData() {
        AssetManager asses = getAssets();
        try {
            InputStream input = asses.open("province_data.xml");
            // 创建一个解析的对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获得解析出来的数据
            mProvince = handler.getDataList();

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
        if (mProvince != null) {
            for (ProvinceModel p : mProvince) {
                List<CityModel> citis = p.getCityList();
                ArrayList<String> cityStr = new ArrayList<>(citis.size());   //城市list

                for (CityModel c : citis) {
                    cityStr.add(c.getName());   //将城市名放入cityStr中

                    ArrayList<ArrayList<String>> dts = new ArrayList<>();  //地区list
                    List<DistrictModel> district = c.getDistrictList();
                    ArrayList<String> districtStr = new ArrayList<>(district.size());

                    for (DistrictModel d : district) {
                        districtStr.add(d.getName());//将地区名放到districtStr

                    }
                    dts.add(districtStr);
                    mDistricts.add(dts);
                }
                mCities.add(cityStr);
            }
        }

    }

    private void initToolbar() {
        toolbar.getRightButton().setVisibility(View.VISIBLE);
        toolbar.getRightButton().setText("完成");
        toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddress();
            }
        });

    }

    /**
     * 保存地址的方法
     */
    public void createAddress() {
        final String consignee=mConsignee.getText().toString();
        final String phone=mPhone.getText().toString();
        final String address=mAddressText.getText().toString()+more_address.getText().toString();

        Map<String,Object> params=new HashMap<>(1);
        params.put("user_id", ContantsApplication.getInstance().getUser().getId());
        params.put("consignee",consignee);
        params.put("phone",phone);
        params.put("addr",address);
        params.put("zip_code","000000");

        helper.post(Contants.API.ADDRESS_CREATE, params, new SpotsCallback<BaseRespMsg>(this) {


            @Override
            public void onSuccess(Call call, Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS){
                    Log.d("", "onSuccess: ");
                    setResult(RESULT_OK);
                    finish();

                }
                setResult(RESULT_OK);
                finish();
                Log.d("", "shibai: "+ContantsApplication.getInstance().getUser().getId()+consignee+phone+address);

            }

            @Override
            public void onError(Call call, Response response, int code, Exception e) {
                finish();
            }
        });


    }

    /**
     * 显示城市选择器
     */
    public void show(){
        mAddressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCitiesPikerView.show();
            }
        });
    }


}

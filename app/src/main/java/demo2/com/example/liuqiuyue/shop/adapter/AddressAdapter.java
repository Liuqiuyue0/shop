package demo2.com.example.liuqiuyue.shop.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.bean.Address;

/**
 * Created by liuqiuyue on 2017/5/3.
 * 地址适配器
 */

public class AddressAdapter extends SimpleAdapter<Address> {

    private AddressListener listener;

    public AddressAdapter(Context context, List<Address> data,AddressListener listener) {
        super(context, data, R.layout.template_address);
        this.listener=listener;
    }

    @Override
    public void bindData(BaseViewHolder holder, final Address address) {
        holder.getTextView(R.id.txt_name).setText(address.getConsignee());
        holder.getTextView(R.id.txt_phone).setText(address.getPhone());
        holder.getTextView(R.id.txt_address).setText(address.getAddr());

        CheckBox checkBox = (CheckBox) holder.getView(R.id.cb_is_defualt);
        boolean isDefault = address.getIsDefault();
        checkBox.setChecked(isDefault);
        if (isDefault) {
            checkBox.setText("默认地址");
        } else {
            checkBox.setChecked(true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && listener != null) {
                        address.setIsDefault(true);
                        listener.setDefault(address);
                    }
                }
            });
        }


    }

    /**
     * 将电话号码中间的四位隐藏用*代替
     * @param phone
     * @return
     */
    public String replacePhoneNum(String phone){

        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }

    public interface AddressListener {
        void setDefault(Address address);
    }
}

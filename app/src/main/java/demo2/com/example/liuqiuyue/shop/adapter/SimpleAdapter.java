package demo2.com.example.liuqiuyue.shop.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by liuqiuyue on 2017/4/14.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {


    public SimpleAdapter(Context context, List<T> data, int layoutResId) {
        super(context, data, layoutResId);
    }


}

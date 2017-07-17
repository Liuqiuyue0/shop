package demo2.com.example.liuqiuyue.shop.adapter;

import android.content.Context;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.bean.Category;

/**
 * Created by liuqiuyue on 2017/4/17.
 * Category分类商品的Adapter
 */

public class CategoryAdapter extends SimpleAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> data) {
        super(context, data, R.layout.template_category_list);
    }

    //控件的绑定
    @Override
    public void bindData(BaseViewHolder holder, Category category) {
       holder.getTextView(R.id.cat_list).setText(category.getName());
    }
}

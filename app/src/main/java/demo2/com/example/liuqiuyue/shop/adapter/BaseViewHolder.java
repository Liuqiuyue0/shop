package demo2.com.example.liuqiuyue.shop.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by liuqiuyue on 2017/4/14.
 * 泛型ViewHolder
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    protected BaseAdapter.OnItemClickListener listener;
    //数组 传入view数据
    private SparseArray<View> views;

    //绑定控件
    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        //实例化views
        views = new SparseArray<View>();
        this.listener=listener;
      itemView.setOnClickListener(this);
    }

    /**
     * 用于外部去调用的得到view的方法（public）
     *
     * @param id
     * @return
     */
    public View getView(int id) {
        return findView(id);
    }

    /**
     * TextView控件
     *
     * @param id
     * @return
     */
    public TextView getTextView(int id) {
        return findView(id);
    }

    /**
     * ImageView控件
     * @param id
     * @return
     */
    public ImageView getImageView(int id) {
        return findView(id);
    }

    /**
     * Button控件
     * @param id
     * @return
     */
    public Button getButton(int id) {
        return findView(id);
    }

    /**
     * 查找ID并绑定
     * @param id
     * @param <T> 用于对不同种ID进行强转
     * @return
     */
    private <T extends View> T findView(int id) {

        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        //得到的view进行强转类型
        return (T) view;
    }

    /***
     * 监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v,getLayoutPosition());
        }
    }
}

package demo2.com.example.liuqiuyue.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.List;

/**
 * Created by liuqiuyue on 2017/4/14.
 * Adapter的封装
 */

public abstract class BaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {
    //这里不用private
    protected List<T> mData;
    //用于view的绑定
    protected LayoutInflater mInflater;
    protected Context mContext;
    //view 的ID
    protected int LayoutResId;
    protected OnItemClickListener listener;

    /**
     * 自定义监听事件
     */
    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    /**
     * 自定义setOnClickListener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }


    /**
     * 构造方法
     *
     * @param context
     * @param data
     * @param layoutResId
     */
    public BaseAdapter(Context context, List<T> data, int layoutResId) {
        this.mContext = context;
        this.mData = data;
        this.LayoutResId = layoutResId;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     * 实现View
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(LayoutResId, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view, listener);
        return holder;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T t = getItem(position);
        bindData(holder, t);
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    //
    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * 用于定位当前最后一个数据
     *
     * @return
     */
    public List<T> getDatas() {
        if (mData != null)
            return mData;
        return null;
    }

    /**
     * 清数据
     */
    public void clearData() {
        if (mData==null||mData.size()<=0)
            return;
        for (Iterator it = mData.iterator(); it.hasNext();){

            T t = (T) it.next();
            //获取位置
            int position = mData.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }
    }

    /**
     * 刷新添加数据的方法
     * 方法之间的调用
     *
     * @param datas
     */
    public void addData(List<T> datas) {

        addData(0, datas);
    }

    /**
     * more加载更多时使用的添加方法
     *
     * @param position
     * @param datas
     */
    public void addData(int position, List<T> datas) {

        if (datas != null && datas.size() > 0) {
            for (T t : datas) {
                datas.add(position, t);
                notifyItemInserted(position);
            }
        }
    }

    /**
     * 更新数据
     */
    public void refreshData(List<T> list) {
        //清数据
//        clearData();
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                mData.add(i, list.get(i));
                notifyItemInserted(i);
            }
        }
    }

    public void loadMoreData(List<T> list) {
        if (list != null && list.size() > 0) {
            int size = list.size();
            int begin = mData.size();
            for (int i = 0; i < size; i++) {
                mData.add(list.get(i));
                notifyItemInserted(i + begin);
            }
        }
    }

    /**
     * 用于绑定数据的抽象方法
     */

    public abstract void bindData(BaseViewHolder holder, T t);

}

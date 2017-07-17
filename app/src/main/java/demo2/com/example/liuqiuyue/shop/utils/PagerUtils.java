package demo2.com.example.liuqiuyue.shop.utils;

import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo2.com.example.liuqiuyue.shop.bean.Page;
import demo2.com.example.liuqiuyue.shop.http.OkHttpHelper;
import demo2.com.example.liuqiuyue.shop.http.SpotsCallback;
import okhttp3.Call;
import okhttp3.Response;

public class PagerUtils {

    private static Builder builder;


    private OkHttpHelper httpHelper;
    //正常状态
    private static final int STATE_NORMAL = 0;
    //刷新状态
    private static final int STATE_REFRESH = 1;
    //加载更多
    private static final int STATE_MORE = 2;
    //当前状态
    private int state = STATE_NORMAL;


    /**
     * 私有构造方法，写一些不动的变量
     */
    private PagerUtils() {

        httpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();

    }

    //公开静态，用于实例化Builder
    public static Builder newBuilder() {

        builder = new Builder();
        return builder;
    }


    /**
     * 公开的请求数据的方法
     */
    public void request() {

        requestData();

    }

    /**
     * 公开的请求数据的方法
     */
    public void requestWare(long categoryId) {
        builder.categoryId = categoryId;
        requestData();
    }

    /**
     * 公开的更改数据的方法
     *
     * @param key
     * @param value
     */
    public void putParam(String key, Object value) {
        builder.params.put(key, value);

    }


    private void initRefreshLayout() {


        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
        //刷新和回调，setMaterialRefreshListener不是一个接口，是一个抽象类
        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
                refresh();
            }


            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if (builder.curPage < builder.totalCount) {
                    loadMore();
                } else {
                    ToastUtils.show(builder.mContext, "没有更多了");
                    materialRefreshLayout.finishRefreshLoadMore();
                    materialRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    /**
     * 获取数据的方法
     */
    private void requestData() {
        String url = buildUrl();
        httpHelper.get(url, new RequestCallBack(builder.mContext));
    }

    /**
     * 显示数据
     */
    private   <T> void showData(List<T> datas, int totalPage, int totalCount) {


        if (datas == null || datas.size() <= 0) {
            Toast.makeText(builder.mContext, "没有商品", Toast.LENGTH_LONG).show();
            return;
        }

        if (STATE_NORMAL == state) {

            if (builder.onPageListener != null) {
                builder.onPageListener.load(datas, totalPage, totalCount);
            }
        } else if (STATE_REFRESH == state) {
            builder.mRefreshLayout.finishRefresh();
            if (builder.onPageListener != null) {
                builder.onPageListener.refresh(datas, totalPage, totalCount);
            }

        } else if (STATE_MORE == state) {

            builder.mRefreshLayout.finishRefreshLoadMore();
            if (builder.onPageListener != null) {
                builder.onPageListener.loadMore(datas, totalPage, totalCount);
            }

        }
    }

    /**
     * 刷新的方法
     */
    private void refresh() {

        state = STATE_REFRESH;
        builder.curPage = 1;
        requestData();
        requestWare(builder.categoryId);
    }

    /**
     * 加载更多的方法
     */
    private void loadMore() {
        state = STATE_MORE;
        builder.curPage = ++builder.curPage;
        requestData();
        requestWare(builder.categoryId);
    }


    /**
     * 创建Url
     *
     * @return
     */
    private String buildUrl() {

        return builder.mUrl + "?" + buildUrlParams();
    }


    private String buildUrlParams() {


        HashMap<String, Object> map = builder.params;

        map.put("curPage", builder.curPage);
        map.put("pageSize", builder.pageSize);
        map.put("categoryId", builder.categoryId);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /**
     * 动的变量
     */
    public static class Builder {


        private Context mContext;
        private Type mType;
        private String mUrl;

        private MaterialRefreshLayout mRefreshLayout;

        private boolean canLoadMore;


        private int totalPage = 1;
        private int curPage = 1;
        private int pageSize = 10;
        private long categoryId = 1;
        private int totalCount;

        private HashMap<String, Object> params = new HashMap<>(5);

        private OnPageListener onPageListener;

        public Builder setUrl(String url) {

            builder.mUrl = url;

            return builder;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return builder;
        }
        public Builder setCurPage(int curPage) {
            this.curPage = curPage;
            return builder;
        }
        public Builder putParam(String key, Object value) {
            params.put(key, value);
            return builder;
        }

        public Builder setLoadMore(boolean loadMore) {
            this.canLoadMore = loadMore;
            return builder;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout) {
            this.mRefreshLayout = refreshLayout;
            return builder;
        }


        public Builder setOnPageListener(OnPageListener onPageListener) {
            this.onPageListener = onPageListener;
            return builder;
        }


        public PagerUtils build(Context context, Type type) {

            this.mType = type;
            this.mContext = context;

            valid();
            return new PagerUtils();
        }

        private void valid() {


            if (this.mContext == null)
                throw new RuntimeException("content can't be null");

            if (this.mUrl == null || "".equals(this.mUrl))
                throw new RuntimeException("url can't be  null");

            if (this.mRefreshLayout == null)
                throw new RuntimeException("MaterialRefreshLayout can't be  null");
        }

    }

    /**
     * RequestCallBack
     *
     * @param <T>
     */
    class RequestCallBack<T> extends SpotsCallback<Page<T>> {


        public RequestCallBack(Context context) {
            super(context);
            super.mType = builder.mType;  //设置type
        }

        @Override
        public void onSuccess(Call call, Response response, Page<T> page) {

            builder.curPage = page.getCurrentPage();
            builder.pageSize = page.getPageSize();
            builder.totalPage = page.getTotalPage();
            builder.totalCount = page.getTotalCount();
            showData(page.getList(), builder.totalPage, builder.totalCount);


        }

        @Override
        public void onFailure(Call call, IOException e) {
            ToastUtils.show(builder.mContext, "加载失败" + e.getMessage());
            if (STATE_REFRESH == state) {
                builder.mRefreshLayout.finishRefresh();
            } else if (STATE_MORE == state) {
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }

        @Override
        public void onError(Call call, Response response, int code, Exception e) {
            ToastUtils.show(builder.mContext, "接收失败" + e.getMessage());
            if (STATE_REFRESH == state) {
                builder.mRefreshLayout.finishRefresh();
            } else if (STATE_MORE == state) {
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }
    }

    public interface OnPageListener<T> {

        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas, int totalPage, int totalCount);

        void loadMore(List<T> datas, int totalPage, int totalCount);

    }


}

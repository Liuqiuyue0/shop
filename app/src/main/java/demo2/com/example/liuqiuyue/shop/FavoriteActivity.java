package demo2.com.example.liuqiuyue.shop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.adapter.FavoriteAdapter;
import demo2.com.example.liuqiuyue.shop.adapter.decoration.DividerGridItemDecoration;
import demo2.com.example.liuqiuyue.shop.bean.Favorites;
import demo2.com.example.liuqiuyue.shop.http.OkHttpHelper;
import demo2.com.example.liuqiuyue.shop.http.SpotsCallback;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liuqiuyue on 2017/5/15.
 * 我的收藏
 */
@ContentView(R.layout.activity_favorite)
public class FavoriteActivity extends BaseActivity {
    @ViewInject(R.id.recycle)
    private RecyclerView mRecyclerView;
    private FavoriteAdapter adapter;
    OkHttpHelper helper=OkHttpHelper.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
     getData();
    }

    public void getData() {
       helper.get(Contants.API.FAVORITE_LIST, new SpotsCallback<List<Favorites>>(this) {


           @Override
           public void onSuccess(Call call, Response response, List<Favorites> datas) {
               show(datas);
           }

           @Override
           public void onError(Call call, Response response, int code, Exception e) {

           }
       });
    }

    private void show(List<Favorites> datas) {
        adapter=new FavoriteAdapter(this,datas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mRecyclerView.setAdapter(adapter);


    }

}

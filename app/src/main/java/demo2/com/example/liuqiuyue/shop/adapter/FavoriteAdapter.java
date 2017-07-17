package demo2.com.example.liuqiuyue.shop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.bean.Favorites;
import demo2.com.example.liuqiuyue.shop.bean.Wares;

/**
 * Created by liuqiuyue on 2017/4/20.
 * 收藏 Adapter
 */

public class FavoriteAdapter extends  SimpleAdapter<Favorites> {


    public FavoriteAdapter(Context context, List<Favorites> data) {
        super(context, data,R.layout.template_favorite);
    }

    @Override
    public void bindData(BaseViewHolder holder, Favorites favorites) {
        Wares wares = favorites.getWares();
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        holder.getTextView(R.id.text_title).setText(wares.getName());
        holder.getTextView(R.id.text_price).setText("  "+wares.getPrice());

        Button buttonRemove =holder.getButton(R.id.btn_remove);
        Button buttonLike =holder.getButton(R.id.btn_like);

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}
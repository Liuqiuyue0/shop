package demo2.com.example.liuqiuyue.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import demo2.com.example.liuqiuyue.shop.R;
import demo2.com.example.liuqiuyue.shop.bean.Campaign;
import demo2.com.example.liuqiuyue.shop.bean.HomeCampaign;


/**
 * Created by liuqiuyue on 2017/4/8.
 * 首页分类信息适配器
 */

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHold> {

    //第一种样式
    private static int VIEW_TYPE_L = 0;
    //第二种样式
    private static int VIEW_TYPE_R = 1;
    //数据
    List<HomeCampaign> datas;
    //LayoutInflater
    private LayoutInflater minflater;
    private Context mContext;

    //声明listener
    private OnCampaignClickListener listener;

    /**
     * setOnClickListener
     */

    public void setOnClickListener(OnCampaignClickListener listener) {
        this.listener = listener;
    }

    public HomeCategoryAdapter(List<HomeCampaign> campaigns, Context context) {
        datas = campaigns;
        mContext = context;
    }

    /**
     * 加载布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        minflater = LayoutInflater.from(parent.getContext());
        View view1 = minflater.inflate(R.layout.template_home_cardview, parent, false);
        View view2 = minflater.inflate(R.layout.template_home_cardview2, parent, false);
        if (viewType == VIEW_TYPE_R) {
            return new ViewHold(view2);
        }
        return new ViewHold(view1);

    }

    /**
     * 添加数据带itemView中
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        HomeCampaign homeCampaign = datas.get(position);
        holder.textTitle.setText(homeCampaign.getTitle());
        //将图片下载缓存下来，在进行加载
        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);
    }

    /**
     * 得到数据的数目
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 两种分类样式交替变换
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return VIEW_TYPE_R;
        }
        return VIEW_TYPE_L;
    }


    /**
     * 自定义泛型
     */
    class ViewHold extends RecyclerView.ViewHolder implements  View.OnClickListener {
        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHold(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
            //绑定监听器
            imageViewBig.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HomeCampaign homeCampaign=datas.get(getLayoutPosition());
            if (listener!=null){
                switch (v.getId()){
                    case R.id.imgview_big:
                        listener.onClick(v,homeCampaign.getCpOne());
                        break;
                    case R.id.imgview_small_top:
                        listener.onClick(v,homeCampaign.getCpTwo());
                        break;
                    case R.id.imgview_small_bottom:
                        listener.onClick(v,homeCampaign.getCpThree());
                        break;
                }
            }
        }
    }

    /**
     * 自定义监听器 interface
     */
    public interface OnCampaignClickListener {
        void onClick(View view, Campaign campaign);
    }
}

package org.xdkitten.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xdkitten.zhihudaily.R;
import org.xdkitten.zhihudaily.entity.LastestPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanders on 2016/7/13.
 */
public class LastestAdapter extends RecyclerView.Adapter<LastestAdapter.LastestHolder> {


    private List<LastestPost> list=new ArrayList<LastestPost>();
    private LayoutInflater inflater;
    private Context context;

    //listener
    private OnRecyclerViewOnClickListener listener;

    //设置监听器
    public void setItemOnClickListener(OnRecyclerViewOnClickListener listener){
        this.listener=listener;
    }

    public static interface OnRecyclerViewOnClickListener {
        public void onItemClick(View view, int position);
    }



    //构造方法
    public LastestAdapter(Context context, List<LastestPost> list){
        this.inflater= LayoutInflater.from(context);
        this.list=list;
        this.context=context;
    }

    //创建新view,为LayoutManager所调用
    @Override
    public LastestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.normal_item,parent,false);
        LastestHolder holder=new LastestHolder(view);
        return holder;
    }

    //讲数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(LastestHolder holder, int position) {
        LastestPost item=list.get(position);
        holder.titleTextView.setText(item.getTitle());
        if(item.getFirstImg()==null){
            holder.itemImageView.setImageResource(R.drawable.no_img);
        }else {
            Glide.with(context)
                    .load(item.getFirstImg())
                    .error(R.drawable.no_img)
                    .centerCrop()
                    .into(holder.itemImageView);
        }

    }

    //获得Item的数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义ViewHolder，持有每个Item的所有界面元素
    public class LastestHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView titleTextView;
        public ImageView itemImageView;
        public CardView cardView;
        public LastestHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_normal_item_title);
            itemImageView = (ImageView) itemView.findViewById(R.id.iv_normal_item_image);
            cardView= (CardView) itemView.findViewById(R.id.card_view_normal_item);
        }

        @Override
        public void onClick(View v) {
            Log.i("info","LastestAdapter:onClick");
            if(listener!=null){
                listener.onItemClick(v,getLayoutPosition());
            }
        }
    }
}

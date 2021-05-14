package com.example.ouchaixun.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Response;

public class MixAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    //1-帖子 2-校友圈 3-新闻 0-没有 4-网络
    private List<Map<String, Object>> list;
    public Context context;
    public final int VIEW_NO = 0;
    public final int VIEW_TIE = 2;
    public final int VIEW_SCHOOLMATE = 3;
    public final int VIEW_NEWS = 1;
    public final int VIEW_NO_NET = 4;
    private long lastClickTime = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 2000;
    private boolean type;
    private int focus;
    private String email;
    private int month;
    private int day;
    private String token;

    public MixAdapter(Context context, List<Map<String, Object>> list){
        this.context = context;
        this.list = list;
        MyData myData = new MyData(context);
        token = myData.load_token();
        email=myData.load_email();

        Log.d("12332","制造成功");
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("12332",Integer.valueOf(list.get(position).get("type").toString())+"拓扑");
        return Integer.valueOf(list.get(position).get("type").toString());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Log.d("12332", viewType + "");
        if (viewType == VIEW_NO) {
            Log.d("12332", "no");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nofans, parent, false);
            return new NoViewHolder(view);
        } else if (viewType == VIEW_TIE) {
                Log.d("12332", "tie");
            view = LayoutInflater.from(context).inflate(R.layout.item_type_squar, parent, false);
            return new TieViewHolder(view);
        } else if (viewType == VIEW_SCHOOLMATE) {
            Log.d("12332", "school");
            view = LayoutInflater.from(context).inflate(R.layout.item_type_quan, parent, false);
            return new SchoolViewHolder(view);
        }  else if (viewType == VIEW_NEWS) {
            Log.d("12332", "news");
            view = LayoutInflater.from(context).inflate(R.layout.item_type_news, parent, false);
            return new NewsViewHolder(view);
        }else {
            Log.d("12332", "net");
            view = LayoutInflater.from(context).inflate(R.layout.item_papernonet, parent, false);
            return new NetViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoViewHolder) {
            NoViewHolder viewHolder = (NoViewHolder) holder;
            viewHolder.textView.setText(list.get(position).get("text").toString());
            Log.d("12332", "00");
        } else if(holder instanceof NewsViewHolder){
            NewsViewHolder viewHolder = (NewsViewHolder) holder;
            viewHolder.news_title.setText(list.get(position).get("title").toString());
            viewHolder.news_hint.setText(list.get(position).get("writer_nickname").toString());
            Log.d("12333",list.get(position).get("banner").toString()+"aaa");
            Glide.with(context).load(list.get(position).get("banner").toString()).centerCrop().into(viewHolder.news_img);
        }else if(holder instanceof  NetViewHolder){
            NetViewHolder viewHolder = (NetViewHolder) holder;

        }else if(holder instanceof  SchoolViewHolder){
            SchoolViewHolder viewHolder = (SchoolViewHolder) holder;
            viewHolder.circle_userName.setText(list.get(position).get("writer_nickname").toString());
            if(Integer.valueOf(list.get(position).get("comment_num").toString())>99){
                viewHolder.comments_num.setText("99+");
            }else{
                viewHolder.comments_num.setText(list.get(position).get("comment_num").toString());
            }
            if(Integer.valueOf(list.get(position).get("like_num").toString())>99){
                viewHolder.likes_num.setText("99+");
            }else{
                viewHolder.likes_num.setText(list.get(position).get("like_num").toString());
            }
            viewHolder.circleItem_content.setText(list.get(position).get("content").toString());
            Glide.with(context).load(list.get(position).get("writer_avatar").toString())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(viewHolder.circle_userHead);

        }else if(holder instanceof  TieViewHolder){
            TieViewHolder viewHolder = (TieViewHolder) holder;

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void myNotifyDataSetChange(){
        notifyDataSetChanged();
    }


    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView news_title;
        TextView news_hint;
        ImageView news_img;
        RelativeLayout news_layout;
        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            news_title = itemView.findViewById(R.id.news_title);
            news_hint = itemView.findViewById(R.id.news_hint);
            news_img = itemView.findViewById(R.id.news_img);
            news_layout = itemView.findViewById(R.id.news_layout);
        }
    }
    class NoViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        NoViewHolder(@NonNull View itemView) {
            super(itemView);
           textView = itemView.findViewById(R.id.textview567);
        }
    }
    class NetViewHolder extends RecyclerView.ViewHolder {

        NetViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    class SchoolViewHolder extends RecyclerView.ViewHolder {
        TextView circle_userName;
        TextView circleItem_content;
        ImageView circle_userHead;
        TextView comments_num;
        TextView likes_num;
        RelativeLayout circle_layout;
        SchoolViewHolder(@NonNull View itemView) {
            super(itemView);
            circle_userName = itemView.findViewById(R.id.circle_userName);
            circleItem_content = itemView.findViewById(R.id.circleItem_content);
            circle_userHead = itemView.findViewById(R.id.circle_userHead);
            comments_num = itemView.findViewById(R.id.comments_num);
            likes_num = itemView.findViewById(R.id.likes_num);
            circle_layout = itemView.findViewById(R.id.circle_layout);
        }
    }
    class TieViewHolder extends RecyclerView.ViewHolder {

        TieViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}

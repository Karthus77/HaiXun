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
import com.example.ouchaixun.Activity.CircleDetilsActivity;
import com.example.ouchaixun.Activity.NewsDetilsActivity;
import com.example.ouchaixun.Activity.SquareDetailsActivity;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
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
            viewHolder.news_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, NewsDetilsActivity.class);
                    intent.putExtra("id",Integer.valueOf(list.get(position).get("news_id").toString()));
                    context.startActivity(intent);
                }
            });
        }else if(holder instanceof  NetViewHolder){
            NetViewHolder viewHolder = (NetViewHolder) holder;

        }else if(holder instanceof  SchoolViewHolder){
            SchoolViewHolder viewHolder = (SchoolViewHolder) holder;
            viewHolder.circle_userName.setText(list.get(position).get("writer_nickname").toString());
            String time=list.get(position).get("release_time").toString();
            String N_time = time.substring(0,10);
            N_time = N_time+" ";
            N_time+=time.substring(10+1,10+6);
            viewHolder.circle_postTime.setText(N_time);
            viewHolder.circleItem_content.setText(list.get(position).get("content").toString());
            Glide.with(context).load(list.get(position).get("writer_avatar").toString())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(viewHolder.circle_userHead);
            Log.d("12333dd",list.get(position).get("talk_id").toString());
            viewHolder.circle_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context, CircleDetilsActivity.class);
                    intent.putExtra("id",list.get(position).get("talk_id").toString());
                    context.startActivity(intent);
                }
            });
        }else if(holder instanceof  TieViewHolder){
            TieViewHolder viewHolder = (TieViewHolder) holder;
            viewHolder.leixing.setText(list.get(position).get("tag").toString());
            viewHolder.hole_name.setText(list.get(position).get("writer_nickname").toString());
            viewHolder.hole_title.setText(list.get(position).get("title").toString());
            String time=list.get(position).get("release_time").toString();
            String N_time = time.substring(0,10);
            N_time = N_time+" ";
            N_time+=time.substring(11,10+6);
            viewHolder.hole_time.setText(N_time);
            Glide.with(context).load(list.get(position).get("writer_avatar").toString())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(viewHolder.hole_head);
            viewHolder.tiezi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context, SquareDetailsActivity.class);
                    intent.putExtra("id",list.get(position).get("post_id").toString());
                    context.startActivity(intent);
                }
            });
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
        RelativeLayout circle_layout;
        TextView circle_postTime;
        SchoolViewHolder(@NonNull View itemView) {
            super(itemView);
            circle_postTime= itemView.findViewById(R.id.circle_postTime);
            circle_userName = itemView.findViewById(R.id.circle_userName);
            circleItem_content = itemView.findViewById(R.id.circleItem_content);
            circle_userHead = itemView.findViewById(R.id.circle_userHead);
            circle_layout = itemView.findViewById(R.id.circle_layout);
        }
    }
    class TieViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout tiezi;
        TextView leixing;
        TextView hole_title;
        TextView hole_name;
        TextView hole_time;
        ImageView hole_head;
        TieViewHolder(@NonNull View itemView) {
            super(itemView);
            tiezi = itemView.findViewById(R.id.tiezi);
            leixing = itemView.findViewById(R.id.leixing);
            hole_title = itemView.findViewById(R.id.hole_title);
            hole_name = itemView.findViewById(R.id.hole_name);
            hole_time = itemView.findViewById(R.id.hole_time);
            hole_head = itemView.findViewById(R.id.hole_head);
        }
    }


}

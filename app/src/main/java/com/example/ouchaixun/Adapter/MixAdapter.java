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
    public final int VIEW_TIE = 1;
    public final int VIEW_SCHOOLMATE = 2;
    public final int VIEW_NEWS = 3;
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

        }else if(holder instanceof  NetViewHolder){

        }else if(holder instanceof  SchoolViewHolder){

        }else if(holder instanceof  TieViewHolder){

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

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);

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

        SchoolViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
    class TieViewHolder extends RecyclerView.ViewHolder {

        TieViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}

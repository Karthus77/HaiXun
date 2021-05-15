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
import com.example.ouchaixun.Activity.LoginActivity;
import com.example.ouchaixun.Activity.MainActivity;
import com.example.ouchaixun.Activity.MixActivity;
import com.example.ouchaixun.Activity.NewsDetilsActivity;
import com.example.ouchaixun.Activity.SquareDetailsActivity;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
            if(!list.get(position).get("typee").toString().equals("1")){
                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (System.currentTimeMillis() - lastClickTime >= FAST_CLICK_DELAY_TIME) {
                            type=true;
                            try {
                                int tp,idd;
                                if(list.get(position).get("typee").toString().equals("2")){
                                    idd=Integer.valueOf(list.get(position).get("history_id").toString());
                                    tp=7;
                                }else{
                                    idd= Integer.valueOf(list.get(position).get("news_id").toString());
                                    tp= 1;
                                }
                                MyData myData = new MyData(context);
                                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                                String requestBody ="{\r\n    \"id\":"+idd+",\r\n    \"type\":"+tp+"\r\n}";
                                Request request = new Request.Builder()
                                        .url("http://47.102.215.61:8888/whole/delete")
                                        .post(RequestBody.create(mediaType, requestBody))
                                        .addHeader("Authorization",myData.load_token())
                                        .build();
                                OkHttpClient okHttpClient = new OkHttpClient();
                                Log.d("1233d", request.toString() + "   " + requestBody.toString());
                                okHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.d("1233", "onFailure: " + e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        Log.d("1233", response.protocol() + " " + response.code() + " " + response.message());
                                        Headers headers = response.headers();
                                        String responseData = response.body().string();
                                        Log.d("12330",responseData);
                                        try {
                                            JSONObject jsonObject = new JSONObject(responseData);
                                            int code = jsonObject.getInt("code");
                                            final String msg = jsonObject.getString("msg");
                                            if(code==200){
                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show(); }
                                                });
                                                removeData(position);
                                            }else{
                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show(); }
                                                });
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        for (int i = 0; i < headers.size(); i++) {
                                            Log.d("1233", headers.name(i) + ":" + headers.value(i));
                                        }
                                        Log.d("1233", "onResponse: " + response.body());
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            lastClickTime = System.currentTimeMillis();
                        } else {
                            if (type){type=false;
                                Toast.makeText(context,"操作频繁，过一会再试吧！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }else{
                viewHolder.delete.setVisibility(View.INVISIBLE);
            }

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
            if(!list.get(position).get("typee").toString().equals("1")){
                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (System.currentTimeMillis() - lastClickTime >= FAST_CLICK_DELAY_TIME) {
                            type=true;
                            try {

                                int tp,idd;
                                if(list.get(position).get("typee").toString().equals("2")){
                                    idd=Integer.parseInt(list.get(position).get("history_id").toString());
                                    tp=7;
                                }else{
                                    idd= Integer.parseInt(list.get(position).get("talk_id").toString());
                                    tp= 3;
                                }
                                MyData myData = new MyData(context);
                                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                                String requestBody ="{\r\n    \"id\":"+idd+",\r\n    \"type\":"+tp+"\r\n}";
                                Request request = new Request.Builder()
                                        .url("http://47.102.215.61:8888/whole/delete")
                                        .post(RequestBody.create(mediaType, requestBody))
                                        .addHeader("Authorization",myData.load_token())
                                        .build();
                                OkHttpClient okHttpClient = new OkHttpClient();
                                Log.d("1233d", request.toString() + "   " + requestBody.toString());
                                okHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.d("1233", "onFailure: " + e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        Log.d("1233", response.protocol() + " " + response.code() + " " + response.message());
                                        Headers headers = response.headers();
                                        String responseData = response.body().string();
                                        Log.d("12330",responseData);
                                        try {
                                            JSONObject jsonObject = new JSONObject(responseData);
                                            int code = jsonObject.getInt("code");
                                            final String msg = jsonObject.getString("msg");
                                            if(code==200){
                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show(); }
                                                });
                                                removeData(position);
                                            }else{
                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show(); }
                                                });
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        for (int i = 0; i < headers.size(); i++) {
                                            Log.d("1233", headers.name(i) + ":" + headers.value(i));
                                        }
                                        Log.d("1233", "onResponse: " + response.body());
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            lastClickTime = System.currentTimeMillis();
                        } else {
                            if (type){type=false;
                                Toast.makeText(context,"操作频繁，过一会再试吧！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }else{
                viewHolder.delete.setVisibility(View.INVISIBLE);
            }
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
            if(!list.get(position).get("typee").toString().equals("1")){
                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (System.currentTimeMillis() - lastClickTime >= FAST_CLICK_DELAY_TIME) {
                            type=true;
                            try {

                                int tp,idd;
                                if(list.get(position).get("typee").toString().equals("2")){
                                    idd=Integer.parseInt(list.get(position).get("history_id").toString());
                                    tp=7;
                                }else{
                                    idd= Integer.parseInt(list.get(position).get("post_id").toString());
                                    tp= 2;
                                }
                                MyData myData = new MyData(context);
                                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                                String requestBody ="{\r\n    \"id\":"+idd+",\r\n    \"type\":"+tp+"\r\n}";
                                Request request = new Request.Builder()
                                        .url("http://47.102.215.61:8888/whole/delete")
                                        .post(RequestBody.create(mediaType, requestBody))
                                        .addHeader("Authorization",myData.load_token())
                                        .build();
                                OkHttpClient okHttpClient = new OkHttpClient();
                                Log.d("1233d", request.toString() + "   " + requestBody.toString());
                                okHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.d("1233", "onFailure: " + e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        Log.d("1233", response.protocol() + " " + response.code() + " " + response.message());
                                        Headers headers = response.headers();
                                        String responseData = response.body().string();
                                        Log.d("12330",responseData);
                                        try {
                                            JSONObject jsonObject = new JSONObject(responseData);
                                            int code = jsonObject.getInt("code");
                                            final String msg = jsonObject.getString("msg");
                                            if(code==200){
                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show(); }
                                                });
                                                removeData(position);
                                            }else{
                                                ((Activity)context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show(); }
                                                });
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        for (int i = 0; i < headers.size(); i++) {
                                            Log.d("1233", headers.name(i) + ":" + headers.value(i));
                                        }
                                        Log.d("1233", "onResponse: " + response.body());
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            lastClickTime = System.currentTimeMillis();
                        } else {
                            if (type){type=false;
                                Toast.makeText(context,"操作频繁，过一会再试吧！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }else{
                viewHolder.delete.setVisibility(View.INVISIBLE);
            }
            viewHolder.leixing.setText(list.get(position).get("tag").toString());
            Log.d("1233i", list.get(position).get("writer_nickname").toString());
            if(list.get(position).get("writer_nickname").toString().equals("该内容由匿名用户发布")){

            }else{
                viewHolder.hole_name.setText(list.get(position).get("writer_nickname").toString());
                Glide.with(context).load(list.get(position).get("writer_avatar").toString())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(viewHolder.hole_head);
            }
            viewHolder.hole_title.setText(list.get(position).get("title").toString());
            String time=list.get(position).get("release_time").toString();
            String N_time = time.substring(0,10);
            N_time = N_time+" ";
            N_time+=time.substring(11,10+6);
            viewHolder.hole_time.setText(N_time);

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
    public void removeData(int position) {
        list.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView news_title;
        TextView news_hint;
        ImageView news_img;
        RelativeLayout news_layout;
        ImageView delete;
        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            news_title = itemView.findViewById(R.id.news_title);
            news_hint = itemView.findViewById(R.id.news_hint);
            news_img = itemView.findViewById(R.id.news_img);
            news_layout = itemView.findViewById(R.id.news_layout);
            delete = itemView.findViewById(R.id.imageView24);
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
        ImageView delete;
        SchoolViewHolder(@NonNull View itemView) {
            super(itemView);
            circle_postTime= itemView.findViewById(R.id.circle_postTime);
            circle_userName = itemView.findViewById(R.id.circle_userName);
            circleItem_content = itemView.findViewById(R.id.circleItem_content);
            circle_userHead = itemView.findViewById(R.id.circle_userHead);
            circle_layout = itemView.findViewById(R.id.circle_layout);
            delete = itemView.findViewById(R.id.imageView24);
        }
    }

    class TieViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout tiezi;
        TextView leixing;
        TextView hole_title;
        TextView hole_name;
        TextView hole_time;
        ImageView hole_head;
        ImageView delete;
        TieViewHolder(@NonNull View itemView) {
            super(itemView);
            tiezi = itemView.findViewById(R.id.tiezi);
            leixing = itemView.findViewById(R.id.leixing);
            hole_title = itemView.findViewById(R.id.hole_title);
            hole_name = itemView.findViewById(R.id.hole_name);
            hole_time = itemView.findViewById(R.id.hole_time);
            hole_head = itemView.findViewById(R.id.hole_head);
            delete = itemView.findViewById(R.id.imageView24);
        }
    }

    public void removeList(int position){
        list.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据
    }

}

package com.example.ouchaixun.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.Activity.CircleDetilsActivity;
import com.example.ouchaixun.Activity.ReportCircleActivity;
import com.example.ouchaixun.Fragment.TimeAreaFragment;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
class Post_like{
    int id;
    int type;
    int action;
}

public class CircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String,Object>> list;
    private View inflater;
    private CircleShowAdapter circleShowAdapter;
    private static final int normal= 0;
    private static final int nonet = 1;
    private static final int nothing=2;
    private static final int ITEM_PAGER =0 ;
    private static final int ITEM_NEWS =1;
    private static final int ITEM_ERROR =2;
    private static final int ITEM_NO =3;
    private String token;

    public void changeLike(ImageView view)
    {
        if (view.getDrawable().getCurrent().getConstantState()==view.getResources().getDrawable(R.drawable.circle_like).getConstantState())
            view.setImageResource(R.drawable.islike);
        else
            view.setImageResource(R.drawable.circle_like);
    }
    public void setNumber(TextView textView,String num)
    {
        if(Integer.parseInt(num)>99)
        {
            textView.setText("99+");
        }
        else
        {
            textView.setText(num);
        }
    }

    public CircleAdapter(Context context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
        MyData myData = new MyData(context);
        token = myData.load_token();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==normal)
        {
            inflater= LayoutInflater.from(context).inflate(R.layout.item_alumnus,parent,false);
            RecyclerView.ViewHolder ViewHolder = new CircleAdapter.ViewHolder(inflater);
            return ViewHolder;}
        else if(viewType==nonet)
        {
            inflater= LayoutInflater.from(context).inflate(R.layout.item_nointernet,parent,false);
            RecyclerView.ViewHolder ViewHolder = new CircleAdapter.ViewHolder(inflater);
            return ViewHolder;
        }
        else
        {
            inflater= LayoutInflater.from(context).inflate(R.layout.item_nodata,parent,false);
            RecyclerView.ViewHolder ViewHolder = new CircleAdapter.ViewHolder(inflater);
            return ViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewholder = (ViewHolder) holder;
        int viewType = getItemViewType(position);
        if (viewType==normal)
        {
        String name=list.get(position).get("name").toString();
        final String content=list.get(position).get("content").toString();
        String time =list.get(position).get("time").toString();
        String comment=list.get(position).get("comment").toString();
        final String like=list.get(position).get("like").toString();
        String islike=list.get(position).get("islike").toString();
        final String id=list.get(position).get("id").toString();
        String head=list.get(position).get("head").toString();
        List<Map<String,Object>> piclist =new ArrayList<>();
        for(int i=0;i<list.get(position).size()-9;i++)
        {
            Map<String,Object> map=new HashMap<>();
            map.put("url",list.get(position).get("url"+i).toString());
            piclist.add(map);
        }
        viewholder.time.setText(time);
        setNumber(viewholder.comments,comment);
        setNumber(viewholder.likes,like);
        viewholder.content.setText(content);
        viewholder.name.setText(name);
        if(islike.equals("1"))
            viewholder.islike.setImageResource(R.drawable.islike);
        Glide.with(context).load(head).circleCrop().into(viewholder.head);
        circleShowAdapter=new CircleShowAdapter(context,piclist);
        GridLayoutManager manager=new GridLayoutManager(context,3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        viewholder.recyclerView.setLayoutManager(manager);
        viewholder.recyclerView.setAdapter(circleShowAdapter);
        viewholder.circle_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, CircleDetilsActivity.class);
                intent.putExtra("id",list.get(position).get("id").toString());
                context.startActivity(intent);
            }
        });
        viewholder.islike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson=new Gson();
                Post_like postLike=new Post_like();
                postLike.id=Integer.parseInt(id);
                postLike.type=1;
                if (viewholder.islike.getDrawable().getCurrent().getConstantState()==viewholder.islike.getResources().getDrawable(R.drawable.circle_like).getConstantState())
                { postLike.action=1;
                changeLike(viewholder.islike);
                setNumber(viewholder.likes,String.valueOf(Integer.parseInt(like)+1));}
                else
                { postLike.action=2;
                changeLike(viewholder.islike);
                setNumber(viewholder.likes,String.valueOf(Integer.parseInt(like)));}

                try {
                    OKhttpUtils.post_json(token, "http://47.102.215.61:8888/whole/like",gson.toJson(postLike) , new OKhttpUtils.OkhttpCallBack() {
                        @Override
                        public void onSuccess(Response response) throws IOException {
                            String responseData = response.body().string();
                        }
                        @Override
                        public void onFail(String error) {
                            Toast.makeText(context,"网络连接好像出问题了",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }else {
        }
    }


    @Override
    public int getItemViewType(int position) {
        int type=Integer.parseInt(list.get(position).get("type").toString());
        if (type==normal)
        {
            return normal;
        }
        else if (type==nonet)
        {
            return  nonet;
        }
        else
        {
            return nothing;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout circle_item;
        RecyclerView recyclerView;
        ImageView islike;
        ImageView head;
        TextView name;
        TextView content;
        TextView likes;
        TextView comments;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            islike=itemView.findViewById(R.id.circleItem_like);
            circle_item=itemView.findViewById(R.id.circle_item);
            recyclerView=itemView.findViewById(R.id.item_NinePictures);
            head=itemView.findViewById(R.id.circle_userHead);
            name=itemView.findViewById(R.id.circle_userName);
            content=itemView.findViewById(R.id.circleItem_content);
            likes=itemView.findViewById(R.id.likes_num);
            comments=itemView.findViewById(R.id.comments_num);
            time=itemView.findViewById(R.id.circle_postTime);
        }


    }
}

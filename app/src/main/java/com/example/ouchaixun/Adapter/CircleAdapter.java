package com.example.ouchaixun.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.Activity.CircleDetilsActivity;
import com.example.ouchaixun.Activity.ReportCircleActivity;
import com.example.ouchaixun.Fragment.TimeAreaFragment;
import com.example.ouchaixun.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String,Object>> list;
    private View inflater;
    private CircleShowAdapter circleShowAdapter;
    private static final int ITEM_PAGER =0 ;
    private static final int ITEM_NEWS =1;
    private static final int ITEM_ERROR =2;
    private static final int ITEM_NO =3;
    public CircleAdapter(Context context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater= LayoutInflater.from(context).inflate(R.layout.item_alumnus,parent,false);
        RecyclerView.ViewHolder ViewHolder = new CircleAdapter.ViewHolder(inflater);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewholder = (ViewHolder) holder;
        String name=list.get(position).get("name").toString();
        final String content=list.get(position).get("content").toString();
        String time =list.get(position).get("time").toString();
        String comment=list.get(position).get("comment").toString();
        String like=list.get(position).get("like").toString();
        String islike=list.get(position).get("islike").toString();
        String head=list.get(position).get("head").toString();
        List<Map<String,Object>> piclist =new ArrayList<>();
        for(int i=0;i<list.get(position).size()-8;i++)
        {
            Map<String,Object> map=new HashMap<>();
            map.put("url",list.get(position).get("url"+i).toString());
            piclist.add(map);
        }
        viewholder.time.setText(time);
        viewholder.comments.setText(comment);
        viewholder.likes.setText(like);
        viewholder.content.setText(content);
        viewholder.name.setText(name);
        Glide.with(context).load(list.get(position).get("head")).into(viewholder.head);
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
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout circle_item;
        RecyclerView recyclerView;
        ImageView head;
        TextView name;
        TextView content;
        TextView likes;
        TextView comments;
        TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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

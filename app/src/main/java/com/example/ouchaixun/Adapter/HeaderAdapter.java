package com.example.ouchaixun.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ouchaixun.Activity.NewsDetilsActivity;
import com.example.ouchaixun.Data.News;

import com.example.ouchaixun.R;

import java.util.List;

public class HeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<News> list;

    private static final int ITEM_NEWS = 2;
    private static final int ITEM_ERROR = 3;
    private static final int ITEM_NO = 4;
    public HeaderAdapter(Context context, List<News> list) {
        this.context = context;
        this.list = list;
    }



    public void addheader(List<News> addList) {
        if (addList != null) {
            list.addAll(addList);
            notifyItemRangeChanged(list.size() - addList.size(), addList.size());

        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view_news = null;
        RecyclerView.ViewHolder holder = null;

        if (i == ITEM_NO) {
            view_news = LayoutInflater.from(context).inflate(R.layout.item_news_header_no, viewGroup, false);
            holder = new NoHolder(view_news);
        }

        if (i == ITEM_NEWS) {
            view_news = LayoutInflater.from(context).inflate(R.layout.item_news_collection, viewGroup, false);
            holder = new Holder(view_news);
        }

        if (i == ITEM_ERROR) {
            view_news = LayoutInflater.from(context).inflate(R.layout.item_news_header_error, viewGroup, false);
            holder = new ErrorHolder(view_news);
        }
        assert holder != null;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int i) {



        if (holder instanceof Holder) {
            ((Holder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewsDetilsActivity.class);
                    intent.putExtra("id", list.get(i).getNews_id());
                    Log.i("asd", list.get(i).getNews_id() + "");
                    context.startActivity(intent);
                }
            });

//        ((Holder) holder).intop.setText("置顶");
//        ((Holder) holder).intop.setTextColor(context.getResources().getColor(R.color.orange));
//        ((Holder) holder).intop.setBackgroundResource(R.drawable.intop);
//        ((Holder) holder).official.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public int getItemViewType(int i) {


        if (list.get(i).getType()==0){
            Log.i("asd", "错误");
            return ITEM_ERROR;
        }else if (list.get(i).getType()==1){
            return ITEM_NO;
        }else {
            return ITEM_NEWS;
        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class ErrorHolder extends RecyclerView.ViewHolder {
        public ErrorHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public static class NoHolder extends RecyclerView.ViewHolder {
        public NoHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    public class Holder extends RecyclerView.ViewHolder {


        TextView intop,official;
        public Holder(@NonNull View itemView) {
            super(itemView);
            intop=itemView.findViewById(R.id.news_header_intop);
            official=itemView.findViewById(R.id.news_header_official);


        }
    }
}

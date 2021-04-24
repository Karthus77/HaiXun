package com.example.ouchaixun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ouchaixun.Data.News;

import com.example.ouchaixun.R;

import java.util.List;

public class HeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<News> list;

    public HeaderAdapter(Context context, List<News> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view_news = LayoutInflater.from(context).inflate(R.layout.item_news_collection, viewGroup, false);
        return (new HeaderAdapter.Holder(view_news)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {


        ((Holder) holder).intop.setText("置顶");
        ((Holder) holder).intop.setTextColor(context.getResources().getColor(R.color.orange));
        ((Holder) holder).intop.setBackgroundResource(R.drawable.intop);
        ((Holder) holder).official.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
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

package com.example.ouchaixun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ouchaixun.R;

import java.util.List;
import java.util.Map;

public class CircleCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String,Object>> list;
    private View inflater;
    public CircleCommentAdapter(Context context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater= LayoutInflater.from(context).inflate(R.layout.item_circle_comment,parent,false);
        ViewHolder viewHolder=new ViewHolder(inflater);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewholder = (ViewHolder) holder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView head;
        TextView time;
        TextView content;
        TextView name;
        public ViewHolder(View view) {
            super(view);
            head=view.findViewById(R.id.comment_userHead);
            time=view.findViewById(R.id.comment_time);
            content=view.findViewById(R.id.comment_content);
            name=view.findViewById(R.id.comment_name);


        }
    }
}

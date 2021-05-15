package com.example.ouchaixun.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.Activity.CircleDetilsActivity;
import com.example.ouchaixun.Activity.SquareDetailsActivity;
import com.example.ouchaixun.R;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SquareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String,Object>> list;
    private View inflater;
    private static final int normal= 0;
    private static final int nonet = 1;
    private static final int nothing=2;
    public SquareAdapter(Context context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==normal)
        {
        inflater= LayoutInflater.from(context).inflate(R.layout.item_wall,parent,false);
        RecyclerView.ViewHolder ViewHolder = new SquareAdapter.ViewHolder(inflater);
        return ViewHolder;}
        else if(viewType==nonet)
        {
            inflater= LayoutInflater.from(context).inflate(R.layout.item_nointernet,parent,false);
            RecyclerView.ViewHolder ViewHolder = new SquareAdapter.ViewHolder(inflater);
            return ViewHolder;
        }
        else
        {
            inflater= LayoutInflater.from(context).inflate(R.layout.item_nodata,parent,false);
            RecyclerView.ViewHolder ViewHolder = new SquareAdapter.ViewHolder(inflater);
            return ViewHolder;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final SquareAdapter.ViewHolder viewholder = (SquareAdapter.ViewHolder) holder;
        int viewType = getItemViewType(position);
        if (viewType == normal) {
            if (list.get(position).get("tag").equals("表白墙")) {
                viewholder.relativeLayout.setBackgroundResource(R.drawable.background_wall);
                viewholder.head.setImageResource(R.drawable.anonymous_wall);
            } else if (list.get(position).get("tag").equals("树洞")) {
                viewholder.relativeLayout.setBackgroundResource(R.drawable.background_tree);
                viewholder.head.setImageResource(R.drawable.anonymous_tree);
            } else if (list.get(position).get("tag").equals("吐槽")) {
                viewholder.relativeLayout.setBackgroundResource(R.drawable.background_bullshit);
                viewholder.head.setImageResource(R.drawable.anonymous_bullshit);
            } else if (list.get(position).get("tag").equals("失物招领")) {
                viewholder.relativeLayout.setBackgroundResource(R.drawable.background_car);
                viewholder.head.setImageResource(R.drawable.anonymous_car);
            } else {
                viewholder.relativeLayout.setBackgroundResource(R.drawable.background_table);
                viewholder.head.setImageResource(R.drawable.anonymous_table);
            }
            viewholder.title.setText(list.get(position).get("title").toString());
            viewholder.time.setText(list.get(position).get("time").toString());
            viewholder.name.setText(list.get(position).get("name").toString());
            if (Objects.requireNonNull(list.get(position).get("anonymous")).toString().equals("2")) {
                Glide.with(context).load(list.get(position).get("head")).circleCrop().into(viewholder.head);
            }
            viewholder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SquareDetailsActivity.class);
                    intent.putExtra("id", list.get(position).get("id").toString());
                    context.startActivity(intent);
                }
            });
        }
        else
        {
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout relativeLayout;
        private ImageView head;
        private TextView name;
        private TextView title;
        private TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.re_wall);
            head=itemView.findViewById(R.id.wall_head);
            name=itemView.findViewById(R.id.wall_name);
            title=itemView.findViewById(R.id.wall_title);
            time=itemView.findViewById(R.id.wall_time);

        }
    }
}

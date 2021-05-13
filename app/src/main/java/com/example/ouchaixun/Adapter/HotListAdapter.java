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

public class HotListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String,Object>> list;
    private View inflater;
    public HotListAdapter(Context context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater= LayoutInflater.from(context).inflate(R.layout.item_hotlist,parent,false);
        RecyclerView.ViewHolder ViewHolder = new HotListAdapter.ViewHolder(inflater);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final HotListAdapter.ViewHolder viewholder = (HotListAdapter.ViewHolder) holder;
            if (position==0)
            {
                viewholder.rank.setImageResource(R.drawable.circle_rank1);
            }
            else if(position==1)
            {
                viewholder.rank.setImageResource(R.drawable.circle_rank2);
            }
            else if(position==2)
            {
                viewholder.rank.setImageResource(R.drawable.circle_rank3);
            }
            else
            {
                viewholder.rank.setVisibility(View.INVISIBLE);
            }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout relativeLayout;
        private ImageView cover;
        private TextView title;
        private ImageView rank;
        private TextView click;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.hot_re);
            cover=itemView.findViewById(R.id.first_pic);
            title=itemView.findViewById(R.id.hot_title);
            rank=itemView.findViewById(R.id.hot_rank);
            click=itemView.findViewById(R.id.hot_click_nums);

        }
    }
}

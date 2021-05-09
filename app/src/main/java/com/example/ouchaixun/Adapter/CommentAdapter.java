package com.example.ouchaixun.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ouchaixun.Data.News;
import com.example.ouchaixun.Data.SquareComment;
import com.example.ouchaixun.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Context context;
    private List<SquareComment> list;
    private static final int ITEM_HEADER = 0;
    private static final int ITEM_COMMENT = 1;
    private static final int ITEM_ERROR = 2;
    private static final int ITEM_NO = 3;


    public CommentAdapter(Context context, List<SquareComment> list) {
        this.context = context;
        this.list = list;

    }

    public void addData(List<SquareComment> addList) {
        if (addList != null) {
            list.addAll(addList);
            notifyItemRangeChanged(list.size() - addList.size(), addList.size());

        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemview = null;
        RecyclerView.ViewHolder holder = null;

        if (i == ITEM_NO) {
            itemview = LayoutInflater.from(context).inflate(R.layout.item_nodata, viewGroup, false);
            holder = new NoHolder(itemview);
        }
        if (i == ITEM_HEADER) {
            itemview = LayoutInflater.from(context).inflate(R.layout.item_square_header, viewGroup, false);
            holder = new HeaderHolder(itemview);
        }
        if (i == ITEM_COMMENT) {
            itemview = LayoutInflater.from(context).inflate(R.layout.item_square_comment, viewGroup, false);
            holder = new CommentHolder(itemview);
        }
        if (i == ITEM_ERROR) {
            itemview = LayoutInflater.from(context).inflate(R.layout.item_nointernet, viewGroup, false);
            holder = new ErrorHolder(itemview);
        }
        assert holder != null;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        //空白页
        if (holder instanceof NewsAdapter.NoHolder) {
        }
    }


    @Override
    public int getItemViewType(int i) {


        if (list.get(i).getTypes()==0){
            Log.i("asd", "错误");
            return ITEM_ERROR;
        }else if (list.get(i).getTypes()==1){
            return ITEM_NO;
        }else if (list.get(i).getTypes()==2){
            return ITEM_HEADER;
        }else {
            return ITEM_COMMENT;
        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public static class CommentHolder extends RecyclerView.ViewHolder {
        public CommentHolder(@NonNull View itemView) {
            super(itemView);

        }
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


}




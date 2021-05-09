package com.example.ouchaixun.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import com.example.ouchaixun.Data.SquareComment;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyGridView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private Context context;
    private List<SquareComment> list;
    private GridViewAdapter gridAdpter;
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
            holder = new sHeaderHolder(itemview);
        }
        if (i == ITEM_COMMENT) {
            itemview = LayoutInflater.from(context).inflate(R.layout.item_square_comment, viewGroup, false);
            holder = new sCommentHolder(itemview);
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
        if (holder instanceof NoHolder) {
        }
        if (holder instanceof sHeaderHolder) {
            ((sHeaderHolder)holder).writer.setText(list.get(i).getWriter_nickname());
            ((sHeaderHolder)holder).time.setText((list.get(i).getTime()).substring(0,10));
            ((sHeaderHolder)holder).title.setText(list.get(i).getTitles());
            ((sHeaderHolder)holder).content.setText(list.get(i).getContents());

            if (list.get(i).getPic_list()!=null){
                gridAdpter = new GridViewAdapter(context,list.get(i).getPic_list());
                ( (sHeaderHolder)holder).gridView.setAdapter(gridAdpter);}
            Glide.with(context)
                    .load("http://47.102.215.61:8888/" +list.get(i).getWriter_avatar())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .error(R.drawable.img_error)
                    .into( ((sHeaderHolder)holder).photo);
        }
        if (holder instanceof sCommentHolder) {

            ((sCommentHolder)holder).sender.setText(list.get(i).getSender_nickname());
            ((sCommentHolder)holder).time.setText((list.get(i).getTime()).substring(0,10));
            ((sCommentHolder)holder).liek_num.setText(list.get(i).getLike_num()+"");
            ((sCommentHolder)holder).comment.setText(list.get(i).getContents());
            Glide.with(context)
                    .load("http://47.102.215.61:8888/" +list.get(i).getSender_avatar())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .error(R.drawable.img_error)
                    .into( ((sCommentHolder)holder).photo);
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



    public static class sHeaderHolder extends RecyclerView.ViewHolder {

        private TextView title,content,time,writer;
        private ImageView photo;
        private MyGridView gridView;
        public sHeaderHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.square_title);
            content=itemView.findViewById(R.id.square_content);
            time=itemView.findViewById(R.id.square_time);
            writer=itemView.findViewById(R.id.square_writer);
            photo=itemView.findViewById(R.id.square_photo);
            gridView=itemView.findViewById(R.id.gridview);

        }
    }
    public static class sCommentHolder extends RecyclerView.ViewHolder {
        private TextView sender,time,liek_num,comment;
        private ImageView photo;
        private Button like;
        public sCommentHolder(@NonNull View itemView) {
            super(itemView);
            sender=itemView.findViewById(R.id.square_comment_writer);
            time=itemView.findViewById(R.id.square_comment_time);
            liek_num=itemView.findViewById(R.id.square_comment_likenum);
            comment=itemView.findViewById(R.id.square_comment);
            photo=itemView.findViewById(R.id.square_comment_photo);
            like=itemView.findViewById(R.id.square_comment_like);

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




package com.example.ouchaixun.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import com.example.ouchaixun.Activity.CircleDetilsActivity;
import com.example.ouchaixun.Activity.SquareDetailsActivity;
import com.example.ouchaixun.Data.Message;
import com.example.ouchaixun.Data.SquareComment;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.InputTextMsgDialog;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.MyGridView;
import com.example.ouchaixun.Utils.OKhttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Response;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private int passage_id;
    private Context context;
    private List<Message> list;
    private GridViewAdapter gridAdpter;
    private static final int ITEM_HEADER = 0;

    private static final int ITEM_ERROR = 2;
    private static final int ITEM_NO = 3;
    //全局定义
    private long lastClickTime = 0L;
    // 两次点击间隔不能少于1000ms
    private static final int FAST_CLICK_DELAY_TIME = 1000;


    public MessageAdapter(Context context, List<Message> list) {
        this.context = context;
        this.list = list;


    }

    public void addData(List<Message> addList) {
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
            itemview = LayoutInflater.from(context).inflate(R.layout.item_nocomment, viewGroup, false);
            holder = new NoHolder(itemview);
        }
        if (i == ITEM_HEADER) {
            itemview = LayoutInflater.from(context).inflate(R.layout.item_message, viewGroup, false);
            holder = new sHeaderHolder(itemview);
        }

        if (i == ITEM_ERROR) {
            itemview = LayoutInflater.from(context).inflate(R.layout.item_nointernet, viewGroup, false);
            holder = new ErrorHolder(itemview);
        }
        assert holder != null;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        //空白页

        if (holder instanceof sHeaderHolder) {

            ((sHeaderHolder)holder).time.setText(list.get(i).getTimes());
            ((sHeaderHolder)holder).writer.setText(list.get(i).getSender_nickname());
            ((sHeaderHolder)holder).content.setText(list.get(i).getPost_title());
            Glide.with(context)
                    .load(list.get(i).getAvatar())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into( ((sHeaderHolder)holder).photo);

            ((sHeaderHolder)holder).delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeList(i);
                }
            });

            ((sHeaderHolder)holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int q=list.get(i).getMsg_type();
                    Log.i("asd",q+"");
                    if (q==1||q==2||q==6||q==8){

                        Intent intent = new Intent(context, SquareDetailsActivity.class);
                        intent.putExtra("id", String.valueOf(list.get(i).getPost_id()));
                        context.startActivity(intent);

                    }else {
                        Intent intent = new Intent(context, CircleDetilsActivity.class);
                        intent.putExtra("id", String.valueOf(list.get(i).getPost_id()));
                        context.startActivity(intent);
                    }
                }
            });

        }

    }


    @Override
    public int getItemViewType(int i) {


        if (list.get(i).getTypes()==0){
            Log.i("asd", "错误");
            return ITEM_ERROR;
        }else if (list.get(i).getTypes()==1){
            return ITEM_NO;
        }else {
            return ITEM_HEADER;
        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class sHeaderHolder extends RecyclerView.ViewHolder {

        private TextView content,time,writer;
        private ImageView photo;
        private Button delete;
        private RelativeLayout relativeLayout;

        public sHeaderHolder(@NonNull View itemView) {
            super(itemView);
            content=itemView.findViewById(R.id.item_message_content);
            time=itemView.findViewById(R.id.item_message_time);
            writer=itemView.findViewById(R.id.item_message_name);
            photo=itemView.findViewById(R.id.item_message_img);
            delete=itemView.findViewById(R.id.btnDelete);
            relativeLayout=itemView.findViewById(R.id.message_layout);
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


    public void removeList(int position){
        list.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据







    }

}


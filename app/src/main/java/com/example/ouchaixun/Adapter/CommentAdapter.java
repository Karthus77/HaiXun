package com.example.ouchaixun.Adapter;

import android.app.Activity;
import android.content.Context;
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

import com.example.ouchaixun.Activity.SquareDetailsActivity;
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

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private int passage_id;
    private Context context;
    private List<SquareComment> list;
    private GridViewAdapter gridAdpter;
    private static final int ITEM_HEADER = 0;
    private static final int ITEM_COMMENT = 1;
    private static final int ITEM_ERROR = 2;
    private static final int ITEM_NO = 3;


    public CommentAdapter(Context context, List<SquareComment> list,int passage_id) {
        this.context = context;
        this.list = list;
        this.passage_id=passage_id;

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
            itemview = LayoutInflater.from(context).inflate(R.layout.item_nocomment, viewGroup, false);
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {
        //空白页
        if (holder instanceof NoHolder) {
        }
        if (holder instanceof sHeaderHolder) {
           // passage_id=list.get(i).getPassage_id();
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
            int islike=list.get(i).getIs_like();
            if (islike==1){
                ((sCommentHolder)holder).like.setBackgroundResource(R.drawable.like_fill);
                Log.i("qwe","asd");
                ((sCommentHolder)holder).like.setClickable(false);
            }
            if (list.get(i).getIs_reply()==1){

                ((sCommentHolder)holder).relativeLayout.setVisibility(View.VISIBLE);

                ((sCommentHolder)holder).reply_writer.setText(list.get(i).getReply_nickname());

                ((sCommentHolder)holder).reply_content.setText(list.get(i).getReply_content());
                Glide.with(context)
                        .load("http://47.102.215.61:8888/" +list.get(i).getReply_avatar())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .error(R.drawable.img_error)
                        .into( ((sCommentHolder)holder).reply_img);

            }






            ((sCommentHolder)holder).like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyData myData = new MyData(context);
                    final String token = myData.load_token();

                    String json="{\"type\": 3,\"id\": "+list.get(i).getId()+",\"action\": 1}";
                    try {
                        OKhttpUtils.post_json(token, "http://47.102.215.61:8888/whole/like", json, new OKhttpUtils.OkhttpCallBack() {
                            @Override
                            public void onSuccess(Response response)  {
                                try {
                                    JSONObject jsonObject=new JSONObject(response.body().string());

                                    Log.i("asd",jsonObject.getString("msg"));

                                    if (jsonObject.getString("msg").equals("点赞成功")){

                                        ((Activity)context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((sCommentHolder)holder).like.setBackgroundResource(R.drawable.like_fill);
                                                ((sCommentHolder)holder).like.setClickable(false);
                                                ((sCommentHolder)holder).liek_num.setText(list.get(i).getLike_num()+1+"");
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFail(String error) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


            ((sCommentHolder)holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean onLongClick(View v) {


                    MyData myData = new MyData(context);
                    final String token = myData.load_token();
                    final InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
                    inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                        @Override
                        public void onTextSend(String msg) {
                            //点击发送按钮后，回调此方法，msg为输入的值
                            final String json= " { \"content\":"+msg+" ,\"type\":2 ,\"id\":"+passage_id+",\"original_com_id\":"+list.get(i).getId()+" }";

                            Log.i("asd",json);
                            try {
                                OKhttpUtils.post_json(token, "http://47.102.215.61:8888/whole/comment", json, new OKhttpUtils.OkhttpCallBack() {
                                    @Override
                                    public void onSuccess(Response response)  {

                                        try {
                                            final JSONObject jsonObject=new JSONObject(Objects.requireNonNull(response.body()).string());

                                            final String msg=jsonObject.getString("msg");

                                            Log.i("asd",msg);
                                            ((Activity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (msg.equals("发布成功")){
                                                        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                                                        inputTextMsgDialog.clearText();
                                                        inputTextMsgDialog.dismiss();


                                                    }

                                                }
                                            });

                                            Log.i("asdf",jsonObject.toString());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onFail(String error) {

                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
                    //设置评论字数
                    inputTextMsgDialog.setMaxNumber(50);
                    inputTextMsgDialog .show();

                    return true;
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
        private TextView sender,time,liek_num,comment,reply_writer,reply_content;
        private ImageView photo,reply_img;
        private Button like;
        private RelativeLayout relativeLayout;

        public sCommentHolder(@NonNull View itemView) {
            super(itemView);
            sender=itemView.findViewById(R.id.square_comment_writer);
            time=itemView.findViewById(R.id.square_comment_time);
            liek_num=itemView.findViewById(R.id.square_comment_likenum);
            comment=itemView.findViewById(R.id.square_comment);
            photo=itemView.findViewById(R.id.square_comment_photo);
            like=itemView.findViewById(R.id.square_comment_like);
            reply_writer=itemView.findViewById(R.id.square_reply_writer);
            reply_content=itemView.findViewById(R.id.square_reply_content);
            reply_img=itemView.findViewById(R.id.square_reply_img);
            relativeLayout=itemView.findViewById(R.id.square_reply_layout);

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




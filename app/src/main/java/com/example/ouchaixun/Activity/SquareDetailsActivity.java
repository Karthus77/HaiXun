package com.example.ouchaixun.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.ouchaixun.Adapter.CommentAdapter;
import com.example.ouchaixun.Adapter.NewsAdapter;
import com.example.ouchaixun.Data.News;
import com.example.ouchaixun.Data.SquareComment;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.InputTextMsgDialog;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class SquareDetailsActivity extends AppCompatActivity {
    private String token,photo;
    private int page=1,old_page=1,refresh_num=0;

    private int passage_id=3;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private CommentAdapter adapter;

    private Button tostar;
    private boolean star=false,old_star=false;


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (star!=old_star){
            int star_type;
            if (star){
                star_type=1;
            }else {     star_type=2;}
            String json="{\"type\": 2,\"id\": "+passage_id+",\"action\": "+star_type+"}";
            Log.i("asd",json);
            try {
                OKhttpUtils.post_json(token, "http://47.102.215.61:8888/whole/star", json, new OKhttpUtils.OkhttpCallBack() {
                    @Override
                    public void onSuccess(Response response)  {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            Log.i("asd",jsonObject.toString());
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

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_square_details);

        tostar=findViewById(R.id.square_btn_star);
        ImageView myPhoto=findViewById(R.id.square_myPhoto);
        recyclerView = findViewById(R.id.square_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(SquareDetailsActivity.this));
        smartRefreshLayout=findViewById(R.id.square_smartRefreshLayout);

        MyData myData = new MyData(SquareDetailsActivity.this);
        token = myData.load_token();
        photo=myData.load_pic_url();


        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                List<SquareComment> list=new ArrayList<>();
                GetComment(list);
                smartRefreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                refresh_num++;
                page=1;old_page=1;
                smartRefreshLayout.setEnableLoadMore(true);
                List<SquareComment> list=new ArrayList<>();
                GetData(list);
                smartRefreshLayout.finishRefresh();
            }
        });






        Glide.with(SquareDetailsActivity.this)
                .load(photo)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .error(R.drawable.img_error)
                .into(myPhoto);

        final List<SquareComment> list=new ArrayList<>();
        GetData(list);





        //评论
        findViewById(R.id.comment_add).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(SquareDetailsActivity.this, R.style.dialog_center);
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        //点击发送按钮后，回调此方法，msg为输入的值
                        final String json= " { \"content\":"+msg+" ,\"type\":2 ,\"id\":"+passage_id+" }";

                        try {
                            OKhttpUtils.post_json(token, "http://47.102.215.61:8888/whole/comment", json, new OKhttpUtils.OkhttpCallBack() {
                                @Override
                                public void onSuccess(Response response)  {

                                    try {
                                        final JSONObject jsonObject=new JSONObject(response.body().string());

                                        final String msg=jsonObject.getString("msg");

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (msg.equals("发布成功")){
                                                    Toast.makeText(SquareDetailsActivity.this,msg,Toast.LENGTH_SHORT).show();
                                                    inputTextMsgDialog.clearText();
                                                    inputTextMsgDialog.dismiss();
                                                    refresh_num++;
                                                    page=1;old_page=1;
                                                    smartRefreshLayout.setEnableLoadMore(true);
                                                    List<SquareComment> list=new ArrayList<>();
                                                    GetData(list);
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
            }
        });

        tostar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (star){
                        star=false;
                        tostar.setBackgroundResource(R.drawable.collection_no);
                }else {
                    star=true;
                    tostar.setBackgroundResource(R.drawable.collect);
                }
            }
        });

        findViewById(R.id.square_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void GetData(final List<SquareComment> list){
        OKhttpUtils.get_token(token, "http://47.102.215.61:8888/passage/"+passage_id+"/detail?page=1", new OKhttpUtils.OkhttpCallBack() {
            @SuppressLint("ResourceType")
            @Override
            public void onSuccess(Response response)  {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());

                    Log.i("asddd",jsonObject.toString());
                    JSONObject jsonObject1=jsonObject.optJSONObject("data");
                    SquareComment data=new SquareComment();
                    data.setPassage_id(passage_id);
                    data.setTitles(jsonObject1.getString("title"));
                    data.setContents(jsonObject1.getString("content"));
                    data.setTag(jsonObject1.getString("tag"));
                    data.setTime(jsonObject1.getString("release_time"));
                    data.setWriter_nickname(jsonObject1.getString("writer_nickname"));
                    data.setWriter_avatar(jsonObject1.getString("writer_avatar"));

                    old_page=jsonObject1.getInt("num_pages");
                    final JSONArray jsonArray1=jsonObject1.getJSONArray("pic_list");
                    if (jsonArray1.length()!=0){
                        List<String> imglist=new ArrayList<>();
                        for (int i=0;i<jsonArray1.length();i++){
                            JSONObject jsonObject2=jsonArray1.getJSONObject(i);

                            imglist.add(jsonObject2.getString("picture"));
                        }
                        data.setPic_list(imglist);}

                    data.setTypes(2);
                    list.add(data);


                    if (refresh_num==0){
                        final int isStar=jsonObject1.getInt("is_star");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isStar==1){
                                    star=true;
                                    old_star=true;
                                    tostar.setBackgroundResource(R.drawable.collect);
                                }

                            }
                        });

                    }

                    JSONArray jsonArray=jsonObject1.getJSONArray("comments");
                    if (jsonArray.length()>0){
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                            SquareComment comments=new SquareComment();
                            comments.setTypes(9);
                            comments.setContents(jsonObject2.getString("content"));
                            comments.setIs_like(jsonObject2.getInt("is_like"));
                            comments.setId(jsonObject2.getInt("id"));
                            comments.setLike_num(jsonObject2.getInt("like_num"));
                            comments.setSender_nickname(jsonObject2.getString("sender_nickname"));
                            comments.setSender_avatar(jsonObject2.getString("sender_avatar"));
                            comments.setTime(jsonObject2.getString("time"));
                            comments.setIs_reply(jsonObject2.getInt(  "is_reply"));
                            if (jsonObject2.getInt(  "is_reply")==1){
                                JSONObject jsonObject3=jsonObject2.getJSONObject("original_comment");

                                comments.setReply_avatar(jsonObject3.getString("sender_avatar"));
                                comments.setReply_content(jsonObject3.getString("content"));
                                comments.setReply_nickname(jsonObject3.getString("sender_nickname"));
                                comments.setReply_time(jsonObject3.getString("time"));
                            }
                            list.add(comments);
                        }

                    }else {
                        SquareComment comments=new SquareComment();
                        comments.setTypes(1);
                        list.add(comments);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            adapter=new CommentAdapter(SquareDetailsActivity.this,list,passage_id);
                            recyclerView.setItemViewCacheSize(10000);
                            recyclerView.setAdapter(adapter);

                            //回复评论
                            adapter.setListener(new CommentAdapter.ItemClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onItemClick(final int position) {

                                    final InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(SquareDetailsActivity.this, R.style.dialog_center);
                                    inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                                        @Override
                                        public void onTextSend(String msg) {
                                            //点击发送按钮后，回调此方法，msg为输入的值
                                            final String json= " { \"content\":"+msg+" ,\"type\":2 ,\"id\":"+passage_id+",\"original_com_id\":"+position+" }";

                                            Log.i("asd",json);
                                            try {
                                                OKhttpUtils.post_json(token, "http://47.102.215.61:8888/whole/comment", json, new OKhttpUtils.OkhttpCallBack() {
                                                    @Override
                                                    public void onSuccess(Response response)  {

                                                        try {
                                                            final JSONObject jsonObject=new JSONObject(response.body().string());

                                                            final String msg=jsonObject.getString("msg");

                                                            Log.i("asd",msg);
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    if (msg.equals("发布成功")){
                                                                        Toast.makeText(SquareDetailsActivity.this,msg,Toast.LENGTH_SHORT).show();
                                                                        inputTextMsgDialog.clearText();
                                                                        inputTextMsgDialog.dismiss();

                                                                        refresh_num++;
                                                                        page=1;old_page=1;
                                                                        smartRefreshLayout.setEnableLoadMore(true);
                                                                        List<SquareComment> list=new ArrayList<>();
                                                                        GetData(list);
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
                                }
                            });

                        }
                    });




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SquareDetailsActivity.this,"网络连接失败，请检查网络连接",Toast.LENGTH_SHORT).show();

                        if (refresh_num==0) {
                            SquareComment comments=new SquareComment();
                            comments.setTypes(0);
                            list.add(comments);
                            adapter = new CommentAdapter(SquareDetailsActivity.this, list,passage_id);
                            recyclerView.setItemViewCacheSize(10000);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });

            }
        });

    }

    private void GetComment(final List<SquareComment> list){

        if (page<old_page) {
            OKhttpUtils.get_token(token, "http://47.102.215.61:8888/passage/" + passage_id + "/detail?page=" + (page+1), new OKhttpUtils.OkhttpCallBack() {
                @Override
                public void onSuccess(Response response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        Log.i("asddd", jsonObject.toString());
                        JSONObject jsonObject1 = jsonObject.optJSONObject("data");

                        JSONArray jsonArray = jsonObject1.getJSONArray("comments");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                SquareComment comments = new SquareComment();
                                comments.setTypes(9);
                                comments.setId(jsonObject2.getInt("id"));
                                comments.setContents(jsonObject2.getString("content"));
                                comments.setIs_like(jsonObject2.getInt("is_like"));
                                comments.setLike_num(jsonObject2.getInt("like_num"));

                                comments.setSender_nickname(jsonObject2.getString("sender_nickname"));
                                comments.setSender_avatar(jsonObject2.getString("sender_avatar"));
                                comments.setTime(jsonObject2.getString("time"));
                                comments.setIs_reply(jsonObject2.getInt(  "is_reply"));
                                if (jsonObject2.getInt(  "is_reply")==1){

                                    JSONObject jsonObject3=jsonObject2.getJSONObject("original_comment");

                                    comments.setReply_avatar(jsonObject3.getString("sender_avatar"));
                                    comments.setReply_content(jsonObject3.getString("content"));
                                    comments.setReply_nickname(jsonObject3.getString("sender_nickname"));
                                    comments.setReply_time(jsonObject3.getString("time"));
                                }


                                list.add(comments);
                            }

                        } else {
                            SquareComment comments = new SquareComment();
                            comments.setTypes(1);
                            list.add(comments);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                page=page+1;
                                adapter.addData(list);
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(String error) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SquareDetailsActivity.this, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();

                            if (refresh_num == 0) {
                                SquareComment comments = new SquareComment();
                                comments.setTypes(0);
                                list.add(comments);
                                adapter .addData(list);
                            }
                        }
                    });

                }
            });
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SquareDetailsActivity.this,"没有更多了",Toast.LENGTH_SHORT).show();
                    smartRefreshLayout.setEnableLoadMore(false);
                }
            });
        }
    }
}
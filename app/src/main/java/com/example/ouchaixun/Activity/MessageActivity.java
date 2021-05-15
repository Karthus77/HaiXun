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
import android.widget.Toast;

import com.example.ouchaixun.Adapter.CommentAdapter;
import com.example.ouchaixun.Adapter.MessageAdapter;
import com.example.ouchaixun.Data.Message;
import com.example.ouchaixun.Data.SquareComment;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.InputTextMsgDialog;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class MessageActivity extends AppCompatActivity {

    private String token,photo;
    private int page=1,old_page=1,refresh_num=0;

    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private MessageAdapter adapter;
    private Boolean isrefresh=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message);

        //ImageView myPhoto=findViewById(R.id.square_myPhoto);
        recyclerView = findViewById(R.id.message_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        smartRefreshLayout=findViewById(R.id.message_smartRefreshLayout);

        MyData myData = new MyData(MessageActivity.this);
        token = myData.load_token();


        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                List<Message> list=new ArrayList<>();
                isrefresh=false;
                GetData(list);
                smartRefreshLayout.finishLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

                isrefresh=true;
                refresh_num++;
                page=1;old_page=1;
                smartRefreshLayout.setEnableLoadMore(true);
                List<Message> list=new ArrayList<>();
                GetData(list);
                smartRefreshLayout.finishRefresh();
            }
        });

        List<Message> list=new ArrayList<>();
        GetData(list);



        findViewById(R.id.message_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void GetData(final List<Message> list) {


        if (page<=old_page) {
            OKhttpUtils.get_token(token, "http://47.102.215.61:8888/self/message?page=" + page, new OKhttpUtils.OkhttpCallBack() {
                @Override
                public void onSuccess(Response response) {


                    try {

                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (isrefresh) {
                            old_page = jsonObject.getInt("num_pages");
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Message message = new Message();

                            int type = jsonObject1.getInt("msg_type");
                            message.setIds(jsonObject1.getInt("id"));
                            message.setMsg_type(type);
                            message.setAvatar(jsonObject1.getString("sender_avatar"));
                            message.setTimes(jsonObject1.getString("create_time").substring(0, 10));
                            message.setSender_nickname(jsonObject1.getString("sender_nickname"));
                            if (type == 1) {
                                message.setPost_id(jsonObject1.getInt("post_id"));
                                message.setPost_title("有人评论了您的帖子\"" + jsonObject1.getString("post_title") + "\"");

                            } else if (type == 2) {

                                message.setPost_id(jsonObject1.getInt("post_id"));
                                message.setPost_title("有人收藏了您的帖子\"" + jsonObject1.getString("post_title") + "\"");

                            } else if (type == 3) {
                                message.setPost_id(jsonObject1.getInt("talk_id"));
                                message.setPost_title("有人评论了您的校友圈\"" + jsonObject1.getString("comment_content") + "\"");

                            } else if (type == 4) {
                                message.setPost_id(jsonObject1.getInt("talk_id"));
                                message.setPost_title("发布校友圈有人点赞");
                            } else if (type == 5) {

                                message.setPost_id(jsonObject1.getInt("talk_id"));
                                message.setPost_title("有人回复了您的校友圈评论\"" + jsonObject1.getString("comment_content") + "\"");

                            } else if (type == 6) {
                                message.setPost_id(jsonObject1.getInt("post_id"));
                                message.setPost_title("有人回复了您的评论\"" + jsonObject1.getString("original_comment_content") + "\"");

                            } else if (type == 7) {

                                message.setPost_id(jsonObject1.getInt("talk_id"));
                                message.setPost_title("有人点赞了您的校友圈评论\"" + jsonObject1.getString("comment_content") + "\"");

                            } else {
                                /// 、、、、、、、、、、
                                message.setPost_id(jsonObject1.getInt("post_id"));
                                message.setPost_title("有人评论了您的帖子\"" + jsonObject1.getString("post_title") + "\"");

                            }
                            message.setTypes(9);
                            list.add(message);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                page++;
                                if (isrefresh){
                                    adapter = new MessageAdapter(MessageActivity.this, list);
                                    //recyclerView.setItemViewCacheSize(10000);
                                    recyclerView.setAdapter(adapter);
                                    adapter.setListener(new MessageAdapter.ItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            List<Message> list=new ArrayList<>();
                                            isrefresh=false;
                                            GetData(list);
                                            smartRefreshLayout.finishLoadMore();
                                        }
                                    });
                                }else {
                                    adapter.addData(list);
                                }


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
                            Toast.makeText(MessageActivity.this, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();

                            if (refresh_num == 0) {
                                Message message = new Message();
                                message.setTypes(0);
                                list.add(message);
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
                    Toast.makeText(MessageActivity.this,"没有更多了",Toast.LENGTH_SHORT).show();
                    smartRefreshLayout.setEnableLoadMore(false);
                }
            });
        }

    }







}
package com.example.ouchaixun.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.Adapter.CircleCommentAdapter;
import com.example.ouchaixun.Adapter.CircleShowAdapter;
import com.example.ouchaixun.Data.CircleDetail;
import com.example.ouchaixun.Data.SquareComment;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.InputTextMsgDialog;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
class Post_like{
    int id;
    int type;
    int action;
}
public class CircleDetilsActivity extends AppCompatActivity {
    private ImageView back;
    private int like_num;
    private String id;
    private ImageView myHead;
    RecyclerView recyclerView;
    RecyclerView recyclerView1;
    ImageView head;
    TextView name;
    TextView content;
    TextView likes;
    TextView comments;
    TextView time;
    private TextView clicks;
    private String token;
    private ImageView islike;
    private ImageView iscomment;
    List<Map<String, Object>> piclist = new ArrayList<>();
    CircleShowAdapter circleShowAdapter;
    List<Map<String, Object>> comlist = new ArrayList<>();
    CircleCommentAdapter circleCommentAdapter;
    private void fresh_comment()
    {

        OKhttpUtils.get_token(token, "http://47.102.215.61:8888/school/" + id + "/detail", new OKhttpUtils.OkhttpCallBack() {
            @Override
            public void onSuccess(Response response) throws IOException {
                Gson gson=new Gson();
                comlist.clear();
                String responseData = response.body().string();
                final CircleDetail circleDetail = gson.fromJson(responseData, CircleDetail.class);
                Log.i("kkk",String.valueOf(comlist.size())+ circleDetail.getData().getComments().size());
                while (comlist.size()<circleDetail.getData().getComments().size())
                {
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("id", circleDetail.getData().getComments().get(comlist.size()).getId());
                    map2.put("content", circleDetail.getData().getComments().get(comlist.size()).getContent());
                    map2.put("time", circleDetail.getData().getComments().get(comlist.size()).getTime());
                    map2.put("name", circleDetail.getData().getComments().get(comlist.size()).getSender_nickname());
                    map2.put("head", circleDetail.getData().getComments().get(comlist.size()).getSender_avatar());
                    map2.put("likes", circleDetail.getData().getComments().get(comlist.size()).getLike_num());
                    map2.put("islike", circleDetail.getData().getComments().get(comlist.size()).getIs_like());
                    comlist.add(map2);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        circleCommentAdapter.notifyDataSetChanged();
                        recyclerView1.setAdapter(circleCommentAdapter);
                    }
                });
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_detils);
        MyData myData = new MyData(CircleDetilsActivity.this);
        token = myData.load_token();
        myHead=findViewById(R.id.user_comment_head);
        Glide.with(CircleDetilsActivity.this).load(myData.load_pic_url()).circleCrop().into(myHead);
        clicks = findViewById(R.id.details_clicknums);
        comments = findViewById(R.id.details_com_nums);
        head = findViewById(R.id.details_userHead);
        name = findViewById(R.id.details_userName);
        content = findViewById(R.id.details_content);
        recyclerView = findViewById(R.id.details_NinePictures);
        recyclerView1 = findViewById(R.id.details_commentItems);
        time = findViewById(R.id.details_postTime);
        back = findViewById(R.id.details_back);
        GridLayoutManager manager = new GridLayoutManager(CircleDetilsActivity.this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
        circleCommentAdapter = new CircleCommentAdapter(CircleDetilsActivity.this, comlist);

        circleShowAdapter = new CircleShowAdapter(CircleDetilsActivity.this, piclist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CircleDetilsActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView1.setLayoutManager(linearLayoutManager);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent int2 = getIntent();
        id = int2.getStringExtra("id");

        OKhttpUtils.get_token(token, "http://47.102.215.61:8888/school/" + id + "/detail", new OKhttpUtils.OkhttpCallBack() {
            @Override
            public void onSuccess(Response response) throws IOException {
                Gson gson = new Gson();
                String responseData = response.body().string();
                Log.i("kkk",responseData);
                final CircleDetail circleDetail = gson.fromJson(responseData, CircleDetail.class);
                if(circleDetail.getCode()==200)
                {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        like_num = circleDetail.getData().getLike_num();
                        name.setText(circleDetail.getData().getWriter_nickname());
                        content.setText(circleDetail.getData().getContent());
                        time.setText(circleDetail.getData().getRelease_time());
                        comments.setText(String.valueOf(circleDetail.getData().getComments().size()));
                        clicks.setText("浏览量:" + String.valueOf(circleDetail.getData().getClick_num()));
                        if (circleDetail.getData().getIs_like() == 1)
                            islike.setImageResource(R.drawable.islike);
                        Glide.with(CircleDetilsActivity.this).load(circleDetail.getData().getWriter_avatar()).circleCrop().into(head);
                        for (int i = 0; i < circleDetail.getData().getPic_list().size(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("url", circleDetail.getData().getPic_list().get(i).getPicture());
                            piclist.add(map);
                        }
                        for (int i = 0; i < circleDetail.getData().getComments().size(); i++) {
                            Map<String, Object> map2 = new HashMap<>();
                            map2.put("id", circleDetail.getData().getComments().get(i).getId());
                            map2.put("content", circleDetail.getData().getComments().get(i).getContent());
                            map2.put("time", circleDetail.getData().getComments().get(i).getTime());
                            map2.put("name", circleDetail.getData().getComments().get(i).getSender_nickname());
                            map2.put("head", circleDetail.getData().getComments().get(i).getSender_avatar());
                            map2.put("likes", circleDetail.getData().getComments().get(i).getLike_num());
                            map2.put("islike", circleDetail.getData().getComments().get(i).getIs_like());
                            comlist.add(map2);
                        }

                        recyclerView1.setAdapter(circleCommentAdapter);
                        recyclerView.setAdapter(circleShowAdapter);
                    }
                });}
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CircleDetilsActivity.this,"操作过于频繁",Toast.LENGTH_SHORT);
                        }
                    });
            }

            }

            @Override
            public void onFail(String error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CircleDetilsActivity.this,"网络连接断开，请检查网络",Toast.LENGTH_SHORT);
                        }
                    });
            }
        });


        //评论
        findViewById(R.id.edit_comment).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(CircleDetilsActivity.this, R.style.dialog_center);
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        //点击发送按钮后，回调此方法，msg为输入的值
                        final String json = " { \"content\":" + msg + " ,\"type\":1 ,\"id\":" + id + " }";

                        try {
                            OKhttpUtils.post_json(token, "http://47.102.215.61:8888/whole/comment", json, new OKhttpUtils.OkhttpCallBack() {
                                @Override
                                public void onSuccess(Response response) {

                                    try {
                                        final JSONObject jsonObject = new JSONObject(response.body().string());

                                        final String msg = jsonObject.getString("msg");

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (msg.equals("发布成功")) {
                                                    Toast.makeText(CircleDetilsActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                    inputTextMsgDialog.clearText();
                                                    inputTextMsgDialog.dismiss();
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            fresh_comment();
                                                        }
                                                    }, 1000);

                                                }

                                            }
                                        });


                                        Log.i("asdf", jsonObject.toString());


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
                inputTextMsgDialog.show();


            }
        });


    }

}


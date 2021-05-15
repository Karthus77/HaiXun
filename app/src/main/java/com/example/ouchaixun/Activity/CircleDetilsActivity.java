package com.example.ouchaixun.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

public class CircleDetilsActivity extends AppCompatActivity {
    private ImageView back;
    RecyclerView recyclerView;
    RecyclerView recyclerView1;
    ImageView head;
    TextView name;
    TextView content;
    TextView likes;
    TextView comments;
    TextView time;
    private ImageView islike;
    private ImageView iscomment;
    List<Map<String,Object>> piclist =new ArrayList<>();
    CircleShowAdapter circleShowAdapter;
    List<Map<String,Object>> comlist =new ArrayList<>();
    CircleCommentAdapter circleCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_detils);
        likes=findViewById(R.id.details_like_nums);
        islike=findViewById(R.id.details_like);
        comments=findViewById(R.id.details_com_nums);
        head=findViewById(R.id.details_userHead);
        name=findViewById(R.id.details_userName);
        content=findViewById(R.id.details_content);
        recyclerView=findViewById(R.id.details_NinePictures);
        recyclerView1=findViewById(R.id.details_commentItems);
        time=findViewById(R.id.details_postTime);
        back=findViewById(R.id.details_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent int2 =getIntent();
        final String id =int2.getStringExtra("id");
        final String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjEyNjQ5MTcsImlhdCI6MTYyMDY2MDExNywiaXNzIjoicnVhIiwiZGF0YSI6eyJ1c2VyaWQiOjN9fQ.JyfnK3uRjTCBnCL9-UdyKrTEkUlvLSR_p9SasjWooEo";
        OKhttpUtils.get_token(token,"http://47.102.215.61:8888/school/"+id+"/detail", new OKhttpUtils.OkhttpCallBack() {
            @Override
            public void onSuccess(Response response) throws IOException {
                Gson gson=new Gson();
                String responseData = response.body().string();
                final CircleDetail circleDetail=gson.fromJson(responseData,CircleDetail.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(circleDetail.getData().getWriter_nickname());
                        content.setText(circleDetail.getData().getContent());
                        time.setText(circleDetail.getData().getRelease_time());
                        comments.setText(String.valueOf(circleDetail.getData().getClick_num()));
                        likes.setText(String.valueOf(circleDetail.getData().getLike_num()));
                        Glide.with(CircleDetilsActivity.this).load(circleDetail.getData().getWriter_avatar()).circleCrop().into(head);


                        for(int i=0;i<circleDetail.getData().getPic_list().size();i++)
                        {
                            Map<String,Object> map=new HashMap<>();
                            map.put("url",circleDetail.getData().getPic_list().get(i).getPicture());
                            piclist.add(map);
                        }
                        for(int i=0;i<circleDetail.getData().getComments().size();i++)
                        {
                            Map<String,Object> map2=new HashMap<>();
                            map2.put("id",circleDetail.getData().getComments().get(i).getId());
                            map2.put("content",circleDetail.getData().getComments().get(i).getContent());
                            map2.put("time",circleDetail.getData().getComments().get(i).getTime());
                            map2.put("name",circleDetail.getData().getComments().get(i).getSender_nickname());
                            map2.put("head",circleDetail.getData().getComments().get(i).getSender_avatar());
                            map2.put("likes",circleDetail.getData().getComments().get(i).getLike_num());
                            map2.put("islike",circleDetail.getData().getComments().get(i).getIs_like());
                            comlist.add(map2);
                        }
                        circleCommentAdapter=new CircleCommentAdapter(CircleDetilsActivity.this,comlist);

                        circleShowAdapter=new CircleShowAdapter(CircleDetilsActivity.this,piclist);
                        recyclerView1.setLayoutManager(new LinearLayoutManager(CircleDetilsActivity.this));
                        recyclerView1.setAdapter(circleCommentAdapter);
                        GridLayoutManager manager=new GridLayoutManager(CircleDetilsActivity.this,3){
                            @Override
                            public boolean canScrollVertically() {
                                return false;
                            }
                        };
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(circleShowAdapter);
                    }
                });

            }

            @Override
            public void onFail(String error) {

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
                        final String json= " { \"content\":"+msg+" ,\"type\":1 ,\"id\":"+id+" }";

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
                                                    Toast.makeText(CircleDetilsActivity.this,msg,Toast.LENGTH_SHORT).show();
                                                    inputTextMsgDialog.clearText();
                                                    inputTextMsgDialog.dismiss();
                                                  //刷新
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




}
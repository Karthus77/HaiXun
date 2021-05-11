package com.example.ouchaixun.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.Adapter.CircleCommentAdapter;
import com.example.ouchaixun.Adapter.CircleShowAdapter;
import com.example.ouchaixun.Data.CircleDetail;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.google.gson.Gson;

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
    List<Map<String,Object>> piclist =new ArrayList<>();
    CircleShowAdapter circleShowAdapter;
    List<Map<String,Object>> comlist =new ArrayList<>();
    CircleCommentAdapter circleCommentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_detils);
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
        String id =int2.getStringExtra("id");
        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjA3NDA2MTAsImlhdCI6MTYyMDEzNTgxMCwiaXNzIjoicnVhIiwiZGF0YSI6eyJ1c2VyaWQiOjF9fQ.M1a1yKyf29lG4PF-8fYnvQ2CwW-OeemRTfuZ6ODXZD8";


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


                        for(int i=0;i<circleDetail.getData().getPic_list().size();i++)
                        {
                            Map<String,Object> map=new HashMap<>();
                            map.put("url"+i,circleDetail.getData().getPic_list().get(i));
                            piclist.add(map);
                        }
                        for(int i=0;i<circleDetail.getData().getComments().size();i++)
                        {
                            Map<String,Object> map2=new HashMap<>();
                            map2.put("content",circleDetail.getData().getComments().get(i).getContent());
                            map2.put("time",circleDetail.getData().getComments().get(i).getTime());
                            map2.put("name",circleDetail.getData().getComments().get(i).getSender_nickname());
                            map2.put("head",circleDetail.getData().getComments().get(i).getSender_avatar());
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
    }
}
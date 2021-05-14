package com.example.ouchaixun.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.ouchaixun.Adapter.MixAdapter;
import com.example.ouchaixun.Adapter.NewsAdapter;
import com.example.ouchaixun.Data.News;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.CountDownTimerUtils;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MixActivity extends AppCompatActivity {
    private TextView head;
    private ImageView back;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private static int i = 0;
    private List<Map<String, Object>> list = new ArrayList<>();
    private int flag = 0;
    private String responseData = "";
    private String tp_url;
    private int typee;
    private boolean once = false;
    private String token;
    private int len;
    private MixAdapter adapter;
    private String myname;
    private String myhead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_collection);
        head = findViewById(R.id.chat_name);
        back = findViewById(R.id.chat_back);
        recyclerView = findViewById(R.id.recyclerview);
        refreshLayout = findViewById(R.id.new_srl1);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        String name = bd.getString("name");
        head.setText(name);
        once = false;
        typee = bd.getInt("num");   // 1:collection 2:history good  3:create;
        switch (typee) {
            case 1:
                tp_url = "/self/mystar";
                break;
            case 2:
                tp_url = "/self/history";
                break;
            case 3:
                tp_url = "/self/mycreate";
                break;
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        list.clear();
        adapter = new MixAdapter(MixActivity.this, list);
        adapter.myNotifyDataSetChange();
        MyData myData = new MyData(MixActivity.this);
        token = myData.load_token();
        myname = myData.load_name();
        myhead = myData.load_pic_url();
        once = false;
        wzy();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        adapter = new MixAdapter(MixActivity.this, list);
        adapter.myNotifyDataSetChange();
        i = 0;
        once = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient().newBuilder()
                            .connectTimeout(3000, TimeUnit.MILLISECONDS)
                            .readTimeout(3000, TimeUnit.MILLISECONDS)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://47.102.215.61:8888" + tp_url)
                            .method("GET", null)
                            .addHeader("Authorization", token)
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    responseData = response.body().string();
                    getfeedback(responseData);
                } catch (IOException e) {
                    list.clear();
                    adapter = new MixAdapter(MixActivity.this, list);
                    adapter.myNotifyDataSetChange();
                    responseData = "";
                    flag = 0;
                    Map map2 = new HashMap();
                    map2.put("type", 4);
                    list.add(map2);
                    Log.d("1233", String.valueOf(map2));
                    MixActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setLayoutManager(new LinearLayoutManager(MixActivity.this));
                            recyclerView.setAdapter(new MixAdapter(MixActivity.this, list));
                        }
                    });
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void wzy() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                flag = 0;
                i = 0;
                list.clear();
                adapter = new MixAdapter(MixActivity.this, list);
                adapter.myNotifyDataSetChange();
                responseData = "";
                onResume();
                refreshlayout.finishRefresh(1000/*,false*/);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getfeedback(responseData);
                refreshlayout.finishLoadMore(500/*,false*/);//传入false表示加载失败
            }
        });
    }
    //1-新闻 2-贴子 3-校友 0-没有 4-网络
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getfeedback(String responseData) {
        if (responseData != "") {

            try {
                JSONObject jsonObject = new JSONObject(responseData);
                if (jsonObject.getInt("code") == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Log.d("12332l", "" + jsonArray.length());
                    len = jsonArray.length();
                    if(len!=0){

                        for (int j = 0; i < jsonArray.length() && j < 8; i++, j++) {
                            Log.d("1233i", "1:" + i);
                            Log.d("1233i", "leng:" + jsonArray.length());
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            Log.d("12332", "新闻消息"+jsonObject2.toString());
                            int type = jsonObject2.getInt("type");
                            Map map = new HashMap();
                            map.put("type",type);
                           if(type==3){
                                int talk_id;
                               String writer_avatar;
                               String writer_nickname;
                                if(typee==3){
                                    talk_id = jsonObject2.getInt("id");
                                    writer_avatar = myhead;
                                    writer_nickname = myname;
                                }else{
                                    talk_id = jsonObject2.getInt("talk_id");
                                    writer_avatar ="http://47.102.215.61:8888"+jsonObject2.getString("writer_avatar");
                                    writer_nickname = jsonObject2.getString("writer_nickname");
                                }
                                String content = jsonObject2.getString("content");
                                int like_num = jsonObject2.getInt("like_num");
                                int comment_num =jsonObject2.getInt("comment_num");;
                                String  release_time = jsonObject2.getString("release_time");
                               map.put("talk_id",talk_id);
                               map.put("like_num",like_num);
                               map.put("comment_num",comment_num);
                               map.put("content",content);
                               map.put("release_time",release_time);
                               map.put("writer_avatar",writer_avatar);
                               map.put("writer_nickname",writer_nickname);
                           }else if(type==1){
                               int news_id;
                               String title;
                               String banner;
                               String writer_avatar;
                               String writer_nickname;
                               if(typee==3){
                                   news_id = jsonObject2.getInt("id");
                                   title = jsonObject2.getString("title");
                                   banner = "http://47.102.215.61:8888/"+jsonObject2.getString("banner");
                                   writer_avatar = myhead;
                                   writer_nickname = myname;
                               }else{
                                   news_id = jsonObject2.getInt("news_id");
                                   title = jsonObject2.getString("title");
                                   banner = "http://47.102.215.61:8888/"+jsonObject2.getString("banner");
                                   writer_avatar ="http://47.102.215.61:8888/"+jsonObject2.getString("writer_avatar");
                                   writer_nickname = jsonObject2.getString("writer_nickname");
                               }
                               map.put("news_id",news_id);
                               map.put("title",title);
                               map.put("banner",banner);
                               map.put("writer_avatar",writer_avatar);
                               map.put("writer_nickname",writer_nickname);
                           }
                            list.add(map);
                        }
                        if (i == jsonArray.length()) {
                            MixActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(once){
                                        Toast.makeText(MixActivity.this, "到底了~", Toast.LENGTH_SHORT).show();
                                    }else {
                                        once = true;
                                    }
                                }
                            });
                        }
                    }else{
                        list.clear();
                        adapter = new MixAdapter(MixActivity.this, list);
                        adapter.myNotifyDataSetChange();
                        responseData = "";
                        Map map2 = new HashMap();
                        map2.put("type", 0);
                        Log.d("12332", typee + "资料");
                        switch (typee) {
                            case 1:
                                map2.put("text", "您还没有收藏任何新闻");
                                break;
                            case 2:
                                map2.put("text", "您没有点赞任何新闻");
                                break;
                            case 3:
                                map2.put("text", "您还未发布任何内容");
                                break;
                        }
                        list.add(map2);
                    }
                }
                Objects.requireNonNull(MixActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (flag != 666) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(MixActivity.this));
                            recyclerView.setAdapter(new MixAdapter(MixActivity.this, list));
                            Log.d("12332", "2here");
                        }
                        if (i == len) {
                            flag = 666;
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

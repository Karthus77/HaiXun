package com.example.ouchaixun.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WelcomeActivity extends AppCompatActivity {

    private MyData myData;
    private String token;
    private String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcome);
        myData=new MyData(WelcomeActivity.this);
        token=myData.load_token();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.d("12333","123");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
//                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://47.102.215.61:8888/self/info";
                        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                                .connectTimeout(500, TimeUnit.MILLISECONDS)//设置连接超时时间
                                .readTimeout(500, TimeUnit.MILLISECONDS)//设置读取超时时间
                                .build();
                        okHttpClient.connectTimeoutMillis();
                        final Request request = new Request.Builder()
                                .url(url)
                                .get()
                                .addHeader("Authorization", token)
                                .build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                WelcomeActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(WelcomeActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                                Log.d("1233gg", "onFailure: " + e.getMessage());
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                try {
                                    JSONObject jsonObject1 = new JSONObject(responseData);
                                    String msg=jsonObject1.getString("msg");
                                    if(msg.equals("一切正常")){
                                        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                                    }else{
                                        startActivity(new Intent(WelcomeActivity.this,WelcomeLoginActivity.class));
                                    }
                                } catch (JSONException e) {
                                    WelcomeActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(WelcomeActivity.this, "无法连接网络", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                                    e.printStackTrace();
                                }
                                Log.d("1233gg", "onResponse: " + responseData);

                            }
                        });
                    }
                }).start();
                return false;
            }
        }).sendEmptyMessageDelayed(0,2000);

    }
}
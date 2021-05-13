package com.example.ouchaixun.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                return false;
            }
        }).sendEmptyMessageDelayed(0,2000);

    }
}
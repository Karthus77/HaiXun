package com.example.ouchaixun.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.Response;

public class NewsDetilsActivity extends AppCompatActivity {

    private int news_id;
    private String token="hgghgg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_news_detils);
        final TextView textView=findViewById(R.id.news_detils_textview);
        RichText.initCacheDir(this);

        news_id= getIntent().getIntExtra("id",1);

        OKhttpUtils.get_token(token,"http://47.102.215.61:8888/news/"+news_id+"/detail", new OKhttpUtils.OkhttpCallBack() {
            @Override
            public void onSuccess(Response response)  {

                try {
                    JSONObject jsonObject0=new JSONObject(response.body().string());
                    Log.i("asd",jsonObject0.toString());

                    JSONObject jsonObject1=jsonObject0.getJSONObject("data");
                    final String content=jsonObject1.getString("content");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RichText.from(content).bind(this)
                                    .showBorder(false)
                                    .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                                    .into(textView);

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {

            }
        });





        findViewById(R.id.news_detils_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
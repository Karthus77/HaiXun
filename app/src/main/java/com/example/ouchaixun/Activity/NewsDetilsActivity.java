package com.example.ouchaixun.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.example.ouchaixun.richtext.publishActivity;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.Response;

public class NewsDetilsActivity extends AppCompatActivity {

    private int news_id;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_news_detils);
        final TextView textView=findViewById(R.id.news_detils_textview);
        final TextView tv_title=findViewById(R.id.news_detils_title);
        final TextView tv_time=findViewById(R.id.news_detils_time);
        TextView tv_type=findViewById(R.id.news_detils_type);
        final ImageView img=findViewById(R.id.news_detils_img);
        RichText.initCacheDir(this);
        MyData myData = new MyData(NewsDetilsActivity.this);
        token = myData.load_token();
        news_id= getIntent().getIntExtra("id",1);




        OKhttpUtils.get_token(token,"http://47.102.215.61:8888/news/"+news_id+"/detail", new OKhttpUtils.OkhttpCallBack() {
            @Override
            public void onSuccess(Response response)  {

                try {
                    JSONObject jsonObject0=new JSONObject(response.body().string());
                    Log.i("asd",jsonObject0.toString());

                    JSONObject jsonObject1=jsonObject0.getJSONObject("data");


                    final String content=jsonObject1.getString("content");
                    final String title=jsonObject1.getString("title");
                    final String time=jsonObject1.getString("release_time");
                    final String photo="http://47.102.215.61:8888/"+jsonObject1.getString("banner");
                    Log.i("asd",photo);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tv_title.setText(title);
                            tv_time.setText(time.substring(0,10));
                            Glide.with(NewsDetilsActivity.this)
                                    .load(photo)
                                    .error(R.drawable.img_error)
                                    .into(img);
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


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewsDetilsActivity.this,"网络连接失败，\n请检查网络设置",Toast.LENGTH_SHORT).show();
                    }
                });
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
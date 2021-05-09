package com.example.ouchaixun.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.ouchaixun.Data.SquareComment;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.InputTextMsgDialog;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

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
    private int page=1,old_page=1;
    private int passage_id=1;
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private CommentAdapter adapter;

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

        ImageView myPhoto=findViewById(R.id.square_myPhoto);
        recyclerView = findViewById(R.id.square_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(SquareDetailsActivity.this));
        smartRefreshLayout=findViewById(R.id.square_smartRefreshLayout);

        MyData myData = new MyData(SquareDetailsActivity.this);
        token = myData.load_token();
        photo=myData.load_pic_url();

        Glide.with(SquareDetailsActivity.this)
                .load(photo)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .error(R.drawable.img_error)
                .into(myPhoto);

        List<SquareComment> list=new ArrayList<>();
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
                        String json="{\"content\": \""+msg+"\"}";

                    }
                });
                //设置评论字数
                inputTextMsgDialog.setMaxNumber(50);
                inputTextMsgDialog .show();
            }
        });

    }


    private void GetData(final List<SquareComment> list){
        OKhttpUtils.get_token(token, "http://47.102.215.61:8888/passage/"+passage_id+"/detail?page="+page, new OKhttpUtils.OkhttpCallBack() {
            @Override
            public void onSuccess(Response response)  {
                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());

                    Log.i("asddd",jsonObject.toString());
                    JSONObject jsonObject1=jsonObject.optJSONObject("data");
                    SquareComment data=new SquareComment();
                    data.setPassage_id(jsonObject1.getInt("id"));
                    data.setTitles(jsonObject1.getString("title"));
                    data.setContents(jsonObject1.getString("content"));
                    data.setTag(jsonObject1.getString("tag"));
                    data.setTime(jsonObject1.getString("release_time"));
                    data.setWriter_nickname(jsonObject1.getString("writer_nickname"));
                    data.setWriter_avatar(jsonObject1.getString("writer_avatar"));

                    final JSONArray jsonArray1=jsonObject1.getJSONArray("pic_list");
                    if (jsonArray1.length()!=0){
                        List<String> imglist=new ArrayList<>();
                        for (int i=0;i<jsonArray1.length();i++){
                            imglist.add(jsonArray1.getString(i));
                        }
                        data.setPic_list(imglist);}

                    data.setTypes(2);
                    list.add(data);

                    old_page=jsonObject1.getInt("num_pages");

                   // data.setIs_star(jsonObject1.getInt("is_star"));



                    JSONArray jsonArray=jsonObject1.getJSONArray("comments");
                    if (jsonArray.length()>0){
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                            SquareComment comments=new SquareComment();
                            comments.setTypes(9);
                            comments.setContents(jsonObject2.getString("content"));
                            comments.setIs_like(jsonObject2.getInt("is_like"));
                            comments.setLike_num(jsonObject2.getInt("like_num"));
                            comments.setSender_nickname(jsonObject2.getString("sender_nickname"));
                            comments.setSender_avatar(jsonObject2.getString("sender_avatar"));
                            comments.setTime(jsonObject2.getString("time"));
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
                            adapter=new CommentAdapter(SquareDetailsActivity.this,list);
                            recyclerView.setAdapter(adapter);
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

    }
}
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

import com.example.ouchaixun.Adapter.CommentAdapter;
import com.example.ouchaixun.Adapter.NewsAdapter;
import com.example.ouchaixun.Data.SquareComment;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.InputTextMsgDialog;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Response;

public class SquareDetailsActivity extends AppCompatActivity {
    private String token;
    private int page=1;
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

        recyclerView = findViewById(R.id.square_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(SquareDetailsActivity.this));
        smartRefreshLayout=findViewById(R.id.square_smartRefreshLayout);

        MyData myData = new MyData(SquareDetailsActivity.this);
        token = myData.load_token();


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
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    SquareComment data=new SquareComment();
                    data.setPassage_id(jsonObject1.getInt("id"));
                    data.setTitles(jsonObject1.getString("title"));
                    data.setContents(jsonObject1.getString("content"));
                    data.setTag(jsonObject1.getString("tag"));
                    data.setIs_star(jsonObject1.getInt("is_star"));
                    data.setTypes(2);
                    list.add(data);


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
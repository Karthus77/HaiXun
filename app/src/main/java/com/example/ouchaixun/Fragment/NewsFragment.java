package com.example.ouchaixun.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ouchaixun.Adapter.NewsAdapter;
import com.example.ouchaixun.Data.News;
import com.example.ouchaixun.Data.ViewPagerData;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.OKhttpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.internal.Utils;
import okhttp3.Response;


public class NewsFragment extends Fragment {


    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private Boolean header=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.new_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Button button=getActivity().findViewById(R.id.news_btn);
        Button button_write=getActivity().findViewById(R.id.news_btn_write);

        List<News> list=new ArrayList<>();
        News news0=new News();

        //header//轮播图
      GetHeader(list);





        //新闻
        GetNews(list);



button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //显示隐藏header

        if (header){
            header=false;
            adapter.visibility(false);
        }else {
            header=true;
            adapter.visibility(true);
            recyclerView.scrollToPosition(0);

            // 关闭正在打开状态的Footer refreshLayout.closeHeaderOrFooter();
            //保证返回到最顶部
        }


    }
});

button_write.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.i("asd","hhhhhhhhhhh");
       // startActivity(new Intent(getActivity(), PublishActivity.class));
    }
});


    }

    private void GetHeader(List<News> list){
        //
        News news0=new News();
        news0.setType(3);
        news0.setVisibility(false);
        list.add(news0);


        ViewPagerData pagerData=new ViewPagerData();
        News news=new News();
        List<ViewPagerData> pager= new ArrayList<>();

        pagerData.setImg("http://47.102.215.61:8888/media/avatar/default.png");//4
        pagerData.setNews_id(1);
        pagerData.setTitle("asd");
        pager.add(pagerData);//4
        for (int i=0;i<4;i++){
            pagerData.setImg("http://47.102.215.61:8888/media/avatar/default.png");
            pagerData.setNews_id(1);
            pagerData.setTitle("asd");
            pager.add(pagerData);
        }
        pagerData.setImg("http://47.102.215.61:8888/media/avatar/default.png");//1
        pagerData.setNews_id(1);
        pagerData.setTitle("asd");
        pager.add(pagerData);//1


        news.setPager(pager);
        news.setType(2);
        list.add(news);
    }



    private void GetNews(final List<News> list){


            OKhttpUtils.get("http://47.102.215.61:8888/news/news_list", new OKhttpUtils.OkhttpCallBack() {
                @Override
                public void onSuccess(Response response) throws IOException {

                    try {
                        JSONObject jsonObject0=new JSONObject(response.body().string());
                        Log.i("asd",jsonObject0.getString("msg"));
                        JSONArray jsonArray=jsonObject0.getJSONArray("data");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            News news=new News();
                            news.setNews_id(jsonObject1.getInt("id"));
                            news.setTitle(jsonObject1.getString("title"));
                            news.setNickName(jsonObject1.getString("writer_nickname"));
                            news.setType(9);
                            list.add(news);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter=new NewsAdapter(getContext(),list,recyclerView);
                                recyclerView.setAdapter(adapter);
                            }
                        });



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFail(String error) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"网络连接失败，请检查网络连接",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });



    }


}
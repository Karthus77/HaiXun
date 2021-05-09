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
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.example.ouchaixun.richtext.publishActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.Response;


public class NewsFragment extends Fragment {


    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private NewsAdapter adapter;
    private Boolean header=false;
    private int page=1,page_num=1,refresh_num=0;
    private String token;

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
        Button button=getActivity().findViewById(R.id.news_btn_header);
        Button button_write=getActivity().findViewById(R.id.news_btn_write);
        smartRefreshLayout=getActivity().findViewById(R.id.new_smartRefreshLayout);
        MyData myData = new MyData(getContext());
        token = myData.load_token();
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                List<News> list=new ArrayList<>();
                GetNews(list);
                smartRefreshLayout.finishLoadMore();
            }
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh_num++;
                smartRefreshLayout.setEnableLoadMore(true);
                List<News> list=new ArrayList<>();
                GetFirst(list);
                smartRefreshLayout.finishRefresh();
            }
        });

        List<News> list=new ArrayList<>();
        //header//轮播图
        GetFirst(list);





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
            smartRefreshLayout.closeHeaderOrFooter();
            // 有加载的动画时不能返回顶部（实际使用中开始到finish时间较短不知会不会出现此情况）
            // 关闭正在打开状态的Footer 保证返回到最顶部
            recyclerView.scrollToPosition(0);
        }
    }
});

button_write.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        startActivity(new Intent(getActivity(), publishActivity.class));
    }
});

    }


    private void GetFirst(final List<News> list){

        OKhttpUtils.get("http://47.102.215.61:8888/news/news_list?page="+page, new OKhttpUtils.OkhttpCallBack() {
            @Override
            public void onSuccess(Response response)  {

                try {
                    JSONObject jsonObject0=new JSONObject(response.body().string());
                    Log.i("asd",jsonObject0.getString("msg"));
                    page_num=jsonObject0.getInt("num_pages");
                    JSONArray jsonArray=jsonObject0.getJSONArray("banner_list");
                    //header
                    News news=new News();
                    news.setType(3);
                    news.setVisibility(false);
                    list.add(news);
                    //pager
                    News news1=new News();
                    List<ViewPagerData> pager= new ArrayList<>();
                    ViewPagerData pagerData=new ViewPagerData();
                    //第四个数据
                    JSONObject jsonObject4=jsonArray.getJSONObject(3);
                    pagerData.setImg(jsonObject4.getString("banner"));
                    pagerData.setNews_id(jsonObject4.getInt("id"));
                    pagerData.setTitle(jsonObject4.getString("title"));
                    pager.add(pagerData);

                    //中间1234
                    for (int i=0;i<4;i++){
                        JSONObject jsonObject2=jsonArray.getJSONObject(i);
                        ViewPagerData pagerData1=new ViewPagerData();
                        pagerData1.setImg(jsonObject2.getString("banner"));
                        pagerData1.setNews_id(jsonObject2.getInt("id"));
                        pagerData1.setTitle(jsonObject2.getString("title"));
                        pager.add(pagerData1);
                    }

                    //第一个数据
                    ViewPagerData pagerData2=new ViewPagerData();
                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                    pagerData2.setImg(jsonObject1.getString("banner"));
                    pagerData2.setNews_id(jsonObject1.getInt("id"));
                    pagerData2.setTitle(jsonObject1.getString("title"));
                    pager.add(pagerData2);
                    //轮播图放进第二个item里
                    news1.setPager(pager);
                    news1.setType(2);
                    list.add(news1);


                   // 置顶新闻
                    JSONArray jsonArray1=jsonObject0.getJSONArray("top_list");
                    Log.i("asd",jsonObject0.getJSONArray("top_list").toString());
                    for (int i=0;i<jsonArray1.length();i++){
                        JSONObject jsonObject2=jsonArray1.getJSONObject(i);
                        News news2=new News();
                        news2.setNews_id(jsonObject2.getInt("id"));
                        news2.setTitle(jsonObject2.getString("title"));
                        news2.setNickName(jsonObject2.getString("writer_nickname"));
                        news2.setImg(jsonObject2.getString("banner"));
                        news2.setIntop(true);
                        news2.setType(9);
                        list.add(news2);
                    }

                    // 新闻
                    JSONArray jsonArray3=jsonObject0.getJSONArray("news_list");

                    for (int i=0;i<jsonArray3.length();i++){
                        JSONObject jsonObject3=jsonArray3.getJSONObject(i);
                        News news3=new News();
                        news3.setNews_id(jsonObject3.getInt("id"));
                        news3.setTitle(jsonObject3.getString("title"));
                        news3.setNickName(jsonObject3.getString("writer_nickname"));
                        news3.setImg(jsonObject3.getString("banner"));
                        news3.setIntop(false);
                        news3.setType(9);
                        list.add(news3);
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

                        if (refresh_num==0) {
                            News news = new News();
                            news.setType(0);
                            list.add(news);
                            adapter = new NewsAdapter(getContext(), list, recyclerView);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
            }
        });


    }



    private void GetNews(final List<News> list) {


        if (page <page_num) {
            OKhttpUtils.get("http://47.102.215.61:8888/news/news_list?page=" + (page + 1), new OKhttpUtils.OkhttpCallBack() {
                @Override
                public void onSuccess(Response response)  {

                    try {
                        Log.i("asd","http://47.102.215.61:8888/news/news_list?page=" + (page + 1));
                        JSONObject jsonObject0 = new JSONObject(response.body().string());
                        Log.i("asd", jsonObject0.getString("msg"));
                        JSONArray jsonArray = jsonObject0.getJSONArray("news_list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            News news = new News();
                            news.setNews_id(jsonObject1.getInt("id"));
                            news.setTitle(jsonObject1.getString("title"));
                            news.setNickName(jsonObject1.getString("writer_nickname"));
                            news.setImg(jsonObject1.getString("banner"));
                            news.setIntop(false);
                            news.setType(9);
                            list.add(news);
                        }


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                adapter.addData(list);
                                page++;
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


                            adapter = new NewsAdapter(getContext(), list, recyclerView);
                            recyclerView.setAdapter(adapter);
                            Toast.makeText(getContext(), "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"没有更多了",Toast.LENGTH_SHORT).show();
                    smartRefreshLayout.setEnableLoadMore(false);
                }
            });
        }
    }
}
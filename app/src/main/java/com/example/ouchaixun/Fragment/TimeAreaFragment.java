package com.example.ouchaixun.Fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ouchaixun.Adapter.CircleAdapter;
import com.example.ouchaixun.Adapter.HotListAdapter;
import com.example.ouchaixun.Data.CircleList;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Response;


public class TimeAreaFragment extends Fragment {
    private String token;
    private SmartRefreshLayout smartRefreshLayout;
    private CircleAdapter circleAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private int page=1,size=9,o_page=1,refresh_num=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyData myData = new MyData(getActivity());
        token = myData.load_token();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_area, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefreshLayout=view.findViewById(R.id.time_refresh);
        recyclerView=view.findViewById(R.id.time_recycler);
        relativeLayout=view.findViewById(R.id.time_background);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final List<Map<String,Object>> list=new ArrayList<>();
        GetCircle(list,true);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                GetCircle(list, false);
                refreshLayout.finishLoadMore();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                refresh_num++;
                list.clear();
                GetCircle(list, true);
                refreshLayout.finishRefresh();
            }
        });}
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void GetCircle(final List<Map<String,Object>> list, final Boolean refresh){

        if (page<=o_page) {

            OKhttpUtils.get_token(token ,
                    "http://47.102.215.61:8888" +
                    "/school/talk_list?page="+page, new OKhttpUtils.OkhttpCallBack() {
                @Override
                public void onSuccess(Response response) {

                    try {
                        Gson gson=new Gson();
                        String responseData = response.body().string();
                        final CircleList circleList=gson.fromJson(responseData,CircleList.class);
                        o_page=circleList.getNum_pages();

                        if (circleList.getMsg().equals("数据库暂无相关数据")){

                        }else {


                            for(int i=0;i<circleList.getData().size();i++)
                            {
                                if(circleList.getData().size()==0)
                                {
                                    Map<String,Object> map=new HashMap<>();
                                    map.put("type",2);
                                    list.add(map);
                                }
                                else {


                                Map<String,Object> map=new HashMap<>();
                                map.put("type",0);
                                map.put("id",circleList.getData().get(i).getId());
                                map.put("head",circleList.getData().get(i).getWriter_avatar());
                                map.put("name",circleList.getData().get(i).getWriter_nickname());
                                map.put("time",circleList.getData().get(i).getRelease_time().substring(0,10));
                                map.put("content",circleList.getData().get(i).getContent());
                                map.put("comment",circleList.getData().get(i).getComment_num());
                                map.put("like",circleList.getData().get(i).getLike_num());
                                map.put("islike",circleList.getData().get(i).getIs_like());
                                for(int k=0;k<circleList.getData().get(i).getPic_list().size();k++)
                                {
                                    map.put("url"+k,circleList.getData().get(i).getPic_list().get(k).getPicture());
                                }
                                list.add(map);}
                            }

                                   getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (refresh) {
                                                circleAdapter = new CircleAdapter(getContext(), list);
                                                recyclerView.setAdapter(circleAdapter);
                                                if(refresh_num!=0)
                                                Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                                            } else {
                                                circleAdapter.notifyDataSetChanged();
                                                Toast.makeText(getContext(),"加载成功",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });}






                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(String error) {

                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, Object> map = new HashMap<>();
                            map.put("type",1);
                            list.add(map);
                            Toast.makeText(getContext(),"网络连接好像断开了哦",Toast.LENGTH_SHORT).show();
                            circleAdapter = new CircleAdapter(getContext(), list);
                            recyclerView.setAdapter(circleAdapter);

                        }
                    });
                }
        });}else {
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"没有更多了",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    }
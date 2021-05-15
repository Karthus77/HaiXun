package com.example.ouchaixun.Fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ouchaixun.Adapter.CircleAdapter;
import com.example.ouchaixun.Adapter.SquareAdapter;
import com.example.ouchaixun.Data.CircleList;
import com.example.ouchaixun.Data.SquareList;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Response;


public class SquareWallFragment extends Fragment {
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjE1MjM4MDIsImlhdCI6MTYyMDkxOTAwMiwiaXNzIjoicnVhIiwiZGF0YSI6eyJ1c2VyaWQiOjN9fQ.zzO6gk1Y6iaRawxb--avh4xaGeUhuI16BnxgtRydxks";
    private static final String tag="1";
    private int page=1,o_page=1,refresh_num=0;
    private SquareAdapter squareAdapter;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void GetSquare(final List<Map<String,Object>> list, final Boolean refresh){

        if (page<=o_page) {

            OKhttpUtils.get_token(token ,
                    "http://47.102.215.61:8888/passage/passage_list?page="+page+"&tag=1", new OKhttpUtils.OkhttpCallBack() {
                        @Override
                        public void onSuccess(Response response) {

                            try {
                                Gson gson=new Gson();
                                String responseData = response.body().string();
                                final SquareList squareList=gson.fromJson(responseData,SquareList.class);
                                o_page=squareList.getNum_pages();

                                if (squareList.getMsg().equals("数据库暂无相关数据")){

                                }else {

                                    for(int i=0;i<squareList.getData().size();i++)
                                    {
                                        Map<String,Object> map=new HashMap<>();
                                        map.put("id",squareList.getData().get(i).getId());
                                        map.put("head",squareList.getData().get(i).getWriter_avatar());
                                        map.put("tag",squareList.getData().get(i).getTag());
                                        map.put("title",squareList.getData().get(i).getTitle());
                                        map.put("time",squareList.getData().get(i).getRelease_time());
                                        if (squareList.getData().get(i).getWriter_nickname().equals("该内容由匿名用户发布"))
                                        {
                                            map.put("anonymous",1);
                                            map.put("name","匿名");
                                        }
                                        else
                                        {
                                            map.put("anonymous",2);
                                            map.put("name",squareList.getData().get(i).getWriter_nickname());
                                        }
                                        list.add(map);
                                    }

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            relativeLayout.setBackgroundResource(R.drawable.background_box22);
                                            if (list.size()==0)
                                                relativeLayout.setBackgroundResource(R.drawable.hbb_no);
                                            if (refresh) {
                                                squareAdapter = new SquareAdapter(getContext(), list);
                                              recyclerView.setAdapter(squareAdapter);
                                                if(refresh_num!=0)
                                                Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                                            } else {
                                        squareAdapter.notifyDataSetChanged();
                                                Toast.makeText(getContext(),"加载成功",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });}
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onFail(String error) {

                            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                  Toast.makeText(getContext(),"网络连接好像断开了哦",Toast.LENGTH_SHORT).show();
                                  relativeLayout.setBackgroundResource(R.drawable.hbb_nonet);
                                }
                            });
                        }
                    });}else {
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"没有更多了",Toast.LENGTH_SHORT).show();
                    smartRefreshLayout.setEnableLoadMore(false);
                }
            });
        }
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_square_wall, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefreshLayout=view.findViewById(R.id.wall_refresh);
        recyclerView=view.findViewById(R.id.wall_recycler);
        relativeLayout=view.findViewById(R.id.wall_background);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final List<Map<String,Object>> list=new ArrayList<>();
        GetSquare(list,true);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                GetSquare(list, false);
                refreshLayout.finishLoadMore();
            }
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                refresh_num++;
                list.clear();
                GetSquare(list, true);
                refreshLayout.finishRefresh();
            }
        });}
    }

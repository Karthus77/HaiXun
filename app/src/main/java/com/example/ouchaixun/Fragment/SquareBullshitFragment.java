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

import com.example.ouchaixun.Adapter.SquareAdapter;
import com.example.ouchaixun.Data.SquareList;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;
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


public class SquareBullshitFragment extends Fragment {
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private String token;
    private static final String tag="2";
    private int page=1,o_page=1,refresh_num=0;
    private SquareAdapter squareAdapter;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void GetSquare(final List<Map<String,Object>> list, final Boolean refresh){

        if (page<=o_page) {

            OKhttpUtils.get_token(token ,
                    "http://47.102.215.61:8888/passage/passage_list?page="+page+"&tag=3", new OKhttpUtils.OkhttpCallBack() {
                        @Override
                        public void onSuccess(Response response) {

                            try {
                                Gson gson=new Gson();
                                String responseData = response.body().string();
                                final SquareList squareList=gson.fromJson(responseData,SquareList.class);
                                o_page=squareList.getNum_pages();

                                if (squareList.getMsg().equals("???????????????????????????")){

                                }else {

                                    for(int i=0;i<squareList.getData().size();i++)
                                    {
                                        if(squareList.getData().size()==0)
                                        {
                                            Map<String,Object> map=new HashMap<>();
                                            map.put("type",2);
                                            list.add(map);
                                        }
                                        else {
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("type", 0);
                                            map.put("id", squareList.getData().get(i).getId());
                                            map.put("head", squareList.getData().get(i).getWriter_avatar());
                                            map.put("tag", squareList.getData().get(i).getTag());
                                            map.put("title", squareList.getData().get(i).getTitle());
                                            map.put("time", squareList.getData().get(i).getRelease_time().substring(0,10));
                                            if (squareList.getData().get(i).getWriter_nickname().equals("??????????????????????????????")) {
                                                map.put("anonymous", 1);
                                                map.put("name", "??????");
                                            } else {
                                                map.put("anonymous", 2);
                                                map.put("name", squareList.getData().get(i).getWriter_nickname());
                                            }
                                            list.add(map);
                                        }
                                    }

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (refresh) {
                                                squareAdapter = new SquareAdapter(getContext(), list);
                                                recyclerView.setAdapter(squareAdapter);
                                                if(refresh_num!=0)
                                                Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT).show();
                                            } else {
                                                squareAdapter.notifyDataSetChanged();
                                                Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT).show();
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
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("type",1);
                                    list.add(map);
                                    Toast.makeText(getContext(),"??????????????????????????????",Toast.LENGTH_SHORT).show();
                                    squareAdapter = new SquareAdapter(getContext(), list);
                                    recyclerView.setAdapter(squareAdapter);
                                }
                            });
                        }
                    });}else {
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"???????????????",Toast.LENGTH_SHORT).show();
                    smartRefreshLayout.setEnableLoadMore(false);

                }
            });
        }
    }






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyData myData = new MyData(getActivity());
        token = myData.load_token();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_square_bullshit, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefreshLayout=view.findViewById(R.id.bullshit_refresh);
        relativeLayout=view.findViewById(R.id.bullshit_background);
        recyclerView=view.findViewById(R.id.bullshit_recycler);
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
        });
    }
}
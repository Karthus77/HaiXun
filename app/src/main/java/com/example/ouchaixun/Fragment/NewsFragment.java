package com.example.ouchaixun.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ouchaixun.R;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {


//    private RecyclerView recyclerView;
//    private NewsAdapter adapter;
//    private Boolean header=false;

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

//        recyclerView = view.findViewById(R.id.new_recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        Button button=getActivity().findViewById(R.id.news_btn);
//        Button button_write=getActivity().findViewById(R.id.news_btn_write);
//
//        List<News> list=new ArrayList<>();
//        News news0=new News();
//
//        //header
//        news0.setType(3);
//        news0.setVisibility(header);
//        list.add(news0);
//
//        //轮播图
//        final News news=new News();
//        List<String> img= new ArrayList<>();
//        img.add("https://pic1.zhimg.com/v2-bd20f21fe8614aa4a5a4f964316a5ae6.jpg?source=8673f162");
//        img.add("https://pic1.zhimg.com/v2-bd20f21fe8614aa4a5a4f964316a5ae6.jpg?source=8673f162");
//        img.add("https://pic1.zhimg.com/v2-bd20f21fe8614aa4a5a4f964316a5ae6.jpg?source=8673f162");
//        img.add("https://pic1.zhimg.com/v2-bd20f21fe8614aa4a5a4f964316a5ae6.jpg?source=8673f162");
//        img.add("https://pic4.zhimg.com/v2-91db039ccf28ecbb786db0adcb5332b7.jpg?source=8673f162");
//        img.add("https://pic4.zhimg.com/v2-91db039ccf28ecbb786db0adcb5332b7.jpg?source=8673f162");
//        news.setPics(img);
//        Log.i("asddd",img.toString());
//        news.setType(2);
//        list.add(news);
//
//
//        //新闻
//        for (int i=0;i<20;i++){
//            News news1=new News();
//            news1.setType(4);
//            news1.setTitle("skjjdsj");
//            list.add(news1);
//        }
//
//
//        adapter=new NewsAdapter(getContext(),list);
//        recyclerView.setAdapter(adapter);
//
//button.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        //显示隐藏header
//
//        if (header){
//            header=false;
//
//            adapter.visibility(false);
//        }else {
//            header=true;
//            adapter.visibility(true);
//            recyclerView.scrollToPosition(0);
//
//            // 关闭正在打开状态的Footer refreshLayout.closeHeaderOrFooter();
//            //保证返回到最顶部
//        }
//
//
//    }
//});
//
//button_write.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Log.i("asd","hhhhhhhhhhh");
//        startActivity(new Intent(getActivity(), PublishActivity.class));
//    }
//});
//

    }
}
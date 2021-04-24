package com.example.ouchaixun.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.ouchaixun.Data.News;
import com.example.ouchaixun.R;


import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<News> list;
    private static final int ITEM_HEADER = 0;
    private static final int ITEM_PAGER = 1;
    private static final int ITEM_NEWS = 2;
    private static final int ITEM_ERROR = 3;
    private static final int ITEM_NO = 4;
    private ViewPagerAdapter viewPagerAdapter;
    private HeaderAdapter headerAdapter;
    private RecyclerView recyclerView;

    public NewsAdapter(Context context, List<News> list, RecyclerView recyclerView) {
        this.context = context;
        this.list = list;
        this.recyclerView=recyclerView;
    }


    public void addData(List<News> addList) {
        if (addList != null) {
            list.addAll(addList);
            notifyItemRangeChanged(list.size() - addList.size(), addList.size());

        }
    }


    public void visibility( Boolean isVisibility) {

//        recyclerView.scrollToPosition(9);
         list.get(0).setVisibility(isVisibility);
        notifyItemChanged(0);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view_news = null;
        RecyclerView.ViewHolder holder = null;

//        if (i == ITEM_NO) {
//            view_news = LayoutInflater.from(context).inflate(R.layout.item_nofocused, viewGroup, false);
//            holder = new NoHolder(view_news);
//        }


        if (i == ITEM_HEADER) {
            view_news = LayoutInflater.from(context).inflate(R.layout.item_header, viewGroup, false);
            holder = new HeaderHolder(view_news);
        }
        if (i == ITEM_NEWS) {
            view_news = LayoutInflater.from(context).inflate(R.layout.item_news, viewGroup, false);
            holder = new NewsHolder(view_news);
        }
        if (i == ITEM_PAGER) {
            view_news = LayoutInflater.from(context).inflate(R.layout.item_viewpager2, viewGroup, false);
            holder = new PagerHolder(view_news);
        }
//        if (i == ITEM_ERROR) {
//            view_news = LayoutInflater.from(context).inflate(R.layout.item_papernonet, viewGroup, false);
//            holder = new ErrorHolder(view_news);
//        }
        assert holder != null;
        return holder;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {

        //空白页
        if (holder instanceof NoHolder) {

        }
        //新闻
        if (holder instanceof NewsHolder) {
           ((NewsHolder) holder).intop.setText("置顶");
            ((NewsHolder) holder).intop.setTextColor(context.getResources().getColor(R.color.orange));
            ((NewsHolder) holder).intop.setBackgroundResource(R.drawable.intop);
            ((NewsHolder) holder).official.setVisibility(View.VISIBLE);

//            ((NewsHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(context, NewsDatilsActivity.class);
//                    intent.putExtra("id",1);
//                    context.startActivity(intent);
//                }
//            });
//


        }
        //顶部收藏
        if (holder instanceof HeaderHolder) {


            ((HeaderHolder)holder).setVisibility(list.get(i).getVisibility());

            List<News> listqq=new ArrayList<>();

            for (int j=0;j<10;j++){
                News news1=new News();
                news1.setTitle("skjjdsj");
                Log.i("asd","asdasd");
                listqq.add(news1);
            }

            LinearLayoutManager layoutManager=new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ((HeaderHolder)holder).recyclerView.setLayoutManager(layoutManager);

            headerAdapter=new HeaderAdapter(context,listqq);
            ((HeaderHolder)holder).recyclerView.setAdapter(headerAdapter);



        }

        //轮播图
        if (holder instanceof PagerHolder) {
            List<String>  img = list.get(i).getPics();
            Log.i("asddd",img.toString());
            viewPagerAdapter = new ViewPagerAdapter(context, img);


            ((PagerHolder) holder).viewPager2.setOffscreenPageLimit(1);
            //滑动效果
            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(10));
            compositePageTransformer.addTransformer(new TransFormer());
            //一屏三页
            ((PagerHolder) holder).viewPager2.setPageTransformer(compositePageTransformer);
            View recyclerView = ((PagerHolder) holder).viewPager2.getChildAt(0);
            if (recyclerView != null && recyclerView instanceof RecyclerView) {
                recyclerView.setPadding(80, 0, 80, 0);
                ((RecyclerView) recyclerView).setClipToPadding(false);
            }


            ((PagerHolder) holder).viewPager2.setAdapter(viewPagerAdapter);
            ((PagerHolder) holder).viewPager2.setCurrentItem(1);
            // 循环滑动
            final List<String> finalImg = img;
            ((PagerHolder) holder).viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                int currentPosition;

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    currentPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // ViewPager.SCROLL_STATE_IDLE 标识的状态是当前页面完全展现，并且没有动画正在进行中，如果不
                    // 是此状态下执行 setCurrentItem 方法回在首位替换的时候会出现跳动！
                    if (state != ViewPager.SCROLL_STATE_IDLE) return;

                    // 当视图在第一个时，将页面号设置为图片的最后一张。
                    if (currentPosition == 0) {
                        ((PagerHolder) holder).viewPager2.setCurrentItem(finalImg.size() - 2, false);

                    } else if (currentPosition == finalImg.size() - 1) {
                        // 当视图在最后一个是,将页面号设置为图片的第一张。
                        ((PagerHolder) holder).viewPager2.setCurrentItem(1, false);
                    }
                }
            });
            viewPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int i) {


        if (list.get(i).getType()==0){
            Log.i("asd", "错误");
            return ITEM_ERROR;
        }else if (list.get(i).getType()==1){
            return ITEM_NO;
        }else if (list.get(i).getType()==2){
            Log.i("asd", "这是轮播图");
            return ITEM_PAGER;
        }else if (list.get(i).getType()==3){
            return ITEM_HEADER;
        }else {
            return ITEM_NEWS;
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PagerHolder extends RecyclerView.ViewHolder {

        public ViewPager2 viewPager2;
        public RelativeLayout linearLayout;

        public PagerHolder(@NonNull View itemView) {
            super(itemView);
            viewPager2 = itemView.findViewById(R.id.recy_pager);
            linearLayout = itemView.findViewById(R.id.viewpager_lin);


        }
    }


    public static class NewsHolder extends RecyclerView.ViewHolder {


        TextView intop,official;
        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            intop=itemView.findViewById(R.id.news_intop);
            official=itemView.findViewById(R.id.news_official);

        }
    }



    public static class HeaderHolder extends RecyclerView.ViewHolder {
        public RecyclerView recyclerView;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView=itemView.findViewById(R.id.news_header_recycler);
        }
            public void setVisibility(boolean isVisible){
                RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
                if (isVisible){
                    param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    itemView.setVisibility(View.VISIBLE);
                }else{
                    itemView.setVisibility(View.GONE);

                    param.height = 1;
//                    param.width = 0;
                }
                itemView.setLayoutParams(param);
            }




    }
    public static class ErrorHolder extends RecyclerView.ViewHolder {


        public ErrorHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

    public static class NoHolder extends RecyclerView.ViewHolder {


        TextView textView;
        //SquareLayout squareLayout;

        public NoHolder(@NonNull View itemView) {
            super(itemView);



        }
    }








    //轮播图滑动动画
    static class TransFormer implements ViewPager2.PageTransformer {

        @Override
        public void transformPage(@NonNull View page, float position) {

//            if (position >= -1.0f && position <= 0.0f) {
//                //控制左侧滑入或者滑出的缩放比例
//                page.setScaleX(1 + position * 0.1f);
//                page.setScaleY(1 + position * 0.2f);
//            } else if (position > 0.0f && position < 1.0f) {
//                //控制右侧滑入或者滑出的缩放比例
//                page.setScaleX(1 - position * 0.1f);
//                page.setScaleY(1 - position * 0.2f);
//            } else {
//                //控制其他View缩放比例
                page.setScaleX(0.96f);
 //               page.setScaleY(0.9f);
//            }
        }
    }


}

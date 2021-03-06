package com.example.ouchaixun.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.Activity.NewsDetilsActivity;
import com.example.ouchaixun.Data.News;
import com.example.ouchaixun.Data.ViewPagerData;
import com.example.ouchaixun.Fragment.UserCenterFragment;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

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

//        recyclerView.getScroll
         list.get(0).setVisibility(isVisibility);
        notifyItemChanged(0);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view_news = null;
        RecyclerView.ViewHolder holder = null;

        if (i == ITEM_NO) {
            view_news = LayoutInflater.from(context).inflate(R.layout.item_nodata, viewGroup, false);
            holder = new NoHolder(view_news);
        }


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
        if (i == ITEM_ERROR) {
            view_news = LayoutInflater.from(context).inflate(R.layout.item_nointernet, viewGroup, false);
            holder = new ErrorHolder(view_news);
        }
        assert holder != null;
        return holder;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int i) {

        //?????????
        if (holder instanceof NoHolder) {

        }
        //??????
        if (holder instanceof NewsHolder) {

            ((NewsHolder) holder).title.setText(list.get(i).getTitle());
            ((NewsHolder) holder).nickname.setText(list.get(i).getNickName());
            Glide.with(context)
                    .load("http://47.102.215.61:8888/"+list.get(i).getImg())
                    .error(R.drawable.img_error)
                    .into(((NewsHolder) holder).imageView);



            if (list.get(i).getIntop()){
                ((NewsHolder) holder).intop.setText("??????");
                ((NewsHolder) holder).intop.setTextColor(context.getResources().getColor(R.color.orange));
                ((NewsHolder) holder).intop.setBackgroundResource(R.drawable.intop);
                ((NewsHolder) holder).official.setVisibility(View.VISIBLE);
            }


            ((NewsHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, NewsDetilsActivity.class);
                    intent.putExtra("id",list.get(i).getNews_id());
                    context.startActivity(intent);
                }
            });



        }
//////////////////////////////////////////????????????
        if (holder instanceof HeaderHolder) {
            MyData myData = new MyData(context);
            String token = myData.load_token();


            final List<News> listheader=new ArrayList<>();
            LinearLayoutManager layoutManager=new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ((HeaderHolder)holder).recyclerView.setLayoutManager(layoutManager);

            OKhttpUtils.get_token(token, "http://47.102.215.61:8888/self/mystar?page=1&type=1", new OKhttpUtils.OkhttpCallBack() {
                @Override
                public void onSuccess(Response response)  {
                    try {
                        JSONObject jsonObject=new JSONObject(response.body().string());
                        Log.i("asdfff",jsonObject.toString());
                        JSONArray jsonArray=jsonObject.getJSONArray("data");

                        if (jsonArray.length()>0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                News news = new News();
                                news.setNews_id(jsonObject1.optInt("news_id"));
                                news.setTitle(jsonObject1.getString("title"));
                                news.setNickName(jsonObject1.getString("writer_nickname"));
                                news.setType(9);
                                listheader.add(news);
                            }

                        }else {
                            News news = new News();
                            news.setType(1);
                            listheader.add(news);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            headerAdapter=new HeaderAdapter(context,listheader);
                            ((HeaderHolder)holder).recyclerView.setAdapter(headerAdapter);

//                            Toast.makeText(context,"???",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFail(String error) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            News news=new News();
                            news.setType(0);
                            listheader.add(news);
                            headerAdapter=new HeaderAdapter(context,listheader);
                            ((HeaderHolder)holder).recyclerView.setAdapter(headerAdapter);
                            Toast.makeText(context,"?????????????????????\n?????????????????????",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


            ((HeaderHolder)holder).setVisibility(list.get(i).getVisibility());



        }

        //  ?????????
        if (holder instanceof PagerHolder) {
            List<ViewPagerData>  img = list.get(i).getPager();
            Log.i("asd",img.toString());
            viewPagerAdapter = new ViewPagerAdapter(context, img);


            ((PagerHolder) holder).viewPager2.setOffscreenPageLimit(1);
            //????????????
            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(10));
            compositePageTransformer.addTransformer(new TransFormer());
            //????????????
            ((PagerHolder) holder).viewPager2.setPageTransformer(compositePageTransformer);
            View recyclerView = ((PagerHolder) holder).viewPager2.getChildAt(0);
            if (recyclerView != null && recyclerView instanceof RecyclerView) {
                recyclerView.setPadding(80, 0, 80, 0);
                ((RecyclerView) recyclerView).setClipToPadding(false);
            }


            ((PagerHolder) holder).viewPager2.setAdapter(viewPagerAdapter);
            ((PagerHolder) holder).viewPager2.setCurrentItem(1);
            // ????????????
            final List<ViewPagerData> finalImg = img;
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
                    // ViewPager.SCROLL_STATE_IDLE ??????????????????????????????????????????????????????????????????????????????????????????
                    // ????????????????????? setCurrentItem ???????????????????????????????????????????????????
                    if (state != ViewPager.SCROLL_STATE_IDLE) return;

                    // ????????????????????????????????????????????????????????????????????????
                    if (currentPosition == 0) {
                        ((PagerHolder) holder).viewPager2.setCurrentItem(finalImg.size() - 2, false);

                    } else if (currentPosition == finalImg.size() - 1) {
                        // ???????????????????????????,??????????????????????????????????????????
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
            Log.i("asd", "??????");
            return ITEM_ERROR;
        }else if (list.get(i).getType()==1){
            return ITEM_NO;
        }else if (list.get(i).getType()==2){
            Log.i("asd", "???????????????");
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


        TextView intop,official,title,nickname;
        ImageView imageView;
        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            intop=itemView.findViewById(R.id.news_intop);
            official=itemView.findViewById(R.id.news_official);
            title=itemView.findViewById(R.id.news_title);
            nickname=itemView.findViewById(R.id.news_hint);
            imageView=itemView.findViewById(R.id.news_img);

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
        public NoHolder(@NonNull View itemView) {
            super(itemView);

        }
    }








    //?????????????????????
    static class TransFormer implements ViewPager2.PageTransformer {

        @Override
        public void transformPage(@NonNull View page, float position) {

//            if (position >= -1.0f && position <= 0.0f) {
//                //?????????????????????????????????????????????
//                page.setScaleX(1 + position * 0.1f);
//                page.setScaleY(1 + position * 0.2f);
//            } else if (position > 0.0f && position < 1.0f) {
//                //?????????????????????????????????????????????
//                page.setScaleX(1 - position * 0.1f);
//                page.setScaleY(1 - position * 0.2f);
//            } else {
//                //????????????View????????????
                page.setScaleX(0.96f);
 //               page.setScaleY(0.9f);
//            }
        }
    }


}

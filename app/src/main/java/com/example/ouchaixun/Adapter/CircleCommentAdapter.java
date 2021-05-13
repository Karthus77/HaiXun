package com.example.ouchaixun.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Response;


public class CircleCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String,Object>> list;
    private View inflater;
    private static String token=null;
    public void changeLike(ImageView view)
    {
        if (view.getDrawable().getCurrent().getConstantState()==view.getResources().getDrawable(R.drawable.circle_like).getConstantState())
            view.setImageResource(R.drawable.islike);
        else
            view.setImageResource(R.drawable.circle_like);
    }
    public void setNumber(TextView textView,String num)
    {
        if(Integer.parseInt(num)>99)
        {
            textView.setText("99+");
        }
        else
        {
            textView.setText(num);
        }
    }
    public CircleCommentAdapter(Context context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater= LayoutInflater.from(context).inflate(R.layout.item_circle_comment,parent,false);
        ViewHolder viewHolder=new ViewHolder(inflater);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewholder = (ViewHolder) holder;
        viewholder.content.setText(list.get(position).get("content").toString());
        String welike=list.get(position).get("islike").toString();
        if(welike.equals("1"))
            viewholder.islike.setImageResource(R.drawable.islike);
        viewholder.name.setText(list.get(position).get("name").toString());
        final String id =list.get(position).get("id").toString();
        final String num_like=list.get(position).get("likes").toString();
        setNumber(viewholder.likes,num_like);
        viewholder.time.setText(list.get(position).get("time").toString());
        Glide.with(context).load(list.get(position).get("head")).circleCrop().into(viewholder.head);
        viewholder.islike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson=new Gson();
                Post_like postLike=new Post_like();
                postLike.id=Integer.parseInt(id);
                postLike.type=2;
                if (viewholder.islike.getDrawable().getCurrent().getConstantState()==viewholder.islike.getResources().getDrawable(R.drawable.circle_like).getConstantState())
                    postLike.action=1;
                else
                    postLike.action=2;
                changeLike(viewholder.islike);
                setNumber(viewholder.likes,String.valueOf(Integer.parseInt(num_like)+1));
                try {
                    OKhttpUtils.post_json(token, "http://47.102.215.61:8888/whole/like",gson.toJson(postLike) , new OKhttpUtils.OkhttpCallBack() {
                        @Override
                        public void onSuccess(Response response) throws IOException {
                            String responseData = response.body().string();
                        }
                        @Override
                        public void onFail(String error) {
                            Toast.makeText(context,"网络连接好像出问题了",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView head;
        TextView time;
        TextView content;
        TextView name;
        ImageView islike;
        TextView likes;
        public ViewHolder(View view) {
            super(view);
            head=view.findViewById(R.id.comment_userHead);
            time=view.findViewById(R.id.comment_time);
            content=view.findViewById(R.id.comment_content);
            name=view.findViewById(R.id.comment_name);
            likes=view.findViewById(R.id.comment_likenums);
            islike=view.findViewById(R.id.click_like);


        }
    }
}

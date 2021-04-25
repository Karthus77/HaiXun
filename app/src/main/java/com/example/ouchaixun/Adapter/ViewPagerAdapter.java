package com.example.ouchaixun.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import com.example.ouchaixun.Activity.NewsDetilsActivity;
import com.example.ouchaixun.Data.ViewPagerData;
import com.example.ouchaixun.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private List<ViewPagerData> img;
    private Context context;
    public ViewPagerAdapter(Context context, List<ViewPagerData> img) {
        this.context=context;
        this.img=img;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewpager_item, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {

        Glide.with(context)
                .load(img.get(i).getImg())
                .error(R.drawable.ic_home_black_24dp)
                .apply(bitmapTransform(new RoundedCornersTransformation(15, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, NewsDetilsActivity.class);
                intent.putExtra("id",img.get(i).getNews_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return img.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        ImageView imageView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.viewpager_img);

    }
}

    }



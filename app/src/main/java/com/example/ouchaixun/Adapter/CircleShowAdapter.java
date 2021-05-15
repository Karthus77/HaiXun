package com.example.ouchaixun.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.Activity.ReportCircleActivity;
import com.example.ouchaixun.R;

import java.util.List;
import java.util.Map;

public class CircleShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Map<String, Object>> list;
    private View inflater;
    private Dialog bottomDialog;

    public CircleShowAdapter(Context context, List<Map<String, Object>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context).inflate(R.layout.item_smallpic, parent, false);
        RecyclerView.ViewHolder ViewHolder = new CircleShowAdapter.ViewHolder(inflater);
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewholder = (ViewHolder) holder;
            Glide.with(context).load(list.get(position).get("url")).into(viewholder.img);
        final Dialog dialog=new Dialog(context,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_showpic, null);
         ImageView big=contentView.findViewById(R.id.big_pic);

        Glide.with(context).load(list.get(position).get("url")).into(big);
       dialog.setContentView(contentView);
            viewholder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });
            big.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            big.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    bottomDialog = new Dialog(context, R.style.Theme_Design_BottomSheetDialog);
                    View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_circlephotopick, null);
                    TextView tv_camera = contentView.findViewById(R.id.tv_camera);
                    TextView tv_chose = contentView.findViewById(R.id.tv_chose);
                    TextView tv_cancle = contentView.findViewById(R.id.tv_cancle);
                    bottomDialog.setContentView(contentView);
                    ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                    contentView.setLayoutParams(layoutParams);
                    bottomDialog.getWindow().setGravity(Gravity.BOTTOM);//弹窗位置
                    bottomDialog.getWindow().setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);//弹窗样式
                    bottomDialog.show();
                    tv_camera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            bottomDialog.dismiss();

                        }
                    });
                    tv_chose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            bottomDialog.dismiss();
                        }
                    });
                    tv_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomDialog.dismiss();
                        }
                    });
                    return true;
                    }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.small_pic);
        }
    }
}

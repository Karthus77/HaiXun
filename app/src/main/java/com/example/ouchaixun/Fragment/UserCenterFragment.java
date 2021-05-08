package com.example.ouchaixun.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.ouchaixun.Activity.LoginActivity;
import com.example.ouchaixun.Activity.RegisterActivity;
import  com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserCenterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private LinearLayout exit;
    private LinearLayout about;
    private LinearLayout change_password;
    private LinearLayout create_center;
    private LinearLayout history;
    private LinearLayout collection;
    private ImageView edit;
    private TextView tv_name;
    private TextView tv_info;
    private ImageView iv_head_pic;
    private ImageView iv_sex;
    private String name;
    private String info;
    private String gender;
    private String avatar;
    public UserCenterFragment() {
        // Required empty public constructor
    }


    public static UserCenterFragment newInstance(String param1, String param2) {
        UserCenterFragment fragment = new UserCenterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_center, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       exit = view.findViewById(R.id.www996);
        edit = view.findViewById(R.id.imageView4);
        about = view.findViewById(R.id.aaaat);
        change_password = view.findViewById(R.id.linearLayout99);
        create_center = view.findViewById(R.id.linearLayout3);
        history = view.findViewById(R.id.linearLayout2);
        collection = view.findViewById(R.id.linearLayout);
        tv_name = view.findViewById(R.id.textView4);
        tv_info = view.findViewById(R.id.textView5);
        iv_sex = view.findViewById(R.id.imageView2);
        iv_head_pic = view.findViewById(R.id.imageView8);

    }
    @Override
    public void onResume() {
        super.onResume();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        final MyData myData = new MyData(getContext());
        final String my_token = myData.load_token();
        if (myData.load_xx()) {
            tv_name.setText(myData.load_name());
            tv_info.setText(info);
            Glide.with(getContext()).load(myData.load_pic_url())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .into(iv_head_pic);
            if(gender=="保密"){
                iv_sex.setImageResource(R.drawable.no_sex);
            }else if(gender=="男"){
                iv_sex.setImageResource(R.drawable.sex_girl);
            }else{
                iv_sex.setImageResource(R.drawable.sex_boy);
            }
        }
        if (my_token != "NO") {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = "http://47.102.215.61:8888/self/info";
                    OkHttpClient okHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .addHeader("Authorization", my_token)
                            .build();
                    Call call = okHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("1233gg", "onFailure: " + e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();
                            Log.d("1233gg", "onResponse: " + responseData);
                            try {
                                JSONObject jsonObject1 = new JSONObject(responseData);
                                int code = jsonObject1.getInt("code");
                                final String msg = jsonObject1.getString("msg");
                                if (code != 200) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {

                                    JSONObject jsonObject2 = jsonObject1.getJSONObject("data");

                                    name = jsonObject2.getString("nickname");
                                    if (name.length() > 6) {
                                        name = name.substring(0, 6);
                                    }
                                    info = jsonObject2.getString("description");
                                    gender = jsonObject2.getString("gender");
                                    avatar = "http://47.102.215.61:8888"+jsonObject2.getString("avatar");
                                    myData.save_info(info);
                                    myData.save_name(name);
                                    myData.save_sex(gender);
                                    myData.save_pic_url(avatar);
                                    Log.d("1233gg", "nassme");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv_name.setText(name);
                                            Glide.with(getContext()).load(avatar)
                                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                                    .into(iv_head_pic);
                                            tv_info.setText(info);
                                            myData.save_xx(true);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();
        }

    }

}
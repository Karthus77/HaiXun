package com.example.ouchaixun.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ouchaixun.Activity.LoginActivity;
import com.example.ouchaixun.Activity.RegisterActivity;
import  com.example.ouchaixun.R;

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
}
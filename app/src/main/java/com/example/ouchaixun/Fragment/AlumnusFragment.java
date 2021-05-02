package com.example.ouchaixun.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.ouchaixun.Adapter.CircleFragmentAdapter;
import com.example.ouchaixun.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AlumnusFragment extends Fragment {
    private CircleFragmentAdapter fragmentAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> titleList=new ArrayList<>();



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alumnus, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("news", "onViewCreated ");
        fragments.clear();
        titleList.clear();
        fragments.add(new HotAreaFragment());
        fragments.add(new TimeAreaFragment());
        titleList.add("热帖区");
        titleList.add("实时区");
        viewPager= Objects.requireNonNull(getActivity()).findViewById(R.id.vp_fragment);
        tabLayout=getActivity().findViewById(R.id.tab1);
        fragmentAdapter =new CircleFragmentAdapter(getChildFragmentManager(),fragments,titleList);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);





    }
}
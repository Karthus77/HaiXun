package com.example.ouchaixun.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ouchaixun.Activity.ReportSquareActivity;
import com.example.ouchaixun.R;


public class SquareFragment extends Fragment {
    private ImageView wall;
    private ImageView carpool;
    private ImageView table;
    private ImageView bullshit;
    private ImageView treehole;
    private ImageView post;
    private SquareWallFragment squareWallFragment=new SquareWallFragment();
    private SquareCarFragment squareCarFragment=new SquareCarFragment();
    private SquareTableFragment squareTableFragment=new SquareTableFragment();
    private SquareBullshitFragment squareBullshitFragment=new SquareBullshitFragment();
    private SquareHoleFragment squareHoleFragment=new SquareHoleFragment();

    public void hideFragment(FragmentTransaction transaction){
        if(squareWallFragment != null){
            transaction.hide(squareWallFragment);
        }
        if(squareCarFragment != null){
            transaction.hide(squareCarFragment);
        }
        if(squareTableFragment != null){
            transaction.hide(squareTableFragment);
        }
        if(squareBullshitFragment != null){
            transaction.hide(squareBullshitFragment);
        }
        if(squareHoleFragment != null){
            transaction.hide(squareHoleFragment);
        }
    }
    public void chooseFragment(Fragment fragment,ImageView imageView)
    {
        falseAll();
        imageView.setSelected(true);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if(!fragment.isAdded())
        {
            transaction.add(R.id.squareChild_fragment,fragment);
        }
        hideFragment(transaction);
        transaction.show(fragment);
        transaction.commit();
    }


    public void falseAll()
    {
        wall.setSelected(false);
        carpool.setSelected(false);
        table.setSelected(false);
        treehole.setSelected(false);
        bullshit.setSelected(false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_square, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wall=view.findViewById(R.id.square_wall);
        post=view.findViewById(R.id.publish_square);
        table=view.findViewById(R.id.square_table);
        carpool=view.findViewById(R.id.square_carpool);
        bullshit=view.findViewById(R.id.square_bullshit);
        treehole=view.findViewById(R.id.square_treehole);
        wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFragment(squareWallFragment,wall);
            }
        });
        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFragment(squareTableFragment,table);
            }
        });
        carpool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFragment(squareCarFragment,carpool);
            }
        });
        treehole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFragment(squareHoleFragment,treehole);
            }
        });
        bullshit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFragment(squareBullshitFragment,bullshit);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ReportSquareActivity.class);
                startActivity(intent);
            }
        });
    }
}
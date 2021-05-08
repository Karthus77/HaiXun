package com.example.ouchaixun.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ouchaixun.Fragment.AlumnusFragment;
import com.example.ouchaixun.Fragment.NewsFragment;
import com.example.ouchaixun.Fragment.SquareFragment;
import com.example.ouchaixun.Fragment.UserCenterFragment;
import com.example.ouchaixun.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private NewsFragment newsFragment=new NewsFragment();
    private AlumnusFragment alumnusFragment=new AlumnusFragment();
    private SquareFragment squareFragment=new SquareFragment();
    private UserCenterFragment userCenterFragment=new UserCenterFragment();
    private Fragment[] fragments;
    private int lastfragment;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 进入这儿表示没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
        initFragment();
    }



    private void initFragment()
    {
        //绑定空间
        fragments = new Fragment[]{newsFragment,alumnusFragment,squareFragment,userCenterFragment};

        //改变顺序1.改下一行数字/对应上一行顺序2.
        lastfragment=2;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,squareFragment).show(squareFragment).commit();
        navigationView=(BottomNavigationView)findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(changeFragment);
    }

    //判断选择的菜单
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.item_News:
                {
                    if(lastfragment!=0)
                    {
                        switchFragment(lastfragment,0);
                        lastfragment=0;
                    }
                    return true;
                }
                case R.id.item_Alumnus:
                {
                    if(lastfragment!=1)
                    {
                        switchFragment(lastfragment,1);
                        lastfragment=1;
                    }
                    return true;
                }
                case R.id.item_Square:
                {
                    if(lastfragment!=2)
                    {
                        switchFragment(lastfragment,2);
                        lastfragment=2;
                    }
                    return true;
                }
                case R.id.item_User:
                {
                    if(lastfragment!=3)
                    {
                        switchFragment(lastfragment,3);
                        lastfragment=3;
                    }
                    return true;
                }

            }


            return false;
        }
    };


    //切换Fragment
    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(fragments[index].isAdded()==false)
        {
            transaction.add(R.id.fragment,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }
}
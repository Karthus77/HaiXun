package com.example.ouchaixun.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.ouchaixun.Fragment.AlumnusFragment;
import com.example.ouchaixun.Fragment.NewsFragment;
import com.example.ouchaixun.Fragment.SquareFragment;
import com.example.ouchaixun.Fragment.UserCenterFragment;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

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
    private String name;
    private String info;
    private String gender;
    private String avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

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
            // ??????????????????????????????
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
        initFragment();


        final MyData myData=new MyData(MainActivity.this);
        OKhttpUtils.get_token(myData.load_token(), "http://47.102.215.61:8888/self/info", new OKhttpUtils.OkhttpCallBack() {
            @Override
            public void onSuccess(Response response)  {



                try {
                    JSONObject jsonObject=new JSONObject(response.body().string());
                    JSONObject jsonObject2=jsonObject.getJSONObject("data");
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
                    myData.save_account(jsonObject2.getString("account"));
                    myData.save_type(jsonObject2.getString("type"));
                    Log.d("1233gg", "type");



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String error) {

            }
        });


    }



    private void initFragment()
    {
        //????????????
        fragments = new Fragment[]{newsFragment,alumnusFragment,squareFragment,userCenterFragment};

        //????????????1.??????????????????/?????????????????????2.
        lastfragment=2;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,squareFragment).show(squareFragment).commit();
        navigationView=(BottomNavigationView)findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(changeFragment);
    }

    //?????????????????????
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


    //??????Fragment
    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//????????????Fragment
        if(fragments[index].isAdded()==false)
        {
            transaction.add(R.id.fragment,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }
}
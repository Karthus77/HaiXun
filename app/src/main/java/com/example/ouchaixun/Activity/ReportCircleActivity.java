package com.example.ouchaixun.Activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.Adapter.NineReportAdapter;
import com.example.ouchaixun.Adapter.OnAddPicturesListener;
import com.example.ouchaixun.Data.Circleback;
import com.example.ouchaixun.Data.picback;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.example.ouchaixun.Utils.Regex;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ReportCircleActivity extends AppCompatActivity {
    private ImageView back;
    private String token;
    private EditText edit_content;
    private Button post;
    private RecyclerView recyclerView;
    private NineReportAdapter nineReportAdapter;
    private ImageView head;
    private String paper_title=null;
    private String paper_content=null;
    private int paper_tag=0;
    private Uri photoUri;   //??????????????????????????????
    private File outputImage;
    private static final int GET_BACKGROUND_FROM_CAPTURE_RESOULT = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int TAKE_PHOTO = 3;
    final List<Map<String,Object>> list = new ArrayList<>();
    private List<String> IdList=new ArrayList<>();
    private Dialog bottomDialog;

    private void selectCamera() {

        //??????file?????????????????????????????????????????????????????????????????????????????????
        outputImage = new File(this.getExternalCacheDir(), "camera_photos.jpg");
        try {
            //?????????????????????????????????????????????????????????
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoUri = Uri.fromFile(outputImage);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, TAKE_PHOTO);

    }
    public static final String STR_IMAGE = "image/*";
    //????????????
    private void selectPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STR_IMAGE);
        startActivityForResult(intent, GET_BACKGROUND_FROM_CAPTURE_RESOULT);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {

            case RESULT_REQUEST_CODE:   //????????????
                final String selectPhoto = getRealPathFromUri(this,cropImgUri);
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaType = MediaType.parse("text/plain");
                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("picture",selectPhoto,
                                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                                    new File(selectPhoto)))
                                    .addFormDataPart("type", "1")
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://47.102.215.61:8888/whole/picture")
                                    .method("POST", body)
                                    .addHeader("Authorization", token)
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
                            Log.i("lll",responseData);
                            Gson gson=new Gson();
                            picback picback =gson.fromJson(responseData, com.example.ouchaixun.Data.picback.class);
                            String url=picback.getPic_url();
                            int id=picback.getPic_id();
                            int size=list.size();
                            list.remove(size-1);
                            Map<String,Object> map=new HashMap<>();
                            map.put("type",1);
                            map.put("uri",url);
                            list.add(map);
                            IdList.add(String.valueOf(id));
                            if(list.size()!=9) {
                                Map<String, Object> map2 = new HashMap<>();
                                map2.put("type", 1);
                                list.add(map2);}
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nineReportAdapter.notifyDataSetChanged();
                            }
                        });


                    }
                });
                thread.start();


                break;

            case TAKE_PHOTO://   ????????????
                cropRawPhoto(photoUri);

                break;
            case GET_BACKGROUND_FROM_CAPTURE_RESOULT:
                photoUri = data.getData();
                cropRawPhoto(photoUri);


        }





    }
    private Uri cropImgUri;
    public void cropRawPhoto(Uri uri) {
        //??????file???????????????????????????????????????
        File cropImage = new File(Environment.getExternalStorageDirectory(), "crop_image.jpg");
        String path = cropImage.getAbsolutePath();
        try {
            if (cropImage.exists()) {
                cropImage.delete();
            }
            cropImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cropImgUri = Uri.fromFile(cropImage);
        Intent intent = new Intent("com.android.camera.action.CROP");
//???????????????uri
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 200);
        intent.putExtra("aspectY", 200);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
//??????????????????uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImgUri);
//??????????????????
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }
    public static String getRealPathFromUri(Context context, Uri uri) {
        if(context == null || uri == null) {
            return null;
        }
        if("file".equalsIgnoreCase(uri.getScheme())) {
            return getRealPathFromUri_Byfile(context,uri);
        } else if("content".equalsIgnoreCase(uri.getScheme())) {
            return getRealPathFromUri_Api11To18(context,uri);
        }
//        int sdkVersion = Build.VERSION.SDK_INT;
//        if (sdkVersion < 11) {
//            // SDK < Api11
//            return getRealPathFromUri_BelowApi11(context, uri);
//        }

//        // SDK > 19
        return getRealPathFromUri_AboveApi19(context, uri);//?????????
    }
    private static String getRealPathFromUri_Byfile(Context context,Uri uri){
        String uri2Str = uri.toString();
        String filePath = uri2Str.substring(uri2Str.indexOf(":") + 3);
        return filePath;
    }
    @SuppressLint("NewApi")
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = null;

        wholeID = DocumentsContract.getDocumentId(uri);

        // ??????':'??????
        String id = wholeID.split(":")[1];

        String[] projection = { MediaStore.Images.Media.DATA };
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = { id };

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    /**
     * //??????api11-api18,??????uri??????????????????????????????
     * ????????????URI?????????Uri:: content://media/external/images/media/1028
     */
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };

        CursorLoader loader = new CursorLoader(context, uri, projection, null,
                null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * ??????api11??????(?????????api11),??????uri???????????????????????????
     */
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_circle);
        MyData myData = new MyData(ReportCircleActivity.this);
        token = myData.load_token();
        head=findViewById(R.id.report_default);
        Glide.with(ReportCircleActivity.this).load(myData.load_pic_url()).circleCrop().into(head);
        back=findViewById(R.id.report_back);
        recyclerView=findViewById(R.id.edit_image);
        nineReportAdapter=new NineReportAdapter(ReportCircleActivity.this,list);
        GridLayoutManager manager=new GridLayoutManager(this,3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        edit_content=findViewById(R.id.report_content);
        post=findViewById(R.id.report);
        Map<String,Object> map=new HashMap<>();
        map.put("type",1);
        list.add(map);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(nineReportAdapter);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        nineReportAdapter.setOnAddPicturesListener(new OnAddPicturesListener() {
            @Override
            public void onAdd() {
                bottomDialog = new Dialog(ReportCircleActivity.this, R.style.Theme_Design_BottomSheetDialog);
                View contentView = LayoutInflater.from(ReportCircleActivity.this).inflate(R.layout.dialog_circlephotopick, null);
                TextView tv_camera = contentView.findViewById(R.id.tv_camera);
                TextView tv_chose = contentView.findViewById(R.id.tv_chose);
                TextView tv_cancle = contentView.findViewById(R.id.tv_cancle);
                bottomDialog.setContentView(contentView);
                ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                layoutParams.width = getResources().getDisplayMetrics().widthPixels;
                contentView.setLayoutParams(layoutParams);
                bottomDialog.getWindow().setGravity(Gravity.BOTTOM);//????????????
                bottomDialog.getWindow().setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);//????????????
                bottomDialog.show();
                tv_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectCamera();
                        bottomDialog.dismiss();

                    }
                });
                tv_chose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectPhoto();
                        bottomDialog.dismiss();
                    }
                });
                tv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomDialog.dismiss();
                    }
                });
            }

        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_content.getText().toString().equals(""))
                {
                    Toast.makeText(ReportCircleActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String ids=IdList.toString();
                    String content=edit_content.getText().toString();
                    String s=ids.substring(1,ids.length()-1);
                    Regex regex=new Regex();
                    if(regex.checkContinuous(content))
                    {
                        Toast.makeText(ReportCircleActivity.this,"????????????????????????????????????",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                    final String img_ids="["+s+"]";
                    final String a="{\"content\":"+"\""+content+"\""+",\"id_list\":"+IdList+"}";
                    Log.v("rec",a);
                    try {
                        OKhttpUtils.post_json(token,"http://47.102.215.61:8888/school/release_talk",a, new OKhttpUtils.OkhttpCallBack() {
                            @Override
                            public void onSuccess(Response response)  {
                                try {
                                    String responseData = response.body().string();
                                    Log.i("rec",responseData);
                                    Gson gson=new Gson();
                                    final Circleback circleback=gson.fromJson(responseData,Circleback.class);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ReportCircleActivity.this,circleback.getMsg(),Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        if (circleback.getCode()==200)
                                        {
                                            finish();
                                        }

                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFail(String error) {
                                Log.i("rec",error);
                            }
                        });
                    }  catch (Exception e) {
                    e.printStackTrace();
                }

                }}
            }
        });
    }

}
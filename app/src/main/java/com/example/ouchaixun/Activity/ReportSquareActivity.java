package com.example.ouchaixun.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
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
import android.os.Bundle;
import android.os.Environment;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class ReportSquareActivity extends AppCompatActivity {
    private ImageView tag_wall;
    private ImageView tag_car;
    private ImageView tag_table;
    private ImageView tag_hole;
    private ImageView tag_bullshit;
    private SwitchCompat anonymous;
    private EditText title;
    private EditText content;
    private RecyclerView recyclerView;
    private NineReportAdapter nineReportAdapter;
    private ImageView back;
    private Button post;private Uri photoUri;   //??????????????????????????????
    private File outputImage;
    private static final int GET_BACKGROUND_FROM_CAPTURE_RESOULT = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int TAKE_PHOTO = 3;
    final List<Map<String,Object>> list = new ArrayList<>();
    private String token;
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
                            MediaType mediaType = MediaType.parse("multipart/form-data");
                            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("picture",selectPhoto,
                                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                                    new File(selectPhoto)))
                                    .addFormDataPart("type", "2")
                                    .build();
                            Request request = new Request.Builder()
                                    .url("http://47.102.215.61:8888/whole/picture")
                                    .method("POST", body)
                                    .addHeader("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MjEyNjQ5MTcsImlhdCI6MTYyMDY2MDExNywiaXNzIjoicnVhIiwiZGF0YSI6eyJ1c2VyaWQiOjN9fQ.JyfnK3uRjTCBnCL9-UdyKrTEkUlvLSR_p9SasjWooEo")
                                    .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                                    .build();
                            Response response = client.newCall(request).execute();
                            String responseData = response.body().string();
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


    public void setTag(final ImageView view)
    {

                tag_table.setSelected(false);
                tag_hole.setSelected(false);
                tag_bullshit.setSelected(false);
                tag_car.setSelected(false);
                tag_wall.setSelected(false);
                view.setSelected(true);
    }
    public  int getAnonymous(final  SwitchCompat switchCompat)
    {
        if(switchCompat.isChecked())
        {
            return 1;
        }
        else
            return 2;
    }
    public  int getTag()
    {
        if(tag_wall.isSelected())
            return 1;
        else if(tag_hole.isSelected())
            return 2;
        else if (tag_bullshit.isSelected())
            return 3;
        else  if (tag_car.isSelected())
            return 4;
        else  if(tag_table.isSelected())
            return 5;
        else
            return 0;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_square);
        MyData myData = new MyData(ReportSquareActivity.this);
        token = myData.load_token();
        recyclerView=findViewById(R.id.edit_square_image);
        nineReportAdapter=new NineReportAdapter(ReportSquareActivity.this,list);
        GridLayoutManager manager=new GridLayoutManager(this,3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        Map<String,Object> map=new HashMap<>();
        map.put("type",1);
        list.add(map);
        recyclerView.setLayoutManager(manager);
        anonymous=findViewById(R.id.check_anonymous);
        recyclerView.setAdapter(nineReportAdapter);
        back=findViewById(R.id.square_report_back);
        tag_wall=findViewById(R.id.tag_wall);
        tag_car=findViewById(R.id.tag_carpool);
        tag_bullshit=findViewById(R.id.tag_bullshit);
        tag_hole=findViewById(R.id.tag_hole);
        tag_table=findViewById(R.id.tag_table);
        title=findViewById(R.id.title_edit);
        post=findViewById(R.id.report);
        content=findViewById(R.id.edit_content);
        recyclerView=findViewById(R.id.edit_square_image);
        nineReportAdapter.setOnAddPicturesListener(new OnAddPicturesListener() {
            @Override
            public void onAdd() {
                bottomDialog = new Dialog(ReportSquareActivity.this, R.style.Theme_Design_BottomSheetDialog);
                View contentView = LayoutInflater.from(ReportSquareActivity.this).inflate(R.layout.dialog_circlephotopick, null);
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
        tag_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTag(tag_wall);
            }
        });
        tag_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTag(tag_car);
            }
        });
        tag_bullshit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTag(tag_bullshit);
            }
        });
        tag_hole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTag(tag_hole);
            }
        });
        tag_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTag(tag_table);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.equals(""))
                {
                    Toast.makeText(ReportSquareActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
                }
                else if(content.equals(""))
                {
                    Toast.makeText(ReportSquareActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
                }
                else if(getTag()==0)
                {
                    Toast.makeText(ReportSquareActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Regex regex=new Regex();
                    if(regex.checkContinuous(content.getText().toString()))
                    {
                        Toast.makeText(ReportSquareActivity.this,"????????????????????????????????????",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                    String square="{\"tag\":"+String.valueOf(getTag())+",\"title\":"+"\""+title.getText().toString()+"\""+",\"content\":"+"\""+content.getText().toString()+"\""+",\"anonymous\":"+ getAnonymous(anonymous) +",\"id_list\":"+IdList.toString()+"}";
                    Log.i("sqback",square);

                    try {
                        OKhttpUtils.post_json(token, "http://47.102.215.61:8888/passage/release_passage",square, new OKhttpUtils.OkhttpCallBack() {
                            @Override
                            public void onSuccess(Response response) throws IOException {
                                String responseData = response.body().string();
                                Log.i("sqback",responseData);
                                Gson gson=new Gson();
                                final Circleback circleback=gson.fromJson(responseData,Circleback.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ReportSquareActivity.this,circleback.getMsg(),Toast.LENGTH_SHORT).show();

                                    }
                                });
                                if (circleback.getCode()==200)
                                {
                                    finish();
                                }
                            }

                            @Override
                            public void onFail(String error) {
                                Log.i("sqback",error);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }}
            }
        });
    }
}
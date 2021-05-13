package com.example.ouchaixun.richtext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.Data.picback;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.CameraActivity;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class publishActivity extends AppCompatActivity implements View.OnClickListener {


    private Button post;private Uri photoUri;   //相机拍照返回图片路径
    private File outputImage;
    private static final int GET_BACKGROUND_FROM_CAPTURE_RESOULT = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int TAKE_PHOTO = 3;
    private void selectCamera() {

        //创建file对象，用于存储拍照后的图片，这也是拍照成功后的照片路径
        outputImage = new File(this.getExternalCacheDir(), "camera_photos.jpg");
        try {
            //判断文件是否存在，存在删除，不存在创建
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
    //选择相册
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

            case RESULT_REQUEST_CODE:   //相册返回



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


                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NotNull Call call, @NotNull Response response)  {
                                    try {
                                        Log.i("asd", response.body().string());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Log.i("asd", "OKHTTPUtils连接成功");
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                });
                thread.start();








                break;

            case TAKE_PHOTO://   拍照返回
                cropRawPhoto(photoUri);

                break;
            case GET_BACKGROUND_FROM_CAPTURE_RESOULT:
                photoUri = data.getData();
                cropRawPhoto(photoUri);


        }

    }
    private Uri cropImgUri;
    public void cropRawPhoto(Uri uri) {
        //创建file文件，用于存储剪裁后的照片
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
//设置源地址uri
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 200);
        intent.putExtra("aspectY", 200);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
//设置目的地址uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropImgUri);
//设置图片格式
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
        return getRealPathFromUri_AboveApi19(context, uri);//没用到
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

        // 使用':'分割
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
     * //适配api11-api18,根据uri获取图片的绝对路径。
     * 针对图片URI格式为Uri:: content://media/external/images/media/1028
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
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
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




    private String token;
    private String title,content;
    private Uri banner;
    private String  filePath=null, fileName=null;
    /********************View**********************/
    private EditText tv_title;
    private ImageView imageView;

    //文本编辑器
    private RichEditor mEditor;
    //加粗按钮
    private ImageView mBold;
    //颜色编辑器
    private TextView mTextColor;
    //显示显示View
    private LinearLayout llColorView;
    //预览按钮
    private TextView mPreView;
    //图片按钮
    private TextView mImage;
    //按序号排列（ol）
    private ImageView mListOL;
    //按序号排列（ul）
    private ImageView mListUL;
    //字体下划线
    private ImageView mLean;
    //字体倾斜
    private ImageView mItalic;
    //字体左对齐
    private ImageView mAlignLeft;
    //字体右对齐
    private ImageView mAlignRight;
    //字体居中对齐
    private ImageView mAlignCenter;
    //字体缩进
    private ImageView mIndent;
    //字体较少缩进
    private ImageView mOutdent;
    //字体索引
    private ImageView mBlockquote;
    //字体中划线
    private ImageView mStrikethrough;
    //字体上标
    private ImageView mSuperscript;
    //字体下标
    private ImageView mSubscript;
    /********************boolean开关**********************/
    //是否加粗
    boolean isClickBold = false;
    //是否正在执行动画
    boolean isAnimating = false;
    //是否按ol排序
    boolean isListOl = false;
    //是否按ul排序
    boolean isListUL = false;
    //是否下划线字体
    boolean isTextLean = false;
    //是否下倾斜字体
    boolean isItalic = false;
    //是否左对齐
    boolean isAlignLeft = false;
    //是否右对齐
    boolean isAlignRight = false;
    //是否中对齐
    boolean isAlignCenter = false;
    //是否缩进
    boolean isIndent = false;
    //是否较少缩进
    boolean isOutdent = false;
    //是否索引
    boolean isBlockquote = false;
    //字体中划线
    boolean isStrikethrough = false;
    //字体上标
    boolean isSuperscript = false;
    //字体下标
    boolean isSubscript = false;
    /********************变量**********************/
    //折叠视图的宽高
    private int mFoldedViewMeasureHeight;

    private boolean isheader=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_publish);
        MyData myData = new MyData(publishActivity.this);
        token = myData.load_token();
        initView();
        initClickListener();

        findViewById(R.id.publish_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("asd","</Div ><head><style>img{ width:100%   !important;}</style></head>"+mEditor.getHtml());

                title=tv_title.getText().toString();
                content="</Div ><head><style>img{ width:100%   !important;}</style></head>"+mEditor.getHtml();


                try {
                    OKhttpUtils.post_form(token, title, content, filePath, fileName, new OKhttpUtils.OkhttpCallBack() {
                        @Override
                        public void onSuccess(Response response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response.body().string());
                                final String msg=jsonObject.getString("msg");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(publishActivity.this,msg,Toast.LENGTH_SHORT).show();
                                        if (msg.equals("发布成功")){
                                            finish();
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFail(String error) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(publishActivity.this,"网络连接失败，请检查网络连接",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        findViewById(R.id.publish_add_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();                isheader=true;
            }
        });

        findViewById(R.id.news_publish_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化View
     */
    private void initView() {
        initEditor();
        initMenu();
        initColorPicker();
    }

    /**
     * 初始化文本编辑器
     */
    private void initEditor() {
        mEditor = findViewById(R.id.re_main_editor);
        //mEditor.setEditorHeight(400);
        //输入框显示字体的大小
        mEditor.setEditorFontSize(18);
        //输入框显示字体的颜色
        mEditor.setEditorFontColor(Color.BLACK);
        //输入框背景设置
        mEditor.setEditorBackgroundColor(Color.WHITE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        //输入框文本padding
        mEditor.setPadding(10, 10, 10, 10);
        //输入提示文本
        mEditor.setPlaceholder("请输入编辑内容");
        //是否允许输入
        //mEditor.setInputEnabled(false);
        //文本输入框监听事件
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.d("mEditor", "html文本：" + text);
            }
        });
    }

    /**
     * 初始化颜色选择器
     */
    private void initColorPicker() {
        ColorPickerView right = findViewById(R.id.cpv_main_color);
        right.setOnColorPickerChangeListener(new ColorPickerView.OnColorPickerChangeListener() {
            @Override
            public void onColorChanged(ColorPickerView picker, int color) {
                mTextColor.setBackgroundColor(color);
                mEditor.setTextColor(color);
            }

            @Override
            public void onStartTrackingTouch(ColorPickerView picker) {

            }

            @Override
            public void onStopTrackingTouch(ColorPickerView picker) {

            }
        });
    }

    /**
     * 初始化菜单按钮
     */
    private void initMenu() {
        tv_title=findViewById(R.id.publish_title);
        imageView=findViewById(R.id.publish_img);
        mBold = findViewById(R.id.button_bold);
        mTextColor = findViewById(R.id.button_text_color);
        llColorView = findViewById(R.id.ll_main_color);
        mPreView = findViewById(R.id.tv_main_preview);
        mImage = findViewById(R.id.button_image);
        mListOL = findViewById(R.id.button_list_ol);
        mListUL = findViewById(R.id.button_list_ul);
        mLean = findViewById(R.id.button_underline);
        mItalic = findViewById(R.id.button_italic);
        mAlignLeft = findViewById(R.id.button_align_left);
        mAlignRight = findViewById(R.id.button_align_right);
        mAlignCenter = findViewById(R.id.button_align_center);
       // mIndent = findViewById(R.id.button_indent);
      //  mOutdent = findViewById(R.id.button_outdent);
        //mBlockquote = findViewById(R.id.action_blockquote);
        mStrikethrough = findViewById(R.id.action_strikethrough);
       // mSuperscript = findViewById(R.id.action_superscript);
       // mSubscript = findViewById(R.id.action_subscript);

        getViewMeasureHeight();
    }

    /**
     * 获取控件的高度
     */
    private void getViewMeasureHeight() {
        //获取像素密度
        float mDensity = getResources().getDisplayMetrics().density;
        //获取布局的高度
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        llColorView.measure(w, h);
        int height = llColorView.getMeasuredHeight();
        mFoldedViewMeasureHeight = (int) (mDensity * height + 0.5);
    }

    private void initClickListener() {
        mBold.setOnClickListener(this);
        mTextColor.setOnClickListener(this);

        mImage.setOnClickListener(this);
        mListOL.setOnClickListener(this);
        mListUL.setOnClickListener(this);
        mLean.setOnClickListener(this);
        mItalic.setOnClickListener(this);
        mAlignLeft.setOnClickListener(this);
        mAlignRight.setOnClickListener(this);
        mAlignCenter.setOnClickListener(this);
       // mIndent.setOnClickListener(this);
        //mOutdent.setOnClickListener(this);
        // mBlockquote.setOnClickListener(this);
        mStrikethrough.setOnClickListener(this);
//        mSuperscript.setOnClickListener(this);
//        mSubscript.setOnClickListener(this);
        mPreView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_bold) {//字体加粗
            if (isClickBold) {
                mBold.setImageResource(R.mipmap.bold);
            } else {  //加粗
                mBold.setImageResource(R.mipmap.bold_);
            }
            isClickBold = !isClickBold;
            mEditor.setBold();
        } else if (id == R.id.button_text_color) {//设置字体颜色
            //如果动画正在执行,直接return,相当于点击无效了,不会出现当快速点击时,
            // 动画的执行和ImageButton的图标不一致的情况
            if (isAnimating) return;
            //如果动画没在执行,走到这一步就将isAnimating制为true , 防止这次动画还没有执行完毕的
            //情况下,又要执行一次动画,当动画执行完毕后会将isAnimating制为false,这样下次动画又能执行
            isAnimating = true;

            if (llColorView.getVisibility() == View.GONE) {
                //打开动画
                animateOpen(llColorView);
            } else {
                //关闭动画
                animateClose(llColorView);
            }
        }  else if (id == R.id.button_list_ol) {
//            if (isListOl) {
//                mListOL.setImageResource(R.mipmap.list_ol);
//            } else {
//                mListOL.setImageResource(R.mipmap.list_ol_);
//            }
            isListOl = !isListOl;
            mEditor.setNumbers();
        } else if (id == R.id.button_list_ul) {
//            if (isListUL) {
//                mListUL.setImageResource(R.mipmap.list_ul);
//            } else {
//                mListUL.setImageResource(R.mipmap.list_ul_);
//            }
            isListUL = !isListUL;
            mEditor.setBullets();
        } else if (id == R.id.button_underline) {
            if (isTextLean) {
                mLean.setImageResource(R.mipmap.underline);
            } else {
                mLean.setImageResource(R.mipmap.underline_);
            }
            isTextLean = !isTextLean;
            mEditor.setUnderline();
        } else if (id == R.id.button_italic) {
            if (isItalic) {
                mItalic.setImageResource(R.mipmap.lean);
            } else {
                mItalic.setImageResource(R.mipmap.lean_);
            }
            isItalic = !isItalic;
            mEditor.setItalic();
        } else if (id == R.id.button_align_left) {
//            if (isAlignLeft) {
//                mAlignLeft.setImageResource(R.mipmap.align_left);
//            } else {
//                mAlignLeft.setImageResource(R.mipmap.align_left_);
//            }
            isAlignLeft = !isAlignLeft;
            mEditor.setAlignLeft();
        } else if (id == R.id.button_align_right) {
//            if (isAlignRight) {
//                mAlignRight.setImageResource(R.mipmap.align_right);
//            } else {
//                mAlignRight.setImageResource(R.mipmap.align_right_);
//            }
            isAlignRight = !isAlignRight;
            mEditor.setAlignRight();
        } else if (id == R.id.button_align_center) {
//            if (isAlignCenter) {
//                mAlignCenter.setImageResource(R.mipmap.align_center);
//            } else {
//                mAlignCenter.setImageResource(R.mipmap.align_center_);
//            }
            isAlignCenter = !isAlignCenter;
            mEditor.setAlignCenter();
        }
//        else if (id == R.id.button_indent) {
//            if (isIndent) {
//                mIndent.setImageResource(R.mipmap.indent);
//            } else {
//                mIndent.setImageResource(R.mipmap.indent_);
//            }
//            isIndent = !isIndent;
//            mEditor.setIndent();
//        } else if (id == R.id.button_outdent) {
//            if (isOutdent) {
//                mOutdent.setImageResource(R.mipmap.outdent);
//            } else {
//                mOutdent.setImageResource(R.mipmap.outdent_);
//            }
//            isOutdent = !isOutdent;
//            mEditor.setOutdent();
//        }
        else if (id == R.id.action_strikethrough) {
//            if (isStrikethrough) {
//                mStrikethrough.setImageResource(R.mipmap.strikethrough);
//            } else {
//                mStrikethrough.setImageResource(R.mipmap.strikethrough_);
//            }
            isStrikethrough = !isStrikethrough;
            mEditor.setStrikeThrough();
        }
//        else if (id == R.id.action_blockquote) {
//            if (isBlockquote) {
//                mBlockquote.setImageResource(R.mipmap.blockquote);
//            } else {
//                mBlockquote.setImageResource(R.mipmap.blockquote_);
//            }
//            isBlockquote = !isBlockquote;
//            mEditor.setBlockquote();
//        }
//         else if (id == R.id.action_superscript) {
//            if (isSuperscript) {
//                mSuperscript.setImageResource(R.mipmap.superscript);
//            } else {
//                mSuperscript.setImageResource(R.mipmap.superscript_);
//            }
//            isSuperscript = !isSuperscript;
//            mEditor.setSuperscript();
//        } else if (id == R.id.action_subscript) {
//            if (isSubscript) {
//                mSubscript.setImageResource(R.mipmap.subscript);
//            } else {
//                mSubscript.setImageResource(R.mipmap.subscript_);
//            }
//            isSubscript = !isSubscript;
//            mEditor.setSubscript();
//        }

        //H1--H6省略，需要的自己添加

        else if (id == R.id.tv_main_preview) {//预览
            Intent intent = new Intent(publishActivity.this, WebDataActivity.class);
            intent.putExtra("diarys", mEditor.getHtml());
            title=tv_title.getText().toString();
            if (title==null){
                Toast.makeText(publishActivity.this,"还没有标题呀",Toast.LENGTH_SHORT).show();

            } if(banner==null){
                Toast.makeText(publishActivity.this,"还没有封面图片",Toast.LENGTH_SHORT).show();
            } else {
                intent.putExtra("banner", banner);
                intent.putExtra("title", tv_title.getText());
                startActivity(intent);
            }



        }else if (id == R.id.button_image) {//插入图片
            //这里的功能需要根据需求实现，通过insertImage传入一个URL或者本地图片路径都可以，这里用户可以自己调用本地相
            //或者拍照获取图片，传图本地图片路径，也可以将本地图片路径上传到服务器
            //返回在服务端的URL地址，将地址传如即可（我这里传了一张写死的图片URL，如果你插入的图片不现实，请检查你是否添加
            startActivity(new Intent(publishActivity.this, CameraActivity.class).putExtra(CameraActivity.ExtraType, CameraActivity.PHOTO));
            isheader=false;

//            mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
//                    "dachshund");
        }
    }

    /**
     * 开启动画
     *
     * @param view 开启动画的view
     */
    private void animateOpen(LinearLayout view) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(view, 0, mFoldedViewMeasureHeight);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
        animator.start();
    }

    /**
     * 关闭动画
     *
     * @param view 关闭动画的view
     */
    private void animateClose(final LinearLayout view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                isAnimating = false;
            }
        });
        animator.start();
    }


    /**
     * 创建动画
     *
     * @param view  开启和关闭动画的view
     * @param start view的高度
     * @param end   view的高度
     * @return ValueAnimator对象
     */
    private ValueAnimator createDropAnimator(final View view, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }


}

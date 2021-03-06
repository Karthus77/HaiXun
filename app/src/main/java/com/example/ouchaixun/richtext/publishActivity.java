package com.example.ouchaixun.richtext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.MyData;
import com.example.ouchaixun.Utils.OKhttpUtils;
import com.wildma.pictureselector.FileUtils;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class publishActivity extends AppCompatActivity implements View.OnClickListener {


    public static boolean LISTENING = false;
    private String token;
    private String title,content;
    private String banner;
    private int banner_id=0;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                final PictureBean pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);



                Thread thread=new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        try {


                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("picture",pictureBean.getPath(),
                                RequestBody.create(MediaType.parse("application/octet-stream"),
                                        new File(pictureBean.getPath())))
                        .build();
                Request request = new Request.Builder()
                        .url("http://47.102.215.61:8888/news/upload_pic")
                        .method("POST", body)
                        .addHeader("Authorization", token)

                        .addHeader("User-Agent", "apifox/1.0.0 (https://www.apifox.cn)")
                        .build();
                Response response = null;

                    response= client.newCall(request).execute();

                    final JSONObject jsonObject=new JSONObject(response.body().string());
                    final String msg=jsonObject.getString("msg");
                    if (msg.equals("一切正常")){

                       if (isheader){
                           banner_id=jsonObject.getInt("id");
                           banner=jsonObject.getString("url");

                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   Glide.with(publishActivity.this)
                                           .load(banner)
                                           .into(imageView);

                               }
                           });

                       }else {

                           final String url=jsonObject.getString("url");
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   mEditor.insertImage(url,
                                           "dachshund");
                               }
                           });

                       }

                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(publishActivity.this, msg,Toast.LENGTH_SHORT).show();


                            }
                        });
                    }
                         //   FileUtils.deleteAllCacheImage(publishActivity.this);


                    Log.i("asd",jsonObject.toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                thread.start();

                //使用 Glide 加载图片
//                Glide.with(this)
//                        .load(pictureBean.isCut() ? pictureBean.getPath() : pictureBean.getUri())
//                        .into(imageView);
            }
        }
    }







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
                Log.i("asd",mEditor.getHtml());

                title=tv_title.getText().toString();
                content=mEditor.getHtml();

                if (content.equals("")){
                    Toast.makeText(publishActivity.this, "还没有新闻呢",Toast.LENGTH_SHORT).show();

                }else if (title.equals("")){
                    Toast.makeText(publishActivity.this, "还没有新闻标题呢",Toast.LENGTH_SHORT).show();

                }else if (banner_id==0){
                    Toast.makeText(publishActivity.this, "还没有新闻封面呢",Toast.LENGTH_SHORT).show();

                }else {

                    try {
                        OKhttpUtils.post_form(token, title, content, banner_id, new OKhttpUtils.OkhttpCallBack() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onSuccess(Response response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                                    final String msg = jsonObject.getString("msg");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(publishActivity.this, msg, Toast.LENGTH_SHORT).show();
                                            if (msg.equals("发布成功")) {
                                                publishActivity.LISTENING = true;
                                                publishActivity.this.finish();
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

                                        Toast.makeText(publishActivity.this, "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        findViewById(R.id.publish_add_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector
                        .create(publishActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                        .selectPicture(true, 200, 100, 2, 1);
              isheader=true;
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
                Toast.makeText(publishActivity.this,"还没有封面图片呀",Toast.LENGTH_SHORT).show();
            } else {
                intent.putExtra("banner", banner);
                intent.putExtra("title", title);
                startActivity(intent);
            }



        }else if (id == R.id.button_image) {//插入图片
            //这里的功能需要根据需求实现，通过insertImage传入一个URL或者本地图片路径都可以，这里用户可以自己调用本地相
            //或者拍照获取图片，传图本地图片路径，也可以将本地图片路径上传到服务器
            //返回在服务端的URL地址，将地址传如即可（我这里传了一张写死的图片URL，如果你插入的图片不现实，请检查你是否添加
            PictureSelector
                    .create(publishActivity.this, PictureSelector.SELECT_REQUEST_CODE)
                    .selectPicture(true, 118, 82, 3, 2);
            isheader=false;

           // mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
           //         "dachshund");
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




    //java
    public static class HtmlFormat {

        public static String getNewContent(String htmltext){

            Document doc= Jsoup.parse(htmltext);

            Elements elements=doc.getElementsByTag("img");

            for (Element element : elements) {

                element.attr("width","100%").attr("height","auto");

            }

            return doc.toString();
        }
    }

}

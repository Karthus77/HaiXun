package com.example.ouchaixun.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.CountDownTimerUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText get_email;
    private EditText get_password_one;
    private EditText get_password_two;
    private EditText get_verify;
    private Button get_get_verify;
    private Button go_change;
    private TextView iv_back;
    private int tp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_changepassword);
        tp = getIntent().getIntExtra("type", 1);

        iv_back = findViewById(R.id.textView8);
        get_email = findViewById(R.id.editTextTextPassword);
        get_password_one = findViewById(R.id.editTextTextPassword2);
        get_password_two = findViewById(R.id.editTextTextPassword2);
        get_verify = findViewById(R.id.editTextNumberSigned);
        get_get_verify = findViewById(R.id.button2);
        go_change = findViewById(R.id.button4);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tp == 2) {
//                    Intent intent=new Intent(ChangePasswordActivity.this,MainActivity.class);
//                    ChangePasswordActivity.this.startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                    ChangePasswordActivity.this.startActivity(intent);
                    finish();
                }
            }
        });
        get_get_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email;
                email = get_email.getText().toString();
                if (!checkEmail(email)) {
                    Toast.makeText(ChangePasswordActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            ChangePasswordActivity.Myemail myemail = new ChangePasswordActivity.Myemail();
                            myemail.email = email;
                            myemail.type = 2;

                            MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
                            String requestBody = gson.toJson(myemail);
                            Request request = new Request.Builder()
                                    .url("http://47.102.215.61:8888/reglog/send_email")
                                    .post(RequestBody.create(mediaType, requestBody))
                                    .build();

                            OkHttpClient okHttpClient = new OkHttpClient();
                            okHttpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("1233", "onFailure: " + e.getMessage());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.d("1233", response.protocol() + " " + response.code() + " " + response.message());
                                    Headers headers = response.headers();
                                    String responseData = response.body().string();
                                    Log.d("1233", responseData);
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(responseData);
                                        int code = jsonObject1.getInt("code");
                                        if (code != 200) {
                                            ChangePasswordActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ChangePasswordActivity.this, "出错啦，请重试", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            ChangePasswordActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(get_get_verify, 60000, 1000);
                                                    mCountDownTimerUtils.start();
                                                    Toast.makeText(ChangePasswordActivity.this, "获取成功", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    for (int i = 0; i < headers.size(); i++) {
                                        Log.d("1233", headers.name(i) + ":" + headers.value(i));
                                    }
                                }
                            });
                        }
                    }).start();
                }

            }
        });
        go_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password;
                final String password2;
                final String email;
                final String string_verify;
                password = get_password_one.getText().toString();
                password2 = get_password_two.getText().toString();
                email = get_email.getText().toString();
                string_verify = get_verify.getText().toString();
                if (!password.equals(password2)) {
                    Log.d("passw", password);
                    Log.d("passw2", password2);
                    Toast.makeText(ChangePasswordActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                } else if (!checkPassword(password)) {
                    Toast.makeText(ChangePasswordActivity.this, "密码应为6~12位字母或数字！", Toast.LENGTH_SHORT).show();
                } else if (!checkEmail(email)) {
                    Toast.makeText(ChangePasswordActivity.this, "邮箱格式有误！", Toast.LENGTH_SHORT).show();
                } else if (!checkVerify(string_verify)) {
                    Toast.makeText(ChangePasswordActivity.this, "验证码有误！", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                            String requestBody = "{\r\n    \"email\": \"" + email + "\",\r\n    \"email_code\": \"" + string_verify + "\",\r\n    \"new_password\": \"" + password + "\"\r\n}";
                            Log.d("1233d", requestBody);
                            Request request = new Request.Builder()
                                    .url("http://47.102.215.61:8888/reglog/change_pwd")
                                    .post(RequestBody.create(mediaType, requestBody))
                                    .build();
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Log.d("1233d", request.toString());
                            okHttpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("1233", "onFailure: " + e.getMessage());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.d("1233", response.protocol() + " " + response.code() + " " + response.message());
                                    Headers headers = response.headers();
                                    String responseData = response.body().string();
                                    try {
                                        JSONObject jsonObject1 = new JSONObject(responseData);
                                        int code = jsonObject1.getInt("code");
                                        final String msg = jsonObject1.getString("msg");
                                        if (code != 200) {
                                            ChangePasswordActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            ChangePasswordActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ChangePasswordActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            if (tp == 2) {
                                                finish();
                                            } else {
                                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                                ChangePasswordActivity.this.startActivity(intent);
                                                finish();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    for (int i = 0; i < headers.size(); i++) {
                                        Log.d("1233", headers.name(i) + ":" + headers.value(i));
                                    }
                                    Log.d("1233", "onResponse: " + response.body());
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    public boolean checkPassword(String str) {
        String regexp = "^[0-9a-zA-Z]{6,12}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public boolean checkVerify(String str) {
        String regexp = "^[0-9]{6}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public boolean checkEmail(String str) {
        String regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public class Myemail {
        public String email;
        int type;
    }

}
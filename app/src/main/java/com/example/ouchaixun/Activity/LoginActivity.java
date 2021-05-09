
package com.example.ouchaixun.Activity;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.example.ouchaixun.R;
        import com.example.ouchaixun.Utils.MyData;

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

public class LoginActivity extends AppCompatActivity {
    private ImageView back;
    private EditText get_username;
    private EditText get_password;
    private Button gogo;
    private String username;
    private int tp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);


        gogo = findViewById(R.id.button);
        get_password = findViewById(R.id.editTextTextPassword);
        get_username = findViewById(R.id.editTextNumber);
        back = findViewById(R.id.imageView);

        findViewById(R.id.textView10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tp=1;
                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                intent.putExtra("type", tp);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });

//注册按钮
        findViewById(R.id.signin_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tp=1;
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("type", tp);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });


        gogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = get_username.getText().toString();
                final String password = get_password.getText().toString();
                if (!checkUsername(username)) {
                    Toast.makeText(LoginActivity.this, "学号不合规范！", Toast.LENGTH_SHORT).show();
                } else if (!checkPassword(password)) {
                    Toast.makeText(LoginActivity.this, "学号或密码错误！", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                            String requestBody = "\r\n{\r\n    \"account\": \"" + username + "\",\r\n    \"password\": \"" + password + "\"\r\n}";
                            Request request = new Request.Builder()
                                    .url("http://47.102.215.61:8888/reglog/login")
                                    .post(RequestBody.create(mediaType, requestBody))
                                    .build();
                            OkHttpClient okHttpClient = new OkHttpClient();
                            Log.d("1233d", request.toString() + "   " + requestBody.toString());
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
                                        if (code != 1000) {
                                            LoginActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            MyData data = new MyData(LoginActivity.this);
                                            JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                                            String token = jsonObject2.getString("token");
                                            data.save_token(token);
                                            LoginActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    MyData myData = new MyData(LoginActivity.this);
                                                    myData.save_email(username);
                                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            Log.d("1233t", data.load_token());
                                            data.save_check(true);
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
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


    public boolean checkUsername(String str) {
        String regexp = "^[0-9]{11}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public boolean checkPassword(String str) {
        String regexp = "^[0-9a-zA-Z]{6,12}$";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
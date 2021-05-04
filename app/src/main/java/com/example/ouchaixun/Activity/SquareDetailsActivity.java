package com.example.ouchaixun.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ouchaixun.R;
import com.example.ouchaixun.Utils.InputTextMsgDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Response;

public class SquareDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_square_details);






        //评论
        findViewById(R.id.comment_add).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(SquareDetailsActivity.this, R.style.dialog_center);
                inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
                    @Override
                    public void onTextSend(String msg) {
                        //点击发送按钮后，回调此方法，msg为输入的值
                        String json="{\"content\": \""+msg+"\"}";





                    }
                });
                //设置评论字数
                inputTextMsgDialog.setMaxNumber(50);
                inputTextMsgDialog .show();
            }
        });

    }
}
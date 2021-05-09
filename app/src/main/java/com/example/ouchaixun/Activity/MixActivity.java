package com.example.ouchaixun.Activity;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.RecyclerView;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.example.ouchaixun.R;
        import com.scwang.smartrefresh.layout.api.RefreshLayout;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;

public class MixActivity extends AppCompatActivity {
    private TextView head;
    private ImageView back;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private static int i = 0;
    private List<Map<String, Object>> list = new ArrayList<>();
    private int flag = 0;
    private String responseData = "";
    private String tp_url;
    private int type;
    private boolean once=false;
    private String token;
    private int len;
//    private MixAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_collection);
        head = findViewById(R.id.chat_name);
        back = findViewById(R.id.chat_back);
        recyclerView = findViewById(R.id.recyclerview);
        refreshLayout = findViewById(R.id.new_srl1);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        String name = bd.getString("name");
        head.setText(name);
        once = false;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

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

public class AboutUsActivity extends AppCompatActivity {
    public ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_collection);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
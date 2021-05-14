
package com.example.ouchaixun.Activity;

        import androidx.appcompat.app.AppCompatActivity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.Toast;
        import com.example.ouchaixun.R;

public class AboutUsActivity extends AppCompatActivity {
    public ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aboutus);
        back = findViewById(R.id.imageView17);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.aaaat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUsActivity.this, AgreementActivity.class));
            }
        });
        findViewById(R.id.www996).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUsActivity.this,"暂无更新！",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
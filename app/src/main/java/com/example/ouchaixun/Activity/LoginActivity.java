
package com.example.ouchaixun.Activity;

        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;

        import com.example.ouchaixun.R;

public class LoginActivity extends AppCompatActivity {
    private ImageView back;
    private EditText get_username;
    private EditText get_password;
    private Button gogo;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
    }
}
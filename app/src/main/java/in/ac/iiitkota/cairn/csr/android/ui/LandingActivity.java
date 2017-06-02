package in.ac.iiitkota.cairn.csr.android.ui;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.ac.iiitkota.cairn.csr.android.R;

public class LandingActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;
    private View logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        btnLogin = (Button) findViewById(R.id.landing_btn_login);
        btnRegister = (Button) findViewById(R.id.landing_btn_register);
        logo = findViewById(R.id.logo);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(0);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(1);
            }
        });

    }

    private void startNewActivity(int flag) {
        Intent intent;
        if (flag == 0)
            intent = new Intent(this, LoginActivity.class);
        else
            intent = new Intent(this, RegistrationActivity.class);

        Pair<View, String> logoPair = Pair.create(logo, "logo_transition");
        ActivityOptionsCompat cardOptions = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, logoPair);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(intent, cardOptions.toBundle());
        } else {
            startActivity(intent);
        }
    }
}

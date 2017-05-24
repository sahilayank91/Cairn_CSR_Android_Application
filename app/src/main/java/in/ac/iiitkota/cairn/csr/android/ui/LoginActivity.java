package in.ac.iiitkota.cairn.csr.android.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.model.User;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    TextView login_email, login_password;
    Button login_button, forgot_password,sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            if (UserData.getInstance(getApplicationContext()).getUserData(this)) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        login_email = (TextView) findViewById(R.id.login_email);
        login_password = (TextView) findViewById(R.id.login_password);
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Login().execute();
            }
        });
        forgot_password = (Button) findViewById(R.id.login_forgot_password_button);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);

            }
        });

        sign_up =(Button)findViewById(R.id.signup_button);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    class Login extends AsyncTask<String, String, String> {
        HashMap<String, String> params = new HashMap<>();
        boolean success = false;
        boolean connection=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (login_email.getText().toString().equals("") || login_password.getText().toString().equals("")) {
                cancel(true);
                Toast.makeText(getApplicationContext(), R.string.fill_all_field, Toast.LENGTH_LONG).show();
            }
            params.put("user_email", login_email.getText().toString());
            params.put("user_password", login_password.getText().toString());

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(connection){
                if (success) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),R.string.incorrect_credential, Toast.LENGTH_LONG).show();
                    login_password.setText("");
                }
            }
            else{
                Toast.makeText(getApplicationContext(), R.string.offline, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                result = Server.performServerCall(getResources().getString(R.string.login_url), params, "POST");

                connection=true;
                    UserData.getInstance(getApplicationContext()).initUserData(new User(new JSONObject(result)), getApplicationContext());



                success = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}

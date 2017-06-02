package in.ac.iiitkota.cairn.csr.android.ui;

import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.model.User;
import in.ac.iiitkota.cairn.csr.android.model.UserData;
import in.ac.iiitkota.cairn.csr.android.utilities.Server;


public class ChangePasswordActivity extends AppCompatActivity {

    private TextView changePasswordLabel;
    private EditText current_password,new_password,repeat_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        changePasswordLabel = (TextView) findViewById(R.id.change_password_label);
        changePasswordLabel.setSelected(true);
        current_password=(EditText)findViewById(R.id.current_password);
        new_password=(EditText)findViewById(R.id.new_password);
        repeat_password=(EditText)findViewById(R.id.repeat_password);
        Button submit=(Button)findViewById(R.id.update_password_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangePassword().execute();
            }
        });

    }

    class ChangePassword extends AsyncTask<String, String, String> {
        HashMap<String, String> params = new HashMap<>();
        boolean success = false;
        boolean connection=false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (current_password.getText().toString().equals("") || new_password.getText().toString().equals("")|| repeat_password.getText().toString().equals("")) {
                cancel(true);
                Toast.makeText(getApplicationContext(), R.string.fill_all_field, Toast.LENGTH_LONG).show();
            }
            if ( !new_password.getText().toString().equals(repeat_password.getText().toString())) {
                cancel(true);
                Toast.makeText(getApplicationContext(), R.string.password_match_fail, Toast.LENGTH_LONG).show();
            }
            params.put("user_email", UserData.getInstance(getApplicationContext()).getEmail());
            params.put("old_password", current_password.getText().toString());
            params.put("new_password", new_password.getText().toString());

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(connection){
                if (success) {
                    Toast.makeText(getApplicationContext(), R.string.password_update_success, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.incorrect_credential, Toast.LENGTH_LONG).show();
                    current_password.setText("");
                    new_password.setText("");
                    repeat_password.setText("");
                }
            }
            else{
                Toast.makeText(getApplicationContext(),R.string.offline, Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                result = Server.performServerCall(getResources().getString(R.string.change_password_url), params, "POST");

                connection=true;
                new User(new JSONObject(result));



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

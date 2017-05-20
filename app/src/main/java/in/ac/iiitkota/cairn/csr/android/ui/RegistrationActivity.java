package in.ac.iiitkota.cairn.csr.android.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class RegistrationActivity extends AppCompatActivity {

    private Button btnNext;
    private LinearLayout emailAndNextButtonHolder;
    private LinearLayout restUserDataHolder;
    private EditText reg_email, reg_name, reg_department, reg_contact, reg_password, reg_repeat_password;
    String email, name, department, contact, password, repeat_password;
    Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailAndNextButtonHolder = (LinearLayout) findViewById(R.id.reg_email_button_holder);
        restUserDataHolder = (LinearLayout) findViewById(R.id.reg_rest_data_holder);
        reg_email = (EditText) findViewById(R.id.reg_email);
        reg_name = (EditText) findViewById(R.id.reg_name);
        reg_department = (EditText) findViewById(R.id.reg_department);
        reg_contact = (EditText) findViewById(R.id.reg_contact);
        reg_password = (EditText) findViewById(R.id.reg_password);
        reg_repeat_password = (EditText) findViewById(R.id.reg_repeat_password);

        btnNext = (Button) findViewById(R.id.reg_next_button);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send otp on entered email and on success pop up dialog
                popupOTPDialog();
                email = reg_email.getText().toString();
            }
        });
        register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = reg_name.getText().toString();
                department = reg_department.getText().toString();
                contact = reg_contact.getText().toString();
                password = reg_password.getText().toString();
                repeat_password = reg_repeat_password.getText().toString();
                if (name.length() == 0 || department.length() == 0 || contact.length() == 0 || password.length() == 0 || repeat_password.length() == 0) {
                    Toast.makeText(getApplicationContext(),R.string.fill_all_field, Toast.LENGTH_LONG).show();
                } else if (!password.equals(repeat_password)) {
                    Toast.makeText(getApplicationContext(), R.string.password_match_fail, Toast.LENGTH_LONG).show();
                } else {
                    new RegisterUser().execute();
                }

            }
        });
    }

    class RegisterUser extends AsyncTask<String, String, String> {
        boolean success = false;
        HashMap<String, String> params = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params.put("user_name", name);
            params.put("user_email", email);
            params.put("user_phone", contact);
            params.put("user_password", password);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (success) {
                Toast.makeText(getApplicationContext(), R.string.reg_success, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                result = Server.performServerCall(getResources().getString(R.string.register_url), params, "POST");

                UserData.getInstance(getApplicationContext()).initUserData(new User(new JSONObject(result)), getApplicationContext());
                success = true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    private void popupOTPDialog() {
        final Dialog dialog = new Dialog(RegistrationActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_otp);
        dialog.setCancelable(false);

        final EditText etOTP = (EditText) dialog.findViewById(R.id.et_otp);
        Button btnVerify = (Button) dialog.findViewById(R.id.btn_verify);
        final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressbar);
        final LinearLayout otpContentHolder = (LinearLayout) dialog.findViewById(R.id.otp_content_holder);
        final TextView otpInstruction = (TextView) dialog.findViewById(R.id.otp_instruction);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                otpContentHolder.setVisibility(View.GONE);
                otpInstruction.setText("Verifying otp ... ");
//
                String otp = etOTP.getText().toString().trim();

                if (true /*otp.equals("retrieved from server")*/) {
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    emailAndNextButtonHolder.setVisibility(View.GONE);
                    restUserDataHolder.setVisibility(View.VISIBLE);
                } else {
                    // stay here and show error message_obj
                    //also change 'otpInstruction'
                }
            }
        });

        dialog.show();

    }
}

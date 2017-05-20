package in.ac.iiitkota.cairn.csr.android.ui;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import in.ac.iiitkota.cairn.csr.android.R;


public class ForgotPasswordActivity extends AppCompatActivity {

    private Button nextBtn;
    private LinearLayout emailAndNextButtonHolder;
    private LinearLayout resetPasswordDataHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        nextBtn = (Button) findViewById(R.id.reset_password_next_button);
        emailAndNextButtonHolder = (LinearLayout) findViewById(R.id.reset_password_email_button_holder);
        resetPasswordDataHolder = (LinearLayout) findViewById(R.id.new_repeat_password_holder);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupOTPDialog();
            }
        });

    }

    private void popupOTPDialog() {
        final Dialog dialog = new Dialog(ForgotPasswordActivity.this);
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
                otpInstruction.setText(R.string.otp_verification);
//
                String otp = etOTP.getText().toString().trim();

                if (true /*otp.equals("retrieved from server")*/) {
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    emailAndNextButtonHolder.setVisibility(View.GONE);
                    resetPasswordDataHolder.setVisibility(View.VISIBLE);
                } else {
                    // stay here and show error message_obj
                    //also change 'otpInstruction'
                }
            }
        });

        dialog.show();

    }
}

package com.sleepingpandaaa.nucleusclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText etEmail, etPassword1;
    TextView tvSignup1, tvSignIn1;
    ProgressDialog loadingBar;
    String userEmail;
    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        InitializeFields();

        tvSignup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String signUpEmail = etEmail.getText().toString();
                String signUpUserPassword = etPassword1.getText().toString();


                if (TextUtils.isEmpty(signUpEmail))
                {
                    Toast.makeText(SignUpActivity.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(signUpUserPassword)){
                    Toast.makeText(SignUpActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                }
                else {

                    userEmail = signUpEmail;

                    loadingBar.setTitle("Signing you up");
                    loadingBar.setMessage("Please wait, while we sign you up...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    //insert details into database
                    Users u = new Users();
                    u.setEmail(signUpEmail);
                    u.setPassword(signUpUserPassword);
                    boolean isInserted = helper.insertUser(u);
                    if (isInserted == true)
                    {
                        Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                    SendUserTODashBoardActivity();
                    Toast.makeText(SignUpActivity.this, "Please add User Details first", Toast.LENGTH_LONG).show();
                    etEmail.setText("");
                    etPassword1.setText("");
                    loadingBar.dismiss();

                }
            }
        });

        tvSignIn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLoginActivity();
            }
        });

    }

    private void SendUserTODashBoardActivity()
    {
        Intent intent = new Intent(SignUpActivity.this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("userEmail", userEmail);
        startActivity(intent);
        finish();
    }

    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(SignUpActivity.this, StartActivity.class);
        startActivity(loginIntent);
    }

    private void InitializeFields() {
        etEmail = findViewById(R.id.etEmail);
        etPassword1 = findViewById(R.id.etPassword1);
        tvSignIn1 = findViewById(R.id.tvSignIn1);
        tvSignup1 = findViewById(R.id.tvSignup1);
        loadingBar = new ProgressDialog(this);
    }
}
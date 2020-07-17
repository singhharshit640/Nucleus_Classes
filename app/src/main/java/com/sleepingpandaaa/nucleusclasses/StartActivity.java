package com.sleepingpandaaa.nucleusclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class StartActivity extends AppCompatActivity {

    TextInputEditText etUsername, etPassword;
    TextView tvSignIn, tvSignUp;
    String username;
    ProgressDialog loadingBar;
    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        InitializeFields();

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = etUsername.getText().toString();
                String userPassword = etPassword.getText().toString();

                if (TextUtils.isEmpty(userName))
                {
                    Toast.makeText(StartActivity.this, "Please enter your Username", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(userPassword)){
                    Toast.makeText(StartActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                }
                else {

                    username = userName;

                    loadingBar.setTitle("Signing you in");
                    loadingBar.setMessage("Please wait, while we sign you in...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    String password = helper.searchPass(userName);
                    if (userPassword.equals(password))
                    {
                        SendUserToDashboardActivity(userName);
                        etUsername.setText("");
                        etPassword.setText("");
                        loadingBar.dismiss();
                    }
                    else {
                        Toast.makeText(StartActivity.this, "Credentials don't match or Account not found", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    
                }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendUserToSignUpActivity();

            }
        });


    }

    private void SendUserToSignUpActivity()
    {

        Intent signupIntent = new Intent(StartActivity.this, SignUpActivity.class);
        startActivity(signupIntent);

    }

    private void SendUserToDashboardActivity(String userName)
    {

        Intent loginIntent = new Intent(StartActivity.this, DashBoardActivity.class);
        loginIntent.putExtra("username", username);
        loginIntent.addFlags(loginIntent.FLAG_ACTIVITY_CLEAR_TOP | loginIntent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
        loadingBar.dismiss();

    }

    private void InitializeFields() {

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvSignIn = findViewById(R.id.tvSignIn);
        loadingBar = new ProgressDialog(this);
        tvSignUp = findViewById(R.id.tvSignUp);

    }
}
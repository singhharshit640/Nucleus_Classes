package com.sleepingpandaaa.nucleusclasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private TextView tvSignIn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

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

                    loadingBar.setTitle("Signing you in");
                    loadingBar.setMessage("Please wait, while we sign you in...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    mAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            loadingBar.dismiss();
                            SendUserToDashboardActivity();
                        }
                        else {
                            String message = task.getException().toString();
                            loadingBar.dismiss();
                            Toast.makeText(StartActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                    
                }

            }
        });



    }


    private void SendUserToDashboardActivity()
    {

        Intent loginIntent = new Intent(StartActivity.this, DashBoardActivity.class);
        loginIntent.addFlags(loginIntent.FLAG_ACTIVITY_CLEAR_TOP | loginIntent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void InitializeFields() {

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvSignIn = findViewById(R.id.tvSignIn);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
    }
}
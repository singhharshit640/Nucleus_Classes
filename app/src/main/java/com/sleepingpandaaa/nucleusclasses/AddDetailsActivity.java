package com.sleepingpandaaa.nucleusclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDetailsActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmailDetail;
    private TextView tvUsername, tvSubmitDetails, tvBatch;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private String currentUserId, name, batch, username;
    private String email;
    private DatabaseReference userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        InitializeFields();

        tvSubmitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDetails();
            }
        });
    }

    private void saveDetails()
    {
        loadingBar.setTitle("Saving Data");
        loadingBar.setMessage("Please wait, while we save your details...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        name = etFullName.getText().toString();
        email = etEmailDetail.getText().toString();


        if (TextUtils.isEmpty(name)) {
            loadingBar.dismiss();
            Toast.makeText(AddDetailsActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(email))
        {
            loadingBar.dismiss();
            Toast.makeText(this, "Please enter the email id provided by the Nucleus Classes", Toast.LENGTH_LONG).show();
        }
        else {

            int startIndex1 = email.indexOf('_')+1;
            int endIndex1 = email.indexOf('@');

            int startIndex2 = email.indexOf(0)+1;
            int endIndex2 = email.indexOf('@');

            batch = email.substring(startIndex1, endIndex1);

            username = email.substring(startIndex2, endIndex2);
            tvUsername.setText(username);

            HashMap<String, Object> userData = new HashMap<>();
            userData.put("userFullName", name);
            userData.put("userEmail", email);
            userData.put("userBatch", batch);
            userData.put("userName", username);

            userInfo.child(currentUserId.toString()).updateChildren(userData);
            tvSubmitDetails.setVisibility(View.INVISIBLE);
            loadingBar.dismiss();
            Toast.makeText(this, "Data added successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddDetailsActivity.this, DashBoardActivity.class));
            finish();
        }

    }

    private void InitializeFields() {
        etFullName = findViewById(R.id.etFullName);
        tvUsername = findViewById(R.id.tvUsername);
        etEmailDetail = findViewById(R.id.etEmailDetail);
        tvSubmitDetails = findViewById(R.id.tvSubmitDetails);
        tvBatch = findViewById(R.id.tvBatch);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userInfo = FirebaseDatabase.getInstance().getReference().child("UserInfo");
        loadingBar = new ProgressDialog(this);
    }
}




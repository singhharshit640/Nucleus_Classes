package com.sleepingpandaaa.nucleusclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class AddDetailsActivity extends AppCompatActivity {

    TextInputEditText etFullName;
    Spinner spBatch;
    String usernameBatch, username, emailForProfile , username1;
    TextView tvUsername, tvSubmitDetails, etEmailDetail;
    ProgressDialog loadingBar;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

       InitializeFields();

        Bundle b = getIntent().getExtras();
        emailForProfile = b.getString("userEmail1");
        username1 = b.getString("username");

        if (TextUtils.isEmpty(username1))
        {
            etEmailDetail.setText(emailForProfile);
        }
        else {
            etEmailDetail.setText(username1);
        }


        spBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String batch = adapterView.getItemAtPosition(i).toString();

                usernameBatch = batch;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvSubmitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingBar.setTitle("Saving Data");
                loadingBar.setMessage("Please wait, while we save your details...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                String name = etFullName.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(AddDetailsActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                else {
                    int id = helper.fetchStudentId();
                    tvUsername.setText(name + id + "_" + usernameBatch);
                    username = tvUsername.getText().toString();

                    //INSERTING VALUES INTO STUDENT TABLE
                    StudentDetails sd = new StudentDetails();
                    sd.setStudent_Name(name);
                    sd.setBatch_Id(usernameBatch);
                    sd.setUsername(username);
                    sd.setEmail(emailForProfile);
                    boolean isInserted = helper.insertStudentDetails(sd);
                    if (isInserted == true) {
                        Toast.makeText(AddDetailsActivity.this, "Details added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddDetailsActivity.this, "Data not inserted", Toast.LENGTH_SHORT).show();
                    }
                    SendUserToDashBoardActivity();
                    loadingBar.dismiss();
                }



            }
        });

    }

    private void InitializeFields()
    {
        etFullName = findViewById(R.id.etFullName);
        tvUsername = findViewById(R.id.tvUsername);
        spBatch = findViewById(R.id.spBatch);
        etEmailDetail = findViewById(R.id.etEmailDetail);
        tvSubmitDetails = findViewById(R.id.tvSubmitDetails);
        loadingBar = new ProgressDialog(this);
        helper = new DatabaseHelper(this);
    }


    private void SendUserToDashBoardActivity()
    {
        Intent intent = new Intent(AddDetailsActivity.this, DashBoardActivity.class);
        intent.putExtra("email", emailForProfile);
        intent.putExtra("username1", username1);
        startActivity(intent);
        finish();
    }
}
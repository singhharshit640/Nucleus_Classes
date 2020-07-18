package com.sleepingpandaaa.nucleusclasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewUserActivity extends AppCompatActivity {

    private TextInputEditText etViewFullName, etViewEmailDetail;
    private TextView tvViewBatch, tvViewUsername;
    private FirebaseAuth mAuth;
    private DatabaseReference userInfo;
    private String currentUserId;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        InitializeFields();

        viewData();

    }

    private void viewData()
    {

        loadingBar.setTitle("Getting User Data");
        loadingBar.setMessage("Please wait, while we fetch your data...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("userBatch").exists() && snapshot.child("userEmail").exists()
                        && snapshot.child("userFullName").exists() && snapshot.child("userName").exists())
                {
                    etViewFullName.setText(snapshot.child("userFullName").getValue().toString());
                    tvViewUsername.setText(snapshot.child("userName").getValue().toString());
                    tvViewBatch.setText(snapshot.child("userBatch").getValue().toString());
                    etViewEmailDetail.setText(snapshot.child("userEmail").getValue().toString());

                    loadingBar.dismiss();
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(ViewUserActivity.this, "Error while fetching!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void InitializeFields()
    {
        etViewEmailDetail = findViewById(R.id.etViewEmailDetail);
        etViewFullName = findViewById(R.id.etViewFullName);
        tvViewBatch = findViewById(R.id.tvViewBatch);
        tvViewUsername = findViewById(R.id.tvViewUsername);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userInfo = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(currentUserId);
    }
}
package com.sleepingpandaaa.nucleusclasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    TextView tvnameProfile, tvemailProfile, tvusernameProfile, tvbatchProfile;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference userInfo;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        InitializeFields();

        showData();

    }

    private void showData()
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
                    tvnameProfile.setText(snapshot.child("userFullName").getValue().toString());
                    tvusernameProfile.setText(snapshot.child("userName").getValue().toString());
                    tvbatchProfile.setText(snapshot.child("userBatch").getValue().toString());
                    tvemailProfile.setText(snapshot.child("userEmail").getValue().toString());

                    loadingBar.dismiss();
                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(UserProfileActivity.this, "Please add your details first!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void InitializeFields()
        {
            tvnameProfile = findViewById(R.id.tvnameProfile);
            tvemailProfile = findViewById(R.id.tvemailProfile);
            tvusernameProfile = findViewById(R.id.tvusernameProfile);
            tvbatchProfile = findViewById(R.id.tvbatchProfile);
            loadingBar = new ProgressDialog(this);
            mAuth = FirebaseAuth.getInstance();
            currentUserId = mAuth.getCurrentUser().getUid();
            userInfo =FirebaseDatabase.getInstance().getReference().child("UserInfo").child(currentUserId);
        }
}
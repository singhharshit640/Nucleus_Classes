package com.sleepingpandaaa.nucleusclasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashBoardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String currentUserId;
    private boolean viewProfile = false;
    private DatabaseReference RootRef;
    ProgressDialog loadingBar;
    private TextView addDetails;
    ImageView ivAddDetails, ivLogout, ivLectures, ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        InitializeFields();


        RootRef.child("UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.hasChild(currentUserId))
                    {
                        loadingBar.dismiss();
                        Toast.makeText(DashBoardActivity.this, "Login Successfull!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        loadingBar.dismiss();
                        Toast.makeText(DashBoardActivity.this, "Please add your details first!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ivAddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!viewProfile)
                {
                    Intent intent = new Intent(DashBoardActivity.this, AddDetailsActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(DashBoardActivity.this, ViewUserActivity.class);
                    startActivity(intent);
                }
            }
        });

        ivLectures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashBoardActivity.this, LecturesActivity.class);
                startActivity(intent);
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(DashBoardActivity.this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashBoardActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });


    }

    private void InitializeFields()
    {
        ivAddDetails = findViewById(R.id.ivAddDetails);
        ivLogout = findViewById(R.id.ivLogout);
        ivLectures = findViewById(R.id.ivLectures);
        ivProfile = findViewById(R.id.ivProfile);
        addDetails = findViewById(R.id.addDetail);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUserId = mFirebaseUser.getUid();

        loadingBar.setTitle("Getting things ready");
        loadingBar.setMessage("Please wait, while we are getting things ready...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        RootRef.child("UserInfo").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {
                    if (snapshot.hasChild("userFullName"))
                    {
                        addDetails.setText("View Details");
                        viewProfile = true;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
package com.sleepingpandaaa.nucleusclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DashBoardActivity extends AppCompatActivity {

    ImageView ivAddDetails, ivLogout, ivLectures, ivProfile;
    String email, userEmail, username, userName1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ivAddDetails = findViewById(R.id.ivAddDetails);
        ivLogout = findViewById(R.id.ivLogout);
        ivLectures = findViewById(R.id.ivLectures);
        ivProfile = findViewById(R.id.ivProfile);

        //This is required for the profile Activity
        //if ()

        Bundle b = getIntent().getExtras();
        userEmail = b.getString("userEmail");
        username = b.getString("username");

        ivAddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DashBoardActivity.this, AddDetailsActivity.class);
                intent.putExtra("userEmail1", userEmail);
                intent.putExtra("username", username);
                startActivity(intent);
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
                Intent intent = new Intent(DashBoardActivity.this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b1 = getIntent().getExtras();
                email = b1.getString("email");
                //userName1 = b1.getString("username1");


                Intent intent = new Intent(DashBoardActivity.this, UserProfileActivity.class);
                intent.putExtra("emailToShow", email);
                intent.putExtra("username1", username);
                startActivity(intent);
            }
        });


    }
}
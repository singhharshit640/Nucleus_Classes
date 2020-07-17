package com.sleepingpandaaa.nucleusclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfileActivity extends AppCompatActivity {

    TextView tvnameProfile, tvemailProfile, tvusernameProfile, tvbatchProfile;
    String email, username1;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        InitializeFields();

        Bundle b = getIntent().getExtras();
        email = b.getString("emailToShow");
        username1 = b.getString("username1");


        Cursor cur = helper.getAllData(email);

        if (cur.getCount() == 0)
        {
            Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
        }

        StringBuffer buffer = new StringBuffer();

        while (cur.moveToNext())
        {
            tvnameProfile.setText(cur.getString(1));
            tvbatchProfile.setText(cur.getString(2));
            tvusernameProfile.setText(cur.getString(3));
            tvemailProfile.setText(cur.getString(0));

            if (TextUtils.isEmpty(username1))
            {
                tvemailProfile.setText(email);
            }
            else {
                tvemailProfile.setText(username1);
            }

        }

    }

    private void InitializeFields()
    {
        tvnameProfile = findViewById(R.id.tvnameProfile);
        tvemailProfile = findViewById(R.id.tvemailProfile);
        tvusernameProfile = findViewById(R.id.tvusernameProfile);
        tvbatchProfile = findViewById(R.id.tvbatchProfile);
        helper = new DatabaseHelper(this);
    }
}
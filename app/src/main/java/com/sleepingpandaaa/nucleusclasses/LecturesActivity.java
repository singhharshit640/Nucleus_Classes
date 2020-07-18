package com.sleepingpandaaa.nucleusclasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.HashMap;

public class LecturesActivity extends YouTubeBaseActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userBatchRef, RootRefB1, RootRefB2, UserInfoRef;
    private String currentUserId, Batch ="", Batch1, FinalBatch;
    private String b1="B1", b2="B2", b1Link = "", b2Link= "", id, videoId;
    private ProgressDialog loadingBar;
    private TextView tvPlay;
    private com.google.android.youtube.player.YouTubePlayerView youTubePlayerView;
    private com.google.android.youtube.player.YouTubePlayer.OnInitializedListener onInitializedListener;

    public void getVideoId(String id)
    {
        videoId = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures);

        InitializeFields();


        onInitializedListener = new com.google.android.youtube.player.YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(com.google.android.youtube.player.YouTubePlayer.Provider provider, com.google.android.youtube.player.YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoId);
                loadingBar.dismiss();
            }

            @Override
            public void onInitializationFailure(com.google.android.youtube.player.YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        tvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayerView.initialize(PlayerConfig.API_KEY, onInitializedListener);
            }
        });


        loadingBar.setTitle("Getting your lecture");
        loadingBar.setMessage("Please wait, while we get your lecture...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        b1Link = "KVEetswpWpk";
        b2Link = "ZihywtixUYo";

        InsertVideoId();

        UserInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("userBatch").exists() && snapshot.child("userEmail").exists()
                        && snapshot.child("userFullName").exists() && snapshot.child("userName").exists())
                {
                    Batch = snapshot.child("userBatch").getValue().toString();
                    sendBatchToPlayVideo(Batch);
                    Log.d("Batch: ", "onDataChange: " + Batch1);
                    loadingBar.dismiss();
                }
                else {
                    Toast.makeText(LecturesActivity.this, "Please fill your details first!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendBatchToPlayVideo(String batch) {
        Batch1 = batch;
        Log.d("Batch1", "sendBatchToPlayVideo: " + Batch1);
        Toast.makeText(this, "Batch: " + Batch1, Toast.LENGTH_SHORT).show();
        playVideo(Batch1);
    }

    private void playVideo(String batch1)
    {

        FinalBatch = batch1;

        if (FinalBatch.equals(b1))
        {
            RootRefB1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        id = snapshot.child("B1_Link").getValue().toString();
                        getVideoId(id);
                    }
                    else {
                        Toast.makeText(LecturesActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        else if(FinalBatch.equals(b2))
        {
            RootRefB2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists())
                    {
                        id = snapshot.child("B2_Link").getValue().toString();
                        getVideoId(id);
                    }
                    else {
                        Toast.makeText(LecturesActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else {
            Toast.makeText(this, "Please fill your details first!", Toast.LENGTH_LONG).show();
            //startActivity(new Intent(LecturesActivity.this, DashBoardActivity.class));
        }


    }

    private void InsertVideoId()
    {
        HashMap<String, Object> userBatch = new HashMap<>();
        userBatch.put("B1", b1);
        userBatch.put("B2", b2);

        userBatchRef.updateChildren(userBatch);

        HashMap<String, Object> refB1 = new HashMap<>();
        refB1.put("B1_Link", b1Link);

        RootRefB1.updateChildren(refB1);

        HashMap<String, Object> refB2 = new HashMap<>();
        refB2.put("B2_Link", b2Link);

        RootRefB2.updateChildren(refB2);
    }


    private void InitializeFields()
    {
        youTubePlayerView = findViewById(R.id.youtube_player);

        tvPlay = findViewById(R.id.tvPlay);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userBatchRef = FirebaseDatabase.getInstance().getReference().child("UserBatch");
        RootRefB1 = FirebaseDatabase.getInstance().getReference().child("UserBatch").child("B1");
        RootRefB2 = FirebaseDatabase.getInstance().getReference().child("UserBatch").child("B2");
        UserInfoRef = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(currentUserId);

    }
}
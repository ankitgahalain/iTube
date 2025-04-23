package com.example.itube;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    EditText etYoutubeUrl;
    Button btnPlay, btnAddToPlaylist, btnMyPlaylist;
    DatabaseHelper dbHelper;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etYoutubeUrl = findViewById(R.id.etYoutubeUrl);
        btnPlay = findViewById(R.id.btnPlay);
        btnAddToPlaylist = findViewById(R.id.btnAddToPlaylist);
        btnMyPlaylist = findViewById(R.id.btnMyPlaylist);

        dbHelper = new DatabaseHelper(this);

        // Get username from login screen
        username = getIntent().getStringExtra("username");

        btnPlay.setOnClickListener(v -> {
            String url = etYoutubeUrl.getText().toString().trim();
            if (!url.isEmpty()) {
                Intent intent = new Intent(HomeActivity.this, VideoPlayerActivity.class);
                intent.putExtra("videoUrl", url);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Enter a YouTube URL", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddToPlaylist.setOnClickListener(v -> {
            String url = etYoutubeUrl.getText().toString().trim();
            if (!url.isEmpty()) {
                dbHelper.addToPlaylist(username, url);
                Toast.makeText(this, "Added to playlist", Toast.LENGTH_SHORT).show();
                etYoutubeUrl.setText("");
            } else {
                Toast.makeText(this, "Enter a URL first", Toast.LENGTH_SHORT).show();
            }
        });

        btnMyPlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PlaylistActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}

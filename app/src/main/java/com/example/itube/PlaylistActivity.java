package com.example.itube;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    ListView lvPlaylist;
    DatabaseHelper dbHelper;
    String username;
    ArrayList<String> playlistLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvPlaylist = findViewById(R.id.lvPlaylist);
        dbHelper = new DatabaseHelper(this);

        username = getIntent().getStringExtra("username");
        playlistLinks = new ArrayList<>();

        loadPlaylist();

        lvPlaylist.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            String url = playlistLinks.get(position);
            Intent intent = new Intent(PlaylistActivity.this, VideoPlayerActivity.class);
            intent.putExtra("videoUrl", url);
            startActivity(intent);
        });
    }

    private void loadPlaylist() {
        Cursor cursor = dbHelper.getPlaylist(username);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String url = cursor.getString(cursor.getColumnIndexOrThrow("youtube_url"));
                playlistLinks.add(url);
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, playlistLinks);
        lvPlaylist.setAdapter(adapter);
    }
}


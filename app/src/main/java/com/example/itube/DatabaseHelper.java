package com.example.itube;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "iTubeApp.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_FULLNAME = "full_name";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    // Playlist table
    private static final String TABLE_PLAYLIST = "playlist";
    private static final String COL_PLAYLIST_ID = "id";
    private static final String COL_PLAYLIST_USERNAME = "username";
    private static final String COL_URL = "youtube_url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FULLNAME + " TEXT, " +
                COL_USERNAME + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Playlist Table
        String createPlaylistTable = "CREATE TABLE " + TABLE_PLAYLIST + " (" +
                COL_PLAYLIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PLAYLIST_USERNAME + " TEXT, " +
                COL_URL + " TEXT)";
        db.execSQL(createPlaylistTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        onCreate(db);
    }

    // Register new user
    public boolean registerUser(String fullName, String username, String password) {
        if (checkUsernameExists(username)) return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FULLNAME, fullName);
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Check username and password
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Check if username already exists
    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COL_USERNAME + "=?",
                new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Add video to playlist
    public void addToPlaylist(String username, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PLAYLIST_USERNAME, username);
        values.put(COL_URL, url);
        db.insert(TABLE_PLAYLIST, null, values);
    }

    // Get user's playlist
    public Cursor getPlaylist(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PLAYLIST +
                        " WHERE " + COL_PLAYLIST_USERNAME + "=?",
                new String[]{username});
    }
}

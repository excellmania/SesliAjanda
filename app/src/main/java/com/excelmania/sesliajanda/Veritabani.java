package com.excelmania.sesliajanda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Veritabani extends SQLiteOpenHelper {
    private static final int SURUM = 1;
    private static final String VERITABANI = "not.db";

    public Veritabani(Context con) {
        super(con, VERITABANI, null, SURUM);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notlar (id INTEGER PRIMARY KEY AUTOINCREMENT,detay TEXT,tarih datetime default current_timestamp ) ");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notlar");
        onCreate(db);
    }
}
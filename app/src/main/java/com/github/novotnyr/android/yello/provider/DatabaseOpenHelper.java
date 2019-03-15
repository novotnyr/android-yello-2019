package com.github.novotnyr.android.yello.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.novotnyr.android.yello.Defaults;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public DatabaseOpenHelper(Context context) {
        super(context, "note", Defaults.DEFAULT_CURSOR_FACTORY, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Provider.Note.SQL);

        ContentValues row = new ContentValues();
        row.put(Provider.Note.DESCRIPTION, "Káva");

        db.insert(Provider.Note.TABLE_NAME, Defaults.NO_NULL_COLUMN_HACK, row);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

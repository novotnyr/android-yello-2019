package com.github.novotnyr.android.yello.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.github.novotnyr.android.yello.Defaults;

public class NoteContentProvider extends ContentProvider {
    public static final String AUTHORITY
            = "com.github.novotnyr.android.yello.provider.NoteContentProvider";

    public static final Uri CONTENT_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(Provider.Note.TABLE_NAME)
            .build();


    private DatabaseOpenHelper dbHelper;

    @Override
    public boolean onCreate() {
        this.dbHelper = new DatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Provider.Note.TABLE_NAME,
                Defaults.ALL_COLUMNS,
                Defaults.NO_SELECTION,
                Defaults.NO_SELECTION_ARGS,
                Defaults.NO_GROUP_BY,
                Defaults.NO_HAVING,
                Defaults.NO_SORT_ORDER);
        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(Provider.Note.TABLE_NAME, Defaults.NO_NULL_COLUMN_HACK, values);

        ContentResolver contentResolver = getContext().getContentResolver();
        contentResolver.notifyChange(CONTENT_URI, Defaults.NO_CONTENT_OBSERVER);

        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
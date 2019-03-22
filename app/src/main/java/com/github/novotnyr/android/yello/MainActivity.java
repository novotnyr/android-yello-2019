package com.github.novotnyr.android.yello;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.github.novotnyr.android.yello.provider.NoteContentProvider;
import com.github.novotnyr.android.yello.provider.Provider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private GridView notesGridView;

    private FloatingActionButton addNoteFab;

    private SimpleCursorAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesGridView = findViewById(R.id.notesGridView);
        addNoteFab = findViewById(R.id.addNoteFab);

        addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("New")
                        .setView(editText)
                        .setPositiveButton("Uložiť", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues row = new ContentValues();
                                row.put(Provider.Note.DESCRIPTION, editText.getText().toString());

                                AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
                                    @Override
                                    protected void onInsertComplete(int token, Object cookie, Uri uri) {
                                        Toast.makeText(MainActivity.this, "Pridané!", Toast.LENGTH_SHORT).show();
                                    }
                                };
                                handler.startInsert(0, Defaults.NO_COOKIE, NoteContentProvider.CONTENT_URI, row);
                            }
                        })
                        .show();
            }
        });

        String[] from = { Provider.Note.DESCRIPTION };
        int[] to = { android.R.id.text1 };

        gridViewAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, Defaults.NO_CURSOR , from, to, Defaults.NO_FLAGS);
        notesGridView.setAdapter(gridViewAdapter);

        getSupportLoaderManager().initLoader(0, Bundle.EMPTY, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(NoteContentProvider.CONTENT_URI);

        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        gridViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        gridViewAdapter.swapCursor(Defaults.NO_CURSOR);
    }
}

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
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.github.novotnyr.android.yello.provider.NoteContentProvider;
import com.github.novotnyr.android.yello.provider.Provider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.MultiChoiceModeListener, AdapterView.OnItemClickListener {
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
        int[] to = { R.id.noteItemEditText };


        gridViewAdapter = new SimpleCursorAdapter(this,
                R.layout.note_item, Defaults.NO_CURSOR , from, to, Defaults.NO_FLAGS);
        notesGridView.setAdapter(gridViewAdapter);

        notesGridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        notesGridView.setMultiChoiceModeListener(this);

        notesGridView.setOnItemClickListener(this);

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

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.note_cab, menu);
        mode.setTitle("Výber položiek");

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if(item.getItemId() == R.id.deleteNoteMenuItem) {
            // TODO teraz mozeme zmazat
            for (long id : notesGridView.getCheckedItemIds()) {
                AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()){};
                handler.startDelete(0, null, NoteContentProvider.CONTENT_URI, "_ID = " + id, null);
            }


            return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        int count = notesGridView.getCheckedItemCount();
        mode.setSubtitle("Selected items: " + count);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
        final EditText editText = new EditText(this);
        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        editText.setText(cursor.getString(cursor.getColumnIndex(Provider.Note.DESCRIPTION)));


        new AlertDialog.Builder(this)
                .setTitle("Edit")
                .setView(editText)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues row = new ContentValues();
                        row.put(Provider.Note.DESCRIPTION, editText.getText().toString());

                        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {};
                        handler.startUpdate(0, null, NoteContentProvider.CONTENT_URI, row, "_ID = " + id, null);
                    }
                })
                .show();

    }
}

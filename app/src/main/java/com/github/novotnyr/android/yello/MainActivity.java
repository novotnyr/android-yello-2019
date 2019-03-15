package com.github.novotnyr.android.yello;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    private GridView notesGridView;

    private FloatingActionButton addNoteFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesGridView = findViewById(R.id.notesGridView);
        addNoteFab = findViewById(R.id.addNoteFab);
    }
}

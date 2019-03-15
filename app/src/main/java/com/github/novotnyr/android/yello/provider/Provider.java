package com.github.novotnyr.android.yello.provider;

import android.provider.BaseColumns;

public interface Provider {
    interface Note extends BaseColumns {
        String TABLE_NAME = "note";

        String DESCRIPTION = "description";

        // -----
        String SQL = String.format("CREATE TABLE note(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)",
                BaseColumns._ID, DESCRIPTION);
    }
}
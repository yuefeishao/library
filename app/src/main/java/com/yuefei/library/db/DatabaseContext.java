package com.yuefei.library.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by Agent on 2018/4/17.
 */

public class DatabaseContext extends ContextWrapper {
    private String mDirPath;

    public DatabaseContext(Context context, String dirPath) {
        super(context);
        this.mDirPath = dirPath;
    }

    public File getDatabasePath(String name) {
        File result = new File(this.mDirPath + File.separator + name);
        if(!result.getParentFile().exists()) {
            result.getParentFile().mkdirs();
        }

        return result;
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath(name), factory);
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(this.getDatabasePath(name).getAbsolutePath(), factory, errorHandler);
    }
}
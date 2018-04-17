package com.yuefei.library.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;


/**
 * Created by Agent on 2017/7/4.
 */

public class DbOpenHelper extends DaoMaster.OpenHelper {
    private Context context;
    public DbOpenHelper(Context context, String name) {
        super(context, name);
        this.context=context;
    }

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
        this.context=context;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

        // 数据库升级更新处理

//        if(oldVersion==2||oldVersion==3){
//            String UPDATE_TABLE_NAME="VIDEO_BEAN";
//            String UPDATE_TABLE_FILED="VIDEO_ID";
//            DaoMaster.createAllTables(db,true);
//            db.execSQL( "ALTER TABLE " +UPDATE_TABLE_NAME+" ADD "+UPDATE_TABLE_FILED+ " TEXT ");
//        }else if(oldVersion==1){
//            dropAllTables(db, true);
//            onCreate(db);
//        } else if (oldVersion == 4) {
//            DaoMaster.createAllTables(db, true);
//        }

    }
}

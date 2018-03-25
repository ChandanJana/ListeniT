package com.example.sayantani.listenit.libs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sayantani on 03-05-2017.
 */
public class MyDBMS {
    private static final String DATABASE_NAME = "listenit";
    private static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS USERINFO");

            db.execSQL("CREATE TABLE IF NOT EXISTS USERINFO(USERID VARCHAR(20),"
                    + "PASS VARCHAR(20), NAME VARCHAR(20));");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int arg2) {
            // TODO Auto-generated method stub
            //db.execSQL("DROP TABLE IF EXISTS USERINFO");
            onCreate(db);
        }

    }

    public MyDBMS(Context c) {
        ourContext = c;
    }

    public MyDBMS open() {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public void executeNonQuery(String query) {
        // TODO Auto-generated method stub
        ourDatabase.execSQL(query);
    }

    public Cursor executeQuery(String query) {
        Cursor c = ourDatabase.rawQuery(query, null);
        return c;
    }
}
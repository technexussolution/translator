package com.translator.quick.easy.LT_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LT_HelperDB extends SQLiteOpenHelper {
    SQLiteDatabase myDb;

    public LT_HelperDB(Context context) {
        super(context, "LangTranslation.db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        this.myDb = sQLiteDatabase;
        sQLiteDatabase.execSQL("CREATE  TABLE IF NOT EXISTS Translation_Data(id integer PRIMARY KEY AUTOINCREMENT,source_lan text, source_txt text, target_lan text, target_txt text);");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL("DROP TABLE IF EXISTS Trans_Data");
        onCreate(sQLiteDatabase);
    }

    public boolean InsertRecord(String str, String str2, String str3, String str4, String str5) {
        this.myDb = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("source_lan", str2);
        contentValues.put("source_txt", str3);
        contentValues.put("target_lan", str4);
        contentValues.put("target_txt", str5);
        return this.myDb.insert(str, null, contentValues) != -1;
    }

    public Cursor getTransData() {
        return getReadableDatabase().rawQuery("select * from Translation_Data", null);
    }

    public Integer delTrans(String str) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        this.myDb = readableDatabase;
        return Integer.valueOf(readableDatabase.delete("Translation_Data", "id= ?", new String[]{str}));
    }

    public void delAll() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        this.myDb = readableDatabase;
        readableDatabase.delete("Translation_Data", null, null);
    }
}

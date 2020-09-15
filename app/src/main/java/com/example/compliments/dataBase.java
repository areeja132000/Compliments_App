package com.example.compliments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class dataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "complimentsApp.db";

    public dataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE ACCOUNTS "
                + "(USERNAME TEXT NOT NULL,"
                + "PASSWORD TEXT NOT NULL)";
        db.execSQL(sql);

        sql = "CREATE TABLE COMPLIMENTS "
                + "(USERNAME TEXT NOT NULL,"
                + "COMPLIMENT TEXT NOT NULL,"
                + "FROM_USER TEXT NOT NULL)";
        db.execSQL(sql);

        sql = "CREATE TABLE BLOCKED_USERS "
                + "(USERNAME TEXT NOT NULL,"
                + "BLOCKED TEXT NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertAccount(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERNAME", username);
        contentValues.put("PASSWORD", password);
        long success = db.insert("ACCOUNTS", null, contentValues);
        db.close();
        return success;
    }

    public long insertCompliment(String username, String compliment, String from) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERNAME", username);
        contentValues.put("COMPLIMENT", compliment);
        contentValues.put("FROM_USER", username);
        long success = db.insert("COMPLIMENTS", null, contentValues);
        db.close();
        return success;
    }

    public long insertBlock(String username, String user_blocked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("USERNAME", username);
        contentValues.put("BLOCKED", user_blocked);
        long success = db.insert("BLOCKED_USERS", null, contentValues);
        db.close();
        return success;
    }

    public Cursor getCompliments(String username) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM COMPLIMENTS WHERE USERNAME IS ?;",
                new String[]{username});
        return cursor;
    }

    public Cursor getBlocked(String username) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT BLOCKED FROM BLOCKED_USERS WHERE USERNAME IS ?;",
                new String[]{username});
        return cursor;
    }

    public ArrayList getNonBlockedCompliments(String username) {
        Cursor unfilteredCompliments = getCompliments(username);
        Cursor blockedPeople = getBlocked(username);

        ArrayList<String> allBlocked = new ArrayList<String>();
        blockedPeople.moveToFirst();
        while (!blockedPeople.isAfterLast()) {
            allBlocked.add(blockedPeople.getString(0));
            blockedPeople.moveToNext();
        }
        blockedPeople.close();

        ArrayList<String> filteredCompliments = new ArrayList<String>();
        unfilteredCompliments.moveToFirst();
        while (!unfilteredCompliments.isAfterLast()) {
            if (!allBlocked.contains(unfilteredCompliments.getString(2))) {
                filteredCompliments.add(unfilteredCompliments.getString(1));
                unfilteredCompliments.moveToNext();
            }
        }
        unfilteredCompliments.close();
        return filteredCompliments;
    }
    public boolean check_login(String username, String password) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM ACCOUNTS WHERE USERNAME IS ? AND PASSWORD IS ?;",
                new String[]{username, password});
        if ((cursor != null) && (cursor.getCount() > 0)) {
            return true;
        } else {
            return false;
        }
    }

}

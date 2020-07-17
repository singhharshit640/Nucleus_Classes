package com.sleepingpandaaa.nucleusclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "user.db";
    private static final String TABLE_NAME1 = "user";
    private static final String TABLE_NAME2 = "student";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_NAME = "Student_Name";
    private static final String COLUMN_BATCH_ID = "Batch_Id";
    private static final String COLUMN_USERNAME = "Username";
    private static final String COLUMN_STUDENT_EMAIL = "email";
    SQLiteDatabase sqLiteDatabase;


    private static final String TABLE_CREATE = "create table user (id integer primary key not null , email text not null , pass text not null);";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
        sqLiteDatabase.execSQL("create table student (id integer primary key not null , Student_Name text not null , Batch_Id text not null, Username text, email text);");
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public Cursor getAllData(String email)
    {
        sqLiteDatabase = this.getWritableDatabase();
        Cursor cur = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME2 + " where email=?",new String[]{email}, null);
        return cur;
    }

    public boolean insertUser(Users u)
    {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from user";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_EMAIL, u.getEmail());
        values.put(COLUMN_PASS, u.getPassword());
        long result = sqLiteDatabase.insert(TABLE_NAME1, null, values);
        sqLiteDatabase.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertStudentDetails(StudentDetails sd)
    {
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String query = "select * from student";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        int count = cursor.getCount();

        values.put(COLUMN_ID, count);
        values.put(COLUMN_NAME, sd.getStudent_Name());
        values.put(COLUMN_BATCH_ID, sd.getBatch_Id());
        values.put(COLUMN_USERNAME, sd.getUsername());
        values.put(COLUMN_STUDENT_EMAIL, sd.getEmail());

        long result = sqLiteDatabase.insert(TABLE_NAME2, null, values);
        sqLiteDatabase.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    public int fetchStudentId()
    {
        sqLiteDatabase = this.getReadableDatabase();
        String query = "select id from " + TABLE_NAME2;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        int res = cursor.getColumnIndex("id");
        return res;
    }

    public String searchPass(String email)
    {
        sqLiteDatabase = this.getReadableDatabase();
        String query = "select email, pass from " + TABLE_NAME1;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        String u, p;
        p = "Not found";
        if (cursor.moveToFirst())
        {
            do {
                u = cursor.getString(0);

                if (u.equals(email))
                {
                    p = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        return p;

    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        String query = "DROP TABLE IF EXISTS " + TABLE_NAME1;
//        sqLiteDatabase.execSQL(query);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
//        this.onCreate(sqLiteDatabase);
    }
}

package com.example.studentcareerapp.ArraylistHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import java.util.ArrayList;

public class FacultyHelper extends SQLiteOpenHelper {

    final static String DBNAME = "FacultyNameList.db";
    final static int DBVERSION = 1;


    // Create constructor
    public FacultyHelper(@Nullable Context context)
    {
        super(context, DBNAME, null, DBVERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table tblFacultyNameList" +
                        "(id integer primary key autoincrement," +
                        "name text)"
        );
    }


    //If version of database is update old table should drop and new table will create

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop table if exists
        db.execSQL("Drop table if exists tblFacultyNameList");

        // Create new table
        onCreate(db);

    }

    public boolean fnInsertItems(String name) {

        SQLiteDatabase database = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);

        long id = database.insert("tblFacultyNameList",null,values);


        // id<=0 means no row is inserted
        if(id<=0){
            return false;
        }
        else {
            // Else return true means item inserted successfully
            return true;
        }
    }

    public ArrayList<String> fnGetItems(){

        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select name from tblFacultyNameList",null);


        if (cursor.moveToFirst())
        {
            do{
                list.add(cursor.getString(0));

            }while (cursor.moveToNext());
        }

        cursor.close();

        // close database
        db.close();

        return list;
    }

    public boolean fnCheckItemsExist(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from tblFacultyNameList where name = ?",new String[]{name});


        // If count>0 means we found the food item
        if(cursor.getCount()>0)
        {
            return true;
        }
        else
        {
            // Else item is not present in table
            return false;
        }
    }

    public void fnRemoveItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Query of deleting from table
        db.delete("tblFacultyNameList",  "name = ?" , new String[] {name} );

        // Close database
        db.close();
    }

    public void fnDeleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Query of deleting all item from table
        db.delete("tblFacultyNameList", null, null);

        // Close database
        db.close();
    }

}

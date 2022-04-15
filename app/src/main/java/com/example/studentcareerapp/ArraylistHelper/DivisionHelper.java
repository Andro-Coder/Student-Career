package com.example.studentcareerapp.ArraylistHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.studentcareerapp.Domains.DivisionDomain;

import java.util.ArrayList;

public class DivisionHelper extends SQLiteOpenHelper {

    final static String DBNAME = "DivisionList.db";
    final static int DBVERSION = 1;


    // Create constructor
    public DivisionHelper(@Nullable Context context)
    {
        super(context, DBNAME, null, DBVERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table tblDivList" +
                        "(id integer primary key autoincrement," +
                        "item text)"
        );
    }


    //If version of database is update old table should drop and new table will create

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop table if exists
        db.execSQL("Drop table if exists tblDivList");

        // Create new table
        onCreate(db);

    }

    public boolean fnInsertItems(String item) {

        SQLiteDatabase database = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("item",item);

        long id = database.insert("tblDivList",null,values);


        // id<=0 means no row is inserted
        if(id<=0){
            return false;
        }
        else {
            // Else return true means item inserted successfully
            return true;
        }
    }

    public ArrayList<DivisionDomain> fnGetItems(){

        ArrayList<DivisionDomain> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("Select item from tblDivList",null);


        // If cursor is pointing the first row deriving from rawQuery
        // Then store data into division domain class method

        if (cursor.moveToFirst())
        {
            do{
                //store data into division domain class method
                DivisionDomain domain = new DivisionDomain();
                domain.setItem(cursor.getString(0));

                list.add(domain);

            }while (cursor.moveToNext());
        }


        // close cursor
        cursor.close();

        // close database
        db.close();

        return list;
    }

    public boolean fnCheckItemsExist(String item){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select * from tblDivList where item = ?",new String[]{item});


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

    public void fnRemoveItem(String item) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Query of deleting from table
        db.delete("tblDivList",  "item = ?" , new String[] {item} );

        // Close database
        db.close();
    }

    public void fnDeleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Query of deleting all item from table
        db.delete("tblDivList", null, null);

        // Close database
        db.close();
    }
}

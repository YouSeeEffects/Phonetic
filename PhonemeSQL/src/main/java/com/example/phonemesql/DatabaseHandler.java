package com.example.phonemesql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @author James Choi
 * @see android.database.sqlite.SQLiteOpenHelper
 * Helps with opening sqlitedatabase file in the assets directory of the source code.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // fileLocation = DB_FULL_PATH+DATABASE_NAME
    private static final String DATABASE_NAME = "cmudict.0.7a.sqlite"; // Database Name
    private static final String DB_FULL_PATH="/data/data/com.example.phonemesql/databases/"; // This is path of cmudict.0.7a.sqlite

    private static final String TABLE_DICTS = "cmudict"; // Table name
    private static final String KEY_ID = "_id"; // Table Columns name
    private static final String KEY_CODE = "code"; // Table Columns name

    /**
     * Constructor
     * @param context
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * If you extend SQLiteOpenHelpwer, you have to have onCreate and onDestroy defined because
     * those two methods are probably abstract.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            if (!checkDataBase()){
                Log.e("!!!!!!Data base is not found","DATABase is not found1");

                //DB_FULL_PATH = db.getPath();
            }else{
                Log.d("!!!!!!Database is found","DAta base is found nigga");
                //DB_FULL_PATH = db.getPath();
            }
        }catch(SQLiteException e){
            Log.d("!!!!!!Database is SQLITEException is thrown","SQLiteException is thrown");
            throw e;
        }
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    /**
     *
     * @param word The word to search for in the sqlite database
     * @return The string arpabet/sampa representation of what was found for the requested query
     */
    public Word getWord(String word) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_FULL_PATH+DATABASE_NAME,null ,SQLiteDatabase.OPEN_READONLY );
        String sqlQuery = "SELECT "+KEY_CODE+" FROM "+TABLE_DICTS+" WHERE "+KEY_ID+"=\""+word+"\"";
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            return new Word(word, cursor.getString(0));
        }
        else{
            return new Word("N/A","N/A") ;
        }
    }


    /**
     * checkDatabase
     * @return Returns true if database is located, false if not found for some reason
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH+DATABASE_NAME, null,SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            Log.e("SQLiteException","Threw a SQLiteException");
        }
        return checkDB != null ;
    }

}

package com.kizzlebot.Phonetic;



import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeMap;


/**
 * @author James Choi
 * @see android.database.sqlite.SQLiteOpenHelper
 * Helps with opening sqlitedatabase file in the assets directory of the source code.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // fileLocation = DB_FULL_PATH+DATABASE_NAME
    private static final String DATABASE_NAME = "cmudict.0.7a.sqlite"; // Database Name
    private static final String DB_FULL_PATH="/data/data/com.kizzlebot.Phonetic/databases/"; // This is path of cmudict.0.7a.sqlite

    private static final String TABLE_DICTS = "cmudict"; // Table name
    private static final String KEY_ID = "_id"; // Table Columns name
    private static final String KEY_CODE = "code"; // Table Columns name
    public static final TreeMap<String,Integer> tmap = new TreeMap<String, Integer>( ); // static - Vars have same value for all instances and accessible via DatabaseHandler.tmap


    /**
     * Constructor
     * @param context
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        copyDataBase( context );
        setTreeMapWithData();
    }

    /**
     * If you extend SQLiteOpenHelper, you have to have onCreate and onDestroy defined because
     * those two methods are abstract.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d( "Databasehandler", "onCreate method has been called setTreeMapWithData called" );
    }
    /**
     * Implemented method from SQLiteOpenHelper
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onCreate(db);
        Log.d("Databasehandler","onUpgrade method has been called");
    }



    /**
     * Copies the contents of Asset cmudict.0.7a.sqlite to /data/data/databases
     * @param myContext Context of caller
     */
    private void copyDataBase(Context myContext){
        String DB_FULL_PATH="/data/data/com.kizzlebot.Phonetic/databases/";
        String DATABASE_NAME = "cmudict.0.7a.sqlite";
        AssetManager assetManage = myContext.getAssets();
        File file = new File(DB_FULL_PATH); // Declare that there is a folder called databases
        file.mkdir();                       // Make that database on install
        Log.d("MKDIR","JUST MKDir");
        try{
            //Open your local db as the input stream
            InputStream myInput = assetManage.open( DATABASE_NAME );

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(DB_FULL_PATH+DATABASE_NAME);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[2048];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch(IOException e){
            Log.d("Something wrong","Database IOException");
        }
    }

    /**
     * Returns a treemap containing
     */
    private void setTreeMapWithData(){

        tmap.put("AA", R.raw.aa);
        tmap.put("AA1", R.raw.aa1);
        tmap.put("AE", R.raw.ae);
        tmap.put("AH0", R.raw.ah0);
        tmap.put("AH1", R.raw.ah1);
        tmap.put("AE", R.raw.ae);
        tmap.put("AO1",R.raw.ao1);
        tmap.put("AW",R.raw.aw);
        tmap.put("AXR",R.raw.axr);
        tmap.put("AY",R.raw.ay);
        tmap.put("B", R.raw.b);
        tmap.put("CH", R.raw.ch);
        tmap.put("D", R.raw.d);
        tmap.put("DH", R.raw.dh);
        tmap.put("EH", R.raw.eh);
        tmap.put("EH1",R.raw.eh1);
        tmap.put("ER",R.raw.er);
        tmap.put("ER1",R.raw.er_);
        tmap.put("F",R.raw.f);
        tmap.put("G", R.raw.g);
        tmap.put("HH", R.raw.hh);
        tmap.put("IH", R.raw.ih);
        tmap.put("IY", R.raw.iy);
        tmap.put("JH", R.raw.jh);
        tmap.put("K", R.raw.k);
        tmap.put("L", R.raw.l);
        tmap.put("M", R.raw.m);
        tmap.put("N", R.raw.n);
        tmap.put("NG", R.raw.ng);
        tmap.put("OW1", R.raw.ow1);
        tmap.put("OY", R.raw.oy);
        tmap.put("P", R.raw.p);
        tmap.put("R", R.raw.r);
        tmap.put("S", R.raw.s);
        tmap.put("SH", R.raw.sh);
        tmap.put("T", R.raw.t);
        tmap.put("TH", R.raw.th);
        tmap.put("UH", R.raw.uh);
        tmap.put("UW", R.raw.uw);
        tmap.put("V", R.raw.v);
        tmap.put("W", R.raw.w);
        tmap.put("Y", R.raw.y);
        tmap.put("Z", R.raw.z);
        tmap.put("ZH", R.raw.zh);

    }
    public int[] getTracks(Word wrd){
        int[] trackArray = new int[wrd.getCode().split("\\s+").length];
        String str = wrd.getCode(); // "HH II LL KO0"
        String[] splitStr = str.split("\\s+"); // {"HH", "II", "LL", "KO0",}

        for ( int i = 0 ; i < splitStr.length ; i++){
            Log.d("SPLITSTR",splitStr[i]);
            trackArray[i] = tmap.get( splitStr[i] );
            Log.d("TRACKARRAY",String.valueOf(trackArray[i]));
        }
        return trackArray;
    }
    /**
     *
     * @param word The word to search for in the sqlite database
     * @return The string arpabet/sampa representation of what was found for the requested query
     */
    public Word getEntry ( String word ) {
        String db_path = DB_FULL_PATH+DATABASE_NAME;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(db_path,null ,SQLiteDatabase.OPEN_READONLY );
        //String sqlQuery = "SELECT "+KEY_CODE+" FROM "+TABLE_DICTS+" WHERE "+KEY_ID+"=\""+word.toUpperCase()+"\"";
        String sqlQuery = "SELECT code from cmudict WHERE _id=\""+word.toUpperCase()+"\"";
        Cursor cursor = db.rawQuery(sqlQuery,null);
        if(cursor.moveToFirst()){
            return new Word(word, cursor.getString(0));
        }
        else{
             return new Word("N/A","N/A") ;
        }
    }


}

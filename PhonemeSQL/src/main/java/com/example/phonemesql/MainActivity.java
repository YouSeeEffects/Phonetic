package com.example.phonemesql;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phonemesql.R.raw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeMap;

public class MainActivity extends Activity implements OnInitListener {

    private DatabaseHandler db;
    private TextToSpeech tts;
    boolean ttsInit = false;
    private MediaPlayer mp;
    private TreeMap<String,Integer> tmap;
    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main);

        final Button mButton = (Button)findViewById( R.id.button);
        final EditText mEdit   = (EditText)findViewById( R.id.editText);
        final TextView mTxt = (TextView)findViewById( R.id.text );
        copyDataBase( this );

        //

        // Load the sound







        // This starts up the TextToSpeech engine
        // The first parameter is the context in which it is operating
        // The second is the class which contains onInit
        this.tts = new TextToSpeech(this, this);
        db = new DatabaseHandler(this);
        mButton.setClickable( true );
        mButton.setOnClickListener(
            new View.OnClickListener(){
                public void onClick(View view){

                    String query = mEdit.getText().toString();
                    Word out = db.getWord( query.toUpperCase());
                    mTxt.setText(out.getCode());

                    String[] tmp = out.getCode().split( "\\s+" );;

                    mp = MediaPlayer.create( MainActivity.this, R.raw.aa );
                    mp.start();

                }
            }
        );
    }

    // When the TTS engine is initialized, this method is called
    public void onInit(int status) {
        this.ttsInit = true;
        Button speak = (Button)findViewById( R.id.button );
        speak.setClickable(true);
    }
    // Called when tts is destroyed
    public void onDestroy() {
        // This shuts down the TTS engine
        tts.shutdown();
    }
    private void fillTreeMap(){
        tmap.put("AA", raw.aa);
        tmap.put("AE", raw.ae);
        tmap.put("AH0", raw.ah0);
        tmap.put("AH1", raw.ah1);
        tmap.put("AE", raw.ae);
        tmap.put("AO1",raw.ao1);
        tmap.put("AW",raw.aw);
        tmap.put("AXR",raw.axr);
        tmap.put("AY",raw.ay);
        tmap.put("B", raw.b);
        tmap.put("CH", raw.ch);
        tmap.put("D", raw.d);
        tmap.put("DH", raw.dh);
        tmap.put("EH", raw.eh);
        tmap.put("EH1",raw.eh1);
        tmap.put("ER",raw.er);
        tmap.put("F",raw.f);
        tmap.put("G", raw.g);
        tmap.put("HH", raw.hh);
        tmap.put("IH", raw.ih);
        tmap.put("IY", raw.iy);
        tmap.put("JH", raw.jh);
        tmap.put("K", raw.k);
        tmap.put("L", raw.l);
        tmap.put("M", raw.m);
        tmap.put("N", raw.n);
        tmap.put("NG", raw.ng);
        tmap.put("OW1", raw.ow1);
        tmap.put("OY", raw.oy);
        tmap.put("P", raw.p);
        tmap.put("R", raw.r);
        tmap.put("S", raw.s);
        tmap.put("SH", raw.sh);
        tmap.put("T", raw.t);
        tmap.put("TH", raw.th);
        tmap.put("UH", raw.uh);
        tmap.put("UW", raw.uw);
        tmap.put("V", raw.v);
        tmap.put("W", raw.w);
        tmap.put("Y", raw.y);
        tmap.put("Z", raw.z);
        tmap.put("ZH", raw.zh);
    }
    private String makeSSML(String cmu){
        String ssml ="<phoneme alphabet=\"x-cmu\" ph=\"";
        String[] tmp = cmu.split( "\\s+" );
        String ssmlend="\"/><break/>";
        String re = new String();
        for ( String txt : tmp ){
            re+=ssml+txt+ssmlend;
        }
        return re;
    }


    /**
     * Copies the contents of Asset cmudict.0.7a.sqlite to /data/data/databases
     * @param myContext Context of caller
     */
    private void copyDataBase(Context myContext){
        String DATABASE_NAME = "cmudict.0.7a.sqlite";
        String DB_FULL_PATH="/data/data/com.example.phonemesql/databases/";
        AssetManager assetManage = myContext.getAssets();
        File file = new File(DB_FULL_PATH); // Declare that there is a folder called databases
        file.mkdir();                       // Make that database on install
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


}

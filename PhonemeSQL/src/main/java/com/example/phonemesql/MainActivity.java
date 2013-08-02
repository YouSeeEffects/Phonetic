package com.example.phonemesql;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phonemesql.R.id;
import com.example.phonemesql.R.layout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity implements OnInitListener {

    private DatabaseHandler db;
    private TextToSpeech tts;
    boolean ttsInit = false;

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( layout.activity_main);

        final Button mButton = (Button)findViewById( R.id.button);
        final EditText mEdit   = (EditText)findViewById( R.id.editText);
        final TextView mTxt = (TextView)findViewById( R.id.text );
        copyDataBase( this );

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
                    Log.d("!!!!!!Database is SQLITEException is thrown","SQLiteException is thrown");
                    tts.speak(makeSSML( out.getCode() ), TextToSpeech.QUEUE_ADD, null) ;

                }
            }
        );
    }

    // When the TTS engine is initialized, this method is called
    public void onInit(int status) {
        this.ttsInit = true;
        Button speak = (Button)findViewById( id.button );
        speak.setClickable(true);
    }
    // Called when tts is destroyed
    public void onDestroy() {
        // This shuts down the TTS engine
        tts.shutdown();
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

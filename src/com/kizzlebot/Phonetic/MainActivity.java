package com.kizzlebot.Phonetic;


import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kizzlebot.Phonetic.R.id;
import com.kizzlebot.Phonetic.R.layout;

public class MainActivity extends Activity implements OnClickListener {
    private DatabaseHandler db;



    public Button mButton ;
    public EditText mEdit ;
    public TextView mTxt ;

    public SoundManager mSoundManager ;

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( layout.activity_main);
        db = new DatabaseHandler(this);

        mButton = (Button)findViewById( id.button);
        mEdit   = (EditText)findViewById( id.editText);
        mTxt = (TextView)findViewById( id.text );


        mSoundManager = new SoundManager();
        mSoundManager.initSounds( this );

        setVolumeControlStream( AudioManager.STREAM_MUSIC );
        mButton.setOnClickListener( this );
    }



    public void onClick ( View v ) {

        String query = mEdit.getText().toString();
        Word out = db.getEntry( query.toUpperCase() ); // getEntry causes query mp3s to be loaded
        mTxt.setText(out.getCode());

        //
        //int[] tracks = db.getTracks( out );
        //mSoundManager.addSound( 1 ,R.raw.eh1);
        //mSoundManager.addSound( 2 ,R.raw.d);
        //mSoundManager.addSound( 3 ,R.raw.ah0);
        //mSoundManager.addSound( 4 ,R.raw.t);
        //
        //
        //
        //mSoundManager.playSound( 1 );
        //mSoundManager.playSound( 2 );
        //mSoundManager.playSound( 3 );
        //mSoundManager.playSound( 4 );
    }


}

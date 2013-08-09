package com.kizzlebot.Phonetic;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kizzlebot.Phonetic.R.id;
import com.kizzlebot.Phonetic.R.layout;
import com.kizzlebot.Phonetic.SoundManager.LocalBinder;

public class MainActivity extends Activity implements OnClickListener {

    private DatabaseHandler db;

    public Button mButton ;
    public EditText mEdit ;
    public TextView mTxt ;
    public boolean mBound=false;
    public SoundManager mSoundManager ;

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( layout.activity_main);
        db = new DatabaseHandler(this);

        mButton = (Button)findViewById( id.button);
        mEdit   = (EditText)findViewById( id.editText);
        mTxt = (TextView)findViewById( id.text );


        mButton.setOnClickListener( this );

    }

    @Override
    protected void onStart () {
        super.onStart();
        Intent intent = new Intent(this,SoundManager.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mSoundManager = binder.getService();
            mBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }};
    public void onClick ( View v ) {
        String query = mEdit.getText().toString();
        Word out = db.getEntry( query.toUpperCase() ); // getEntry causes query mp3s to be loaded
        //int[] r = {R.raw.aa,R.raw.aa1};
        //mSoundManager.setRequests( r );
        mTxt.setText( out.getCode() );
        Log.i( "onClick has been called", "Onclick called, val of mBound="+mBound );
        if( mBound){
            mSoundManager.soundPlay( 1 );
        }
    }
    public void onButtonClick(View v){
        Log.i( "onButtonClick has been called", "onButtonClick called" );
        String query = mEdit.getText().toString();
        Word out = db.getEntry( query.toUpperCase() ); // getEntry causes query mp3s to be loaded
        int[] r = {R.raw.aa,R.raw.aa1};
        mSoundManager.setRequests( r );
        mTxt.setText( out.getCode() );
    }

}

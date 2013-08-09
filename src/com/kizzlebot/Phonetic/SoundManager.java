package com.kizzlebot.Phonetic;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kizzlebot on 8/9/13.
 */
public class SoundManager extends Service{

    private  SoundPool mSoundPool;
    private final IBinder mBinder = new LocalBinder();

//    public  AudioManager mAudioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);;

    private HashMap<Integer,Integer> mSoundPoolMap = new HashMap<Integer, Integer>(  );

    public  static boolean loaded ;

    private int[] resId ;
    @Override
    public void onCreate () {
        super.onCreate();
        loaded = false;
        //addAllSound( resId );

        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        mSoundPoolMap.put(1,mSoundPool.load(this,R.raw.aa,1));
        //mSoundPoolMap.put(2,mSoundPool.load(this,R.raw.ae,1));
        //mSoundPool.setOnLoadCompleteListener( new OnLoadCompleteListener() {
        //    @Override
        //    public void onLoadComplete ( SoundPool soundPool, int sampleId, int status ) {
        //        loaded = true;
        //    }
        //} );
    }
    public void setRequests(int[] tracks){
        this.resId = tracks ;
    }


    public void addAllSound(int[] tracks){
        if ( tracks.length > 1){
            for ( int i = 0; i < tracks.length ;i++){
                Log.i("SoundManager","addsounds() called,added soundID arg i="+i+" tracks[i]="+tracks[i]);
                mSoundPoolMap.put(i+1, mSoundPool.load(this, tracks[i], i+1));
            }
        }else{
            mSoundPoolMap.put( 1,mSoundPool.load( this,tracks[0] ,1) );
        }
    }

    public void playSound(int index) {
        Log.i("SoundManager","play is called(Context thecontexte) called"+mSoundPoolMap.get(index));

        AudioManager mgr = (AudioManager)getSystemService( Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        mSoundPool.play(mSoundPoolMap.get(index), 10, 10, 1, 0, 1.0f);

    }
    public void soundPlay(int index){
        playSound(index);
        Log.d("SOUND1","hi1");
    }

    public class LocalBinder extends Binder {
        SoundManager getService() {
            // Return this instance of SoundManager so clients can call public methods
            return SoundManager.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}

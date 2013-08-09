package com.kizzlebot.Phonetic;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Handler;
import android.util.Log;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by kizzlebot on 8/9/13.
 */
public class SoundManager{


    public SoundPool mSoundPool;
    public HashMap<Integer, Integer> mSoundPoolMap;
    public  AudioManager mAudioManager;
    public  Context mContext;
    public  Vector<Integer> mAvailibleSounds = new Vector<Integer>();
    public  Vector<Integer> mKillSoundQueue = new Vector<Integer>();
    public  Handler mHandler = new Handler();
    public  boolean loaded ;
    public SoundManager(Integer numTracks,Context theContext){
        mContext = theContext;
        loaded = false;
        mSoundPool = new SoundPool(numTracks, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
                loaded = true;
            }
        });
    }
    public SoundManager(Context theContext){
        mContext=theContext;
        loaded = false;
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });
    }
    public void initSounds() {
        Log.i("SoundManager","initSounds(Context thecontexte) called");


        mSoundPoolMap = new HashMap<Integer, Integer>();
        mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public void addSound(int Index, int SoundID){
        Log.i("SoundManager","addsounds() called,added soundID arg "+SoundID);
        mAvailibleSounds.add(Index);
        int soundID = mSoundPool.load(mContext, SoundID, Index);
        Log.i("addSound::soundID=",String.valueOf( soundID ));

        mSoundPoolMap.put(Index, soundID);
    }

    public void playSound(int index) {
        Log.i("SoundManager","play is called(Context thecontexte) called");
        // don't have a sound for this obj, return.
        if(mAvailibleSounds.contains(index)){

            int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            Log.i("StreamVolume","streamValue is ="+String.valueOf( streamVolume));


            // schedule the current sound to stop after set milliseconds
            //mHandler.postDelayed(new Runnable() {
            //    public void run() {
            //        if(!mKillSoundQueue.isEmpty()){
            //            mSoundPool.stop(mKillSoundQueue.firstElement());
            //        }}}, 5000);
        }
        Log.i("The track going into mSoundPool.play(track,rightVol,leftVol,priority,loop,rate) = ","mSounPoolMap.get(index) = "+mSoundPoolMap.get(index));
        Log.i("The real value of the raw asset ogg file is ","R.raw.eh1="+R.raw.eh1+" mSoundPoolMap.get("+index+")="+mSoundPoolMap.get( index ));
        if (loaded){
            int soundId = mSoundPool.play(mSoundPoolMap.get(index), 10, 10, 1, 0, 1f);
            mKillSoundQueue.add(soundId);
        }
    }
}

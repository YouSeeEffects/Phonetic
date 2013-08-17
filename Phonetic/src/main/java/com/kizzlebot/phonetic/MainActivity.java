package com.kizzlebot.phonetic;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.kizzlebot.phonetic.R.style;

import java.util.Locale;


public class MainActivity extends SherlockActivity implements SearchView.OnQueryTextListener{



    private static final String[] COLUMNS = { BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, };
    private ResponseReceiver receiver;
    private PhoneticDatabaseHandler phonemeDB ;
    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        boolean isLight = this.getTheme().hashCode() == style.Theme_Sherlock_Light;

        //Create the search view
        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Search for Phonemes…");
        searchView.setOnQueryTextListener(this);



        menu.add("Search")
            .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.abs__ic_search)
            .setActionView(searchView)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        return true ;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phoneme_search);

        // Initialize database
        phonemeDB = new PhoneticDatabaseHandler(this);

        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public boolean onQueryTextSubmit (String query){
        // Change the TextViews on submission
        Word wrd = phonemeDB.getWord(query.toUpperCase(Locale.US));
        ((TextView)findViewById(R.id.wordTextView)).setText(wrd.getWord());
        ((TextView)findViewById(R.id.phonemeTextView)).setText(wrd.getPhonetic());

        // Make URI of rawID's that are integers
        String[] strID = new String[wrd.getRawID().length];
        int[] rawID = wrd.getRawID();
        for (int i = 0 ; i < wrd.getRawID().length ; i++){
            strID[i] = "android.resource://"+getPackageName()+"/"+String.valueOf( rawID[i] );
        }


        // Play that shit.
        Intent serviceIntent = new Intent(this, MuzakService.class);
        serviceIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        serviceIntent.putExtra("tracks",strID);
        startService( serviceIntent );
        return true;
    }

    @Override
    public boolean onQueryTextChange (String newText){
        return false;
    }

    public class ResponseReceiver extends BroadcastReceiver{
        private String DTAG = "ResponseReceiver";

        public static final String ACTION_RESP = "com.kizzlebot.phonetic.intent.action.MESSAGE_PROCESSED";
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(MuzakService.PARAM_OUT_MSG,0) == 1){
                stopService( intent );
                Log.i("ResponseReceiver", "inside of ResponseReceiver.onReceive, just called stopService");
            }

        }
    }
}
package com.alaskalinuxuser.justchess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class IntroActivity extends AppCompatActivity {

    static ImageView singleButton, doubleButton;
    static Boolean pBlack, pPass;
    static HashMap<String, GameMode> gameModes;
    static String currentGameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First you define it.
                Intent myintent = new Intent(IntroActivity.this, aboutActivity.class);
                //myintent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                // Now you call it.
                startActivity(myintent);
            }
        });

        singleButton = (ImageView) findViewById(R.id.playSingleButton);
        doubleButton = (ImageView) findViewById(R.id.playDoubleButton);
        pBlack = false;
        pPass = false;
        MainActivity.engineStrength=3;

        Spinner gameModeSpinner = (Spinner) findViewById(R.id.chooseGameMode);
        gameModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
                currentGameMode = parent.getItemAtPosition(pos).toString();
                setSelectedGameMode(currentGameMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        gameModes = new HashMap<>();
        addGameMode("Classic",
                    "Typical Chess",
                    "RNBQKBNRPPPPPPPP********************************pppppppprnbqkbnr");

        GetGameModesTask task = new GetGameModesTask(this);
        task.execute();
        try {
            // Waits for game modes to be loaded
            task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    } // End on create.

    protected void addGameMode(String name, String description, String newBoard) {
        try {
            gameModes.put(name, new GameMode(name, description, newBoard));

            ArrayList<String> gameModeNames = new ArrayList<>(gameModes.keySet());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, gameModeNames);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            Spinner gameModeSpinner = (Spinner) findViewById(R.id.chooseGameMode);
            gameModeSpinner.setAdapter(dataAdapter);

            if ( currentGameMode == null ) {
                currentGameMode = name;
            }

            setSelectedGameMode(currentGameMode);

        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    protected void setSelectedGameMode(String gameModeName) {
        //Update spinner
        Spinner gameModeSpinner = (Spinner) findViewById(R.id.chooseGameMode);
        gameModeSpinner.setSelection(getSpinnerIndex(gameModeSpinner, gameModeName));

        //Update description
        TextView gameModeDescriptionView = (TextView)findViewById(R.id.gameModeDescription);
        GameMode onlyGameMode = gameModes.get(gameModeName);
        gameModeDescriptionView.setText(onlyGameMode.getDescription());
    }

    //Copied from Akhil Jain's answer at https://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position
    private int getSpinnerIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // First you define it.
            Intent myintent = new Intent(IntroActivity.this, SettingsActivity.class);
            // Now you call it.
            startActivity(myintent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void playSingle (View singleButton) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Black or White?")
                .setMessage(
                        "Please choose to play as black or white.")
                .setPositiveButton("Black", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Testing only //
                        Log.i("WJH", "Clicked Black.");

                        //what to do.
                        pBlack = true;
                        pPass = false;

                        // First you define it.
                        Intent myintent = new Intent(IntroActivity.this, MainActivity.class);
                        myintent.putExtra("START_BOARD", gameModes.get(currentGameMode).getNewBoard() );
                        // Now you call it.
                        startActivity(myintent);

                    }
                })
                .setNegativeButton("White", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Log.i("WJH", "Clicked White.");

                        pBlack = false;
                        pPass = false;

                        // First you define it.
                        Intent myintent = new Intent(IntroActivity.this, MainActivity.class);
                        myintent.putExtra("START_BOARD", gameModes.get(currentGameMode).getNewBoard() );
                        // Now you call it.
                        startActivity(myintent);

                    }
                })
                .show(); // Make sure you show your popup or it wont work very well!
    } // End playSingle

    public void playDouble (View doubleButton) {

        pBlack = false;
        pPass = true;
        // First you define it.
        Intent myintent = new Intent(IntroActivity.this, MainActivity.class);
        myintent.putExtra("START_BOARD", gameModes.get(currentGameMode).getNewBoard() );
        // Now you call it.
        startActivity(myintent);
    } // End playDouble

    public void previewGameMode(View view) {
        Intent myintent = new Intent(IntroActivity.this, PreviewActivity.class);
        myintent.putExtra("START_BOARD", gameModes.get(currentGameMode).getNewBoard() );
        startActivity(myintent);
    }
}

/**
 * Downloads JSON, parses each squirrel, and adds it to the collection via a task.
 */
class GetGameModesTask extends AsyncTask<String, Void, Boolean> {
    protected IntroActivity mIntroActivity;

    public GetGameModesTask(IntroActivity introActivity) {
        mIntroActivity = introActivity;
    }

    /**
     * Downloads list of squirrels encoded using JSON from url
     * and adds them to a SquirrelList
     * @param strings
     * @return SquirrelList
     */
    @Override
    protected Boolean doInBackground(String... strings) {
        // Code adapted from http://chillyfacts.com/java-send-url-http-request-read-json-response/
        try {
            String url = "https://gist.githubusercontent.com/dparker1005/6e2b7ce7c71ad711c9debc064438402d/raw/8efa20b4eb4095e1a3bdbfbaf918f6f1ad845551/just_chess_game_modes.json";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            InputStream is = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in = new BufferedReader(isr);
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray arr = new JSONArray(response.toString());
            for(int i = 0; i < arr.length(); i++) {
                String name = arr.getJSONObject(i).getString("name");
                String description = arr.getJSONObject(i).getString("description");
                String newBoard = arr.getJSONObject(i).getString("newBoard");
                mIntroActivity.addGameMode( name, description, newBoard );
            }
        } catch(Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
}

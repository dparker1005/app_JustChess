package com.alaskalinuxuser.justchess;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class IntroActivity extends AppCompatActivity {

    static ImageView singleButton, doubleButton;
    static Boolean pBlack, pPass;
    HashMap<String, GameMode> gameModes;
    String currentGameMode;

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

        addGameMode("Pawns Galore",
                "All non-king pieces are replaced with pawns",
                "PPPPKPPPPPPPPPPP********************************ppppppppppppkppp");

        addGameMode("Spy",
                "All non-king pieces are replaced with pawns, with a spy on the other side!",
                "PPPPKPPPPPPPpPPP********************************ppPpppppppppkppp");

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

        } catch (RuntimeException e) {
            System.out.println("Invalid board: " + name);
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
        // Now you call it.
        startActivity(myintent);
    } // End playDouble

}

package com.alaskalinuxuser.justchess;

/*  Copyright 2017 by AlaskaLinuxUser (https://thealaskalinuxuser.wordpress.com)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import static com.alaskalinuxuser.justchess.MainActivity.chessImage;

import java.util.Arrays;

import static com.alaskalinuxuser.justchess.IntroActivity.pBlack;
import static com.alaskalinuxuser.justchess.IntroActivity.pPass;
import static com.alaskalinuxuser.justchess.TheEngine.getPromoteToB;
import static com.alaskalinuxuser.justchess.TheEngine.promoteToW;
import static com.alaskalinuxuser.justchess.TheEngine.terminal;
import static com.alaskalinuxuser.justchess.TheEngine.theBoard;
import static com.alaskalinuxuser.justchess.TheUserInterface.drawBoardPieces;

public class PreviewActivity extends AppCompatActivity {


    static ImageView x63,x62,x61,x60,x59,x58,x57,x56,x55,x54,x53,x52,x51,x50,x49,x48,x47,x46,x45,
            x44,x43,x42,x41,x40,x39,x38,x37,x36,x35,x34,x33,x32,x31,x30,x29,x28,x27,x26,x25,x24,
            x23,x22,x21,x20,x19,x18,x17,x16,x15,x14,x13,x12,x11,x10,x9,x8,x7,x6,x5,x4,x3,x2,x1,x0;

    static int[] imageViews = {R.id.p0,R.id.p1,R.id.p2,R.id.p3,R.id.p4,R.id.p5,R.id.p6,R.id.p7,R.id.p8,R.id.p9,
            R.id.p10,R.id.p11,R.id.p12,R.id.p13,R.id.p14,R.id.p15,R.id.p16,R.id.p17,R.id.p18,R.id.p19,
            R.id.p20,R.id.p21,R.id.p22,R.id.p23,R.id.p24,R.id.p25,R.id.p26,R.id.p27,R.id.p28,R.id.p29,
            R.id.p30,R.id.p31,R.id.p32,R.id.p33,R.id.p34,R.id.p35,R.id.p36,R.id.p37,R.id.p38,R.id.p39,
            R.id.p40,R.id.p41,R.id.p42,R.id.p43,R.id.p44,R.id.p45,R.id.p46,R.id.p47,R.id.p48,R.id.p49,
            R.id.p50,R.id.p51,R.id.p52,R.id.p53,R.id.p54,R.id.p55,R.id.p56,R.id.p57,R.id.p58,R.id.p59,
            R.id.p60,R.id.p61,R.id.p62,R.id.p63};

    static int engineStrength;
    boolean wTurn, firstClick;
    String tryMove;

    static String moveOptions;
    static int firstNum;

    @Override
    public void onResume(){
        super.onResume();
        Log.i("WJH", "Resumed.");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chessImage = new ImageView[]{x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,
                x16,x17,x18,x19,x20,x21,x22,x23,x24,x25,x26,x27,x28,x29,x30,x31,
                x32,x33,x34,x35,x36,x37,x38,x39,x40,x41,x42,x43,x44,x45,x46,x47,
                x48,x49,x50,x51,x52,x53,x54,x55,x56,x57,x58,x59,x60,x61,x62,x63};

        // Declare all of our image views programatically.
        for (int i=0; i<64; i++) {
            chessImage[i]=(ImageView)findViewById(imageViews[i]);
        } // checker board.

        //Get board start state
        String newBoardState = "RNBQKBNRPPPPPPPP********************************pppppppprnbqkbnr";
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                newBoardState= extras.getString("START_BOARD");
            }
        } else {
            newBoardState = (String) savedInstanceState.getSerializable("START_BOARD");
        }

        //Start a new game.
        terminal("newGame." + newBoardState );

        // Visually Draw the board....
        drawBoardPieces();

    }// End on create.

    public void endPreview(View view) {
        Intent myintent = new Intent(PreviewActivity.this, IntroActivity.class);
        startActivity(myintent);
    }

} // End main.
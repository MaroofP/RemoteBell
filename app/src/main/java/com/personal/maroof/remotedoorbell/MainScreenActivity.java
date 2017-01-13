package com.personal.maroof.remotedoorbell;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainScreenActivity extends AppCompatActivity {


    private View mContentView;
    private View mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main_screen);
        mContentView = findViewById(R.id.show_ui);
        mContent = findViewById(R.id.ui);
        mContent.setVisibility(View.GONE);
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContent.setVisibility(View.VISIBLE);
                mContentView.setVisibility(View.GONE);

            }
        });


        findViewById(R.id.dummy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transfer();
            }
        });

        findViewById(R.id.aboutButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAbout();
            }
        });


    }


    protected  void showAbout(){

        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);

    }


    protected  void transfer(){

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }

    protected void exit(View view){
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

    }





}

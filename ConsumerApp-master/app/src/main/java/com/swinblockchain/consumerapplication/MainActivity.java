package com.swinblockchain.consumerapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
/*
  Swinburne Capstone Project - ICT90004
  Aidan Beale & John Humphrys
  https://github.com/SwinburneBlockchain
*/
  
/**
 * The main activity is displayed to the user when the application starts up
 *
 * @author John Humphrys
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Starts the ScanActivity ativity
     *
     * @param view
     */
    public void scanProduct(View view) {
        Intent i = new Intent(MainActivity.this, ScanActivity.class);
        startActivity(i);
    }

    /**
     * Starts the AcknowledgementsActivity activity
     *
     * @param view
     */
    public void displayAck(View view) {
        Intent i = new Intent(MainActivity.this, AcknowledgementsActivity.class);
        startActivity(i);
    }

    /**
     * On back pressed does nothing to prevent unexpected results
     */
    @Override
    public void onBackPressed() {
        // Do nothing
    }
}

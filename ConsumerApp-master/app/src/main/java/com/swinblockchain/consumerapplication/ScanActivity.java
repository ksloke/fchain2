package com.swinblockchain.consumerapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static android.R.attr.type;
/*
  Swinburne Capstone Project - ICT90004
  Aidan Beale & John Humphrys
  https://github.com/SwinburneBlockchain
*/
  
/**
 * Scan activity is used to scan a qr code and get a response
 */
public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        scan();
    }

    /**
     * Called when the scan activity finishes
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        JsonObject returnedJsonObject = null;
        try {
            if (scanningResult.getContents() == null) {
                onBackPressed();
            } else {
                returnedJsonObject = stringToJsonObject(scanningResult.getContents().toString());
                String accAddr = returnedJsonObject.getString("accAddr", "accAddrError");
                String pubKey = returnedJsonObject.getString("pubKey", "pubKeyError");
                String privKey = returnedJsonObject.getString("privKey", "privKeyError");

                changeActivity(new Scan(accAddr, pubKey, privKey));
            }
        } catch (Exception e) {
            e.printStackTrace();
            startError("The scanned QR code is not valid.\nError Code: Cannot convert JSON to required objects");
        }
    }

    /**
     * Converts a string to json object
     *
     * @param stringToJson the string to convert
     * @return The jsonobject
     */
    private JsonObject stringToJsonObject(String stringToJson) {
        JsonValue jsonResponse;

        try {
            // Parses the string response into a JsonValue
            jsonResponse = Json.parse(stringToJson);
            // Converts the JsonValue into an Object
            JsonObject objectResponse = jsonResponse.asObject();
            // Returns JsonObject
            return objectResponse;
        } catch (Exception e) {
            e.printStackTrace();
            startError("The scanned QR code is not valid.\nError Code: Cannot convert QR code to JSON object");
        }
        return null;
    }

    /**
     * displays a message to the user and sends them back to the main menu
     *
     * @param errorMessage The error message to display
     */
    private void startError(String errorMessage) {
        Intent i = new Intent(ScanActivity.this, MainActivity.class);
        i.putExtra("errorMessage", errorMessage);
        startActivity(i);
    }

    /**
     * Scans a QR code
     */
    private void scan() {
        IntentIntegrator scan = new IntentIntegrator(ScanActivity.this);

        // Display message is Scanner application is not installed on the device
        scan.setMessage("Scanner needs to be downloaded in order to use this application.");
        scan.initiateScan();
    }

    /**
     * Used to change activities
     *
     * @param s The scan to pass to the next activity
     */
    private void changeActivity(Scan s) {
        Intent i = new Intent(ScanActivity.this, QueryServerActivity.class);
        i.putExtra("scan", s);

        startActivity(i);
    }

}

package com.swinblockchain.consumerapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/*
  Swinburne Capstone Project - ICT90004
  Aidan Beale & John Humphrys
  https://github.com/SwinburneBlockchain
*/
  
/**
 * Class is used to query the caching server and return the response.
 * @author John Humphrys
 */
public class QueryServerActivity extends AppCompatActivity {

    Scan s;
    ArrayList<Producer> prodArrayList = new ArrayList<>();

    private final String CACHING_SERVER = "http://ec2-54-153-202-123.ap-southeast-2.compute.amazonaws.com:3000/";
    private final String VALIDATE_ACCOUNT = "NXT-HP3G-T95S-6W2D-AEPHE";
    private final String VALID_MESSAGE = "VALIDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_blockchain);

        init();
    }

    /**
     * Get intent and start request
     */
    private void init() {
        s = getIntent().getParcelableExtra("scan");
        makeRequest();
    }


    /**
     * Create request to send to the QR code generating web server
     */
    private void makeRequest() {
        RequestQueue queue = Volley.newRequestQueue(QueryServerActivity.this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CACHING_SERVER + "productInfo/" + s.getAccAddr(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JsonObject jsonProduct = null;
                ArrayList<JsonObject> jsonProducer = null;

                // If response valid get data
                if (checkValid(response)) {

                    try {
                        jsonProduct = createJsonProduct(response);
                        jsonProducer = createJsonProducer(response);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                        startError("The product contains empty or invalid data.\nError Code: Could not create Json objects due to invalid data. " + e.toString());
                    }

                    try {
                        // May not be required
                        String qrAddress = jsonProduct.getString("qrAddress", "qrAddressError");
                        String qrPubKey = jsonProduct.getString("qrPubKey", "qrPubKeyError");
                        String producerAddr = jsonProduct.getString("producerAddr", "producerAddrError");

                        // Single product information
                        String productName = jsonProduct.getString("productName", "productNameError");
                        String productId = jsonProduct.getString("productId", "productIdError");
                        String batchId = jsonProduct.getString("batchId", "batchIdError");

                        Product newProduct = new Product(productName, productId, batchId);

                        for (JsonObject p : jsonProducer) {
                            // Each producer information
                            String producerName = p.getString("producerName", "producerNameError");
                            double producerTimestamp = p.getDouble("timestamp", 0);
                            String producerLocation = p.getString("producerLocation", "producerLocationError");

                            prodArrayList.add(new Producer(producerName, producerTimestamp, producerLocation));
                        }
                        displayProductInformation(newProduct, prodArrayList);


                    } catch (Exception e) {
                        e.printStackTrace();
                        startError("The returned data from the server is invalid.\nError Code: Data returned from server is in an unexpected form. " + e.toString());
                    }
                } else {
                    startError("The scanned product has not been verified by the blockchain.\nError Code: Product not verified in blockchain");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print(error.toString());
                startError("Error querying server\nError Code: " + error.toString());
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //params.put("accAddr", s.getAccAddr());
                System.out.println("Parameters: " + params);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    /**
     * Used to create json product from string
     *
     * @param response The string to convert
     * @return The jsonObject
     */
    private JsonObject createJsonProduct(String response) {
        String[] cleanedResponseArr = response.split("\\|");
        return stringToJsonObject(cleanedResponseArr[0]);
    }

    /**
     * Used to create a json producer from string
     *
     * @param response The string to convert
     * @return The jsonObject
     */
    private ArrayList<JsonObject> createJsonProducer(String response) {
        String[] cleanedResponseArr = response.split("\\|");
        ArrayList<JsonObject> jsonProducers = new ArrayList<>();

        for (int i = 1; i < cleanedResponseArr.length; i++) {
            jsonProducers.add(stringToJsonObject(cleanedResponseArr[i]));
        }
        return jsonProducers;
    }

    /**
     * Checks to see if the response is valid
     *
     * @param response The recieved response is valid
     * @return boolean based on result
     */
    private boolean checkValid(String response) {
        String[] cleanedResponseArr = response.split("\\|");
        JsonObject json = stringToJsonObject(cleanedResponseArr[1]);

        try {
            String blockchainAcc = json.getString("actionAddress", "actionAddressError");
            String validMessage = json.getString("action", "actionError");

            if (blockchainAcc.equals(VALIDATE_ACCOUNT) && validMessage.equals(VALID_MESSAGE)) {
                return true;
            } else {
                startError("The product/producer information is not valid.\nError Code: Blockchain account address incorrect or invalidated.");
                return false;
            }
        } catch (Exception e) {
            startError("The product/producer information is not valid.\nError Code: Blockchain account address incorrect or invalidated.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Moves to the display information activity screen
     *
     * @param newProduct The product to display
     * @param producerList An arraylist of the producers who made a transaction with the product
     */
    private void displayProductInformation(Product newProduct, ArrayList<Producer> producerList) {
        Intent i = new Intent(QueryServerActivity.this, InformationActivity.class);
        i.putExtra("product", newProduct);
        i.putParcelableArrayListExtra("prodArrayList", producerList);
        startActivity(i);
    }

    /**
     * Displays an error message and sends the user back to the main menu
     *
     * @param errorMessage The message to display
     */
    private void startError(String errorMessage) {
        Intent i = new Intent(QueryServerActivity.this, MainActivity.class);
        i.putExtra("errorMessage", errorMessage);
        startActivity(i);
    }

    /**
     * Used to convery a string into a json object
     *
     * @param stringToJson The string to convert
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
     * On back pressed sends the user to the main activity to prevent unexpected results
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(QueryServerActivity.this, MainActivity.class);
        startActivity(i);
    }

}

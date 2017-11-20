package com.swinblockchain.consumerapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
/*
  Swinburne Capstone Project - ICT90004
  Aidan Beale & John Humphrys
  https://github.com/SwinburneBlockchain
*/
  
/**
 * InformationActivity displays all information recieved from the caching server about a product
 *
 * @author John Humphrys
 */
public class InformationActivity extends AppCompatActivity {

    /**
     * Run when activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        init();
    }

    /**
     * Initalise variables
     */
    private void init() {
        Product product = getIntent().getParcelableExtra("product");
        ArrayList<Producer> prodArrayList = (ArrayList<Producer>) getIntent().getSerializableExtra("prodArrayList");

        // Display product information
        displayProductInformation(product, prodArrayList);
    }

    /**
     * Displays all the product information
     *
     * @param product       The product information to display
     * @param prodArrayList A collection of all producers who moved the product
     */
    private void displayProductInformation(Product product, ArrayList<Producer> prodArrayList) {
        // Draw the top, main window
        TextView productName = (TextView) findViewById(R.id.productName);
        TextView batchID = (TextView) findViewById(R.id.batchID);

        productName.setText("Product name: " + product.getProductName());
        batchID.setText("Batch ID: " + product.getBatchId());

        // Draw a new column for each location
        for (Producer p : prodArrayList) {
            createTableRow(p);
            // Clean up bottom of table
            createTableRowFinal();
        }
    }

    /**
     * Adds a gap between each producer
     */
    private void createTableRowFinal() {
        final TableLayout detailsTable = (TableLayout) findViewById(R.id.mainTableLayout);
        final TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerowbottom, null);
        detailsTable.addView(tableRow);
    }

    /**
     * Creates a table row for each producer
     *
     * @param p The producer to make the row for
     */
    private void createTableRow(Producer p) {
        final TableLayout detailsTable = (TableLayout) findViewById(R.id.mainTableLayout);
        final TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.tablerow, null);
        TextView tv;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        tv = (TextView) tableRow.findViewById(R.id.informationCell);
        tv.setText("Date: " + sdf.format(p.getProducerTimestamp()) + "\n" + "Registered by: " + p.getProducerName() + "\n" + "Location: " + p.getProducerLocation());

        //Add row to the table
        detailsTable.addView(tableRow);
    }


    /**
     * On back pressed sends the user to the main activity to prevent unexpected results
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(InformationActivity.this, MainActivity.class);
        startActivity(i);
    }
}

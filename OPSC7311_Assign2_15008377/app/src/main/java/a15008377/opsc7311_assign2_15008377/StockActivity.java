/**
 * Author: Matthew Syrén
 *
 * Date:   19 May 2017
 *
 * Description: Class allows you to add or update Stock information
 */

package a15008377.opsc7311_assign2_15008377;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class StockActivity extends AppCompatActivity {
    String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_stock);

            Bundle bundle = getIntent().getExtras();
            action = bundle.getString("action");
            if(action.equals("update")){
                EditText txtStockID = (EditText) findViewById(R.id.text_stock_id);
                txtStockID.setEnabled(false);
                Button button = (Button) findViewById(R.id.button_add_stock);
                button.setText("Update Stock");
                Stock stock = (Stock) bundle.getSerializable("stockObject");
                displayData(stock);
            }
            else if(action.equals("add")){
                Button button = (Button) findViewById(R.id.button_add_stock);
                button.setText("Add Stock");
            }
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method pre-populates the TextViews on this Activity with the data from the Stock item that was clicked on in the previous Activity and sent through the bundle
    public void displayData(Stock stock){
        try{
            EditText txtStockID = (EditText) findViewById(R.id.text_stock_id);
            EditText txtStockDescription = (EditText) findViewById(R.id.text_stock_description);
            EditText txtStockQuantity = (EditText) findViewById(R.id.text_stock_quantity);

            txtStockID.setText(stock.getStockID());
            txtStockDescription.setText(stock.getStockDescription());
            txtStockQuantity.setText(stock.getStockQuantity() + "");
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void addStockOnClick(View view) {
        try{
            EditText txtStockID = (EditText) findViewById(R.id.text_stock_id);
            EditText txtStockDescription = (EditText) findViewById(R.id.text_stock_description);
            EditText txtStockQuantity = (EditText) findViewById(R.id.text_stock_quantity);
            Intent intent = null;

            String stockID = txtStockID.getText().toString();
            String stockDescription = txtStockDescription.getText().toString();
            int stockQuantity = Integer.parseInt(txtStockQuantity.getText().toString());

            Stock stock = new Stock(stockID, stockDescription, stockQuantity);
            if(stock.validateStock(this)){
                if(action.equals("add") && !stock.checkStockID(this)){
                    writeToFile(stock.getStockID(), stock.getStockDescription(), stock.getStockQuantity());
                    Toast.makeText(this, "Stock item added successfully", Toast.LENGTH_LONG).show();
                    intent = getIntent();
                    finish();
                }
                else if(action.equals("update")){
                    //Fetches all Stock items from the Stock.txt text file
                    ArrayList<Stock> lstStock = Stock.readStockItems(this);
                    boolean stockUpdated = false;

                    //Loops through the Stock items and updates the appropriate Stock item
                    for(int i = 0; i < lstStock.size() && !stockUpdated; i++){
                        if(lstStock.get(i).getStockID().equals(stock.getStockID())){
                            lstStock.set(i, stock);
                            stockUpdated = true;
                        }
                    }

                    stock.rewriteFile(lstStock, getApplicationContext());
                    Toast.makeText(this, "Stock item updated successfully", Toast.LENGTH_LONG).show();
                    intent = new Intent(StockActivity.this, StockControlActivity.class);
                }
                startActivity(intent);
            }

        }
        catch(NumberFormatException nfe){
            Toast.makeText(getApplicationContext(), "Please enter a whole number for the Stock Quantity", Toast.LENGTH_LONG).show();
        }
        catch(IOException ioe){
            Toast.makeText(getApplicationContext(), ioe.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //Method writes the stock information to the Stock.txt file
    public void writeToFile(String stockID, String stockDescription, int stockQuantity){
        try{
            File file = new File(getFilesDir(), "Stock.txt");
            FileOutputStream fileOutputStream = openFileOutput(file.getName(), MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(stockID + "|" + stockDescription + "|" + stockQuantity + "\n");
            outputStreamWriter.close();
        }
        catch(IOException ioe){
            Toast.makeText(getApplicationContext(), ioe.getMessage(), Toast.LENGTH_LONG).show();
        }
        catch(Exception exc){
            Toast.makeText(getApplicationContext(), exc.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}